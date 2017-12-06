package src.sheel.BarbarianFisherAndCooker.leaves;

import src.sheel.BarbarianFisherAndCooker.TreeBot.LeafTask;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;

public class WaitUntilNotCooking extends LeafTask
{
    public WaitUntilNotCooking(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {
        Condition.wait(() -> !ctx.productionInterface.working(), 250, 10);
    }
}
