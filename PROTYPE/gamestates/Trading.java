package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.JOptionPane;
import main.Game;
import main.UserAccounts;
import tradeserver.*;
import ui.BackButton;
import ui.DisplayCollection;
import ui.StartTrade;
import utilz.LoadSave;


public class Trading extends State implements Statemethods{
    private BackButton[] backButton = new BackButton[1];
    private BufferedImage backgroundImg;
    private DisplayCollection[] cards = new DisplayCollection[20];
    private StartTrade[] trade = new StartTrade[1];
    private int X, Y, Width, Height;
    private boolean connected=false;
    public String username;
    public int[] playerCards;
    public int[] playerDuplicates;
    
    
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

        trade[0]= new StartTrade(832, 600, 0, Gamestate.TRADESERVER);//i dont know if this gamestate is needed
    }

    @Override
    public void update() {
        
       for (BackButton mb : backButton) {
            mb.update();
            return;
        }
        
        for (StartTrade mb : trade) {
            mb.update();
            return;
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
        try{
            loadUserData();
        } catch(IOException a){
            System.out.println("error");
        }
        String copies = "Copies of card: ";//add num copies
        int c = 250;
        int r = 208;
        for (int i=0; i<20;i++) { 
            copies = copies + playerDuplicates[i];
            c = (c +175); //how much space to add between cards, also increase initial start position
            if(i%5==0 && i!=0){ //after 5 cards changes the x and y values
                c=425; //start of a row, starting x + 175
                r=r+100; //increments y value
            }
            g.drawString(copies,c,r);
            copies = "Copies of card: ";
        }

        for (StartTrade t: trade) {
            t.draw(g);
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
                return;
            }
        }
        for (StartTrade mb : trade) {
            if (isIn(e, mb)) {
                mb.setMousePressed(true);
                return;
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
                            resetButtons();
                            return;
                    }
                }
                break;
            }
        }
        
        for (int i = 0; i < trade.length; i++) {
            if(connected){ //stops user from opening 2 trade panels. Dont allow trading with yourself. 
                JOptionPane.showMessageDialog(null,"Already connected", "Connection", JOptionPane.OK_OPTION);
                break;
            }
            StartTrade mb = trade[i];
            String[] options = {"Host (Player 1)", "Join (Player 2)"};// Pop-up dialog to choose Host (Player 1) or Client (Player 2)
            int choice = JOptionPane.showOptionDialog(null, "Select your role:", "Card Trader Setup",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            try{
                loadUserData();
            } catch(IOException a){
                System.out.println("error");
            }
            
            if (isIn(e, mb) && mb.isMousePressed()) {
                switch (i) {
                    case 0: {
                        if (choice == 0) { //calls CardTrade popup, become host
                            new CardTrade(username, playerCards, playerDuplicates, true, "localhost", 5000);
                            connected=true;
                            
                        } else if (choice == 1) { //calls CardTrade popup, join as 2nd player
                            new CardTrade(username, playerCards, playerDuplicates, false, "localhost", 5000);
                            connected=true;
                        }
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
        for (StartTrade mb : trade) {
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

    public void loadUserData() throws IOException {
        String loggedInUsername =
            UserAccounts.getCurrentUser();

        if (loggedInUsername == null) {
            throw new IllegalStateException(
                "No user is logged in."
            );
        }
        int[] UserCards =  
            UserAccounts.getCards(loggedInUsername);

        int[] UserDup =
            UserAccounts.getDuplicates(loggedInUsername);
        username = loggedInUsername;
        playerCards = UserCards;
        playerDuplicates = UserDup;
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
