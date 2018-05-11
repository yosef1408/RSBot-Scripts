package sintaax.tutorialisland.engine.modules.four.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class OptionsMenu2 extends Task<ClientContext> {
    public OptionsMenu2(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 190
                && varps.get(1021) == 12;
    }

    @Override
    public void execute() {
        components.OPTIONS_BUTTON.click();
    }
}
