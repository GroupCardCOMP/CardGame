package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel;

public class Keyboardinputs implements KeyListener {

    private GamePanel gamePanel;

    public Keyboardinputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                gamePanel.changeYDelta(-5);
                // Handle up key press
                break;
            case KeyEvent.VK_S:
                gamePanel.changeYDelta(5);
                // Handle down key press
                break;
            case KeyEvent.VK_A:
                gamePanel.changeXDelta(-5);
                // Handle left key press
                break;
            case KeyEvent.VK_D:
                gamePanel.changeXDelta(5);
                // Handle right key press
                break;
            default:
                // Handle other key presses
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
