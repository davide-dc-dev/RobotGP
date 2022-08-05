package robogp.matchmanager;

import Partita.RobotMarker;
import Utility.Istruzione;
import Main.MatchManagerApp;
import Partita.PoolGlobale;
import Partita.RegistroPartita;
import connection.Connection;
import connection.Message;
import connection.MessageObserver;
import connection.PartnerShutDownException;
import java.awt.LayoutManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.SingleServerConfig;
import Partita.PoolGlobale.PoolGiocatore;
import Main.MatchManagerApp;
import robogp.robodrome.Robodrome;
import robogp.robodrome.view.RobodromeView;

/**
 *
 * @author claudia
 */
public class Match implements MessageObserver,Serializable {

    public static final int ROBOTSINGAME = 8;
    public static final String MatchJoinRequestMsg = "joinMatchRequest";
    public static final String MatchJoinReplyMsg = "joinMatchReply";
    public static final String MatchStartMsg = "startMatch";
    public static final String MatchCancelMsg = "cancelMatch";
    public static final String MatchPoolGenerationMsg="generatePool";
    public static final String MatchProgramMsg="matchProgram";
    public static final String MatchRobotMove="robotMove";
    public static final String MatchRobotPlay="robotPlay";
    public static final String MatchBelt="matchBelt";
    public static final String MatchRegeneratePool="regeneratePool";
    public static final String MatchRefreshList="refreshList";
    private transient Semaphore semp;

    public enum EndGame {
        First, First3, AllButLast
    };

    public enum State {
        Created, Started, Canceled
    };

    private static final String[] ROBOT_COLORS = {"blue", "red", "yellow", "emerald", "violet", "orange", "turquoise", "green"};
    private static final String[] ROBOT_NAMES = {"robot-blue", "robot-red", "robot-yellow", "robot-emerald", "robot-violet", "robot-orange", "robot-turquoise", "robot-green"};
    private final Robodrome theRobodrome;
    private final RobotMarker[] robots;
    private final EndGame endGameCondition;
    private final int nMaxPlayers;
    private final int nRobotsXPlayer;
    private int priorita;
    private Istruzione istr;
    private final boolean initUpgrades;
    private ArrayList<RobotMarker> listRobot;
    private String  rbdFileName;
    private ArrayList<String> playerReady;
    
   
    private State status;

    private final HashMap<String, Connection> waiting;
    private final HashMap<String, Connection> players;
    
    private Map <String,RobodromeView> playersRobodrome;
    private Map<Integer,String> listPriorita;
    private ArrayList<Integer> prioritaSchede;
    private Map <String,RegistroPartita[]> registriPlayers;
    

    /* Gestione pattern singleton */
    private static Match singleInstance;

    private Match(String rbdName, int nMaxPlayers, int nRobotsXPlayer, EndGame endGameCond, boolean initUpg) {
        this.nMaxPlayers = nMaxPlayers;
        this.nRobotsXPlayer = nRobotsXPlayer;
        this.endGameCondition = endGameCond;
        this.initUpgrades = initUpg;
        rbdFileName = "robodromes/" + rbdName + ".txt";
        this.robots = new RobotMarker[Match.ROBOT_NAMES.length];
        this.theRobodrome = new Robodrome(rbdFileName);
        
        for (int i = 0; i < Match.ROBOT_NAMES.length; i++) {
            this.robots[i] = new RobotMarker(Match.ROBOT_NAMES[i], Match.ROBOT_COLORS[i]);
        }
        waiting = new HashMap<>();
        players = new HashMap<>();
        listRobot=new ArrayList<>();
        registriPlayers=new HashMap<>();
        playerReady=new ArrayList<>();
        playersRobodrome=new HashMap<>();
        listPriorita=new HashMap<>();
        prioritaSchede=new ArrayList<>();
        this.status = State.Created;
    }

    public static Match getInstance(String rbdName, int nMaxPlayers,
            int nRobotsXPlayer, EndGame endGameCond, boolean initUpg) {
        if (Match.singleInstance == null || Match.singleInstance.status == Match.State.Canceled) {
            Match.singleInstance = new Match(rbdName, nMaxPlayers, nRobotsXPlayer, endGameCond, initUpg);
        }
        return Match.singleInstance;
    }

    public static Match getInstance() {
        if (Match.singleInstance == null || Match.singleInstance.status == Match.State.Canceled) {
            return null;
        }
        return Match.singleInstance;
    }

