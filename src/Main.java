import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;

public class Main implements Runnable {
    public static int tileHeight = 50;
    static Canvas gameCanvas;
    static Game game;
    public static void main(String[] args) {

        ImageFetcher.initializeImages();
        gameCanvas = new Canvas();

        JFrame frame = createFrame();

        frame.setContentPane(gameCanvas);

        game = new Game();
        game.render(gameCanvas);

        Main mainInstance = new Main();
        mainInstance.run();
    }

    private static JFrame createFrame() {
        Application frame = new Application("puzzle tetris");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(new Dimension(600, 800));
        frame.setResizable(false);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getExtendedKeyCode();
                System.out.println(key);
                Application.keyData.setPressed(key);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getExtendedKeyCode();
                System.out.println(key);
                Application.keyData.setReleased(key);
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
            game.runFrame();

            // game.runFrame();
            game.render(gameCanvas);
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
}