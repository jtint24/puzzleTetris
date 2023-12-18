import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ImageFetcher {

    static HashMap<String, BufferedImage> images = new HashMap<>();
    static String[] imageNames = {
            "redtile",
            "orangetile",
            "yellowtile",
            "pinktile",
            "greentile",
            "bluetile",
            "redtilesmall",
            "orangetilesmall",
            "yellowtilesmall",
            "pinktilesmall",
            "greentilesmall",
            "bluetilesmall",
    };

    public static Image getImage(String s) {
        if (!images.containsKey(s)) {
            throw new IllegalArgumentException("There is no image called " + s);
        }
        return images.get(s);
    }

    public static void initializeImages() {
        for (String name : imageNames) {
            try {
                //images.put(name, ImageIO.read(new File("src/resources/"+name+".png")));
                ClassLoader systemCL = ImageFetcher.class.getClassLoader();
                assert systemCL != null;

                // BufferedImage newImage = ImageIO.read(new FileInputStream("./resources/"+name+".png"));
                //URL resourceURL = Images.class.getResource(name);

                //BufferedImage newImage = ImageIO.read(new File(resourceURL.getFile()));
                BufferedImage newImage = ImageIO.read(ImageFetcher.class.getResource(name+".png"));
                images.put(name, newImage);
                //images.put(Toolkit.getDefaultToolkit().getImage(Images.class.getResource("src/resources/"+name+".png")));
            } catch (Exception e) {
                System.out.println(name + ".png not found");
                e.printStackTrace();
            }
        }
    }

    public static Image preparedImage(String name) {
        BufferedImage unscaledImage = (BufferedImage) getImage(name);
        return unscaledImage.getScaledInstance(Main.tileHeight, Main.tileHeight, Image.SCALE_DEFAULT);
    }
}