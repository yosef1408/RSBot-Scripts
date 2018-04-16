package TMKCodes.Gathering.Tasks;

import TMKCodes.Gathering.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.Random;
import java.util.concurrent.Callable;

public class BuryBones extends Task {


    public BuryBones(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(526).count() > 0 && !ctx.players.local().inCombat();
    }

    @Override
    public void execute() {
        if(ctx.game.tab() != Game.Tab.INVENTORY) {
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        for(Item bones : ctx.inventory.select().id(526)) {
            final int startAmount = ctx.inventory.select().count();
            bones.interact("Bury", "Bones");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() != startAmount;
                }
            }, Random.nextInt(1200, 600), Random.nextInt(20, 10));
        }
    }
}
