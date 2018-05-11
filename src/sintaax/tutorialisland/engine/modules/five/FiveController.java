package sintaax.tutorialisland.engine.modules.five;

import sintaax.tutorialisland.engine.Manager;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.modules.five.tasks.*;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

import java.util.Arrays;

public class FiveController extends Task<ClientContext> {
    public FiveController(ClientContext ctx) {
        super(ctx);
    }

    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return Manager.inactive(ctx) && (varps.get(281) >= 260 && varps.get(281) < 370);
    }

    @Override
    public void execute() {
        Manager.updateTasks(Arrays.asList(new MiningInstructor(ctx), new Prospect(ctx), new Mine(ctx), new Smelt(ctx),
                new Smith(ctx), new Gate2(ctx)));
    }
}
