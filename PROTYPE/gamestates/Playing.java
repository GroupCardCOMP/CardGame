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
import javax.swing.JOptionPane;
import main.Game;
import main.UserAccounts;
import ui.CardUI;
import utilz.LoadSave;

public class Playing extends State implements Statemethods {

    private enum Stages {
        DEALER_DRAW, BOTH_DRAW, EVALUATE, APPLY_EFFECTS, RESET, GAME_OVER
    }
    private Stages stage = Stages.DEALER_DRAW;

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
    private int[] botEasy = new int[3];
    private int[] botMed = {4, 5, 6, 7, 8, 9, 10, 11};
    private int[] botHard = {12, 13, 14, 15, 16, 17, 18, 19};

    int[][] botDiff = { BotDraw(botplayerCards), easyBotDraw(botEasy)};

    private final int health = 100;
    public int PlayerOneHealth = health;
    public int botHealth = health;

    public int PlayerHeart = 0;
    public int BotHeart = 0;

    public int PlayerClubs = 0;
    public int BotClubs = 0;

    public int PlayerOneSh = 0;
    public int botShield = 0;

    public int PlayerOneAttack = 0;
    public int BotAttack = 0;

    final private int winCoins = 50;

    
    public int[] dealersHand = {-1, -1, -1, -1};
    public int[] playerOneHand = {-1, -1, -1, -1};
    public int[] botHand = {-1, -1, -1, -1};

    public List<Integer> applyPlayerHand = new ArrayList<>();
    public List<Integer> transPlayerHand = new ArrayList<>();
    public List<Integer> PlayerHand = new ArrayList<>();

    public List<Integer> applyBotHand = new ArrayList<>();
    public List<Integer> transBotHand = new ArrayList<>();
    public List<Integer> BotHandAct = new ArrayList<>();

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

    public int[] easyBotDraw(int[] botdraw) {
        Random random = new Random();

        for (int i = 0; i < botdraw.length; i++) {
            botdraw[i] = random.nextInt(botEasy.length);
        }

        return botdraw;
    }

    public int[] mediumBotDraw(int[] botdraw){
        Random random = new Random();

        for(int i = 0; i < botdraw.length; i++) {
            int randomIndex = random.nextInt(botMed.length);
            botdraw[i] = botMed[randomIndex];
        }
        return botdraw;
    }

