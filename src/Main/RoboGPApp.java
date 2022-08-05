package Main;

import Utility.BackgroundPanel;
import Partita.PartitaApp;
import com.sun.scenario.Settings;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import Allenamento.AllenamentoApp;
import static Allenamento.AllenamentoApp.pathMusicBackGround;

/**
 *
 * @author John Hundred
 */
public class RoboGPApp {
        private JFrame frame;
        private static String pathMenuMusic="sound/menuVanquish.wav";
        private Thread threadBackgroundMusic;
        private Clip clip;
	private Dimension ss = Toolkit.getDefaultToolkit ().getScreenSize ();
	Dimension frameSize = new Dimension ( 450, 280 );
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RoboGPApp window = new RoboGPApp();
				
					window.frame.setVisible(true);
                                        window.play(pathMenuMusic);
                                        
                                        Image imageLauncher = null;
                                        try {
                                        imageLauncher =ImageIO.read(new File(("img/iconLauncher.jpg")));
                                        }catch(IOException e) {
                                            e.getMessage();
                                        }
                                        window.frame.setIconImage(imageLauncher);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RoboGPApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Font myFont=null;
		try {
			myFont=Font.createFont(Font.TRUETYPE_FONT,
					new File("font/overseer.otf"));
			myFont = myFont.deriveFont(Font.BOLD,28);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Image background = Toolkit.getDefaultToolkit().
				createImage("img/menu.jpg");
		BackgroundPanel panel = new  BackgroundPanel(background);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(panel, GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
		);
		
		JLabel iniziaPartitaLabel = new JLabel("INIZIA PARTITA");
		iniziaPartitaLabel.setForeground(new Color(220, 20, 60));
		iniziaPartitaLabel.setFont(myFont); 
		
		iniziaPartitaLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		iniziaPartitaLabel.setOpaque(true);
		iniziaPartitaLabel.setBackground(Color.LIGHT_GRAY);
		
		iniziaPartitaLabel.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent me) {
                            
                            try {
                                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                                    if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                    }
                                }
                            } catch (ClassNotFoundException ex) {
                                java.util.logging.Logger.getLogger(AllenamentoApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (InstantiationException ex) {
                                java.util.logging.Logger.getLogger(AllenamentoApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                java.util.logging.Logger.getLogger(AllenamentoApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                                java.util.logging.Logger.getLogger(AllenamentoApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            }
                            
                            
                             frame.setVisible(false); 
                             frame.dispose();
                             PartitaApp partitaApp= new PartitaApp(clip);
                             
                        }
			
			@Override
                        public void mouseEntered(MouseEvent e) {
				iniziaPartitaLabel.setBackground(Color.CYAN);

                        }

                        @Override
                        public void mouseExited(MouseEvent e) {
                                iniziaPartitaLabel.setBackground(Color.LIGHT_GRAY);
                        }
			
		});
		
		JLabel lniziaAllenamentoLabel = new JLabel("INIZIA ALLENAMENTO");
		lniziaAllenamentoLabel.setForeground(new Color(220, 20, 60));
		lniziaAllenamentoLabel.setFont(myFont);
		lniziaAllenamentoLabel.setOpaque(true);
		lniziaAllenamentoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lniziaAllenamentoLabel.setBackground(Color.LIGHT_GRAY);
		
		lniziaAllenamentoLabel.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent me) { 
                            try {
                                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                                    if ("Nimbus".equals(info.getName())) {
                                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                                        break;
                                    }
                                }
                            } catch (ClassNotFoundException ex) {
                                java.util.logging.Logger.getLogger(AllenamentoApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (InstantiationException ex) {
                                java.util.logging.Logger.getLogger(AllenamentoApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (IllegalAccessException ex) {
                                java.util.logging.Logger.getLogger(AllenamentoApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                                java.util.logging.Logger.getLogger(AllenamentoApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            }
                            
                            clip.stop();
                            threadBackgroundMusic.interrupt();
                            frame.setVisible(false); 
                            frame.dispose(); 
                            AllenamentoApp.singleInstance = new AllenamentoApp();
                            AllenamentoApp.singleInstance.setVisible(true);
                            AllenamentoApp.singleInstance.play(pathMusicBackGround);
                        } 
			
			@Override
	        public void mouseEntered(MouseEvent e) {
				lniziaAllenamentoLabel.setBackground(Color.CYAN);

	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	        	lniziaAllenamentoLabel.setBackground(Color.LIGHT_GRAY);
	        }
			
		});
		JLabel esciLabel = new JLabel("ESCI");
		esciLabel.setFont(myFont);
		esciLabel.setForeground(new Color(220, 20, 60));
		esciLabel.setOpaque(true);
		esciLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		esciLabel.setBackground(Color.LIGHT_GRAY);
		
		esciLabel.addMouseListener(new MouseAdapter() {
			
			public void mousePressed(MouseEvent me) {
                                
				frame.setVisible(false); 
				frame.dispose();
                                System.exit(0);
	          }
			
			@Override
	        public void mouseEntered(MouseEvent e) {
				esciLabel.setBackground(Color.CYAN);

	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	        	esciLabel.setBackground(Color.LIGHT_GRAY);
	        }
			
		});
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap(293, Short.MAX_VALUE)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(esciLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(iniziaPartitaLabel, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED))
								.addGroup(gl_panel.createSequentialGroup()
									.addComponent(lniziaAllenamentoLabel, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)))
							.addGap(10))))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addGap(47)
					.addComponent(iniziaPartitaLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
					.addComponent(lniziaAllenamentoLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addGap(39)
					.addComponent(esciLabel, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addGap(61))
		);
		panel.setLayout(gl_panel);
		
		frame.getContentPane().setLayout(groupLayout);
		frame.setBounds(ss.width / 2 - frameSize.width / 2, 
                ss.height / 2 - frameSize.height / 2,
                frameSize.width, frameSize.height);
		
		frame.setUndecorated(true);
	
	}
        
        
        public void play(String path) {
            threadBackgroundMusic=new Thread() {
             public void run() {
                    
               
                    try {
                        
                        File file = new File(path);
                        clip = AudioSystem.getClip();
                        clip.open(AudioSystem.getAudioInputStream(file));
                        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        float range = gainControl.getMaximum() - gainControl.getMinimum();
                        float gain = (range * 0.70f) + gainControl.getMinimum();
                        gainControl.setValue(gain);
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                       
                        
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                
                
            }
        };
        threadBackgroundMusic.start();
        
        }

}
