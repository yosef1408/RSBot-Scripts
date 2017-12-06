package src.sheel.BarbarianFisherAndCooker.leaves;

import src.sheel.BarbarianFisherAndCooker.TreeBot.LeafTask;
import org.powerbot.script.rt6.ClientContext;

public class OpenBank extends LeafTask
{

    public OpenBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void execute()
    {
        ctx.bank.open();
    }
}
