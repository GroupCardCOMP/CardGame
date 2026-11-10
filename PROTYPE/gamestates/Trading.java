package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import main.Game;
import ui.BackButton;
import ui.DisplayCollection;
import utilz.LoadSave;

public class Trading extends State implements Statemethods{
   
    private BackButton[] backButton = new BackButton[1];

    private BufferedImage backgroundImg;

    private DisplayCollection[] cards = new DisplayCollection[20];

    private int X, Y, Width, Height;

    int rows = 5;
    int cols = 4;


    public Trading(Game game) {
        super(game);

        loadButtons();
        loadBackground();
    }

    private void loadBackground() {

        backgroundImg = LoadSave.GetSpriteAtlas(
            LoadSave.ninebynine
        );

        Width = (int) (backgroundImg.getWidth() * 1f);
        Height = (int) (backgroundImg.getHeight() * 1f);

        X = Game.GAME_WIDTH / 2 - Width / 2;
        Y = (int) (0 * Game.SCALE);
    }

    private void loadButtons() {
        backButton[0] = new BackButton(
            65,
             (int) (10 * Game.SCALEX),
            0, Gamestate.MENU
        );
        
        //change these numbers depending on what fits in panel
        int c=307; //starting x value for cards
        int r=150; //starting y value for cards
        for (int i=0; i<20;i++) { 
            c = (c +175); //how much space to add between cards, also increase initial start position
            if(i%5==0 && i!=0){ //after 5 cards changes the x and y values
                c=482; //start of a row, starting x + 175
                r=r+100; //increments y value
            }
            cards[i] = new DisplayCollection(c, r, 0); //creates a card 
        }
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
            X, Y,
            Width, Height,
            null
        );

        for (BackButton mb : backButton) {
            mb.draw(g);
        }

        for (DisplayCollection c : cards){
            c.draw(g);
        }

        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
