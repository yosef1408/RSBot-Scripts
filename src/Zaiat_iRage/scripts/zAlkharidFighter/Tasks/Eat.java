package Zaiat_iRage.scripts.zAlkharidFighter.Tasks;

import org.powerbot.script.rt4.ClientContext;
import Zaiat_iRage.scripts.zAlkharidFighter.zAlkharidFighter;

/**
 * Created by Zaiat on 4/20/2017.
 */
public class Eat extends Task<ClientContext>
{
    private zAlkharidFighter script;
    private long eat_timer_1, eat_timer_2;

    public Eat(zAlkharidFighter script, ClientContext ctx)
    {
        super(ctx);
        this.script = script;
    }

    @Override
    public boolean activate()
    {
        return ctx.players.local().healthPercent() <= 30 && !ctx.inventory.select().name(zAlkharidFighter.food).isEmpty();
    }

    @Override
    public void execute()
    {
        if(eat_timer_1 != 0)
        {
            eat_timer_2 = script.getRuntime();
            if(eat_timer_2 - eat_timer_1 >= 3000)
                eat_timer_1 = 0;
        }
        if(eat_timer_1 == 0)
        {
            zAlkharidFighter.status = "Low HP, eating " + zAlkharidFighter.food;
            ctx.inventory.select().name((zAlkharidFighter.food)).poll().interact("Eat");
            eat_timer_1 = script.getRuntime();
        }
    }
}
