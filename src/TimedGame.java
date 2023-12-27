public class TimedGame extends Game {

    TimedGame() {
        super();
        // dropSpeed = 5;
    }
    public void runFrame() {
        super.runFrame();
        long elapsedTime = (System.currentTimeMillis() - super.startTimeMillis);
        if (elapsedTime >= 1000*60*2 && super.state != GameState.LOST) {
            super.state = GameState.LOST;
            super.lossTimeMillis = super.startTimeMillis + 1000*60*2;

        }
    }
}
