package iDzn.OldschoolRS.Tasks;

import iDzn.OldschoolRS.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;


public class OpenNests extends Task {
    public OpenNests(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.bank.opened() && ctx.inventory.select().id(13653).count()>0;
    }

    @Override
    public void execute() {
        Item nestToOpen = ctx.inventory.select().id(13653).poll();
        nestToOpen.interact("Search", "Bird nest");
        Condition.wait(new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inMotion();
            }
        }, 30,20);



        }

    }


