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
public class ClimbTreeBranch_2 extends Task<ClientContext>
{
    private boolean firstClick;
    private int id_tree_branch = 23560;
    private final int[] bounds = {-56, -20, -128, -92, -4, 8};
    private zGnomeAgility script;
    private int floor = 2;
    private Area area_tree_branch_2 = new Area(new Tile(2483, 3421+1, floor), new Tile(2488+1, 3418, floor));
    private long timer;

    public ClimbTreeBranch_2(zGnomeAgility script, ClientContext ctx)
    {
        super(ctx);
        this.script = script;
    }

    @Override
    public boolean activate()
    {
        boolean activate = ctx.game.floor() == floor &&  area_tree_branch_2.contains(ctx.players.local());
        if(activate && ctx.players.local().inMotion())
            timer = script.getRuntime();
        if(!activate && firstClick)
            firstClick = false;
        return activate;
    }

    @Override
    public void execute()
    {
        script.UpdateScript("Climbing Tree branch");

        if(timer != 0)
            if(script.getRuntime() - timer >= 2000)
                timer = 0;
        if(timer == 0 || !firstClick)
        {
            GameObject object = ctx.objects.select().id(id_tree_branch).nearest().each(Interactive.doSetBounds(bounds)).poll();
            if (object.inViewport())
            {
                if (object.interact("Climb-down", "Tree branch"))
                {
                    firstClick = true;
                    script.CameraLookatSync(0);
                    timer = script.getRuntime();
                }
            }
        }
    }
}
