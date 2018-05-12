package sintaax.tutorialisland.engine.modules.zero;

import sintaax.tutorialisland.engine.Manager;
import sintaax.tutorialisland.engine.modules.zero.tasks.*;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

import java.util.Arrays;

public class ZeroController extends Task<ClientContext> {
    public ZeroController(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return new Audio(ctx).activate() || new Camera(ctx).activate() || new Chatter(ctx).activate()
                || new Run(ctx).activate() || new Designer(ctx).activate();
    }

    @Override
    public void execute() {
        Manager.updateTasks(Arrays.asList(new Audio(ctx), new Camera(ctx), new Chatter(ctx), new Run(ctx),
                new Designer(ctx)));
    }
}
