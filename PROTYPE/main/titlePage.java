package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;

public class titlePage {
    private JFrame jframe;

    public titlePage(GamePanel gamePanel) {
        jframe = new JFrame();


        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setLocationRelativeTo(null);
        jframe.setResizable(false);
        jframe.pack();
        jframe.setVisible(true); 
        jframe.addWindowFocusListener(new WindowFocusListener() {
            
            @Override
            public void windowGainedFocus(WindowEvent e) {
                // Handle window focus gained
                
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                // Handle window focus lost
                gamePanel.getGame().windowFocusLost();
            }
        });
    }
}