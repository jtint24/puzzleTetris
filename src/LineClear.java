public class LineClear implements Renderable {
    int scoreBonus;
    int multiplier;
    Tile.TileType type;
    int x;
    int y;
    int scoreBoxX = Main.tileOffsetX+10*Main.tileHeight+100-10;
    int scoreBoxY = Main.tileOffsetY+Main.tileOffsetY+5;
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
        return (int) Math.abs(((y-scoreBoxY)*0.85+scoreBoxY) - y) + (int) Math.abs(((x-scoreBoxX)*0.85+scoreBoxX) - x) < 9;
    }

    @Override
    public void render(Canvas c) {
        c.textToRender.push(
                new RenderedText(""+scoreBonus, x, y, type.color)
        );
        c.textToRender.push(
                new RenderedText("x"+multiplier, x, y+25, type.color, 16)
        );
    }
}
