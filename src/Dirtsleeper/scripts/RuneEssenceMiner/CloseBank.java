package Dirtsleeper.scripts.RuneEssenceMiner;

import org.powerbot.script.rt6.ClientContext;

/**
 * Created by Casey on 12/29/2016.
 */
public class CloseBank extends Task
{

    private int[] itemId = {1436};

    public CloseBank(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        return ctx.backpack.select().count() == 0
                && ctx.bank.opened();
    }

    @Override
    public void execute()
    {
        ctx.bank.close();
    }
}
