package lugge.scripts.rs3.luckykebbit.data;

public enum Tracks {
    //START_ HOLE_PATH_#_STEP_#_OPTION_#
    SHP1S1(49152, 66472, Tiles.NULL),
    SHP1S2(1073857024, 66470, Tiles.NULL),
    SHP1S3(1342293504, 0, Tiles.WEST_SNOW_PILE),

    SHP2S1(1544, 66468, Tiles.NULL),
    SHP2S2O1(1073743386, 66467, Tiles.NULL),
    SHP2S2O2(1073743448, 66469, Tiles.NULL),
    SHP2S3O1(1342178844, 0, Tiles.WEST_SNOW_PILE),
    SHP2S3O2(1476396760, 0, Tiles.EAST_SNOW_PILE),

    SHP3S1(131200, 66469, Tiles.NULL),
    SHP3S2O1(1073873154, 66467, Tiles.NULL),
    SHP3S2O2(1073873168, 66468, Tiles.NULL),
    SHP3S3O1(1342308612, 0, Tiles.WEST_SNOW_PILE),
    SHP3S3O2(1342308640, 0, Tiles.WEST_SNOW_PILE),

    SHP4S1(135168, 66471, Tiles.NULL),
    SHP4S2(1073885696, 66470, Tiles.NULL),
    SHP4S3(1342322176, 0, Tiles.WEST_SNOW_PILE),

    SHP5S1(1537, 66467, Tiles.NULL),
    SHP5S2O1(1073743427, 66469, Tiles.NULL),
    SHP5S2O2(1073743379, 66468, Tiles.NULL),
    SHP5S3O1(1476396739, 0, Tiles.EAST_SNOW_PILE),
    SHP5S3O2(1342178851, 0, Tiles.WEST_SNOW_PILE),

    SHP6S1(16512, 66469, Tiles.NULL),
    SHP6S2O1(1073758466, 66467, Tiles.NULL),
    SHP6S2O2(1073758480, 66468, Tiles.NULL),
    SHP6S3O1(1342193924, 0, Tiles.WEST_SNOW_PILE),
    SHP6S3O2(1342193952, 0, Tiles.WEST_SNOW_PILE),

    NHP1S1(1028, 66470, Tiles.NULL),
    NHP1S2O1(1073809412, 66472, Tiles.NULL),
    NHP1S2O2(1073752068, 66471, Tiles.NULL),
    NHP1S3O1(1476528132, 0, Tiles.EAST_SNOW_PILE),
    NHP1S3O2(1476413444, 0, Tiles.EAST_SNOW_PILE),

    NHP2S1(12, 66468, Tiles.NULL),
    NHP2S2O1(1073741916, 66469, Tiles.NULL),
    NHP2S202(1073809412, 66472, Tiles.NULL),
    NHP2S3O1(1476395228, 0, Tiles.EAST_SNOW_PILE),
    NHP2S3O2(1476528132, 0, Tiles.EAST_SNOW_PILE),

    NHP3S1(33, 66467, Tiles.NULL),
    NHP3S2(1073741923, 66469, Tiles.NULL),
    NHP3S3(1476395235, 0, Tiles.EAST_SNOW_PILE),

    NHP4S1(4288, 66471, Tiles.NULL),
    NHP4S2O1(1073819840, 66472, Tiles.NULL),
    NHP4S2O2(1073754816, 66470, Tiles.NULL),
    NHP4S3O1(1476538560, 0, Tiles.EAST_SNOW_PILE),
    NHP4S3O2(1342191296, 0, Tiles.WEST_SNOW_PILE),

    NHP5S1(1056, 66470, Tiles.NULL),
    NHP5S2O1(1073752096, 66471, Tiles.NULL),
    NHP5S2O2(1073809440, 66472, Tiles.NULL),
    NHP5S3O1(1476413472, 0, Tiles.EAST_SNOW_PILE),
    NHP5S3O2(1476528160, 0, Tiles.EAST_SNOW_PILE),

    NHP6S1(32960, 66472, Tiles.NULL),
    NHP6S2O1(1073840832, 66470, Tiles.NULL),
    NHP6S2O2(1073848512, 66471, Tiles.NULL),
    NHP6S3O1(1342277312, 0, Tiles.WEST_SNOW_PILE),
    NHP6S3O2(1476509888, 0, Tiles.EAST_SNOW_PILE);

    private final int varpbitSetting;
    private final int nextObjectID;
    private final Tiles finalTile;

    Tracks(int varpbitSetting, int nextObjectID, Tiles finalTile) {
        this.varpbitSetting = varpbitSetting;
        this.nextObjectID = nextObjectID;
        this.finalTile = finalTile;
    }

    public final int getVarpbitSetting() {
        return varpbitSetting;
    }

    public final int getNextObjectID() {
        return nextObjectID;
    }

    public final Tiles getFinalTile() {
        return finalTile;
    }
}
