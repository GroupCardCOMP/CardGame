
package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import main.Game;
import main.UserAccounts;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends State implements Statemethods {

    private MenuButton[] buttons = new MenuButton[6];

    private BufferedImage backgroundImg;

    private int menuX, menuY, menuWidth, menuHeight;
    public String username;
    public int coins;


    public Menu(Game game) {
        super(game);
        
        loadButtons();
        loadBackground();
        
    }

    public void loadUserData() throws IOException {
        String loggedInUsername = UserAccounts.getCurrentUser();
        if (loggedInUsername == null) {
            throw new IllegalStateException("No user is logged in.");
        }

        int currentCoins = UserAccounts.getCoins(loggedInUsername);
        username = loggedInUsername;
        coins = currentCoins;
    }

    private void loadBackground() {

        backgroundImg = LoadSave.GetSpriteAtlas(
            LoadSave.AccMenuBackground
        );

        menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);

        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Game.SCALE);
    }

    private void loadButtons() {

        int buttonInterval = (int) (42 * Game.SCALE);
        int buttonStartY = (int) (100 * Game.SCALE);

        buttons[0] = new MenuButton(
            Game.GAME_WIDTH / 2,
            buttonStartY + (int) (0 * buttonInterval),
            0, Gamestate.PLAYING
        );

        buttons[1] = new MenuButton(
            Game.GAME_WIDTH / 2,
            buttonStartY + (int) (1 * buttonInterval),
            1, Gamestate.STORE
        );

        buttons[2] = new MenuButton(
            Game.GAME_WIDTH / 2,
            buttonStartY + (int) (2 * buttonInterval),
            2, Gamestate.COLLECTION
        );

        buttons[3] = new MenuButton(
            Game.GAME_WIDTH / 2,
            buttonStartY + (int) (3 * buttonInterval),
            3, Gamestate.TRADE
        );

        buttons[4] = new MenuButton(
            Game.GAME_WIDTH / 2,
            buttonStartY + (int) (4 * buttonInterval),
            4, Gamestate.OPTIONS
        );

        buttons[5] = new MenuButton(
            Game.GAME_WIDTH / 2,
            buttonStartY + (int) (5 * buttonInterval),
            5, Gamestate.QUIT
        );
    }

    @Override
    public void update() {

        for (MenuButton mb : buttons) {
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

        for (MenuButton mb : buttons) {
            mb.draw(g);
        }
        g.setColor(Color.BLACK);
        g.drawString("Coins: " + coins, menuWidth, 110);
        g.drawString("player: " + username, (int) (menuWidth * 1.85), 110);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

        for (MenuButton mb : buttons) {

            if (isIn(e, mb)) {
                mb.setMousePressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        for (int i = 0; i < buttons.length; i++) {

            MenuButton mb = buttons[i];

            if (isIn(e, mb) && mb.isMousePressed()) {

                switch (i) {

                    case 0:
                            Gamestate.state = Gamestate.PLAYING;

                        break;

                    case 1:
                        // STORE
                        Gamestate.state = Gamestate.STORE;
                        break;

                    case 2:
                        // COLLECTION
                        Gamestate.state = Gamestate.COLLECTION;
                        break;

                    case 3:
                        // TRADE
                        Gamestate.state = Gamestate.TRADE;
                        break;

                    case 4:
                        // OPTIONS
                        Gamestate.state = Gamestate.OPTIONS;
                        break;

                    case 5:
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

        for (MenuButton mb : buttons) {
            mb.resetBools();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        for (MenuButton mb : buttons) {
            mb.setMouseOver(false);
        }

        for (MenuButton mb : buttons) {

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
