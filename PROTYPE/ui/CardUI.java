package ui;

import gamestates.Gamestate;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class CardUI {

    private Gamestate state;
    private BufferedImage[] imgs;
    public int[] cards;

    



    private void loadImgs() {
        imgs = new BufferedImage[2];
    }

    public void draw(Graphics g) {

    }

    public void update() {

    }

    public void applyGamestate() {
        Gamestate.state = state;
    }

}
