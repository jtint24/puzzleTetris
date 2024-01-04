import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.List;

public class Game implements Renderable {
    Grid grid;
    Piece activePiece;
    int score = 0;
    GameState state = GameState.COUNT_IN;
    int stateChangeFrame = 0;
    long startTimeMillis = System.currentTimeMillis();
    long lossTimeMillis = 0;
    List<Tile.TileType> availableTypes;
    int dropSpeed = 5;
    long pauseTimeMillis = 0;

    boolean paused = false;

    Game() {
        reset();
    }

    public void runFrame() {
        if (state == GameState.COUNT_IN) {
            long millisElapsed = System.currentTimeMillis()-startTimeMillis;
            int seconds = (int) (millisElapsed % 60000)/1000;
            if (seconds > 3) {
                startTimeMillis = System.currentTimeMillis();
                state = GameState.PLAY;
            }
        } else {
            if (Application.keyData.getIsTyped(KeyEvent.VK_ENTER)) {
                paused = !paused;
                pauseTimeMillis = System.currentTimeMillis()-startTimeMillis;
            }
            if (paused) {
                startTimeMillis = System.currentTimeMillis()-pauseTimeMillis;
                return;
            }

            if (activePiece == null && state == GameState.PLAY ) {
                getNewPiece();
            }

            int lineCount = grid.lineClears.size();
            int scoreBonus;

            if (state == GameState.PLAY) {
                scoreBonus = grid.runFrame();
            } else if (state == GameState.LOST) {
                if (lossTimeMillis == 0) {
                    lossTimeMillis = System.currentTimeMillis();
                }
                activePiece = null;
                scoreBonus = grid.runLossFrame();

                if (!grid.existsVisibleTile() && grid.lineClears.size() == 0) {
                    state = GameState.TRY_AGAIN;
                }
            } else if (state == GameState.TRY_AGAIN) {
                scoreBonus = 0;
            } else if (grid.isFull()) {
                scoreBonus = grid.runLineClears();
                state = GameState.LOST;
            } else {
                scoreBonus = 0;
            }

            score += scoreBonus;

            if (lineCount < grid.lineClears.size() && state == GameState.PLAY) {
                stateChangeFrame = Application.frameCount;
            }

            // System.out.println(stateChangeFrame+" "+Application.frameCount);
            // System.out.println(state);

            // score += 2;
            //if (state == GameState.PLAY) {
                movePiece();
            //}
        }
    }

    public void movePiece() {
        if (activePiece == null) {
            return;
        }
        // System.out.println(pieceConflicts());
        if (Application.keyData.getIsTyped(KeyEvent.VK_LEFT)) {
            // System.out.println("Left!");

            activePiece.headX--;
            if (pieceConflicts()) {
                activePiece.headX++;
            }
        } else if (Application.keyData.getIsTyped(KeyEvent.VK_RIGHT)) {
            // System.out.println("Right!");
            activePiece.headX++;
            if (pieceConflicts()) {
                activePiece.headX--;
            }
        }
        if (Application.keyData.getIsPressed(KeyEvent.VK_SPACE)) {
            dropPiece(10, true);
            if (pieceConflicts()) {

                raisePiece(10);
            }
        }
        if (Application.keyData.getIsTyped(KeyEvent.VK_UP)) {
            activePiece.rotateCounterclockwise();
            if (pieceConflicts()) {
                activePiece.rotateClockwise();
            }
        }
        if (Application.keyData.getIsTyped(KeyEvent.VK_DOWN)) {
            activePiece.rotateClockwise();
            if (pieceConflicts()) {
                activePiece.rotateCounterclockwise();
            }
        }
        //if (Application.frameCount % 10 == 0) {
        dropPiece(dropSpeed, false);
        //}
        if (pieceConflicts()) {
            raisePiece(dropSpeed);
            copyPieceToGrid();
            activePiece = null;
            // getNewPiece();
        }
    }

    private void raisePiece(int diff) {
        if (activePiece == null) {
            return;
        }
        for (int i = 0; i<4; i++) {
            activePiece.tiles[i].offsetY -= diff;
        }
        if (activePiece.tiles[0].offsetY < -Main.tileHeight) {
            activePiece.headY--;

            for (int i = 0; i<4; i++) {
                activePiece.tiles[i].offsetY += Main.tileHeight; // 0;
            }
        }
    }

    private void dropPiece(int diff, boolean forceDropped) {
        if (activePiece == null) {
            return;
        }
        for (int i = 0; i<4; i++) {
            activePiece.tiles[i].offsetY += diff;
        }
        if (activePiece.tiles[0].offsetY >= 0) {
            activePiece.headY++;
            if (forceDropped) {
                score += 1;
            }

            for (int i = 0; i<4; i++) {
                activePiece.tiles[i].offsetY -= Main.tileHeight;
            }
        }
    }

