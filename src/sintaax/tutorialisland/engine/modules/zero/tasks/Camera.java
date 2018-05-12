package sintaax.tutorialisland.engine.modules.zero.tasks;

import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

public class Camera extends Task<ClientContext> {
    public Camera(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return Random.nextInt(1, 100) > 50;
    }

    @Override
    public void execute() {
        ctx.camera.pitch(Random.nextInt(40, 50));
    }
}
