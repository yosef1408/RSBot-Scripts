package sintaax.tutorialisland.engine.modules.one.tasks;

import sintaax.tutorialisland.engine.objects.Task;
import sintaax.tutorialisland.engine.constants.NPCs;
import sintaax.tutorialisland.engine.constants.Varps;

import org.powerbot.script.rt4.ClientContext;

public class RSGuide extends Task<ClientContext> {
    public RSGuide(ClientContext ctx) {
        super(ctx);
    }

    private NPCs npcs = new NPCs(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 0 || varps.get(281) == 7;
    }

    @Override
    public void execute() {
        npcs.talk(npcs.RUNESCAPE_GUIDE);
    }
}
