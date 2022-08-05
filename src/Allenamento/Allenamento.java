package Allenamento;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import robogp.robodrome.Direction;
import robogp.robodrome.Robodrome;
import robogp.robodrome.view.RobodromeView;

public class Allenamento {
    
    private  RobodromeView rv;
    private boolean pausa;
    private boolean inEsecuzione;
    private boolean fermo;
    
    
    
    public Allenamento() {
               
        pausa=false;
        inEsecuzione=false;
        fermo=false;
    }
    
    public boolean setAllenamento(String robodromo,Direction dir,int row,int col,
                                    String color) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        rv=new RobodromeView(new Robodrome("./robodromes/"+robodromo+".txt"),42);
        
        if(rv.getRobodrome().setRobot(dir,row,col,color)==true) {
            rv.placeRobot(rv.getRobodrome().getRobot(), true);
            return true;
        }else

            return false;
    }
    
    
    public boolean isInPausa() {
        return pausa==true;
    }
    
    public boolean isInEsecuzione() {
        return inEsecuzione==true;
    }
    
    public RobodromeView getRobodromeView() {
        return rv;
    }
    
    public boolean isFermo() {
        return fermo==true;
    }
    
    public void setPausa(boolean val) {
        this.pausa=val;
    }
    public void setInEsecuzione(boolean val) {
        this.inEsecuzione=val;
    }
    
    public void setFermo(boolean val) {
        this.fermo=val;
    }
    
    
}
