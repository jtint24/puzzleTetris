import javax.swing.*;
import java.util.HashMap;

public class Application extends JFrame {
    static KeyData keyData = new KeyData();
    static int frameCount = 0;

    public Application(String title) {
        super(title);
    }

    static void advanceFrame() {
        frameCount++;
        keyData.runFrame();
    }

    static class KeyData {
        private final HashMap<Integer, Boolean> data = new HashMap<>();
        private final HashMap<Integer, Boolean> typeData = new HashMap<>();

        void setPressed(int key) {
            data.put(key, true);
            if (!typeData.containsKey(key)) {
                typeData.put(key, true);
            }
        }

        void setReleased(int key) {
            data.put(key, false);
            typeData.remove(key);
        }

        void runFrame() {
            typeData.replaceAll((k, v) -> false);
        }

        boolean getIsPressed(int key) {
            if (!data.containsKey(key)) {
                return false;
            }
            return data.get(key);
        }
        boolean getIsTyped(int key) {
            if (!typeData.containsKey(key)) {
                return false;
            }
            if (typeData == null) {
                return false;
            }
            return typeData.get(key);
        }
    }
}
