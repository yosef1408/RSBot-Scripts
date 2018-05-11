package sintaax.tutorialisland.engine.modules.nine.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class FriendsMenu extends Task<ClientContext> {
    public FriendsMenu(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 580 && varps.get(1021) == 9;
    }

    @Override
    public void execute() {
        components.FRIENDS_BUTTON.click();
    }
}
