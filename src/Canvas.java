import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class Canvas extends JPanel {
    public Stack<RenderedImage> imagesToRender = new Stack<>();
    public Stack<RenderedText> textToRender = new Stack<>();

    Canvas() {
        setPreferredSize(new Dimension(200, 200));
        this.setBackground(new Color(230, 230, 255));
    }


    public Graphics paintBackground(Graphics g) {
        int offset = Application.frameCount/3 % 100 - 100;
        for (int i = 0; i<11; i++) {
            for (int j = 0; j<10; j++) {
                if ((i+j) % 2 == 0) {
                    g.drawImage(ImageFetcher.getImage("splotchSmall"), offset+i*100, offset+j*100, null);
                }
            }
        }

        if (Main.phase == Phase.MENU) {
            ((Graphics2D) g).setPaint(new Color(230, 230, 255));
            g.fillRect(0, 150, 1000, 530);
        }

        return g;
    }

    public Graphics paintGameUI(Graphics g) {



        ((Graphics2D)g).setPaint(new Color(0x80FFFFFF, true));
        g.fillRoundRect(Main.tileOffsetX-25,Main.tileOffsetY-25,10*Main.tileHeight+50, 15*Main.tileHeight+50, 50,50);

        for (int i = 0; i<10; i++) {
            int j = 2;
            g.drawImage(ImageFetcher.preparedImage("splotchDot"), i*Main.tileOffsetX+Main.tileOffsetX, j*Main.tileOffsetY+Main.tileOffsetY, null);
        }

        ((Graphics2D)g).setPaint(new Color(0xCCFFFFFF, true));
        g.fillRect(Main.tileOffsetX,Main.tileOffsetY,10*Main.tileHeight, 15*Main.tileHeight);


        // Score

        ((Graphics2D)g).setPaint(new Color(0x80FFFFFF, true));
        g.fillRoundRect(Main.tileOffsetX+10*Main.tileHeight+75,Main.tileOffsetY-25,6*Main.tileHeight+50, Main.tileHeight+50, 50,50);

        ((Graphics2D)g).setPaint(new Color(0xCCFFFFFF, true));
        g.fillRect(Main.tileOffsetX+10*Main.tileHeight+100,Main.tileOffsetY,6*Main.tileHeight, Main.tileHeight);

        // Time

        ((Graphics2D)g).setPaint(new Color(0x80FFFFFF, true));
        g.fillRoundRect(Main.tileOffsetX+10*Main.tileHeight+75,Main.tileOffsetY-25+3*Main.tileHeight,6*Main.tileHeight+50, Main.tileHeight+50, 50,50);

        ((Graphics2D)g).setPaint(new Color(0xCCFFFFFF, true));
        g.fillRect(Main.tileOffsetX+10*Main.tileHeight+100,Main.tileOffsetY+3*Main.tileHeight,6*Main.tileHeight, Main.tileHeight);

        // Level

        if (!(Main.game instanceof TimedGame)) {
            ((Graphics2D) g).setPaint(new Color(0x80FFFFFF, true));
            g.fillRoundRect(Main.tileOffsetX + 10 * Main.tileHeight + 75, Main.tileOffsetY - 25 + 6 * Main.tileHeight, 6 * Main.tileHeight + 50, Main.tileHeight + 50, 50, 50);

            ((Graphics2D) g).setPaint(new Color(0xCCFFFFFF, true));
            g.fillRect(Main.tileOffsetX + 10 * Main.tileHeight + 100, Main.tileOffsetY + 6 * Main.tileHeight, 6 * Main.tileHeight, Main.tileHeight);
        }

        return g;
    }
    @Override
    public void paintComponent(Graphics g) {

        g = paintBackground(g);

        if (Main.phase == Phase.GAME) {
            g = paintGameUI(g);
        }

        Stack<RenderedImage> imagesToRenderCopy = (Stack<RenderedImage>) imagesToRender.clone();
        // System.out.println(imagesToRenderCopy.size());
        for (RenderedImage i : imagesToRenderCopy) {
            g.drawImage(i.image, i.x, i.y, null);
        }

        Stack<RenderedText> textToRenderCopy = (Stack<RenderedText>) textToRender.clone();
        g.setFont(FontFetcher.getFont("jost.ttf", 40, TextAttribute.WEIGHT_BOLD, Color.BLACK));

        for (RenderedText text : textToRenderCopy) {
            g.setFont(FontFetcher.getFont("jost.ttf", 40, TextAttribute.WEIGHT_BOLD, text.color));
            g.drawString(text.label, text.x, text.y);
        }
    }

    void clear() {
        imagesToRender = new Stack<>();
        textToRender = new Stack<>();
    }
}