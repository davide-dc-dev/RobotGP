package Utility;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;

/**
 *
 * @author John Hundred
 */
public class NotifyGameOver extends JFrame{
    
    private Dimension ss = Toolkit.getDefaultToolkit ().getScreenSize ();
    private Dimension frameSize = new Dimension ( 650, 280 );
    private String s;
    
    public  NotifyGameOver (String s) {
        initialize(s);
        
    }
    
    public void initialize(String s) {
                
                TinySound.init();
                File gameOverFile = new File("sound/GameOver.wav");
                Music gameOverSound=TinySound.loadMusic(gameOverFile);
                gameOverSound.play(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
		);
		
		JLabel label = new JLabel(s);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    System.exit(0);
                }
                });
                
                addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    
                       
                        System.exit(0);
                    }
                });
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(168)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(153))
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addGap(14)
							.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(175))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(54)
					.addComponent(label)
					.addGap(41)
					.addComponent(btnNewButton)
					.addContainerGap(89, Short.MAX_VALUE))
		);
                
                
		panel.setLayout(gl_panel);
		getContentPane().setLayout(groupLayout);
		setBounds(ss.width / 2 - frameSize.width / 2, 
                ss.height / 2 - frameSize.height / 2,
                frameSize.width, frameSize.height);
		setResizable(false);
                setVisible(true);
        
        
        
    }
    
    
}
