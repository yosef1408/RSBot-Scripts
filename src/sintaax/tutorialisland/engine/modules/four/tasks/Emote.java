package sintaax.tutorialisland.engine.modules.four.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class Emote extends Task<ClientContext> {
    public Emote(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 187;
    }

    @Override
    public void execute() {
        if (components.EMOTE_WINDOW.visible())
            components.EMOTE_RANDOM.click();
        else
            components.EMOTE_BUTTON.click();
    }
}
