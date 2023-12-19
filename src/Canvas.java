import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class Canvas extends JPanel {

    public Stack<RenderedImage> imagesToRender = new Stack<>();
    public Stack<RenderedText> textToRender = new Stack<>();

    Canvas() {
        setPreferredSize(new Dimension(200, 200));
        this.setBackground(new Color(255, 255, 255));
    }

    @Override
    public void paintComponent(Graphics g) {

        Stack<RenderedImage> imagesToRenderCopy = (Stack<RenderedImage>) imagesToRender.clone();
        // System.out.println(imagesToRenderCopy.size());
        for (RenderedImage i : imagesToRenderCopy) {
            g.drawImage(i.image, i.x, i.y, null);
        }

        Stack<RenderedText> textToRenderCopy = (Stack<RenderedText>) textToRender.clone();

        for (RenderedText text : textToRenderCopy) {
            g.drawString(text.label, text.x, text.y);
        }
    }

    void clear() {
        imagesToRender = new Stack<>();
        textToRender = new Stack<>();
    }
}