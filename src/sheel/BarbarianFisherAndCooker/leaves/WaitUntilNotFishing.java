package src.sheel.BarbarianFisherAndCooker.leaves;

import src.sheel.BarbarianFisherAndCooker.Constants;
import src.sheel.BarbarianFisherAndCooker.TreeBot.LeafTask;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

public class WaitUntilNotFishing extends LeafTask {

    public WaitUntilNotFishing(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {
        Player player = ctx.players.local();

        Condition.wait(() -> player.animation() != Constants.FISHING_ANIMATION, 250, 10);

    }
}
