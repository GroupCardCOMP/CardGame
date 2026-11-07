package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import main.UserAccounts;
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
            LoadSave.MenuBackground
        );

        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);

        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);
    }

    private void loadButtons() {

        buttons[0] = new LoginButton(
            Game.GAME_WIDTH / 2,
            (int) (150 * Game.SCALE),
            0, Gamestate.MENU
        );

        buttons[1] = new LoginButton(
            Game.GAME_WIDTH / 2,
            (int) (220 * Game.SCALE),
            1, Gamestate.NEW_ACCOUNT
        );

        buttons[2] = new LoginButton(
            Game.GAME_WIDTH / 2,
            (int) (290 * Game.SCALE),
            2, Gamestate.QUIT
        );
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
                        // OLD ACCOUNT
                        if (UserAccounts.login()) {
                            Gamestate.state = Gamestate.MENU;
                        }
                        break;

                    case 1:
                        // NEW ACCOUNT 
                        UserAccounts.register();
                        break;

                    case 2:
                        // QUIT
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
