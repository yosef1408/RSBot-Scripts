package lugge.scripts.rs3.luckykebbit.data;

import org.powerbot.script.Tile;

public enum Path {
    BOTH_WAYS(new Tile[]{
            new Tile(2873, 3480),
            new Tile(2879, 3470),
            new Tile(2882, 3458),
            new Tile(2882, 3449),
            new Tile(2883, 3435),
            new Tile(2882, 3420),
            new Tile(2875, 3417)
    });

    private final Tile[] path;

    Path(Tile[] path) {
        this.path = path;
    }

    public Tile[] getPath() {
        return this.path;
    }

}
