package supaplex.tasks.stringing;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Player;
import supaplex.tasks.Task;
import supaplex.util.BankFinder;
import supaplex.util.Constants;
import supaplex.util.GlobalVariables;

import java.util.concurrent.Callable;

/**
 * Created by Andreas on 20.07.2016.
 */
public class DepositBows extends Task<ClientContext> {

    private Player player = ctx.players.local();

    public DepositBows(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        // Don't deposit if inventory is empty
        if (ctx.inventory.select().isEmpty()) {
            return false;
        }
        // Deposit if no unf bows, nor bow strings in inventory
        if (ctx.inventory.select().id(GlobalVariables.unfBowId).isEmpty() || ctx.inventory.select().id(Constants.BOW_STRING).isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void execute() {
        GlobalVariables.working = false;
        if (!ctx.bank.inViewport()) {
            BankFinder.lookForBank(ctx);
        }

        if (!ctx.bank.opened()) {
            ctx.bank.open();
        }

        System.out.println("Deposits...");
        ctx.bank.depositInventory();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.select().isEmpty();
            }
        }, 500, 2);
    }

}
