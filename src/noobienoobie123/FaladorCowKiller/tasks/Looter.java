package noobienoobie123.FaladorCowKiller.tasks;

/**
 * Created by larry on 7/18/2017.
 */

import noobienoobie123.FaladorCowKiller.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;


import java.util.concurrent.Callable;


public class Looter extends Task{

    final static int cowHide =  1739;
    public static int cowHideTotal;

    public Looter(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        GroundItem cowHide = ctx.groundItems.select().id(1739).nearest().poll();
        return ctx.inventory.select().count()<28 && cowHide.valid();
    }

    @Override
    public void execute() {
        final GroundItem cowHide = ctx.groundItems.select().id(1739).nearest().poll();
        final int inventoryTemp = ctx.inventory.select().count();

        if(!cowHide.inViewport())
        {
            ctx.movement.step(cowHide);
            ctx.camera.turnTo(cowHide);

            Condition.wait(new Callable<Boolean>()
            {
                @Override
                public Boolean call() throws Exception
                {
                    return cowHide.inViewport();
                }
            }, 250, 20);
        }

        cowHide.interact("Take","Cowhide");
        cowHideTotal = cowHideTotal + 1;



        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {


                return inventoryTemp != ctx.inventory.select().count();
            }
        },200,10);
    }

    public int getCowHideTotal(){
        return cowHideTotal;
    }
}
