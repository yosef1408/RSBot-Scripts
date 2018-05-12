package sintaax.tutorialisland.engine.modules;

import sintaax.tutorialisland.engine.Manager;
import sintaax.tutorialisland.engine.modules.eight.EightController;
import sintaax.tutorialisland.engine.modules.five.FiveController;
import sintaax.tutorialisland.engine.modules.four.FourController;
import sintaax.tutorialisland.engine.modules.nine.NineController;
import sintaax.tutorialisland.engine.modules.one.OneController;
import sintaax.tutorialisland.engine.modules.seven.SevenController;
import sintaax.tutorialisland.engine.modules.six.SixController;
import sintaax.tutorialisland.engine.modules.ten.TenController;
import sintaax.tutorialisland.engine.modules.three.ThreeController;
import sintaax.tutorialisland.engine.modules.two.TwoController;
import sintaax.tutorialisland.engine.modules.zero.ZeroController;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

import java.util.Arrays;

public class Controller extends Task<ClientContext> {
    public Controller(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return new ZeroController(ctx).activate() || new OneController(ctx).activate()
                || new TwoController(ctx).activate() || new ThreeController(ctx).activate()
                || new FourController(ctx).activate() || new FiveController(ctx).activate()
                || new SixController(ctx).activate() || new SevenController(ctx).activate()
                || new EightController(ctx).activate() || new NineController(ctx).activate()
                || new TenController(ctx).activate();
    }

    @Override
    public void execute() {
        Manager.updateTasks(Arrays.asList(new ZeroController(ctx), new OneController(ctx),
                new TwoController(ctx), new ThreeController(ctx), new FourController(ctx),
                new FiveController(ctx), new SixController(ctx), new SevenController(ctx),
                new EightController(ctx), new NineController(ctx), new TenController(ctx)));
    }
}
