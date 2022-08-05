package Utility;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;
import Partita.RobotMarker;

/**
 *
 * @author John Hundred
 */
public class ListRobotRenderer extends JLabel implements ListCellRenderer<RobotMarker>{
        
        private String owner;
        
	public ListRobotRenderer(String owner) {
                        
            this.owner=owner;
            setOpaque(true);
            setOpaque(true);
            setBorder(new EmptyBorder(1, 1, 1, 1));
                   
        }
		
    public Component getListCellRendererComponent(JList<? extends RobotMarker> list,
    		RobotMarker robot, int index,boolean isSelected, boolean cellHasFocus) {
       
       if(robot.getOwner().equals(owner))
            setText(robot.getOwner()+" "+"(Io)   "+"Punti Salute : "+
                    robot.getSalute() +" Punti Vita : "+robot.getVita());
       else
           setText(robot.getOwner()+" "+"   "+"Punti Salute : "+
                    robot.getSalute() +" Punti Vita : "+robot.getVita());
        
       setIcon(new ImageIcon(robot.getImage(100)));
        
      
        
                       
        return this;
    }
}
