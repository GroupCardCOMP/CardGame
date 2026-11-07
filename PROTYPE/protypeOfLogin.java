import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.*;


public class protypeOfLogin {

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;
    private BufferedImage backgroundImg;
    private int menuX, menuY, menuWidth, menuHeight;


    

 
  
    public static void main(String[] args) {

        JFrame window = new JFrame("Test");
        window.setSize(GAME_WIDTH, GAME_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setLocation(0, 0);
        panel.setSize(GAME_WIDTH, GAME_HEIGHT);
    
        
        panel.setLayout(null); // Use null layout for absolute positioning
        window.add(panel);
        int holder = 0;

        
        JLabel label = new JLabel(String.valueOf(holder));
        JTextField textField = new JTextField();
        textField.setBounds(GAME_WIDTH / 4 - 20, 350, 300, 30); // Set position
        panel.add(textField);   
        label.setForeground(Color.white);
        label.setBounds(GAME_WIDTH / 4 - 20, 300, 300, 30); // Set position
        panel.add(label);

        window.setVisible(true);



        
        
        
        


    }
}