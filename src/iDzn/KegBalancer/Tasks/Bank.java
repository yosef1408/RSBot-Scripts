package iDzn.KegBalancer.Tasks;

import iDzn.KegBalancer.Task;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Bank extends Task<org.powerbot.script.ClientContext<Client>> {

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count()<28
                && ctx.bank.nearest().tile().distanceTo(ctx.players.local()) <4;
    }

    @Override
    public void execute() {
        System.out.println("Banking");
        if (ctx.bank.opened()) {
            if (ctx.bank.depositInventory()) {
                Condition.wait(new Callable<Boolean>() {
                    final int inventCount = ctx.inventory.select().count();

                    @Override
                    public Boolean call() throws Exception {
                        return ctx.inventory.select().count() != inventCount;
                    }
                }, 250, 20);

            }
        } else {
            if (ctx.bank.inViewport()) {
                if (ctx.bank.open()) {
                    Condition.wait(new Callable<Boolean>() {

                        @Override
                        public Boolean call() throws Exception {
                            return ctx.bank.opened();
                        }
                    }, 250, 20);
                }
            } else {
                ctx.camera.turnTo(ctx.bank.nearest());
            }
        }

    }
}


