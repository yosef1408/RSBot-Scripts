package sintaax.tutorialisland.engine.modules.nine.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class PrayerMenu extends Task<ClientContext> {
    public PrayerMenu(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 560 && varps.get(1021) == 6;
    }

    @Override
    public void execute() {
        components.PRAYER_BUTTON.click();
    }
}
