import javax.swing.text.AttributeSet;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class FontFetcher {
    public static HashMap<String, Font> fonts = new HashMap<>();

    public static String[] fontNames = {
            "jost-italic.ttf",
            "jost.ttf"
    };

    public static void initializeFonts() {
        for (String name : fontNames) {
            try {
                Font font = Font.createFont(Font.TRUETYPE_FONT, Canvas.class.getResource(name).openStream());
                fonts.put(name, font);
            } catch (IOException ioe) {
                System.out.println("Can't find "+name);
            } catch (FontFormatException ffe) {
                System.out.println(name+" is improperly formatted");
            }
        }
    }

    public static Font getFont(String name, int size, float weight, Color color) {
        Font base = getFont(name);

        HashMap<TextAttribute, Object> attributes = new HashMap<>();

        attributes.put(TextAttribute.WEIGHT, weight);
        attributes.put(TextAttribute.SIZE, (float) size);
        attributes.put(TextAttribute.FOREGROUND, color);
        // attributes.put(TextAttribute.OU)

        return base.deriveFont(attributes);
    }

    public static Font getFont(String name) {
        if (fonts.containsKey(name)) {
            return fonts.get(name);
        } else {
            throw new IllegalArgumentException("Can't find a font named "+name+". Maybe you forgot to put it in the initializer list?");
        }
    }
}
