package Dirtsleeper.scripts.RuneEssenceMiner;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import static org.powerbot.script.Condition.sleep;

/**
 * Created by Casey on 12/29/2016.
 */
public class MoveToPortal extends Task
{
    private int[] portalId = {39831};

    public MoveToPortal(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        return ctx.backpack.select().count() == 28
                && !ctx.objects.select().id(portalId).isEmpty()
                && RuneEssenceMiner.STATUS != STATE.PORTAL;
    }

    @Override
    public void execute()
    {
        GameObject portal = ctx.objects.nearest().poll();
        if (portal.inViewport())
        {
            if (portal.interact("Enter"))
                RuneEssenceMiner.STATUS = STATE.PORTAL;
        }
        else
        {
            ctx.movement.step(portal);
            ctx.camera.turnTo(portal);
        }
    }
}
