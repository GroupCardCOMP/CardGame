package inputs;

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
        // Handle mouse click events\
        System.out.println("Mouse clicked at: (" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Handle mouse press events
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Handle mouse release events
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
        // Handle mouse move events
        gamePanel.setRectanglePosition(e.getX(), e.getY());
        System.out.println("Mouse moved to: (" + e.getX() + ", " + e.getY() + ")");
    }

}
