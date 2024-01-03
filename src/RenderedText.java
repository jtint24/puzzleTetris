import java.awt.*;

public class RenderedText {
    String label;
    int x;
    int y;
    Color color = Color.BLACK;
    int size = 40;

    public RenderedText(String label, int x, int y) {
        this.label = label;
        this.x = x;
        this.y = y;
    }
    public RenderedText(String label, int x, int y, Color color) {
        this(label, x, y);
        this.color = color;
    }

    public RenderedText(String label, int x, int y, Color color, int size) {
        this(label, x, y, color);
        this.size = size;
    }

    public Object clone() {
        return new RenderedText(label, x, y, color);
    }
}
