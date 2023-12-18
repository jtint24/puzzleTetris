import javax.swing.*;
import java.awt.event.KeyEvent;

public class Game implements Renderable {
    Grid grid;

    Piece activePiece;

    Game() {
        grid = new Grid(6, 10);
        Tile.TileType[] values = Tile.TileType.values();
        for (int i = 0; i<6; i++) {
            for (int j = 5; j<10; j++) {
                Tile.TileType value = values[Math.min(i%6, j%6)];
                grid.tiles[i][j] = new Tile(value);
            }
        }
        grid.tiles[2][4] = new Tile(Tile.TileType.ORANGE);

        activePiece = Piece.getRandomPiece();
        activePiece.headX = 3;
        activePiece.headY = 3;
    }

    public void runFrame() {

        grid.runFrame();
        movePiece();
    }

    public void movePiece() {
        // System.out.println(pieceConflicts());
        if (Application.keyData.getIsTyped(KeyEvent.VK_LEFT)) {
            System.out.println("Left!");

            activePiece.headX--;
            if (pieceConflicts()) {
                activePiece.headX++;
            }
        } else if (Application.keyData.getIsTyped(KeyEvent.VK_RIGHT)) {
            System.out.println("Right!");
            activePiece.headX++;
            if (pieceConflicts()) {
                activePiece.headX--;
            }
        }
        if (Application.frameCount % 10 == 0) {
            dropPiece();
        }
        if (pieceConflicts()) {
            raisePiece();
            copyPieceToGrid();
            getNewPiece();
        }
    }

    private void raisePiece() {
        activePiece.headY--;
    }

    private void dropPiece() {
        activePiece.headY++;
    }

    public void getNewPiece() {
        activePiece = Piece.getRandomPiece();
        // activePiece = null;
    }

    public void copyPieceToGrid() {
        for (int i = 0; i<4; i++) {
            Point location = activePiece.locations[i];
            Tile tile = activePiece.tiles[i];

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
                System.out.println(x+","+y);
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
    }
}
