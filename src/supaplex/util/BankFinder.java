package supaplex.util;

import supaplex.exceptions.BankException;
import org.powerbot.script.Condition;
import org.powerbot.script.Locatable;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.TilePath;

import java.util.concurrent.Callable;

/**
 * Created by Andreas on 30.06.2016.
 */
public class BankFinder {
    /**
     * Searches for nearby bank for 5 seconds. Throws exception if the bank not found
     *
     * @param ctx
     * @throws BankException
     */
    public static void lookForBank(ClientContext ctx) throws BankException {
        System.out.println("Looks for bank..");
        Locatable nearest = ctx.bank.nearest();
        TilePath tilePath = ctx.movement.newTilePath(nearest.tile());
        tilePath.traverse();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.inViewport();
            }
        }, 1000, 5);

        if (!ctx.bank.inViewport()) {
            throw new BankException("Cannot find the bank");
        }
    }
}
