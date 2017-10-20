package thebonobo.SalmonCollector.tasks;

import org.powerbot.script.rt4.ClientContext;
import thebonobo.SalmonCollector.utils.Info;
import thebonobo.SalmonCollector.utils.Paths;
import thebonobo.SalmonCollector.utils.Walker;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

public class WalkToBank extends Task<ClientContext> {
    private final int bankBoothID = 6943;
    private Walker walker;

    public WalkToBank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().count() == 28 && !ctx.objects.select(3).id(bankBoothID).nearest().poll().inViewport();
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Walking to bank");
        walker = new Walker(ctx, ctx.movement.newTilePath(Paths.FISHING_TO_BANK));
        walker.Walk();
    }
}
