package Dirtsleeper.scripts.RuneEssenceMiner;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import static org.powerbot.script.Condition.sleep;

public class Mine extends Task
{
    private int[] veinId = {2491};

    public Mine(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        return ctx.backpack.select().count() < 28
                && !ctx.objects.select().id(veinId).isEmpty()
                && RuneEssenceMiner.STATUS != STATE.MINING;
    }

    @Override
    public void execute()
    {
        GameObject vein = ctx.objects.nearest().poll();
        if (vein.inViewport())
        {
            if (vein.click())
                RuneEssenceMiner.STATUS = STATE.MINING;
        }
        else
        {
            ctx.movement.step(vein);
            ctx.camera.turnTo(vein);
        }
    }

}
