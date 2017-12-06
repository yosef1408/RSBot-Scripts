package src.sheel.BarbarianFisherAndCooker.leaves;

import src.sheel.BarbarianFisherAndCooker.TreeBot.LeafTask;
import org.powerbot.script.rt6.ClientContext;

public class Cook extends LeafTask {
    public Cook(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute() {
        ctx.productionInterface.makeItem();
    }
}
