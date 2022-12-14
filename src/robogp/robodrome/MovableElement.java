/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robogp.robodrome;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import Allenamento.Robot;
import Partita.RobotMarker;
import robogp.robodrome.image.ImageUtil;

/**
 *
 * @author claudia
 */
public class MovableElement  implements Serializable{

    private transient BufferedImage img;
    private transient BufferedImage resizedImg;
    private int resizedWidth;
    private int resizedHeight;
    private int rowPos;
    private int colPos;
    private Direction dir;
    private int posX;
    private int posY;
    private float rotation;
    private boolean visible;
    private transient BufferedImage image;
    private boolean hasWallAhead=false;
    private Direction beltDirection;
    private int distanza;

    public MovableElement(BufferedImage img, Direction imageDir) {
        this.img = img;
        this.resizedImg = img;
        this.resizedWidth = -1;
        this.resizedHeight = -1;
        this.dir = imageDir;
    }
    
    public void  setHasWallAhead(boolean flag) {
        hasWallAhead=flag;
    }
    
    public boolean getHasWallAhead() {
        return hasWallAhead;
    }
    
    public void setDirectionBelt(Direction dir) {
        beltDirection=dir;
    }
    
    public Direction getDirectionBelt() {
        return beltDirection;
    }

    public void setImage(BufferedImage img, Direction imageDir,Robot robot) {
        if (imageDir != this.dir) {
            System.out.println("Original image direction:" + imageDir);
            System.out.println("Movable element direction:" + dir);
            robot.setDirection(dir);
            Rotation rot = Rotation.getClockwiseRotation(imageDir, this.dir);
            System.out.println("Rotation: " + rot);
            this.img = ImageUtil.rotate(img, rot);
        } else {
            this.img = img;
        }
        if (resizedWidth < 0 || resizedHeight < 0) {
            this.resizedImg = this.img;
        } else {
            resetImageSize(resizedWidth, resizedHeight);
        }

    }
    
    public void setImage(BufferedImage img, Direction imageDir,RobotMarker robot) {
        if (imageDir != this.dir) {
            System.out.println("Original image direction:" + imageDir);
            System.out.println("Movable element direction:" + dir);
            robot.setDirection(dir);
            Rotation rot = Rotation.getClockwiseRotation(imageDir, this.dir);
            System.out.println("Rotation: " + rot);
            this.img = ImageUtil.rotate(img, rot);
        } else {
            this.img = img;
        }
        if (resizedWidth < 0 || resizedHeight < 0) {
            this.resizedImg = this.img;
        } else {
            resetImageSize(resizedWidth, resizedHeight);
        }

    }
    
    public void setImage(BufferedImage img) {
        
            this.img = img;
       
    }

    public void resetImageSize(int w, int h) {
        this.resizedImg = ImageUtil.scale(this.img, w, h);
        this.resizedWidth = w;
        this.resizedHeight = h;
    }

    public void setBoardPosition(int row, int col) {
        this.rowPos = row;
        this.colPos = col;
    }

    /**
     * @return the posX
     */
    public int getPosX() {
        return posX;
    }

    /**
     * @return the posY
     */
    public int getPosY() {
        return posY;
    }
    
    public void setRowPos(int posX) {
        rowPos=posX;
    }
    public void setColPos(int posY) {
        colPos=posY;
    }

    /**
     * @return the img
     */
    public BufferedImage getImage() {
        return this.resizedImg;
    }

    /**
     * @param posX the posX to set
     */
    public void setPosX(int posX) {
        this.posX = posX;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @return the rotation
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * @return the rowPos
     */
    public int getRowPos() {
        return rowPos;
    }

    /**
     * @return the colPos
     */
    public int getColPos() {
        return colPos;
    }

    /**
     * @param posY the posY to set
     */
    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * @param rotation the rotation to set
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    /**
     * @return the dir
     */
    public Direction getDirection() {
        return dir;
    }

    /**
     * @param dir the direction to set
     */
    public void setDirection(Direction dir,Robot robot) {
        boolean imageReset = false;
        image = this.img;
        Direction oldDir = this.dir;
        if (!this.dir.equals(dir)) {
            imageReset = true;
        }
        this.dir = dir;
        if (imageReset) this.setImage(image, oldDir,robot);
    }
    
    public void setDirection(Direction dir,RobotMarker robot) {
        boolean imageReset = false;
        image = this.img;
        Direction oldDir = this.dir;
        if (!this.dir.equals(dir)) {
            imageReset = true;
        }
        this.dir = dir;
        if (imageReset) this.setImage(image, oldDir,robot);
    }
    
    

    public void resizeImage(int size) {
        img = ImageUtil.scale(img, size, size);
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setDirection(Direction dir) {
        this.dir=dir;
    }
    
    public void setDistanza(int distanza) {
        this.distanza=distanza;
    }
    
    public int getDistanza() {
        return distanza;
    }
}
