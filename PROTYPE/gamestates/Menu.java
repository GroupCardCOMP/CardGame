
package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JOptionPane;
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
        String loggedInUsername =
            UserAccounts.getCurrentUser();

        if (loggedInUsername == null) {
            throw new IllegalStateException(
                "No user is logged in."
            );
        }

        int currentCoins =
            UserAccounts.getCoins(loggedInUsername);

        username = loggedInUsername;
        coins = currentCoins;
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(
            "res/main_menu_pixel_background.png"
        );

        menuX = 0;
        menuY = 0;
        menuWidth = Game.GAME_WIDTH;
        menuHeight = Game.GAME_HEIGHT;
    }

    private void loadButtons() {
        int[] top = {160,260,358,457,556,652};

        Gamestate[] states = {
            Gamestate.PLAYING,
            Gamestate.STORE,
            Gamestate.COLLECTION,
            Gamestate.TRADE,
            Gamestate.OPTIONS,
            Gamestate.QUIT
        };

        for (int i = 0; i < buttons.length; i++) {
            int x = 671 * Game.GAME_WIDTH / 1664;
            int y = top[i] * Game.GAME_HEIGHT / 896;
            int w = 304 * Game.GAME_WIDTH / 1664;
            int h = 78 * Game.GAME_HEIGHT / 896;

            buttons[i] = new MenuButton(
                x + w/2, y, i, states[i], w, h
            );
        }
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

        g.setColor(Color.WHITE);
        g.setFont(new Font(
            Font.MONOSPACED,
            Font.BOLD,
            Math.max(12, Game.GAME_HEIGHT / 48)
        ));

        FontMetrics metrics = g.getFontMetrics();

        int left = 438 * Game.GAME_WIDTH / 1664;
        int right = 1220 * Game.GAME_WIDTH / 1664;
        int baseline = metrics.getAscent() + 6;

        String coinsText = "Coins: " + coins;
        String playerText = "Player: " +
            (username == null ? "" : username);

        g.drawString(coinsText, left, baseline);
        g.drawString(
            playerText,
            right - metrics.stringWidth(playerText),
            baseline
        );
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
                        try {
                            game.getPlaying().loadUserData();
                            game.getPlaying().startGame();
                            Gamestate.state = Gamestate.PLAYING;
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(
                                null,
                                "Could not load player data: "
                                    + ex.getMessage(),
                                "Game Error",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                        break;

                    case 1:
                        Gamestate.state = Gamestate.STORE;
                        break;

                    case 2:
                        Gamestate.state = Gamestate.COLLECTION;
                        break;

                    case 3:
                        Gamestate.state = Gamestate.TRADE;
                        break;

                    case 4:
                        Gamestate.state = Gamestate.OPTIONS;
                        break;

                    case 5:
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
