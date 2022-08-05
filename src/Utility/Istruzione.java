package Utility;

import java.io.Serializable;
import javax.swing.ImageIcon;

public class Istruzione implements Comparable<Istruzione> ,Serializable{
    private int priorita;
    private String tipoScheda;
    private final String imagePath;
    private transient ImageIcon image;
    private boolean selected=false;
  

    
    
    public Istruzione(String tipoScheda,int priorita) {
        this.tipoScheda=tipoScheda;
        this.priorita=priorita;
        this.imagePath = "img/"+tipoScheda+".png";
    }
    
    public String toString() {
        return tipoScheda;
    }
    public boolean equals(Istruzione istruzione) {
        return this.priorita==istruzione.priorita && this.tipoScheda==istruzione.tipoScheda;
    }
    
    public String getTipoScheda() {
        return tipoScheda;
    }
    
    public int getPriorita() {
        return priorita;
    }
    
    public void setSelected(boolean flag) {
        selected=flag;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    @Override
    public int compareTo(Istruzione o) {
        return this.getTipoScheda().compareTo(o.getTipoScheda());
    }
    
     public ImageIcon getImage() {
        if (image == null) {
          image = new ImageIcon(imagePath);
        }
        return image;
  }
}
