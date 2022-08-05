package Partita;

import Partita.PartitaApp;
import connection.Connection;
import connection.Message;
import connection.MessageObserver;
import connection.PartnerShutDownException;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;

import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JSeparator;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import Utility.Istruzione;
import Utility.ListPartitaRenderer;
import Utility.ListRobotRenderer;
import robogp.matchmanager.Match;
import Utility.MyJTextPane;
import static Allenamento.AllenamentoApp.playFX;
import java.util.Observable;
import java.util.Observer;
import javax.imageio.ImageIO;
import robogp.robodrome.view.RobodromeView;

/**
 *
 * @author John Hundred
 */
public class PartitaView extends JFrame implements Observer {
      
	private Dimension ss = Toolkit.getDefaultToolkit ().getScreenSize ();
	public JLabel lblPs;
	public JLabel lblPv;
	public JLabel lblChekpointNumber;
        Dimension frameSize = new Dimension ( 450, 280 );
	public JTextField textField_avvisi;
        public JLabel iconRegistro1;
        public JLabel iconRegistro2;
        public JLabel iconRegistro3;
        public JLabel iconRegistro4;
        public JLabel iconRegistro5;
        public JList listMano;
        public JLabel label;
        public JLabel label_1;
        public JLabel label_2;
        public JLabel label_3;
        public JLabel label_4;
        public RegistroPartita [] registriRobot;
        public boolean inEsecuzione=false;
        public DefaultListModel <Istruzione> poolGicatoreListModel;
        private Font myFont;
        private Font myFont2;
        public MyJTextPane textArea;
        private String pathReloadFX="sound/ReloadFX.wav";
        private String pathDeleteFX="sound/DeleteFX.wav";
        private String pathSelectionFX="sound/SelectSoundFX.wav";
        private RobotMarker[] robotOwner;
        public JLabel lblPuntiSaluteNumber;
	public JLabel lblPuntiVitaNumber;
        public Style style;
        public Connection conn;
        public ArrayList<RobotMarker> robots;
        public PartitaController control;
        
		
		
	
	public PartitaView(RobodromeView rv,ArrayList<RobotMarker> listRobot,
                DefaultListModel<Istruzione> listPool,RobotMarker[] robotsOwner,
                Connection conn) {
            initialize(rv,listRobot,listPool,robotsOwner,conn);
	}

	
	private void initialize(RobodromeView rv,ArrayList<RobotMarker> robots,
                DefaultListModel <Istruzione> listPool,RobotMarker[] robotsOwner,
                Connection conn) {
            
               this.conn = conn;
               this.robots = robots;
                
               Image imageLauncher = null;
               try {
                     imageLauncher =ImageIO.read(new File(("img/iconLauncher.jpg")));
               }catch(IOException e) {
                     System.err.println(e.getMessage());
               }
               setIconImage(imageLauncher);
               
                
                robotOwner=robotsOwner;
                PartitaController controller = new PartitaController(robotsOwner[0]);
                control = controller;
                try {
                myFont=Font.createFont(Font.TRUETYPE_FONT,
                            new File("font/overseer.otf"));
                
                myFont2=Font.createFont(Font.TRUETYPE_FONT,
                            new File("font/overseer.otf"));
                
                    } catch (FontFormatException ex) {
                        Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(PartitaApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                myFont = myFont.deriveFont(Font.BOLD,28);
                myFont2 = myFont.deriveFont(Font.BOLD,18);
                
                lblPuntiSaluteNumber  = new JLabel("10");
                lblPuntiSaluteNumber.setFont(myFont2);
                
                lblPuntiVitaNumber= new JLabel("3");
                lblPuntiVitaNumber.setFont(myFont2);
		
                poolGicatoreListModel=listPool;
                registriRobot= new RegistroPartita[5];
                for(int i=0;i<5;i++)
                    registriRobot[i]=new RegistroPartita();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panelGenerale = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panelGenerale, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panelGenerale, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
		);
		
		JPanel panel_tabellone = new JPanel();
		panel_tabellone.setBackground(Color.BLACK);
                
                panel_tabellone.add(rv);
		
		JPanel panel_controllo = new JPanel();
		
		
		JTabbedPane tabRobotState = new JTabbedPane(JTabbedPane.BOTTOM);
		
		
		JPanel panel_robot = new JPanel();
		
		JPanel panel_log = new JPanel();
		panel_log.setBackground(Color.WHITE);
                
		
		JPanel panel_avvisi = new JPanel();
		panel_avvisi.setBackground(Color.WHITE);
                            
		GroupLayout gl_panelGenerale = new GroupLayout(panelGenerale);
		gl_panelGenerale.setHorizontalGroup(
			gl_panelGenerale.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelGenerale.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelGenerale.createParallelGroup(Alignment.LEADING)
                                                .addComponent(control, GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
						.addComponent(panel_tabellone, GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
						.addComponent(panel_avvisi, GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
						
						.addComponent(tabRobotState))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelGenerale.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_robot, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
						.addComponent(panel_log, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panelGenerale.setVerticalGroup(
			gl_panelGenerale.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelGenerale.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelGenerale.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelGenerale.createSequentialGroup()
							.addComponent(panel_robot, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_log, 0, 0, Short.MAX_VALUE))
						.addGroup(gl_panelGenerale.createSequentialGroup()
							.addComponent(panel_avvisi, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_tabellone, GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(control, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tabRobotState, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panel_log = new GroupLayout(panel_log);
		gl_panel_log.setHorizontalGroup(
			gl_panel_log.createParallelGroup(Alignment.TRAILING)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
		);
		gl_panel_log.setVerticalGroup(
			gl_panel_log.createParallelGroup(Alignment.TRAILING)
				.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
		);
		
                textArea = new MyJTextPane();
		

                style = textArea.addStyle("Style", null);
                StyleConstants.setForeground(style, Color.BLACK);
		
                textArea.setEditable(false);
                textArea.setFont(myFont2);
                
                
		scrollPane_1.setViewportView(textArea);
		panel_log.setLayout(gl_panel_log);
                
                
                
               JTabbedPane tabPool_Robot = new JTabbedPane(JTabbedPane.TOP);
                
		
		
		GroupLayout gl_panel_robot = new GroupLayout(panel_robot);
		gl_panel_robot.setHorizontalGroup(
			gl_panel_robot.createParallelGroup(Alignment.LEADING)
				.addComponent(tabPool_Robot, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
		);
		gl_panel_robot.setVerticalGroup(
			gl_panel_robot.createParallelGroup(Alignment.LEADING)
				.addComponent(tabPool_Robot, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
		);
		
		
		
		
		
		JList listRobot = new JList();
		tabPool_Robot.addTab("GIOCATORI", null, listRobot, null);
		
		listMano = new JList();
		tabPool_Robot.addTab("POOL", null, listMano, null);
		panel_robot.setLayout(gl_panel_robot);
                
                listMano.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                         boolean enabled = (listMano.getSelectedIndex() >= 0);
                         if(!(inEsecuzione)) {
                            
                         
                            if(enabled){
                                playFX(pathSelectionFX);
                                if(poolGicatoreListModel.get(listMano.getSelectedIndex()).isSelected()) {
                                   control.btnRimuovi.setEnabled(true);
                                   control.btnInserisci.setEnabled(false);
                                }else {
                                    control.btnRimuovi.setEnabled(false);
                                    control.btnInserisci.setEnabled(enabled);
                                }
                           }else {
                                control.btnRimuovi.setEnabled(false);
                           }
                        }
                    }
                });
                
                DefaultListModel listModelRobot= new DefaultListModel();
               
                listRobot.setLayoutOrientation(JList.VERTICAL);
                listRobot.setVisibleRowCount(-1);
                
                
                JScrollPane scrollPane = new JScrollPane(listRobot);
		tabPool_Robot.addTab("GIOCATORI", null, scrollPane, null);
		
		JScrollPane scrollPane_2 = new JScrollPane(listMano);
		tabPool_Robot.addTab("POOL", null, scrollPane_2, null);
		panel_robot.setLayout(gl_panel_robot);
                
                /* !!!!!!!!!!!!ATTENZIONE QUESTO E SOLO PER UN ROBOT !!!!!!!!!!!!!!!!!!!!!!!*/
                
                listRobot.setCellRenderer(new ListRobotRenderer(robotsOwner[0].getOwner()));
		
		for(RobotMarker r: robots) {
                    listModelRobot.addElement(r);
                }
                
                listMano.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                listMano.setLayoutOrientation(JList.VERTICAL);
               
                listMano.setVisibleRowCount(-1);
                listMano.setCellRenderer(new ListPartitaRenderer());
                listMano.clearSelection();
		
		
		listRobot.setModel(listModelRobot);
				
		
		listMano.setModel(poolGicatoreListModel);
		
		JPanel panel_controlloStato = new JPanel();
		panel_controlloStato.setBackground(Color.YELLOW);
		
		JPanel panel_controlloRegistro = new JPanel();
		panel_controlloRegistro.setBackground(Color.DARK_GRAY);
                
                
		GroupLayout gl_panel_controllo = new GroupLayout(panel_controllo);
		gl_panel_controllo.setHorizontalGroup(
			gl_panel_controllo.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_controllo.createSequentialGroup()
					.addGap(6)
					.addComponent(panel_controlloStato, GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_controlloRegistro, GroupLayout.PREFERRED_SIZE, 390, Short.MAX_VALUE))
		);
		gl_panel_controllo.setVerticalGroup(
			gl_panel_controllo.createParallelGroup(Alignment.TRAILING)
				.addComponent(panel_controlloStato, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
				.addComponent(panel_controlloRegistro, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 136, Short.MAX_VALUE)
		);
		
		lblPs = new JLabel("PS : ");
		
		lblPv = new JLabel("PV : ");
		
		lblPuntiSaluteNumber = new JLabel("10");
		
		lblPuntiVitaNumber = new JLabel("3");
		
		JLabel lblChekpoint = new JLabel("CHEKPOINT :");
		
		lblChekpointNumber = new JLabel(" ");
                lblChekpointNumber .setFont(myFont2);
                
		GroupLayout gl_panel_controlloStato = new GroupLayout(panel_controlloStato);
		gl_panel_controlloStato.setHorizontalGroup(
			gl_panel_controlloStato.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_controlloStato.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_controlloStato.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_controlloStato.createSequentialGroup()
							.addGroup(gl_panel_controlloStato.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(lblPv, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblPs, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_controlloStato.createParallelGroup(Alignment.LEADING)
								.addComponent(lblPuntiSaluteNumber, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
								.addComponent(lblPuntiVitaNumber, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel_controlloStato.createSequentialGroup()
							.addComponent(lblChekpoint)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblChekpointNumber, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel_controlloStato.setVerticalGroup(
			gl_panel_controlloStato.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_controlloStato.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_controlloStato.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPs)
						.addComponent(lblPuntiSaluteNumber, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_controlloStato.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPv)
						.addComponent(lblPuntiVitaNumber, GroupLayout.PREFERRED_SIZE, 11, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_controlloStato.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblChekpoint)
						.addComponent(lblChekpointNumber))
					.addContainerGap(15, Short.MAX_VALUE))
		);
		panel_controlloStato.setLayout(gl_panel_controlloStato);
		
		
               
		iconRegistro1 = new JLabel();
                iconRegistro1.setEnabled(false);
		
		iconRegistro2 = new JLabel();
		iconRegistro2.setEnabled(false);
		
		iconRegistro3 = new JLabel();
		iconRegistro3.setEnabled(false);
                
		iconRegistro4 = new JLabel();
                iconRegistro4.setEnabled(false);
		
		iconRegistro5 = new JLabel();
                iconRegistro5.setEnabled(false);
		
		JLabel label_registro1 = new JLabel("REGISTRO 1");
		label_registro1.setForeground(Color.WHITE);
		
		JLabel label_registro2 = new JLabel("REGISTRO 2");
		label_registro2.setForeground(Color.WHITE);
		
		JLabel label_registro3 = new JLabel("REGISTRO 3");
		label_registro3.setForeground(Color.WHITE);
		
		JLabel label_registro4 = new JLabel("REGISTRO 4");
		label_registro4.setForeground(Color.WHITE);
		
		JLabel label_registro5 = new JLabel("REGISTRO 5");
		label_registro5.setForeground(Color.WHITE);
                
                label = new JLabel(" ");
		label.setForeground(Color.WHITE);
		
		label_1 = new JLabel(" ");
		label_1.setForeground(Color.WHITE);
		
		label_2 = new JLabel(" ");
		label_2.setForeground(Color.WHITE);
		
		label_3 = new JLabel(" ");
		label_3.setForeground(Color.WHITE);
                
                label_4 = new JLabel(" ");
                label_4.setForeground(Color.WHITE);
                
		GroupLayout gl_panel_controlloRegistro = new GroupLayout(panel_controlloRegistro);
		gl_panel_controlloRegistro.setHorizontalGroup(
			gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
					.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(label_registro1)
								.addComponent(iconRegistro1, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
									.addComponent(label_registro2))
								.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(iconRegistro2, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
							.addGap(40)
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
							.addGap(53)
							.addComponent(label_1, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(iconRegistro3, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
							.addGap(37)
							.addComponent(label_2, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
							.addGap(18)
							.addComponent(label_registro3)))
					.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING)
								.addComponent(iconRegistro4, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
								.addComponent(label_registro4)))
						.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
							.addGap(33)
							.addComponent(label_3, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)))
					.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.TRAILING)
								.addComponent(iconRegistro5, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
								.addComponent(label_registro5)))
						.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
							.addGap(27)
							.addComponent(label_4, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel_controlloRegistro.setVerticalGroup(
			gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
					.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.BASELINE)
							.addComponent(label_registro2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(label_registro3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(label_registro4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(label_registro5, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(label_registro1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_controlloRegistro.createSequentialGroup()
							.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING)
								.addComponent(iconRegistro2, GroupLayout.PREFERRED_SIZE, 85, Short.MAX_VALUE)
								.addComponent(iconRegistro1, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
								.addComponent(iconRegistro3, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 85, Short.MAX_VALUE)
								.addComponent(iconRegistro4, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.BASELINE)
									.addComponent(label)
									.addComponent(label_1)
									.addComponent(label_2))
								.addGroup(gl_panel_controlloRegistro.createParallelGroup(Alignment.BASELINE)
									.addComponent(label_3)
									.addComponent(label_4))))
						.addComponent(iconRegistro5, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
					.addGap(71))
		);
		panel_controlloRegistro.setLayout(gl_panel_controlloRegistro);
		panel_controllo.setLayout(gl_panel_controllo);
		
		textField_avvisi = new JTextField();
		textField_avvisi.setColumns(10);
                textField_avvisi.setEditable(false);
                textField_avvisi.setHorizontalAlignment(JTextField.CENTER);
                textField_avvisi.setForeground(new Color(220, 20, 60));
		textField_avvisi.setFont(myFont);
                
                
		GroupLayout gl_panel_avvisi = new GroupLayout(panel_avvisi);
		gl_panel_avvisi.setHorizontalGroup(
			gl_panel_avvisi.createParallelGroup(Alignment.TRAILING)
				.addComponent(textField_avvisi, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
		);
		gl_panel_avvisi.setVerticalGroup(
			gl_panel_avvisi.createParallelGroup(Alignment.TRAILING)
				.addComponent(textField_avvisi, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
		);
		panel_avvisi.setLayout(gl_panel_avvisi);
		panelGenerale.setLayout(gl_panelGenerale);
		
		int i=1;
		for(RobotMarker r:robotsOwner) {
                    if(i<2)
                        tabRobotState.add("Robot "+i, panel_controllo);
                    else
                        tabRobotState.add("Robot "+i,null);
                    
                    i++;
                    
                }
		
		
		
		
		
		 tabRobotState.addChangeListener(new ChangeListener() {
		        public void stateChanged(ChangeEvent e) {
		            System.out.println("Tab: " + tabRobotState.getSelectedIndex());
		            
		            if(tabRobotState.getSelectedIndex()==0) {
		            	lblPuntiSaluteNumber.setText("10");
		            	lblPuntiVitaNumber.setText("3");
		            	lblChekpointNumber.setText("1,2,3,4");
		            	
		            }else if(tabRobotState.getSelectedIndex()==1) {
		            	lblPuntiSaluteNumber.setText("3");
		            	lblPuntiVitaNumber.setText("2");
		            	lblChekpointNumber.setText("1,2,3,4,5,6,7,8");
		            }else {
		            	lblPuntiSaluteNumber.setText("7");
		            	lblPuntiVitaNumber.setText("1");
		            	lblChekpointNumber.setText("1,2");
		            }
		        }
		    });
		
		
		
		
		
		getContentPane().setLayout(groupLayout);
		
		
                GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice 	vc=ge.getDefaultScreenDevice();
                
		vc.setFullScreenWindow(this);
		setResizable(false);
		
		setVisible(true);
		
	
	}
        
        public void resetRegistri(int puntiSalute) {
            
            
            registriRobot= new RegistroPartita[5];
                for(int i=0;i<5;i++)
                    registriRobot[i]=new RegistroPartita();
                
            switch(puntiSalute) {
                
                case 5:
                    registriRobot[4].block();
                    textArea.append("Il robot "+ robotOwner[0].getName()+
                                    " di "+robotOwner[0].getOwner()+" ha il 5 rregistro bloccato ! "+"\n",style);
                    break;
               
                case 4:
                    registriRobot[4].block();
                    registriRobot[3].block();
                    textArea.append("Il robot "+ robotOwner[0].getName()+
                                    " di "+robotOwner[0].getOwner()+" ha il 5 e il 4 rregistro bloccato ! "+"\n",style);
                    break;
                    
                case 3:
                    registriRobot[4].block();
                    registriRobot[3].block();
                    registriRobot[2].block();
                    textArea.append("Il robot "+ robotOwner[0].getName()+
                                    " di "+robotOwner[0].getOwner()+" ha il 5,4 e il 3 rregistro bloccato ! "+"\n",style);
                    break;
                    
                case 2:
                    registriRobot[4].block();
                    registriRobot[3].block();
                    registriRobot[2].block();
                    registriRobot[1].block();
                    textArea.append("Il robot "+ robotOwner[0].getName()+
                                    " di "+robotOwner[0].getOwner()+" ha il 5 , 4, 3 e il 2 rregistro bloccato ! "+"\n",style);
                    break;
                    
                case 1:
                    registriRobot[4].block();
                    registriRobot[3].block();
                    registriRobot[2].block();
                    registriRobot[1].block();
                    registriRobot[0].block();
                    textArea.append("Il robot "+ robotOwner[0].getName()+
                                    " di "+robotOwner[0].getOwner()+" ha tutti i registri  bloccati ! "+"\n",style);
                    control.confermaButton.setEnabled(true);
                    break;
                
                         
            }
            
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
        
    public void aggiungiIstruzione(){
        System.out.println("ok");
        boolean enabled = (listMano.getSelectedIndex() >= 0);
                       if(enabled) {
                        boolean full=true;
                        if(!(inEsecuzione)) {
                            if(listMano!=null) {
                                    
                                    if(registriRobot[0].isBlocked()) {
                                           control.confermaButton.setEnabled(true);
                                           control.btnInserisci.setEnabled(false);


                                     }else if((!(iconRegistro1.isEnabled()))&&
                                           !(registriRobot[0].isBlocked())) {
                                         
                                       playFX(pathReloadFX);
                                       iconRegistro1.setEnabled(true);
                                       iconRegistro1.setIcon(
                                               poolGicatoreListModel.get(listMano.getSelectedIndex()).getImage());
                                       label.setText(""+poolGicatoreListModel.get(listMano.getSelectedIndex()).getPriorita());
                                       poolGicatoreListModel.get(listMano.getSelectedIndex()).setSelected(true);
                                       registriRobot[0].setIstruzione(poolGicatoreListModel.get(listMano.getSelectedIndex()));
                                       System.out.println("Registro 1 "+registriRobot[0].getIstruzione());
                                       
                                       if(registriRobot[1].isBlocked()) {
                                           control.confermaButton.setEnabled(true);
                                           control.btnInserisci.setEnabled(false);
                                           full=true;
                                       }

                                       for(int i=0;i<5;i++) {
                                           if(!(registriRobot[i].isNotEmpty())) {
                                               full=false;
                                               break;
                                           }
                                       }

                                       if(full) {
                                           control.confermaButton.setEnabled(true);
                                           control.btnInserisci.setEnabled(false);
                                       }

                                       listMano.clearSelection();


                                   }else if((!(iconRegistro2.isEnabled())) &&
                                           !(registriRobot[1].isBlocked())) {
                                       
                                       playFX(pathReloadFX);
                                       iconRegistro2.setEnabled(true);
                                       iconRegistro2.setIcon(
                                               poolGicatoreListModel.get(listMano.getSelectedIndex()).getImage());
                                       label_1.setText(""+poolGicatoreListModel.get(listMano.getSelectedIndex()).getPriorita());
                                       poolGicatoreListModel.get(listMano.getSelectedIndex()).setSelected(true);

                                       registriRobot[1].setIstruzione(poolGicatoreListModel.get(listMano.getSelectedIndex()));
                                       System.out.println("Registro 2 "+registriRobot[1].getIstruzione());
                                       
                                       if(registriRobot[2].isBlocked()) {
                                           control.confermaButton.setEnabled(true);
                                           control.btnInserisci.setEnabled(false);
                                           full=true;
                                       }


                                       for(int i=0;i<5;i++) {
                                           if(!(registriRobot[i].isNotEmpty())) {
                                               full=false;
                                               break;
                                           }
                                       }

                                       if(full) {
                                           control.confermaButton.setEnabled(true);
                                           control.btnInserisci.setEnabled(false);
                                       }



                                       listMano.clearSelection();

                                   }else if((!(iconRegistro3.isEnabled()))&&
                                           !(registriRobot[2].isBlocked())) {
                                       
                                       playFX(pathReloadFX);
                                       iconRegistro3.setEnabled(true);
                                       iconRegistro3.setIcon(
                                             poolGicatoreListModel.get(listMano.getSelectedIndex()).getImage());
                                       label_2.setText(""+poolGicatoreListModel.get(listMano.getSelectedIndex()).getPriorita());
                                       poolGicatoreListModel.get(listMano.getSelectedIndex()).setSelected(true);

                                       registriRobot[2].setIstruzione(poolGicatoreListModel.get(listMano.getSelectedIndex()));
                                       System.out.println("Registro 3 "+registriRobot[2].getIstruzione());

                                       if(registriRobot[3].isBlocked()) {
                                           control.confermaButton.setEnabled(true);
                                           control.btnInserisci.setEnabled(false);
                                           full=true;
                                       }


                                       for(int i=0;i<5;i++) {
                                           if(!(registriRobot[i].isNotEmpty())) {
                                               full=false;
                                               break;
                                           }
                                       }

                                       if(full) {
                                           control.confermaButton.setEnabled(true);
                                           control.btnInserisci.setEnabled(false);
                                       }





                                       listMano.clearSelection();

                                   }else if((!(iconRegistro4.isEnabled())) &&
                                           !(registriRobot[3].isBlocked())) {
                                       
                                       playFX(pathReloadFX);
                                       iconRegistro4.setEnabled(true);
                                       iconRegistro4.setIcon(
                                             poolGicatoreListModel.get(listMano.getSelectedIndex()).getImage());
                                       label_3.setText(""+poolGicatoreListModel.get(listMano.getSelectedIndex()).getPriorita());
                                       poolGicatoreListModel.get(listMano.getSelectedIndex()).setSelected(true);

                                       registriRobot[3].setIstruzione(poolGicatoreListModel.get(listMano.getSelectedIndex()));
                                       System.out.println("Registro 4 "+registriRobot[3].getIstruzione());

                                       if(registriRobot[4].isBlocked()) {
                                           control.confermaButton.setEnabled(true);
                                           control.btnInserisci.setEnabled(false);
                                           full=true;
                                       }

                                       for(int i=0;i<5;i++) {
                                           if(!(registriRobot[i].isNotEmpty())) {
                                               full=false;
                                               break;
                                           }
                                       }

                                       if(full) {
                                           control.confermaButton.setEnabled(true);
                                           control.btnInserisci.setEnabled(false);
                                       }





                                       listMano.clearSelection();

                                   }else if((!(iconRegistro5.isEnabled())) &&
                                           !(registriRobot[4].isBlocked())) {
                                       
                                        playFX(pathReloadFX);
                                        iconRegistro5.setEnabled(true);
                                        iconRegistro5.setIcon(
                                               poolGicatoreListModel.get(listMano.getSelectedIndex()).getImage());
                                        label_4.setText(""+poolGicatoreListModel.get(listMano.getSelectedIndex()).getPriorita());
                                        poolGicatoreListModel.get(listMano.getSelectedIndex()).setSelected(true);

                                        registriRobot[4].setIstruzione(poolGicatoreListModel.get(listMano.getSelectedIndex()));
                                        System.out.println("Registro 4 "+registriRobot[4].getIstruzione());
                                        
                                        control.confermaButton.setEnabled(true);
                                        control.btnInserisci.setEnabled(false);
                                        listMano.clearSelection();
                                   }
                             }
                            }
                        
                        }
    }
    
    public void rimuoviIstruzione(){
        boolean enabled = (listMano.getSelectedIndex() >= 0);
                        if(enabled) {
                           if(poolGicatoreListModel.get(listMano.getSelectedIndex()).isSelected()) {
                                Istruzione istruzione=poolGicatoreListModel.get(listMano.getSelectedIndex());
                                for(int i=0;i<5;i++) {
                                    if(registriRobot[i]!=null && !(registriRobot[i].isBlocked())) {
                                        if(registriRobot[i].getIstruzione()!=null){
                                            if(registriRobot[i].getIstruzione().equals(istruzione)) {
                                                switch(i) {
                                                     case 0:
                                                        
                                                        playFX(pathDeleteFX);
                                                        iconRegistro1.setIcon(null);
                                                        iconRegistro1.setEnabled(false);
                                                        label.setText("");
                                                        poolGicatoreListModel.get(listMano.getSelectedIndex()).setSelected(false);
                                                        registriRobot[i].removeIstruzione();
                                                        control.confermaButton.setEnabled(false);
                                                        listMano.clearSelection();
                                                        
                                                        break;
                                                     case 1:
                                                         
                                                        playFX(pathDeleteFX);
                                                        iconRegistro2.setIcon(null);
                                                        iconRegistro2.setEnabled(false);
                                                        label_1.setText("");
                                                        poolGicatoreListModel.get(listMano.getSelectedIndex()).setSelected(false);
                                                        registriRobot[i].removeIstruzione();
                                                        control.confermaButton.setEnabled(false);
                                                        listMano.clearSelection();
                                                        break;
                                                     case 2:
                                                         
                                                        playFX(pathDeleteFX);
                                                        iconRegistro3.setIcon(null);
                                                        iconRegistro3.setEnabled(false);
                                                        label_2.setText("");
                                                        poolGicatoreListModel.get(listMano.getSelectedIndex()).setSelected(false);
                                                        registriRobot[i].removeIstruzione();
                                                        control.confermaButton.setEnabled(false);
                                                        listMano.clearSelection();
                                                        break;
                                                     case 3:
                                                         
                                                         playFX(pathDeleteFX);
                                                         iconRegistro4.setIcon(null);
                                                         iconRegistro4.setEnabled(false);
                                                         label_3.setText("");
                                                         poolGicatoreListModel.get(listMano.getSelectedIndex()).setSelected(false);
                                                         registriRobot[i].removeIstruzione();
                                                         control.confermaButton.setEnabled(false);
                                                         listMano.clearSelection();
                                                         break;
                                                     case 4:
                                                         
                                                         playFX(pathDeleteFX);
                                                         iconRegistro5.setIcon(null);
                                                         iconRegistro5.setEnabled(false);
                                                         label_4.setText("");
                                                         poolGicatoreListModel.get(listMano.getSelectedIndex()).setSelected(false);
                                                         registriRobot[i].removeIstruzione();
                                                         control.confermaButton.setEnabled(false);
                                                         listMano.clearSelection();
                                                         break;


                                                }
                                            }
                                        }
                                    }else {
                                        
                                    }
                                }
                           }
                       }
    }
    
    public void confermaProgrammazione(){
        for(int i=0;i<poolGicatoreListModel.getSize();) {
            if(poolGicatoreListModel.get(i).isSelected()){
                poolGicatoreListModel.remove(i);
                i=0;
            }else {
                i++;
            }
        }
                        
        try {
            for(int i=0;i<5;i++)
                System.out.println("Registro "+i+1+" :"+registriRobot[i].getIstruzione()+
                                        " Priorita" +registriRobot[i].getIstruzione().getPriorita() );
                            
                Message send = new Message(Match.MatchProgramMsg);
                            
                Object[] parameters = new Object[3];
                parameters[0] = robotOwner[0].getOwner();
                parameters[1] = registriRobot;
                parameters[2]=robots;
                            
                send.setParameters(parameters);
                            
                conn.sendMessage(send);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(PartitaView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        //
        if(arg.equals("aggiungi")){
            aggiungiIstruzione();
        }else if(arg.equals("rimuovi")){
            rimuoviIstruzione();
        }else {
            inEsecuzione=true;
            confermaProgrammazione();
        }
    }
        
}
