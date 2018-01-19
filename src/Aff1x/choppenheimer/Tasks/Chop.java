package Aff1x.choppenheimer.Tasks;

import Aff1x.choppenheimer.Choppenheimer;
import Aff1x.choppenheimer.Util.TreeEnum;
import org.powerbot.script.rt6.ClientContext;
import Aff1x.choppenheimer.Task;
import org.powerbot.script.rt6.GameObject;

public class Chop extends Task<ClientContext>{

    public Chop(ClientContext ctx)
    {
        super (ctx);
    }

    @Override
    public boolean activate()
    {
        return ctx.backpack.select().count() < 28
                && !ctx.objects.select().name(Choppenheimer.config.getTreeType().name[0]).isEmpty()
                && ctx.players.local().animation() == -1;
    }

    @Override
    public void execute()
    {
        GameObject tree = ctx.objects.select().name(Choppenheimer.config.getTreeType().name[0]).nearest().poll();
        if(tree.inViewport() && ctx.players.local().animation() == -1 && !ctx.players.local().inMotion()) {
            tree.interact("Chop");
        } else {
            ctx.camera.turnTo(tree);
            if(!ctx.players.local().inMotion())
                ctx.movement.step(tree);
        }
    }
}
