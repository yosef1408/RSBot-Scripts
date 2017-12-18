package iDzn.OgreCannon.Tasks;

import iDzn.OgreCannon.OgreCannon;
import iDzn.OgreCannon.Task;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class fireCannon extends Task<org.powerbot.script.ClientContext<Client>> {

    OgreCannon main;

    public fireCannon(ClientContext ctx, OgreCannon main) {

        super(ctx);
        this.main = main;


    }

    private final Area lootArea = new Area(new Tile(2523, 3377, 0), new Tile(2533, 3373, 0));
    public int reloadTrue, fixTrue = 0;

    @Override
    public boolean activate() {
        return !lootArea.contains(ctx.players.local())
                && ctx.inventory.select().id(6, 8, 10, 12).count() < 1
                && !ctx.players.local().inMotion()
                && ctx.inventory.select().id(2).poll().stackSize() > 30;

    }

    @Override
    public void execute() {
        final GameObject cannonToFire = ctx.objects.select().id(6).poll();
        final GameObject cannonToFix = ctx.objects.select().id(14916).poll();
        if (ctx.widgets.widget(162).component(44).component(0).text().contains("ammo")) {
            reloadTrue = 1;
        }
        if (ctx.widgets.widget(162).component(44).component(0).text().contains("30")) {
            reloadTrue = 0;
        }
        if (ctx.widgets.widget(162).component(44).component(0).text().contains("broken")) {
            fixTrue = 1;
        }
        if (!cannonToFix.valid()) {
            fixTrue = 0;
        }
        if ((reloadTrue > 0 || main.xpGained == 0) && !cannonToFire.inViewport()) {
            ctx.camera.turnTo(cannonToFire);
            ctx.camera.pitch(80);
        } else if ((reloadTrue > 0 || main.xpGained == 0) && cannonToFire.inViewport()) {
            cannonToFire.interact("Fire");
            System.out.println("Reloading");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return main.BallsCount < ctx.inventory.select().id(2).poll().stackSize();
                }
            }, 150, 20);
        } else if (fixTrue > 0) {
            cannonToFix.interact("Repair", "Broken multicannon");
            System.out.println("Fixing");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !cannonToFix.valid();
                }
            }, 300, 20);
        }
    }
}