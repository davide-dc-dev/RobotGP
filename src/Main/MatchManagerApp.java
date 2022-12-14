package Main;

import connection.Message;
import connection.MessageObserver;
import java.awt.CardLayout;
import java.io.File;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import Partita.PoolGlobale.PoolGiocatore;
import Partita.IniziarePartitaController;
import robogp.matchmanager.Match;
import robogp.matchmanager.RobotChooser;
import Partita.RobotMarker;
import robogp.matchmanager.RobotStatePanel;

public class MatchManagerApp extends javax.swing.JFrame  {

    private static MatchManagerApp singleInstance;
    private RobotStatePanel[] robotPanel;

    /**
     * Creates new form MatchManager
     */
    private MatchManagerApp() {
        initComponents();
        this.inizPartCtrl = IniziarePartitaController.getInstance();
        this.robotChooser = new RobotChooser(this, true);
    }

    public static MatchManagerApp getAppInstance() {
        return MatchManagerApp.singleInstance;
    }

    private final IniziarePartitaController inizPartCtrl;

    private final RobotChooser robotChooser;

    public IniziarePartitaController getIniziarePartitaController() {
        return this.inizPartCtrl;
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("RoboGP Match Manager");
        setName("main frame"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new java.awt.CardLayout());

        initPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 5, 5, 5));
        initPanel.setLayout(new java.awt.BorderLayout());

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel1.setText("Porta su cui girer?? il server di gioco:");
        jPanel2.add(jLabel1, java.awt.BorderLayout.WEST);

        portField.setColumns(6);
        portField.setText("2222");
        jPanel2.add(portField, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 30, 0));
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel2.setText("Parola chiave per l'accesso al server:");
        jPanel3.add(jLabel2, java.awt.BorderLayout.WEST);

        keyField.setColumns(10);
        jPanel3.add(keyField, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3);

        jPanel4.add(jPanel1);

        initPanel.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));

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
        jPanel5.add(initButton);

        initPanel.add(jPanel5, java.awt.BorderLayout.SOUTH);

        getContentPane().add(initPanel, "init");

        matchPanel.setLayout(new java.awt.BorderLayout());

        createButton.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        createButton.setText("Crea Partita");
        createButton.setAlignmentX(0.5F);
        createButton.setMaximumSize(new java.awt.Dimension(172, 40));
        createButton.setMinimumSize(new java.awt.Dimension(122, 40));
        createButton.setPreferredSize(new java.awt.Dimension(172, 40));
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
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
        jLabel4.setText("Fine partita:");
        jPanel12.add(jLabel4, java.awt.BorderLayout.WEST);

        endGameCombo.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        endGameCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "primo arrivato", "primi 3 arrivati", "tutti tranne l'ultimo" }));
        endGameCombo.setMinimumSize(new java.awt.Dimension(152, 27));
        endGameCombo.setPreferredSize(new java.awt.Dimension(152, 33));
        jPanel12.add(endGameCombo, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel12);

        jPanel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 5, 5, 5));
        jPanel13.setAlignmentX(0.0F);
        jPanel13.setLayout(new java.awt.BorderLayout());

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel5.setText("Numero massimo giocatori:");
        jPanel13.add(jLabel5, java.awt.BorderLayout.WEST);

        maxPlayersCombo.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        maxPlayersCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2", "3", "4", "5", "6", "7", "8" }));
        maxPlayersCombo.setPreferredSize(new java.awt.Dimension(100, 33));
        jPanel13.add(maxPlayersCombo, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel13);

        jPanel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 20, 5));
        jPanel14.setAlignmentX(0.0F);
        jPanel14.setLayout(new java.awt.BorderLayout());

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        jLabel6.setText("Numero robot per giocatore:");
        jPanel14.add(jLabel6, java.awt.BorderLayout.WEST);

        nRobotsCombo.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        nRobotsCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3", "4" }));
        nRobotsCombo.setPreferredSize(new java.awt.Dimension(100, 33));
        jPanel14.add(nRobotsCombo, java.awt.BorderLayout.CENTER);

        jPanel10.add(jPanel14);

        upgradeCheck.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        upgradeCheck.setText("Dotazione iniziale upgrade");
        jPanel10.add(upgradeCheck);

        jPanel7.add(jPanel10);

        matchPanel.add(jPanel7, java.awt.BorderLayout.CENTER);

        getContentPane().add(matchPanel, "match");

        playersPanel.setLayout(new java.awt.BorderLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2), javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5))));
        jPanel8.setLayout(new java.awt.BorderLayout());

        requestList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        requestList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                requestListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(requestList);

        jPanel8.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        acceptRequestButton.setText("Accetta...");
        acceptRequestButton.setEnabled(false);
        acceptRequestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptRequestButtonActionPerformed(evt);
            }
        });
        jPanel15.add(acceptRequestButton);

        rejectRequestButton.setText("Rifiuta");
        rejectRequestButton.setEnabled(false);
        rejectRequestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rejectRequestButtonActionPerformed(evt);
            }
        });
        jPanel15.add(rejectRequestButton);

        jPanel8.add(jPanel15, java.awt.BorderLayout.SOUTH);

        playersPanel.add(jPanel8, java.awt.BorderLayout.WEST);

        robotRecapPanel.setLayout(new java.awt.GridLayout(2, 4));
        playersPanel.add(robotRecapPanel, java.awt.BorderLayout.CENTER);

        startMatchButton.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        startMatchButton.setText("Avvia");
        startMatchButton.setAlignmentX(0.5F);
        startMatchButton.setEnabled(false);
        startMatchButton.setMaximumSize(new java.awt.Dimension(122, 40));
        startMatchButton.setMinimumSize(new java.awt.Dimension(122, 40));
        startMatchButton.setPreferredSize(new java.awt.Dimension(122, 40));
        startMatchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startMatchButtonActionPerformed(evt);
            }
        });
        jPanel16.add(startMatchButton);

        cancelMatchButton.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        cancelMatchButton.setText("Annulla");
        cancelMatchButton.setAlignmentX(0.5F);
        cancelMatchButton.setMaximumSize(new java.awt.Dimension(122, 40));
        cancelMatchButton.setMinimumSize(new java.awt.Dimension(122, 40));
        cancelMatchButton.setPreferredSize(new java.awt.Dimension(122, 40));
        cancelMatchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelMatchButtonActionPerformed(evt);
            }
        });
        jPanel16.add(cancelMatchButton);

        playersPanel.add(jPanel16, java.awt.BorderLayout.SOUTH);

        getContentPane().add(playersPanel, "players");

        jLabel7.setText("Partita in corso...");
        ongoingMatchPanel.add(jLabel7);

        getContentPane().add(ongoingMatchPanel, "ongoing");

        setBounds(0, 0, 737, 373);
    }// </editor-fold>//GEN-END:initComponents

    private void initButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initButtonActionPerformed
        int num = 0;
        boolean ok = true;
        try {
            num = Integer.parseInt(this.portField.getText().trim());
        } catch (NumberFormatException ex) {
            ok = false;
            JOptionPane.showMessageDialog(this, "La porta deve essere un numero\ncompreso fra 1024 e 65535");
        }
        if (ok && (num < 1024 || num > 65535)) {
            ok = false;
            JOptionPane.showMessageDialog(this, "La porta deve essere un numero\ncompreso fra 1024 e 65535");
        }
        if (ok) {
            this.inizPartCtrl.iniziaCreazionePartita(num, this.keyField.getText().trim());
            this.setupMatchPanel();
            ((CardLayout) this.getContentPane().getLayout()).show(this.getContentPane(), "match");
        }
    }//GEN-LAST:event_initButtonActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        String rbdName = this.robodromeCombo.getSelectedItem().toString();
        int endGameOpt = this.endGameCombo.getSelectedIndex();
        boolean upgradeOpt = this.upgradeCheck.isSelected();
        int nPl = this.maxPlayersCombo.getSelectedIndex() + 2;
        int nRob = this.nRobotsCombo.getSelectedIndex() + 1;
        boolean ok = true;
        if (nPl * nRob > Match.ROBOTSINGAME) {
            ok = false;
            JOptionPane.showMessageDialog(this, "Con " + nRob + " per giocatore, non si possono avere " + nPl + " giocatori.\n"
                    + "Il massimo numero di robot nel gioco ?? " + Match.ROBOTSINGAME);
        }
        if (ok) {
            this.inizPartCtrl.creaPartita(rbdName, nPl, nRob, Match.EndGame.values()[endGameOpt], upgradeOpt);
            
            System.out.println("Crea partita e stato chiamato");
            
            
            this.requestList.setModel(this.inizPartCtrl.getRequestList());
            ArrayList<RobotMarker> allRobots = Match.getInstance().getAllRobots();
            this.robotPanel = new RobotStatePanel[allRobots.size()];
            this.robotRecapPanel.removeAll();
            for (int i=0; i < robotPanel.length; i++) {
                this.robotPanel[i] = new RobotStatePanel(allRobots.get(i));
                this.robotRecapPanel.add(this.robotPanel[i]);
            }
            ((CardLayout) this.getContentPane().getLayout()).show(this.getContentPane(), "players");
        }
    }//GEN-LAST:event_createButtonActionPerformed

    private void requestListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_requestListValueChanged
        boolean enabled = (this.requestList.getSelectedIndex() >= 0);
        this.acceptRequestButton.setEnabled(enabled && this.inizPartCtrl.canAcceptMoreRequests());
        this.rejectRequestButton.setEnabled(enabled);
    }//GEN-LAST:event_requestListValueChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.inizPartCtrl.chiudi();
    }//GEN-LAST:event_formWindowClosing

    private void acceptRequestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptRequestButtonActionPerformed
        int pos = this.requestList.getSelectedIndex();
        String nickname = this.requestList.getSelectedValue();
        ArrayList<RobotMarker> robots = Match.getInstance().getAvailableRobots();
        this.robotChooser.setup(robots, Match.getInstance().getRobotsPerPlayer());
        this.robotChooser.setVisible(true);
        if (this.robotChooser.getCloseStatus() == JOptionPane.OK_OPTION) {
            ((DefaultListModel<String>) this.requestList.getModel()).remove(pos);
            this.inizPartCtrl.accettaRichiesta(nickname, this.robotChooser.getSelection());
            this.acceptRequestButton.setEnabled(this.requestList.getSelectedIndex() >= 0 && this.inizPartCtrl.canAcceptMoreRequests());
            this.startMatchButton.setEnabled(this.inizPartCtrl.canStartMatch());
            for (RobotStatePanel rsp: robotPanel) {
                rsp.update();
            }
            this.robotRecapPanel.updateUI();
        }
    }//GEN-LAST:event_acceptRequestButtonActionPerformed

    private void rejectRequestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rejectRequestButtonActionPerformed
        int pos = this.requestList.getSelectedIndex();
        String nickname = this.requestList.getSelectedValue();
        this.inizPartCtrl.rifiutaRichiesta(nickname);
        ((DefaultListModel<String>) this.requestList.getModel()).remove(pos);

    }//GEN-LAST:event_rejectRequestButtonActionPerformed

    private void cancelMatchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelMatchButtonActionPerformed
        this.inizPartCtrl.annullaPartita();
        ((CardLayout) this.getContentPane().getLayout()).show(this.getContentPane(), "match");
    }//GEN-LAST:event_cancelMatchButtonActionPerformed

    private void startMatchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startMatchButtonActionPerformed
        
        this.inizPartCtrl.avviaPartita();
        ((CardLayout) this.getContentPane().getLayout()).show(this.getContentPane(), "ongoing");
    }//GEN-LAST:event_startMatchButtonActionPerformed
    
      
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MatchManagerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MatchManagerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MatchManagerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MatchManagerApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            MatchManagerApp.singleInstance = new MatchManagerApp();
            MatchManagerApp.singleInstance.setVisible(true);
        });
    }

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
}

