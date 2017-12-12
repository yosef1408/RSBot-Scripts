package iDzn.KegBalancer.Tasks;

import iDzn.KegBalancer.Task;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import java.util.concurrent.Callable;

public class WithdrawTuna extends Task<org.powerbot.script.ClientContext<Client>> {

    public WithdrawTuna(ClientContext ctx) {
        super(ctx);
    }
    private static int TUNA = 361;
    private static int ENERGY = 3008;

    @Override
    public boolean activate() {
        return ctx.inventory.select().count()==0 && ctx.bank.opened();

    }

    @Override
    public void execute() {
        System.out.println("Withdrawing Tuna");
        if (ctx.bank.select().id(TUNA).isEmpty()) {
            ctx.controller.stop();
        } else if (ctx.inventory.select().count()==0 && !ctx.bank.select().id(TUNA).isEmpty()) {
            ctx.bank.withdraw(TUNA,2);
            ctx.bank.withdraw(ENERGY, 26);
            ctx.bank.close();
            Condition.wait(new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.opened();
                }
            }, 200, 20);
        }    }

}