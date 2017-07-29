package noobienoobie123.FaladorCowKiller.tasks;

import noobienoobie123.FaladorCowKiller.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.Bank;

import java.util.concurrent.Callable;

/**
 * Created by larry on 7/27/2017.
 */
public class BankForTan extends Task {

    Area AlBank = new Area(new Tile(3272,3171,0),new Tile(3269,3161,0));


    public BankForTan(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {

        return (((ctx.inventory.select().id(1741).count()>0 || ctx.inventory.select().id(1743).count()>0) && ctx.bank.nearest().tile().distanceTo(ctx.players.local())<4) || (ctx.inventory.select().id(1739).count()==0 && ctx.bank.nearest().tile().distanceTo(ctx.players.local())<4))  ;

    }

    @Override
    public void execute() {
        if(ctx.bank.opened()){
            if (ctx.bank.deposit("Hard leather" , Bank.Amount.ALL)){

                Condition.wait(new Callable<Boolean>()
                {
                    @Override
                    public Boolean call() throws Exception
                    {
                        return ctx.inventory.select().count() == 1;
                    }
                }, 200, 20);

            }
            if(ctx.inventory.select().id(995).count()==0){
                Condition.sleep(750);

                ctx.bank.withdraw(995, Bank.Amount.ALL);
            }
            if(ctx.inventory.select().id(1739).count() == 0){
                   Condition.sleep(500);
                    ctx.bank.withdraw(1739, 27);

                if(ctx.bank.select().id(1739).count()==0){
                    ctx.controller.stop();
                }
            }
        }
        else{
            if (ctx.bank.inViewport()){
                if(ctx.bank.open()){
                    Condition.wait(new Callable<Boolean>()
                    {
                        @Override
                        public Boolean call() throws Exception
                        {
                            return ctx.bank.opened();
                        }
                    }, 250, 20);
                }
            }

            else{
                ctx.camera.turnTo(ctx.bank.nearest());
            }

        }
    }
}
