package Allenamento;

import Allenamento.Robot;
import Allenamento.Allenamento;
import connection.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import Utility.Istruzione;
import robogp.matchmanager.Match;
import robogp.robodrome.Direction;


public class AllenamentoController {

    private Allenamento allenamento;
    
   
    private Match theMatch;
    

    /* Gestione Pattern Singleton */
    private static AllenamentoController singleInstance;

    private AllenamentoController() {
     
        
    }

   
    public static AllenamentoController getInstance() {
        if (AllenamentoController.singleInstance == null) {
            AllenamentoController.singleInstance = new AllenamentoController();
        }
        return AllenamentoController.singleInstance;
    }

   

    public void creaPartita(String rbdName, int nMaxPlayers, int nRobotsXPlayer, Match.EndGame endGameCond, boolean initUpg) {
        this.theMatch = Match.getInstance(rbdName, nMaxPlayers, nRobotsXPlayer, endGameCond, initUpg);
        
       
    }

   

    

    void chiudi() {
        if (this.theMatch != null) {
            if (this.theMatch.getStatus() == Match.State.Started) {
                this.theMatch.stop();
            } else if (this.theMatch.getStatus() == Match.State.Created) {
                this.theMatch.cancel();
            }
        }
       
    }

     

    

    public void rifiutaRichiesta(String nickname) {
        this.theMatch.refusePlayer(nickname);
    }

    public void avviaPartita() {
        
            this.theMatch.start();
        
    }

    public void annullaPartita() {
        this.theMatch.cancel();
        
    }
    
    public boolean iniziaAllenamento() {
        allenamento=new Allenamento();
        return true;
    }
    
    public boolean sceglieCaratteristiche(String robodromo,Direction dir,
                                            int row,int col,String color) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        
        if(allenamento!=null) {
            if(allenamento.setAllenamento(robodromo,dir,row,col,color)==true)
                return true;
            else
                return false;
        }else
            return false;
    }
    
    public Allenamento getAllenamento() {
        return allenamento;
    }
    
    public boolean aggiungiIstruzione(Istruzione istruzione) {
        if(!(allenamento.getRobodromeView().isPlayingAnimation())) {
            allenamento.getRobodromeView().getRobodrome().getRobot().
                    getRegistro().getIstruzioniDaEseguire().addElement(istruzione);

            return true;
        }else
            return false;
    }
   
    /*
    public void stampaRegistro(JTextArea console) {
        allenamento.getRobodromeView().getRobodrome().getRobot().
                getRegistro().stampaRegistro(console);
    }
*/
    
    public int getSizeIstruzioni() {
        return allenamento.getRobodromeView().getRobodrome().getRobot().
                getRegistro().sizeIstruzioni();
    }
    
    
    
    public boolean togliIstruzione(Istruzione istruzione) {
          if(!(allenamento.getRobodromeView().isPlayingAnimation())) {
              allenamento.getRobodromeView().getRobodrome().getRobot().
                getRegistro().remove(istruzione);
              
             return true;
          }else {
              return false;
          }
              
    }
    
    public boolean modificaOrdine(String s) {
          if(!(allenamento.getRobodromeView().isPlayingAnimation())) {
            if(allenamento.getRobodromeView().getRobodrome().getRobot().
                    getRegistro().modificaOrdine(s))
              return true;
            else
                return false;
          }
          else {
              return false;
          }
    }
    
    public void avvia(JButton avviaButton, JComboBox comboBox_2,
                      Semaphore semp) {
        
             
      
            Istruzione istruzione=allenamento.getRobodromeView().addRobotMove(allenamento.
                    getRobodromeView().getRobodrome().getRobot());
            allenamento.getRobodromeView().play(semp,avviaButton);
            
            
            comboBox_2.removeItem(istruzione.getTipoScheda());
            
    }
    
    public void undo(JButton avviaButton,JComboBox comboBox_2,
                    Semaphore semp) {
       
            Istruzione istruzione=allenamento.getRobodromeView().addRobotUndo(allenamento.
                    getRobodromeView().getRobodrome().getRobot());
            
            allenamento.getRobodromeView().play(semp,avviaButton);
            
            comboBox_2.addItem(istruzione.getTipoScheda());
    }
    
    
    public void mettiInEsecuzione(){
        
        allenamento.setInEsecuzione(true);
        allenamento.setPausa(false);
        allenamento.setFermo(false);
    }
    
    public boolean isEsecuzione() {
        return allenamento.isInEsecuzione();
    }
    
    public boolean isPausa() {
       return  allenamento.isInPausa();
    }
    
    
    public boolean isFermo() {
        return allenamento.isFermo();
    }

    
    public void mettiInPausa() {
        allenamento.setInEsecuzione(false);
        allenamento.setPausa(true);
        allenamento.setFermo(false);
      
    }
    
    public void mettiInFermo() {
        allenamento.setFermo(true);
        allenamento.setInEsecuzione(false);
        allenamento.setPausa(false);
    }
    
    public void resetState() {
        allenamento.setInEsecuzione(false);
        allenamento.setPausa(false);
        allenamento.setFermo(false);
       // allenamento.getRobodromeView().getRobodrome().getRobot().
        //        getRegistro().getIstruzioniEseguite().clear();
    }
    
    public void pulisciRegistro() {
        allenamento.getRobodromeView().getRobodrome().getRobot().
                getRegistro().getIstruzioniDaEseguire().clear();
        allenamento.getRobodromeView().getRobodrome().getRobot().
                getRegistro().getIstruzioniEseguite().clear();
    }
            
    public void statoIniziale() {
        Robot robot=allenamento.getRobodromeView().getRobodrome().getRobot();
        allenamento.getRobodromeView().changeRobotPosition(
              robot,robot.getInizialDirection(),robot.getInizialRow(),
               robot.getInizialCol(), true);
    }
}
