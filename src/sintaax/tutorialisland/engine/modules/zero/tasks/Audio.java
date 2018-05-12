package sintaax.tutorialisland.engine.modules.zero.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class Audio extends Task<ClientContext> {
    public Audio(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.OPTIONS_TOGGLED && varps.AUDIO_ON;
    }

    @Override
    public void execute() {
        components.openWindow(components.OPTIONS_WINDOW, components.OPTIONS_BUTTON);
        components.openWindow(components.AUDIO_WINDOW, components.AUDIO_BUTTON);
        components.AUDIO_OFF_BUTTON.click();
    }
}
