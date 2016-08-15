package supaplex.tasks.stringing;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Player;
import supaplex.tasks.Task;
import supaplex.util.Constants;
import supaplex.util.GlobalVariables;

import java.util.concurrent.Callable;

/**
 * Created by Andreas on 20.07.2016.
 */
public class String extends Task<ClientContext> {

    private Player player = ctx.players.local();

    public String(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        // These checks are necessary because the player animation is sometimes -1 during execution.
        // Avoids unnecessary waiting during the startup of each cycle.
        if (!GlobalVariables.working) {
            return !ctx.inventory.select().id(Constants.BOW_STRING).isEmpty() && !ctx.inventory.select().id(GlobalVariables.unfBowId).isEmpty();
        } else {
            if (!ctx.inventory.select().id(Constants.BOW_STRING).isEmpty() && !ctx.inventory.select().id(GlobalVariables.unfBowId).isEmpty()) {
                if (player.animation() == -1) {
                    Condition.sleep(1200);
                    if (player.animation() == -1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void execute() {
        System.out.println("Stringing...");
        if (ctx.bank.opened()) {
            ctx.bank.close();
        }

        Item bowstring = ctx.inventory.select().id(Constants.BOW_STRING).poll();
        Item unfBow = ctx.inventory.select().id(GlobalVariables.unfBowId).poll();


        bowstring.interact("Use");
        if (unfBow.interact("Use")) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(Constants.STRING_WIDGET).component(Constants.STRING_COMPONENT).visible();
                }
            }, 250, 4);
        }

        // Starts stringing bows.
        Component component = ctx.widgets.widget(Constants.STRING_WIDGET).component(Constants.STRING_COMPONENT);
        component.interact("Make All");
        // Used to make sure the player is actually stringing bows
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (player.animation() != -1) {
                    GlobalVariables.working = true;
                    return true;
                }
                return false;
            }
        }, 250, 6);
    }
}
