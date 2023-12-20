import java.awt.*;

public class RenderedText {
    String label;
    int x;
    int y;
    Color color = Color.BLACK;

    public RenderedText(String label, int x, int y) {
        this.label = label;
        this.x = x;
        this.y = y;
    }
    public RenderedText(String label, int x, int y, Color color) {
        this(label, x, y);
        this.color = color;
    }

    public Object clone() {
        return new RenderedText(label, x, y, color);
    }
}
