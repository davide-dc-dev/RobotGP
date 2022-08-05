package Utility;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;

public class MyJTextPane extends JTextPane {
	
	public MyJTextPane() {
		super();
	}
	
	public void append(String str,Style style) 
	{
	     StyledDocument document = (StyledDocument) this.getDocument();
	     try {
			document.insertString(document.getLength(), str, style);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	                                                    // ^ or your style attribute  
	 }
	
}