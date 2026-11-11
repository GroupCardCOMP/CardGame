package gamestates;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import main.Game;
import main.UserAccounts;
import utilz.LoadSave;

public class Store extends State implements Statemethods {
    private static final int PACK_PRICE = 50;
    // Coordinates are measured on the original 1202 x 1308 store artwork.
    private static final int ART_WIDTH = 1202, ART_HEIGHT = 1308;
    private final BufferedImage backgroundImg;
    private final Rectangle buyBounds = area(365, 650, 815, 755);
    private final Rectangle backBounds = area(380, 1105, 820, 1215);
    private boolean buyHover, buyPressed, backHover, backPressed;
    private int[] lastPack;
    private String status = "";

    public Store(Game game) {
        super(game);
        backgroundImg = LoadSave.GetSpriteAtlas("res/store_pixel_background.png");
    }

    // Scale uniformly and center the portrait artwork without stretching it.
    private static double artScale() {
        return Math.min((double) Game.GAME_WIDTH / ART_WIDTH,
                        (double) Game.GAME_HEIGHT / ART_HEIGHT);
    }

    private static int artX() {
        return (Game.GAME_WIDTH - (int) Math.round(ART_WIDTH * artScale())) / 2;
    }

    private static int artY() {
        return (Game.GAME_HEIGHT - (int) Math.round(ART_HEIGHT * artScale())) / 2;
    }

    private static Rectangle area(int x1, int y1, int x2, int y2) {
        double scale = artScale();
        int left = artX() + (int) Math.round(x1 * scale);
        int top = artY() + (int) Math.round(y1 * scale);
        int right = artX() + (int) Math.round(x2 * scale);
        int bottom = artY() + (int) Math.round(y2 * scale);
        return new Rectangle(left, top, right - left, bottom - top);
    }

    @Override public void update() { }

    private void centered(Graphics g, String s, int centerX, int baseline) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, centerX - fm.stringWidth(s) / 2, baseline);
    }

    private void shadeButton(Graphics g, Rectangle r, boolean hover, boolean pressed) {
        if (hover || pressed) {
            g.setColor(pressed ? new Color(0, 0, 0, 170) : new Color(170, 190, 215, 105));
            g.fillRect(r.x + 5, r.y + 5, r.width - 10, r.height - 10);
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
        g.drawImage(backgroundImg, artX(), artY(),
                (int) Math.round(ART_WIDTH * artScale()),
                (int) Math.round(ART_HEIGHT * artScale()), null);
        shadeButton(g, buyBounds, buyHover, buyPressed);
        shadeButton(g, backBounds, backHover, backPressed);

        // The artwork contains a sample balance; replace it with the live balance.
        Rectangle coinArea = area(0, 0, 232, 56);
        g.setColor(new Color(1, 12, 38));
        g.fillRect(coinArea.x, coinArea.y, coinArea.width, coinArea.height);
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, Math.max(15, Game.GAME_HEIGHT / 44)));
        String username = UserAccounts.getCurrentUser();
        String coins = "--";
        if (username != null) {
            try { coins = String.valueOf(UserAccounts.getCoins(username)); }
            catch (IOException ex) { coins = "ERR"; }
        }
        g.drawString("Coins: " + coins, coinArea.x + 8, coinArea.y + Math.max(14, coinArea.height * 3 / 4));

        // Display logged-in username in the top-right box
String playerName = "Player: " + UserAccounts.getCurrentUser();
        Rectangle nameBox = area(965, 0, 1175, 55);

        if (playerName != null) {
            g.setColor(Color.WHITE);

            int fontSize = Math.max(12,
                    (int) (27 * artScale()));

            g.setFont(new Font(Font.MONOSPACED,
                    Font.BOLD, fontSize));

            // Reduce font size for longer usernames
            while (g.getFontMetrics().stringWidth(playerName)
                    > nameBox.width - 10 && fontSize > 8) {
                fontSize--;
                g.setFont(new Font(Font.MONOSPACED,
                        Font.BOLD, fontSize));
            }

            FontMetrics fm = g.getFontMetrics();

            int x = nameBox.x +
                    (nameBox.width - fm.stringWidth(playerName)) / 2;

            int y = nameBox.y +
                    (nameBox.height - fm.getHeight()) / 2 +
                    fm.getAscent();

            g.drawString(playerName, x, y);
        }

        // Show purchased card IDs over the four question-mark slots.
        if (lastPack != null) {
            for (int i = 0; i < Math.min(4, lastPack.length); i++) {
                Rectangle slot = area(237 + i * 187, 840, 378 + i * 187, 1010);
                g.setColor(new Color(4, 23, 65, 245));
                g.fillRect(slot.x + 3, slot.y + 3, slot.width - 6, slot.height - 6);
                g.setColor(new Color(104, 190, 255));
                g.drawRect(slot.x + 3, slot.y + 3, slot.width - 6, slot.height - 6);
                g.setColor(Color.WHITE);
                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, Math.max(13, Game.GAME_HEIGHT / 40)));
                centered(g, "CARD", slot.x + slot.width / 2, slot.y + slot.height / 2 - 5);
                centered(g, "#" + (lastPack[i] + 1), slot.x + slot.width / 2, slot.y + slot.height / 2 + 24);
            }
        }
        if (!status.isEmpty()) {
            Rectangle msg = area(205, 1045, 975, 1086);
            g.setColor(new Color(2, 15, 43, 235));
            g.fillRect(msg.x, msg.y, msg.width, msg.height);
            g.setColor(Color.WHITE);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, Math.max(12, Game.GAME_HEIGHT / 56)));
            centered(g, status, msg.x + msg.width / 2, msg.y + msg.height * 3 / 4);
        }
    }

    @Override public void mouseClicked(MouseEvent e) { }

    @Override public void mousePressed(MouseEvent e) {
        buyPressed = buyBounds.contains(e.getPoint());
        backPressed = backBounds.contains(e.getPoint());
    }

    @Override public void mouseReleased(MouseEvent e) {
        if (backPressed && backBounds.contains(e.getPoint())) {
            try { game.getMenu().loadUserData(); }
            catch (IOException | IllegalStateException ex) { /* Menu will refresh on next login. */ }
            Gamestate.state = Gamestate.MENU;
        } else if (buyPressed && buyBounds.contains(e.getPoint())) {
            String username = UserAccounts.getCurrentUser();
            if (username == null) {
                status = "Log in before buying cards.";
            } else {
                try {
                    lastPack = UserAccounts.buyCardPack(username, PACK_PRICE);
                    game.getMenu().loadUserData();
                    status = "Purchased! Four cards saved to your collection.";
                } catch (IllegalStateException ex) {
                    status = ex.getMessage();
                } catch (IOException ex) {
                    status = "Purchase failed: " + ex.getMessage();
                }
            }
        }
        buyPressed = false;
        backPressed = false;
        buyHover = buyBounds.contains(e.getPoint());
        backHover = backBounds.contains(e.getPoint());
    }

    @Override public void mouseMoved(MouseEvent e) {
        buyHover = buyBounds.contains(e.getPoint());
        backHover = backBounds.contains(e.getPoint());
    }
    @Override public void keyPressed(KeyEvent e) { }
    @Override public void keyReleased(KeyEvent e) { }
}