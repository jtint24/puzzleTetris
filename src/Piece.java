public class Piece implements Renderable {
    Tile[] tiles;
    Point[] locations;
    int headX = 1;
    int headY = 1;

    int pieceType;

    Piece(Tile[] tiles, Point[] locations, int pieceType) {
        this.tiles = tiles;
        this.locations = locations;
        this.pieceType = pieceType;
    }

    public static Piece getRandomPiece() {
        // Types of pieces:
        // L
        // N
        // N-flip
        // I
        // O
        // Y


        Tile.TileType[] types = new Tile.TileType[2];
        types[0] = Tile.TileType.values()[(int) (Math.random()*Tile.TileType.values().length)];
        do {
            types[1] = Tile.TileType.values()[(int) (Math.random() * Tile.TileType.values().length)];
        } while (types[1] == types[0]);

        int pieceType = (int) (Math.random() * 6);
        Tile[] tiles = new Tile[4];
        for (int i = 0; i<4; i++) {
            tiles[i] = new Tile(types[(int) (2*Math.random())]);
        }

        while (tiles[0].type == tiles[1].type && tiles[0].type == tiles[2].type && tiles[0].type == tiles[3].type) {
            tiles[(int) (Math.random()*4)].type = Tile.TileType.values()[(int) (Math.random() * Tile.TileType.values().length)];
        }

        Point[] locations = new Point[4];

        switch (pieceType) {
            case 0 -> { // I
                locations = new Point[]{
                        new Point(-2, 0),
                        new Point(-1, 0),
                        new Point(0, 0),
                        new Point(1, 0),
                };
            }
            case 1 -> { // N
                locations = new Point[]{
                        new Point(-1,0),
                        new Point(0,0),
                        new Point(0,1),
                        new Point(1,1),
                };
            }
            case 2 -> { // N flip
                locations = new Point[] {
                        new Point(-1,1),
                        new Point(0, 1),
                        new Point(0,0),
                        new Point(1,0)
                };
            }
            case 3 -> { // L
                locations = new Point[] {
                        new Point(-1,0),
                        new Point(0,0),
                        new Point(1,1),
                        new Point(1,0)
                };
            }
            // (-1, 1) -> (0, 1) -> (0, 0) -> (-1, 0)
            //  (y-1, -x) cc (-y, 1+x) cl
            case 4 -> { // O
                locations = new Point[] {
                        new Point(-1,1),
                        new Point(0,1),
                        new Point(-1, 0),
                        new Point(0,0)
                };
            }
            case 5 -> { // Y
                locations = new Point[] {
                        new Point(-1,0),
                        new Point(0,0),
                        new Point(1,0),
                        new Point(0,1)
                };
            }
        }

        return new Piece(tiles, locations, pieceType);
    }

    public void rotateClockwise() {
        // y <- -x
        // x <- y
        for (int i = 0; i<4; i++) {
            int oldX = locations[i].x;

            if (pieceType == 4 || pieceType == 0) {
                locations[i].x = -locations[i].y;
                locations[i].y = oldX+1;
            } else {
                locations[i].x = -locations[i].y;
                locations[i].y = oldX;
            }
        }
    }

    public void rotateCounterclockwise() {
        // y <- x
        // x <- -y
        for (int i = 0; i<4; i++) {
            int oldX = locations[i].x;

            if (pieceType == 4 || pieceType == 0) {
                locations[i].x = locations[i].y - 1;
                locations[i].y = -oldX;
            } else {
                locations[i].x = locations[i].y;
                locations[i].y = -oldX;
            }
        }
    }

    @Override
    public void render(Canvas c) {
        for (int i = 0; i<4; i++) {
            Tile tile = tiles[i];
            Point location = locations[i];

            RenderedImage renderedImage = new RenderedImage(
                    tile.getImage(),
                    (location.x+headX)*Main.tileHeight + tile.offsetX,
                    (location.y+headY)*Main.tileHeight + tile.offsetY
            );

            c.imagesToRender.push(renderedImage);

        }
    }
}

