package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static utilz.Constants.UI.Buttons.*;


public class DisplayCollection {
    private int xPos, yPos, rowIndex, index;
    private int xOffsetCenter = B_WIDTHS / 2;
    private BufferedImage[] imgs;
    private boolean mouseOver, mousePressed;
    private Rectangle bounds;

    public DisplayCollection(int xPos, int yPos, int rowIndex) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        loadImgs();
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(xPos - xOffsetCenter, yPos, B_WIDTHS, B_HEIGHTS);
    }

    private void loadImgs() {
        imgs = new BufferedImage[21]; 
        //get all card images
        BufferedImage temp= null; 
        try {
            temp = ImageIO.read(new File("PROTYPE/res/back_of_card.png"));
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        } 
        imgs[20]= temp;
        for (int i = 0; i < 20; i++) {
            String file = i+".png";
            try {
                temp = ImageIO.read(new File("PROTYPE/res/"+file));
            } catch (IOException e) {
                System.out.println("Error loading image: " + e.getMessage());
            } 
            imgs[i] = temp;
        }

    }

    public void draw(Graphics g, int i) {
        g.drawImage(imgs[i], xPos - xOffsetCenter, yPos,  B_WIDTHS, 150, null);
    }

    public void update() {
        // Update the button state if needed
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

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

}
