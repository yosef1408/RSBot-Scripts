package supaplex.tasks;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;

/**
 * Created by Andreas on 20.06.2016.
 */
public abstract class Task<C extends ClientContext> extends ClientAccessor<C> {
    public Task(C ctx) {
        super(ctx);
    }
    public abstract boolean activate();
    public abstract void execute();
}