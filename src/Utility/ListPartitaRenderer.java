package Utility;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author John Hundred
 */
public class ListPartitaRenderer extends JLabel implements ListCellRenderer<Istruzione> {
	
        
        
	public ListPartitaRenderer() {
                        
            
            setOpaque(true);
            setBorder(new EmptyBorder(1, 1, 1, 1));
                   
        }
		
    public Component getListCellRendererComponent(JList<? extends Istruzione> list,
    		Istruzione istruzione, int index,boolean isSelected, boolean cellHasFocus) {
        
        
        
        setText(istruzione.getTipoScheda()+"       "
               +"Priorita :"+istruzione.getPriorita());
        setIcon(istruzione.getImage());
        
        if(istruzione.isSelected()) {
            if(isSelected) {
                setBackground(Color.YELLOW);
            }else {
                 setBackground(Color.RED);
            }
            return this;
            
        }
       
        
        if (isSelected) {
                	        	      	
            setBackground(Color.CYAN);
           
            
        } else {
        	        	
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        
                       
        return this;
    }}
