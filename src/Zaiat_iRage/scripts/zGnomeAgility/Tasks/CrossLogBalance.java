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
public class CrossLogBalance extends Task<ClientContext>
{
    private boolean firstClick;
    private int id_log_Balance = 23145;
    private final int[] bounds = {40, 80, 12, 48, 20, 100};

    private zGnomeAgility script;
    private int floor = 0;
    private Area area_log_balance = new Area(new Tile(2469, 3440+1,floor), new Tile(2490+1, 3436, floor));
    private long timer;

    public CrossLogBalance(zGnomeAgility script, ClientContext ctx)
    {
        super(ctx);
        this.script = script;
    }

    @Override
    public boolean activate()
    {
        boolean activate = ctx.game.floor() == floor &&  area_log_balance.contains(ctx.players.local()) && ctx.players.local().animation() == -1;
        if(activate && ctx.players.local().inMotion())
            timer = script.getRuntime();
        else if(!activate && firstClick)
            firstClick = false;
        return activate;
    }

    @Override
    public void execute()
    {
        script.UpdateScript("Crossing Log balance");

        if(timer != 0)
            if(script.getRuntime() - timer >= 2000)
                timer = 0;
        if(timer == 0 || !firstClick)
        {
            GameObject object = ctx.objects.select().id(id_log_Balance).nearest().each(Interactive.doSetBounds(bounds)).poll();
            if (object.inViewport())
            {
                if (object.interact("Walk-across", "Log balance"))
                {
                    firstClick = true;
                    script.CameraLookatSync(180);
                    timer = script.getRuntime();
                }
            }
        }
    }
}
