package lugge.scripts.rs3.luckykebbit.data;

import org.powerbot.script.Tile;

public enum Tiles {
    CENTER_POINT(new Tile(2871, 3480)),
    BANK_POINT(new Tile(2876, 3417)),
    WEST_SNOW_PILE(new Tile(2866, 3483)),
    EAST_SNOW_PILE(new Tile(2876, 3480)),
    NULL(null);

    private final Tile tile;

    Tiles(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return this.tile;
    }
}
