package thebonobo.SalmonCollector.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import thebonobo.SalmonCollector.utils.Antiban;
import thebonobo.SalmonCollector.utils.Info;
import thebonobo.SalmonCollector.utils.WorldHelper;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

public class Loot extends Task<ClientContext>{
    private Tile barbarianTile = new Tile(3106, 3432, 0);
    private int fishIds[] = {331, 329};
    private int troutIds[] = {333, 335};
    private WorldHelper worldHelp;

    public Loot(ClientContext ctx) {
        super(ctx);
        worldHelp = new WorldHelper(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() != 28 && barbarianTile.distanceTo(ctx.players.local()) <= 10;
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Looting salmon");
        if (ctx.camera.pitch() < 99) {
            ctx.camera.pitch(true);
        }
        Loot();
    }

    private void Loot() {
        GroundItem fishOnGround = ctx.groundItems.select().within(barbarianTile, 10.0).id(fishIds).nearest().poll();
        if (fishOnGround.tile().distanceTo(ctx.players.local().tile()) == 0) {
            // standing on fish, spamclick to take it
            TakeFish(fishOnGround);

        } else if (fishOnGround.tile().distanceTo(ctx.players.local().tile()) > 0){
            // fish is not under player
            if (fishOnGround.inViewport()) {

                fishOnGround.interact("Take", fishOnGround.name());

            } else {

                ctx.movement.findPath(fishOnGround.tile()).traverse();

            }
            // wait until we arrived at fish destination
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    return fishOnGround.tile().distanceTo(ctx.players.local().tile()) == 0 || !fishOnGround.valid();
                }
            }, 400, 20);

        } else { // no fish
            ctx.inventory.select().id(troutIds).first().poll().interact("Drop");
            Antiban.MoveMouseOffScreen(ctx);
        }
        // move mouse around
        Antiban.RunAntiban(ctx);

        if (worldHelp.ShouldHop()){
            worldHelp.RandomWorldHop();
        }
    }
    private void TakeFish(GroundItem fishOnGround){
        fishOnGround.interact("Take", fishOnGround.name());
        boolean nearbyNPC = ctx.npcs.select().within(1).poll().valid();
        if (!nearbyNPC) {

            if (Random.nextInt(0, 5) > 2) {
                Antiban.Wait(25, 50);
                fishOnGround.click(true);
            }
            if (Random.nextInt(0, 5) < 1) {
                Antiban.Wait(50, 75);
                fishOnGround.click(true);
            }
            if (Random.nextInt(0, 5) > 2) {
                Antiban.Wait(25, 50);
                fishOnGround.click(true);
            }
        }
    }


}