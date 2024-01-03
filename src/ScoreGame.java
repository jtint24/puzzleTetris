import java.util.HashSet;

public class ScoreGame extends Game {
    int level = 1;

    public void runFrame() {
        super.runFrame();
        long elapsedTime = System.currentTimeMillis() - super.startTimeMillis;
        level = (int) (elapsedTime/1000/60) + 1;
        // level = (score / 2500)+1;
        dropSpeed = Math.min(level*2, Math.min(10, 2+level));
        if (level >= 5 && super.availableTypes.size() == 3 || (level >= 10 && super.availableTypes.size() == 4) || (level >= 15 && super.availableTypes.size() == 5)) {
            HashSet<Tile.TileType> typeSet = new HashSet<>(super.availableTypes);
            Tile.TileType newType;
            do {
                newType = Tile.TileType.values()[(int) (Math.random() * Tile.TileType.values().length)];
            } while (typeSet.contains(newType));
            super.availableTypes.add(newType);
        }

    }

    public void reset() {
        super.reset();
    }

    @Override
    public void render(Canvas c) {
        super.render(c);

        c.textToRender.push(new RenderedText("Level " + level, Main.tileOffsetX + 10 * Main.tileHeight + 100 + 10 + 20 + 30, Main.tileOffsetY + Main.tileOffsetY * 9 + 5));


        if (state == GameState.TRY_AGAIN) {
            c.imagesToRender.push(new RenderedImage(ImageFetcher.getImage("loss"), 100, 100));
            if (Application.mouseData.inBox(100, 300, 300, 340)) {
                c.textToRender.push(new RenderedText("Play Again", 100, 300, Tile.TileType.RED.color));
                if (Application.mouseData.getIsClicked()) {
                    reset();
                }
            } else {
                c.textToRender.push(new RenderedText("Play Again", 100, 300));
            }

            if (Application.mouseData.inBox(100, 360, 190, 400)) {
                c.textToRender.push(new RenderedText("Quit", 100, 360, Tile.TileType.RED.color));
                if (Application.mouseData.getIsClicked()) {
                    Main.phase = Phase.MENU;
                }
            } else {
                c.textToRender.push(new RenderedText("Quit", 100, 360));
            }
        }
    }
}
