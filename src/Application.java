import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Application extends JFrame {
    static KeyData keyData = new KeyData();
    static MouseData mouseData = new MouseData();
    static int frameCount = 0;

    public Application(String title) {
        super(title);
    }

    static void advanceFrame() {
        frameCount++;
        keyData.runFrame();
        mouseData.runFrame();
    }

    static class MouseData {
        private Component clickedComponent;
        private double mouseX = 0;
        private double mouseY = 0;
        private boolean clicked = false;
        private boolean pressed = false;

        Component getClickedComponent() {
            return clickedComponent;
        }

        double getMouseX() {
            return mouseX;
        }

        double getMouseY() {
            return mouseY;
        }

        boolean getIsClicked() {
            return clicked;
        }

        boolean getIsPressed() {
            return pressed;
        }

        void setClicked(MouseEvent e) {
            clicked = true;
            pressed = true;
            clickedComponent = e.getComponent();
            updateXY(e);
        }

        void setReleased(MouseEvent e) {
            clicked = false;
            pressed = false;
            clickedComponent = null;
            updateXY(e);
        }

        void updateXY(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }

        boolean inBox(int minX, int minY, int maxX, int maxY) {
            return minX < mouseX && mouseX < maxX && minY < mouseY && mouseY < maxY;
        }

        void runFrame() {
            clicked = false;
            // System.out.println(mouseX+", "+mouseY);
        }
    }

    static class KeyData {
        private final ConcurrentHashMap<Integer, Boolean> data = new ConcurrentHashMap<>();
        private final ConcurrentHashMap<Integer, Boolean> typeData = new ConcurrentHashMap<>();

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
