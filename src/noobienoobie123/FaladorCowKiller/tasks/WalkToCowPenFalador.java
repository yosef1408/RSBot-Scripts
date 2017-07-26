package noobienoobie123.FaladorCowKiller.tasks;

/**
 * Created by larry on 7/17/2017.
 */

import noobienoobie123.FaladorCowKiller.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.TilePath;

import java.util.concurrent.Callable;


public class WalkToCowPenFalador extends Task{

    public static final Tile[] pathToCowPen = {new Tile(3012, 3355, 0), new Tile(3012, 3358, 0), new Tile(3009, 3359, 0), new Tile(3008, 3356, 0), new Tile(3008, 3353, 0), new Tile(3008, 3350, 0), new Tile(3008, 3347, 0), new Tile(3008, 3344, 0), new Tile(3008, 3341, 0), new Tile(3008, 3338, 0), new Tile(3008, 3335, 0), new Tile(3008, 3332, 0), new Tile(3008, 3329, 0), new Tile(3008, 3326, 0), new Tile(3008, 3323, 0), new Tile(3011, 3321, 0), new Tile(3014, 3320, 0), new Tile(3017, 3321, 0), new Tile(3020, 3321, 0), new Tile(3023, 3319, 0), new Tile(3026, 3319, 0), new Tile(3029, 3320, 0), new Tile(3032, 3322, 0), new Tile(3035, 3322, 0), new Tile(3035, 3319, 0), new Tile(3034, 3316, 0), new Tile(3031, 3314, 0)};
    public static final Tile[] pathToGate = {new Tile(3031, 3305, 0), new Tile(3031, 3307, 0), new Tile(3031, 3309, 0), new Tile(3031, 3311, 0), new Tile(3032, 3313, 0)};
    public static final Tile[] pathInCow = {new Tile(3032, 3314, 0), new Tile(3032, 3312, 0), new Tile(3032, 3310, 0)};
    Area cowPen = new Area(new Tile(3043,3313,0),new Tile(3021,3297,0));
    TilePath toCowPenTilePath = ctx.movement.newTilePath(pathToCowPen);
    TilePath backToBank = ctx.movement.newTilePath(pathToCowPen).reverse();
    TilePath toPathInCowPen = ctx.movement.newTilePath(pathInCow);
    TilePath toGate = ctx.movement.newTilePath(pathToGate);
    public static final Tile Gate = new Tile(3032,3313,0);
    Area faladorBank = new Area(new Tile(3009,3358,0),new Tile(3019,3355,0));

    public WalkToCowPenFalador(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {

        return ((ctx.inventory.select().count() >27 || ((ctx.inventory.select().count() == 0) && !cowPen.contains(ctx.players.local()))));
    }

    @Override
    public void execute() {
    int counter  = 0;
        if((ctx.inventory.select().count()==0 && pathToCowPen[0].distanceTo(ctx.players.local())>7 && !cowPen.contains(ctx.players.local()))|| (ctx.inventory.select().count()==0 && faladorBank.contains(ctx.players.local()) && !cowPen.contains(ctx.players.local()))){
            while(pathInCow[0].distanceTo(ctx.players.local())>2) {

                if(ctx.controller.isStopping()){
                    break;
                }

                toCowPenTilePath.traverse();
                Condition.sleep(500);
            }

            final GameObject closedGate = ctx.objects.select().id(1562).nearest().poll();
            if (closedGate.orientation() == 5){
                ctx.camera.turnTo(closedGate);
                Condition.sleep(1150);
                int pitch = 43;
                ctx.camera.pitch(pitch);
                closedGate.interact("Open", "Gate");

            }
            while(!cowPen.contains(ctx.players.local())) {

                if(ctx.controller.isStopping()){
                    break;
                }
                toPathInCowPen.traverse();
            }


        }
        if(ctx.inventory.select().count() >27 && pathToCowPen[0].distanceTo(ctx.players.local()) > 2){
            while(!ctx.players.local().tile().equals((Gate))){

                if(ctx.controller.isStopping()){
                    break;
                }

                ctx.movement.findPath(Gate).traverse();
                Condition.sleep(500);
            }

            final GameObject closedGate = ctx.objects.select().id(1562).nearest().poll();


            while (closedGate.orientation() == 5 ) {

                if(ctx.controller.isStopping()){
                    break;
                }




                ctx.camera.angleTo(3580);
                Condition.sleep(500);

                ctx.camera.pitch(35);
                Condition.sleep(500);
                closedGate.interact("Open", "Gate");

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return closedGate.orientation() == 4;
                    }
                },250,20);

                while(!faladorBank.contains(ctx.players.local())) {

                    if(ctx.controller.isStopping()){
                        break;
                    }

                    backToBank.traverse();
                }

            }
            if(!faladorBank.contains(ctx.players.local())){

                while(!faladorBank.contains(ctx.players.local())) {

                    if(ctx.controller.isStopping()){
                        break;
                    }

                    backToBank.traverse();
                }


            }
        }



    }
}
