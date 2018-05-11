package sintaax.tutorialisland.engine.modules.two.tasks;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.constants.GameObjects;
import sintaax.tutorialisland.engine.constants.Items;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class Fire extends Task<ClientContext> {
    public Fire(ClientContext ctx) {
        super(ctx);
    }

    private Components components = new Components(ctx);
    private GameObjects gameObjects = new GameObjects(ctx);
    private Items items = new Items(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 50;
    }

    @Override
    public void execute() {
        if (components.BACKPACK_WINDOW.visible()) {
            if (items.existsInInventory((items.LOG))) {
                items.makeFire();
            } else {
                gameObjects.chop(gameObjects.TREE);
            }
        } else
            components.BACKPACK_BUTTON.click();
    }
}