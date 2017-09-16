package Elt.AlKharidFishNCook.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import Elt.AlKharidFishNCook.Task;

import java.util.concurrent.Callable;

public class Cook extends Task {

    private final int rangeId = 26181;
    private final int cookingAnimationId = 896;
    private final Tile[] spotToRangePath = {new Tile(3273, 3145, 0), new Tile(3273, 3149, 0), new Tile(3273, 3153, 0), new Tile(3273, 3157, 0), new Tile(3273, 3161, 0), new Tile(3273, 3166, 0), new Tile(3274, 3170, 0), new Tile(3275, 3174, 0), new Tile(3276, 3178, 0), new Tile(3272, 3180, 0)};

    public Cook(ClientContext ctx) {
        super(ctx);
    }

    private void clickRawFish() {
        for (Integer rawFishId : rawFishIds) {
            if (ctx.inventory.select().id(rawFishId).count() > 0) {
                ctx.inventory.select().id(rawFishId).poll().click();
                return;
            }
        }
    }

    @Override
    public boolean activate() {
        return (ctx.inventory.select().count() == 28 && hasRawFish());
    }

    @Override
    public void execute() {
        if (!ctx.objects.select().id(rangeId).isEmpty() && ctx.objects.select().id(rangeId).nearest().poll().tile().distanceTo(ctx.players.local()) < 3) {
            if (ctx.widgets.component(307, 3).visible()) {
                ctx.widgets.widget(307).component(3).interact("Cook All");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation() == cookingAnimationId;
                    }
                }, 150, 20);
            } else {
                final GameObject range = ctx.objects.select().id(rangeId).nearest().poll();
                clickRawFish();
                range.click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.component(307, 3).visible();
                    }
                }, 150, 20);
            }
        } else {
            ctx.movement.newTilePath(spotToRangePath).traverse();
        }
    }

}
