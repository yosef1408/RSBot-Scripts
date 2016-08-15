package supaplex.tasks.cutting;

import org.powerbot.script.rt4.*;
import supaplex.tasks.Task;
import supaplex.util.BankFinder;
import supaplex.util.Constants;
import supaplex.util.GlobalVariables;

/**
 * Created by Andreas on 29.06.2016.
 */
public class DepositUnfBows extends Task<ClientContext> {

    private Player player = ctx.players.local();

    public DepositUnfBows(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        // Checks if player stands still
        if (player.animation() == -1) {
            if (ctx.inventory.select().isEmpty()) {
                return false;
            }
            // Start depositing if no logs, nor knife in inventory
            if (ctx.inventory.select().id(GlobalVariables.logId).isEmpty() || ctx.inventory.select().id(Constants.KNIFE).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute() {

        if (!ctx.bank.inViewport()) {
            BankFinder.lookForBank(ctx);
        }

        if (!ctx.bank.opened()) {
            ctx.bank.open();
        }

        System.out.println("Deposits...");
        ItemQuery<Item> items = ctx.inventory.select();

        for (Item item : items) {
            if (item.id() == Constants.KNIFE || item.id() == GlobalVariables.unfBowId) {
                continue;
            }
            ctx.bank.depositInventory();
            break;
        }

        if (!ctx.inventory.select().id(GlobalVariables.unfBowId).isEmpty())
            ctx.bank.deposit(GlobalVariables.unfBowId, 0);

        if (ctx.inventory.select().id(Constants.KNIFE).isEmpty()) ctx.bank.depositInventory();

    }
}
