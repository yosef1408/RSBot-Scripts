package sscripts.sgaltar.tasks.walk;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import sscripts.sgaltar.SGAltar;
import sscripts.sgaltar.tasks.Task;

import java.util.concurrent.Callable;

public class WalkEnergy extends Task{
    public WalkEnergy(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        final GameObject fountain = ctx.objects.select().name("Ornate rejuvenation pool").nearest().poll();
        return ctx.movement.energyLevel() < 70 && fountain.valid() && ctx.inventory.select().count() == 28;
    }

    @Override
    public void execute() {
        final GameObject fountain = ctx.objects.select().name("Ornate rejuvenation pool").nearest().poll();
        SGAltar.status = "Refreshing Energy";
        if (fountain.inViewport()) {
            if (fountain.interact("Drink")) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.movement.energyLevel() == 100;
                    }
                }, 500, 4);
            }
        } else {
            ctx.camera.turnTo(fountain);
            ctx.movement.step(fountain);
            SGAltar.status = "searching for fountain";
        }

    }
}
