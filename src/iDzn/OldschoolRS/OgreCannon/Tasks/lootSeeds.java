package iDzn.OldschoolRS.OgreCannon.Tasks;

import iDzn.OldschoolRS.OgreCannon.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import java.util.concurrent.Callable;


public class lootSeeds  extends Task {
    private final Area lootArea = new Area(new Tile(2523, 3377, 0), new Tile(2533,3373, 0));

    public lootSeeds(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate() {
        return lootArea.contains (ctx.players.local()) &&
                !ctx.groundItems.select().id(5300, 5304, 5295, 532).isEmpty();

    }

    @Override
    public void execute() {
        if (ctx.inventory.select().count() < 27) ;
        GroundItem rSeed = ctx.groundItems.select().id(5295).nearest().poll();
        GroundItem sSeed = ctx.groundItems.select().id(5300).nearest().poll();
        GroundItem tSeed = ctx.groundItems.select().id(5304).nearest().poll();

        if (rSeed.valid())
            if (!rSeed.inViewport()) {
                ctx.camera.turnTo(rSeed);
                ctx.camera.pitch(0);
            }
        rSeed.interact("Take", "Ranarr seed");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !(ctx.players.local().animation() ==-1);
            }
        }, 350, 5);
        if (tSeed.valid())
        if (!tSeed.inViewport()) {
            ctx.camera.turnTo(tSeed);
            ctx.camera.pitch(0);
        }
        tSeed.interact("Take", "Torstol seed");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !(ctx.players.local().animation() ==-1);
            }
        }, 350, 5);
        if (sSeed.valid())
            if (!sSeed.inViewport()) {
                ctx.camera.turnTo(sSeed);
                ctx.camera.pitch(0);
            }
        sSeed.interact("Take", "Snapdragon seed");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !(ctx.players.local().animation() ==-1);
            }
        }, 350, 5);
    }
}