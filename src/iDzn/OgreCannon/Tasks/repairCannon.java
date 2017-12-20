package iDzn.OgreCannon.Tasks;

import iDzn.OgreCannon.Task;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class repairCannon extends Task<org.powerbot.script.ClientContext<Client>> {
    public repairCannon(ClientContext ctx) {
        super(ctx);
    }

    GameObject cannonToFix = ctx.objects.select().id(14916).poll();

    @Override
    public boolean activate() {
        return cannonToFix.valid();

    }

    @Override
    public void execute() {
        final GameObject cannonToFix = ctx.objects.select().id(14916).poll();
        if (cannonToFix.valid()){
            System.out.println("Fixing");
            cannonToFix.interact("Repair", "Broken multicannon");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !cannonToFix.valid();
                }
            }, 100, 20);
        }
    }
}