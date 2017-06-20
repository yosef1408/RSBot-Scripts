package scripts.TPowerChop.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import scripts.TPowerChop.resources.Antiban;
import scripts.TPowerChop.resources.MyConstants;
import scripts.TPowerChop.resources.Task;

import java.util.concurrent.Callable;

/**
 * Created by Tyskie on 20-6-2017.
 */
public class Chop extends Task {

    private int treeId;
    private boolean isChopping = false;
    private Antiban antiban;
    private String interact;

    public Chop(ClientContext ctx, int treeId, String interact) {
        super(ctx);
        this.treeId = treeId;
        antiban = new Antiban();
        this.interact = interact;
    }

    @Override
    public boolean activate() {
        return ctx.inventory.count() != MyConstants.INVENTORY_FULL;
    }

    @Override
    public void execute() {
        GameObject treeToCut = ctx.objects.select().id(treeId).nearest().poll();
        if (treeToCut.inViewport()){
            treeToCut.hover();
            Condition.sleep(Random.nextInt(250, 500));
            treeToCut.interact("Chop down", interact);
            Condition.sleep(Random.nextInt(2000, 2500));
            isChopping = !isChopping;
            while (isChopping){
                if (Random.nextDouble() > 0.75){
                    antiban.doAntibanAction(Random.nextInt(1, 10));
                }

                if (!treeToCut.valid() || ctx.players.local().animation() == MyConstants.ANIMATION_IDLE){
                    isChopping = !isChopping;
                }
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !isChopping;
                    }
                }, 250, 20);
            }
        } else {
            ctx.camera.turnTo(treeToCut);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return treeToCut.inViewport();
                }
            }, 250, 10);
        }
    }
}
