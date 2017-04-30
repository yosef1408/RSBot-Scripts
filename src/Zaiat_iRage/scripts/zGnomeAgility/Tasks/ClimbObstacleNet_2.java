
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
public class ClimbObstacleNet_2 extends Task<ClientContext>
{
    private boolean movedCamera;
    private int id_obstacle_net = 23135;
    private final int[] bounds = {-112, 108, -244, 0, -12, 8};
    private zGnomeAgility script;
    private int floor = 0;
    private Area area_obstacle_net_2 = new Area(new Tile(2481, 3426+1, floor), new Tile(2490+1, 3420, floor));
    private long timer;

    public ClimbObstacleNet_2(zGnomeAgility script, ClientContext ctx)
    {
        super(ctx);
        this.script = script;
    }

    @Override
    public boolean activate()
    {
        boolean activate = ctx.game.floor() == floor &&  area_obstacle_net_2.contains(ctx.players.local());
        if(activate && ctx.players.local().inMotion())
            timer = script.getRuntime();
        return activate;
    }

    @Override
    public void execute()
    {
        script.UpdateScript("Climbing Obstacle net");

        if(timer != 0)
            if(script.getRuntime() - timer >= 2000)
                timer = 0;
        if(timer == 0)
        {
            GameObject object = ctx.objects.select().id(id_obstacle_net).nearest().each(Interactive.doSetBounds(bounds)).poll();
            if (object.inViewport())
            {
                if (object.interact("Climb-over", "Obstacle net"))
                {
                    script.CameraLookatSync(45);
                    timer = script.getRuntime();
                }
            }
        }
    }
}
