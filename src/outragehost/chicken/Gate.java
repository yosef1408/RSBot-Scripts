package outragehost.chicken;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public class Gate extends Task<ClientContext> {

    public Gate(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        GameObject closedGate = ctx.objects.select().id(45208).nearest().limit(1).poll();
        return closedGate.valid();
    }

    @Override
    public void execute() {
        Fighter.displayStatus = ("Opening Gate");
        GameObject closedGate = ctx.objects.select().id(45208).nearest().limit(1).poll();
        closedGate.interact("Open");
    }
}