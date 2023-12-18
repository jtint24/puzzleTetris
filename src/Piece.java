public class Piece implements Renderable {
    Tile[] tiles;
    Point[] locations;
    int headX = 1;
    int headY = 1;

    Piece(Tile[] tiles, Point[] locations) {
        this.tiles = tiles;
        this.locations = locations;
    }

    public static Piece getRandomPiece() {
        // Types of pieces:
        // L
        // N
        // N-flip
        // I
        // O
        // Y

        int pieceType = (int) (Math.random() * 6);
        Tile[] tiles = new Tile[4];
        for (int i = 0; i<4; i++) {
            tiles[i] = new Tile();
        }

        Point[] locations = new Point[4];

        switch (pieceType) {
            case 0 -> { // I
                locations = new Point[]{
                        new Point(-1, 0),
                        new Point(0, 0),
                        new Point(1, 0),
                        new Point(2, 0),
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
                        new Point(-1,1),
                        new Point(0,1),
                        new Point(1,1),
                        new Point(1,0)
                };
            }
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

        return new Piece(tiles, locations);
    }

    @Override
    public void render(Canvas c) {
        for (int i = 0; i<4; i++) {
            Tile tile = tiles[i];
            Point location = locations[i];

            RenderedImage renderedImage = new RenderedImage(
                    tile.getImage(),
                    (location.x+headX)*75 + tile.offsetX,
                    (location.y+headY)*75 + tile.offsetY
            );

            c.imagesToRender.push(renderedImage);

        }
    }
}

