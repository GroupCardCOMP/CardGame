package inputs;

import gamestates.Gamestate;
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
       switch (Gamestate.state) {
        case LOGIN:
            gamePanel.getGame().getLogin().keyPressed(e);
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
            case LOGIN:
                gamePanel.getGame().getLogin().keyReleased(e);
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
