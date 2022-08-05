
package Allenamento;

import Utility.ListRenderer;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import Utility.Istruzione;
import robogp.matchmanager.RobotStatePanel;
import robogp.robodrome.Direction;
import robogp.robodrome.view.RobodromeView;


public class AllenamentoApp extends javax.swing.JFrame {

    public static AllenamentoApp singleInstance;
    private RobotStatePanel[] robotPanel;
    private JSplitPane splitPane;
    private JPanel panel;
    private JComboBox boxX;
    private JComboBox boxY;
    private JComboBox comboBox_1;
    private JTextArea textArea;
    private JComboBox comboBox_2;
    private JRadioButtonMenuItem rdbtnNewRadioButton_2;
    private JRadioButtonMenuItem radioButton;
    private JButton btnNewButton_3;
    private JButton avviaButton;
    private JButton btnAggiungiIstrz;
    private JButton btnNewButton;
    private JButton pausabutton;
    private Semaphore lock = new Semaphore(0);
    private Semaphore semp=new Semaphore(0);
    //private JButton btnUndo ;
   // private JComboBox comboBox ;
    static int count=1;
    private JList list;
    private JScrollPane scrollPane_1;
    private JPanel panel_6;
    private JList list_1;
    private JScrollPane scrollPane_2;
    private JButton btnNewButton_1;
    private Dimension ss = Toolkit.getDefaultToolkit ().getScreenSize ();
    Dimension frameSize = new Dimension ( 400, 450 );
    String pathPausa="img/pause.png";
    String pathAvvia="img/start.png";
    String pathUndo="img/undo.png";
    String pathStop="img/stop.png";
    String pathStartFX="sound/StartFX.wav";
    String pathStopFX="sound/StopFX.wav";
    String pathUndoFX="sound/UndoFX.wav";
    String pathPauseFX="sound/PauseFX.wav";
    String pathRobotMovementFX="sound/RobotMovementFX.wav";
    String pathReloadFX="sound/ReloadFX.wav";
    String pathDeleteFX="sound/DeleteFX.wav";
    String pathShiftFX="sound/ShiftFX.wav";
    public static String pathMusicBackGround="sound/Quartu.wav";
    String pathAggiungi="img/add.png";
    String pathRemove="img/remove.png";
    String pathShift="img/Shift.png";
    String pathShiftAvanti="img/shiftAvanti.png";
    String pathShiftDietro="img/ShiftDietro.jpg";
    String pathImgAllenamento="img/allenamentoImg.jpg";
    
    /**
     * Creates new form MatchManager
     */
    public AllenamentoApp() {
		
        initComponents();
        this.inizPartCtrl = AllenamentoController.getInstance();
       
    }

    public static AllenamentoApp getAppInstance() {
        return AllenamentoApp.singleInstance;
    }

    private final AllenamentoController inizPartCtrl;

   

    public AllenamentoController getIniziarePartitaController() {
        return this.inizPartCtrl;
    }

    
    
