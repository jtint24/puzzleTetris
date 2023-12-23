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
        this.setBackground(new Color(255, 255, 255));
    }


    public Graphics paintBackground(Graphics g) {
        int offset = Application.frameCount/2 % 100;
        for (int i = 0; i<20; i++) {
            for (int j = 0; j<20; j++) {
                if ((i+j) % 2 == 0) {
                    g.drawImage(ImageFetcher.getImage("splotchSmall"), offset+i*100, offset+j*100, null);
                }
            }
        }

        ((Graphics2D)g).setPaint(new Color(0xEEFFFFFF, true));
        g.fillRoundRect(0,0,10*Main.tileHeight, 15*Main.tileHeight, 50,50);

        return g;
    }
    @Override
    public void paintComponent(Graphics g) {


        Stack<RenderedImage> imagesToRenderCopy = (Stack<RenderedImage>) imagesToRender.clone();
        // System.out.println(imagesToRenderCopy.size());
        for (RenderedImage i : imagesToRenderCopy) {
            g.drawImage(i.image, i.x, i.y, null);
        }

        Stack<RenderedText> textToRenderCopy = (Stack<RenderedText>) textToRender.clone();
        g.setFont(FontFetcher.getFont("jost-italic.ttf", 40, TextAttribute.WEIGHT_BOLD, Color.BLACK));

        for (RenderedText text : textToRenderCopy) {
            g.setFont(FontFetcher.getFont("jost-italic.ttf", 40, TextAttribute.WEIGHT_BOLD, text.color));
            g.drawString(text.label, text.x, text.y);
        }
    }

    void clear() {
        imagesToRender = new Stack<>();
        textToRender = new Stack<>();
    }
}