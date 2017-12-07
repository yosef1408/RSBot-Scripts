package iDzn.OgreCannon.Tasks;

import iDzn.OgreCannon.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class fireCannon extends Task {
    private final Area lootArea = new Area(new Tile(2523, 3377, 0), new Tile(2533,3373, 0));

    public fireCannon(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate() {
        return !lootArea.contains(ctx.players.local()) && ctx.inventory.select().id(6, 8, 10, 12).count() < 1 && !ctx.players.local().inMotion();

    }

    @Override
    public void execute() {
        int breakT = Random.nextInt(8000, 18000);
        GameObject cannonToFire = ctx.objects.select().id(6).poll();
        if (cannonToFire.inViewport()) {
            ctx.camera.turnTo(cannonToFire);
            ctx.camera.pitch(0);
            cannonToFire.interact("Fire");
            try {
                Thread.sleep(breakT);
            } catch (InterruptedException ex) {
               Thread.currentThread().interrupt();
                }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !(ctx.players.local().animation() ==-1);
                }
            }, 800, 5);
            }

        }
    }
