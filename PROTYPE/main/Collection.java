import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;


public class Collection implements ActionListener {
    private JFrame frame = new JFrame();
    private JLabel label = new JLabel("collection");
    private JPanel backMenu = new JPanel(new BorderLayout());

    public Collection() throws Exception {
        int rows = 5;
        int cols = 4;
        int horizontalGap = 5;  // pixels between columns
        int verticalGap = 5;    // pixels between rows

        JButton backButton = new JButton("Back to Menu"); //create back button
        backMenu.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //border padding
        backMenu.add(backButton, BorderLayout.WEST); //put button into backMenu panel
        backButton.addActionListener(this);

        JPanel gridContainer = new JPanel(new GridLayout(rows, cols, horizontalGap, verticalGap));//create grid
        gridContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //border padding

        int totalCells = rows * cols;
        for (int i = 1; i <= totalCells; i++) { //goes through each grid
            JPanel cards = new JPanel();

            BufferedImage ogicon = ImageIO.read(new File("PROTYPE\\main\\image\\64x64file.png")); //sprite sheet placeholder
            BufferedImage subimage = ogicon.getSubimage(10, 5, 4, 6); //get subimage from sprite sheet

            Image image = subimage.getScaledInstance(100, 100, Image.SCALE_SMOOTH); //resize image to fit grid
            ImageIcon scaledIcon = new ImageIcon(image); //set back to icon
            JButton button = new JButton(scaledIcon); //makes button into scaled image

            //for setting image of each card
            cards.setBackground(Color.BLUE/*add image replace blue*/); 
            cards.setBorder(new LineBorder(Color.BLACK, 1));
            cards.add(button);
            button.addActionListener(this);

            //words in each card (temporary) change later
            JLabel label = new JLabel("Card " + i);
            label.setForeground(Color.WHITE);
            cards.add(label);

            //Add the card to the grid container
            gridContainer.add(cards);
        }
        // set up the frame and display it
        frame.add(gridContainer, BorderLayout.CENTER);
        frame.add(backMenu, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Collection");
        frame.pack();
        frame.setVisible(true);
        frame.setSize(900, 900);
        frame.setResizable(false);
        }

    // process the button clicks
    public void actionPerformed(ActionEvent e) { //add click function
        frame.dispose(); //close current frame
    }

    // create one Frame
    public static void main(String[] args) throws Exception{
        new Collection();
    }
}
