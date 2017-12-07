package iDzn.OgreCannon.Tasks;

import iDzn.OgreCannon.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class repairCannon extends Task {
    public repairCannon(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate() {
        return (!ctx.objects.select().id(14916).isEmpty());

    }

    @Override
    public void execute() {
              GameObject cannonToFix = ctx.objects.select().id(14916).poll();
            if (cannonToFix.inViewport()) {
                cannonToFix.interact("Repair", "Broken multicannon");
                Condition.wait(new Callable<Boolean>(){
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().inMotion();
                    }
                }, 100,20);
                }
            }

        }

