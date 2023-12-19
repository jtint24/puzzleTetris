public class LineClear implements Renderable {
    int scoreBonus;
    int multiplier;
    Tile.TileType type;
    int x;
    int y;
    LineClear(int scoreBonus, int multiplier, Tile.TileType type, int x, int y) {
        this.scoreBonus = scoreBonus;
        this.multiplier = multiplier;
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void runFrame() {
        x *= 0.95;
        y *= 0.85;
    }

    @Override
    public void render(Canvas c) {
        c.textToRender.push(
                new RenderedText(""+scoreBonus, x, y)
        );
    }
}
