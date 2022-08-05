package Partita;

import java.io.Serializable;
import Utility.Istruzione;

public class RegistroPartita implements Serializable {
    private boolean isBlocked=false;
    private Istruzione istruzione;
    
    public RegistroPartita(Istruzione istruzione) {
        this.istruzione=istruzione;
    }
    
    public RegistroPartita() {
       
    }
    
    public void setIstruzione(Istruzione istruzione) {
        this.istruzione=istruzione;
    }
    
    public Istruzione getIstruzione() {
        return istruzione;
    }
    
    public boolean isBlocked() {
        return isBlocked;
    }
    
    public void block() {
        this.isBlocked=true;
    }
    
    public void release() {
        this.isBlocked=false;
    }
    
    public void removeIstruzione() {
        istruzione=null;
    }
    
    public boolean isNotEmpty() {
        if(istruzione!=null) {
            return true;
        }else {
            return false;
        }
    }
}