    public void getNewPiece() {

        activePiece = Piece.getRandomPiece(availableTypes);
        activePiece.headX = 5;
        /*
        for (int i = 0; i<6; i++) {
            activePiece.headX = 6-i;
            if (!pieceConflicts()) {
                return;
            }
            activePiece.headX = 6+i;
            if (!pieceConflicts())  {
                return;
            }
        }
        */

        if (pieceConflicts()) {
            state = GameState.LOST;
            activePiece = null;
        }

        // activePiece = null;
    }

    public void copyPieceToGrid() {
        for (int i = 0; i<4; i++) {
            Point location = activePiece.locations[i];
            Tile tile = activePiece.tiles[i];
            tile.offsetX=0;
            tile.offsetY=0;

            grid.tiles[location.x+activePiece.headX][location.y+activePiece.headY] = tile;
        }
    }

    private boolean piecePermanentlyConflicts() {
        if (activePiece == null) {
            return true;
        }
        for (int i = 0; i<4; i++) {
            Point location = activePiece.locations[i];
            int x = location.x+activePiece.headX;
            int y = location.y+activePiece.headY;
            if (0>x || x>=grid.tiles.length || 0>y || y>=grid.tiles[0].length) {
                // System.out.println(x+","+y);
                return true;
                // continue;
            }
            if (grid.tiles[x][y] != null && grid.tileIsResting(x, y)) {
                return true;
            }
        }

        return false;
    }

    public boolean pieceConflicts() {
        if (activePiece == null) {
            return true;
        }
        for (int i = 0; i<4; i++) {
            Point location = activePiece.locations[i];
            int x = location.x+activePiece.headX;
            int y = location.y+activePiece.headY;
            if (0>x || x>=grid.tiles.length || 0>y || y>=grid.tiles[0].length) {
                // System.out.println(x+","+y);
                return true;
                // continue;
            }
            if (grid.tiles[location.x+activePiece.headX][location.y+activePiece.headY] != null || grid.soonToClearTiles[location.x+activePiece.headX][location.y+activePiece.headY] != null) {
                return true;
            }
        }

        return false;
    }

    public void reset() {
        grid = new Grid(10, 15);
        List<Tile.TileType> types = Arrays.asList(Tile.TileType.values());
        availableTypes = Utils.randomSubList(types, 3);

        // for (Tile.TileType type : availableTypes) {
            // System.out.println(type);
        // }

        getNewPiece();
        state = GameState.COUNT_IN;
        stateChangeFrame = 0;
        score = 0;
        startTimeMillis = System.currentTimeMillis();
        lossTimeMillis = 0;
    }

    @Override
    public void render(Canvas c) {
        grid.render(c);

        if (state == GameState.COUNT_IN) {
            long millisElapsed = System.currentTimeMillis()-startTimeMillis;
            int seconds = (int) (millisElapsed % 60000)/1000;
            String label = seconds > 2 ? "Go!" : ""+(3-seconds);
            c.textToRender.push(new RenderedText(label, Main.tileOffsetX+(int)(Main.tileHeight*4.5), Main.tileOffsetY+Main.tileOffsetY*9, Color.BLACK, 64));
        }



        if (activePiece != null && state != GameState.COUNT_IN) {
            activePiece.render(c);
        }

        // Score UI
        String scoreString = String.format("%010d", score);
        c.textToRender.push(new RenderedText(scoreString, Main.tileOffsetX+10*Main.tileHeight+100+10+10, Main.tileOffsetY+Main.tileOffsetY+5));

        // Time UI
        if (paused) {
            startTimeMillis = System.currentTimeMillis() - pauseTimeMillis;
        }

        long millisElapsed = System.currentTimeMillis()-startTimeMillis;
        if (lossTimeMillis >= startTimeMillis) {
            millisElapsed = lossTimeMillis - startTimeMillis;
        }
        int milliseconds = (int) (millisElapsed % 1000);
        int seconds = (int) ((millisElapsed % 60000) / 1000);
        int minutes = (int) ((millisElapsed % 3600000)) / 60000;
        int hours = (int) ((millisElapsed % 360000000)) / 3600000 % 10;

        if (state == GameState.COUNT_IN) {
            milliseconds = 0;
            seconds = 0;
            minutes = 0;
            hours = 0;
        }

        String timeString = String.format("%d:%02d'%02d\"%03d", hours, minutes, seconds, milliseconds);

        c.textToRender.push(new RenderedText(timeString, Main.tileOffsetX+10*Main.tileHeight+100+10+20, Main.tileOffsetY+Main.tileOffsetY*5+5));

        if (paused) {
            c.imagesToRender.push(new RenderedImage(ImageFetcher.getImage("pause"), 100, 100));
        }
    }
}
