package TMKCodes.Gathering.Tasks;

import TMKCodes.Gathering.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.Random;

import java.util.concurrent.Callable;

public class Drop extends Task {

    private int fishes[] = {317, 327, 331, 345, 321, 335, 341, 349, 353, 359, 363, 371, 377, 383, 389, 5001, 3379, 7944, 10138, 11328, 11330};
    private int woods[] = { 1511, 1521, 1515 };
    private int ores[] = { 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 444, 445, 453, 454, 447, 448, 449, 450, 451, 452 };

    /* Still needed fishes
     * Raw karambwan
     * Leaping sturgeon
     * Raw sea turtle
     * Raw anglerfish
     * Raw dark crab
     */

    private int drop[];
    private String what;

    public Drop(ClientContext ctx, String what) {
        super(ctx);
        this.what = what;
    }

    @Override
    public boolean activate() {
        System.out.println("Dropping");
        if(what.equals("fish")) {
            drop = fishes;
        } else if(what.equals("wood")) {
            drop = woods;
        } else if(what.equals("ore")) {
            drop = ores;
        }
        return ctx.inventory.select().count() == 28;
    }

    @Override
    public void execute() {
        if(ctx.game.tab() != Game.Tab.INVENTORY) {
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        for(final Item item : ctx.inventory.select().id(drop)) {
            if(ctx.controller.isStopping()) {
                break;
            }
            final int currentAmount = ctx.inventory.select().id(item).count();
            item.interact("Drop");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(drop).isEmpty();
                }
            }, Random.nextInt(120, 60), Random.nextInt(20, 10));
        }
        if(ctx.inventory.select().id(fishes).count() > 0) this.execute();
    }
}
