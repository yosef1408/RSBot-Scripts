package sintaax.tutorialisland.engine.modules.three.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class MusicPlayerMenu extends Task<ClientContext> {
    public MusicPlayerMenu(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 170 && varps.get(1021) == 14;
    }

    @Override
    public void execute() {
        components.MUSICPLAYER_BUTTON.click();
    }
}
