package iDzn.OldschoolRS.OgreCannon.Tasks;

import iDzn.OldschoolRS.OgreCannon.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

import java.util.concurrent.Callable;

public class deployCannon extends Task {
    public deployCannon(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(6, 8, 10, 12).count() >0 && ctx.players.local().animation()==-1;
    }

    @Override
    public void execute() {
        Item cannonToDeploy = ctx.inventory.select().id(6).poll();
        cannonToDeploy.interact("Set-up", "Cannon base");
        Condition.wait(new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inMotion();
            }
        }, 300,20);
    }
}
