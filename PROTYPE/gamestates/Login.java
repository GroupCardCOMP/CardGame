package gamestates;

import java.awt.Graphics; 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.*;
import main.Game;
import ui.MenuButton;
import utilz.LoadSave;

public class Login extends State implements Statemethods, ActionListener {

    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImg;
    private int menuX, menuY, menuWidth, menuHeight;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public Login(Game game) {
        super(game);
        loadButtons();
        loadBackground();
        
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MenuBackground);
        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);
    }
    // 150 220 290
    private void loadButtons() {


        
      
        //usernameField = new JTextField(15);
         

        
       //passwordField = new JPasswordField(15);
        

        //loginButton = new JButton("Login");
       

    }

    @Override 
    public void update() {
        for(MenuButton mb : buttons) {
            mb.update();
        }
    }

    @Override 
    public void draw(Graphics g) {

        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
    
        for(MenuButton mb : buttons) {
            mb.draw(g);
        }
    }

    @Override 
    public void mouseClicked(MouseEvent e) {

}

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons) {
            mb.setMouseOver(false);
        }
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            Gamestate.state = Gamestate.LOGIN;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
