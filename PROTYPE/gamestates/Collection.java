package gamestates;

import java.awt.Graphics;
import javax.swing.*;
import java.awt.Color;
import java.awt.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import main.UserAccounts;
import ui.BackButton;
import ui.MenuButton;
import utilz.LoadSave;


public class Collection extends State implements Statemethods{

        private BackButton[] backButton = new BackButton[1];

        private BufferedImage backgroundImg;

        private int menuX, menuY, menuWidth, menuHeight;




    public Collection(Game game) {
        super(game);

        loadButtons();
        loadBackground();
    }

    private void loadBackground() {

        backgroundImg = LoadSave.GetSpriteAtlas(
            LoadSave.ninebynine
        );

        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);

        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (0 * Game.SCALE);
    }

    private void loadButtons() {
        backButton[0] = new BackButton(
            65,
             (int) (10 * Game.SCALEX),
            0, Gamestate.MENU
        );
    }

    @Override
    public void update() {
        
       for (BackButton mb : backButton) {
            mb.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(
            backgroundImg,
            menuX, menuY,
            menuWidth, menuHeight,
            null
        );

        for (BackButton mb : backButton) {
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
        for (BackButton mb : backButton) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
       for (int i = 0; i < backButton.length; i++) {

            BackButton mb = backButton[i];

            if (isIn(e, mb)) {
                if (mb.isMousePressed()) {
                    switch (i) {
                        case 0:
                            Gamestate.state = Gamestate.MENU;
                            break;
                    }
                }
                break;
            }
        }

        
            resetButtons();
        
    }

    private void resetButtons() {
        for (BackButton mb : backButton) {
            mb.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (BackButton mb : backButton) {
            mb.setMouseOver(false);
        }

        for (BackButton mb : backButton) {

            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
