package inputs;

import gamestates.Gamestate;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import main.GamePanel;


public class MouseInputs implements MouseListener, MouseMotionListener {
    private GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       switch (Gamestate.state) {
        case PLAYING:
            //gamePanel.getGame();
            break;
        default:
            break;
       }
    }

    @Override
    public void mousePressed(MouseEvent e) {
            switch (Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
            case PLAYING:
                //gamePanel.getGame();
                break;
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case PLAYING:
                //gamePanel.getGame();
                break;
            default:
                break;
        }
        
    }
    

    @Override
    public void mouseEntered(MouseEvent e) {
        // Handle mouse enter events
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Handle mouse exit events
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Handle mouse drag events
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case PLAYING:
                //gamePanel.getGame();
                break;
            default:
                break;
        }
    }

}
