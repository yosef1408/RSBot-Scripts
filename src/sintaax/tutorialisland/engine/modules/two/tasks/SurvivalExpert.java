package sintaax.tutorialisland.engine.modules.two.tasks;

import sintaax.tutorialisland.engine.constants.NPCs;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class SurvivalExpert extends Task<ClientContext> {
    public SurvivalExpert(ClientContext ctx) {
        super(ctx);
    }

    private NPCs npcs = new NPCs(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 20 || varps.get(281) == 70;
    }

    @Override
    public void execute() {
        npcs.talk(npcs.SURVIVAL_EXPERT);
    }
}
