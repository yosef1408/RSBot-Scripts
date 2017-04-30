
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
public class ClimbObstacleNet_1 extends Task<ClientContext>
{
    private boolean firstClick;
    private int id_obstacle_net = 23134;
    private final int[] bounds = {-112, 108, -244, 0, -12, 8};
    private zGnomeAgility script;
    private int floor = 0;
    private Area area_obstacle_net_1 = new Area(new Tile(2469, 3430+1, floor), new Tile(2480+1, 3414, floor));
    private long timer;

    public ClimbObstacleNet_1(zGnomeAgility script, ClientContext ctx)
    {
        super(ctx);
        this.script = script;
    }

    @Override
    public boolean activate()
    {
        boolean activate = ctx.game.floor() == floor &&  area_obstacle_net_1.contains(ctx.players.local());
        if(activate && ctx.players.local().inMotion())
            timer = script.getRuntime();
        if(!activate && firstClick)
            firstClick = false;
        return activate;
    }

    @Override
    public void execute()
    {
        script.UpdateScript("Climbing Obstacle net");

        if(timer != 0)
            if(script.getRuntime() - timer >= 2000)
                timer = 0;
        if(timer == 0 || !firstClick)
        {
            GameObject object = ctx.objects.select().id(id_obstacle_net).nearest().each(Interactive.doSetBounds(bounds)).poll();
            if (object.inViewport())
            {
                if (object.interact("Climb-over", "Obstacle net"))
                {
                    firstClick = true;
                   // script.CameraLookatSync(225);
                    timer = script.getRuntime();
                }
            }
        }
    }
}
