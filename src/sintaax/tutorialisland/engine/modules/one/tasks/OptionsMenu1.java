package sintaax.tutorialisland.engine.modules.one.tasks;

import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;
import sintaax.tutorialisland.engine.constants.Components;

import org.powerbot.script.rt4.ClientContext;

public class OptionsMenu1 extends Task<ClientContext> {
    public OptionsMenu1(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 3 && varps.get(1021) == 12;
    }

    @Override
    public void execute() {
        components.OPTIONS_BUTTON.click();
    }
}
