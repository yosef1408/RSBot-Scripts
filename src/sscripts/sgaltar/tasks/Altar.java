package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.Item;
import sscripts.sgaltar.SGAltar;

import java.util.concurrent.Callable;

public class Altar extends Task {

    public Altar(ClientContext ctx) {
        super(ctx);
    }



    @Override
    public boolean activate() {
        final GameObject portalIn = ctx.objects.select().id(4525).nearest().poll();
        final GameObject altar = ctx.objects.select().name("Altar").nearest().poll();

        return (portalIn.inViewport() || altar.inViewport()) && !ctx.inventory.select().isEmpty();
    }

    @Override
    public void execute() {
        SGAltar.status = "Interacting with Altar";
        final Item b = ctx.inventory.select().id(SGAltar.boneID).first().poll();
        final int[] bounds = {-44, 4, -108, 28, -80, 84};
        final GameObject altar = ctx.objects.select().name("Altar").each(Interactive.doSetBounds(bounds)).nearest().poll();
        if (altar.inViewport()) {

            if (!ctx.players.local().inMotion() && ctx.players.local().animation() == -1 || SGAltar.failSave) {
                if (b.interact("Use")) {
                    if (altar.interact("Use")) {
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.inventory.select().isEmpty() || ctx.widgets.component(233, 2).visible() || SGAltar.failSave ;
                            }
                        }, 2250, 28);
                    }
                }
            }
        } else {
                SGAltar.status = "Looking for Altar";
                ctx.camera.turnTo(altar);
                ctx.movement.step(altar);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return altar.inViewport();
                    }
                }, 500, 3);
        }

    }

}
