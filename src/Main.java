
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Time;
import java.util.Objects;

public class Main implements Runnable {
    public static int tileHeight = 50;
    public static int tileOffsetX = 50;
    public static int tileOffsetY = 37;
    static Canvas gameCanvas;
    static Game game;
    static Phase phase = Phase.MENU;
    public static void main(String[] args) {

        ImageFetcher.initializeImages();
        FontFetcher.initializeFonts();
        gameCanvas = new Canvas();

        JFrame frame = createFrame();

        frame.setContentPane(gameCanvas);

        game = new MainGame();
        // game = new TimedGame();
        game.render(gameCanvas);

        Main mainInstance = new Main();
        mainInstance.run();
    }

    private static JFrame createFrame() {
        Application frame = new Application("puzzle tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(new Dimension(1000, 850));
        frame.setResizable(false);

        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Application.mouseData.setClicked(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Application.mouseData.updateXY(e);
            }
        });
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getExtendedKeyCode();
                Application.keyData.setPressed(key);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getExtendedKeyCode();
                Application.keyData.setReleased(key);
            }
        });

        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                Application.mouseData.setClicked(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Application.mouseData.setReleased(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Application.mouseData.updateXY(e);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Application.mouseData.updateXY(e);
            }
        });
        return frame;
    }

    @Override
    public void run() {
        game.render(gameCanvas);

        boolean running = true;
        while (running) {
            gameCanvas.clear();

            if (phase == Phase.GAME) {
                game.runFrame();
                // game.runFrame();
                game.render(gameCanvas);
            } else if (phase == Phase.MENU) {
                runMainMenu();
                renderMainMenu(gameCanvas);
            }


            gameCanvas.repaint();

            // game.grid.toConsole();
            Application.advanceFrame();

            try {
                Thread.sleep(40);
            } catch (InterruptedException err) {
                running = false;
                System.out.println(err.getMessage());
            }
        }
    }

    private void renderMainMenu(Canvas c) {
        c.imagesToRender.push(new RenderedImage(ImageFetcher.getImage("logoSmall"), 20, 250));
        c.imagesToRender.push(new RenderedImage(ImageFetcher.getImage("classicSmall"), 480, 180));
        c.imagesToRender.push(new RenderedImage(ImageFetcher.getImage("scoreSmall"), 480, 410));
        c.imagesToRender.push(new RenderedImage(ImageFetcher.getImage("timeSmall"), 480+230, 410));
    }
    private void runMainMenu() {
        if (Application.mouseData.inBox(485, 211,959, 404) && Application.mouseData.getIsClicked()) {
            // Classic mode
            phase = Phase.GAME;
            game = new MainGame();
        }
        if (Application.mouseData.inBox(483, 442, 705, 653) && Application.mouseData.getIsClicked()) {
            // Score mode
            phase = Phase.GAME;
            game = new ScoreGame();
        }
        if (Application.mouseData.inBox(727, 447, 954, 666) && Application.mouseData.getIsClicked()) {
            // Time mode
            phase = Phase.GAME;
            game = new TimedGame();

        }
    }
}