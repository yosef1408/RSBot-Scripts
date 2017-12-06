package src.sheel.BarbarianFisherAndCooker.leaves;

import src.sheel.BarbarianFisherAndCooker.Constants;
import src.sheel.BarbarianFisherAndCooker.TreeBot.LeafTask;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

public class WalkToBank extends LeafTask
{
    public WalkToBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {
        Player player = ctx.players.local();
        Tile bankRegion = Constants.BANK_TILE;
        ctx.movement.step(bankRegion);

        Condition.wait(() -> bankRegion.distanceTo(player) < 3, 250, 10);
    }
}
