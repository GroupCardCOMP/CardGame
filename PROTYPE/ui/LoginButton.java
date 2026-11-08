
package ui;

import gamestates.Gamestate;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import static utilz.Constants.UI.Buttons.*;
import utilz.LoadSave;

public class LoginButton {

    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTH / 2;
    private Gamestate state;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;
    private boolean themed;

    public LoginButton(
        int xPos, int yPos, int rowIndex,
        Gamestate state
    ) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        loadImgs();
        initBounds();
    }

    public LoginButton(
        int xPos, int yPos, int rowIndex,
        Gamestate state, int width, int height
    ) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.state = state;
        this.themed = true;
        this.xOffsetCenter = width / 2;

        this.bounds = new Rectangle(
            xPos - width / 2, yPos, width, height
        );

        this.imgs = new BufferedImage[3];

        imgs[0] = LoadSave.GetSpriteAtlas(
            "res/login_button_" + rowIndex + ".png"
        );

        imgs[1] = LoadSave.GetSpriteAtlas(
            "res/login_button_" + rowIndex + "_hover.png"
        );

        imgs[2] = LoadSave.GetSpriteAtlas(
            "res/login_button_" + rowIndex + "_pressed.png"
        );
    }

    private void initBounds() {
        bounds = new Rectangle(
            xPos - xOffsetCenter,
            yPos,
            B_WIDTH,
            B_HEIGHT
        );
    }

    private void loadImgs() {
        imgs = new BufferedImage[3];

        BufferedImage temp = LoadSave.GetSpriteAtlas(
            LoadSave.MenuButton
        );

        for (int i = 0; i < imgs.length; i++) {
            imgs[i] = temp.getSubimage(
                i * B_WIDTH_DEFAULT,
                rowIndex * B_HEIGHT_DEFAULT,
                B_WIDTH_DEFAULT,
                B_HEIGHT_DEFAULT
            );
        }
    }

    public void draw(Graphics g) {
        g.drawImage(
            imgs[index],
            bounds.x,
            bounds.y,
            bounds.width,
            bounds.height,
            null
        );
    }

    public void update() {
        index = 0;

        if (mouseOver) {
            index = 1;
        }

        if (mousePressed) {
            index = 2;
        }
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void applyGamestate() {
        Gamestate.state = state;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
