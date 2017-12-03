package iDzn.NoobNester.Tasks;

import iDzn.NoobNester.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import java.util.concurrent.Callable;

public class WithdrawRings extends Task{

    public WithdrawRings(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count()==0;

    }

    @Override
    public void execute() {
        if (ctx.bank.opened()) {
            if (ctx.bank.select().id(5074).isEmpty()) {
                ctx.controller.stop();
            } else if (ctx.inventory.select().count()==0 && !ctx.bank.select().id(5074).isEmpty()) {
                ctx.bank.withdraw(5074, 14);
                ctx.bank.close();
                Condition.wait(new Callable<Boolean>() {

                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
                }, 55, 20);
            }    }

    }

    public static class Withdraw extends Task{

        public Withdraw(ClientContext ctx) {
            super(ctx);
        }

        @Override
        public boolean activate() {
            return ctx.inventory.select().count()==0;

            }

            @Override
        public void execute() {
                     if (ctx.bank.opened()) {
                        if (ctx.bank.select().id(13653).isEmpty()) {
                            ctx.controller.stop();
                        } else if (ctx.inventory.select().count()==0 && !ctx.bank.select().id(13653).isEmpty()) {
                            ctx.bank.withdraw(13653, 16);
                            ctx.bank.close();
                            Condition.wait(new Callable<Boolean>() {

                                @Override
                                public Boolean call() throws Exception {
                                    return ctx.bank.opened();
                                }
                            }, 55, 20);
                }    }

        }
    }
}
