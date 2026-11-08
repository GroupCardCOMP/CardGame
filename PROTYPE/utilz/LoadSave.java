package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class LoadSave {

    public static final String MenuButton = "res/button_atlas-2.png";
    public static final String MenuBackground = "res/menu_background-2.png";
    public static final String AccMenuButton = "res/button_atlas.png";
    public static final String AccMenuBackground = "res/menu_BackGround.png";
    public static final String backButton = "res/back_button.png";
    public static final String ninebynine = "res/900x900.png";

    public static BufferedImage GetSpriteAtlas(String fileName) {
        BufferedImage img = null;
        InputStream is = LoadSave.class.getResourceAsStream( "/" + fileName);
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return img;
    }


}
