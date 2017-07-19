package noobienoobie123.FaladorCowKiller.tasks;


import noobienoobie123.FaladorCowKiller.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;


import java.util.concurrent.Callable;

/**
 * Created by larry on 7/17/2017.
 */
public class CowKiller extends Task{

    final static int COW_IDS [] = {2805,2808,2816,2806};
    Area cowPen = new Area(new Tile(3043,3313,0),new Tile(3021,3297,0));



    public CowKiller(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate() {

        return ctx.inventory.select().count()<28 && !ctx.players.local().inCombat() && cowPen.contains(ctx.players.local());
    }

    @Override
    public void execute() {

        final Npc cowToAttack = ctx.npcs.select().id(COW_IDS).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return !npc.inCombat();
            }
        }).nearest().poll();

        if(!cowToAttack.inViewport())
        {
            ctx.movement.step(cowToAttack);
            ctx.camera.turnTo(cowToAttack);

            Condition.wait(new Callable<Boolean>()
            {
                @Override
                public Boolean call() throws Exception
                {
                    return cowToAttack.inViewport();
                }
            }, 250, 20);
        }

        cowToAttack.interact("Attack");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat();
            }
        },250,20);

    }





}
