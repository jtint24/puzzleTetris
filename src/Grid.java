import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Grid implements Renderable {
    Tile[][] tiles;
    Tile[][] soonToClearTiles;
    ArrayList<LineClear> lineClears = new ArrayList<>();
    Point[][] velocities;

    Sound[] clearSounds = new Sound[6];

    public Grid(int width, int height) {
        tiles = new Tile[width][height];
        soonToClearTiles = new Tile[width][height];
        velocities = new Point[width][height];

        for (int i = 1; i<6; i++) {
            clearSounds[i] = new Sound("pop"+i);
        }
    }
    public void removeLines() {
        removeLines(true);
    }
    public void removeLines(boolean mute) {
        int scoreSum = 0;

        // Identify vertical lines

        for (int i = 0; i<tiles.length; i++) {
            ArrayList<Tile> matchedTiles = new ArrayList<>();
            int startJ = 0;

            for (int j = 0; j<tiles[0].length; j++) {
                Tile searchedTile = tiles[i][j];

                if (searchedTile == null || !tileIsResting(i,j) || (matchedTiles.size() > 0 && searchedTile.type != matchedTiles.get(0).type)) {
                    scoreSum += removeLine(mute, i, startJ, i+1, j, matchedTiles);
                    startJ = j;
                    matchedTiles = new ArrayList<>();
                }
                if (searchedTile != null && tileIsResting(i,j)) {
                    matchedTiles.add(searchedTile);
                }
            }

            scoreSum += removeLine(mute, i, startJ, i+1, tiles[0].length, matchedTiles);
        }

        // Identify horizontal lines
        for (int j = 0; j<tiles[0].length; j++) {

            ArrayList<Tile> matchedTiles = new ArrayList<>();
            int startI = 0;

            for (int i = 0; i<tiles.length; i++) {
                Tile searchedTile = tiles[i][j];

                if (searchedTile == null || !tileIsResting(i,j) || (matchedTiles.size() > 0 && searchedTile.type != matchedTiles.get(0).type)) {
                    scoreSum += removeLine(mute, startI, j, i, j+1, matchedTiles);
                    startI = i;
                    matchedTiles = new ArrayList<>();
                }

                if (searchedTile != null && tileIsResting(i,j)) {
                    matchedTiles.add(searchedTile);
                }
            }

            scoreSum += removeLine(mute, startI, j, tiles.length, j+1, matchedTiles);
        }

        // Remove tiles in lines
        for (int i = 0; i<tiles.length; i++) {
            for (int j = 0; j<tiles[0].length; j++) {
                if (tiles[i][j] != null && tiles[i][j].soonToClear()) {
                    soonToClearTiles[i][j] = tiles[i][j];
                    tiles[i][j] = null;
                    int j_diff = 1;
                    while (tiles[i][j-j_diff] != null ) {
                        tiles[i][j - j_diff].multiplier += 1;
                        j_diff++;
                    }
                }
            }
        }

        // Return combined score
        // return scoreSum;
    }

    private int removeLine(boolean mute, int startI, int startJ, int endI, int endJ, ArrayList<Tile> matchedTiles) {
        if (matchedTiles.size() < 3) {
            return 0;
        }

        int maxMultiplier = 1;

        for (Tile tile : matchedTiles) {
            tile.clearFrame = Application.frameCount;
            maxMultiplier = Math.max(maxMultiplier, tile.multiplier);
        }


        int score = getScoreForClear(matchedTiles.size(), maxMultiplier);

        lineClears.add(new LineClear(score, maxMultiplier, matchedTiles.get(0).type, startI*Main.tileHeight, startJ*Main.tileHeight));
        if (!mute) {
            clearSounds[Math.min(5, maxMultiplier)].play();
        }
        return score;
    }

    public int getScoreForClear(int tiles, int multiplier) {
        return tiles * 30 * multiplier;
    }

    public int runFrame(boolean mute) {
        dropTiles();
        removeLines(mute);
        resetMultipliers();
        removeSoonToClearTiles();
        int scoreBonus = runLineClears();

        // Return the score bonus
        return scoreBonus;
    }

    public void resetMultipliers() {
        // reset multipliers of blocks that are on the ground;

        for (int i = 0; i<tiles.length; i++) {
            for (int j = 0; j<tiles[0].length; j++) {
                Tile searchedTile = tiles[i][j];

                if (searchedTile != null && tileIsPermanentlyResting(i,j)) {
                    searchedTile.multiplier = 1;
                }
            }
        }

    }

    public ArrayList<Tile> getAllTiles() {
        ArrayList<Tile> allTiles = new ArrayList<>();
        for (Tile[] tileRow : tiles) {
            Collections.addAll(allTiles, tileRow);
        }

        return allTiles;
    }

    public void dropTiles() {
        // Find blocks where the block beneath it is null
        // Drop them a pixel

        for (int i = 0; i<tiles.length; i++) {
            for (int j = tiles[0].length-1; j>=0; j--) {
                Tile searchedTile = tiles[i][j];

                if (searchedTile != null && !tileIsResting(i,j)) {

                    searchedTile.offsetY += 15;

                    if (searchedTile.offsetY >= Main.tileHeight) {
                        tiles[i][j + 1] = searchedTile;
                        tiles[i][j] = null;
                        searchedTile.offsetY = 0;
                    }
                }
            }
        }
    }

    public boolean tileIsResting(int x, int y) {
        if (y >= tiles[0].length-1) {
            return true;
        } else {
            return (tiles[x][y+1] != null || soonToClearTiles[x][y+1] != null) && tileIsResting(x, y+1);
        }
    }

    public boolean tileIsPermanentlyResting(int x, int y) {
        if (y >= tiles[0].length-1) {
            return true;
        } else {
            return (tiles[x][y+1] != null) && tileIsPermanentlyResting(x, y+1);
        }
    }

    public void swapTiles(int x, int y) {
        // Where x is of the leftmost square of the cursor
        Tile temp = tiles[x][y];
        tiles[x][y] = tiles[x+1][y];
        tiles[x+1][y] = temp;
    }

    public void toConsole() {
        for (int i = 0; i<tiles[0].length; i++) {
            for (int j = 0; j<tiles.length; j++) {
                Tile searchedTile = tiles[j][i];
                if (searchedTile == null) {
                    System.out.print("-");
                } else {
                    System.out.print(searchedTile.type.symbol);
                }
            }
            System.out.println();
        }
    }

    public boolean isFull() {
        for (int i = 0; i<tiles.length; i++) {
            //if (tiles[i][0] != null && tileIsResting(i, 0)) {
            if (tiles[i][2] != null && tileIsResting(i,2)) {
                // System.out.println("Board is full!");
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(Canvas c) {

        for (int i = 0; i<tiles.length; i++) {
            for (int j = 0; j<tiles[0].length; j++) {
                Tile searchedTile = tiles[i][j];
                if (soonToClearTiles[i][j] != null) {
                    searchedTile = soonToClearTiles[i][j];
                }

                if (searchedTile != null) {
                    // name = "greenTileSmall";
                    Image tileImage = searchedTile.getImage();

                    RenderedImage renderedImage = new RenderedImage(tileImage, Main.tileHeight * i + searchedTile.offsetX+Main.tileOffsetX, Main.tileHeight * j + searchedTile.offsetY+Main.tileOffsetY);
                    c.imagesToRender.push(renderedImage);
                }

            }
        }

        for (LineClear lineClear : lineClears) {
            lineClear.render(c);
        }
    }

    public int runLineClears() {
        ArrayList<LineClear> lineClearsToRemove = new ArrayList<>();
        for (LineClear lineClear : lineClears) {
            lineClear.runFrame();
            if (lineClear.atDestination()) {
                lineClearsToRemove.add(lineClear);
            }
        }
        int scoreBonus = 0;

        for (LineClear lineClear : lineClearsToRemove) {
            lineClears.remove(lineClear);
            scoreBonus += lineClear.scoreBonus;
        }

        return scoreBonus;
    }

    public boolean existsVisibleTile() {
        int boardHeight = tiles[0].length;
        for (int i = 0; i<tiles.length; i++) {
            for (int j = 0; j<tiles[0].length; j++) {
                Tile tile = tiles[i][j];
                if (tile != null) {
                    if ((boardHeight-j)*Main.tileHeight+100 > tiles[i][j].offsetY) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public int runLossFrame() {
        int scoreBonus = runLineClears();

        // System.out.println("running loss frame");

        for (int i = 0; i<velocities.length; i++) {
            for (int j = 0; j<velocities[0].length; j++) {
                if (velocities[i][j] == null) {
                    velocities[i][j] = new Point(0,0);
                }

                velocities[i][j].y += 2;

                if (tiles[i][j] != null) {
                    tiles[i][j].offsetY += velocities[i][j].y;
                    tiles[i][j].offsetY += Math.abs(5-i);
                }
            }
        }

        removeAllSoonToClearTiles();

        return scoreBonus;
    }

    public void removeSoonToClearTiles() {
        for (int i = 0; i<tiles.length; i++) {
            for (int j = 0; j<tiles[0].length; j++) {
                Tile tile = soonToClearTiles[i][j];
                if (tile != null && tile.soonToClear() && tile.clearFrame+15 < Application.frameCount) {
                    soonToClearTiles[i][j] = null;
                }
            }
        }
    }

    public void removeAllSoonToClearTiles() {
        for (int i = 0; i<tiles.length; i++) {
            for (int j = 0; j<tiles[0].length; j++) {
                Tile tile = soonToClearTiles[i][j];
                if (tile != null) {
                    soonToClearTiles[i][j] = null;
                }
            }
        }
    }
}
