package Partita;

import Partita.Checkpoint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import robogp.robodrome.Direction;
import robogp.robodrome.image.ImageUtil;

/**
 *
 * @author claudia
 */
public class RobotMarker extends Observable implements Serializable {

    private transient BufferedImage robotImage;
    private final String name;
    private final String color;
    private String owner;
    private int dockNumber;
    private int puntiSalute;
    private int puntiVita;
    private Direction dir;
    private Checkpoint chekpoint;
    private boolean distrutto;
    
   

    public RobotMarker(String name, String color) {
        this.name = name;
        this.color = color;
        this.dockNumber = 0;
        puntiSalute=10;
        puntiVita=3;
        chekpoint= new Checkpoint();
        distrutto=false;
    }

    public BufferedImage getImage(int size) {
        if (this.robotImage == null) {
            String imgFile = "robots/" + name + ".png";
            try {
                robotImage = ImageIO.read(new File(imgFile));
            } catch (IOException ex) {
                Logger.getLogger(RobotMarker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ImageUtil.scale(ImageUtil.superImpose(null, this.robotImage),size, size);
    }
    
    public BufferedImage getImage(int size,String dir) {
        if (this.robotImage == null) {
            String imgFile = "robots/" + name +"-"+dir+".png";
            try {
                robotImage = ImageIO.read(new File(imgFile));
            } catch (IOException ex) {
                Logger.getLogger(RobotMarker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ImageUtil.scale(ImageUtil.superImpose(null, this.robotImage),size, size);
    }

    public void assign(String nickname, int dock) {
        this.owner = nickname;
        this.dockNumber = dock;
        
    }

    public void free() {
        this.owner = null;
        this.dockNumber = 0;
    }

    public boolean isAssigned() {
        return (this.dockNumber > 0);
    }

    public String getOwner() {
        return this.owner;
    }

    public int getDock() {
        return this.dockNumber;
    }
    
    public void setChekpoint(int posX,int posY,int number) {
        chekpoint.setPosX(posX);
        chekpoint.setPosY(posY);
        chekpoint.setNumberChekpoint(number);
    }
    
    public Checkpoint getCheckpoint() {
        return chekpoint;
    }
    
    public String getName() {
        return name;
    }
    
    public void setSalute(int salute) {
        this.puntiSalute=salute;
    }
    
    public int getSalute() {
        return puntiSalute;
    }
    
    public void setDirection(Direction dir) {
        this.dir=dir;
    }
    
    public Direction getDirection() {
        return dir;
    }
    
    public void setDistrutto(boolean flag) {
        if(flag==true){
            puntiSalute=10;
            puntiVita=puntiVita-1;
            this.distrutto=flag;
        }
        this.distrutto=flag;
    }
    
    public boolean isDistrutto() {
        return distrutto;
    }
    
    public void setVita(int vita) {
        puntiVita=vita;
    }
    
    public int getVita() {
        return puntiVita;
    }
    
    public void confermaProgrammazione(){
        setChanged();
        notifyObservers("conferma");
    }
    public void aggiungiIstruzione(){
        setChanged();
        notifyObservers("aggiungi");
    }
    
    public void rimuoviIstruzione(){
        setChanged();
        notifyObservers("rimuovi");
    }
    
}
