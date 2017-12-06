package src.sheel.BarbarianFisherAndCooker.leaves;

import src.sheel.BarbarianFisherAndCooker.Constants;
import src.sheel.BarbarianFisherAndCooker.TreeBot.LeafTask;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.Npc;
import org.powerbot.script.rt6.ClientContext;

public class Fish extends LeafTask
{
    public Fish(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {
                Npc spot = ctx.npcs.select().id(Constants.FISHING_SPOT).nearest().poll();

                if(!spot.inViewport())
                {
                    ctx.camera.turnTo(spot);
                }

                spot.interact("Lure");

        Condition.wait(() -> ctx.players.local().animation() == Constants.FISHING_ANIMATION, 250, 10);
    }
}
