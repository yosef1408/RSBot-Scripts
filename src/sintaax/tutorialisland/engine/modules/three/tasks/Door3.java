package sintaax.tutorialisland.engine.modules.three.tasks;

import sintaax.tutorialisland.engine.constants.GameObjects;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class Door3 extends Task<ClientContext> {
    public Door3(ClientContext ctx) {
        super(ctx);
    }

    private GameObjects gameObjects = new GameObjects(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 180;
    }

    @Override
    public void execute() {
        gameObjects.open(gameObjects.DOOR3);
    }
}
