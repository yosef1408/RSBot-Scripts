package sintaax.tutorialisland.engine.modules.three.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Items;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class Dough extends Task<ClientContext> {
    public Dough(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Items items = new Items(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 150;
    }

    @Override
    public void execute() {
        if (components.BACKPACK_WINDOW.visible())
            items.makeDough();
        else
            components.BACKPACK_BUTTON.click();
    }
}
