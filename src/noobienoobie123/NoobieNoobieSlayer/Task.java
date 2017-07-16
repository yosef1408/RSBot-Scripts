package NoobieNoobieSlayer;

import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created by larry on 7/12/2017.
 */
public abstract class Task extends ClientAccessor{

    public Task(ClientContext ctx) {
        super(ctx);
    }

    public abstract boolean activate();
    public abstract void execute();

}