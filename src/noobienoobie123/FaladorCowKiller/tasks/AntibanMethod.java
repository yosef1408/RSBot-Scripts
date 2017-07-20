package noobienoobie123.FaladorCowKiller.tasks;

/**
 * Created by larry on 7/17/2017.
 */

import noobienoobie123.FaladorCowKiller.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;


public class AntibanMethod extends Task
{
    public AntibanMethod(ClientContext ctx) {
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
