package sintaax.tutorialisland.engine.modules.ten.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.NPCs;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class MagicInstructor extends Task<ClientContext> {
    public MagicInstructor(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private NPCs npcs = new NPCs(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 620 || varps.get(281) == 670;
    }

    @Override
    public void execute() {
        if (components.CONTINUE_BUTTON.visible())
            components.CONTINUE_BUTTON.click();
        else {
            if (npcs.MAGIC_INSTRUCTOR.valid())
                npcs.talk(npcs.MAGIC_INSTRUCTOR);
            else
                ctx.movement.step(new Tile(3139, 3087, 0));
        }
    }
}
