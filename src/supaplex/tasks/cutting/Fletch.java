package supaplex.tasks.cutting;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.*;
import supaplex.tasks.Task;
import supaplex.util.Constants;
import supaplex.util.GlobalVariables;

import java.util.concurrent.Callable;


/**
 * Created by Andreas on 20.06.2016.
 */
public class Fletch extends Task<ClientContext> {

    private Player player = ctx.players.local();

    public Fletch(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        // Checks if player stands still
        if (player.animation() == -1) {
            // Start fletching if knife and logs in inventory
            return !ctx.inventory.select().id(Constants.KNIFE).isEmpty() && !ctx.inventory.select().id(GlobalVariables.logId).isEmpty();
        }
        return false;
    }

    @Override
    public void execute() {
        System.out.println("Fletching...");

        if (ctx.bank.opened()) {
            ctx.bank.close();
        }

        Item knife = ctx.inventory.select().id(Constants.KNIFE).poll();
        Item log = ctx.inventory.select().id(GlobalVariables.logId).poll();

        knife.interact("Use");
        if (log.interact("Use")) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(GlobalVariables.widgetId).component(GlobalVariables.componentId).visible();
                }
            }, 250, 10);
        }

        Component component = ctx.widgets.widget(GlobalVariables.widgetId).component(GlobalVariables.componentId);

        if (component.interact("Make X")) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.chat.pendingInput();
                }
            }, 250, 10);
        }

        if (ctx.chat.sendInput(27)) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(GlobalVariables.logId).isEmpty();
                }
            }, 250, 10);
        }

    }
}
