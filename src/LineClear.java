public class LineClear implements Renderable {
    int scoreBonus;
    int multiplier;
    Tile.TileType type;
    int x;
    int y;
    int scoreBoxX = 500;
    int scoreBoxY = 100;
    LineClear(int scoreBonus, int multiplier, Tile.TileType type, int x, int y) {
        this.scoreBonus = scoreBonus;
        this.multiplier = multiplier;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void runFrame() {
        x = (int) ((x-scoreBoxX)*0.95+scoreBoxX);
        y = (int) ((y-scoreBoxY)*0.85+scoreBoxY);
    }

    public boolean atDestination() {
        return (int) ((y-scoreBoxY)*0.85+scoreBoxY) == y;
    }

    @Override
    public void render(Canvas c) {
        c.textToRender.push(
                new RenderedText(""+scoreBonus, x, y, type.color)
        );
    }
}
