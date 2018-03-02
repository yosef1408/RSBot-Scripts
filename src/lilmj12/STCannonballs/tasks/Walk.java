package lilmj12.STCannonballs.tasks;

import lilmj12.STCannonballs.main.Task;
import lilmj12.STCannonballs.misc.Walker;
import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class Walk extends Task {

    public Walk(ClientContext ctx, Area bankArea, Area smeltingArea, Tile[] pathFromFurnace){
        super(ctx);
        this.bankArea = bankArea;
        this.smeltingArea = smeltingArea;
        this.pathFromFurnace = pathFromFurnace;
    }

    private final int STEEL_BAR_ID = 2353;


    Area bankArea;
    Area smeltingArea;
    Tile[] pathFromFurnace;

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
