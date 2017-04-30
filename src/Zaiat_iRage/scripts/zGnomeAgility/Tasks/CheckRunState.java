package Zaiat_iRage.scripts.zGnomeAgility.Tasks;

import org.powerbot.script.rt4.ClientContext;

import java.util.Random;

/**
 * Created by Zaiat on 4/28/2017.
 */
public class CheckRunState extends Task<ClientContext>
{
    private int RUN_ENERGY_LEVEL = 0;
    public CheckRunState(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        return ctx.movement.energyLevel() >= RUN_ENERGY_LEVEL && !ctx.movement.running();
    }

    @Override
    public void execute()
    {
        ctx.movement.running(true);
        RUN_ENERGY_LEVEL = 5 + new Random().nextInt(11);
    }

}
