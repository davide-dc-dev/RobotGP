/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robogp.robodrome.view;


import connection.Connection;
import connection.Message;
import connection.PartnerShutDownException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.text.StyleConstants;



import org.redisson.api.RLock;
import static Allenamento.AllenamentoApp.playFX;
import Utility.Istruzione;
import robogp.matchmanager.Match;
import Utility.NotifyGameOver;
import Partita.PartitaView;
import Partita.RegistroPartita;
import Allenamento.Robot;
import Partita.RobotMarker;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;
import robogp.robodrome.BeltCell;
import robogp.robodrome.BoardCell;
import robogp.robodrome.Direction;
import robogp.robodrome.FloorCell;
import robogp.robodrome.MovableElement;
import robogp.robodrome.PitCell;
import robogp.robodrome.Robodrome;
import robogp.robodrome.RobodromeException;
import robogp.robodrome.Rotation;
import robogp.robodrome.animation.Animation;
import robogp.robodrome.animation.LaserFireAnimation;
import robogp.robodrome.animation.LaserHideAnimation;
import robogp.robodrome.animation.PauseAnimation;
import robogp.robodrome.animation.RobotFallAnimation;
import robogp.robodrome.animation.RobotHitAnimation;
import robogp.robodrome.animation.RobotMoveAnimation;
import robogp.robodrome.animation.TransitionAnimation;
import robogp.robodrome.image.ImageUtil;
import robogp.robodrome.image.TileProvider;


/**
 * Questa classe permette di visualizzare un Robodromo e mostrare con semplici
 * animazioni le azioni di gioco.
 *
 * @author claudia
 */
public class RobodromeView extends JComponent implements Serializable {
    
    public  static boolean isPartita=false;

    private class Play extends Thread implements Serializable {
        
        
        private Semaphore semp;
        private JButton avviaButton;
       
        
        
        public Play(Semaphore semp,JButton avviaButton) {
            this.semp=semp;
            this.avviaButton=avviaButton;
            
        }
        
        public Play(Semaphore semp,boolean flag) {
            this.semp=semp;
            isPartita=flag;
            
        }
        
        public Play(boolean flag) {
            isPartita=flag;
        }
        
        
        private int waitTime;
        
