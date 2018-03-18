package swamlol.woodcutting.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;
import sun.rmi.runtime.Log;
import swamlol.woodcutting.Task;

import java.util.concurrent.Callable;

public class Drop extends Task{

    final static int normalLogs = 1511;

    public Drop(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.backpack.select().count()>27;
    }

    @Override
    public void execute() {

    for(Item treeLog : ctx.backpack.select().id(normalLogs)){
        if(ctx.controller.isStopping()){
            break;
        }

        final int startAmountLogs = ctx.backpack.select().id(normalLogs).count();
        treeLog.interact("Drop","Logs");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.backpack.select().id(normalLogs).count() != startAmountLogs;
            }
        }, 25, 20);
    }

    }
}
