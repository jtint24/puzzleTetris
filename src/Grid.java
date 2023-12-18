import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Grid implements Renderable {
    Tile[][] tiles;

    public Grid(int width, int height) {
        tiles = new Tile[width][height];
    }

    public int removeLines() {
        int scoreSum = 0;

        // Identify vertical lines

        for (int i = 0; i<tiles.length; i++) {
            ArrayList<Tile> matchedTiles = new ArrayList<>();
            int startJ = 0;

            for (int j = 0; j<tiles[0].length; j++) {
                Tile searchedTile = tiles[i][j];

                if (searchedTile == null || !tileIsResting(i,j) || (matchedTiles.size() > 0 && searchedTile.type != matchedTiles.get(0).type)) {
                    scoreSum += removeLines(i, startJ, i+1, j, matchedTiles);
                    startJ = j;
                    matchedTiles = new ArrayList<>();
                }
                if (searchedTile != null && tileIsResting(i,j)) {
                    matchedTiles.add(searchedTile);
                }
            }

            scoreSum += removeLines(i, startJ, i+1, tiles[0].length, matchedTiles);
        }

        // Identify horizontal lines
        for (int j = 0; j<tiles[0].length; j++) {

            ArrayList<Tile> matchedTiles = new ArrayList<>();
            int startI = 0;

            for (int i = 0; i<tiles.length; i++) {
                Tile searchedTile = tiles[i][j];

                if (searchedTile == null || !tileIsResting(i,j) || (matchedTiles.size() > 0 && searchedTile.type != matchedTiles.get(0).type)) {
                    scoreSum += removeLines(startI, j, i, j+1, matchedTiles);
                    startI = i;
                    matchedTiles = new ArrayList<>();
                }

                if (searchedTile != null && tileIsResting(i,j)) {
                    matchedTiles.add(searchedTile);
                }
            }

            scoreSum += removeLines(startI, j, tiles.length, j+1, matchedTiles);
        }

        // Remove tiles in lines
        for (int i = 0; i<tiles.length; i++) {
            for (int j = 0; j<tiles[0].length; j++) {
                if (tiles[i][j] != null && tiles[i][j].soonToClear) {
                    tiles[i][j] = null;

                    if (j >= 1 && tiles[i][j-1] != null) {
                        tiles[i][j-1].multiplier += 1;
                    }
                }
            }
        }

        // Return combined score
        return scoreSum;
    }

    private int removeLines(int startI, int startJ, int endI, int endJ, ArrayList<Tile> matchedTiles) {
        if (matchedTiles.size() < 3) {
            return 0;
        }

        for (int i = startI; i<endI; i++) {
            for (int j = startJ; j<endJ; j++) {
                // tiles[i][j] = null;
            }
        }

        int maxMultiplier = 1;

        for (Tile tile : matchedTiles) {
            tile.soonToClear = true;
            maxMultiplier = Math.max(maxMultiplier, tile.multiplier);
        }

        return getScoreForClear(matchedTiles.size(), maxMultiplier);
    }

    public int getScoreForClear(int tiles, int multiplier) {
        return tiles * 100 * multiplier;
    }

    public int runFrame() {
        dropTiles();
        int scoreBonus = removeLines();
        resetMultipliers();

        // Return the score bonus
        return scoreBonus;
    }

    public void resetMultipliers() {
        // reset multipliers of blocks that are on the ground;

        for (int i = 0; i<tiles.length; i++) {
            for (int j = 0; j<tiles[0].length; j++) {
                Tile searchedTile = tiles[i][j];

                if (searchedTile != null && tileIsResting(i,j)) {
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
            return (tiles[x][y+1] != null) && tileIsResting(x, y+1);
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

    @Override
    public void render(Canvas c) {

        for (int i = 0; i<tiles.length; i++) {
            for (int j = 0; j<tiles[0].length; j++) {
                Tile searchedTile = tiles[i][j];

                if (searchedTile != null) {
                    // name = "greenTileSmall";
                    Image tileImage = searchedTile.getImage();

                    RenderedImage renderedImage = new RenderedImage(tileImage, Main.tileHeight * i + searchedTile.offsetX, Main.tileHeight * j + searchedTile.offsetY);
                    c.imagesToRender.push(renderedImage);
                }

            }
        }
    }
}
