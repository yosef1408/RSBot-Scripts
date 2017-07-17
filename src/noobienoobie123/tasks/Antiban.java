package noobienoobie123.tasks;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import osrs.Task;

/**
 * Created by larry on 7/16/2017.
 */
public class Antiban extends Task{


    public Antiban(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void execute() {
        int random =  (int )(Math. random() * 100 + 1);

        if (random ==1){
            Condition.sleep((int )(Math. random() * 1800 + 1000));

        }
        if (random==2){
            ctx.camera.turnTo(ctx.players.select().nearest().poll());

        }
        if (random==3){
            if(ctx.game.tab(Game.Tab.STATS)!= true) {
                ctx.game.tab(Game.Tab.STATS);
                Condition.sleep((int) (Math.random() * 1000 + 500));
            }
            else{
                Condition.sleep((int) (Math.random() * 1000 + 500));
            }
        }
    }
}
