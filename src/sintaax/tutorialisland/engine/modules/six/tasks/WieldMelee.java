package sintaax.tutorialisland.engine.modules.six.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Items;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class WieldMelee extends Task<ClientContext> {
    public WieldMelee(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Items items = new Items(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 420;
    }

    @Override
    public void execute() {
        if (components.BACKPACK_WINDOW.visible()) {
            items.wield(items.BRONZE_SWORD);
            items.wield(items.WOODEN_SHIELD);
        }
        else
            components.BACKPACK_BUTTON.click();
    }
}
