package sintaax.tutorialisland.engine.modules.six;

import sintaax.tutorialisland.engine.Manager;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.modules.six.tasks.*;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

import java.util.Arrays;

public class SixController extends Task<ClientContext> {
    public SixController(ClientContext ctx) {
        super(ctx);
    }

    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return Manager.inactive(ctx) && (varps.get(281) >= 370 && varps.get(281) < 440);
    }

    @Override
    public void execute() {
        Manager.updateTasks(Arrays.asList(new CombatInstructor(ctx), new WornEquipmentMenu(ctx),
                new ViewEquipmentStats(ctx), new EquipDagger(ctx), new WieldMelee(ctx), new CombatOptionsMenu(ctx)));
    }
}
