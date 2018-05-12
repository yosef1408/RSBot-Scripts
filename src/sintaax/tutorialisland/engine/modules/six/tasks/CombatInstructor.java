package sintaax.tutorialisland.engine.modules.six.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.NPCs;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class CombatInstructor extends Task<ClientContext> {
    public CombatInstructor(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private NPCs npcs = new NPCs(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 370 || varps.get(281) == 410;
    }

    @Override
    public void execute() {
        if (components.EQUIPMENTSTATS_WINDOW.visible())
            components.EQUIPMENTSTATS_CLOSE_BUTTON.click();
        else
            npcs.talk(npcs.COMBAT_INSTRUCTOR);
    }
}
