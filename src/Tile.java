import java.awt.*;
import java.util.Locale;

public class Tile {
    TileType type;
    int offsetY = 0;
    int offsetX = 0;
    int multiplier = 1;
    boolean soonToClear = false;

    public static final String BLACK_BACKGROUND = "\033[40m"; // BLACK
    public static final String RED_BACKGROUND = "\033[41m"; // RED
    public static final String GREEN_BACKGROUND = "\033[42m"; // GREEN
    public static final String YELLOW_BACKGROUND = "\033[43m"; // YELLOW
    public static final String BLUE_BACKGROUND = "\033[44m"; // BLUE
    public static final String PURPLE_BACKGROUND = "\033[45m"; // PURPLE
    public static final String CYAN_BACKGROUND = "\033[46m"; // CYAN
    public static final String WHITE_BACKGROUND = "\033[47m"; // WHITE
    public static final String RESET = "\033[0m"; // Text Reset

    public Tile(TileType type) {
        this.type = type;
    }

    public Tile() {
        this(TileType.values()[(int) (Math.random() * TileType.values().length)]);
    }

    public Image getImage() {
        String imageName = type.name().toLowerCase(Locale.ROOT)+"tilesmall";
        return ImageFetcher.preparedImage(imageName);
    }

    public enum TileType {
        RED(RED_BACKGROUND+"#"+RESET),
        PINK(PURPLE_BACKGROUND+"#"+RESET),
        ORANGE(WHITE_BACKGROUND+"#"+RESET),
        YELLOW(YELLOW_BACKGROUND+"#"+RESET),
        GREEN(GREEN_BACKGROUND+"#"+RESET),
        BLUE(BLUE_BACKGROUND+"#"+RESET);

        String symbol;

        TileType(String s) {
            symbol = s;
        }
    }
}
