package sintaax.tutorialisland.engine.modules.eight.tasks;

import sintaax.tutorialisland.engine.constants.NPCs;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class FinancialAdvisor extends Task<ClientContext> {
    public FinancialAdvisor(ClientContext ctx) {
        super(ctx);
    }

    private NPCs npcs = new NPCs(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 530;
    }

    @Override
    public void execute() {
        npcs.talk(npcs.FINANCIAL_ADVISOR);
    }
}
