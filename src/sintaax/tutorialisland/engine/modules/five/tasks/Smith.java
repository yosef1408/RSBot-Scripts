package sintaax.tutorialisland.engine.modules.five.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.GameObjects;
import sintaax.tutorialisland.engine.constants.Items;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class Smith extends Task<ClientContext> {
    public Smith(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private GameObjects gameObjects = new GameObjects(ctx);
    private Items items = new Items(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 340 || varps.get(281) == 350;
    }

    @Override
    public void execute() {
        if (components.BACKPACK_WINDOW.visible()) {
            if (!components.SMITHING_WINDOW.visible()) {
                items.use(items.BRONZE_BAR);
                gameObjects.use(gameObjects.ANVIL);
            } else
                components.SMITHING_DAGGER_BUTTON.click();
        } else
            components.BACKPACK_BUTTON.click();
    }
}