    private void initComponents() {

        initPanel = new javax.swing.JPanel();
        javax.swing.JPanel jPanel4 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel1 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel2 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        portField = new javax.swing.JTextField();
        javax.swing.JPanel jPanel3 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel2 = new javax.swing.JLabel();
        keyField = new javax.swing.JTextField();
        javax.swing.JPanel jPanel5 = new javax.swing.JPanel();
        initButton = new javax.swing.JButton();
        matchPanel = new javax.swing.JPanel();
        javax.swing.JPanel jPanel6 = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        javax.swing.JPanel jPanel7 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel10 = new javax.swing.JPanel();
        javax.swing.JPanel jPanel11 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel3 = new javax.swing.JLabel();
        robodromeCombo = new javax.swing.JComboBox<>();
        javax.swing.JPanel jPanel12 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel4 = new javax.swing.JLabel();
        endGameCombo = new javax.swing.JComboBox<>();
        javax.swing.JPanel jPanel13 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel5 = new javax.swing.JLabel();
        maxPlayersCombo = new javax.swing.JComboBox<>();
        javax.swing.JPanel jPanel14 = new javax.swing.JPanel();
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        nRobotsCombo = new javax.swing.JComboBox<>();
        upgradeCheck = new javax.swing.JCheckBox();
        playersPanel = new javax.swing.JPanel();
        javax.swing.JPanel jPanel8 = new javax.swing.JPanel();
        javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
        requestList = new javax.swing.JList<>();
        javax.swing.JPanel jPanel15 = new javax.swing.JPanel();
        acceptRequestButton = new javax.swing.JButton();
        rejectRequestButton = new javax.swing.JButton();
        robotRecapPanel = new javax.swing.JPanel();
        javax.swing.JPanel jPanel16 = new javax.swing.JPanel();
        startMatchButton = new javax.swing.JButton();
        cancelMatchButton = new javax.swing.JButton();
        ongoingMatchPanel = new javax.swing.JPanel();
        javax.swing.JLabel jLabel7 = new javax.swing.JLabel();
        
        
        
        
	BufferedImage buttonPausa=null;
	BufferedImage buttonAvvia=null;
	BufferedImage buttonUndo=null;
	BufferedImage buttonStop=null;
        BufferedImage buttonAggiungi=null;
	BufferedImage buttonRemove=null;
        BufferedImage buttonShift=null;
        BufferedImage buttonShiftAvanti=null;
        BufferedImage buttonShiftDietro=null;
		
	try {
            buttonPausa = ImageIO.read(new File(pathPausa));
            buttonAvvia=ImageIO.read(new File(pathAvvia));
            buttonUndo=ImageIO.read(new File(pathUndo));
            buttonStop=ImageIO.read(new File(pathStop));
            buttonAggiungi=ImageIO.read(new File(pathAggiungi));
            buttonRemove=ImageIO.read(new File(pathRemove));
            buttonShift=ImageIO.read(new File(pathShift));
            buttonShiftAvanti=ImageIO.read(new File(pathShiftAvanti));
            buttonShiftDietro=ImageIO.read(new File(pathShiftDietro));
			 
	} catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
	}
        

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Allenamento");
        setName("main frame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.CardLayout());
        
        
        
        initPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 1, 2, 1));
        initPanel.setLayout(new java.awt.BorderLayout());
        
        
        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JPanel panel20 = new JPanel();
        initPanel.add(panel20, BorderLayout.NORTH);
		
        JLabel lblBenvenutoNellaModalta = new JLabel("BENVENUTO NELLA MODALTA ALLENAMENTO");
        lblBenvenutoNellaModalta.setFont(new Font("Serif",Font.BOLD,16));
        panel20.add(lblBenvenutoNellaModalta);
		
        JPanel panel_16 = new JPanel();
        initPanel.add(panel_16, BorderLayout.CENTER);
	
        ImageIcon icon = new ImageIcon(pathImgAllenamento); 
        JLabel thumb = new JLabel();
        thumb.setIcon(icon);
        panel_16.add(thumb);
		
	JPanel panel_23 = new JPanel();
	initPanel.add(panel_23, BorderLayout.SOUTH);
		
	
	panel_23.add(initButton);

        /*
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

       

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel1.setText("BENVENUTO NELLA MODALITA ALLENAMENTO");
        */
        portField.setColumns(6);
		
	/*	
        jPanel1.add(jLabel1, java.awt.BorderLayout.CENTER);
		
		
        jPanel4.add(jPanel1);

        initPanel.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        */
        initButton.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        initButton.setText("Continua");
        initButton.setAlignmentX(0.5F);
        initButton.setMaximumSize(new java.awt.Dimension(122, 40));
        initButton.setMinimumSize(new java.awt.Dimension(122, 40));
        initButton.setPreferredSize(new java.awt.Dimension(122, 40));
        initButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initButtonActionPerformed(evt);
            }
        });
        
        /*
        jPanel5.add(initButton);

        initPanel.add(jPanel5, java.awt.BorderLayout.SOUTH);
        */

        getContentPane().add(initPanel, "init");

        matchPanel.setLayout(new java.awt.BorderLayout());

        createButton.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        createButton.setText("Avvia Allenamento");
        createButton.setAlignmentX(0.5F);
        createButton.setMaximumSize(new java.awt.Dimension(250, 40));
        createButton.setMinimumSize(new java.awt.Dimension(250, 40));
        createButton.setPreferredSize(new java.awt.Dimension(250, 40));
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
				
                try {
                    startMatchButtonActionPerformed(evt);
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(AllenamentoApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedAudioFileException ex) {
                    Logger.getLogger(AllenamentoApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(AllenamentoApp.class.getName()).log(Level.SEVERE, null, ex);
                }
				 
            }
        });
		
		
        jPanel6.add(createButton);

        matchPanel.add(jPanel6, java.awt.BorderLayout.SOUTH);

        jPanel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 5, 5, 5));

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.Y_AXIS));

        jPanel11.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel11.setAlignmentX(0.0F);
        jPanel11.setLayout(new java.awt.BorderLayout());

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel3.setText("Robodromo:");
        jPanel11.add(jLabel3, java.awt.BorderLayout.WEST);

        
        robodromeCombo.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        robodromeCombo.setPreferredSize(new java.awt.Dimension(200, 33));
        jPanel11.add(robodromeCombo, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel11);

        jPanel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 10, 5));
        jPanel12.setAlignmentX(0.0F);
        jPanel12.setLayout(new java.awt.BorderLayout());

		
        jLabel4.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel4.setText("Posizione Robot:");
        
        JLabel labelX=new JLabel("PoS X - POS Y: ");
        boxX= new JComboBox();
        boxX.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"0", "1", "2",
            "3","4","5","6","7","8","9","10","11","12","13","14","15"}));
       
        boxY=new JComboBox();
        boxY.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2",
            "3","4","5","6","7","8","9","10","11"}));
        
        jPanel12.add(jLabel4, java.awt.BorderLayout.NORTH);
        jPanel12.add(labelX,java.awt.BorderLayout.WEST);
        jPanel12.add(boxX,java.awt.BorderLayout.CENTER);
        jPanel12.add(boxY, java.awt.BorderLayout.EAST);

        jPanel10.add(jPanel12);

        jPanel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 5, 5));
        jPanel13.setAlignmentX(0.0F);
        jPanel13.setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel5.setText("Direzione Robot:");
        jPanel13.add(jLabel5, java.awt.BorderLayout.WEST);

        maxPlayersCombo.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        maxPlayersCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "W", "N", "E","S" }));
        maxPlayersCombo.setPreferredSize(new java.awt.Dimension(100, 33));
        jPanel13.add(maxPlayersCombo, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel13);

        jPanel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 20, 5));
        jPanel14.setAlignmentX(0.0F);
        jPanel14.setLayout(new java.awt.BorderLayout());
		
		
        jLabel6.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel6.setText("Colore Robot:");
        jPanel14.add(jLabel6, java.awt.BorderLayout.WEST);

        nRobotsCombo.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        nRobotsCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "blue", "emerald", "green","orange",
                                                                        "red","turquoise","violet","yellow"}));
        nRobotsCombo.setPreferredSize(new java.awt.Dimension(100, 33));
        jPanel14.add(nRobotsCombo, java.awt.BorderLayout.CENTER);
		
        jPanel10.add(jPanel14);
		
		
        jPanel7.add(jPanel10);
		

        matchPanel.add(jPanel7, java.awt.BorderLayout.CENTER);

        getContentPane().add(matchPanel, "match");

        playersPanel.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))));
        jPanel8.setLayout(new java.awt.BorderLayout());
		
	/*** VIEW ROBODROMO   ********************/
        
                JPanel panel30=new JPanel();


		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.GREEN);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(Color.WHITE);
		
		JPanel panel_3 = new JPanel();
		       
		        
		panel_3.setBackground(Color.ORANGE);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.CYAN);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(Color.BLACK);
		separator.setBackground(Color.BLACK); 
		
		panel = new JPanel();
		
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBackground(Color.BLACK);
		separator_1.setForeground(Color.BLACK);
		
		JPanel panel_5 = new JPanel();
                
                
		GroupLayout groupLayout = new GroupLayout(panel30);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(separator, GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
								.addComponent(separator_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
								.addComponent(panel_1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 330, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
								.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
								.addComponent(panel_3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
					.addGap(0))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel, GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(18))
						.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_3, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(180)
					.addComponent(panel_5, GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
					.addGap(103))
		);
		
		panel_6 = new JPanel();
		
		JPanel panel_7 = new JPanel();
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup()
					.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
		);
		gl_panel_5.setVerticalGroup(
			gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
				.addComponent(panel_7, GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
		);
		
		scrollPane_2 = new JScrollPane();
		
		JLabel lblSchedeEseguite = new JLabel("SCHEDE ESEGUITE");
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(
			gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
				.addGroup(gl_panel_7.createSequentialGroup()
					.addGap(33)
					.addComponent(lblSchedeEseguite, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(42))
		);
		gl_panel_7.setVerticalGroup(
			gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_7.createSequentialGroup()
					.addComponent(lblSchedeEseguite)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
		);
                
                /****  JLIST **********************/
		
		
		panel_7.setLayout(gl_panel_7);
		
		scrollPane_1 = new JScrollPane();
		
		JLabel lblRegistro = new JLabel("REGISTRO");
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
				.addGroup(Alignment.TRAILING, gl_panel_6.createSequentialGroup()
					.addGap(69)
					.addComponent(lblRegistro, GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
					.addGap(56))
		);
		gl_panel_6.setVerticalGroup(
			gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_6.createSequentialGroup()
					.addComponent(lblRegistro)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
		);
		
		
                
		panel_6.setLayout(gl_panel_6);
		panel_5.setLayout(gl_panel_5);
                
                
                
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		        
		
		
		btnAggiungiIstrz = new JButton(new ImageIcon(buttonAggiungi));
		btnAggiungiIstrz.setContentAreaFilled(false);
		btnAggiungiIstrz.addActionListener(new java.awt.event.ActionListener() {
                                    
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        playFX(pathReloadFX);
                        aggiungiIstruzione();
                    			 
                    }
                });
		btnNewButton = new JButton(new ImageIcon(buttonRemove));
		btnNewButton.setContentAreaFilled(false);
                
                btnNewButton.addActionListener(new java.awt.event.ActionListener() {
                                    
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                       playFX(pathDeleteFX);
                       togliIstruzione();
                    
				 
                    }
                });
		
		 btnNewButton_3 = new JButton(new ImageIcon(buttonShift));
		 btnNewButton_3.setContentAreaFilled(false);
                 
                 btnNewButton_3.addActionListener(new java.awt.event.ActionListener() {
                                    
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                       playFX(pathShiftFX);
                       modificaOrdine();
                    
				 
                    }
                });
		
		
		
		comboBox_1 = new JComboBox();
                
                comboBox_1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"U-turn","Turn Left",
                                   "Turn Right","Back-up","Move 1","Move 2","Move 3"}));
		
		
		
		comboBox_2 = new JComboBox();
		
		rdbtnNewRadioButton_2 = new JRadioButtonMenuItem("Shift Avanti",new ImageIcon(buttonShiftAvanti));
                rdbtnNewRadioButton_2.doClick();
		rdbtnNewRadioButton_2.setContentAreaFilled(false);
		
		radioButton = new JRadioButtonMenuItem("Shift Indietro",new ImageIcon(buttonShiftDietro));
		radioButton.setContentAreaFilled(false);
		
                
		ButtonGroup group = new ButtonGroup();
                group.add(rdbtnNewRadioButton_2);
                group.add(radioButton);
	    
		
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnNewButton_3, Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
						.addComponent(btnAggiungiIstrz, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 44, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(comboBox_1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(43))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(rdbtnNewRadioButton_2, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
							.addGap(18)))
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(comboBox_2, 0, 73, Short.MAX_VALUE))
						.addComponent(radioButton, GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
					.addGap(25))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
							.addComponent(btnAggiungiIstrz, 0, 0, Short.MAX_VALUE)
							.addComponent(comboBox_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 42, Short.MAX_VALUE))
						.addComponent(comboBox_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(radioButton, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
						.addComponent(rdbtnNewRadioButton_2, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
						.addComponent(btnNewButton_3, GroupLayout.PREFERRED_SIZE, 37, Short.MAX_VALUE))
					.addGap(8))
		);
		panel_1.setLayout(gl_panel_1);
		        panel30.setLayout(groupLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
		);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		panel_2.setLayout(gl_panel_2);
                textArea.setEditable(false);
		        
		        //============ PANNELLO PULSANTI DI STOP,PAUSA ECC.....=============================
		
		btnNewButton_1 = new JButton(new ImageIcon(buttonStop));
               
                btnNewButton_1.addActionListener(new java.awt.event.ActionListener() {
                                    
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        playFX(pathStopFX);
                        
                        btnNewButton_1.setEnabled(false);
                        pausabutton.setEnabled(false);
                        inizPartCtrl.mettiInFermo();
                      
                        
                    }
                });
                
		btnNewButton_1.setContentAreaFilled(false);
                
		avviaButton = new JButton(new ImageIcon(buttonAvvia));
                avviaButton.setContentAreaFilled(false);
		avviaButton.setEnabled(false);
                
                avviaButton.addActionListener(new java.awt.event.ActionListener() {
                                    
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        
                        
                        avviaButton.setEnabled(false);
                        btnNewButton_3.setEnabled(false);
                        btnAggiungiIstrz.setEnabled(false);
                        btnNewButton.setEnabled(false);
                        pausabutton.setEnabled(true);
                        btnNewButton_1.setEnabled(true);
                        
                        if(inizPartCtrl.isPausa()){
                            inizPartCtrl.mettiInEsecuzione();
                            RobodromeView.isUndo=false;
                            //btnUndo.setEnabled(false);
                            lock.release();
                           
                        }
                        else {
                            
                            inizPartCtrl.mettiInEsecuzione();
                            RobodromeView.isUndo=false;
                            new Thread(new Runnable(){
                                public void run() {
                                    while(inizPartCtrl.getSizeIstruzioni()>0){
                                        
                                        System.out.println("E in esecuzione? "+inizPartCtrl.isEsecuzione());
                                        if(inizPartCtrl.isEsecuzione()) {
                                            
                                            try {
                                                textArea.append("Verrà eseguita l'ultima istruzione del registro...."+"\n");
                                                playFX(pathRobotMovementFX);
                                                inizPartCtrl.avvia(avviaButton, comboBox_2,semp);
                                                //comboBox.addItem(count++);
                                                textArea.append("Fine Esecuzione...."+"\n");
                                                //inizPartCtrl.stampaRegistro(textArea);
                                                semp.acquire(); // aspetto che finisca l 'animazione
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(AllenamentoApp.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                        if(inizPartCtrl.isPausa()) {
                                           
                                            try {
                                                
                                                lock.acquire(); // aspetto la ripresa della partita
                                                
                                            } catch (InterruptedException ex) {
                                                Logger.getLogger(AllenamentoApp.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                          
                                        }
                                      
                                        if(inizPartCtrl.isFermo()) {
                                            
                                                                                       
                                            //inizPartCtrl.statoIniziale();
                                            break;
                                        }
                                                                         
                                    } 
                                    // FINE ISTRUZIONI
                                        playFX(pathStopFX);
                                        //comboBox.removeAllItems();
                                        
                                        if(inizPartCtrl.getSizeIstruzioni()>0)
                                            avviaButton.setEnabled(true);
                                        else
                                            avviaButton.setEnabled(false);
                                        
                                        btnNewButton_3.setEnabled(true);
                                        btnAggiungiIstrz.setEnabled(true);
                                        btnNewButton.setEnabled(true);
                                        
                                        
                                        pausabutton.setEnabled(false);
                                        btnNewButton_1.setEnabled(false);
                                        //btnUndo.setEnabled(false);
                                      
                                        inizPartCtrl.resetState();
                                        
                                }
                            }).start();
                        }			 
                    }
                });
                
		pausabutton = new JButton(new ImageIcon(buttonPausa));
                pausabutton.setContentAreaFilled(false);
                
                pausabutton.addActionListener(new java.awt.event.ActionListener() {
                                    
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                            
                            playFX(pathPauseFX);
                            inizPartCtrl.mettiInPausa();
                            pausabutton.setEnabled(false);
                            avviaButton.setEnabled(true);
                            btnNewButton_1.setEnabled(false);
                            
                            /*
                            if(comboBox.getSelectedItem()!=null)
                                btnUndo.setEnabled(true);
                            else
                                btnUndo.setEnabled(false);
                            */
                    }
                });
                
		/*
		btnUndo = new JButton(new ImageIcon(buttonUndo));
                btnUndo.setContentAreaFilled(false);
                
                btnUndo.addActionListener(new java.awt.event.ActionListener() {
                                    
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                       
                       if(!(inizPartCtrl.getAllenamento().getRobodromeView().isPlayingAnimation())) {
                            RobodromeView.isUndo=true;
                            avviaButton.setEnabled(false);
                            btnUndo.setEnabled(false);
                            new Thread(new Runnable(){
                                     public void run() {
                                         int passiIndietro=(int)comboBox.getSelectedItem();

                                         for(int i=0;i<passiIndietro;i++) {
                                             try {
                                                 playFX(pathUndoFX);
                                                 textArea.append("Undo "+(i+1)+"istruzione"+"\n");
                                                 inizPartCtrl.undo(avviaButton,comboBox_2,semp);
                                                 comboBox.removeItem(count--);

                                                // inizPartCtrl.stampaRegistro(textArea);
                                                 semp.acquire();
                                             } catch (InterruptedException ex) {
                                                 Logger.getLogger(AllenamentoApp.class.getName()).log(Level.SEVERE, null, ex);
                                             }

                                         }
                                         comboBox.removeItem(count);

                                         avviaButton.setEnabled(true);
                                         if(comboBox.getSelectedItem()!=null)
                                             btnUndo.setEnabled(true);
                                         else {
                                             btnUndo.setEnabled(false);
                                             
                                         }
                                     }
                            }).start();
                       }
                    }
                });
                */
                
                
                pausabutton.setEnabled(false);
                btnNewButton_1.setEnabled(false);
                //btnUndo.setEnabled(false);
              
		
		//comboBox = new JComboBox();
                
                 GroupLayout gl_panel_3 = new GroupLayout(panel_3);
                gl_panel_3.setHorizontalGroup(
                	gl_panel_3.createParallelGroup(Alignment.LEADING)
                		.addGroup(gl_panel_3.createSequentialGroup()
                			.addGap(2)
                			.addComponent(avviaButton, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                			.addPreferredGap(ComponentPlacement.UNRELATED)
                			.addComponent(pausabutton, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                			.addPreferredGap(ComponentPlacement.RELATED)
                			.addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
                			.addPreferredGap(ComponentPlacement.UNRELATED)
                			//.addComponent(btnUndo, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
                			.addGap(11)
                			//.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                gl_panel_3.setVerticalGroup(
                	gl_panel_3.createParallelGroup(Alignment.TRAILING)
                		.addGroup(gl_panel_3.createSequentialGroup()
                			.addGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
                				.addComponent(avviaButton, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 64, Short.MAX_VALUE)
                				.addComponent(pausabutton, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 64, Short.MAX_VALUE)
                				.addComponent(btnNewButton_1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 64, Short.MAX_VALUE)
                				//.addComponent(btnUndo, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 64, Short.MAX_VALUE))
                			.addGap(26)))
                		.addGroup(gl_panel_3.createSequentialGroup()
                			.addGap(23)
                			//.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                			.addContainerGap(47, Short.MAX_VALUE))
                );
		
		        panel_3.setLayout(gl_panel_3);
                
                        
                               
		
		getContentPane().add(panel30,"ongoing");
		
		
		
                setBounds ( ss.width / 2 - frameSize.width / 2, 
                  ss.height / 2 - frameSize.height / 2,
                  frameSize.width, frameSize.height );
                
               
                
                setResizable(false);
    }

    private void initButtonActionPerformed(java.awt.event.ActionEvent evt) {
		
            this.inizPartCtrl.iniziaAllenamento();
            this.setupMatchPanel();
            ((CardLayout) this.getContentPane().getLayout()).show(this.getContentPane(), "match");
        
    }

    

    

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.inizPartCtrl.chiudi();
    }//GEN-LAST:event_formWindowClosing

    
    

    private void cancelMatchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelMatchButtonActionPerformed
        this.inizPartCtrl.annullaPartita();
        ((CardLayout) this.getContentPane().getLayout()).show(this.getContentPane(), "match");
    }//GEN-LAST:event_cancelMatchButtonActionPerformed

    private void startMatchButtonActionPerformed(java.awt.event.ActionEvent evt) throws LineUnavailableException, UnsupportedAudioFileException, IOException {

        String robodromo=(String)robodromeCombo.getSelectedItem();
        String direzioneRobot=(String)maxPlayersCombo.getSelectedItem();
        int col=Integer.parseInt((String)boxX.getSelectedItem());
        int row=Integer.parseInt((String)boxY.getSelectedItem());
        Direction dir;
        
        switch(direzioneRobot) {
            case "W":
                dir=Direction.W;
                break;
            case "N":
                dir=Direction.N;
                break;
            case "E":
                dir=Direction.E;
                break;
            case "S":
                dir=Direction.S;
                break;
            default:
                dir=null;
        }
        
        String color=(String)nRobotsCombo.getSelectedItem();
        this.inizPartCtrl.sceglieCaratteristiche(robodromo,dir,row,col,color);
     
        list = new JList(inizPartCtrl.getAllenamento().
                        getRobodromeView().getRobodrome().getRobot().getRegistro().getIstruzioniDaEseguire()
                        );
                
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
                
	scrollPane_1.setViewportView(list);
        list.setCellRenderer(new ListRenderer());
        
        
        list_1 = new JList(inizPartCtrl.getAllenamento().
                        getRobodromeView().getRobodrome().getRobot().getRegistro().getIstruzioniEseguite());
	scrollPane_2.setViewportView(list_1);
        
        list_1.setCellRenderer(new ListRenderer());
        list_1.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list_1.setLayoutOrientation(JList.VERTICAL);
		list_1.setVisibleRowCount(-1);
                
                
        panel.add(inizPartCtrl.getAllenamento().getRobodromeView());
     
        playFX(pathStartFX);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
       ((CardLayout) this.getContentPane().getLayout()).show(this.getContentPane(), "ongoing");
    }//GEN-LAST:event_startMatchButtonActionPerformed
    
    private void aggiungiIstruzione() {
        String nomeIstruzione=(String)comboBox_1.getSelectedItem();
        inizPartCtrl.aggiungiIstruzione(new Istruzione(nomeIstruzione,21));
        textArea.append("Inserita l'istruzione "+nomeIstruzione+"\n");
        //inizPartCtrl.stampaRegistro(textArea);
        comboBox_2.addItem(nomeIstruzione);
        
        avviaButton.setEnabled(true);
    }
    
    private void togliIstruzione() {
        String nomeIstruzione = (String)comboBox_2.getSelectedItem();
        if(nomeIstruzione!=null) {
            inizPartCtrl.togliIstruzione(new Istruzione(nomeIstruzione,21));
            textArea.append("Eliminata l'istruzione "+ nomeIstruzione+" dal registro"+"\n");
            // inizPartCtrl.stampaRegistro(textArea);
            comboBox_2.removeItem(nomeIstruzione);
           
            if(inizPartCtrl.getSizeIstruzioni()==0){
                avviaButton.setEnabled(false);
            }
                 
        }
    }
    
    private void modificaOrdine() {
        if(rdbtnNewRadioButton_2.isSelected()) {
            if(inizPartCtrl.modificaOrdine("SpostaAvanti"))
                textArea.append("Spostato Avanti istruzione"+"\n");
               // inizPartCtrl.stampaRegistro(textArea);
        }
        else {
            if(inizPartCtrl.modificaOrdine("SpostaIndietro"))
                textArea.append("Spostato indietro istruzione"+"\n");
               // inizPartCtrl.stampaRegistro(textArea);
        }
        
    }
    
    

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptRequestButton;
    private javax.swing.JButton cancelMatchButton;
    private javax.swing.JButton createButton;
    private javax.swing.JComboBox<String> endGameCombo;
    private javax.swing.JButton initButton;
    private javax.swing.JPanel initPanel;
    private javax.swing.JTextField keyField;
    private javax.swing.JPanel matchPanel;
    private javax.swing.JComboBox<String> maxPlayersCombo;
    private javax.swing.JComboBox<String> nRobotsCombo;
    private javax.swing.JPanel ongoingMatchPanel;
    private javax.swing.JPanel playersPanel;
    private javax.swing.JTextField portField;
    private javax.swing.JButton rejectRequestButton;
    private javax.swing.JList<String> requestList;
    private javax.swing.JComboBox<String> robodromeCombo;
    private javax.swing.JPanel robotRecapPanel;
    private javax.swing.JButton startMatchButton;
    private javax.swing.JCheckBox upgradeCheck;
    // End of variables declaration//GEN-END:variables

    private void setupMatchPanel() {
        File robodromeDir = new File("robodromes");
        File[] robodromeFiles = robodromeDir.listFiles();
        String[] opts = new String[robodromeFiles.length];
        for (int i = 0; i < opts.length; i++) {
            opts[i] = robodromeFiles[i].getName().split("\\.")[0];
        }
        this.robodromeCombo.setModel(new DefaultComboBoxModel<>(opts));
    }
    
    
    public void play(String path) {
        new Thread() {
             public void run() {
                try {
                    File file = new File(path);
                    Clip clip = AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(file));
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    
		} catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }.start();
    }
    
    public static void playFX(String path) {
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
