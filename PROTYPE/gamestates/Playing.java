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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import main.Game;
import main.UserAccounts;
import ui.BackButton;
import ui.CardUI;
import utilz.LoadSave;

public class Playing extends State implements Statemethods {
    
    private BackButton[] backButton = new BackButton[1];

    private BufferedImage backgroundImg;

    private int menuX, menuY, menuWidth, menuHeight;

    private final Rectangle nextButton = new Rectangle(Game.GAME_WIDTH - 200,
         Game.GAME_HEIGHT - 100, 140, 50);
    private boolean nextButtonPressed;

    public String username;
    private int coins;

    public int[] playerCards;

    public int[] playerDuplicates;

    private int[] botplayerCards = new int[19];

    private int health = 100;
    public int PlayerOneHealth = health;
    public int botHealth = health;
    
    public int PlayerOneSh = 0;
    public int botShield = 0;

    public int PlayerOneAttack;
    public int BotAttack;

    final private int winCoins = 50;

    
    public int[] dealersHand = {0, 0, 0, 0};
    public int[] playerOneHand = {0, 0, 0, 0};
    public int[] botHand = {0, 0, 0, 0};

    public List<Integer> applyPlayerHand = new ArrayList<>();
    public List<Integer> transPlayerHand = new ArrayList<>();
    public List<Integer> PlayerHand = new ArrayList<>();

    public int[] CardsNum = {0 ,0, 0, 0};

    private final CardUI cardui;


    public int[] DealerDraw(int[] dealerdraw ) {
        int[] dealerDraw = {0, 1, 2, 3};
        Random random = new Random();

        for (int i = 0; i < dealerdraw.length; i++) {
            dealerdraw[i] =  random.nextInt(dealerDraw.length);
        }


    return dealerdraw;
    }

    public int[] BotDraw(int[] botdraw){
        Random random = new Random();

        for (int i = 0; i < botdraw.length; i++) {
            botdraw[i] = random.nextInt(botplayerCards.length);
        }
    return botdraw;
    }
    
    public int[] PlayerDraw(int[] playerdraw) {
        Arrays.fill(playerdraw, -1);
        List<Integer> availableCards = new ArrayList<>();

        for (int cardId = 0; cardId < playerCards.length; cardId++) {
            if (playerCards[cardId] == 1) {
                int copies = 1 + playerDuplicates[cardId];

                for (int copy = 0; copy < copies; copy++) {
                    availableCards.add(cardId);
                }
            }
        }

        Random random = new Random();

        for (int i = 0; i < playerdraw.length && !availableCards.isEmpty(); i++) {
            int randomIndex = random.nextInt(availableCards.size());
            playerdraw[i] = availableCards.remove(randomIndex);
        }

        return playerdraw;
    }

    public void Deal() {
        DealerDraw(dealersHand);
        BotDraw(botHand);
        PlayerDraw(playerOneHand);
        Evalute();
    }

    public void Evalute() {
        List<Integer> tempPlayerList = new ArrayList<>(playerOneHand.length);

        for (int card : playerOneHand) {
            if (card >= 0) {
                tempPlayerList.add(card);
            }
        }

        Arrays.fill(CardsNum, 0);
        applyPlayerHand.clear();
        transPlayerHand.clear();
        PlayerHand.clear();

        for (int i = 0; i < dealersHand.length; i++) {
            int rank = dealersHand[i];
            if (rank >= 0 && rank < CardsNum.length) {
                CardsNum[rank]++;
            }
        }

        for (int rank = 0; rank < CardsNum.length; rank++) {
            if (CardsNum[rank] > 0) {
                cardui.CardActions(rank, CardsNum[rank], tempPlayerList);
            }
        }
    }




    


    public Playing(Game game) {
        super(game);
        cardui = new CardUI(this);
        loadButtons();
        loadBackground();
    }

    public void startGame() {
        Deal();
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

        int[] UserCards =  
            UserAccounts.getCards(loggedInUsername);

        int[] UserDup =
            UserAccounts.getDuplicates(loggedInUsername);
        username = loggedInUsername;
        coins = currentCoins;
        playerCards = UserCards;
        playerDuplicates = UserDup;
    }

    private void loadBackground() {

        backgroundImg = LoadSave.GetSpriteAtlas(
            LoadSave.ninebynine
        );

        menuWidth = (int) (backgroundImg.getWidth() * 1f);
        menuHeight = (int) (backgroundImg.getHeight() * 1f);

        menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (0 * Game.SCALE);
    }

    private void loadButtons() {
        backButton[0] = new BackButton(
            65,
             (int) (10 * Game.SCALEX),
            0, Gamestate.MENU
        );
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
            menuX, menuY,
            menuWidth, menuHeight,
            null
        );

        for (BackButton mb : backButton) {
            mb.draw(g);
        }

        g.setColor(Color.black);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, Math.max(12, Game.GAME_HEIGHT / 48)));

        FontMetrics metrics = g.getFontMetrics();

        int middleX = (int) (Game.GAME_WIDTH / 2);
        int middleY = (int) (Game.GAME_HEIGHT / 2);
        String DealerArrayToText = Arrays.toString(dealersHand);
        String PlayerUsedCard = applyPlayerHand.toString();
        String PlayerHandTrans = transPlayerHand.toString();
        String ActualHandused = PlayerHand.toString();

        String PlayerText = "player: " + (username == null ? "" : username);
        String DealerText = "dealerhand [" + DealerArrayToText + "]";
        String PlayerArrayToText = Arrays.toString(playerOneHand);
        String BotArrayToText = Arrays.toString(botHand);
        String PhealtString = (username == null ? "" : username) + " health: " + PlayerOneHealth;
        String bothealthStr = "bot health: " + botHealth;
        String playerShieldString = "Player shield: " + PlayerOneSh;
        String BotShieldString = "bot shield: " + botShield;
        String playerAttackString = "player damage output: " + PlayerOneAttack;
        String botAttackString = "bot damage output: " + BotAttack;
        String ApplyPlayerHand = "applys cards used: " + PlayerUsedCard;
        String ApplyPlayerTrans = "matching rank: " + PlayerHandTrans;
        String ApplyActualHand = "actual card used: " + ActualHandused;

        g.drawString(DealerText, middleX, middleY);

        g.drawString(playerAttackString, middleX, middleY + 100);
        g.drawString(PlayerText, middleX, middleY + 150);
        g.drawString(PlayerArrayToText, middleX, middleY + 200);
        g.drawString(ApplyActualHand, middleX, middleY + 250);
        g.drawString(ApplyPlayerHand, middleX, middleY + 300);
        g.drawString(ApplyPlayerTrans, middleX, middleY + 350);
        g.drawString(playerShieldString, middleX, middleY + 400);
        g.drawString(PhealtString, middleX, middleY + 430);

        g.drawString(botAttackString, middleX, middleY - 150);
        g.drawString("bot", middleX, middleY - 200);
        g.drawString(BotArrayToText, middleX, middleY - 250);
        g.drawString(BotShieldString, middleX, middleY - 300);
        g.drawString(bothealthStr, middleX, middleY - 350);

        g.drawRect(nextButton.x, nextButton.y, nextButton.width, nextButton.height);
        g.drawString("Next", nextButton.x + 45, nextButton.y + 30);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        nextButtonPressed = nextButton.contains(e.getPoint());

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

        if (nextButtonPressed && nextButton.contains(e.getPoint())) {
            Deal();
        }
        nextButtonPressed = false;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
