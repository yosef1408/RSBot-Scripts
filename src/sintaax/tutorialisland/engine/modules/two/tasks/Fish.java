package sintaax.tutorialisland.engine.modules.two.tasks;

import sintaax.tutorialisland.engine.constants.NPCs;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class Fish extends Task<ClientContext> {
    public Fish(ClientContext ctx) {
        super(ctx);
    }

    private NPCs npcs = new NPCs(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 80;
    }

    @Override
    public void execute() {
        npcs.net(npcs.FISHING_SPOT);
    }
}
