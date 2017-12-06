package src.sheel.BarbarianFisherAndCooker.leaves;

import src.sheel.BarbarianFisherAndCooker.Constants;
import src.sheel.BarbarianFisherAndCooker.TreeBot.LeafTask;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

public class WalkToFishingRegion extends LeafTask {

    public WalkToFishingRegion(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {
        Player player = ctx.players.local();
        Tile fishingRegion = Constants.FISHING_TILE;
        ctx.movement.step(fishingRegion);

        Condition.wait(() -> fishingRegion.distanceTo(player) < 3, 250, 10);



    }
}
