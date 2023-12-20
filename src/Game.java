import javax.swing.*;
import java.awt.event.KeyEvent;

public class Game implements Renderable {
    Grid grid;
    Piece activePiece;
    int score = 0;
    GameState state = GameState.PLAY;
    int stateChangeFrame = 0;

    Game() {
        grid = new Grid(10, 15);
        Tile.TileType[] values = Tile.TileType.values();
        /* for (int i = 0; i<6; i++) {
            for (int j = 5; j<10; j++) {
                Tile.TileType value = values[Math.min(i%6, j%6)];
                grid.tiles[i][j] = new Tile(value);
            }
        }
        grid.tiles[2][4] = new Tile(Tile.TileType.ORANGE);*/

        getNewPiece();

    }

    public void runFrame() {
        int lineCount = grid.lineClears.size();
        int scoreBonus;

        if (state == GameState.PLAY) {
            scoreBonus = grid.runFrame();
        } else if (grid.isFull()) {
            scoreBonus = grid.runLineClears();
            state = GameState.LOST;
        } else {
            scoreBonus = grid.runLineClears();
        }

        score += scoreBonus;

        if (lineCount < grid.lineClears.size() && state == GameState.PLAY) {
            state = GameState.PAUSE;
            stateChangeFrame = Application.frameCount;
        }

        System.out.println(stateChangeFrame+" "+Application.frameCount);
        System.out.println(state);
        if (stateChangeFrame+5 < Application.frameCount && state == GameState.PAUSE) {
            state = GameState.PLAY;
        }

        // score += 2;
        if (state == GameState.PLAY) {
            movePiece();
        }
    }

    public void movePiece() {
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
        dropPiece(5, false);
        //}
        if (pieceConflicts()) {
            raisePiece(5);
            copyPieceToGrid();
            getNewPiece();
        }
    }

    private void raisePiece(int diff) {
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
        activePiece = Piece.getRandomPiece();
        activePiece.headX = 6;

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
            if (grid.tiles[location.x+activePiece.headX][location.y+activePiece.headY] != null) {
                return true;
            }
        }

        return false;
    }



    @Override
    public void render(Canvas c) {
        grid.render(c);
        if (activePiece != null) {
            activePiece.render(c);
        }

        // Score UI

        c.textToRender.push(new RenderedText(""+score, 500, 100));
    }
}
