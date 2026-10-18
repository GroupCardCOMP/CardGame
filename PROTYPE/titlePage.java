

import javax.swing.JFrame;
import javax.swing.JPanel;

public class titlePage {
    private JFrame jframe;
    private JPanel gamePanel;

    public titlePage() {
        jframe = new JFrame();
        gamePanel = new JPanel();

        jframe.setSize(400, 400);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setVisible(true);
    }
}