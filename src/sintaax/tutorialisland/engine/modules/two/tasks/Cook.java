package sintaax.tutorialisland.engine.modules.two.tasks;

import sintaax.tutorialisland.engine.constants.*;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class Cook extends Task<ClientContext> {
    public Cook(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private GameObjects gameObjects = new GameObjects(ctx);
    private Items items = new Items(ctx);
    private NPCs npcs = new NPCs(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 90 || varps.get(281) == 100 || varps.get(281) == 110;
    }

    @Override
    public void execute() {
        if (!items.existsInInventory(items.SHRIMP)) {
            if (components.BACKPACK_WINDOW.visible()) {
                if (items.existsInInventory(items.RAW_SHRIMP)) {
                    if (gameObjects.FIRE.valid()) {
                        items.use(items.RAW_SHRIMP);
                        gameObjects.use(gameObjects.FIRE);
                    } else {
                        if (items.existsInInventory(items.LOG))
                            items.makeFire();
                        else
                            gameObjects.chop(gameObjects.TREE);
                    }
                } else
                    npcs.net(npcs.FISHING_SPOT);
            } else
                components.BACKPACK_BUTTON.click();
        }
    }
}
