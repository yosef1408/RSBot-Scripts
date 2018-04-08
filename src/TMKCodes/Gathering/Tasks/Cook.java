package TMKCodes.Gathering.Tasks;

import TMKCodes.Gathering.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;

import java.util.concurrent.Callable;

public class Cook extends Task {

    private int fishes[] = { 317, 321, 327, 331, 345, 321, 335, 341, 349, 353, 359, 363, 371, 377, 383, 389, 5001, 3379, 7944, 10138, 11328, 11330 };

    private int range[] = { 26181 };

    public Cook(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(fishes).count() >= 0;
    }

    @Override
    public void execute() {
        GameObject rangeToUse = ctx.objects.select().id(range).nearest().poll();
        if(!rangeToUse.inViewport()) {
            ctx.movement.step(rangeToUse);
            ctx.camera.turnTo(rangeToUse);
            Condition.sleep(Random.nextInt(800, 600));
        }
        if(ctx.game.tab() != Game.Tab.INVENTORY) {
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        System.out.println("Fish count: " + ctx.inventory.select().id(fishes).count());
        if(ctx.inventory.select().id(fishes).count() == 0) {
            return;
        }
        for(int id : fishes) {
            System.out.println("Fish id: " + id + " count: " + ctx.inventory.select().id(id).count());
            if(ctx.inventory.select().id(id).count() == 0) {
                continue;
            }
            final Component cookingInterface = ctx.widgets.widget(270).component(13);
            Item fish = ctx.inventory.select().id(id).poll();
            if(!cookingInterface.visible()) {
                System.out.println("Use fish");
                fish.interact("Use", fish.name());
                Condition.sleep(Random.nextInt(240, 120));
                System.out.println("On the range");
                System.out.println(rangeToUse.tile());
                rangeToUse.interact("Use");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return cookingInterface.visible();
                    }
                }, Random.nextInt(120, 60), Random.nextInt(20, 10));
            }
            if(cookingInterface.visible()) {
                cookingInterface.click();
                Condition.sleep(Random.nextInt(30000, 60000));
            }
            if(ctx.inventory.select().id(fishes).count() == 0) {
                break;
            }
        }

    }
}
