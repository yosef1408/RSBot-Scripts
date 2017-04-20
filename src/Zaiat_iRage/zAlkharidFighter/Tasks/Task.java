package scripts.zAlkharidFighter.Tasks;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created by Zaiat on 4/20/2017.
 */

public abstract class Task<C extends ClientContext> extends ClientAccessor<C>
{
    public Task(C ctx) {
        super(ctx);
    }

    public abstract boolean activate();
    public abstract void execute();
}
