package PMiner.Tasks;

import PMiner.GrandExchange;
import PMiner.PMinerConst;
import PMiner.Task;
import PMiner.Tasks.SubTasks.GeToBank;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

public class Sell extends Task
{
    GrandExchange ge = new GrandExchange(ctx);
    public Sell(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        ctx.inventory.select().id(441).count();
        return ctx.inventory.select().id(441).count() != 0;
    }

    @Override
    public void execute() {
        System.out.println("ge closed");
        if(ge.open()){
            Item ore = ctx.inventory.select().id(441).poll();
            System.out.println("ge open");
       int slots = ge.getAvailableSlots();
           if(ge.sell(ore,ctx.inventory.select().id(441).poll().stackSize(),70)){
               if(ge.collectToBank()){
                   Condition.wait(new Callable<Boolean>() {
                       @Override
                       public Boolean call() throws Exception {
                           return ge.getAvailableSlots() != slots;
                       }
                   },200,10);
               }
               if(ge.close()){
                   Condition.wait(new Callable<Boolean>() {
                       @Override
                       public Boolean call() throws Exception {
                           return !ge.opened();
                       }
                   },200,10);
               }
               GeToBank walk = new GeToBank(ctx,PMinerConst.EastBankToGE);
               walk.execute();
           }
        }

    }
}
