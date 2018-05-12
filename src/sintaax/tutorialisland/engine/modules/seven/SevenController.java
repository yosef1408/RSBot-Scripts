package sintaax.tutorialisland.engine.modules.seven;

import sintaax.tutorialisland.engine.Manager;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.modules.seven.tasks.*;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

import java.util.Arrays;

public class SevenController extends Task<ClientContext> {
    public SevenController(ClientContext ctx) {
        super(ctx);
    }

    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return Manager.inactive(ctx) && (varps.get(281) >= 440 && varps.get(281) < 510);
    }

    @Override
    public void execute() {
        Manager.updateTasks(Arrays.asList(new Gate3(ctx), new RatMelee(ctx), new RatRange(ctx), new Ladder2(ctx)));
    }
}
