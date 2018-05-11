package sintaax.tutorialisland.engine.modules.six.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class ViewEquipmentStats extends Task<ClientContext> {
    public ViewEquipmentStats(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 400;
    }

    @Override
    public void execute() {
        if (components.WORNEQUIPMENT_WINDOW.visible())
            components.EQUIPMENTSTATS_BUTTON.click();
        else
            components.WORNEQUIPMENT_BUTTON.click();
    }
}
