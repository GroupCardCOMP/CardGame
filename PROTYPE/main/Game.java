package main;

import gamestates.Collection;
import gamestates.Gamestate;
import gamestates.Login;
import gamestates.Menu;
import gamestates.Playing;
import gamestates.Store;
import gamestates.Trading;
import java.awt.Graphics;

public class Game implements Runnable {

    private titlePage titlepage;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private Menu menu;
    private Login login;
    private Collection collection;
    private Playing playing;
    private Trading trade;
    private Store store;


    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2f;
    public final static float SCALEX = 1.3f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

        public Game() {
            initClasses();

            gamePanel = new GamePanel(this);
            titlepage = new titlePage(gamePanel);
            gamePanel.requestFocus();

            startGameLoop();
        }

        private void initClasses() {
            login = new Login(this);
            menu = new Menu(this);
            collection = new Collection(this);
            playing = new Playing(this);
            trade = new Trading(this);
            store = new Store(this);
        }

        private void startGameLoop() {
            gameThread = new Thread(this);
            gameThread.start();
        }

        public void update() {
            switch (Gamestate.state) {
                case LOGIN:
                    login.update();
                    break;
                case MENU:
                    menu.update();
                    break;
                case PLAYING:
                    playing.update();
                    break;
                case STORE:
                    store.update();
                    break;
                case COLLECTION:
                    collection.update();
                    break;
                case TRADE:
                    trade.update();
                    break;
                case OPTIONS:
                    // Update options state
                    break;
                case QUIT:
                    default:
                        System.exit(0); // Exit the game
                    // Update quit state
                    break;
            }
        }

        public void render(Graphics g) {
            switch (Gamestate.state) {
                case LOGIN:
                    login.draw(g);
                    break;
                case MENU:
                    menu.draw(g);
                    break;
                case PLAYING:
                    playing.draw(g);
                    break;
                case STORE:
                    store.draw(g);
                    break;
                case COLLECTION:
                    collection.draw(g);
                    break;
                case TRADE:
                    trade.draw(g);
                    break;
                case OPTIONS:
                    // Render options state
                    break;
                default:
                    // Render quit state
                    break;
            }
        }

    @Override
    public void run() {

        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;
        long previousTime = System.nanoTime();
        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();
        double deltaU = 0, deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if (deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }
            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }

    }

    public void windowFocusLost() {
        if (Gamestate.state == Gamestate.PLAYING) {
            // Handle window focus lost for playing state
        }
    }

    public Login getLogin(){
        return login;
    }

    public Menu getMenu() {
        return menu;
    }

    public Collection getCollection() {
        return collection;
    }

    public Playing getPlaying() {
        return playing;
    }

    public Trading getTrading() {
        return trade;
    }

    public Store getStore(){
        return store;
    }
}
