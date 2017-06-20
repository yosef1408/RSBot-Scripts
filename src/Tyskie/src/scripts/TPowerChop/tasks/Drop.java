package scripts.TPowerChop.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import scripts.TPowerChop.resources.MyConstants;
import scripts.TPowerChop.resources.Task;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

/**
 * Created by Tyskie on 20-6-2017.
 */
public class Drop extends Task {

    private boolean shiftClickOn = false;

    public Drop(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == MyConstants.INVENTORY_FULL
                && ctx.players.local().animation() == MyConstants.ANIMATION_IDLE;
    }

    @Override
    public void execute() {
        if ((ctx.varpbits.varpbit(1055) & 131072) > 0){
            shiftClickOn = true;
        }

        if (shiftClickOn){
            ctx.input.send("{VK_SHIFT down}");
        }

        for (Item log : ctx.inventory.select().name(Pattern.compile("(.*logs)|(Logs)"))) {
            if (ctx.controller.isStopping()){
                break;
            }

            final int startAmtInventory = ctx.inventory.select().count();

            if (shiftClickOn){
                log.click();
            } else {
                log.interact("Drop");
            }

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() != startAmtInventory;
                }
            }, 25, 10);
        }

        if (shiftClickOn){
            ctx.input.send("{VK_SHIFT up}");
        }
    }
}
