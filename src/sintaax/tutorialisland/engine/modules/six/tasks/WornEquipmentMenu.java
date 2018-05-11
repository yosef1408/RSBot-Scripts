package sintaax.tutorialisland.engine.modules.six.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class WornEquipmentMenu extends Task<ClientContext> {
    public WornEquipmentMenu(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 390 && varps.get(1021) == 5;
    }

    @Override
    public void execute() {
        components.WORNEQUIPMENT_BUTTON.click();
    }
}
