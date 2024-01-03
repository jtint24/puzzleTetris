public class TimedGame extends Game {

    TimedGame() {
        super();
        // dropSpeed = 5;
    }
    public void runFrame() {
        super.runFrame();
        long elapsedTime = (System.currentTimeMillis() - super.startTimeMillis);
        if (elapsedTime >= 1000*60*2 && super.state != GameState.TRY_AGAIN) {
            super.state = GameState.LOST;
            super.lossTimeMillis = super.startTimeMillis + 1000*60*2;
        }
    }

    @Override
    public void render(Canvas c) {
        super.render(c);

        if (state == GameState.TRY_AGAIN) {

            if (lossTimeMillis < super.startTimeMillis+1000*60*2) {
                c.imagesToRender.push(new RenderedImage(ImageFetcher.getImage("loss"), 100, 100));
            } else {
                c.imagesToRender.push(new RenderedImage(ImageFetcher.getImage("timeup"), 100, 100));
            }

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
