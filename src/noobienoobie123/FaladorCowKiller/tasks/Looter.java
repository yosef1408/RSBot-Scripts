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
    final int inventoryTemp = ctx.inventory.select().count();


    public Looter(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        GroundItem cowHide = ctx.groundItems.select().id(1739).nearest().poll();
        return ctx.inventory.select().count()<28 && cowHide.valid() && ctx.players.local().inCombat() == false ;
    }

    @Override
    public void execute() {

        int cowHideTempCounter = ctx.inventory.select().count();
        final GroundItem cowHide = ctx.groundItems.select().id(1739).nearest().poll();


        if(ctx.movement.energyLevel()>20 && !ctx.movement.running()){
            ctx.movement.running(true);
        }

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




        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {


                return inventoryTemp != ctx.inventory.select().count();
            }
        },600,10);

        Condition.sleep(1050);

    if(cowHideTempCounter!= ctx.inventory.select().count()){
        cowHideTotal = cowHideTotal + 1;
    }


    }

    public int getCowHideTotal(){
        return cowHideTotal;
    }
}
