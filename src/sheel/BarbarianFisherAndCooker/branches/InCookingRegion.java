package src.sheel.BarbarianFisherAndCooker.branches;

import src.sheel.BarbarianFisherAndCooker.Constants;
import src.sheel.BarbarianFisherAndCooker.leaves.WalkToCookingRegion;
import src.sheel.BarbarianFisherAndCooker.TreeBot.BranchTask;
import src.sheel.BarbarianFisherAndCooker.TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class InCookingRegion extends BranchTask
{
    private IsCooking isCookingBranch = new IsCooking(ctx);
    private WalkToCookingRegion walkToCookingRegionLeaf = new WalkToCookingRegion(ctx);

    public InCookingRegion(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.players.local().tile().distanceTo(Constants.FIRE_TILE) < 3;
    }

    @Override
    public TreeTask successTask() {
        return isCookingBranch;
    }

    @Override
    public TreeTask failureTask() {
        return walkToCookingRegionLeaf;
    }
}
