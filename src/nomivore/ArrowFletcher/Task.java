package nomivore.ArrowFletcher;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.Condition;

import java.util.concurrent.Callable;


public abstract class Task<C extends ClientContext> extends ClientAccessor<C>
{
   public Task(C ctx)
   {
     super(ctx);
   }
   protected boolean failed = false;

   protected int limit;

   public abstract void initialise();

   public abstract boolean activate();

   public abstract void execute();

   public abstract void message(MessageEvent me);

   public boolean failed()
   {
     return failed;
   }

   public abstract String getName();

   public abstract String getActionName();

   public abstract void setLimit(int num);

   public abstract int profit();

    public void openNearbyBank() {
        if (ctx.bank.inViewport()) {
            if (ctx.inventory.selectedItem().valid()) ctx.inventory.selectedItem().interact("Cancel");
            if (ctx.bank.open()) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
                }, 250, 10);
            }
        } else {
            ctx.camera.turnTo(ctx.bank.nearest());
        }
    }

    public void depositInventory() {
        if (ctx.bank.depositInventory()) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().count() == 0;
                }
            });
        }
    }
}