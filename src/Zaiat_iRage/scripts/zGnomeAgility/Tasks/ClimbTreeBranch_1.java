package Zaiat_iRage.scripts.zGnomeAgility.Tasks;

import Zaiat_iRage.scripts.zGnomeAgility.zGnomeAgility;
import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;

/**
 * Created by Zaiat on 4/28/2017.
 */
public class ClimbTreeBranch_1 extends Task<ClientContext>
{
    private boolean movedCamera;
    private int id_tree_branch = 23559;
    private final int[] bounds = {-2, 2, -156, -128, -16, 36};
    private zGnomeAgility script;
    private int floor = 1;
    private Area area_tree_branch_1 = new Area(new Tile(2471, 3424+1, floor), new Tile(2476+1, 3422, floor));
    private long timer;

    public ClimbTreeBranch_1(zGnomeAgility script, ClientContext ctx)
    {
        super(ctx);
        this.script = script;
    }

    @Override
    public boolean activate()
    {
        boolean activate = ctx.game.floor() == floor &&  area_tree_branch_1.contains(ctx.players.local());
        if(activate && ctx.players.local().inMotion())
            timer = script.getRuntime();
        return activate;
    }

    @Override
    public void execute()
    {
        script.UpdateScript("Climbing Tree branch");

        if(timer != 0)
            if(script.getRuntime() - timer >= 2000)
                timer = 0;
        if(timer == 0)
        {
            GameObject object = ctx.objects.select().id(id_tree_branch).nearest().each(Interactive.doSetBounds(bounds)).poll();
            if (object.inViewport())
            {
                if (object.interact("Climb", "Tree branch"))
                {
                    script.CameraLookatSync(270);
                    timer = script.getRuntime();
                }
            }
        }
    }
}
