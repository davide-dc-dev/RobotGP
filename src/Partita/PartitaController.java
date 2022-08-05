package Partita;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author Marco
 */
public class PartitaController extends JPanel implements ActionListener {
    
    public JButton confermaButton;
    public JButton btnInserisci;
    public JButton btnRimuovi;
    public RobotMarker robot;
    
    public PartitaController(RobotMarker robot){
        
        super();
        this.robot = robot;
        
        confermaButton = new JButton("CONFERMA PROGRAMMAZIONE");
        confermaButton.setEnabled(false);
                
        btnInserisci = new JButton("INSERISCI");
        btnInserisci.setEnabled(false);
                
	btnRimuovi = new JButton("RIMUOVI");
        btnRimuovi.setEnabled(false);
        
        confermaButton.addActionListener(this);        
        btnInserisci.addActionListener(this);        
        btnRimuovi.addActionListener(this);
        
        this.add(confermaButton);
        this.add(btnInserisci);
        this.add(btnRimuovi);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton)e.getSource();
	if(source.getText().equals("INSERISCI")){
            robot.aggiungiIstruzione();
        }
        else if(source.getText().equals("RIMUOVI")){
            robot.rimuoviIstruzione();
        }
        else if(source.getText().equals("CONFERMA PROGRAMMAZIONE")){
            confermaButton.setEnabled(false);
            btnInserisci.setEnabled(false);
            btnRimuovi.setEnabled(false);
                                  
            robot.confermaProgrammazione();  
        }
    }
    

}
