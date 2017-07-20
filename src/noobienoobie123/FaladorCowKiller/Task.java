package noobienoobie123.FaladorCowKiller;

/**
 * Created by larry on 7/17/2017.
 */
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