        @Override
        public void run() {
            waitTime = (int) (1000 / FRAMERATE);
            while (isPlayingAnimation()) {
                try {
                    
                    evolveAnimation();
                    
                    repaint();
                    Thread.sleep(waitTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RobodromeView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(semp!=null && !(isPartita))
                semp.release();
            if(semp !=null && isPartita) {
               
               
                semp.release();
                
                
            }
           
        }
    }

    private static final int BORDER = 15;
    private static final int FRAMERATE = 60;

    private final Robodrome drome;
    private final int cellSize;
    private transient BufferedImage boardImage;
    private transient BufferedImage cellImg;
    private transient BufferedImage baseCell;

    private int originX;
    private int originY;

    private boolean followingAction = false;
    private boolean dragging = false;
    private boolean playingAnimation = false;
    private Play player;
    
    private boolean rotationFlag=true;
    private int lastMousePressedX;
    private int lastMousePressedY;
    private int dragOriginX;
    private int dragOriginY;
    private int counterBelt=1;
    private int counterBeltE=1;
    private RobotMoveAnimation ani;
    private MovableElement theRobot;
    

    private LinkedList<Animation> animationsQueue;
    
    private boolean transitioning;
    private Animation currentAnimation;
    private Direction preDirectionBelt;

    private final HashMap<String, MovableElement> robotMarkers;
    private MovableElement hitMarker;
    public static boolean isUndo=false;
    
    private HashMap<MovableElement,RobotMarker> listRobotMarkers= new HashMap<>();
    private LaserFireAnimation.LaserState laser;

    private final Color laserLightColor = Color.decode("#00C1CF");
    private final Color laserDarkColor = Color.decode("#3200FF");
    private transient final Stroke laserExternalStroke = new BasicStroke(6, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
    private transient final Stroke laserInternalStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);

    private final ArrayList<RobodromeAnimationObserver> observers;
    
    private String pathBeltSound="sound/BeltSoundFX.wav";
    private String pathLaserFX="sound/LaserFX.wav";
    private String pathDamageFX="sound/DamageFX.wav";
    private String pathMovement="sound/RobotMovementFX.wav";
    private Sound laserSound;
    private Sound damageSound;
    private Sound movementSound;
    private File laserFile;
    
    
    
    public static boolean beltFlag=true;
    
    

    private void startDragging() {
        /*if (followingAction && isPlayingAnimation()) {
            stopFollowingAction();
        }*/
        dragging = true;
        this.dragOriginX = originX;
        this.dragOriginY = originY;
    }

    private void stopDragging() {
        dragging = false;
    }

    private boolean isDragging() {
        return dragging;
    }

    /**
     * Imposta la visualizzazione del robodromo in modo che segua
     * automaticamente l'elemento in movimento durante le animazioni, nel caso
     * il robodromo non sia interamente inquadrato.
     */
    public void startFollowingAction() {
        //if (!isDragging()) {
        followingAction = true;
        //}
    }

    /**
     * @return true se la vista &egrave; impostata per seguire automaticamente
     * l'elemento in movimento durante le animazioni.
     */
    public boolean isFollowingAction() {
        return followingAction;
    }

    /**
     * Imposta la visualizzazione in modo che non segua pi&ugrave; l'elemento in
     * movimento durante le animazioni.
     */
    public void stopFollowingAction() {
        if (isFollowingAction() && currentAnimation != null && currentAnimation.isTransition()
                && !((TransitionAnimation) currentAnimation).isCommanded()) {
            currentAnimation = null;
        }
        followingAction = false;
    }

    /**
     * @return true se c'&egrave; un'animazione in riproduzione.
     */
    public boolean isPlayingAnimation() {
        return this.playingAnimation;
    }

    /**
     * Aggiunge all'elenco di animazioni da riprodurre il movimento (traslazione
     * e/o rotazione) di un robot. Attenzione: non vi &egrave; alcuna verifica
     * sulla "logica" del movimento (ad esempio il robot potrebbe attraversare
     * le pareti).
     *
     * @param robot il robot da muovere
     * @param movement il numero di caselle di cui si muove
     * @param dir la direzione del movimento
     * @param rot l'entit&agrave; e la direzione di rotazione
     */
    public void addRobotMove(RobotMarker robot, int movement, Direction dir, Rotation rot) {
        if (!isPlayingAnimation()) {
            this.animationsQueue.add(new RobotMoveAnimation(robot.getOwner(), movement, dir, rot));
        } else {
            throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
    }

    /**
     * Aggiunge all'elenco di animazioni da riprodurre il movimento (traslazione
     * e/o rotazione) di un robot, che ne spinge altri con s&eacute;.
     * Attenzione: la "spinta" in questo caso significa semplicemente che gli
     * altri robot si muovono in modo identico a quello principale per quanto
     * riguarda l'aspetto di traslazione. Non vi &egrave; alcuna verifica sulla
     * "logica" del movimento (ad esempio il robot spinto qui specificato
     * potrebbe essere lontano dal robot principale, e si muoverebbe lo stesso).
     *
     * @param robot il robot da muovere
     * @param movement il numero di caselle di cui si muove
     * @param dir la direzione del movimento
     * @param rot l'entit&agrave; e la direzione di rotazione
     */
    public void addRobotMove(RobotMarker robot, int movement, Direction dir, Rotation rot, RobotMarker[] push) {
        if (!isPlayingAnimation()) {
            String[] p = new String[push.length];
            for (int i = 0; i < p.length; i++) {
                p[i] = push[i].getOwner();
            }
            this.animationsQueue.add(new RobotMoveAnimation(robot.getOwner(), movement, dir, rot, p));
        } else {
            throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
    }
    
    public void addRobotMove(RobotMarker robot, Istruzione istr, ArrayList<RobotMarker> push) {
        if (!isPlayingAnimation()) {
            
            String scheda=null;
            Direction direction=null;
            Rotation rot=Rotation.NO;
            int  movement=0;
            scheda=istr.getTipoScheda();
            direction=robot.getDirection();
            
            switch(scheda) {
                
                case "U-turn":
                    rot=Rotation.CCW180;
                    break;
                    
                case  "Turn Left":
                    rot=Rotation.CCW90;
                    break;
                
                case  "Turn Right":
                    rot=Rotation.CW90;
                    break;
                
                case "Back-up":
                    movement=1;
                    direction=reverse(direction);
                    break;         
                    
                case "Move 1":
                    movement=1;
                    break;
                    
                case "Move 2":
                    movement=2;
                    break;
                
                case "Move 3":
                    movement=3;
                    break;
                    
                default:
                   System.err.println("ERRORE NESSUNA DIREZIONE TROVATA");
                   System.exit(-1);
            }
            
            
            String[] p = new String[push.size()];
            for (int i = 0; i < p.length; i++) {
                p[i] = push.get(i).getOwner();
               
            }
             this.animationsQueue.add(new RobotMoveAnimation(robot.getOwner(), movement, direction, rot, p));
           
        } else {
            throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
    }
    
    
    
    public Istruzione addRobotMove(Robot robot) {
        
        DefaultListModel <Istruzione> istruzioniVector=null;
        Istruzione istruzione=null;
        String scheda=null;
        Direction direction=null;
        Rotation rot=Rotation.NO;
        int  movement=0;
        
        if (!isPlayingAnimation()) {
            istruzioniVector=robot.getRegistro().getIstruzioniDaEseguire();
            istruzione=istruzioniVector.firstElement();
            istruzioniVector.remove(0);
            robot.getRegistro().getIstruzioniEseguite().addElement(istruzione);
            scheda=istruzione.getTipoScheda();
            direction=robot.getDirection();
            
            switch(scheda) {
                
                case "U-turn":
                    rot=Rotation.CCW180;
                    break;
                    
                case  "Turn Left":
                    rot=Rotation.CCW90;
                    break;
                
                case  "Turn Right":
                    rot=Rotation.CW90;
                    break;
                
                case "Back-up":
                    movement=1;
                    direction=reverse(direction);
                    break;         
                    
                case "Move 1":
                    movement=1;
                    break;
                    
                case "Move 2":
                    movement=2;
                    break;
                
                case "Move 3":
                    movement=3;
                    break;
                    
                default:
                   System.err.println("ERRORE NESSUNA DIREZIONE TROVATA");
                   System.exit(-1);
            }
            
            
            
            
            this.animationsQueue.add(new RobotMoveAnimation(drome.getRobot().getColor(),movement, direction,rot));
           
            beltFlag=true;
            rotationFlag=true;
            counterBelt=1;
        
            return istruzione;
            
            
        }else {
            throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
    }
    
    public void addRobotMove(RobotMarker robot,Istruzione istr) {
        
        String scheda=null;
        Direction direction=null;
        Rotation rot=Rotation.NO;
        
        
        int  movement=0;
        
        if (!isPlayingAnimation()) {
            
            scheda=istr.getTipoScheda();
            direction=robot.getDirection();
            
            switch(scheda) {
                
                case "U-turn":
                    rot=Rotation.CCW180;
                    break;
                    
                case  "Turn Left":
                    rot=Rotation.CCW90;
                    break;
                
                case  "Turn Right":
                    rot=Rotation.CW90;
                    break;
                
                case "Back-up":
                    movement=1;
                    direction=reverse(direction);
                    break;         
                    
                case "Move 1":
                    movement=1;
                    break;
                    
                case "Move 2":
                    movement=2;
                    break;
                
                case "Move 3":
                    movement=3;
                    break;
                    
                default:
                   System.err.println("ERRORE NESSUNA DIREZIONE TROVATA");
                   System.exit(-1);
            }
            
            
            
            
            this.animationsQueue.add(new RobotMoveAnimation(robot.getOwner(),movement, direction,rot));
           
           
           
           
            beltFlag=true;
            rotationFlag=true;
            counterBelt=1;
        }else {
             throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
            
        
        
    }
    
    public Istruzione addRobotUndo(Robot robot) {
        
        DefaultListModel <Istruzione> istruzioniEseguiteVector=null;
        Istruzione istruzione=null;
        String scheda=null;
        Direction direction=null;
        Rotation rot=Rotation.NO;
        int  movement=0;
        
        if (!isPlayingAnimation()) {
            
           
            istruzioniEseguiteVector=robot.getRegistro().getIstruzioniEseguite();
            istruzione=istruzioniEseguiteVector.lastElement();
            istruzioniEseguiteVector.remove(istruzioniEseguiteVector.size()-1);
            
            robot.getRegistro().getIstruzioniDaEseguire().add(0, istruzione);
            scheda=istruzione.getTipoScheda();
            direction=robot.getDirection();
            
           
            switch(scheda) {
                
                case "U-turn":
                    rot=Rotation.CCW180;
                    break;
                    
                case  "Turn Left":
                    rot=Rotation.CW90;
                    break;
                
                case  "Turn Right":
                    rot=Rotation.CCW90;
                    break;
                
                case "Back-up":
                    movement=1;
                    break;
                    
                case "Move 1":
                    movement=1;
                    direction=reverse(direction);
                    break;
                    
                case "Move 2":
                    movement=2;
                    direction=reverse(direction);
                    break;
                
                case "Move 3":
                    movement=3;
                    direction=reverse(direction);
                    break;
                    
                default:
                   System.err.println("Non e stata trovata la scheda");
                   System.exit(-1);
                
            }
            
            
            
            
            this.animationsQueue.add(new RobotMoveAnimation(drome.getRobot().getColor(),movement, direction,rot));
            
             return istruzione; 
        
        
        }else {
            throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
       
    }
          
            

    /**
     * Aggiunge all'elenco di animazioni da riprodurre la "caduta" di un robot
     * in un buco nero. Attenzione: non ci sono verifiche sulla "logica" del
     * movimento: il robot precipita anche se non si trova davvero su un buco.
     *
     * @param robot Il robot che precipita
     */
    public void addRobotFall(RobotMarker robot) {
        if (!isPlayingAnimation()) {
            this.animationsQueue.add(new RobotFallAnimation(robot.getOwner()));
        } else {
            throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
    }
    
    public Direction reverse(Direction direction){
        Direction dir=null;
        switch(direction) {
            case N:
                dir=Direction.S;
                break;
                        
            case S:
                dir=Direction.N;
                break;
                        
            case W:
                dir=Direction.E;
                break;
                            
            case E:
                dir=Direction.W;
                break;
                            
            default:
               System.err.println("Errore nessuna direz trovata");
               System.exit(-1);
        }
        return dir;
                    
    }

    /**
     * Aggiunge all'elenco di animazioni da riprodurre il lampeggiare di un
     * colpo che indica un danno subito dal robot. Attenzione: non ci sono
     * verifiche sulla "logica" dell'animazione: il robot potrebbe non avere
     * affatto subito danni.
     *
     * @param robot il robot che viene colpito
     * @param dir il lato da cui deve essere visualizzato il colpo
     */
    public void addRobotHit(RobotMarker robot, Direction dir) {
        if (!isPlayingAnimation()) {
           this.animationsQueue.add(new RobotHitAnimation(robot.getOwner(), dir));
           
        } else {
            throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
    }

    /**
     * Aggiunge all'elenco di animazioni da riprodurre lo sparo di un laser,
     * dalla casella di partenza a quella di destinazione. L'ultimo laser
     * sparato rimane visibile sino a che non viene attivata un'animazione di
     * tipo "hideLaserFire". Attenzione: non ci sono verifiche sulla "logica"
     * dell'animazione, ossia che lo sparo parta davvero dal robot e arrivi ad
     * una effettiva destinazione.
     *
     * @param robot il robot che spara
     * @param dir la direzione in cui spara
     * @param start la casella di partenza dello sparo (la colonna, se lo sparo
     * &egrave; orizzontale; la riga, se lo sparo &egrave; verticale)
     * @param end la casella di arrivo dello sparo (la colonna, se lo sparo
     * &egrave; orizzontale; la riga, se lo sparo &egrave; verticale)
     * @param hitRobot true se lo sparo colpisce un robot nella casella di
     * arrivo
     * @param hitEndWall true se lo sparo colpisce una parete nella casella di
     * arrivo (attenzione: se lo sparo colpisce una parete nella casella
     * successiva a quella di arrivo, questo parametro deve essere false)
     * @see hideLaserFire#addHideLaser
     */
    public void addLaserFire(RobotMarker robot, Direction dir, int start, int end,
            boolean hitRobot, boolean hitEndWall) {
        if (!isPlayingAnimation()) {
            this.animationsQueue.add(new LaserFireAnimation(robot.getOwner(), dir, start, end, hitRobot, hitEndWall, cellSize));
        } else {
            throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
    }

    /**
     * Aggiunge all'elenco di animazioni da riprodurre la scomparsa dallo
     * schermo degli spari laser.
     */
    public void addHideLaser() {
        if (!isPlayingAnimation()) {
            this.animationsQueue.add(new LaserHideAnimation());
        } else {
            throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
    }

    /**
     * Aggiunge una pausa all'elenco di animazioni da riprodurre.
     *
     * @param millisec la durata della pausa.
     */
    public void addPause(long millisec) {
        if (!isPlayingAnimation()) {
            this.animationsQueue.add(new PauseAnimation(millisec));
        } else {
            throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
    }

    private MovableElement getMarker(RobotMarker robot) {
        String s = robot.getOwner();
       
        if (this.robotMarkers.get(s)== null) throw new RobodromeException("Il Robot " + robot.getOwner() + " non si"
                + " trova sul robodromo.");
        return this.robotMarkers.get(s);
    }
    
    /**
     * Aggiunge all'elenco di animazioni da riprodurre lo scorrimento della
     * vista per inquadrare un dato robot.
     *
     * @param robot il robot da inquadrare
     */
    public void addFocusMove(RobotMarker robot) {
        if (!isPlayingAnimation()) {
            this.animationsQueue.add(new TransitionAnimation(getMarker(robot).getPosX(),
                    getMarker(robot).getPosY(), true));
        } else {
            throw new RobodromeException("Cannot add animation steps while playing animation.");
        }
    }

    /**
     * Mette un segnalino robot sul robodromo, nella posizione indicata.
     * Attenzione: non pu&ograve; essere invocato quando c'&egrave;
     * un'animazione in corso.
     *
     * @param robot il Robot da inserire
     * @param dir la direzione in cui guarda il robot inizialmente
     * @param row la riga a cui si posiziona il robot
     * @param col la colonna a cui si posiziona il robot
     * @param visible true se il robot &egrave; inizialmente visibile
     */
    public void placeRobot(Robot robot, boolean visible) {
        if (!this.isPlayingAnimation()) {
            
            MovableElement me =  new MovableElement(robot.getImage(cellSize), Direction.E);
            me.setBoardPosition(robot.getRow(), robot.getCol());
            me.setPosX(robot.getCol() * cellSize + cellSize / 2 + BORDER);
            me.setPosY(robot.getRow() * cellSize + cellSize / 2 + BORDER);
            me.setDirection(robot.getDirection(),drome.getRobot());
            me.setVisible(visible);
            this.robotMarkers.put(robot.getColor(), me);
            //this.robotMarkers.put(robot.getName(), me);
            repaint();
        } else {
            throw new RobodromeException("Cannot add robot during animation.");
        }
    }
    
    
    /*
    
    METODO PRECEDENTE 
    
    public void placeRobot(RobotMarker robot, Direction dir, int row, int col, boolean visible) {
        if (!this.isPlayingAnimation()) {
            
            MovableElement me =  new MovableElement(robot.getImage(cellSize), Direction.E);
            me.setBoardPosition(row, col);
            me.setPosX(col * cellSize + cellSize / 2 + BORDER);
            me.setPosY(row * cellSize + cellSize / 2 + BORDER);
            me.setDirection(dir);
            me.setVisible(visible);
            this.robotMarkers.put(robot.getOwner(), me);
            listRobotMarkers.put(me, robot);
            repaint();
        } else {
            throw new RobodromeException("Cannot add robot during animation.");
        }
    }
    
    */
    
    public void placeRobot(RobotMarker robot, Direction dir, int row, int col, boolean visible) {
        if (!this.isPlayingAnimation()) {
            
            MovableElement me =  new MovableElement(robot.getImage(cellSize), Direction.E);
            me.setBoardPosition(row, col);
            me.setPosX(col * cellSize + cellSize / 2 + BORDER);
            me.setPosY(row * cellSize + cellSize / 2 + BORDER);
            me.setDirection(dir);
            me.setVisible(visible);
            this.robotMarkers.put(robot.getOwner(), me);
            listRobotMarkers.put(me, robot);
            repaint();
        } else {
            throw new RobodromeException("Cannot add robot during animation.");
        }
    }
    
    
    
    /**
     * Posiziona sul robodromo un robot gi&agrave; precedentemente inserito con
     * addRobot. Attenzione: non pu&ograve; essere invocato quando c'&egrave;
     * un'animazione in corso.
     *
     * @param robot il robot da posizionare
     * @param dir la direzione in cui guarda il robot inizialmente
     * @param row la riga a cui si posiziona il robot
     * @param col la colonna a cui si posiziona il robot
     * @param visible true se il robot &egrave; visibile
     * @see addRobot#placeRobot
     */
    public void changeRobotPosition(Robot robot, Direction dir, int row, int col, boolean visible) {
        if (!this.isPlayingAnimation()) {
            MovableElement me = robotMarkers.get(robot.getColor());
            me.setDirection(dir,drome.getRobot());
            me.setBoardPosition(row, col);
            me.setPosX(col * cellSize + cellSize / 2 + BORDER);
            me.setPosY(row * cellSize + cellSize / 2 + BORDER);
            me.setVisible(visible);
            me.resetImageSize(cellSize, cellSize);
            repaint();
        } else {
            throw new RobodromeException("Cannot place robot during animation.");
        }
    }
    
     public void changeRobotPosition(RobotMarker robot, Direction dir, int row, int col, boolean visible) {
        if (!this.isPlayingAnimation()) {
            
            
           
            MovableElement me = robotMarkers.get(robot.getOwner());
            
            BufferedImage image =robot.getImage(cellSize);
            me.setImage(image);
            me.setDirection(Direction.E);
            robot.setDirection(Direction.E);
            me.setRotation(0.000001f);
            me.setBoardPosition(row, col);
            me.setPosX(col * cellSize + cellSize / 2 + BORDER);
            me.setPosY(row * cellSize + cellSize / 2 + BORDER);
            me.resetImageSize(cellSize, cellSize);
            me.setVisible(visible);
            repaint();
            
            
            
        } else {
            throw new RobodromeException("Cannot place robot during animation.");
        }
    }
    
    

    /**
     * Crea una vista per un robodromo
     *
     * @param rd il robodromo da visualizzare
     * @param cellSize la dimensione del lato di una cella, in pixel
     */
    public RobodromeView(Robodrome rd, int cellSize) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        this.drome = rd;
        this.cellSize = cellSize;
        robotMarkers = new HashMap<>();
        

        buildBoardImage();

        originX = 0;
        originY = 0;

        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                onResize(e);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
            }

            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                onMouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                onMousePressed(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                onMouseReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        this.setMinimumSize(new Dimension(cellSize, cellSize));
        //this.setMaximumSize(new Dimension(boardImage.getWidth(), boardImage.getHeight()));
        this.setBackground(Color.black);

        this.animationsQueue = new LinkedList<>();

        hitMarker = new MovableElement(TileProvider.getTileProvider().getTile("H", Direction.W), Direction.W);
        hitMarker.setVisible(false);
     
        
        laser = new LaserFireAnimation.LaserState();
        laser.setVisible(false);

        observers = new ArrayList<>();
        
        
        TinySound.init();
        laserFile = new File("sound/LaserFX.wav");
        laserSound=TinySound.loadSound(laserFile);
	
       
        File damageFile=new File(pathDamageFX);
        damageSound=TinySound.loadSound(damageFile);
        
        File movementFile=new File(pathMovement);
        movementSound=TinySound.loadSound(movementFile);
	
    }

    private void buildBoardImage() {
        int contentW = this.drome.getColumnCount() * cellSize + BORDER * 2;
        int contentH = this.drome.getRowCount() * cellSize + BORDER * 2;
        boardImage = new BufferedImage(contentW, contentH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D imgG2 = (Graphics2D) boardImage.getGraphics();

        for (int r = 0; r < this.drome.getRowCount(); r++) {
            for (int c = 0; c < this.drome.getColumnCount(); c++) {
                BoardCell bc = this.drome.getCell(r, c);
                cellImg = bc.getImage();
                baseCell = ImageUtil.scale(cellImg, cellSize, cellSize);
                imgG2.drawImage(baseCell, BORDER + c * cellSize, BORDER + r * cellSize, this);
            }
        }

    }

    /**
     * D&agrave; inizio alla riproduzione delle animazioni inserite in coda.
     */
    public void play(Semaphore semp,JButton avviaButton) {
        if (!isPlayingAnimation() && !this.animationsQueue.isEmpty()) {
            this.currentAnimation = null;
            playingAnimation = true;
            
                    
                       
            /*if (this.isFollowingAction()) {
                int nextRobot = this.animationsQueue.peek().getWhich();
                TransitionAnimation ani = new TransitionAnimation(robotMarkers[nextRobot].getPosX(),
                        robotMarkers[nextRobot].getPosY(), false);
                this.animationsQueue.push(ani);
            }*/
            this.player = new Play(semp,avviaButton);
            for (RobodromeAnimationObserver obs : observers) {
                obs.animationStarted();
            }
            this.player.start();
        }
    }
    
    public void play(Semaphore sem,boolean flag) {
        
        if (!isPlayingAnimation() && !this.animationsQueue.isEmpty()) {
            this.currentAnimation = null;
            playingAnimation = true;
            
                    
                       
            /*if (this.isFollowingAction()) {
                int nextRobot = this.animationsQueue.peek().getWhich();
                TransitionAnimation ani = new TransitionAnimation(robotMarkers[nextRobot].getPosX(),
                        robotMarkers[nextRobot].getPosY(), false);
                this.animationsQueue.push(ani);
            }*/
            this.player = new Play(sem,flag);
            for (RobodromeAnimationObserver obs : observers) {
                obs.animationStarted();
            }
            this.player.start();
        }else {
            
            sem.release();
        }
    }
    
    public void play(boolean flag) {
        if (!isPlayingAnimation() && !this.animationsQueue.isEmpty()) {
            this.currentAnimation = null;
            playingAnimation = true;
            
                    
                       
            /*if (this.isFollowingAction()) {
                int nextRobot = this.animationsQueue.peek().getWhich();
                TransitionAnimation ani = new TransitionAnimation(robotMarkers[nextRobot].getPosX(),
                        robotMarkers[nextRobot].getPosY(), false);
                this.animationsQueue.push(ani);
            }*/
            this.player = new Play(flag);
            for (RobodromeAnimationObserver obs : observers) {
                obs.animationStarted();
            }
            this.player.start();
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.black);
        g2.fillRect(0, 0, this.getWidth(), this.getHeight());
        g2.drawImage(boardImage, -originX, -originY, this);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (laser.isVisible()) {
            g2.setPaint(this.laserLightColor);
            float circleR = laser.getCircleRadius() * 1.5f;
            g2.fillOval((int) (laser.getCircleX() - originX - circleR),
                    (int) (laser.getCircleY() - originY - circleR),
                    (int) circleR * 2, (int) circleR * 2);
            if (laser.isFired()) {
                g2.setStroke(this.laserExternalStroke);
                g2.drawLine(laser.getCircleX() - originX, laser.getCircleY() - originY,
                        laser.getCurrentX() - originX, laser.getCurrentY() - originY);
            }
            g2.setPaint(this.laserDarkColor);
            circleR = laser.getCircleRadius();
            g2.fillOval((int) (laser.getCircleX() - originX - circleR),
                    (int) (laser.getCircleY() - originY - circleR), (int) circleR * 2, (int) circleR * 2);
            if (laser.isFired()) {
                g2.setStroke(this.laserInternalStroke);
                g2.drawLine(laser.getCircleX() - originX, laser.getCircleY() - originY,
                        laser.getCurrentX() - originX, laser.getCurrentY() - originY);
            }
        }
        for (MovableElement m: robotMarkers.values()) {
            if (m != null && m.isVisible()) {
                if (m.getRotation() != 0) {
                    AffineTransform at = new AffineTransform();
                    at.translate(m.getPosX() - originX, m.getPosY() - originY);
                    at.rotate(m.getRotation());
                    at.translate(-m.getImage().getWidth() / 2,
                            -m.getImage().getHeight() / 2);
                    g2.drawImage(m.getImage(), at, this);
                } else {
                    g2.drawImage(m.getImage(), m.getPosX() - m.getImage().getWidth() / 2 - originX,
                            m.getPosY() - m.getImage().getHeight() / 2 - originY, this);
                }
            }
        }

        if (hitMarker.isVisible()) {
            g2.drawImage(hitMarker.getImage(), hitMarker.getPosX() - originX - hitMarker.getImage().getWidth() / 2,
                    hitMarker.getPosY() - originY - hitMarker.getImage().getHeight() / 2, this);
        }
        
      
        drawBorder(g2);
    }

    private void drawBorder(Graphics2D g2) { // DA SISTEMARE
        g2.setPaint(Color.black);
        g2.fillRect(-originX, -originY, BORDER, boardImage.getHeight());
        g2.fillRect(-originX, -originY, boardImage.getWidth(), BORDER);
        g2.fillRect(-originX, boardImage.getHeight() - BORDER - originY, boardImage.getWidth(), BORDER);
        g2.fillRect(boardImage.getWidth() - BORDER - originX, -originY, BORDER, boardImage.getHeight());
        int emptyX = this.getWidth() - (boardImage.getWidth() - originX);
        int emptyY = this.getHeight() - (boardImage.getHeight() - originY);
        g2.fillRect(boardImage.getWidth() - originX, 0, emptyX, this.getHeight());
        g2.fillRect(0, boardImage.getHeight() - originY, this.getWidth(), emptyY);
    }

    private void onResize(ComponentEvent e) {
        int gapX = this.getWidth() - (boardImage.getWidth() - originX);
        int gapY = this.getHeight() - (boardImage.getHeight() - originY);
        if (gapX > 0) {
            originX = Math.max(0, originX - gapX);
        }
        if (gapY > 0) {
            originY = Math.max(0, originY - gapY);
        }
        this.repaint();
    }

    private void onMouseDragged(MouseEvent e) {
        if (!isDragging()) {
            startDragging();
        }
        int shiftX = e.getX() - this.lastMousePressedX;
        int shiftY = e.getY() - this.lastMousePressedY;
        originX = Math.max(0, Math.min(this.dragOriginX - shiftX, boardImage.getWidth() - this.getWidth()));
        originY = Math.max(0, Math.min(this.dragOriginY - shiftY, boardImage.getHeight() - this.getHeight()));
        repaint();
    }

    private void onMousePressed(MouseEvent e) {
        this.lastMousePressedX = e.getX();
        this.lastMousePressedY = e.getY();
    }

    private void onMouseReleased(MouseEvent e) {
        if (isDragging()) {
            stopDragging();
        }
    }

    private synchronized void evolveAnimation() {
       
        if (isPlayingAnimation()) {
            if (currentAnimation == null) { // step to next animation
                if (animationsQueue.isEmpty()) {
                    for (RobodromeAnimationObserver obs : observers) {
                        obs.animationFinished();
                    }
                    this.playingAnimation = false;
                } else { // queue not empty 
                    if (this.isFollowingAction() && !transitioning) {
                        String watch = animationsQueue.peek().getWhich();
                        if (watch != null && watch.length()>0) {
                           
                            currentAnimation = new TransitionAnimation(robotMarkers.get(watch).getPosX(),
                                    robotMarkers.get(watch).getPosY(), false);
                          
                            transitioning = true;
                            
                            
                        } else {
                            
                            currentAnimation = animationsQueue.poll();
                            transitioning = false;
                        }
                    } else {
                        currentAnimation = animationsQueue.poll();
                        transitioning = false;
                    }
                    RobotMoveAnimation robotMove=null;
                    
                    if(currentAnimation instanceof RobotMoveAnimation) {
                        robotMove= (RobotMoveAnimation)currentAnimation;
                    }else if(currentAnimation instanceof PauseAnimation) {
                        PauseAnimation pauseAnimation=(PauseAnimation)currentAnimation;
                    }
                    
                   
                    
                    
                    if(currentAnimation instanceof RobotMoveAnimation) {
                        MovableElement theRobot = robotMarkers.get(currentAnimation.getWhich());
                       
                            if(robotMove.getMovement()>=1 && drome.getCell(theRobot.getRowPos(),theRobot.getColPos())!=null &&
                                   drome.getCell(theRobot.getRowPos(),theRobot.getColPos()).
                                hasWall(robotMove.getDirection())==true) {
                                    System.out.println("Muro");
                                    currentAnimation=null;
                                return;
                            } 
                    }
                    
                    
                  
                        currentAnimation.setStartTime(System.currentTimeMillis());
                        currentAnimation.setLastTime(currentAnimation.getStartTime());
                        startAnimation();
                   
                    
                    
                
                }
            }

            if (currentAnimation != null) { // continue animation
                long currtime = System.currentTimeMillis();
                long elapsed = currtime - currentAnimation.getStartTime();
                long step = currtime - currentAnimation.getLastTime();
                if (currentAnimation.hasFinished(elapsed)) {
                    finishAnimation(elapsed, step);
                    
                if(!(isPartita)) {
                    if(rotationFlag==true) {     
                        if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos())!=null){
                            if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos()).getType()=='F') {
                                FloorCell floorCell=(FloorCell)drome.getCell(theRobot.getRowPos(),theRobot.getColPos());
                                if(floorCell.isLeftRotator()) {
                                    this.animationsQueue.add(new RobotMoveAnimation(drome.getRobot().getColor(),0,
                                                  theRobot.getDirection(),Rotation.CCW90));
                                    rotationFlag=false;
                                }else if(floorCell.isRightRotator()) {
                                    this.animationsQueue.add(new RobotMoveAnimation(drome.getRobot().getColor(),0,
                                                  theRobot.getDirection(),Rotation.CW90));
                                    rotationFlag=false;
                                }

                            }
                        }
                    }
                }
                if(!(isPartita)) {
                 // CASELLA
                    if(beltFlag==true) {
                        
                            if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos())!=null){
                                if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos()).getType()=='B'){

                                            if(isUndo==false) {
                                                BeltCell beltCell=(BeltCell)drome.getCell(theRobot.getRowPos(),theRobot.getColPos());

                                                this.animationsQueue.add(new RobotMoveAnimation(drome.getRobot().getColor(),1,
                                                       beltCell.getOutputDirection(),Rotation.NO));

                                                playFX(pathBeltSound);

                                                preDirectionBelt=beltCell.getOutputDirection();

                                                beltFlag=false;

                                            }else {
                                                BeltCell beltCell=(BeltCell)drome.getCell(theRobot.getRowPos(),theRobot.getColPos());

                                                this.animationsQueue.add(new RobotMoveAnimation(drome.getRobot().getColor(),1,
                                                       reverse(beltCell.getOutputDirection()),Rotation.NO));

                                                playFX(pathBeltSound);

                                                preDirectionBelt=reverse(beltCell.getOutputDirection());

                                                beltFlag=false;

                                            }



                                }else if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos()).getType()=='E'){

                                    if(counterBelt<=2) {

                                        if(isUndo==false) {
                                            BeltCell beltCell=(BeltCell)drome.getCell(theRobot.getRowPos(),theRobot.getColPos());


                                             this.animationsQueue.add(new RobotMoveAnimation(drome.getRobot().getColor(),1,
                                                   beltCell.getOutputDirection(),Rotation.NO));
                                             preDirectionBelt=beltCell.getOutputDirection();


                                             playFX(pathBeltSound);

                                             counterBelt++;
                                             beltFlag=false;
                                        }else {
                                            BeltCell beltCell=(BeltCell)drome.getCell(theRobot.getRowPos(),theRobot.getColPos());


                                             this.animationsQueue.add(new RobotMoveAnimation(drome.getRobot().getColor(),1,
                                                   reverse(beltCell.getOutputDirection()),Rotation.NO));
                                             preDirectionBelt=reverse(beltCell.getOutputDirection());


                                             playFX(pathBeltSound);

                                             counterBelt++;
                                             beltFlag=false;

                                        }
                                    }
                                }

                            }
                        
                    }else{
                         Direction beltCellDirection=null;
                        if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos()) instanceof BeltCell) {
                            BeltCell beltCell=(BeltCell)drome.getCell(theRobot.getRowPos(),theRobot.getColPos());
                            if(isUndo==true) {
                                beltCellDirection=reverse(beltCell.getOutputDirection());
                                
                            }else {
                                beltCellDirection=beltCell.getOutputDirection();
                            }
                                
                            if(preDirectionBelt!=beltCellDirection) {
                                 Rotation rot=Rotation.NO;
                                 switch (beltCellDirection) {
                                     case W:
                                         switch(theRobot.getDirection()) {
                                             case N:
                                                 rot=Rotation.CCW90; //turn left
                                                 break;
                                             case E:
                                                 rot=Rotation.CCW180;
                                                 break;
                                             case S:
                                                 rot=Rotation.CW90;
                                                 break;
                                             case W:
                                                 rot=Rotation.NO;
                                                 break;
                                         }
                                         break;

                                     case E:
                                         switch(theRobot.getDirection()) {
                                             case N:
                                                 rot=Rotation.CW90;
                                                 break;
                                             case E:
                                                 rot=Rotation.NO;
                                                 break;
                                             case S:
                                                 rot=Rotation.CCW90;
                                                 break;
                                             case W:
                                                 rot=Rotation.CCW180;
                                                 break;

                                         }
                                         break;

                                     case S:
                                         switch(theRobot.getDirection()) {
                                             case N:
                                                 rot=Rotation.CCW180;
                                                 break;
                                             case E:
                                                 rot=Rotation.CW90;
                                                 break;
                                             case S:
                                                 rot=Rotation.NO;
                                                 break;
                                             case W:
                                                 rot=Rotation.CCW90;
                                                 break;

                                         }
                                         break;

                                     case N:
                                         switch(theRobot.getDirection()) {
                                             case N:
                                                 rot=Rotation.NO;
                                                 break;
                                             case E:
                                                 rot=Rotation.CCW90;
                                                 break;
                                             case W:
                                                 rot=Rotation.CW90;
                                                 break;
                                             case S:
                                                 rot=Rotation.CCW180;
                                                 break;

                                         }
                                         break;

                                 }

                                 preDirectionBelt=beltCellDirection;
                                
                                    this.animationsQueue.add(new RobotMoveAnimation(drome.getRobot().getColor(),0,
                                           beltCellDirection,rot));
                                 
                                
                            }
                           
                                if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos()).getType()=='E') {
                                        if(counterBelt<=2) {

                                            if(isUndo==false) {
                                                this.animationsQueue.add(new RobotMoveAnimation(drome.getRobot().getColor(),1,
                                                   beltCell.getOutputDirection(),Rotation.NO));
                                                preDirectionBelt=beltCell.getOutputDirection();
                                            }else {
                                                 this.animationsQueue.add(new RobotMoveAnimation(drome.getRobot().getColor(),1,
                                                   reverse(beltCell.getOutputDirection()),Rotation.NO));
                                                preDirectionBelt=reverse(beltCell.getOutputDirection());

                                            }

                                            counterBelt++;
                                            beltFlag=false;
                                        }
                                }
                            
                        }
                        
                    }
                }
                
                    currentAnimation = null;
                    
                    
                } else {
                    stepAnimation(elapsed, step);
                    currentAnimation.setLastTime(currtime);
                }
            }
            
           
        }

    }

