package src.sheel.BarbarianFisherAndCooker.branches;

import src.sheel.BarbarianFisherAndCooker.leaves.Cook;
import src.sheel.BarbarianFisherAndCooker.leaves.UseFire;
import src.sheel.BarbarianFisherAndCooker.TreeBot.BranchTask;
import src.sheel.BarbarianFisherAndCooker.TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class IsCookingInterfaceOpen extends BranchTask
{
    private Cook cookLeaf = new Cook(ctx);
    private UseFire useFireLeaf = new UseFire(ctx);

    public IsCookingInterfaceOpen(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.productionInterface.opened();
    }

    @Override
    public TreeTask successTask() {
        return cookLeaf;
    }

    @Override
    public TreeTask failureTask() {
        return useFireLeaf;
    }
}
