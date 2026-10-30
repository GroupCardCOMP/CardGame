package main;

import inputs.Keyboardinputs;
import inputs.MouseInputs;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;

public class GamePanel extends JPanel{
    private Game game;

    private MouseInputs mouseInputs;


    public GamePanel(Game game) {
        this.game = game;

        mouseInputs = new MouseInputs(this);
        setPanelSize();
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
        addKeyListener(new Keyboardinputs(this));

    }
    
    private void setPanelSize(){
        Dimension dim = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(dim);
    }


    public void updateGame() {

    }

    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public Game getGame() {
        return game;
    }
    
}
