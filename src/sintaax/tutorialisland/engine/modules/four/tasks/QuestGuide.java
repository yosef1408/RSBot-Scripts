package sintaax.tutorialisland.engine.modules.four.tasks;

import sintaax.tutorialisland.engine.constants.NPCs;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class QuestGuide extends Task<ClientContext> {
    public QuestGuide(ClientContext ctx) {
        super(ctx);
    }

    private NPCs npcs = new NPCs(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 220 || varps.get(281) == 240;
    }

    @Override
    public void execute() {
        npcs.talk(npcs.QUEST_GUIDE);
    }
}
