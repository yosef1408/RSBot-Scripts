package TMKCodes.Gathering.Tasks;

import TMKCodes.Gathering.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Item;
import java.util.concurrent.Callable;

public class Heal extends Task {

    private int foods[] = { 315, 316, 325, 326, 329, 330, 333, 334, 347, 348, 351, 352, 355, 356, 361, 362, 379, 380, 373, 374, 1971, 1993, 1994, 2003, 2004, 2140, 2141, 2142, 2143, 2309, 2310 };

    public Heal(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.players.local().healthPercent() <= 50 && ctx.inventory.select().id(foods).count() > 0;
    }

    @Override
    public void execute() {
        if(ctx.game.tab() != Game.Tab.INVENTORY) {
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        Item food = ctx.inventory.select().id(foods).poll();
        final int cHealth = ctx.players.local().healthPercent();
        food.interact("Eat");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().healthPercent() > cHealth;
            }
        }, Random.nextInt(120, 60), Random.nextInt(20, 10));
    }
}
