package robogp.robodrome.animation;

import java.io.Serializable;
import robogp.robodrome.Direction;

/**
 *
 * @author John Hundred
 */
public  class BeltAnimation extends Animation implements Serializable {
    
    private Direction dir;
    private int startCell;
    private int endCell;
    private boolean hitRobot;
    private boolean hitEndWall;
    private boolean visible;
    private int startX;
    private int startY;
    private int endX;
    private int endY;

    public static int HEATTIME = 300;
    private int heatRadMax;
    
    public Direction getDirection() {
        return dir;
    }
    
    public BeltAnimation() {
        this.type = Animation.Type.BELT_MOVE;
    }
    
    public BeltAnimation(String robotName, Direction dir, int start, int end,
            boolean hitRobot, boolean hitEndWall, int cellSize) {
        this();
        which = robotName;
        duration = (int) (Animation.TIMEUNIT * Math.abs(end - start) * (cellSize / 300.0) + LaserFireAnimation.HEATTIME);
        this.dir = dir;
        startCell = start;
        endCell = end;
        this.hitRobot = hitRobot;
        this.hitEndWall = hitEndWall;
        heatRadMax = 10;
    }
    
    public void setStartPosition(int x, int y) {
        this.startX = x;
        this.startY = y;
    }
    
    public void setEndPosition(int x, int y) {
        this.endX = x;
        this.endY = y;
    }
    
     public boolean isVisible() {
            return visible;
     }

        /**
         * @param visible the visible to set
         */
    public void setVisible(boolean visible) {
            this.visible = visible;
    }
   
}
