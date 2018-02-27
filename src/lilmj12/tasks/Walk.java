package lilmj12.tasks;

import lilmj12.main.Task;
import lilmj12.misc.Walker;
import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class Walk extends Task {

    public Walk(ClientContext ctx){
        super(ctx);
    }

    private final int STEEL_BAR_ID = 2353;

    private final int lowerLeftX = 3269;
    private final int lowerLeftY = 3161;

    private final int upperRightX = 3272;
    private final int upperRightY = 3173;

    Tile lowerLeftTile = new Tile(lowerLeftX, lowerLeftY);
    Tile upperRightTile = new Tile(upperRightX, upperRightY);

    Area bankArea = new Area(lowerLeftTile, upperRightTile);

    private int rightUpperX = 3279;
    private int rightUpperY = 3188;

    private int leftLowerX = 3274;
    private int leftLowerY = 3184;

    Tile rightUpperTile = new Tile(rightUpperX, rightUpperY);
    Tile leftLowerTile = new Tile(leftLowerX, leftLowerY);

    Area smeltingArea = new Area(rightUpperTile, leftLowerTile);

    public static final Tile[] pathFromFurnace = {new Tile(3276, 3186, 0), new Tile(3280, 3185, 0), new Tile(3280, 3181, 0), new Tile(3280, 3177, 0), new Tile(3277, 3173, 0), new Tile(3275, 3169, 0), new Tile(3271, 3167, 0)};
    private final Walker walker = new Walker(ctx);

    @Override
    public boolean activate(){
        if(!smeltingArea.contains(ctx.players.local().tile()) && !bankArea.contains(ctx.players.local().tile())){
            return true;
        }

        if(bankArea.contains(ctx.players.local().tile()) && ctx.inventory.select().id(STEEL_BAR_ID).count() == 27){
            return true;
        }

        if(smeltingArea.contains(ctx.players.local().tile()) && ctx.inventory.select().id(STEEL_BAR_ID).count() == 0) return true;
        return false;
    }

    @Override
    public void execute(){

        if(!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL)) {
            if(ctx.inventory.select().id(STEEL_BAR_ID).count() == 0){
                walker.walkPath(pathFromFurnace);
            }else if(ctx.inventory.select().id(STEEL_BAR_ID).count() == 27){
                walker.walkPathReverse(pathFromFurnace);
            }
        }
    }

}
