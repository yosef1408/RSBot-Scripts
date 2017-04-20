package scripts.zAlkharidFighter.Tasks;


import org.powerbot.script.rt4.ClientContext;

import java.util.Random;

/**
 * Created by Zaiat on 4/20/2017.
 */
public class ToggleRun extends Task<ClientContext>
{
    private int run_energy_level = 0;
    public ToggleRun(ClientContext ctx)
    {
        super(ctx);
        run_energy_level = 5 + new Random().nextInt(11);
    }

    @Override
    public boolean activate()
    {
        return ctx.movement.energyLevel() >= run_energy_level && !ctx.movement.running();
    }

    @Override
    public void execute()
    {
        ctx.movement.running(true);
        run_energy_level = 5 + new Random().nextInt(11);
    }
}
