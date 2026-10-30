package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import main.GamePanel;
import gamestates.Gamestate;

public class Keyboardinputs implements KeyListener {

    private GamePanel gamePanel;

    public Keyboardinputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
       switch (Gamestate.state) {
        case MENU:
            gamePanel.getGame().getMenu().keyPressed(e);
            break;
        case PLAYING:
            gamePanel.getGame();
            break;
        default:
            break;
       }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().keyReleased(e);
                break;
            case PLAYING:
                gamePanel.getGame();
                break;
            default:
                break;
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}
