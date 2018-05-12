package sintaax.tutorialisland.engine.modules.seven.tasks;

import sintaax.tutorialisland.engine.constants.GameObjects;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;

public class Ladder2 extends Task<ClientContext> {
    public Ladder2(ClientContext ctx) {
        super(ctx);
    }

    private GameObjects gameObjects = new GameObjects(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 500;
    }

    @Override
    public void execute() {
        gameObjects.climbUp(gameObjects.LADDER2);
    }
}