    private void startAnimation() {
        
        if (currentAnimation.isRobotMove()) {
            
            movementSound.play();
            ani = (RobotMoveAnimation) currentAnimation;
            
                   
            ani.setStartPosition(robotMarkers.get(ani.getWhich()).getPosX(),
                    robotMarkers.get(ani.getWhich()).getPosY());
            
         
            theRobot = robotMarkers.get(ani.getWhich());
           
                     
        } else if (currentAnimation.isRobotFall()) {
            damageSound.play();
            RobotFallAnimation ani = (RobotFallAnimation) currentAnimation;
            ani.setStartSize(cellSize);
        } else if (currentAnimation.isRobotHit()) {
            damageSound.play();
            RobotHitAnimation ani = (RobotHitAnimation) currentAnimation;
            MovableElement theRobot = robotMarkers.get(ani.getWhich());
            this.hitMarker = new MovableElement(ImageUtil.scale(
                    TileProvider.getTileProvider().getTile("H", ani.getDirection()), cellSize, cellSize), ani.getDirection());
            this.hitMarker.setBoardPosition(theRobot.getRowPos(), theRobot.getColPos());
            this.hitMarker.setPosX(theRobot.getPosX() + (int) (cellSize * ani.getShiftX()));
            this.hitMarker.setPosY(theRobot.getPosY() + (int) (cellSize * ani.getShiftY()));
            this.hitMarker.setDirection(ani.getDirection(),listRobotMarkers.get(theRobot));
            this.hitMarker.setVisible(true);
            ani.adjustPhase();
        } else if (currentAnimation.isLaserFire()) {
            
            laserSound.play(50);
            LaserFireAnimation ani = (LaserFireAnimation) currentAnimation;
            MovableElement theRobot = robotMarkers.get(ani.getWhich());
            int endLen = (int) (cellSize * (ani.shouldHitRobot() ? 0.5 : (ani.shouldHitEndWall() ? 0.92 : 1)));
            switch (ani.getDirection()) {
                case E:
                    
                    laser.setCirclePosition(BORDER + ani.getStartCell() * cellSize + cellSize,
                            BORDER + theRobot.getRowPos() * cellSize + cellSize / 2);
                    ani.setStartPosition(laser.getCircleX(), laser.getCircleY());
                    ani.setEndPosition(BORDER + ani.getEndCell() * cellSize + endLen,
                            laser.getCircleY());
                    break;
                case W:
                    laser.setCirclePosition(BORDER + ani.getStartCell() * cellSize,
                            BORDER + theRobot.getRowPos() * cellSize + cellSize / 2);
                    ani.setStartPosition(laser.getCircleX(), laser.getCircleY());
                    ani.setEndPosition(BORDER + ani.getEndCell() * cellSize + (cellSize - endLen),
                            laser.getCircleY());
                    break;
                case N:
                    laser.setCirclePosition(BORDER + theRobot.getColPos() * cellSize + cellSize / 2,
                            BORDER + ani.getStartCell() * cellSize);
                    ani.setStartPosition(laser.getCircleX(), laser.getCircleY());
                    ani.setEndPosition(laser.getCircleX(),
                            BORDER + ani.getEndCell() * cellSize + (cellSize - endLen));
                    break;
                case S:
                    laser.setCirclePosition(BORDER + theRobot.getColPos() * cellSize + cellSize / 2,
                            BORDER + ani.getStartCell() * cellSize + cellSize);
                    ani.setStartPosition(laser.getCircleX(), laser.getCircleY());
                    ani.setEndPosition(laser.getCircleX(),
                            BORDER + ani.getEndCell() * cellSize + endLen);
                    break;
            }
            laser.setCircleRadius(0);
            laser.setCurrentPosition(laser.getCircleX(), laser.getCircleY());
            laser.setFired(false);
            laser.setVisible(true);
        } else if (currentAnimation.isTransition()) { // TO DO START TRANSITION
            System.out.println("Sono in transizione!");
            TransitionAnimation ani = (TransitionAnimation) currentAnimation;
            ani.setBoundary(this.getWidth(), this.getHeight(), boardImage.getWidth(), boardImage.getHeight(), cellSize);
            /*ani.startOriginX = originX;
            ani.startOriginY = originY;*/
            int tX, tY;
            if (ani.getBoundary().getLeft() < originX) {
                tX = ani.getBoundary().getLeft();
            } else if (ani.getBoundary().getRight() > (originX + this.getWidth())) {
                tX = ani.getBoundary().getRight() - this.getWidth();
            } else {
                tX = originX;
            }
            if (ani.getBoundary().getTop() < originY) {
                tY = ani.getBoundary().getTop();
            } else if (ani.getBoundary().getBottom() > (originY + this.getHeight())) {
                tY = ani.getBoundary().getBottom() - this.getHeight();
            } else {
                tY = originY;
            }
            ani.setCurrentOrigin(originX, originY);
            ani.setTargetOrigin(tX, tY);
            /*int diffX = ani.targetOriginX - ani.startOriginX;
            int diffY = ani.targetOriginY - ani.startOriginY;
            double distance = Math.sqrt(diffX * diffX + diffY * diffY) / cellSize;
            ani.duration = (int) (distance * cellMovementTime) / 2;*/
            ani.setSpeed(cellSize);
        } else if (currentAnimation.isLaserHide()) {
            laser.setVisible(false);
        }
    }

