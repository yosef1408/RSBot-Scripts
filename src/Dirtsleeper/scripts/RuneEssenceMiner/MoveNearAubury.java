package Dirtsleeper.scripts.RuneEssenceMiner;

import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Path;

/**
 * Created by Casey on 12/29/2016.
 */
public class MoveNearAubury extends Task
{
    private int[] auburyId = {5913};
    private Tile[] tileList = {new Tile(3259,3407, 0), new Tile(3258,3409, 0),
                                new Tile(3252,3407, 0), new Tile(3255,3407, 0),
                                new Tile(3258,3407, 0), new Tile(3258,3408, 0),
                                new Tile(3259,3408, 0), new Tile(3259,3406, 0)};
    private Tile moveTo;
    private Path p;
    public MoveNearAubury(ClientContext ctx)
    {
        super(ctx);
    }


    @Override
    public boolean activate()
    {
        boolean b = ctx.backpack.select().count() == 0
                && ctx.npcs.select().id(auburyId).isEmpty()
                && ctx.players.local().animation() == -1;
        if (b)
        {
            if (moveTo == null)
            {
                int i = Random.nextInt(0, tileList.length);
                moveTo = tileList[i];
                p = ctx.movement.findPath(moveTo);
            }
        }
        else
        {
            moveTo = null;
        }
        return b;
    }

    @Override
    public void execute()
    {
        RuneEssenceMiner.STATUS = STATE.MOVING;
        p.traverse();
    }
}
