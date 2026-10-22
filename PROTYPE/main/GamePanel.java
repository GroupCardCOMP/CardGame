package main;


import inputs.MouseInputs;
import java.awt.Graphics;
import javax.swing.JPanel;

public class GamePanel extends JPanel{

    private MouseInputs mouseInputs;
    private int xdelta = 100, ydelta = 100;

    public GamePanel() {

        mouseInputs = new MouseInputs(this);
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);

    }

    public void changeXDelta(int delta) {
        this.xdelta += delta;
        repaint();
    }

    public void changeYDelta(int delta) {
        this.ydelta += delta;
        repaint();
    }  

    public void setRectanglePosition(int x, int y) {
        this.xdelta = x;
        this.ydelta = y;
        repaint();
    }

    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.fillRect(xdelta, ydelta, 200, 50);
    }
    
}
