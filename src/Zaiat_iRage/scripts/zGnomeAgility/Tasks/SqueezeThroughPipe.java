
package Zaiat_iRage.scripts.zGnomeAgility.Tasks;

import Zaiat_iRage.scripts.zGnomeAgility.zGnomeAgility;
import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zaiat on 4/28/2017.
 */
public class SqueezeThroughPipe extends Task<ClientContext>
{
    private boolean movedCamera;
    private int[] id_pipe = {23138, 23139};
    private int id_log_Balance = 23145;
    private final int[] bounds = {-32, 20, -120, -52, -48, 4};
    private zGnomeAgility script;
    private int floor = 0;
    private Area area_obstacle_pipe = new Area(new Tile(2481+1, 3432, floor), new Tile(2490+1, 3427, floor));
    private long timer;
    private Tile pipe_tile = new Tile(2484, 3430);

    public SqueezeThroughPipe(zGnomeAgility script, ClientContext ctx)
    {
        super(ctx);
        this.script = script;
    }

    @Override
    public boolean activate()
    {
        boolean activate = ctx.game.floor() == floor &&  area_obstacle_pipe.contains(ctx.players.local());
        if(activate && ctx.players.local().inMotion())
            timer = script.getRuntime();
        return activate;
    }

    @Override
    public void execute()
    {
        script.UpdateScript("Squeezing through Pipe");

        if(timer != 0)
            if(script.getRuntime() - timer >= 2000)
                timer = 0;
        if(timer == 0)
        {
            GameObject object = ctx.objects.select().id(id_pipe).nearest(pipe_tile).each(Interactive.doSetBounds(bounds)).poll();
            if (object.inViewport())
            {
                if (object.interact("Squeeze-through", "Obstacle pipe"))
                {
                    script.CameraLookatSync(90);
                    timer = script.getRuntime();
                }
            }
        }
    }
}
