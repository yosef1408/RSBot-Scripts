package thebonobo.SalmonCollector.utils;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.LocalPath;
import org.powerbot.script.rt4.TilePath;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

public class Walker {
    private TilePath randomizedPath;
    private LocalPath generatedPath;
    private ClientContext ctx;

    TilePath path;

    public Walker(ClientContext ctx, TilePath path){
        this.ctx=ctx;
        this.path = path;
    }

    public void Walk() {
        setRandomizedPath();
        if (!hasArrived()) {
            randomizedPath.traverse();
            System.out.println("Started walking path");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    Condition.sleep(100);
                    // Only click next tile when close to current destination tile, when it can't find path, find path with the API.
                    double distanceNextTile = ctx.movement.destination().distanceTo(randomizedPath.next());
                    if (Random.nextInt(10,90) == ctx.movement.energyLevel() || ctx.movement.energyLevel() > 99)
                        ctx.movement.running(true);
                    if ((!ctx.players.local().inMotion() || (distanceNextTile > 8 && distanceNextTile < 12)) && CanReachNextTile()) {
                        ctx.movement.step(randomizedPath.next());
                    } else
                        setRandomizedPath();
                    return hasArrived() || !ctx.players.local().inMotion();
                }
            }, 400, 20);
        }
    }


    public boolean CanReachNextTile(){
        List<Tile> tiles = Arrays.asList(path.toArray());
        Tile destination = tiles.get(tiles.indexOf(ctx.players.local().tile()) + 1 % tiles.size());
        return ctx.movement.reachable(ctx.players.local(), destination);
    }

    public void setRandomizedPath(){
        randomizedPath = path.randomize(Random.nextInt(0, 2), Random.nextInt(0, 2));
    }

    private boolean hasArrived(){
        return randomizedPath.end().distanceTo(ctx.players.local()) <= 3;
    }

}
