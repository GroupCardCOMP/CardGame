
package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import main.Game;
import main.UserAccounts;
import javax.swing.JOptionPane;
import ui.MenuButton;
import utilz.LoadSave;
import ui.LoginButton;

public class Login extends State implements Statemethods {

    private LoginButton[] buttons = new LoginButton[3];
    private BufferedImage backgroundImg;
    private int menuX, menuY, menuWidth, menuHeight;

    public Login(Game game) {
        super(game);
        loadButtons();
        loadBackground();
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(
            "res/login_pixel_background.png"
        );
        menuX = 0;
        menuY = 0;
        menuWidth = Game.GAME_WIDTH;
        menuHeight = Game.GAME_HEIGHT;
    }

    private void loadButtons() {
        int[][] r = {
            {570,445,510,100},
            {570,565,510,100},
            {570,685,510,100}
        };

        Gamestate[] states = {
            Gamestate.MENU,
            Gamestate.NEW_ACCOUNT,
            Gamestate.QUIT
        };

        for (int i = 0; i < buttons.length; i++) {
            int x = r[i][0] * Game.GAME_WIDTH / 1670;
            int y = r[i][1] * Game.GAME_HEIGHT / 941;
            int w = r[i][2] * Game.GAME_WIDTH / 1670;
            int h = r[i][3] * Game.GAME_HEIGHT / 941;

            buttons[i] = new LoginButton(
                x + w/2, y, i, states[i], w, h
            );
        }
    }

    @Override
    public void update() {
        for (LoginButton mb : buttons) {
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

        for (LoginButton mb : buttons) {
            mb.draw(g);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (LoginButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (int i = 0; i < buttons.length; i++) {
            LoginButton mb = buttons[i];

            if (isIn(e, mb) && mb.isMousePressed()) {
                switch (i) {
                    case 0:
                        if (UserAccounts.login()) {
                            try {
                                game.getMenu().loadUserData();
                                Gamestate.state = Gamestate.MENU;
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Could not load account data: "
                                        + ex.getMessage(),
                                    "Account Error",
                                    JOptionPane.ERROR_MESSAGE
                                );
                            }
                        }
                        break;

                    case 1:
                        UserAccounts.register();
                        break;

                    case 2:
                        System.exit(0);
                        break;
                }
                break;
            }
        }

        resetButton();
    }

    private void resetButton() {
        for (LoginButton mb : buttons) {
            mb.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (LoginButton mb : buttons) {
            mb.setMouseOver(false);
        }

        for (LoginButton mb : buttons) {
            if (isIn(e, mb)) {
                mb.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
