package iDzn.OgreCannon.Tasks;

import iDzn.OgreCannon.Task;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

public class deployCannon extends Task<org.powerbot.script.ClientContext<Client>> {
    public deployCannon(ClientContext ctx) {
        super(ctx);
    }

    Tile deployTile = new Tile(2528, 3371, 0);

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(6, 8, 10, 12).count() > 0 && ctx.players.local().animation() == -1;
    }

    @Override
    public void execute() {
        Random RANDO = new Random();
        final Item cannonToDeploy = ctx.inventory.select().id(6).poll();
        final GameObject cannonToFire = ctx.objects.select().id(6).poll();

        if (deployTile.distanceTo(ctx.players.local()) != 0)
            ctx.movement.step(deployTile);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return deployTile.distanceTo(ctx.players.local()) == 0;
            }
        }, 250, 20);
        if (deployTile.distanceTo(ctx.players.local()) == 0) {
            cannonToDeploy.interact("Set-up", "Cannon base");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return cannonToFire.valid();
                }
            }, 300, 20);
        }
    }
}
