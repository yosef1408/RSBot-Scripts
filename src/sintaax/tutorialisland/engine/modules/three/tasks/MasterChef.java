package sintaax.tutorialisland.engine.modules.three.tasks;

import sintaax.tutorialisland.engine.constants.NPCs;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class MasterChef extends Task<ClientContext> {
    public MasterChef(ClientContext ctx) {
        super(ctx);
    }

    private NPCs npcs = new NPCs(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 140;
    }

    @Override
    public void execute() {
        npcs.talk(npcs.MASTER_CHEF);
    }
}
