package supaplex.tasks.stringing;

import supaplex.exceptions.ResourceException;
import supaplex.exceptions.ResourceType;
import org.powerbot.script.rt4.*;
import supaplex.tasks.Task;
import supaplex.util.*;
import supaplex.util.Constants;

/**
 * Created by Andreas on 20.07.2016.
 */
public class WithdrawUnfBowsAndStrings extends Task<ClientContext> {

    private Player player = ctx.players.local();

    public WithdrawUnfBowsAndStrings(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        // Checks if player stands still
        if (player.animation() == -1) {
            // Withdraw only if inventory is empty
            if (ctx.inventory.select().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Withdraws unf bows and bow strings. Throws exception if none in bank
     *
     * @throws ResourceException
     */
    @Override
    public void execute() throws ResourceException {
        GlobalVariables.working = false;
        if (!ctx.bank.inViewport()) {
            BankFinder.lookForBank(ctx);
        }

        if (!ctx.bank.opened()) {
            ctx.bank.open();
        }

        System.out.println("Withdraws...");

        // Only withdraw items if bank window is open
        if (ctx.bank.opened()) {
            // Tries to withdraw bow strings
            if (ctx.bank.withdraw(supaplex.util.Constants.BOW_STRING, 14)) {
                // Tries to withdraw unfinished bows
                if (!ctx.bank.withdraw(GlobalVariables.unfBowId, 0)) {
                    // Failed to withdraw unfinished bows, check if bank is empty
                    if (ctx.bank.select().id(GlobalVariables.unfBowId).isEmpty()) {
                        ctx.bank.close();
                        GlobalVariables.taskFinished = true;
                        throw new ResourceException("No unf bows in bank", ResourceType.UNF_BOWS);
                        // Sets global variable indicating that a new batch of items were withdrawn
                    }
                }
            }
            // Failed to withdraw bow strings
            else {
                // Check if bank is empty
                if (ctx.bank.select().id(Constants.BOW_STRING).isEmpty()) {
                    ctx.bank.close();
                    GlobalVariables.taskFinished = true;
                    throw new ResourceException("No bow strings in bank", ResourceType.BOW_STRINGS);
                }
            }
        }

        ctx.bank.close();

    }
}
