package src.sheel.BarbarianFisherAndCooker.branches;

import src.sheel.BarbarianFisherAndCooker.Constants;
import src.sheel.BarbarianFisherAndCooker.leaves.Fish;
import src.sheel.BarbarianFisherAndCooker.leaves.WaitUntilNotFishing;
import src.sheel.BarbarianFisherAndCooker.TreeBot.BranchTask;
import src.sheel.BarbarianFisherAndCooker.TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class IsFishing extends BranchTask {

    private Fish fishLeaf = new Fish(ctx);
    private WaitUntilNotFishing waitUntilNotFishingLeaf = new WaitUntilNotFishing(ctx);


    public IsFishing(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.players.local().animation() == Constants.FISHING_ANIMATION;
    }

    @Override
    public TreeTask successTask() {
        return waitUntilNotFishingLeaf;
    }

    @Override
    public TreeTask failureTask() {
        return fishLeaf;
    }
}
