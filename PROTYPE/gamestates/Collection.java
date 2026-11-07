package gamestates;

import java.awt.Graphics;
import javax.swing.*;
import java.awt.Color;
import java.awt.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import main.UserAccounts;
import utilz.LoadSave;
import ui.MenuButton;


public class Collection extends State implements Statemethods{


    private void loadBackground() {
        BufferedImage backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MenuBackground);
        int menuWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
        int menuHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
        int menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
        int menuY = (int) (45 * Game.SCALE);

        // Create a JLabel to display the background image
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImg));
        backgroundLabel.setBounds(menuX, menuY, menuWidth, menuHeight);

        
    }

}
