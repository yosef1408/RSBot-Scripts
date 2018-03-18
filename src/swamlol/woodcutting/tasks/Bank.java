package swamlol.woodcutting.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import swamlol.woodcutting.Task;

import java.util.concurrent.Callable;

public class Bank extends Task {

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.backpack.select().count()>27 && ctx.bank.nearest().tile().distanceTo(ctx.players.local())<6;
    }

    @Override
    public void execute() {
        if(ctx.bank.opened()){
            final int inventCount = ctx.backpack.select().count();
            if(ctx.bank.depositInventory()){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.backpack.select().count() != inventCount;
                    }
                }, 250, 20);
            ctx.bank.close();}
        } else {
            if(ctx.bank.inViewport()) {
                if(ctx.bank.open()){
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.bank.opened();
                        }
                    }, 250, 20);
                }
            } else{
                ctx.camera.turnTo(ctx.bank.nearest());
            }
        }
    }
}
