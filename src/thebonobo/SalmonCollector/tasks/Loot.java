package thebonobo.SalmonCollector.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import thebonobo.SalmonCollector.utils.*;

import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

public class Loot extends Task<ClientContext> {
    private WorldHelper worldHelp;
    private Properties userProperties;
    private GroundItem fishOnGround;

    public Loot(ClientContext ctx, Properties userProperties) {
        super(ctx);
        this.userProperties = userProperties;
        worldHelp = new WorldHelper(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() != 28 && Paths.BARBARIAN_TILE.distanceTo(ctx.players.local()) <= 10;
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Looting salmon");
        fishOnGround = ctx.groundItems.select().within(Paths.BARBARIAN_TILE, 10.0).id(Items.SALMONS).nearest().poll();
        if (fishOnGround.inViewport()) {
            if (fishOnGround.tile().distanceTo(ctx.players.local().tile()) == 0) {
                // standing on fish, click to take it
                takeFish(fishOnGround);

            } else {

                // fish is not under player
                fishOnGround.interact("Take", fishOnGround.name());
                // wait until we arrived at fish destination
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return fishOnGround.tile().distanceTo(ctx.players.local().tile()) == 0 || !fishOnGround.valid();
                    }
                }, 300, 40);
                // Condition.wait(() -> fishOnGround.tile().distanceTo(ctx.players.local().tile()) == 0 || !fishOnGround.valid(), 300, 40);

            }
        } else {

            lookOtherFishingSpot();
            // drop accidental picked up trout
            ctx.inventory.select().id(Items.TROUT).shuffle().poll().interact("Drop");
            // fastest rate is 9/1000 chance so do it every 60000ms/111 = 1,6 chance per minute at the most
            if (Random.nextInt(0, 1000) <= Integer.parseInt(userProperties.getProperty("right-click-player-rate"))) ;
                Antiban.moveMouseOffScreen(ctx);
            Condition.sleep(Random.nextInt(2000, 4000));

        }

        if (worldHelp.shouldHop()) {
            // hop worlds if not enough salon in the last 10 minutes
            worldHelp.randomWorldHop();
        }
    }

    private void takeFish(GroundItem fishOnGround) {
        fishOnGround.interact("Take", fishOnGround.name());
        boolean nearbyNPC = ctx.npcs.select().within(1).poll().valid();
        if (!nearbyNPC && userProperties.getProperty("double-click") == "1") {

            if (Random.nextInt(0, 5) > 2) {
                Antiban.wait(25, 50);
                fishOnGround.click(true);
            }

            if (Random.nextInt(0, 5) < 1) {
                Antiban.wait(50, 75);
                fishOnGround.click(true);
            }
            if (Random.nextInt(0, 5) > 2) {
                Antiban.wait(25, 50);
                fishOnGround.click(true);
            }
        }
    }

    private void lookOtherFishingSpot() {
        int distanceToNorth = ctx.movement.distance(ctx.players.local(), Paths.NORTH_FISHING_SPOT);
        int distanceToSouth = ctx.movement.distance(ctx.players.local(), Paths.SOUTH_FISHING_SPOT);

        if (distanceToNorth > distanceToSouth) {
            // check if target is not already in viewport
            if (!Paths.NORTH_FISHING_SPOT.matrix(ctx).inViewport())
                ctx.camera.turnTo(Paths.NORTH_FISHING_SPOT);
        } else {
            if (!Paths.SOUTH_FISHING_SPOT.matrix(ctx).inViewport()) {
                ctx.camera.turnTo(Paths.SOUTH_FISHING_SPOT);
                if (ctx.camera.pitch() > 55)
                    ctx.camera.pitch(Random.nextInt(30, 54));
            }
        }
    }


}