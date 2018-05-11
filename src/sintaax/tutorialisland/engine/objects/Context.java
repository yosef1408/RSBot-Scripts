package sintaax.tutorialisland.engine.objects;

import org.powerbot.script.ClientAccessor;
import org.powerbot.script.ClientContext;

public abstract class Context<C extends ClientContext> extends ClientAccessor<C> {
    public Context(C ctx) {
        super(ctx);
    }
}
