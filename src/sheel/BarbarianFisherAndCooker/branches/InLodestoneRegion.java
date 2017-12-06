package src.sheel.BarbarianFisherAndCooker.branches;

import src.sheel.BarbarianFisherAndCooker.Constants;
import src.sheel.BarbarianFisherAndCooker.leaves.WalkToBank;
import src.sheel.BarbarianFisherAndCooker.TreeBot.BranchTask;
import src.sheel.BarbarianFisherAndCooker.TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class InLodestoneRegion extends BranchTask
{
    private WalkToBank walkToBankLeaf = new WalkToBank(ctx);

    public InLodestoneRegion(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.players.local().tile().distanceTo(Constants.EDGEVILLE_LODESTONE_TILE) < 5;
    }

    @Override
    public TreeTask successTask() {
        return walkToBankLeaf;
    }

    @Override
    public TreeTask failureTask() {
        return null;
    }
}
