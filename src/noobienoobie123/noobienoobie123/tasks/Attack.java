package noobienoobie123.tasks;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import osrs.Task;

import java.util.concurrent.Callable;

/**
 * Created by larry on 7/14/2017.
 */
public class Attack extends Task {

    final static int MONSTER_IDS [] = {2864,2863,2143,2144,2145,3017,3019,3029,3030,3031,3032,3033,3034,3035};
    Area goblinHouse = new Area(new Tile(3243,3244,0),new Tile(3248,3247,0));


    public Attack(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.players.local().inCombat();
    }



    @Override
    public void execute() {


        final Npc monsterToAttack = ctx.npcs.select().id(MONSTER_IDS).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc) {
                return !npc.inCombat() && !goblinHouse.contains(npc);
            }
        }).nearest().poll();


        if(!monsterToAttack.inViewport())
        {
            ctx.movement.step(monsterToAttack);
            ctx.camera.turnTo(monsterToAttack);

            Condition.wait(new Callable<Boolean>()
            {
                @Override
                public Boolean call() throws Exception
                {
                    return monsterToAttack.inViewport();
                }
            }, 250, 20);
        }

        monsterToAttack.interact("Attack");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat();
            }
        },500,100);




    }














}
