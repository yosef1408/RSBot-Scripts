package noobienoobie123.FaladorCowKiller.tasks;

import noobienoobie123.FaladorCowKiller.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.TilePath;

/**
 * Created by larry on 7/27/2017.
 */
public class WalkToTanner extends Task {

    public static final Tile[] pathToDoor = {new Tile(3269, 3167, 0), new Tile(3271, 3167, 0), new Tile(3273, 3167, 0), new Tile(3273, 3169, 0), new Tile(3275, 3171, 0), new Tile(3275, 3173, 0), new Tile(3275, 3175, 0), new Tile(3276, 3177, 0), new Tile(3277, 3179, 0), new Tile(3278, 3181, 0), new Tile(3280, 3183, 0), new Tile(3281, 3185, 0), new Tile(3281, 3187, 0), new Tile(3281, 3189, 0), new Tile(3280, 3191, 0), new Tile(3278, 3191, 0)};
    public static final Tile[] pathInHouse = {new Tile(3278, 3191, 0), new Tile(3276, 3191, 0), new Tile(3274, 3191, 0)};
    TilePath toDoor = ctx.movement.newTilePath(pathToDoor);
    TilePath inHouse = ctx.movement.newTilePath(pathInHouse);
    TilePath backDoor = ctx.movement.newTilePath(pathToDoor).reverse();
    Area tanHouse = new Area(new Tile(3278,3194,0),new Tile(3270,3190,0));
    Area AlBank = new Area(new Tile(3272,3171,0),new Tile(3269,3161,0));
    final static int coins =  995;
    final static int cowHides = 1739;
    final static int softLeather =  1741;
    final static int hardLeather =  1743;
    final static int door = 1535;

    public WalkToTanner(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ((ctx.inventory.select().id(cowHides).count()>0 && !tanHouse.contains(ctx.players.local())) || ctx.inventory.select().id(hardLeather).count() > 0 && tanHouse.contains(ctx.players.local()) );
    }

    @Override
    public void execute() {

        if(!tanHouse.contains(ctx.players.local())){
            toDoor.traverse();
            final GameObject closedDoor = ctx.objects.select().id(door).nearest().poll();
            if(closedDoor.valid()){
                closedDoor.interact("Open", "Door");
                Condition.sleep(750);

            }
            inHouse.traverse();

        }

        else if(tanHouse.contains(ctx.players.local())){
            final GameObject closedDoor = ctx.objects.select().id(door).nearest().poll();
            if(closedDoor.valid()){
                closedDoor.interact("Open", "Door");
                Condition.sleep(750);

            }
            while(!AlBank.contains(ctx.players.local())) {
                backDoor.traverse();
            }

        }

    }
}
