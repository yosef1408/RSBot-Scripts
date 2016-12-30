package Dirtsleeper.scripts.RuneEssenceMiner;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Npc;

import static org.powerbot.script.Condition.sleep;

/**
 * Created by Casey on 12/29/2016.
 */
public class MoveToAubury extends Task
{
    private int[] auburyId = {5913};
    Npc aubury;

    public MoveToAubury(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        ctx.npcs.select().id(auburyId).isEmpty();
        aubury = ctx.npcs.nearest().poll();
        return ctx.backpack.select().count() == 0
                && aubury.valid()
                && ctx.movement.reachable(ctx.players.local().tile(), aubury.tile())
                && RuneEssenceMiner.STATUS != STATE.AUBURY;
    }

    @Override
    public void execute()
    {
        if (aubury.inViewport())
        {
            if (aubury.interact("Teleport"))
            {
                RuneEssenceMiner.STATUS = STATE.AUBURY;
                sleep(2500);
            }

        }
        else
        {
            ctx.movement.step(aubury);
            ctx.camera.turnTo(aubury);
        }
    }
}
