package scripts.zAlkharidFighter.Tasks;

import org.powerbot.script.rt4.ClientContext;
import scripts.zAlkharidFighter.zAlkharidFighter;

/**
 * Created by Zaiat on 4/20/2017.
 */
public class Eat extends Task<ClientContext>
{
    public Eat(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        return ctx.players.local().healthPercent() <= 25 && !ctx.inventory.select().name(zAlkharidFighter.food).isEmpty();
    }

    @Override
    public void execute()
    {
        zAlkharidFighter.status = "Low HP, eating food";
        ctx.inventory.select().name((zAlkharidFighter.food)).poll().interact("Eat");
    }
}
