package gamestates;

import java.awt.event.MouseEvent;
import main.Game;
import ui.BackButton;
import ui.LoginButton;
import ui.MenuButton;

public class State {

    protected Game game;

    public State(Game game) {
        this.game = game;
    }

    public boolean isIn(MouseEvent e, MenuButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }
    public boolean isIn(MouseEvent e, LoginButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }
    public boolean isIn(MouseEvent e, BackButton mb) {
        return mb.getBounds().contains(e.getX(), e.getY());
    }
   

    public Game getGame() {
        return game;
    }

}
