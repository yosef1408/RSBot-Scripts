package sintaax.tutorialisland.engine.modules.four.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class QuestListMenu extends Task<ClientContext> {
    public QuestListMenu(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 230 && varps.get(1021) == 3;
    }

    @Override
    public void execute() {
        components.QUESTLIST_BUTTON.click();
    }
}
