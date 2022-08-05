package Partita;

import java.io.Serializable;

/**
 *
 * @author John Hundred
 */
public class Checkpoint implements Serializable {
    private int x;
    private int y;
    private int number;
    
    
    public Checkpoint() {
        x=-1;
        y=-1;
        number=-1;
    }
    
    public void setPosX(int x) {
        this.x=x;
    }
    
    public int getX() {
        return x;
    }
    
    public void setPosY(int y) {
        this.y=y;
    }
    
    public int getY() {
        return y;
    }
    
    public void setNumberChekpoint(int number) {
        if(this.number<=0 && number<2) {
            this.number=number;
        }else if(this.number>0 && this.number==(number-1)) {
             this.number=number;   
        }
        
           
    }
    
    public int getNumberChekpoint() {
        return number;
    }
    
    
}
