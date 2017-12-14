package iDzn.KegBalancer.Tasks;

import iDzn.KegBalancer.KegBalancer;
import iDzn.KegBalancer.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import java.util.concurrent.Callable;

public class Withdraw extends Task<ClientContext> {

       KegBalancer main;

    public Withdraw(ClientContext ctx, KegBalancer main) {

        super(ctx);
        this.main = main;

    }

    private static int ENERGY = 3008;

    @Override
    public boolean activate() {
        return ctx.inventory.select().count()==0 && ctx.bank.opened() && main.FOOD != 0;

    }

    @Override
    public void execute() {
        System.out.println("Withdrawing food && energy potions");

        if (ctx.bank.select().id(main.FOOD).isEmpty()) {
            ctx.controller.stop();
        } else if (ctx.inventory.select().count()==0 && !ctx.bank.select().id(main.FOOD).isEmpty()) {
            ctx.bank.withdraw(main.FOOD,2);
            ctx.bank.withdraw(ENERGY, 26);
            ctx.bank.close();
            Condition.wait(new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.opened();
                }
            }, 85, 20);
        }    }

}
