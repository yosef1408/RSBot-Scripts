package PMiner.Tasks;

import PMiner.PMinerConst;
import PMiner.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Banking extends Task {

    public Banking(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count()>27 && ctx.bank.nearest().tile().distanceTo(ctx.players.local())<6;
    }

    @Override
    public void execute() {
        if(ctx.bank.opened()){
            final int inventCount = ctx.inventory.select().count();

           if(ctx.bank.depositAllExcept("pickaxe")){

                        Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().count() != inventCount;
                        }
                    },250,20);
                }

            if (ctx.bank.select().id(PMinerConst.IRON_ORE).poll().stackSize() >500) {
                if (ctx.bank.withdrawModeNoted(true)){

                if (ctx.bank.withdraw(PMinerConst.IRON_ORE, Bank.Amount.ALL)) {

                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().count() != inventCount;
                        }
                    }, 250, 20);

                }
                if(ctx.bank.close()){
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return !ctx.bank.opened();
                        }
                    },250,10);
                }

            }
            }
        }


        else {
            if (ctx.bank.inViewport()) {
               if( ctx.bank.open()){
                   Condition.wait(new Callable<Boolean>() {
                       @Override
                       public Boolean call() throws Exception {
                           return ctx.bank.opened();
                       }
                   },250,20);
               }
            }
            else{
                ctx.camera.turnTo((ctx.bank.nearest()));

            }


        }

    }

}
