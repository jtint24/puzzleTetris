import java.awt.*;
import java.util.Collections;
import java.util.List;
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

    public Tile(List<TileType> types) {
        this(types.get((int) (types.size() * Math.random()) ));
    }



    public Image getImage() {
        if (Application.frameCount % 12 < 6 && soonToClear) {
            return ImageFetcher.preparedImage("cleartile");
        }
        String imageName = type.name().toLowerCase(Locale.ROOT)+"TileSketchSmall";
        return ImageFetcher.preparedImage(imageName);
    }

    public enum TileType {
        RED(RED_BACKGROUND+"#"+RESET, new Color(246, 13, 90)),
        PINK(PURPLE_BACKGROUND+"#"+RESET, new Color(236, 74, 242)),
        ORANGE(WHITE_BACKGROUND+"#"+RESET, new Color(228, 120, 40)),
        YELLOW(YELLOW_BACKGROUND+"#"+RESET, new Color(255, 240, 11)),
        GREEN(GREEN_BACKGROUND+"#"+RESET, new Color(51, 228, 46)),
        BLUE(BLUE_BACKGROUND+"#"+RESET, new Color(45, 137, 251));

        String symbol;
        Color color;

        TileType(String s, Color c) {
            symbol = s;
            color = c;
        }
    }
}