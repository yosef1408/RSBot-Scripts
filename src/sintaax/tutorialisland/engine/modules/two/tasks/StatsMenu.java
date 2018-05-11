package sintaax.tutorialisland.engine.modules.two.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class StatsMenu extends Task<ClientContext> {
    public StatsMenu(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 60 && varps.get(1021) == 2;
    }

    @Override
    public void execute() {
        components.STATS_BUTTON.click();
    }
}
