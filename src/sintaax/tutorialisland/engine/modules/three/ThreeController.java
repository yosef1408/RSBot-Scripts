package sintaax.tutorialisland.engine.modules.three;

import sintaax.tutorialisland.engine.Manager;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.modules.three.tasks.*;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

import java.util.Arrays;

public class ThreeController extends Task<ClientContext> {
    public ThreeController(ClientContext ctx) {
        super(ctx);
    }

    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return Manager.inactive(ctx) && (varps.get(281) >= 130 && varps.get(281) < 183);
    }

    @Override
    public void execute() {
        Manager.updateTasks(Arrays.asList(new Door2(ctx), new MasterChef(ctx), new Dough(ctx), new Bake(ctx),
                new MusicPlayerMenu(ctx), new Door3(ctx)));
    }
}
