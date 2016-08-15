package supaplex.tasks.cutting;

import supaplex.exceptions.ResourceException;
import supaplex.exceptions.ResourceType;
import org.powerbot.script.rt4.*;
import supaplex.tasks.Task;
import supaplex.util.BankFinder;
import supaplex.util.Constants;
import supaplex.util.GlobalVariables;

/**
 * Created by Andreas on 29.06.2016.
 */
public class WithdrawLogs extends Task<ClientContext> {

    private Player player = ctx.players.local();

    public WithdrawLogs(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        // Checks if player stands still
        if (player.animation() == -1) {
            ItemQuery<Item> items = ctx.inventory.select();

            // Checks for other items than knife, logs or unfinished bows in inventory. If other items are present, don't withdraw new items
            for (Item item : items) {
                if (item.id() == Constants.KNIFE || item.id() == GlobalVariables.logId || item.id() == GlobalVariables.unfBowId)
                    continue;
                return false;
            }

            // Start withdrawing if inventory is empty or only knife is present
            if ((ctx.inventory.select().id(Constants.KNIFE).count() == 1 && ctx.inventory.select().count() == 1) || ctx.inventory.select().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Withdraws knife and logs. Throws exception if none in bank
     *
     * @throws ResourceException
     */
    @Override
    public void execute() throws ResourceException {

        if (!ctx.bank.inViewport()) {
            BankFinder.lookForBank(ctx);
        }

        if (!ctx.bank.opened()) {
            ctx.bank.open();
        }

        System.out.println("Withdraws...");

        if (ctx.inventory.select().id(Constants.KNIFE).isEmpty()) {
            if (ctx.bank.opened() && !ctx.bank.withdraw(Constants.KNIFE, 1) && ctx.bank.select().id(Constants.KNIFE).isEmpty()) {
                ctx.bank.close();
                throw new ResourceException("No knife in bank", ResourceType.KNIFE);
            }
        }

        if (ctx.inventory.select().id(GlobalVariables.logId).isEmpty()) {
            if (ctx.bank.opened() && !ctx.bank.withdraw(GlobalVariables.logId, 0) && ctx.bank.select().id(GlobalVariables.logId).isEmpty()) {
                ctx.bank.close();
                GlobalVariables.taskFinished = true;
                throw new ResourceException("No logs in bank", ResourceType.LOGS);
            }
        }

        ctx.bank.close();

    }
}
