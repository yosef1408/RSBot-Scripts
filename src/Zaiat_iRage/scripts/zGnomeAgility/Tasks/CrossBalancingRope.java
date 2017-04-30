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
public class CrossBalancingRope extends Task<ClientContext>
{
    private boolean movedCamera;
    private int id_balancing_rope = 23557;
    private final int[] bounds = {4, 60, -4, 22, 49, 85};
    private zGnomeAgility script;
    private int floor = 2;
    private Area area_balancing_rope = new Area(new Tile(2472, 3422, floor), new Tile(2478, 3418, floor));
    private long timer;

    public CrossBalancingRope(zGnomeAgility script, ClientContext ctx)
    {
        super(ctx);
        this.script = script;
    }

    @Override
    public boolean activate()
    {
        boolean activate = ctx.game.floor() == floor &&  area_balancing_rope.contains(ctx.players.local());
        if(activate && ctx.players.local().inMotion())
            timer = script.getRuntime();
        return activate;
    }

    @Override
    public void execute()
    {
        script.UpdateScript("Crossing Balancing rope");

        if(timer != 0)
            if(script.getRuntime() - timer >= 2000)
                timer = 0;
        if(timer == 0)
        {
            GameObject object = ctx.objects.select().id(id_balancing_rope).nearest().each(Interactive.doSetBounds(bounds)).poll();
            if (object.inViewport())
            {
                if (object.interact("Walk-on", "Balancing rope"))
                {
                    script.CameraLookatSync(315);
                    timer = script.getRuntime();
                }
            }
        }
    }
}
