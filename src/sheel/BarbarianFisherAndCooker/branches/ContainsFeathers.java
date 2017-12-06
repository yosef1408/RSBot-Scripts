package src.sheel.BarbarianFisherAndCooker.branches;

import src.sheel.BarbarianFisherAndCooker.leaves.StopBot;
import src.sheel.BarbarianFisherAndCooker.TreeBot.BranchTask;
import src.sheel.BarbarianFisherAndCooker.TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

public class ContainsFeathers extends BranchTask {

    private InFishingRegion inFishingRegionBranch = new InFishingRegion(ctx);
    private StopBot stopBotLeaf = new StopBot(ctx);


    public ContainsFeathers(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return contains("Feather");
    }

    @Override
    public TreeTask successTask() {
        return inFishingRegionBranch;
    }

    @Override
    public TreeTask failureTask() {
        return stopBotLeaf;
    }


    private boolean contains (String name)
    {
        Item[] items = ctx.backpack.items();
        for (Item item : items)
        {
            if(item.name().equals(name))
                return true;
        }
        return false;
    }
}
