package PMiner.Tasks.SubTasks;

import PMiner.PMinerConst;
import PMiner.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.TilePath;

import java.util.concurrent.Callable;

public class GeToBank extends Task {

    public Tile[] path = {};
    public GeToBank(ClientContext ctx, Tile[] path) {
        super(ctx);
        this.path = path;
    }

    @Override
    public boolean activate() {
        return false;
    }

    @Override
    public void execute() {

        TilePath pathBack = new TilePath(ctx, path);
        pathBack.reverse();


            System.out.println("walking");
            while (path[0].distanceTo(ctx.players.local()) >= 10) {
                pathBack.traverse();
            }
        }

}
