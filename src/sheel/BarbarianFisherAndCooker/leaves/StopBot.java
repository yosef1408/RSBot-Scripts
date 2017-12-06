package src.sheel.BarbarianFisherAndCooker.leaves;

import org.powerbot.script.rt6.ClientContext;
import src.sheel.BarbarianFisherAndCooker.TreeBot.LeafTask;

public class StopBot extends LeafTask
{
    public StopBot(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {
        ctx.controller.stop();
    }
}
