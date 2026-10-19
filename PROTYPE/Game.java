

public class Game {

    private titlePage titlepage;
    private GamePanel gamePanel;
    
        public Game() {
            gamePanel = new GamePanel();
            titlepage = new titlePage(gamePanel);
        }
}
