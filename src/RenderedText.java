public class RenderedText {
    String label;
    int x;
    int y;

    public RenderedText(String label, int x, int y) {
        this.label = label;
        this.x = x;
        this.y = y;
    }

    public Object clone() {
        return new RenderedText(label, x, y);
    }
}
