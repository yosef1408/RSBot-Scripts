package swamlol.woodcutting.tasks;


import org.powerbot.script.Area;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;
import org.powerbot.script.rt6.TilePath;
import swamlol.woodcutting.Task;

public class Walk extends Task {

    private TilePath pathToBank, pathToArea;
    public Tile MAINPATH[] = {};

    public Walk(ClientContext ctx, Tile[] PATH){
        super(ctx);

        this.MAINPATH = PATH;
    }


    private final Area normalLogsEastVarrock = new Area(new Tile(3130, 3444, 0), new Tile(3148, 3418, 0));
    private final Area oakTreesDranyor = new Area(new Tile(3114, 3258, 0), new Tile(3132, 3248, 0));
    private final Area willowTreesDranyor = new Area(new Tile(3081, 3238, 0), new Tile(3090, 3227, 0));
    private Player localplayer = ctx.players.local();

    @Override
    public boolean activate() {
        pathToBank = new TilePath(ctx, MAINPATH);
        pathToArea = new TilePath(ctx, MAINPATH).reverse();
        return ctx.backpack.select().count()>27 || (ctx.backpack.select().count()<28 && !normalLogsEastVarrock.contains(localplayer) && !oakTreesDranyor.contains(localplayer) && !willowTreesDranyor.contains(localplayer));
    }

    @Override
    public void execute() {
        if(!ctx.movement.running() && ctx.movement.energyLevel()> Random.nextInt(35,50)){
            System.out.println("trying to turn on energy");
            ctx.movement.running(true);
        }
        if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 8) {
            if (ctx.backpack.select().count() > 27) {
                pathToBank.traverse();
            } else {
                pathToArea.traverse();

            }
        }

    }
}