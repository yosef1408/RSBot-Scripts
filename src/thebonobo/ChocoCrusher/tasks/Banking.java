package thebonobo.ChocoCrusher.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;
import thebonobo.ChocoCrusher.utils.Antiban;
import thebonobo.ChocoCrusher.utils.Info;
import thebonobo.ChocoCrusher.utils.Items;

import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 16/09/17
 */

public class Banking extends Task<ClientContext> {
    private int[] bankBooths = {100063, 10059, 10061, 10060};

    public Banking(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.objects.select(5).id(bankBooths).poll().valid() &&
                ctx.inventory.select().id(Items.CHOCOLATE_BAR).count() == 0;
    }

    @Override
    public void execute() {
        System.out.println("bars in inventory is: " + ctx.inventory.select().id(Items.CHOCOLATE_BAR).count());
        Info.getInstance().setCurrentTask("Banking");

        if (ctx.inventory.selectedItem().valid())
            ctx.bank.open();

        if (ctx.bank.opened()) {
            ctx.bank.deposit(Items.CHOCOLATE_DUST, Bank.Amount.ALL);
            if (ctx.bank.select().id(Items.CHOCOLATE_BAR).isEmpty()) {
                ctx.controller.stop();
            } else {
                Antiban.WaitAfterBankInteraction();
                ctx.bank.withdraw(Items.CHOCOLATE_BAR, Bank.Amount.ALL);
            }
            Antiban.WaitAfterBankInteraction();
            ctx.bank.close();
        } else {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    //ctx.camera.turnTo(ctx.objects.select(5).id(bankBooths).shuffle().poll().tile());
                    ctx.bank.open();
                    return ctx.bank.opened();
                }
            }, Random.nextInt(800, 1500), 5);

        }
    }
}
