package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import sscripts.sgaltar.SGAltar;

import java.util.concurrent.Callable;


public class Banking extends Task {
    public Banking(ClientContext arg0) {
        super(arg0);
    }

    @Override
    public boolean activate() {
        return ctx.bank.opened() && ctx.inventory.select().count() != 28;
    }

    @Override
    public void execute() {
        SGAltar.status="Banking - Withdrawing Bones";
        final Item i = ctx.bank.select().id(SGAltar.boneID).first().poll();
        if(ctx.bank.withdraw(i, 28)){
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().contains(i);
                }
            }, 300, 5);
        }
    }
}
