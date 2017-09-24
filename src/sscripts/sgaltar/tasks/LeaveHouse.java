package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import sscripts.sgaltar.SGAltar;

import java.util.concurrent.Callable;

public class LeaveHouse extends Task {
    public LeaveHouse(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate() {
        final GameObject portal = ctx.objects.select().id(4525).nearest().poll();
        final GameObject altar = ctx.objects.select().name("Altar").nearest().poll();


        return (ctx.inventory.select().isEmpty() && altar.inViewport()) || (portal.inViewport() && ctx.inventory.select().isEmpty());
    }

    @Override
    public void execute() {
        final GameObject portal = ctx.objects.select().id(4525).nearest().poll();
        if (portal.inViewport()){
            SGAltar.status="Leaving House";
            if (portal.interact("Enter", "Portal")) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().inMotion() && ctx.players.local().animation() != -1;
                    }
                }, 500, 2);
                SGAltar.failSave = false;
            }
        } else {
            ctx.movement.step(portal);
            SGAltar.status="Stepping to Portal";
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().inMotion() && portal.inViewport();
                }
            }, 500, 3);
        }

    }
}
