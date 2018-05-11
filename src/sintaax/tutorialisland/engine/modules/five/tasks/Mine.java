package sintaax.tutorialisland.engine.modules.five.tasks;

import sintaax.tutorialisland.engine.constants.GameObjects;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class Mine extends Task<ClientContext> {
    public Mine(ClientContext ctx) {
        super(ctx);
    }

    private GameObjects gameObjects = new GameObjects(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 300 || varps.get(281) == 310 || varps.get(281) == 311;
    }

    @Override
    public void execute() {
        if (varps.get(281) == 300 || varps.get(281) == 311)
            gameObjects.mine(gameObjects.TIN_VEIN);
        else
            gameObjects.mine(gameObjects.COPPER_VEIN);
    }
}
