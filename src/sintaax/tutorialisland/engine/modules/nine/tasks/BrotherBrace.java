package sintaax.tutorialisland.engine.modules.nine.tasks;

import sintaax.tutorialisland.engine.constants.GameObjects;
import sintaax.tutorialisland.engine.constants.NPCs;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.rt4.ClientContext;
import sun.plugin2.message.GetAppletMessage;

public class BrotherBrace extends Task<ClientContext> {
    public BrotherBrace(ClientContext ctx) {
        super(ctx);
    }

    private NPCs npcs = new NPCs(ctx);
    private GameObjects gameObjects = new GameObjects(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 550 || varps.get(281) == 570 || varps.get(281) == 600;
    }

    @Override
    public void execute() {
        if (gameObjects.LARGE_DOOR_CLOSED.valid())
            gameObjects.open(gameObjects.LARGE_DOOR_CLOSED);
        else {
            if (npcs.BROTHER_BRACE.inViewport() || gameObjects.LARGE_DOOR_OPEN.inViewport()) {
                ctx.camera.turnTo(npcs.BROTHER_BRACE);
                ctx.movement.step(npcs.BROTHER_BRACE);
                npcs.BROTHER_BRACE.interact("Talk-to");
            } else {
                ctx.camera.turnTo(npcs.BROTHER_BRACE);
                ctx.movement.step(gameObjects.LARGE_DOOR_OPEN);
            }
        }
    }
}
