package swamlol.woodcutting.tasks;


import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import swamlol.woodcutting.Task;
import org.powerbot.script.Tile;

import java.util.concurrent.Callable;

public class Chop extends Task{

    int treeIds[] = {};

    Tile treeLocation = Tile.NIL;

    public Chop(ClientContext ctx, int[] treeIds) {
        super(ctx);
        this.treeIds = treeIds;
    }

    @Override
    public boolean activate() {
        return ctx.objects.select().id(treeIds).poll().equals(ctx.objects.nil()) && ctx.backpack.select().count()<28 || ctx.players.local().animation()==-1 && ctx.backpack.select().count()<28;
    }

    @Override
    public void execute() {
        GameObject treeToChop = ctx.objects.select().id(treeIds).nearest().poll();
        treeLocation = treeToChop.tile();

        if (treeToChop.inViewport()) {
            treeToChop.interact("Chop down");
        } else {
            ctx.camera.turnTo(treeToChop);
            ctx.movement.step(treeToChop);
        }

        Condition.wait(new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation()!=-1;
            }
        }, 200, 10);
    }
}
