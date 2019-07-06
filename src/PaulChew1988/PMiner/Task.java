package PMiner;

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

public abstract class Task extends ClientAccessor {
    boolean chickens = false;
    boolean clay = false;
    public Task(ClientContext ctx) {
        super(ctx);
    }
    public abstract boolean activate();
    public abstract void execute();
}
