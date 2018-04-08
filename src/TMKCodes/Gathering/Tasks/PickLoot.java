package TMKCodes.Gathering.Tasks;

import TMKCodes.Gathering.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class PickLoot extends Task {

    private String lootPattern = "(.*rune)|(Coins)|(.*bolts)|(.*hide)|(.*arrows)|(.*ones)";

    public PickLoot(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.groundItems.select().name(Pattern.compile(lootPattern)).nearest().poll().tile().distanceTo(ctx.players.local().tile()) <= 2 && ctx.inventory.select().count() < 28 && !ctx.players.local().inCombat();
    }

    @Override
    public void execute() {
        GroundItem loot = ctx.groundItems.select().name(Pattern.compile(lootPattern)).nearest().poll();
        final int startInventory = ctx.inventory.select().name(Pattern.compile(lootPattern)).count();
        if(ctx.inventory.select().count() < 28) {
            ctx.camera.turnTo(loot);
            if(!loot.inViewport())
                ctx.movement.step(loot);
            loot.interact("Take");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().name(Pattern.compile(lootPattern)).count() != startInventory;
                }
            }, Random.nextInt(120, 60), Random.nextInt(20, 10));
        }
    }
}
