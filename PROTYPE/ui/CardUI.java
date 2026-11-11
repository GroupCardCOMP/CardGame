package ui;

import gamestates.Gamestate;
import gamestates.Playing;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;


public class CardUI {

    private Gamestate state;
    private BufferedImage[] imgs;
    public int[] cards;
    public Playing playing;


    public int[] clubsArray = {0, 4, 8, 12, 16};
    public int[] clubsAdd = {1, 2, 3, 4, 5};
    public int[] heartsArray = {1, 5, 9, 13, 17};
    public int[] heartAdd = {5, 10, 15, 20, 25};
    public int[] diamondsArray = {2, 6, 10, 14, 18};
    public int[] diamondAdd = {5, 10, 15, 20 , 25};
    public int[] spadesArray = {3, 7, 11, 15, 19};
    public int[] spadeAdd = {5, 10, 15, 20, 25};

    public CardUI(Playing playing) {
        this.playing = playing;
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
