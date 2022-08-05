package Utility;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class ListRenderer extends JLabel implements ListCellRenderer<Istruzione> {
	
       
        
	public ListRenderer() {
                        
            setOpaque(true);
            setOpaque(true);
            setBorder(new EmptyBorder(1, 1, 1, 1));
                   
        }
		
    public Component getListCellRendererComponent(JList<? extends Istruzione> list,
    		Istruzione istruzione, int index,boolean isSelected, boolean cellHasFocus) {
                        
       setText(istruzione.getTipoScheda());
       setIcon(istruzione.getImage());
        
        if (isSelected) {
                	        	      	
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
            
        } else {
        	        	
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
        
                       
        return this;
    }
     
}
