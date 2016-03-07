package fighter;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.*;

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