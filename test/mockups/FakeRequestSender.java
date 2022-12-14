package mockups;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import connection.Connection;
import connection.Message;
import connection.MessageObserver;
import connection.PartnerShutDownException;
import java.io.IOException;
import java.util.ArrayList;
import robogp.matchmanager.Match;
import Partita.RobotMarker;

/**
 *
 * @author claudia
 */
public class FakeRequestSender extends javax.swing.JDialog implements MessageObserver {
    private final ArrayList<Connection> connections;
    private int requestCounter;
    
    /**
     * Creates new form FakeRequestSender
     */
    public FakeRequestSender(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        connections = new ArrayList<>();
        requestCounter = 0;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        javax.swing.JTextArea jTextArea1 = new javax.swing.JTextArea();
        requestButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setLineWrap(true);
        jTextArea1.setRows(5);
        jTextArea1.setText("Quando si preme il pulsante \"Genera Richiesta\" viene creata una connessione ad un Server di RoboGP su indirizzo \"localhost\" e porta 2222. Successivamente viene inviata una richiesta di partecipazione da parte dell'utente \"PippoXXX\" (XXX ?? incrementato di volta in volta di 1) e parola chiave pari alla stringa vuota. Quindi per effettuare il test il Server deve essere configurato con queste impostazioni.");
        jTextArea1.setWrapStyleWord(true);
        jTextArea1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        jTextArea1.setEnabled(false);
        jTextArea1.setMaximumSize(new java.awt.Dimension(600, 600));
        jPanel1.add(jTextArea1, java.awt.BorderLayout.PAGE_START);

        requestButton.setText("Genera Richiesta");
        requestButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                requestButtonActionPerformed(evt);
            }
        });
        jPanel1.add(requestButton, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        setBounds(0, 0, 403, 322);
    }// </editor-fold>//GEN-END:initComponents

    private void requestButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_requestButtonActionPerformed
        try {
            InetAddress address = InetAddress.getByName("localhost");
            Connection conn = Connection.connectToHost(address, 2222);
            conn.addMessageObserver(this);
            this.connections.add(conn);
            Message msg = new Message(Match.MatchJoinRequestMsg);
            Object[] pars = new Object[2];
            pars[0] = "Pippo"+this.requestCounter++;
            pars[1] = "";
            msg.setParameters(pars);
            conn.sendMessage(msg);
        } catch (UnknownHostException ex) {
            Logger.getLogger(FakeRequestSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FakeRequestSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PartnerShutDownException ex) {
            Logger.getLogger(FakeRequestSender.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }//GEN-LAST:event_requestButtonActionPerformed

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
            java.util.logging.Logger.getLogger(FakeRequestSender.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FakeRequestSender.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FakeRequestSender.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FakeRequestSender.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FakeRequestSender dialog = new FakeRequestSender(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        for (Connection conn: dialog.connections) {
                            conn.disconnect();
                        }
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton requestButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void notifyMessageReceived(Message msg) {
        System.out.println("***NEW Messaggio ricevuto:" + msg.getName());
        if (msg.getName().equals(Match.MatchJoinReplyMsg)) {
            boolean reply = (Boolean)msg.getParameter(0);
            System.out.println("\tRisposta: " + (reply ? "Accettato" : "Rifiutato"));
            if (reply) {
                RobotMarker[] robots = (RobotMarker[])msg.getParameter(1);
                for (RobotMarker r: robots) 
                    System.out.println("\tRobot assegnato: " + r.getName() + " al dock " + r.getDock());
            }
        }
    }
}
