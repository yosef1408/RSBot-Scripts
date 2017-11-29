package iDzn.OldschoolRS.Tasks;

import iDzn.OldschoolRS.Task;
import iDzn.OldschoolRS.Walker;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class Walk extends Task {

    public static final Tile[] pathToBank = {new Tile(3228, 3146, 0), new Tile(3231, 3150, 0), new Tile(3231, 3154, 0), new Tile(3234, 3157, 0), new Tile(3237, 3160, 0), new Tile(3237, 3164, 0), new Tile(3237, 3168, 0), new Tile(3237, 3172, 0), new Tile(3238, 3176, 0), new Tile(3238, 3180, 0), new Tile(3239, 3184, 0), new Tile(3242, 3188, 0), new Tile(3244, 3192, 0), new Tile(3242, 3196, 0), new Tile(3239, 3200, 0), new Tile(3236, 3203, 0), new Tile(3236, 3207, 0), new Tile(3236, 3212, 0), new Tile(3234, 3217, 0), new Tile(3230, 3218, 0), new Tile(3226, 3218, 0), new Tile(3222, 3218, 0), new Tile(3218, 3219, 0), new Tile(3215, 3216, 0), new Tile(3215, 3212, 0), new Tile(3211, 3211, 0), new Tile(3207, 3210, 0), new Tile(3205, 3209, 1), new Tile(3205, 3209, 2), new Tile(3205, 3213, 2), new Tile(3206, 3217, 2), new Tile(3209, 3220, 2)};
    private final Walker walker = new Walker(ctx);

    public Walk(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count()>27 || (ctx.inventory.select().count()<28 && pathToBank[0].distanceTo(ctx.players.local())>6);
    }

    @Override
    public void execute() {
        if(ctx.movement.running() && ctx.movement.energyLevel()>45){
            ctx.movement.running(true);
        }

        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5);
           if(ctx.inventory.select().count()>27){
            walker.walkPath(pathToBank);
           } else {
               walker.walkPathReverse(pathToBank);
           }


    }
}