    public int[] hardBotDraw(int[] botdraw){
        Random random = new Random();

        for(int i = 0; i < botdraw.length; i++) {
            int randomIndex = random.nextInt(botHard.length);
            botdraw[i] = botHard[randomIndex];
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
        
    }

    public void Evalute() {
        List<Integer> tempPlayerList = new ArrayList<>(playerOneHand.length);
        List<Integer> tempBotList = new ArrayList<>(botHand.length);

        for (int card : playerOneHand) {
            if (card >= 0) {
                tempPlayerList.add(card);
            }
        }
        for (int card : botHand) {
            if (card >= 0) {
                tempBotList.add(card);
            }
        }

        Arrays.fill(CardsNum, 0);
        applyPlayerHand.clear();
        transPlayerHand.clear();
        PlayerHand.clear();

        applyBotHand.clear();
        transBotHand.clear();
        BotHandAct.clear();

        for (int i = 0; i < dealersHand.length; i++) {
            int rank = dealersHand[i];
            if (rank >= 0 && rank < CardsNum.length) {
                CardsNum[rank]++;
            }
        }

        for (int rank = 0; rank < CardsNum.length; rank++) {
            if (CardsNum[rank] > 0) {
                cardui.CardActions(rank, CardsNum[rank], tempPlayerList);
                cardui.CardActionsBot(rank, CardsNum[rank], tempBotList);
            }
        }
    }

    public void applyAllEffects(){
        applyPlayerEffects();
        applyBotEffects();
        int playerDamage = Math.max(0, BotAttack - PlayerOneSh);
        PlayerOneSh = Math.max(0, PlayerOneSh - BotAttack);
        PlayerOneHealth = Math.max(0, PlayerOneHealth - playerDamage);

        int botDamage = Math.max(0, PlayerOneAttack - botShield);
        botShield = Math.max(0, botShield - PlayerOneAttack);
        botHealth = Math.max(0, botHealth - botDamage);

    }

    public void applyPlayerEffects (){
        int healthToRestore = 0;
        boolean hasHeartEffect = false;

        for (int i = 0; i < transPlayerHand.size(); i++) {
            int amount = applyPlayerHand.get(i);
            switch (transPlayerHand.get(i)) {
                case 1:
                    PlayerHeart += amount;
                    healthToRestore += amount;
                    hasHeartEffect = true;
                    break;
                case 2:
                    PlayerOneSh += amount;
                    break;
                case 3:
                    PlayerOneAttack += amount;
                    break;
                default:
                    break;
            }
        }

        for (int i = 0; i < transPlayerHand.size(); i++) {
            if (transPlayerHand.get(i) != 0) {
                continue;
            }

            int amount = applyPlayerHand.get(i);
            PlayerClubs += amount;
            if (PlayerHeart != 0) {
                PlayerHeart += amount;
                if (hasHeartEffect) {
                    healthToRestore += amount;
                }
            }
            if (PlayerOneSh != 0) {
                PlayerOneSh += amount;
            }
            if (PlayerOneAttack != 0) {
                PlayerOneAttack += amount;
            }
        }

        PlayerOneHealth = Math.min(health, PlayerOneHealth + healthToRestore);

        }


    public void applyBotEffects(){
        int healthToRestore = 0;
        boolean hasHeartEffect = false;

        for (int i = 0; i < transBotHand.size(); i++) {
            int amount = applyBotHand.get(i);
            switch (transBotHand.get(i)) {
                case 1:
                    BotHeart += amount;
                    healthToRestore += amount;
                    hasHeartEffect = true;
                    break;
                case 2:
                    botShield += amount;
                    break;
                case 3:
                    BotAttack += amount;
                    break;
                default:
                    break;
            }
        }

        for (int i = 0; i < transBotHand.size(); i++) {
            if (transBotHand.get(i) != 0) {
                continue;
            }

            int amount = applyBotHand.get(i);
            BotClubs += amount;
            if (BotHeart != 0) {
                BotHeart += amount;
                if (hasHeartEffect) {
                    healthToRestore += amount;
                }
            }
            if (botShield != 0) {
                botShield += amount;
            }
            if (BotAttack != 0) {
                BotAttack += amount;
            }
        }

        botHealth = Math.min(health, botHealth + healthToRestore);

    }

    public void resetAll() {
        Arrays.fill(dealersHand, -1);
        Arrays.fill(botHand, -1);
        Arrays.fill(playerOneHand, -1);
        Arrays.fill(CardsNum, 0);
        applyPlayerHand.clear();
        transPlayerHand.clear();
        PlayerHand.clear();
        applyBotHand.clear();
        transBotHand.clear();
        BotHandAct.clear();
        botShield = 0;
        PlayerOneSh = 0;
        BotAttack = 0;
        BotAttack = 0;
        PlayerOneAttack = 0;
        BotHeart = 0;
        PlayerHeart = 0;
        BotClubs = 0;
        PlayerClubs = 0;
    }

    public void finishGame() {
        if (PlayerOneHealth > 0 && botHealth <= 0) {
            try {
                coins += winCoins;
                UserAccounts.updateCoins(username, coins);
                JOptionPane.showMessageDialog(null, "you win");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    null,
                    "Could not save your coin reward: " + ex.getMessage(),
                    "Game Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } else if (botHealth > 0) {
            // loser
            JOptionPane.showMessageDialog(null, "you lose");
        }else {
            // draw
        }
        Gamestate.state = Gamestate.GAMEOVER;
    }

    public void gameSwitch() {
        switch (stage) {
            case DEALER_DRAW:
                DealerDraw(dealersHand);
                stage = Stages.BOTH_DRAW;
                break;
            case BOTH_DRAW:
                BotDraw(botHand);
                PlayerDraw(playerOneHand);
                stage = Stages.EVALUATE;
                break;
            case EVALUATE:
                Evalute();
                stage = Stages.APPLY_EFFECTS;
                break;
            case APPLY_EFFECTS:
                applyAllEffects();
                if(PlayerOneHealth <= 0 || botHealth <= 0) {
                    finishGame();
                }else {
                    stage = Stages.RESET;
                }
                break;
            case RESET:
                resetAll();
                stage = Stages.DEALER_DRAW;
                break;
            case GAME_OVER:
                finishGame();
                stage = Stages.GAME_OVER;
                break;
            default:
                throw new IllegalStateException("dont know what stage" + stage);
        }

    }

    public Playing(Game game) {
        super(game);
        cardui = new CardUI(this);
        loadBackground();
    }

    public void startGame() {
        PlayerOneHealth = health;
        botHealth = health;
       resetAll();
       stage = Stages.DEALER_DRAW;
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
            "res/background_playing.png"
        );

        menuWidth = backgroundImg.getWidth();
        menuHeight = Game.GAME_HEIGHT;
        menuX =  Game.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = 0;

    }

    @Override
    public void update() {
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(
            backgroundImg,
            menuX, menuY,
            menuWidth, menuHeight,
            null
        );
        cardui.drawHands(
            g,
            backgroundImg,
            menuX,
            menuY,
            menuWidth,
            menuHeight,
            stage == Stages.APPLY_EFFECTS || stage == Stages.RESET
        );

        g.setColor(Color.white);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, Math.max(12, Game.GAME_HEIGHT / 48)));

        FontMetrics metrics = g.getFontMetrics();

        int middleX = (int) (Game.GAME_WIDTH / 2);
        int middleY = (int) (Game.GAME_HEIGHT / 2);
        

        String PlayerText = "player: " + (username == null ? "" : username);
       
        String PhealtString = (username == null ? "" : username) + " health: " + PlayerOneHealth;
        String bothealthStr = "bot health: " + botHealth;
        String playerShieldString = (username == null ? "" : username) + " shield: " + PlayerOneSh;
        String BotShieldString = "bot shield: " + botShield;
        String playerAttackString = (username == null ? "" : username) + " damage output: " + PlayerOneAttack;
        String botAttackString = "bot damage output: " + BotAttack;
        

        g.drawString(playerAttackString, middleX / 2 - 119, middleY + 145);
        g.drawString(PlayerText, middleX, middleY + 145);
       
        g.drawString(playerShieldString, middleX, middleY + 390);
        g.drawString(PhealtString, middleX, middleY + 410);

        g.drawString(botAttackString, middleX /2 -119, middleY - 140);
        g.drawString("bot", middleX , middleY - 140);
        
        g.drawString(BotShieldString, middleX, middleY - 390);
        g.drawString(bothealthStr, middleX, middleY - 410);

        g.setColor(Color.BLACK);
        g.drawRect(nextButton.x , nextButton.y, nextButton.width, nextButton.height);
        g.drawString("Next", nextButton.x + 45, nextButton.y + 30);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        nextButtonPressed = nextButton.contains(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (nextButtonPressed && nextButton.contains(e.getPoint())) {
            gameSwitch();
        }
        nextButtonPressed = false;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }

}
