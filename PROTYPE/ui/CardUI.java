package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import utilz.LoadSave;


public class CardUI {

    private Gamestate state;
    private BufferedImage[] imgs;
    private BufferedImage[] dealersImgs;
    private BufferedImage backImg;
    public int[] cards;
    public Playing playing;

    private static final int[] CARD_X = {119, 360, 601, 842};
    private static final int[] ROW_Y = {130, 504, 879};
    private static final int CARD_WIDTH = 240;
    private static final int CARD_HEIGHT = 300;


    public int[] clubsArray = {0, 4, 8, 12, 16};
    public int[] clubsAdd = {1, 2, 3, 4, 5};
    public int[] heartsArray = {1, 5, 9, 13, 17};
    public int[] heartAdd = {1, 2, 3, 4, 5};
    public int[] diamondsArray = {2, 6, 10, 14, 18};
    public int[] diamondAdd = {5, 10, 15, 20 , 25};
    public int[] spadesArray = {3, 7, 11, 15, 19};
    public int[] spadeAdd = {5, 10, 15, 20, 25};

    public CardUI(Playing playing) {
        this.playing = playing;
        loadImgs();
        loadDealersImgs();
        backImg = LoadSave.GetSpriteAtlas("res/back_of_card.png");
    }

    public void CardActions(int rank, int cardNum, List<Integer> PlayCards) {
        switch (rank) {
            case 0:
                findPlayercards(cardNum, PlayCards, clubsArray, clubsAdd);
                break;
            case 1:
                findPlayercards(cardNum, PlayCards, heartsArray, heartAdd);
                break;
            case 2:
                findPlayercards(cardNum, PlayCards, diamondsArray, diamondAdd);
                break;
            case 3:
                findPlayercards(cardNum, PlayCards, spadesArray, spadeAdd);
                break;
            default:
                throw new IllegalArgumentException("Invalid card rank: " + rank);
        }
    }

    public void CardActionsBot(int rank, int cardNum, List<Integer> PlayCards){
        switch (rank) {
            case 0:
                findBotCards(cardNum, PlayCards, clubsArray, clubsAdd);
                break;
            case 1:
                findBotCards(cardNum, PlayCards, heartsArray, heartAdd);
                break;
            case 2:
                findBotCards(cardNum, PlayCards, diamondsArray, diamondAdd);
                break;
            case 3:
                findBotCards(cardNum, PlayCards, spadesArray, spadeAdd);
                break;
            default:
                throw new IllegalArgumentException("Invalid card rank: " + rank);
        }
    }

    public void findBotCards(int cardNum, 
        List<Integer> botsCards, 
        int[] cardsUsed, 
        int[] cardsUseApplied
    ){
        int cardsToFind = cardNum;

        for (int i = 0; i < botsCards.size() && cardsToFind > 0;) {
            int cardId = botsCards.get(i);
            int matchingIndex = -1;

            for (int j = 0;j <  cardsUsed.length; j++) {
                if (cardsUsed[j] == cardId) {
                    matchingIndex = j;
                    break;
                }
            }
            if(matchingIndex < 0) {
                i++;
                continue;
            }
            playing.applyBotHand.add(cardsUseApplied[matchingIndex]);
            playing.transBotHand.add(cardsUsed[0]);
            playing.BotHandAct.add(cardId);
            botsCards.remove(i);
            cardsToFind--;
        }
    }

    public void findPlayercards(
        int cardNum,
        List<Integer> playersCards,
        int[] cardsUsed,
        int[] cardsUseApplied
    ) {
        int cardsToFind = cardNum;

        for (int i = 0; i < playersCards.size() && cardsToFind > 0;) {
            int cardId = playersCards.get(i);
            int matchingIndex = -1;

            for (int j = 0; j < cardsUsed.length; j++) {
                if (cardsUsed[j] == cardId) {
                    matchingIndex = j;
                    break;
                }
            }

            if (matchingIndex < 0) {
                i++;
                continue;
            }

            playing.applyPlayerHand.add(cardsUseApplied[matchingIndex]);
            playing.transPlayerHand.add(cardsUsed[0]);
            playing.PlayerHand.add(cardId);
            playersCards.remove(i);
            cardsToFind--;
        }
    }


    private void loadImgs() {
        imgs = new BufferedImage[20];
        for(int i = 0; i < imgs.length; i++){
            imgs[i] = LoadSave.GetSpriteAtlas(
                "res/" + i + ".png");
        }
    }

    private void loadDealersImgs(){
        dealersImgs = new BufferedImage[4];
        for(int i = 0; i < dealersImgs.length; i++){
            dealersImgs[i] = LoadSave.GetSpriteAtlas(
            "res/d" + i + ".png");
        }

    }

    public void drawHands(
        Graphics g,
        BufferedImage background,
        int backgroundX,
        int backgroundY,
        int backgroundWidth,
        int backgroundHeight,
        boolean showOnlyAppliedCards
    ) {
        drawHand(
            g, playing.botHand, imgs,
            showOnlyAppliedCards ? playing.BotHandAct : null, 0,
            background, backgroundX, backgroundY,
            backgroundWidth, backgroundHeight
        );
        drawHand(
            g, playing.dealersHand, dealersImgs, null, 1,
            background, backgroundX, backgroundY,
            backgroundWidth, backgroundHeight
        );
        drawHand(
            g, playing.playerOneHand, imgs,
            showOnlyAppliedCards ? playing.PlayerHand : null, 2,
            background, backgroundX, backgroundY,
            backgroundWidth, backgroundHeight
        );
    }

    private void drawHand(
        Graphics g,
        int[] hand,
        BufferedImage[] images,
        List<Integer> faceUpCards,
        int row,
        BufferedImage background,
        int backgroundX,
        int backgroundY,
        int backgroundWidth,
        int backgroundHeight
    ) {
        List<Integer> remainingFaceUpCards = faceUpCards == null
            ? null
            : new ArrayList<>(faceUpCards);

        for (int i = 0; i < CARD_X.length; i++) {
            int cardId = i < hand.length ? hand[i] : -1;
            BufferedImage cardImage = backImg;

            if (cardId >= 0 && cardId < images.length) {
                if (remainingFaceUpCards == null) {
                    cardImage = images[cardId];
                } else {
                    int faceUpIndex = remainingFaceUpCards.indexOf(cardId);
                    if (faceUpIndex >= 0) {
                        remainingFaceUpCards.remove(faceUpIndex);
                        cardImage = images[cardId];
                    }
                }
            }

            int x = backgroundX
                + CARD_X[i] * backgroundWidth / background.getWidth();
            int y = backgroundY
                + ROW_Y[row] * backgroundHeight / background.getHeight();
            int width = CARD_WIDTH * backgroundWidth / background.getWidth();
            int height = CARD_HEIGHT * backgroundHeight / background.getHeight();

            g.drawImage(cardImage, x, y, width, height, null);
        }
    }

    public void update() {

    }

    public void applyGamestate() {
        Gamestate.state = state;
    }

}