    private void finishAnimation(long elapsed, long step) {
        if (currentAnimation.isRobotMove()) {
            ani = (RobotMoveAnimation) currentAnimation;
            theRobot = robotMarkers.get(ani.getWhich());
            
            
                                  
            if (ani.getMovement() > 0) {
                int diffRow = 0;
                int diffCol = 0;
                switch (ani.getDirection()) {
                    case W:
                        diffCol = -ani.getMovement();
                        break;
                    case E:
                        diffCol = ani.getMovement();
                        break;
                    case N:
                        diffRow = -ani.getMovement();
                        break;
                    case S:
                        diffRow = ani.getMovement();
                        break;
                }
                int i=0,j=0;
                
                
                
                searchWall(diffRow,diffCol,i,j);
                
               
                
                theRobot.setPosX(theRobot.getColPos() * cellSize + cellSize / 2 + BORDER);
                theRobot.setPosY(theRobot.getRowPos() * cellSize + cellSize / 2 + BORDER);
               
                           
                
                
              
                
                for (String r : ani.getPushRobots()) {
                    
                    
                    System.out.println("ani getDirection"+ani.getDirection());
                    switch(ani.getDirection()) {
                        
                        case S:
                            if(ani.getMovement()==1) {
                                robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                break;
                            }
                            else if(ani.getMovement()==2){
                                switch(robotMarkers.get(r).getDistanza()) {
                                    case 1:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;

                                    case 2:
                                        robotMarkers.get(r).setRowPos(robotMarkers.get(r).getRowPos()-1);
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;
                                }
                                
                            }else if(ani.getMovement()==3) {
                                    switch(robotMarkers.get(r).getDistanza()) {
                                        case 1:

                                            robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                            robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                            break;

                                        case 2:
                                            robotMarkers.get(r).setRowPos(robotMarkers.get(r).getRowPos()-1);
                                            robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                            robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                            break;   
                                        
                                        case 3:
                                            robotMarkers.get(r).setRowPos(robotMarkers.get(r).getRowPos()-2);
                                            robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                            robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                            break;   
                                            
                                            
                                        
                                        
                                }
                                
                                
                                
                            }
                            
                            break;
                        
                        
                        
                        case N:
                            if(ani.getMovement()==1) {
                                
                                robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                break;
                                
                            }else if (ani.getMovement()==2) {
                                
                                switch(robotMarkers.get(r).getDistanza()) {
                                    case 1:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;

                                    case 2:
                                        robotMarkers.get(r).setRowPos(robotMarkers.get(r).getRowPos()+1);
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;
                                }
                                
                                
                                
                                
                                
                            }else if (ani.getMovement()==3) {
                                switch(robotMarkers.get(r).getDistanza()) {
                                    case 1:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;

                                    case 2:
                                        robotMarkers.get(r).setRowPos(robotMarkers.get(r).getRowPos()+1);
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;
                                        
                                    case 3:
                                        robotMarkers.get(r).setRowPos(robotMarkers.get(r).getRowPos()+2);
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;
                                     
                                        
                                        
                                        
                                }
                                
                                
                                
                                
                            }
                            
                            
                            
                            
                            
                            break;
                        
                                         
                        
                        
                        case W:
                            if(ani.getMovement()==1) {
                                
                                /*FUNZIONA MA GLI ALTRI CASI DI MOVIMENTO NO
                                if(drome.getCell(robotMarkers.get(r).getRowPos(),robotMarkers.get(r).getColPos())!=null &&
                                   drome.getCell(robotMarkers.get(r).getRowPos(),robotMarkers.get(r).getColPos())
                                   .hasWall(Direction.W)) {
                                          
                                        theRobot.setColPos(robotMarkers.get(r).getColPos()-4);
                                        robotMarkers.get(r).setColPos(robotMarkers.get(r).getColPos()+2);
                                                    
                                        break;
                                }else {
                                    /*
                                    theRobot.setPosX(theRobot.getColPos() * cellSize + cellSize / 2 + BORDER);
                                    theRobot.setPosY(theRobot.getRowPos() * cellSize + cellSize / 2 + BORDER);
                                    
                                    */
                                
                                    robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                    robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                break;
                                
                                
                               
                            }else if(ani.getMovement()==2) {
                                switch(robotMarkers.get(r).getDistanza()) {
                                    case 1:
                                          if(drome.getCell(robotMarkers.get(r).getRowPos(),robotMarkers.get(r).getColPos())!=null &&
                                            
                                            drome.getCell(robotMarkers.get(r).getRowPos(),robotMarkers.get(r).getColPos()-1).hasWall(Direction.W))  {
                                          
                                            theRobot.setColPos(robotMarkers.get(r).getColPos());
                                            robotMarkers.get(r).setColPos(robotMarkers.get(r).getColPos()-1);
                                                    
                                        break;
                                    }else {
                                        theRobot.setPosX(theRobot.getColPos() * cellSize + cellSize / 2 + BORDER);
                                        theRobot.setPosY(theRobot.getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                    break;
                                    }
                                                
                                      

                                    case 2:
                                        if(drome.getCell(robotMarkers.get(r).getRowPos(),robotMarkers.get(r).getColPos())!=null &&
                                            drome.getCell(robotMarkers.get(r).getRowPos(),robotMarkers.get(r).getColPos())
                                            .hasWall(Direction.W)) {
                                          
                                            theRobot.setColPos(robotMarkers.get(r).getColPos()-4);
                                            robotMarkers.get(r).setColPos(robotMarkers.get(r).getColPos()+2);
                                            break;
                                        }else{
                                                    
                                            theRobot.setPosX(theRobot.getColPos() * cellSize + cellSize / 2 + BORDER);
                                            theRobot.setPosY(theRobot.getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        

                                            robotMarkers.get(r).setColPos(robotMarkers.get(r).getColPos()+2);
                                            robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                            robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        
                                        }
                                        break;
                                }
                                
                                
                            }else if (ani.getMovement()==3) {
                                switch(robotMarkers.get(r).getDistanza()) {
                                    case 1:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;

                                    case 2:
                                        robotMarkers.get(r).setColPos(robotMarkers.get(r).getColPos()+2);
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;
                                        
                                        
                                    case 3:
                                        robotMarkers.get(r).setColPos(robotMarkers.get(r).getColPos()+4);
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;
                                        
                                }
                                
                                
                                
                                
                            }
                        
                                      
                        
                        
                        
                        case E:
                        
                            if(ani.getMovement()==1) {
                                robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                break;



                            }else  if(ani.getMovement()==2) {
                                switch(robotMarkers.get(r).getDistanza()) {
                                    case 1:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;

                                    case 2:
                                        robotMarkers.get(r).setColPos(robotMarkers.get(r).getColPos()-1);
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;
                                }

                            }else if(ani.getMovement()==3) {

                                switch(robotMarkers.get(r).getDistanza()) {
                                    case 1:
                                         robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                         robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                         break;
                                    case 2:
                                        robotMarkers.get(r).setColPos(robotMarkers.get(r).getColPos()-1);
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;

                                    case 3:
                                        robotMarkers.get(r).setColPos(robotMarkers.get(r).getColPos()-2);
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                                        break;

                                }


                            }else {
                                robotMarkers.get(r).setPosX(robotMarkers.get(r).getColPos() * cellSize + cellSize / 2 + BORDER);
                                robotMarkers.get(r).setPosY(robotMarkers.get(r).getRowPos() * cellSize + cellSize / 2 + BORDER);
                            }
                        
                    }
                        
                        
                }
                if (this.isFollowingAction()) { // camera needs to follow movement
                    cameraOn(theRobot.getPosX(), theRobot.getPosY());
                }
            }
            
            
            if(isPartita) {
                if (ani.getRotation() != Rotation.NO) {
                    theRobot.setRotation(0);
                    theRobot.setDirection(Rotation.changeDirection(theRobot.getDirection(), ani.getRotation()),
                            listRobotMarkers.get(theRobot));
                    theRobot.resetImageSize(cellSize, cellSize);
                }
                
            } else {
                if (ani.getRotation() != Rotation.NO) {
                    theRobot.setRotation(0);
                    theRobot.setDirection(Rotation.changeDirection(theRobot.getDirection(), ani.getRotation()),
                            drome.getRobot());
                    theRobot.resetImageSize(cellSize, cellSize);
                }
            }
          
            
        } else if (currentAnimation.isRobotFall()) {
            RobotFallAnimation ani = (RobotFallAnimation) currentAnimation;
            MovableElement theRobot = robotMarkers.get(ani.getWhich());
            theRobot.setVisible(false);
            theRobot.resetImageSize(cellSize,cellSize);
        } else if (currentAnimation.isRobotHit()) {
            hitMarker.setVisible(false);
        } else if (currentAnimation.isLaserFire()) {
            LaserFireAnimation ani = (LaserFireAnimation) currentAnimation;
            laser.setCurrentPosition(ani.getEndX(), ani.getEndY());
        } else if (currentAnimation.isTransition()) {
            TransitionAnimation ani = (TransitionAnimation) currentAnimation;
            originX = ani.getTargetOriginX();
            originY = ani.getTargetOriginY();
            ani.setCurrentOrigin(originX, originY);
        }
    }
    
    public void searchWall (int diffRow,int diffCol,int i ,int j) {
        if(i==diffRow && j==diffCol) {
            theRobot.setBoardPosition(theRobot.getRowPos() + (i),
            theRobot.getColPos() +(j));
                        
            for (String r : ani.getPushRobots()) {
                robotMarkers.get(r).setBoardPosition(robotMarkers.get(r).getRowPos() +i,
                robotMarkers.get(r).getColPos() + j);
            }
            return;
        }
        
        if(drome.getCell(theRobot.getRowPos()+i,theRobot.getColPos()+j)!=null &&
                drome.getCell(theRobot.getRowPos()+i,theRobot.getColPos()+j) 
            .hasWall(ani.getDirection())==true ) {
                theRobot.setBoardPosition(theRobot.getRowPos() + (i),
                theRobot.getColPos() +(j));
                        
                for (String r : ani.getPushRobots()) {
                    robotMarkers.get(r).setBoardPosition(robotMarkers.get(r).getRowPos() +i,
                    robotMarkers.get(r).getColPos() + j);
                }
                        
                        
                return;
        }
        
        if(diffRow >=0 && diffCol <0) {
            if(i<diffRow) 
                i++;
            if(diffCol<j)
                j--;
            
        }else if(diffRow <0 && diffCol <0) {
            if(diffRow<i)
                i--;
            if(diffCol<j)
                j--;
            
        }
        else if(diffRow <0 && diffCol >=0) {
            if(diffRow<i)
                i--;
            if(j<diffCol)
                j++;
        }
        
        else {
            if(i<diffRow)
                i++;
            if(j<diffCol)
                j++;
                
        }
             
        
        searchWall(diffRow,diffCol,i,j);
        
    }
    
    public void searchWall (int diffRow,int diffCol,int i ,int j,MovableElement theRobot) {
        if(i==diffRow && j==diffCol) {
            theRobot.setBoardPosition(theRobot.getRowPos() + (i),
            theRobot.getColPos() +(j));
                        
            for (String r : ani.getPushRobots()) {
                robotMarkers.get(r).setBoardPosition(robotMarkers.get(r).getRowPos() +i,
                robotMarkers.get(r).getColPos() + j);
            }
            return;
        }
        
        if(drome.getCell(theRobot.getRowPos()+i,theRobot.getColPos()+j)!=null &&
                drome.getCell(theRobot.getRowPos()+i,theRobot.getColPos()+j) 
            .hasWall(ani.getDirection())==true ) {
                theRobot.setBoardPosition(theRobot.getRowPos() + (i),
                theRobot.getColPos() +(j));
                        
                for (String r : ani.getPushRobots()) {
                    robotMarkers.get(r).setBoardPosition(robotMarkers.get(r).getRowPos() +i,
                    robotMarkers.get(r).getColPos() + j);
                }
                        
                        
                return;
        }
        
        if(diffRow >=0 && diffCol <0) {
            if(i<diffRow) 
                i++;
            if(diffCol<j)
                j--;
            
        }else if(diffRow <0 && diffCol <0) {
            if(diffRow<i)
                i--;
            if(diffCol<j)
                j--;
            
        }
        else if(diffRow <0 && diffCol >=0) {
            if(diffRow<i)
                i--;
            if(j<diffCol)
                j++;
        }
        
        else {
            if(i<diffRow)
                i++;
            if(j<diffCol)
                j++;
                
        }
             
        
        searchWall(diffRow,diffCol,i,j);
        
    }
    
    public boolean searchWallRobotColpito(int diffRow,int diffCol,int i ,int j,String r) {
         
        
        if(drome.getCell(theRobot.getRowPos()+i,theRobot.getColPos()+j)!=null &&
                drome.getCell(theRobot.getRowPos()+i,theRobot.getColPos()+j) 
            .hasWall(ani.getDirection())==true ) {
                theRobot.setBoardPosition(theRobot.getRowPos() + (i),
                theRobot.getColPos() +(j));
                        
              
                robotMarkers.get(r).setBoardPosition(robotMarkers.get(r).getRowPos() +i,
                robotMarkers.get(r).getColPos() + j);
                
                        
                        
                return true;
        }
        
        if(diffRow >=0 && diffCol <0) {
            if(i<diffRow) 
                i++;
            if(diffCol<j)
                j--;
            
        }else if(diffRow <0 && diffCol <0) {
            if(diffRow<i)
                i--;
            if(diffCol<j)
                j--;
            
        }
        else if(diffRow <0 && diffCol >=0) {
            if(diffRow<i)
                i--;
            if(j<diffCol)
                j++;
        }
        
        else {
            if(i<diffRow)
                i++;
            if(j<diffCol)
                j++;
                
        }
             
        
        return searchWallRobotColpito(diffRow,diffCol,i,j,r);
    }
    
    private void stepAnimation(long elapsed, long step) {
        
        if (currentAnimation.isRobotMove()) {
            RobotMoveAnimation ani = (RobotMoveAnimation) currentAnimation;
            
            float timeFraction = elapsed / (float) currentAnimation.getDuration();
            MovableElement theRobot = robotMarkers.get(ani.getWhich());
            if (ani.getMovement() > 0) {
                int realmove = (int) (cellSize * ani.getMovement() * timeFraction);
                float timeMove=  cellSize  * timeFraction;
                int move;
                int oldPosX = theRobot.getPosX();
                int oldPosY = theRobot.getPosY();
                int rowPos=theRobot.getRowPos();
                int colPos=theRobot.getColPos();
                int k =1;
                
                
                    switch (ani.getDirection()) {
                        
                        
                            
                            
                        
                        
                        
                        case W:


                            while(k<=ani.getMovement()){


                                if(drome.getCell(rowPos,colPos-k)!=null &&
                                        drome.getCell(rowPos,colPos-k).hasWall(ani.getDirection())==true){
                                         theRobot.setPosX(ani.getStartX() - (int)timeMove*k);
                                         break;
                                }
                                else if(k==ani.getMovement()){
                                   theRobot.setPosX(ani.getStartX() - realmove );
                                    break;
                                }
                                k++;

                            }

                            break;
                        case E:

                            while(k<=ani.getMovement()){


                                if(drome.getCell(rowPos,colPos+k)!=null &&
                                        drome.getCell(rowPos,colPos+k).
                                        hasWall(ani.getDirection())==true) {


                                     theRobot.setPosX(ani.getStartX() + (int)timeMove*k);

                                     break;

                                }

                                else if(k==ani.getMovement()) {
                                     theRobot.setPosX(ani.getStartX() + realmove);
                                     break;
                                }

                                k++;
                            }

                            break;
                        case N:

                             while(k<=ani.getMovement()){



                                if(drome.getCell(rowPos-k,colPos)!=null &&
                                        drome.getCell(rowPos-k,colPos).hasWall(ani.getDirection())==true) {

                                    theRobot.setPosY(ani.getStartY() - (int)timeMove*k);
                                    break;
                                }
                                else if(k==ani.getMovement()) {
                                    theRobot.setPosY(ani.getStartY() - realmove);
                                    break;
                                }
                                k++;

                            }
                                break;
                        case S:
                            while(k<=ani.getMovement()){


                                if(drome.getCell(rowPos+k,colPos)!=null &&
                                        drome.getCell(rowPos+k,colPos).hasWall(ani.getDirection())==true) {
                                         theRobot.setPosY(ani.getStartY() +(int)timeMove*k);

                                        break;
                                }else if(k==ani.getMovement()) {
                                     theRobot.setPosY(ani.getStartY() + realmove);
                                     break;
                                }
                                k++;

                            }
                            break;
                }
                int diffX = theRobot.getPosX() - oldPosX;
                int diffY = theRobot.getPosY() - oldPosY;
                for (String r : ani.getPushRobots()) {
                    
                    switch (ani.getDirection()) {
                        
                        case S:
                            if(ani.getMovement()==1) {
                                robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                break;
                      
                            }
                            
                            else if(ani.getMovement()==2) {
                                switch(robotMarkers.get(r).getDistanza()) {

                                    case 1:
                                        
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;

                                    case 2:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX()+diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY());
                                        break;
                                        
                                     
                                }
                                
                                
                                
                            }else if(ani.getMovement()==3) {
                                switch(robotMarkers.get(r).getDistanza()) {

                                    case 1:
                                        
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;

                                    case 2:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX()+diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY());
                                        break;
                                        
                                    case 3:
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX()+diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY());
                                        break;
                                        
                                     
                                }
                                
                                
                                
                                
                            }
                            
                            break;
                        
                        
                        
                        case N:
                            if(ani.getMovement()==1) {
                                robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                break;


                            
                            
                            
                            }else if(ani.getMovement()==2) {
                                switch(robotMarkers.get(r).getDistanza()) {

                                    case 1:
                                        
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;

                                    case 2:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX()+diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY());
                                        break;
                                }
                                break;
                                
                                
                            }else if (ani.getMovement()==3) {
                                switch(robotMarkers.get(r).getDistanza()) {

                                    case 1:
                                        
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;

                                    case 2:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX()+diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY());
                                        break;
                                        
                                    case 3:
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX()+diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY());
                                        break;
                                        
                                }
                                break;
                                
                                
                                
                                
                                
                            }
                        
                        
                        
                        break;
                        case W:
                            if(ani.getMovement()==1) {
                                robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                break;


                            }else if(ani.getMovement()==2) {

                                switch(robotMarkers.get(r).getDistanza()) {

                                    case 1:
                                        
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;

                                    case 2:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX());
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;
                                    
                                    
                                }

                            }else if(ani.getMovement()==3) {
                                switch(robotMarkers.get(r).getDistanza()) {

                                    case 1:
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;

                                    case 2:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX());
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;

                                    case 3:
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX());
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;
                                }


                            }
                            
                            break;
                            
                            
                        case E:
                            
                            if(ani.getMovement()==1) {
                                robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                break;


                            }else if(ani.getMovement()==2) {

                                switch(robotMarkers.get(r).getDistanza()) {

                                    case 1:
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;

                                    case 2:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX());
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;
                                }

                            }else if(ani.getMovement()==3) {
                                switch(robotMarkers.get(r).getDistanza()) {

                                    case 1:
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX() + diffX);
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;

                                    case 2:

                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX());
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;

                                    case 3:
                                        robotMarkers.get(r).setPosX(robotMarkers.get(r).getPosX());
                                        robotMarkers.get(r).setPosY(robotMarkers.get(r).getPosY() + diffY);
                                        break;
                                }


                            }
                            break;
                    }
                    
                }
                if (this.isFollowingAction()) { // camera needs to follow movement
                    cameraOn(theRobot.getPosX(), theRobot.getPosY());
                }
            }
            if (ani.getRotation() != Rotation.NO) {
                theRobot.setRotation((float) Math.toRadians(
                        ani.getRotation().getDegrees() * timeFraction) * ani.getRotation().getWise());
            }
        } else if (currentAnimation.isRobotFall()) {
            RobotFallAnimation ani = (RobotFallAnimation) currentAnimation;
            float timeFraction = (float) elapsed / (float) currentAnimation.getDuration();
            MovableElement theRobot = robotMarkers.get(ani.getWhich());
            float currdeg = (RobotFallAnimation.DEGREES * timeFraction) % 360;
            float rad = (float) Math.toRadians(currdeg);
            theRobot.setRotation(rad);
            int currSize = (int) (ani.getStartSize() * (1 - timeFraction));
            if (currSize > 0) {
                theRobot.resizeImage(currSize);
            } else {
                theRobot.setVisible(false);
            }

        } else if (currentAnimation.isRobotHit()) {
            RobotHitAnimation ani = (RobotHitAnimation) currentAnimation;
            int phaseNum = (int) (elapsed / ani.getPhase() + 1);
            boolean on = (phaseNum % 2) == 1;
            hitMarker.setVisible(on);
        } else if (currentAnimation.isLaserFire()) {
            LaserFireAnimation ani = (LaserFireAnimation) currentAnimation;
            MovableElement theRobot = robotMarkers.get(ani.getWhich());
            boolean isHeating = (elapsed < LaserFireAnimation.HEATTIME);
            if (isHeating) {
                float heatFraction = elapsed / (float) LaserFireAnimation.HEATTIME;
                laser.setFired(false);
                laser.setCircleRadius((int) (ani.getMaxHeatRadius() * heatFraction));
                if (this.isFollowingAction()) {
                    // move center of bounding box from firing robot to laser start point
                    int bbx = theRobot.getPosX() + (int) ((laser.getCircleX() - theRobot.getPosX()) * heatFraction);
                    int bby = theRobot.getPosY() + (int) ((laser.getCircleY() - theRobot.getPosY()) * heatFraction);
                    this.cameraOn(bbx, bby);
                }
            } else {
                float fireFraction = (elapsed - LaserFireAnimation.HEATTIME) / (float) (ani.getDuration() - LaserFireAnimation.HEATTIME);
                laser.setFired(true);
                int x = laser.getCircleX() + (int) ((ani.getEndX() - ani.getStartX()) * fireFraction);
                int y = laser.getCircleY() + (int) ((ani.getEndY() - ani.getStartY()) * fireFraction);
                laser.setCurrentPosition(x, y);
                if (this.isFollowingAction()) {
                    // move center of bounding box following laser end point
                    this.cameraOn(laser.getCurrentX(), laser.getCurrentY());
                }
            }
        } else if (currentAnimation.isTransition()) {
            TransitionAnimation ani = (TransitionAnimation) currentAnimation;
            int diffX = ani.getTargetOriginX() - originX;
            int diffY = ani.getTargetOriginY() - originY;
            float distance = (float) Math.sqrt(diffX * diffX + diffY * diffY);
            float covered = step * ani.getSpeed();
            float perc = Math.min(covered / distance, 1.0f);
            originX = ani.getCurrentOriginX() + (int) ((ani.getTargetOriginX() - ani.getCurrentOriginX()) * perc);
            originY = ani.getCurrentOriginY() + (int) ((ani.getTargetOriginY() - ani.getCurrentOriginY()) * perc);
            ani.setCurrentOrigin(originX, originY);
        }
        
        
        
        
    }
    
    
    public void searchRobot(ArrayList <RobotMarker> listRobot,RobotMarker robot,Istruzione istruzione){
            
            ArrayList<RobotMarker> robotColpito =new ArrayList<RobotMarker>();
            String scheda=istruzione.getTipoScheda();
            Direction direction=robot.getDirection();
            Rotation rot=Rotation.NO;
            Direction direzColpo=null;
            int movement=-1;
            
            switch(scheda) {
                
                case "U-turn":
                    rot=Rotation.CCW180;
                    break;
                    
                case  "Turn Left":
                    rot=Rotation.CCW90;
                    break;
                
                case  "Turn Right":
                    rot=Rotation.CW90;
                    break;
                
                case "Back-up":
                    movement=1;
                    direction=reverse(direction);
                    break;         
                    
                case "Move 1":
                    movement=1;
                    break;
                    
                case "Move 2":
                    movement=2;
                    break;
                
                case "Move 3":
                    movement=3;
                    break;
                    
                default:
                   System.err.println("ERRORE NESSUNA DIREZIONE TROVATA");
                   System.exit(-1);
            }
        
            if(movement>0) {
            
               MovableElement theRobot = robotMarkers.get(robot.getOwner());
                

                int start=-1;
                int i;
                int k=1;
                boolean colpito=false;

                
               

                switch(direction) {
                    case W:
                        start=theRobot.getColPos();

                        i=start-1;


                        while(k<=movement && i>=0 && !(colpito)) {

                            for(RobotMarker robot2: listRobot) {


                                if(colpito==false) {
                                    
                                    if((theRobot.getRowPos()==robotMarkers.get(robot2.getOwner()).getRowPos())&&
                                        i==robotMarkers.get(robot2.getOwner()).getColPos()) {
                                     
                                  
                                    
                                        colpito=true;
                                        System.out.println("Colpito Robot");
                                        robotColpito.add(robot2);
                                        
                                        direzColpo=Direction.E;
                                        MovableElement theRobot2 = robotMarkers.get(robot2.getOwner());
                                        theRobot2.setDistanza(k);
                                        

                                    }
                                }
                            }

                            i--;
                            k++;

                        }


                        break;

                    case E:


                        start=theRobot.getColPos();


                        i=start+1;


                        while(k<=movement && i<=15 && !(colpito)) {

                            for(RobotMarker robot2: listRobot) {

                                if(colpito==false) {
                                    
                                    
                                    if((theRobot.getRowPos()==robotMarkers.get(robot2.getOwner()).getRowPos())&&
                                        i==robotMarkers.get(robot2.getOwner()).getColPos()) {
                                    
                                    
                                    robotMarkers.get(robot2.getOwner());
                                    
                                    
                                  
                                    
                                        colpito=true;

                                        System.out.println("Colpito Robot");
                                        
                                        robotColpito.add(robot2);
                                        direzColpo=Direction.W;
                                        MovableElement theRobot2 = robotMarkers.get(robot2.getOwner());
                                        theRobot2.setDistanza(k);
                                        
                                    }
                                }
                            }

                            i++;
                            k++;

                        }

                        break;

                    case N:

                        start=theRobot.getRowPos();
                        i=start-1;


                        while(k<=movement && i>=0 && !(colpito)) {

                            for(RobotMarker robot2: listRobot) {


                                if(colpito==false) {
                                    
                                    
                                    if((theRobot.getColPos()==robotMarkers.get(robot2.getOwner()).getColPos())&&
                                        i==robotMarkers.get(robot2.getOwner()).getRowPos()) {
                                    
                                    
                                    
                                        colpito=true;

                                        System.out.println("Colpito Robot");
                                        robotColpito.add(robot2);
                                        direzColpo=Direction.S;
                                        MovableElement theRobot2 = robotMarkers.get(robot2.getOwner());
                                        theRobot2.setDistanza(k);
                                    }
                                }
                            }


                            i--;
                            k++;
                        }


                        break;



                    case S:
                        start=theRobot.getRowPos();
                        i=start+1;


                        while(k<=movement && i<=11 && !(colpito)) {

                            for(RobotMarker robot2: listRobot) {


                                if(colpito==false) {
                                    
                                    
                                    if((theRobot.getColPos()==robotMarkers.get(robot2.getOwner()).getColPos())&&
                                        i==robotMarkers.get(robot2.getOwner()).getRowPos()) {
                                    
                                   
                                        
                                        
                                        colpito=true;

                                        System.out.println("Colpito Robot");
                                        robotColpito.add(robot2);
                                        direzColpo=Direction.N;
                                        MovableElement theRobot2 = robotMarkers.get(robot2.getOwner());
                                        theRobot2.setDistanza(k);
                                    }
                                }
                            }


                            i++;
                            k++;
                        }



                        break;

                }
            }
          
            
            if(!(robotColpito.isEmpty())) {
                
                this.addRobotMove(robot,istruzione, robotColpito);
                for(RobotMarker r:robotColpito)
                    this.addRobotHit(r,direzColpo);
               damageSound.play();
                
                
            }else {
                this.addRobotMove(robot, istruzione);
            }
            
            
        
    }
    
    public void isBucoNeroOrFuori(ArrayList<RobotMarker> listRobot,PartitaView partitaView){
        for(RobotMarker robot: listRobot) {
            
            MovableElement theRobot = robotMarkers.get(robot.getOwner());
            if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos())==null){
                this.addRobotFall(robot);
                robot.setDistrutto(true);
                StyleConstants.setForeground(partitaView.style, Color.RED);
                partitaView.textArea.append("Il robot "+robot.getName()+"di "+robot.getOwner()+" e stato distrutto"+"\n",partitaView.style);
                
                
            }else if((drome.getCell(theRobot.getRowPos(),theRobot.getColPos()))
                    instanceof PitCell) {
                this.addRobotFall(robot);
                robot.setDistrutto(true);
                StyleConstants.setForeground(partitaView.style, Color.RED);
                 partitaView.textArea.append("Il robot "+robot.getName()+"di "+robot.getOwner()+" e stato distrutto"+"\n",partitaView.style);
                
                
            }
        }
         StyleConstants.setForeground(partitaView.style, Color.BLACK);
               
    }
    
    public void relocateRobotDistrutti(ArrayList<RobotMarker> listRobot,PartitaView partitaView) {
         for(RobotMarker robot: listRobot) {
            
            MovableElement theRobot = robotMarkers.get(robot.getOwner());
            if(robot.isDistrutto()) {
                if(robot.getVita()>0) {
                    changeRobotPosition(robot, theRobot.getDirection(),
                           robot.getCheckpoint().getX(),robot.getCheckpoint().getY() , true); // giusto


                    robot.setDistrutto(false);
                    
                }else {
                    
                    new NotifyGameOver("<html> &emsp &emsp &emsp &emsp Il robot "+robot.getName()+" di "+robot.getOwner()+"<br>"+
                                 "&emsp &emsp non ha piu vite" + " pertanto non puo piu giocare");
                        
                    StyleConstants.setForeground(partitaView.style, Color.RED);
                    partitaView.textArea.append("Il gicatore "+robot.getOwner()+" e stato sconfitto"+"\n",partitaView.style);            
                    
                    
                }
            }
        
        }
        StyleConstants.setForeground(partitaView.style, Color.BLACK);
    }
    
    public void cleanListRobotDistrutti(ArrayList<RobotMarker> listRobot,
            Map <String,RegistroPartita[]> listRegistri,Connection conn) {
        
        for(int i=0;i<listRobot.size();i++) {
            if(listRobot.get(i).getVita()==0) {
                
                Message send= new Message(Match.MatchRefreshList);
                Object[] parameters = new Object[1];
                parameters[0] = listRobot.get(i).getOwner();
                send.setParameters(parameters);
                try {
                    conn.sendMessage(send);
                } catch (PartnerShutDownException ex) {
                    Logger.getLogger(RobodromeView.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                listRegistri.remove(listRobot.get(i).getOwner());
                listRobot.remove(i);
            }
        }
            
    }
    
    
    public void activateBeltExpress(ArrayList<RobotMarker> listRobot) {
        
        for(RobotMarker robot: listRobot) {
            
            MovableElement theRobot = robotMarkers.get(robot.getOwner());
            if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos())!=null){
                if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos()).getType()=='E') {
                   BeltCell beltCell=(BeltCell)drome.getCell(theRobot.getRowPos(),theRobot.getColPos());


                    this.animationsQueue.add(new RobotMoveAnimation(robot.getOwner(),1,
                                                       beltCell.getOutputDirection(),Rotation.NO));
                    preDirectionBelt=beltCell.getOutputDirection();
                    theRobot.setDirectionBelt(preDirectionBelt);

                    playFX(pathBeltSound);

                    

                }
                else {
                    theRobot.setDirectionBelt(null);
                }
            }else {
                theRobot.setDirectionBelt(null);
            }
        }
    }
    
    public void isBeltRotation(ArrayList<RobotMarker> listRobot) {
        
        
        
         for(RobotMarker robot: listRobot) {
            
            MovableElement theRobot = robotMarkers.get(robot.getOwner());
            Direction beltCellDirection=null;
            if(theRobot.getDirectionBelt()!=null) {
                if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos())!=null){
                            if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos()) instanceof BeltCell) {
                               BeltCell beltCell=(BeltCell)drome.getCell(theRobot.getRowPos(),theRobot.getColPos());

                                beltCellDirection=beltCell.getOutputDirection();


                                if(theRobot.getDirectionBelt()!=beltCellDirection) {
                                     Rotation rot=Rotation.NO;
                                     switch (beltCellDirection) {
                                         case W:
                                             switch(theRobot.getDirection()) {
                                                 case N:
                                                     rot=Rotation.CCW90; //turn left
                                                     break;
                                                 case E:
                                                     rot=Rotation.CCW180;
                                                     break;
                                                 case S:
                                                     rot=Rotation.CW90;
                                                     break;
                                                 case W:
                                                     rot=Rotation.NO;
                                                     break;
                                             }
                                             break;

                                         case E:
                                             switch(theRobot.getDirection()) {
                                                 case N:
                                                     rot=Rotation.CW90;
                                                     break;
                                                 case E:
                                                     rot=Rotation.NO;
                                                     break;
                                                 case S:
                                                     rot=Rotation.CCW90;
                                                     break;
                                                 case W:
                                                     rot=Rotation.CCW180;
                                                     break;

                                             }
                                             break;

                                         case S:
                                             switch(theRobot.getDirection()) {
                                                 case N:
                                                     rot=Rotation.CCW180;
                                                     break;
                                                 case E:
                                                     rot=Rotation.CW90;
                                                     break;
                                                 case S:
                                                     rot=Rotation.NO;
                                                     break;
                                                 case W:
                                                     rot=Rotation.CCW90;
                                                     break;

                                             }
                                             break;

                                         case N:
                                             switch(theRobot.getDirection()) {
                                                 case N:
                                                     rot=Rotation.NO;
                                                     break;
                                                 case E:
                                                     rot=Rotation.CCW90;
                                                     break;
                                                 case W:
                                                     rot=Rotation.CW90;
                                                     break;
                                                 case S:
                                                     rot=Rotation.CCW180;
                                                     break;

                                             }
                                             break;

                                     }

                                     preDirectionBelt=beltCellDirection;
                                     theRobot.setDirectionBelt(preDirectionBelt);
                                     this.animationsQueue.add(new RobotMoveAnimation(robot.getOwner(),0,
                                               beltCellDirection,rot));

                                }

                            }
                }
            }
         }
    }
    
    public void activateBelt(ArrayList <RobotMarker> listRobot) {
       
        for(RobotMarker robot: listRobot) {
            MovableElement theRobot = robotMarkers.get(robot.getOwner());
            if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos())!=null){
                
                    if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos()).getType()=='B'){


                        BeltCell beltCell=(BeltCell)drome.getCell(theRobot.getRowPos(),theRobot.getColPos());

                        this.animationsQueue.add(new RobotMoveAnimation(robot.getOwner(),1,
                                             beltCell.getOutputDirection(),Rotation.NO));

                        playFX(pathBeltSound);

                        preDirectionBelt=beltCell.getOutputDirection();
                        theRobot.setDirectionBelt(preDirectionBelt);
                        
                                            



                    }else if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos()).getType()=='E'){


                           if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos()).getType()=='E') {
                                BeltCell beltCell=(BeltCell)drome.getCell(theRobot.getRowPos(),theRobot.getColPos());


                                 this.animationsQueue.add(new RobotMoveAnimation(robot.getOwner(),1,
                                                                    beltCell.getOutputDirection(),Rotation.NO));
                                 preDirectionBelt=beltCell.getOutputDirection();
                                 theRobot.setDirectionBelt(preDirectionBelt);

                                 playFX(pathBeltSound);

                               



                            }


                    }else {
                       theRobot.setDirectionBelt(null);
                    }
                

                }else {
                    theRobot.setDirectionBelt(null);
                }
        }
        
    }
    
    public void activateRotationBelt(ArrayList <RobotMarker> listRobot){
            for(RobotMarker robot: listRobot) {
                MovableElement theRobot = robotMarkers.get(robot.getOwner()); 
                    if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos())!=null){
                        if(drome.getCell(theRobot.getRowPos(),theRobot.getColPos()).getType()=='F') {
                            FloorCell floorCell=(FloorCell)drome.getCell(theRobot.getRowPos(),theRobot.getColPos());
                            if(floorCell.isLeftRotator()) {
                                this.animationsQueue.add(new RobotMoveAnimation(robot.getOwner(),0,
                                              theRobot.getDirection(),Rotation.CCW90));
                                rotationFlag=false;
                            }else if(floorCell.isRightRotator()) {
                                this.animationsQueue.add(new RobotMoveAnimation(robot.getOwner(),0,
                                              theRobot.getDirection(),Rotation.CW90));
                                rotationFlag=false;
                            }

                        }
                    }
            }
            System.out.println("Fine rotationBelt");
        
    }

    public void activateLaser(ArrayList <RobotMarker> listRobot,PartitaView partitaView) {
        
        for(RobotMarker robot: listRobot) {
            
            MovableElement theRobot = robotMarkers.get(robot.getOwner());
            
            
            int start=-1;
            int end=-1;
            int i;
            int j;
            boolean colpito=false;
            boolean flag=true;
            RobotMarker robotColpito=null;
            Direction direzColpo=null;
                    
            switch(theRobot.getDirection()) {
                case W:
                    start=theRobot.getColPos();
                    
                    i=start-1;
                    j=start;
                    
                    while(i>=0 && !(colpito)) {
                        
                        for(RobotMarker robot2: listRobot) {
                           
                            if(flag) {
                                if(drome.getCell(theRobot.getRowPos(),j)!=null){
                                    if((drome.getCell(theRobot.getRowPos(),j).hasWall(Direction.W))) {
                                        colpito=true;
                                        end=j;
                                        System.out.println("Colpito Muro");
                                        
                                    }
                                }
                                
                                flag=false;
                                
                            }
                            
                            if(colpito==false) {
                                if((theRobot.getRowPos()==robotMarkers.get(robot2.getOwner()).getRowPos())&&
                                    i==robotMarkers.get(robot2.getOwner()).getColPos()) {
                                    
                                    
                                    colpito=true;
                                    end=i;
                                   
                                    robotColpito=robot2;
                                    direzColpo=Direction.E;
                                    robot2.setSalute(robot2.getSalute()-1);
                                    StyleConstants.setForeground(partitaView.style, Color.RED);
                                    partitaView.textArea.append("Il robot "+robot2.getName()+" e stato colpito"+"\n",partitaView.style);
                                   

                                }
                            }
                        }
                        
                        if(colpito==false) {
                            if(drome.getCell(theRobot.getRowPos(),j)!=null){
                                if((drome.getCell(theRobot.getRowPos(),j).hasWall(Direction.W))) {
                                    colpito=true;
                                    end=j;
                                    System.out.println("Colpito Muro");
                                   
                                }
                            }
                        }else {
                            
                        }
                        
                        i--;
                        j--;
                    }
                    if(colpito==false)
                        end=0;
                    
                    
                    break;
                    
                case E:
                    
                    
                    start=theRobot.getColPos();
                    
                    
                    i=start+1;
                    j=start;
                    
                    while(i<=15 && !(colpito)) {
                        
                        for(RobotMarker robot2: listRobot) {
                            
                            
                            
                            if(flag) {
                                if(drome.getCell(theRobot.getRowPos(),j)!=null){
                                    if((drome.getCell(theRobot.getRowPos(),j).hasWall(Direction.E))) {
                                    colpito=true;
                                    end=j;
                                    System.out.println("Colpito Muro");
                                   
                                     }
                                }
                                
                                flag=false;
                                
                            }
                            
                            if(colpito==false) {
                                if((theRobot.getRowPos()==robotMarkers.get(robot2.getOwner()).getRowPos())&&
                                    i==robotMarkers.get(robot2.getOwner()).getColPos()) {
                                    
                                    
                                    colpito=true;
                                    end=i;
                                    System.out.println("Colpito Robot");
                                    robotColpito=robot2;
                                    direzColpo=Direction.W;
                                    robot2.setSalute(robot2.getSalute()-1);
                                    StyleConstants.setForeground(partitaView.style, Color.RED);
                                    partitaView.textArea.append("Il robot "+robot2.getName()+" e stato colpito"+"\n",partitaView.style);
                                }
                            }
                        }
                        if(colpito==false) {
                            if(drome.getCell(theRobot.getRowPos(),j)!=null){
                                if((drome.getCell(theRobot.getRowPos(),j).hasWall(Direction.E))) {
                                    colpito=true;
                                    end=j;
                                    System.out.println("Colpito Muro");
                                   
                                }
                            }
                        }else {
                           
                        }
                        
                        i++;
                        j++;
                    }
                    if(colpito==false)
                        end=15;
                    break;
                    
                case N:
                    
                    start=theRobot.getRowPos();
                    i=start-1;
                    j=start;
                    
                    while(i>=0 && !(colpito)) {
                        
                        for(RobotMarker robot2: listRobot) {
                            
                           
                            if(flag) {
                                if(drome.getCell(theRobot.getRowPos(),j)!=null){
                                    if((drome.getCell(j,theRobot.getColPos()).hasWall(Direction.N))) {
                                        colpito=true;
                                        end=j;
                                        System.out.println("Colpito Muro");
                                    
                                    }
                                }
                                
                                flag=false;
                                
                            }
                            
                            if(colpito==false) {
                            
                                if((theRobot.getColPos()==robotMarkers.get(robot2.getOwner()).getColPos())&&
                                    i==robotMarkers.get(robot2.getOwner()).getRowPos()) {
                                    
                                    
                                    colpito=true;
                                    end=i;
                                    System.out.println("Colpito Robot");
                                    robotColpito=robot2;
                                    direzColpo=Direction.S;
                                    robot2.setSalute(robot2.getSalute()-1);
                                    StyleConstants.setForeground(partitaView.style, Color.RED);
                                    partitaView.textArea.append("Il robot "+robot2.getName()+" e stato colpito"+"\n",partitaView.style);
                                }
                            }
                        }
                        if(colpito==false) {
                            if(drome.getCell(theRobot.getRowPos(),j)!=null){
                                if((drome.getCell(j,theRobot.getColPos()).hasWall(Direction.N))) {
                                    colpito=true;
                                    end=j;
                                    System.out.println("Colpito Muro");
                                    
                                }
                            }
                        }else {
                            
                        }
                        
                        i--;
                        j--;
                    }
                    
                    
                    if(colpito==false)
                        end=0;
                    
                    
                    
                    break;
                    
                    
                    
                case S:
                    start=theRobot.getRowPos();
                    i=start+1;
                    j=start;
                    
                    while(i<=11 && !(colpito)) {
                        
                        for(RobotMarker robot2: listRobot) {
                            
                            
                            if(flag) {
                                if(drome.getCell(theRobot.getRowPos(),j)!=null){
                                    if((drome.getCell(j,theRobot.getColPos()).hasWall(Direction.S))) {
                                        colpito=true;
                                        end=j;
                                        System.out.println("Colpito Muro");

                                    }
                                }
                                
                                flag=false;
                                
                            }
                            
                            
                            if(colpito==false) {
                                if((theRobot.getColPos()==robotMarkers.get(robot2.getOwner()).getColPos())&&
                                    i==robotMarkers.get(robot2.getOwner()).getRowPos()) {
                                    
                                    
                                    colpito=true;
                                    end=i;
                                    System.out.println("Colpito Robot");
                                    robotColpito=robot2;
                                    direzColpo=Direction.N;
                                    robot2.setSalute(robot2.getSalute()-1);
                                    StyleConstants.setForeground(partitaView.style, Color.RED);
                                    partitaView.textArea.append("Il robot "+robot2.getName()+" e stato colpito"+"\n",partitaView.style);
                                }
                            }
                        }
                        if(colpito==false) {
                            if(drome.getCell(theRobot.getRowPos(),j)!=null){
                                if((drome.getCell(j,theRobot.getColPos()).hasWall(Direction.S))) {
                                    colpito=true;
                                    end=j;
                                    System.out.println("Colpito Muro");
                                   
                                }
                            }
                        }else {
                           
                        }
                        
                        i++;
                        j++;
                    }
                    
                    
                    if(colpito==false)
                        end=11;
                    
                    
                   
                    break;
                
            }
            
            this.addLaserFire(robot, theRobot.getDirection(), start, end, false, false);
            
            if(robotColpito!=null) {
                this.addRobotHit(robotColpito,direzColpo);
                
            }
            
           
        }
       StyleConstants.setForeground(partitaView.style, Color.BLACK);
                                   
                                    
        
        
        
        
    }
    
    public void activateCheckpoint(ArrayList <RobotMarker> listRobot,PartitaView partitaView) {
        
       for(RobotMarker robot: listRobot) {
            MovableElement theRobot = robotMarkers.get(robot.getOwner());
            for(int i=0;i<16;i++){
                for(int j=0;j<12;j++){
                    if(drome.getCell(i,j) instanceof FloorCell) {
                            
                        if(theRobot.getRowPos()==i && theRobot.getColPos()==j) {
                            if(((FloorCell)drome.getCell(i, j)).isCheckpoint()){
                                    robot.setChekpoint(i,j,(((FloorCell)drome.getCell(i, j)).getCheckpoint()));
                                    
                                    StyleConstants.setForeground(partitaView.style, Color.GREEN);
                                    partitaView.textArea.append("Checkpoint salvato per "+robot.getName()+"\n",partitaView.style);
                                    
                                  
                                  
                            }
                            else if(((FloorCell)drome.getCell(i, j)).isRepair()) {
                                    robot.setChekpoint(i,j,-1);
                                    
                                    StyleConstants.setForeground(partitaView.style, Color.GREEN);
                                    partitaView.textArea.append("Checkpoint salvato per "+robot.getName()+"\n",partitaView.style);
                                    
                                    
                            }else if(((FloorCell)drome.getCell(i, j)).isUpgrade()) {
                                robot.setChekpoint(i,j,-1);
                               
                                StyleConstants.setForeground(partitaView.style, Color.GREEN);
                                partitaView.textArea.append("Checkpoint salvato per "+robot.getName()+"\n",partitaView.style);
                            }
                        }
        
                    }
                }
            }
        }
       
        StyleConstants.setForeground(partitaView.style, Color.BLACK);
                                    
    }
    
    public void effettiRiparazioniCheckpoint(ArrayList <RobotMarker> listRobot,
                                              PartitaView partitaView) {
        for(RobotMarker robot: listRobot) {
            MovableElement theRobot = robotMarkers.get(robot.getOwner());
            for(int i=0;i<16;i++){
                for(int j=0;j<12;j++){
                    if(drome.getCell(i,j) instanceof FloorCell) {
                            
                        if(theRobot.getRowPos()==i && theRobot.getColPos()==j) {
                            if(((FloorCell)drome.getCell(i, j)).isCheckpoint()){
                                    robot.setSalute(robot.getSalute()+1);
                                    
                                    StyleConstants.setForeground(partitaView.style, Color.GREEN);
                                    partitaView.textArea.append("Il robot "+robot.getName()+" guadagna un punto salute"+"\n",partitaView.style);
                                    
                                   
                            }
                            else if(((FloorCell)drome.getCell(i, j)).isRepair()) {
                                    
                                    robot.setSalute(robot.getSalute()+1);
                                    StyleConstants.setForeground(partitaView.style, Color.GREEN);
                                    partitaView.textArea.append("Il robot "+robot.getName()+" guadagna un punto salute"+"\n",partitaView.style);
                                    
                            }else if(((FloorCell)drome.getCell(i, j)).isUpgrade()) {
                                    robot.setSalute(robot.getSalute()+1);
                                    StyleConstants.setForeground(partitaView.style, Color.GREEN);
                                    partitaView.textArea.append("Il robot "+robot.getName()+" guadagna un punto salute"+"\n",partitaView.style);
                            }
                        }
        
                    }
                }
            }
        }
        StyleConstants.setForeground(partitaView.style, Color.BLACK);
    }
    
    public void checkSaluteRobot(ArrayList <RobotMarker> listRobot, PartitaView partitaView) {
         for(RobotMarker robot: listRobot) {
            
            MovableElement theRobot = robotMarkers.get(robot.getOwner());
                if(robot.getSalute()==0) {
                    robot.setVita(robot.getVita()-1);
                    robot.setSalute(10);
            
                    if(robot.getVita()>0) {
                        changeRobotPosition(robot, theRobot.getDirection(),
                            robot.getCheckpoint().getX(),robot.getCheckpoint().getY() , true); 
                    }else {
                         new NotifyGameOver("<html> &emsp &emsp &emsp &emsp Il robot "+robot.getName()+" di "+robot.getOwner()+"<br>"+
                                 "&emsp &emsp non ha piu vite" + " pertanto non puo piu giocare");
                         
                          StyleConstants.setForeground(partitaView.style, Color.RED);
                          partitaView.textArea.append("Il gicatore "+robot.getOwner()+" e stato sconfitto"+"\n",partitaView.style);
                   
                    }
                }
         }
         StyleConstants.setForeground(partitaView.style, Color.BLACK);
                
                    
                    
                    
                
    }
    
    private void cameraOn(int x, int y) {
        TransitionAnimation.BoundingRect bound = new TransitionAnimation.BoundingRect(
                x, y, this.getWidth(), this.getHeight(),
                boardImage.getWidth(), boardImage.getHeight(), cellSize);
        if (bound.getLeft() < originX) {
            originX = bound.getLeft();
        } else if (bound.getRight() > (originX + this.getWidth())) {
            originX = bound.getRight() - this.getWidth();
        }
        if (bound.getTop() < originY) {
            originY = bound.getTop();
        } else if (bound.getBottom() > (originY + this.getHeight())) {
            originY = bound.getBottom() - this.getHeight();
        }
    }

    /**
     * Aggiunge un Animation Observer al robodromo, che ricever&agrave;
     * notifiche sull'inizio e la fine dell'animazione.
     *
     * @param obs l'observer da aggiungere
     */
    public void addObserver(RobodromeAnimationObserver obs) {
        observers.add(obs);
    }

    /**
     * Rimuove un Animation Observer dal robodromo, in modo che non riceva
     * pi&ugrave; notifiche.
     *
     * @param obs l'observer da rimuovere
     */
    public void removeObserver(RobodromeAnimationObserver obs) {
        observers.remove(obs);
    }
    
    
    public Dimension getPreferredSize() {
        return new Dimension(700, 550);
    }
    
    public Robodrome getRobodrome() {
        return drome;
    }
    
    
    
    
}