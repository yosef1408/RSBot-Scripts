package thebonobo.SalmonCollector.tasks;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import thebonobo.SalmonCollector.utils.Info;
import thebonobo.SalmonCollector.utils.Paths;
import thebonobo.SalmonCollector.utils.Walker;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

public class WalkToBarbVillage extends Task<ClientContext> {
    private Tile[] bankPath;
    private Tile[] barbArea;
    private int fishIds[] = {331, 329};
    private Walker walker;


    Tile barbarianTile = new Tile(3106, 3432, 0);

    public WalkToBarbVillage(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().id(fishIds).count() == 0 && barbarianTile.distanceTo(ctx.players.local().tile()) > 10) || !Paths.BARBARIAN_AREA.contains(ctx.players.local());
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Walking to Barbarian Village");

        if (ctx.players.local().tile().y()< 3424){
            walker = new Walker(ctx, ctx.movement.newTilePath(Paths.LUMBRIDGE_TO_BARBARIAN_VILLAGE));
        } else {
            walker = new Walker(ctx, ctx.movement.newTilePath(Paths.BANK_TO_FISHING));
        }

        walker.Walk();
    }
}
