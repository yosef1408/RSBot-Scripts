package Dirtsleeper.scripts.RuneEssenceMiner;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

/**
 * Created by Casey on 12/29/2016.
 */
public class MoveToBank extends Task
{
    private int[] bankId = {782};

    public MoveToBank(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        return ctx.backpack.select().count() == 28
                && ctx.bank.present()
                && !ctx.objects.select().id(bankId).isEmpty();
    }

    @Override
    public void execute()
    {
        RuneEssenceMiner.STATUS = STATE.BANK;
        GameObject bank = ctx.objects.nearest().poll();
        if (bank.inViewport())
        {
            ctx.bank.open();
        }
        else
        {
            ctx.movement.step(bank);
            ctx.camera.turnTo(bank);
        }
    }
}
