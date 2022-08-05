package Allenamento;

import Allenamento.Registro;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import Partita.RobotMarker;
import robogp.robodrome.Direction;
import robogp.robodrome.image.ImageUtil;


public class Robot  implements Serializable{
    private Registro registro;
    private int posizioneRobot;
    private int direzioneRobot;
    private transient BufferedImage robotImage;
    private final String color;
    private Direction dir;
    private int row;
    private int col;
    private int inizialRow;
    private int inizialCol;
    private Direction inizialDir;
    
    public Robot(String color,Direction dir,int row,int col) {
        
        this.color=color;
        this.dir=dir;
        this.row=row;
        this.col=col;
        inizialRow=row;
        inizialCol=col;
        inizialDir=dir;
    }
    
    public void setDirection(Direction dir) {
        this.dir=dir;
    }
    
    
    
    
    public void creaRegistro() {
        registro=new Registro();
        
    }
    
    public BufferedImage getImage(int size) {
        if (this.robotImage == null) {
            String imgFile = "robots/"+"robot-"+color+".png";
            try {
                robotImage = ImageIO.read(new File(imgFile));
            } catch (IOException ex) {
                Logger.getLogger(RobotMarker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ImageUtil.scale(ImageUtil.superImpose(null, this.robotImage),size, size);
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public Direction getDirection() {
        return dir;
    }
    
    public String getColor() {
        return color;
    }
    
    public Registro getRegistro() {
        return registro;
    }
    
    
    public int getInizialRow() {
        return inizialRow;
        
    }
    
    public int getInizialCol() {
        return inizialCol;
    }
    
    public Direction getInizialDirection() {
        return inizialDir;
    }
   
    
    
    
}
