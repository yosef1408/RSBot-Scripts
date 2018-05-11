package sintaax.tutorialisland.engine.modules.one;

import sintaax.tutorialisland.engine.Manager;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.modules.one.tasks.*;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

import java.util.Arrays;

public class OneController extends Task<ClientContext> {
    public OneController(ClientContext ctx) {
        super(ctx);
    }

    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return Manager.inactive(ctx) && (varps.get(281) >= -1 && varps.get(281) < 20);
    }

    @Override
    public void execute() {
        Manager.updateTasks(Arrays.asList(new RSGuide(ctx), new OptionsMenu1(ctx), new Door1(ctx)));
    }
}
