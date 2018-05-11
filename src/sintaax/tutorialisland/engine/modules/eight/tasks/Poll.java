package sintaax.tutorialisland.engine.modules.eight.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.GameObjects;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class Poll extends Task<ClientContext> {
    public Poll(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private GameObjects gameObjects = new GameObjects(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 520;
    }

    @Override
    public void execute() {
        if (varps.get(375) == 8)
            components.CONTINUE_BUTTON.click();
        else {
            if (!components.BANK_WINDOW.visible()) {
                gameObjects.use(gameObjects.POLL);
            } else
                components.BANK_CLOSE_BUTTON.click();
        }
    }
}