    @Override
    public  void notifyMessageReceived(Message msg) {
        if (msg.getName().equals(Match.MatchJoinRequestMsg)) {
            String nickName = (String) msg.getParameter(0);
            this.waiting.put(nickName, msg.getSenderConnection());
            MatchManagerApp.getAppInstance().getIniziarePartitaController().matchJoinRequestArrived(msg);
        }else if(msg.getName().equals(Match.MatchPoolGenerationMsg)) {
            
            synchronized(this) {
                String owner=(String)msg.getParameter(0);
                int puntiSalute=(int)msg.getParameter(1);
            
            
                PoolGlobale poolGlobale=PoolGlobale.getInstance();
                PoolGiocatore poolGiocatore=poolGlobale.generaPoolGiocatore(puntiSalute);
                Message reply = new Message(Match.MatchPoolGenerationMsg);

                Object[] parameters = new Object[1];
                parameters[0] = poolGiocatore.getPoolPersonale();
                reply.setParameters(parameters);

                try {
                    players.get(owner).sendMessage(reply);
                } catch (PartnerShutDownException ex) {
                    Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
        }else if (msg.getName().equals(Match.MatchProgramMsg)) {
            
            
            synchronized (this) {
                
                String owner=(String)msg.getParameter(0);
                RegistroPartita[] registriPartita=(RegistroPartita[])msg.getParameter(1);
                listRobot=(ArrayList<RobotMarker>)msg.getParameter(2);
            
            
                
                for(int i=0;i<5;i++) {
                    System.err.println(" DEBUG : Registro "+i+1+" "+registriPartita[i].getIstruzione());
                }
                
                registriPlayers.put(owner, registriPartita);
                
                
                if(playerReady.isEmpty()) {
                    playerReady.add(owner);
                }else if(playerReady.contains(owner)){
                    
                }else {
                    playerReady.add(owner);
                }
                
            }
            
            
            if(playerReady.size()==players.size()) {
                Message send = new Message(Match.MatchRobotPlay);
                Object[] parameters = new Object[3];
                parameters[0] = registriPlayers;
                parameters[1] = listPriorita;
                parameters[2] = prioritaSchede;
                send.setParameters(parameters);
                for(String key: players.keySet()) {
                    try {
                        players.get(key).sendMessage(send);
                    } catch (PartnerShutDownException ex) {
                        Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                playerReady.clear();
                registriPlayers.clear();
                listRobot.clear();
                
                
            }
        }else if(msg.getName().equals(Match.MatchRegeneratePool)) {
            
            synchronized(this) {
                registriPlayers=new HashMap<>();
                
                String owner=(String)msg.getParameter(0);
                int puntiSalute=(int)msg.getParameter(1);
                int dimPoolEsistente=(int)msg.getParameter(2);

                PoolGlobale poolGlobale=PoolGlobale.getInstance();
                PoolGiocatore poolGiocatore=poolGlobale.generaPoolGiocatore(puntiSalute,dimPoolEsistente);
                Message reply = new Message(Match.MatchRegeneratePool);
                Object[] parameters = new Object[1];
                parameters[0] = poolGiocatore.getPoolPersonale();
                reply.setParameters(parameters);
                
                
                try {
                    players.get(owner).sendMessage(reply);
                } catch (PartnerShutDownException ex) {
                    Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            
        }else if (msg.getName().equals(Match.MatchRefreshList)) {
            
            
            synchronized(this) {
                
                String owner=(String)msg.getParameter(0);
                players.remove(owner);
                
                
            }
            
        }
    
    }  
    

    public State getStatus() {
        return this.status;
    }

    public void cancel() {
        this.status = State.Canceled;

        Message msg = new Message(Match.MatchCancelMsg);
        for (String nickname : waiting.keySet()) {
            this.refusePlayer(nickname);
        }

        players.values().stream().forEach((conn) -> {
            try {
                conn.sendMessage(msg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void start() {
        this.status = State.Started;

        Message msg = new Message(Match.MatchStartMsg);
        Object[] parameters = new Object[2];
        parameters[0] = rbdFileName;
        parameters[1] = listRobot;
        
        msg.setParameters(parameters);
        

        players.values().stream().forEach((conn) -> {
            try {
                conn.sendMessage(msg);
            } catch (PartnerShutDownException ex) {
                Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public void stop() {
        // PROBABILMENTE NON IMPLEMENTATO NEL CORSO DI QUESTO PROGETTO
    }

    public ArrayList<RobotMarker> getAvailableRobots() {
        ArrayList<RobotMarker> ret = new ArrayList<>();
        for (RobotMarker m : this.robots) {
            if (!m.isAssigned()) {
                ret.add(m);
            }
        }
        return ret;
    }

    public ArrayList<RobotMarker> getAllRobots() {
        ArrayList<RobotMarker> ret = new ArrayList<>();
        for (RobotMarker m : this.robots) {
            ret.add(m);
        }
        return ret;
    }

    public int getRobotsPerPlayer() {
        return this.nRobotsXPlayer;
    }

    public void refusePlayer(String nickname) {
        try {

            Connection conn = this.waiting.get(nickname);

            Message reply = new Message(Match.MatchJoinReplyMsg);
            Object[] parameters = new Object[1];
            parameters[0] = new Boolean(false);
            reply.setParameters(parameters);

            conn.sendMessage(reply);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            waiting.remove(nickname);
        }
    }

    public boolean addPlayer(String nickname, List<RobotMarker> selection) {
        boolean added = false;
        try {
            for (RobotMarker rob : selection) {
                int dock = this.getFreeDock();
                rob.assign(nickname, dock);
                listRobot.add(rob);
            }

            Connection conn = this.waiting.get(nickname);

            Message reply = new Message(Match.MatchJoinReplyMsg);
            Object[] parameters = new Object[2];
            parameters[0] = new Boolean(true);
            parameters[1] = selection.toArray(new RobotMarker[selection.size()]);
            reply.setParameters(parameters);

            conn.sendMessage(reply);
            this.players.put(nickname, conn);
            added = true;
            
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            waiting.remove(nickname);
        }
        return added;

    }

    private int getFreeDock() {
        boolean[] docks = new boolean[this.theRobodrome.getDocksCount()];
        for (RobotMarker rob : this.robots) {
            if (rob.isAssigned()) {
                docks[rob.getDock() - 1] = true;
            }
        }
        int count = 0;
        while (docks[count]) {
            count++;
        }
        if (count < docks.length) {
            return count + 1;
        }
        return -1;
    }

    public int getPlayerCount() {
        return this.players.size();
    }

    public int getMaxPlayers() {
        return this.nMaxPlayers;
    }
    
    
    
   
}
