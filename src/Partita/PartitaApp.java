package Partita;

import connection.Connection;
import connection.Message;
import connection.MessageObserver;
import connection.PartnerShutDownException;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UnsupportedLookAndFeelException;
import org.redisson.api.RLock;
import Utility.Istruzione;
import robogp.matchmanager.Match;
import Partita.PoolGlobale.PoolGiocatore;
import java.awt.Image;
import javax.imageio.ImageIO;
import robogp.robodrome.FloorCell;
import robogp.robodrome.Robodrome;
import robogp.robodrome.view.RobodromeView;
import static robogp.robodrome.view.RobodromeView.isPartita;

/**
 *
 * @author John Hundred
 */
    public class PartitaApp extends JFrame implements MessageObserver{
        
        private Dimension ss = Toolkit.getDefaultToolkit ().getScreenSize ();
	private Dimension frameSize = new Dimension ( 450, 280 );
        private JTextField textFieldNomeGiocatore;
        private JTextField textFieldIndirizzoServer;
        private JTextField textFieldPortaServer;
        private JTextField textFieldPassword;
        private Connection conn;
        private JPanel panel;
        private JPanel panel2;
        private GroupLayout groupLayout;
        private JPanel panel3;
        private JPanel panel4;
        private ArrayList<RobotMarker> listRobot;
        private RobodromeView rv;
        private RobotMarker[] robotsOwner;
        private Semaphore semp=new Semaphore(0);
        private int numberCheckpoint=0;
        PartitaView partitaView;
        private Clip clipBackgroundMenu;
        private Clip decision;
        private Clip megalovania;
        private String pathDecision="sound/MassEffect3-Decision.wav";
        private String pathMegalovania="sound/Megalovania.wav";
        private boolean gameOver=false;
      
        
       
		
			
                
            public PartitaApp(Clip clip) {
               
                
               Image imageLauncher = null;
               try {
                     imageLauncher =ImageIO.read(new File(("img/iconLauncher.jpg")));
               }catch(IOException e) {
                     System.err.println(e.getMessage());
               }
               setIconImage(imageLauncher);
                
                clipBackgroundMenu=clip;
                
               
		setResizable(false);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
                panel = new JPanel();
                
                
		groupLayout = new GroupLayout(getContentPane());
                
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
		);
		
		JLabel labelPassword = new JLabel("PASSWORD");
		
		textFieldPassword = new JTextField();
		textFieldPassword.setColumns(10);
		
		JLabel labelPorta = new JLabel("PORTA SERVER");
		
		textFieldPortaServer = new JTextField();
		textFieldPortaServer.setColumns(10);
		
		JLabel labelIndirizzoServer = new JLabel("INDIRIZZO SERVER");
		
		textFieldIndirizzoServer = new JTextField();
		textFieldIndirizzoServer.setText("localhost");
		textFieldIndirizzoServer.setColumns(10);
		
		
		
		JLabel labelGiocatore = new JLabel("NOME GIOCATORE");
		
		textFieldNomeGiocatore = new JTextField();
		textFieldNomeGiocatore.setColumns(10);
		
		JButton buttonInviaRichiesta = new JButton("INVIA RICHIESTA");
                buttonInviaRichiesta.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        requestButtonActionPerformed(evt);
                    }
                });
                
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(95)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(labelGiocatore, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelPorta)
						.addComponent(labelPassword)
						.addComponent(labelIndirizzoServer))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(textFieldPassword)
						.addComponent(textFieldPortaServer)
						.addComponent(textFieldIndirizzoServer)
						.addComponent(textFieldNomeGiocatore, GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
					.addGap(183))
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(168)
					.addComponent(buttonInviaRichiesta)
					.addContainerGap(270, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(19)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelGiocatore)
						.addComponent(textFieldNomeGiocatore, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldIndirizzoServer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(labelIndirizzoServer))
					.addGap(21)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelPorta)
						.addComponent(textFieldPortaServer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(labelPassword)
						.addComponent(textFieldPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(buttonInviaRichiesta)
					.addGap(66))
		);
		panel.setLayout(gl_panel);
                
		getContentPane().setLayout(groupLayout);
		setBounds(ss.width / 2 - frameSize.width / 2, 
                ss.height / 2 - frameSize.height / 2,
                frameSize.width, frameSize.height);
                addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        if(conn!=null)
                            conn.disconnect();
                        System.exit(0);
                    }
                });
                
                setVisible(true);
                getContentPane().add(panel,"init");
                
                panel2 = new JPanel();
		
		
		JLabel label_1 = new JLabel("Richiesta inviata con successo");
		
		JLabel label_2 = new JLabel("In attesa di risposta...");
		ImageIcon gifImage = new ImageIcon(
				"img/caricamento.gif");
		
		
		JLabel labelGif = new JLabel(gifImage);
		
		GroupLayout gl_panel2 = new GroupLayout(panel2);
		gl_panel2.setHorizontalGroup(
			gl_panel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel2.createSequentialGroup()
					.addGroup(gl_panel2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel2.createSequentialGroup()
							.addGap(164)
							.addComponent(label_2))
						.addGroup(gl_panel2.createSequentialGroup()
							.addGap(143)
							.addComponent(label_1))
						.addGroup(gl_panel2.createSequentialGroup()
							.addGap(101)
							.addComponent(labelGif, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(102, Short.MAX_VALUE))
		);
		gl_panel2.setVerticalGroup(
			gl_panel2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel2.createSequentialGroup()
					.addGap(36)
					.addComponent(label_1)
					.addGap(31)
					.addComponent(label_2)
					.addGap(18)
					.addComponent(labelGif, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(21, Short.MAX_VALUE))
		);
		panel2.setLayout(gl_panel2);
                
                
                panel3 = new JPanel();
		
		
		JLabel label_3 = new JLabel("Richiesta accettata");
		label_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_3.setForeground(Color.GREEN);
		
		JLabel label_4 = new JLabel("In attesa dei giocatori...");
		
		
		
		JLabel labelGif2 = new JLabel(gifImage);
		
		GroupLayout gl_panel3 = new GroupLayout(panel3);
		gl_panel3.setHorizontalGroup(
			gl_panel3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel3.createSequentialGroup()
					.addGap(101)
					.addComponent(labelGif2, GroupLayout.PREFERRED_SIZE, 241, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(102, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_panel3.createSequentialGroup()
					.addContainerGap(179, Short.MAX_VALUE)
					.addGroup(gl_panel3.createParallelGroup(Alignment.LEADING)
						.addComponent(label_4)
						.addComponent(label_3))
					.addGap(148))
		);
		gl_panel3.setVerticalGroup(
			gl_panel3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel3.createSequentialGroup()
					.addGap(35)
					.addComponent(label_3)
					.addGap(32)
					.addComponent(label_4)
					.addGap(18)
					.addComponent(labelGif2, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(21, Short.MAX_VALUE))
		);
		panel3.setLayout(gl_panel3);
                
                
                panel4 = new JPanel();
		
		
		JLabel label_5 = new JLabel("Richiesta rifiutata");
		label_5.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_5.setForeground(Color.RED);
		
		JLabel label_6 = new JLabel("Il manager ha rifiutato la"
				+ " richiesta si consiglia di riprovare successivamente");
		label_6.setForeground(Color.RED);
		
		
		JButton btnNewButton = new JButton("ESCI");
                btnNewButton.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        if(conn!=null)
                            conn.disconnect();
                        System.exit(0);

                    }
                });
		
		GroupLayout gl_panel4 = new GroupLayout(panel4);
		gl_panel4.setHorizontalGroup(
			gl_panel4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel4.createSequentialGroup()
					.addGroup(gl_panel4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel4.createSequentialGroup()
							.addGap(163)
							.addComponent(label_5))
						.addGroup(gl_panel4.createSequentialGroup()
							.addContainerGap()
							.addComponent(label_6))
						.addGroup(gl_panel4.createSequentialGroup()
							.addGap(184)
							.addComponent(btnNewButton)))
					.addContainerGap(78, Short.MAX_VALUE))
		);
		gl_panel4.setVerticalGroup(
			gl_panel4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel4.createSequentialGroup()
					.addGap(36)
					.addComponent(label_5)
					.addGap(29)
					.addComponent(label_6)
					.addGap(55)
					.addComponent(btnNewButton)
					.addContainerGap(80, Short.MAX_VALUE))
		);
		panel4.setLayout(gl_panel4);
                
            
            }
            
            
            private void requestButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
                try {
                    
                    String adressServer=textFieldIndirizzoServer.getText();
                    int portaServer=Integer.parseInt(textFieldPortaServer.getText());
                    String nomeGiocatore=textFieldNomeGiocatore.getText();
                    String password=textFieldPassword.getText();
                    
                    InetAddress address = InetAddress.getByName(adressServer);
                    conn = Connection.connectToHost(address, portaServer);
                    conn.addMessageObserver(this);
                    Message msg = new Message(Match.MatchJoinRequestMsg);
                    Object[] pars = new Object[2];
                    pars[0] = nomeGiocatore;
                    pars[1] = password;
                    msg.setParameters(pars);
                    conn.sendMessage(msg);
                    
                    getContentPane().remove(panel);
                    revalidate(); 
                    repaint();
                    groupLayout = new GroupLayout(getContentPane());
                    groupLayout.setHorizontalGroup(
                            groupLayout.createParallelGroup(Alignment.LEADING)
                                    .addComponent(panel2, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                    );
                    groupLayout.setVerticalGroup(
                            groupLayout.createParallelGroup(Alignment.LEADING)
                                    .addComponent(panel2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                    );
                    
                    getContentPane().setLayout(groupLayout);
                    
                } catch (UnknownHostException ex) {
                    Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (PartnerShutDownException ex) {
                    Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                    System.exit(1);
                }
            }
            
            @Override
            public void notifyMessageReceived(Message msg) {
                System.out.println("***NEW Messaggio ricevuto:" + msg.getName());
                if (msg.getName().equals(Match.MatchJoinReplyMsg)) {
                    boolean reply = (Boolean)msg.getParameter(0);
                    System.out.println("\tRisposta: " + (reply ? "Accettato" : "Rifiutato"));
                    if(reply) {
                        
                        getContentPane().remove(panel2);
                        revalidate(); 
                        repaint();
                        
                        groupLayout = new GroupLayout(getContentPane());
                        groupLayout.setHorizontalGroup(
                                groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(panel3, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                        );
                        groupLayout.setVerticalGroup(
                                groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(panel3, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                        );
                        
                        getContentPane().setLayout(groupLayout);
                    
                   
                        robotsOwner = (RobotMarker[])msg.getParameter(1);
                        for (RobotMarker r: robotsOwner) {
                            System.out.println("\tRobot assegnato: " + r.getName() + " al dock " + r.getDock());
                        }
                    }else {
                        
                        getContentPane().remove(panel2);
                        revalidate(); 
                        repaint();
                        
                        groupLayout = new GroupLayout(getContentPane());
                        groupLayout.setHorizontalGroup(
                                groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(panel4, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
                        );
                        groupLayout.setVerticalGroup(
                                groupLayout.createParallelGroup(Alignment.LEADING)
                                        .addComponent(panel4, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                        );
                        
                        getContentPane().setLayout(groupLayout);
                    }
                }else if(msg.getName().equals(Match.MatchStartMsg)) {
                    
                    String fileName=(String)msg.getParameter(0);
                    listRobot = (ArrayList<RobotMarker>)msg.getParameter(1);
                    
                    try {
                        rv=new RobodromeView(new Robodrome(fileName),38);
                    } catch (LineUnavailableException ex) {
                        Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedAudioFileException ex) {
                        Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    RobodromeView.isPartita=true;
                   
                    
                  
                    
                    
                    for(RobotMarker r: listRobot) {
                        for(int i=0;i<16;i++)
                            for(int j=0;j<12;j++)
                                 if(rv.getRobodrome().getCell(i,j) instanceof FloorCell)
                                     if(((FloorCell)rv.getRobodrome().getCell(i, j)).isDock()) {
                                         if(((FloorCell)rv.getRobodrome().getCell(i, j))
                                                 .getDock()==r.getDock()) {
                                            rv.placeRobot(r,
                                                    ((FloorCell)rv.getRobodrome().getCell(i, j)).getDockDirection(),
                                                    i,j,true);
                                            r.setChekpoint(i, j,-1);
                                            System.out.println("Direzione Dock "+((FloorCell)rv.getRobodrome().getCell(i, j)).getDockDirection());
                                            r.setDirection(((FloorCell)rv.getRobodrome().getCell(i, j)).getDockDirection());
                                         }
                                     }else if(((FloorCell)rv.getRobodrome().getCell(i, j)).isCheckpoint()) {
                                            numberCheckpoint++;
                                     }
                    }
                    for(RobotMarker r:robotsOwner) {
                        try {
                            Message reply = new Message(Match.MatchPoolGenerationMsg);
                            
                            Object[] parameters = new Object[2];
                            parameters[0] = r.getOwner();
                            parameters[1] = r.getSalute();
                            reply.setParameters(parameters);
                            
                            conn.sendMessage(reply);
                        } catch (PartnerShutDownException ex) {
                            Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }else if(msg.getName().equals(Match.MatchPoolGenerationMsg)) {
                    DefaultListModel<Istruzione> listPool=(DefaultListModel<Istruzione>)msg.getParameter(0);
                    
                    setVisible(false); 
                    dispose();
                    
                    
                    
                     for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                                    if ("Nimbus".equals(info.getName())) {
                                        try {
                                            javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                            break;
                                        } catch (ClassNotFoundException ex) {
                                            Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (InstantiationException ex) {
                                            Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (IllegalAccessException ex) {
                                            Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (UnsupportedLookAndFeelException ex) {
                                            Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
             
                    partitaView = new PartitaView(rv,listRobot,listPool,robotsOwner,
                                                              conn);
                    robotsOwner[0].addObserver(partitaView);
                    
                    clipBackgroundMenu.close();
                    
                   
                    playDecision();
                   
                    
                    partitaView.textField_avvisi.setText(" INIZIO FASE DI PROGRAMMAZIONE ! ");
                   
               } else if(msg.getName().equals(Match.MatchRobotPlay)) {
                   
                    decision.close();
                    
                    Map <String,RegistroPartita[]> registriPlayers=(HashMap)msg.getParameter(0);
                    Map<Integer,String> listPriorita=(HashMap)msg.getParameter(1);
                    ArrayList<Integer> prioritaSchede= (ArrayList<Integer>)msg.getParameter(2);
                    int priorita=-1;
                    Istruzione istr;
                    
                    
                      for(String key: registriPlayers.keySet()) {
                          for(int h=0;h<5;h++)
                            System.err.println(" DEBUG in PARTITA APP MATCH ROBOT PLAY: Registro "+h+1+" "+
                                    registriPlayers.get(key)[h].getIstruzione());
                }
                    
                    int i=1;
                    
                    
                    while(i<=5) {
                       
                        
                    try {
                        for (String  key : registriPlayers.keySet()) {
                            
                            RegistroPartita[] reg = registriPlayers.get(key);
                            
                            /* ROUND DI GIOCO */
                            switch(i) {
                                case 1:
                                    istr=reg[0].getIstruzione();
                                    if(istr==null) {
                                        priorita=-1;
                                    }else {
                                        priorita=istr.getPriorita();
                                    }
                                    break;
                                    
                                case 2:
                                    istr=reg[1].getIstruzione();
                                    if(istr==null) {
                                        priorita=-1;
                                    }else {
                                        priorita=istr.getPriorita();
                                    }
                                    break;
                                    
                                case 3:
                                    istr=reg[2].getIstruzione();
                                    if(istr==null) {
                                        priorita=-1;
                                    }else {
                                        priorita=istr.getPriorita();
                                    }
                                    break;
                                    
                                case 4:
                                    istr=reg[3].getIstruzione();
                                    if(istr==null) {
                                        priorita=-1;
                                    }else {
                                        priorita=istr.getPriorita();
                                    }
                                    break;
                                    
                                case 5:
                                    istr=reg[4].getIstruzione();
                                    if(istr==null) {
                                        priorita=-1;
                                    }else {
                                        priorita=istr.getPriorita();
                                    }
                                    break;
                                    
                            }
                            
                            listPriorita.put(priorita,key);
                            
                        }
                        
                        for (Integer   key : listPriorita.keySet()) {
                            if(key!=-1) {
                                prioritaSchede.add(key);
                            }
                        }
                        
                        Collections.sort(prioritaSchede);
                        
                        while(!(prioritaSchede.isEmpty())) {
                            
                            System.out.println("Piroita schede size "+prioritaSchede.size());
                            
                            priorita=prioritaSchede.get(prioritaSchede.size()-1);
                            String ownerPriorita=listPriorita.get(priorita);
                            RobotMarker robotPlayer=null;
                            /* NON CONTROLLA SE UN PRIORIERTAORI PUO AVERE PIU ROBOT */
                            for(RobotMarker robot:listRobot){
                                if(robot.getOwner().equals(ownerPriorita)) {
                                    robotPlayer=robot;
                                }
                            }
                            RegistroPartita[] reg2=registriPlayers.get(ownerPriorita);
                            
                            // Bug la spinta avviene di un quadrato piu in la vedere
                            // il video fatto , non controlla se sono presenti muri 
                            // nel robot spintonato
                            partitaView.textArea.append("Il robot del giocatore "+ownerPriorita+
                                    " esegue "+reg2[i-1].getIstruzione().getTipoScheda()+"\n",partitaView.style);
                            
                            
                            
                            
                            rv.searchRobot(listRobot, robotPlayer, reg2[i-1].getIstruzione());
                            
                            
                            
                            prioritaSchede.remove(prioritaSchede.size()-1);
                            listPriorita.remove(priorita);
                            
                        }
                        
                      
                        
                        
                        if(megalovania ==null || !(megalovania.isActive()) && gameOver==false)
                            playMegalovania();
                       
                        
                        partitaView.textField_avvisi.setText(" INIZIO FASE DI ESECUZIONE ! ");
                        
                        rv.play(semp, true);
                        semp.acquire();
                        
                        // robotView.setText(ATTIVAZIONE ROBODROMO).......
                        partitaView.textField_avvisi.setText(" INIZIO ATTIVAZIONE ROBODROMO ! ");
                         
                        rv.addPause(1500);
                        rv.play(semp,true);
                        semp.acquire();
                        
                        rv.isBucoNeroOrFuori(listRobot,partitaView);
                        rv.play(semp, true);
                        semp.acquire();
                        
                        rv.relocateRobotDistrutti(listRobot,partitaView);
                        
                        
                        for(RobotMarker robot:listRobot){
                            if(robot.getOwner().equals(robotsOwner[0].getOwner())) {
                                
                                System.err.println("DEBUG : salute "+robot.getSalute());
                                System.err.println("DEBUG : vita "+robot.getVita());
                                
                                partitaView.lblPuntiSaluteNumber.setText(""+robot.getSalute());  
                                partitaView.lblPuntiVitaNumber.setText(""+robot.getVita());
                                if(robot.getVita()==0) {
                                    gameOver=true;
                                    megalovania.stop();
                                }
                                                                
                                if(robot.getCheckpoint().getNumberChekpoint()==2)
                                    partitaView.lblChekpointNumber.setText("1 , 2");
                                else if(robot.getCheckpoint().getNumberChekpoint()==1)
                                    partitaView.lblChekpointNumber.setText("1 ");
                                else if(robot.getCheckpoint().getNumberChekpoint()==3)
                                    partitaView.lblChekpointNumber.setText("1 , 2, 3");
                                else
                                    partitaView.lblChekpointNumber.setText("");
                            }
                        }
                        
                        
                        
                        
                        rv.cleanListRobotDistrutti(listRobot,registriPlayers,conn);
                        
                        rv.activateBeltExpress(listRobot);
                        rv.play(semp,true);
                        semp.acquire();
                          
                        rv.isBeltRotation(listRobot);
                        rv.play(semp,true);
                        semp.acquire();
                        
                        rv.activateBelt(listRobot);
                        rv.play(semp,true);
                        semp.acquire();
                        
                        rv.isBeltRotation(listRobot);
                        rv.play(semp,true);
                        semp.acquire();
                        
                        rv.activateRotationBelt(listRobot);
                        rv.play(semp,true);
                        semp.acquire();
                       
                        partitaView.textField_avvisi.setText(" INIZIO ATTIVAZIONE LASER ! ");
                        
                        rv.addPause(1500);
                        rv.play(semp,true);
                        semp.acquire();
                        
                        
                         // robotView.setText(ATTIVAZIONE LASER).......
                         
                        rv.activateLaser(listRobot,partitaView);
                        rv.addHideLaser();
                        rv.play(semp,true);
                        semp.acquire();
                        
                        partitaView.textField_avvisi.setText(" INIZIO ATTIVAZIONE CHECKPOINT ! ");
                        
                        rv.addPause(1500);
                        rv.play(semp,true);
                        semp.acquire();
                        
                        rv.activateCheckpoint(listRobot,partitaView);
                        
                        
                        
                       
                        
                        for(RobotMarker r:listRobot) {
                            if(r.getCheckpoint().getNumberChekpoint()==numberCheckpoint) {
                                System.out.println("Ha vinto ! "+r.getOwner());
                                // JOPtionPAne????
                                break;
                            }
                        }
                        
                        partitaView.textField_avvisi.setText(" TERMINE FASE DI  ESECUZIONE  ! ");
                        
                        rv.addPause(1500);
                        rv.play(semp,true);
                        semp.acquire();
                        
                        rv.checkSaluteRobot(listRobot,partitaView);
                        
                        
                        
                        for(RobotMarker robot:listRobot){
                            if(robot.getOwner().equals(robotsOwner[0].getOwner())) {
                                
                                System.err.println("DEBUG : salute "+robot.getSalute());
                                System.err.println("DEBUG : vita "+robot.getVita());
                                
                                partitaView.lblPuntiSaluteNumber.setText(""+robot.getSalute());  
                                partitaView.lblPuntiVitaNumber.setText(""+robot.getVita());
                                
                                if(robot.getVita()==0) {
                                    gameOver=true;
                                    megalovania.close();
                                }
                                    
                                                                
                                if(robot.getCheckpoint().getNumberChekpoint()==2)
                                    partitaView.lblChekpointNumber.setText("1 , 2");
                                else if(robot.getCheckpoint().getNumberChekpoint()==1)
                                    partitaView.lblChekpointNumber.setText("1 ");
                                else if(robot.getCheckpoint().getNumberChekpoint()==3)
                                    partitaView.lblChekpointNumber.setText("1 , 2, 3");
                                else
                                    partitaView.lblChekpointNumber.setText("");
                            }
                        }
                        
                        rv.cleanListRobotDistrutti(listRobot,registriPlayers,conn);
                        
                        
                        i++;
                   
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Match.class.getName()).log(Level.SEVERE, null, ex);
                    }
                
                  }
                    
                    
                    partitaView.textField_avvisi.setText(" FINE MANCHE ! ");
                    
                    rv.addPause(1500);
                    rv.play(semp,true);
                    try {
                        semp.acquire();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    rv.effettiRiparazioniCheckpoint(listRobot,partitaView);
                    
                    
                    for(RobotMarker robot:listRobot){
                        if(robot.getOwner().equals(robotsOwner[0].getOwner())) {
                            
                            partitaView.lblPuntiSaluteNumber.setText(""+robot.getSalute());  
                            partitaView.lblPuntiVitaNumber.setText(""+robot.getVita());
                            
                             if(robot.getVita()==0) {
                                    gameOver=true;
                                    megalovania.close();
                              }
                            
                            if(robot.getCheckpoint().getNumberChekpoint()==2)
                                partitaView.lblChekpointNumber.setText("1 , 2");
                            else if(robot.getCheckpoint().getNumberChekpoint()==1)
                                partitaView.lblChekpointNumber.setText("1 ");
                            else if(robot.getCheckpoint().getNumberChekpoint()==3)
                                partitaView.lblChekpointNumber.setText("1 , 2, 3");
                            else
                                partitaView.lblChekpointNumber.setText("");
                        }
                    }
                    
                    
                    for(int k=0;k<5;k++) {
                        if(!(partitaView.registriRobot[k].isBlocked())) {
                            switch(k){
                                    case 0:
                                        partitaView.iconRegistro1.setIcon(null);
                                        partitaView.iconRegistro1.setEnabled(false);
                                        partitaView.label.setText("");
                                        partitaView.registriRobot[k].removeIstruzione();
                                        break;
                                    case 1:
                                        partitaView.iconRegistro2.setIcon(null);
                                        partitaView.iconRegistro2.setEnabled(false);
                                        partitaView.label_1.setText("");
                                        partitaView.registriRobot[k].removeIstruzione();
                                        break;
                                        
                                    case 2:
                                        partitaView.iconRegistro3.setIcon(null);
                                        partitaView.iconRegistro3.setEnabled(false);
                                        partitaView.label_2.setText("");
                                        partitaView.registriRobot[k].removeIstruzione();
                                        break;
                                        
                                    case 3:
                                        partitaView.iconRegistro4.setIcon(null);
                                        partitaView.iconRegistro4.setEnabled(false);
                                        partitaView.label_3.setText("");
                                        partitaView.registriRobot[k].removeIstruzione();
                                        break;
                                        
                                    case 4:
                                        partitaView.iconRegistro5.setIcon(null);
                                        partitaView.iconRegistro5.setEnabled(false);
                                        partitaView.label_4.setText("");
                                        partitaView.registriRobot[k].removeIstruzione();
                                        break;
                            }
                        }else {
                            break;
                        }
                            
                    }
                    
                    System.out.println("CAPACITA POOL RIMAMENTE GIOCATORE "+partitaView.poolGicatoreListModel.getSize());
                    
                    
                    for(RobotMarker r:listRobot) {
                            if(r.getOwner().equals(robotsOwner[0].getOwner())) {
                                try {
                                    Message reply = new Message(Match.MatchRegeneratePool);

                                    Object[] parameters = new Object[3];
                                    parameters[0] = r.getOwner();
                                    parameters[1] = r.getSalute();
                                    parameters[2] =partitaView.poolGicatoreListModel.getSize();
                                    reply.setParameters(parameters);

                                    conn.sendMessage(reply);
                                } catch (PartnerShutDownException ex) {
                                    Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                    
                    
                    }
                    
                    
               }else if(msg.getName().equals(Match.MatchRegeneratePool)) {
                   
                   DefaultListModel<Istruzione> listPool=(DefaultListModel<Istruzione>)msg.getParameter(0);
                   
                   for(int i=0;i<listPool.getSize();i++) {
                       partitaView.poolGicatoreListModel.addElement(listPool.get(i));
                   }
                   
                   partitaView.inEsecuzione=false;
                   
                   
                   
                    for(RobotMarker r:listRobot) {
                            if(r.getOwner().equals(robotsOwner[0].getOwner())) {
                                partitaView.resetRegistri(r.getSalute());
                            }
                    }
                   
                   megalovania.close();
                   
                   playDecision();
                   
                   partitaView.textField_avvisi.setText(" INIZIO FASE DI PROGRAMMAZIONE ! ");
                   
               }
                        
            }
            
            public void playDecision() {
                new Thread() {
                    public void run() {


                           try {

                               File file = new File(pathDecision);
                               decision = AudioSystem.getClip();
                               decision.open(AudioSystem.getAudioInputStream(file));
                               decision.loop(Clip.LOOP_CONTINUOUSLY);
                               
                           } catch (Exception e) {
                               System.out.println(e.getMessage());
                           }


                   }
                }.start();
               
        
            }
            
            public void playMegalovania(){
                new Thread() {
                    public void run() {


                           try {

                               File file = new File(pathMegalovania);
                               megalovania = AudioSystem.getClip();
                               megalovania.open(AudioSystem.getAudioInputStream(file));
                               megalovania.loop(Clip.LOOP_CONTINUOUSLY);

                           } catch (Exception e) {
                               System.out.println(e.getMessage());
                           }


                   }
                }.start();
                
                
                
            }
            
             public  void playFX(String path) {
                new Thread() {
                     public void run() {
                        try {
                            File file = new File(path);
                            Clip clip = AudioSystem.getClip();
                            clip.open(AudioSystem.getAudioInputStream(file));
                            clip.start();
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }.start();
            }
             
             
            
}
