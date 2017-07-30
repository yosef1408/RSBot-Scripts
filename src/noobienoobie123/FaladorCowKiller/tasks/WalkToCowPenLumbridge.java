package noobienoobie123.FaladorCowKiller.tasks;

import noobienoobie123.FaladorCowKiller.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.TilePath;

/**
 * Created by larry on 7/24/2017.
 */
public class WalkToCowPenLumbridge extends Task {

    public static final Tile[] path2ndFloorCastle = {new Tile(3208, 3220, 2), new Tile(3206, 3218, 2), new Tile(3206, 3216, 2), new Tile(3206, 3214, 2), new Tile(3205, 3212, 2), new Tile(3207, 3210, 2), new Tile(3205, 3209, 2)};
    public static final Tile [] floor1Castle = {new Tile(3206, 3208, 1), new Tile(3205, 3209, 1)};
    public static final Tile[] pathToCowPen = {new Tile(3206, 3208, 0), new Tile(3208, 3209, 0), new Tile(3210, 3209, 0), new Tile(3212, 3209, 0), new Tile(3214, 3210, 0), new Tile(3215, 3212, 0), new Tile(3215, 3214, 0), new Tile(3215, 3217, 0), new Tile(3217, 3218, 0), new Tile(3219, 3218, 0), new Tile(3221, 3218, 0), new Tile(3223, 3218, 0), new Tile(3225, 3218, 0), new Tile(3227, 3218, 0), new Tile(3229, 3218, 0), new Tile(3232, 3218, 0), new Tile(3234, 3219, 0), new Tile(3236, 3221, 0), new Tile(3238, 3223, 0), new Tile(3239, 3225, 0), new Tile(3241, 3225, 0), new Tile(3243, 3225, 0), new Tile(3245, 3225, 0), new Tile(3247, 3225, 0), new Tile(3249, 3225, 0), new Tile(3251, 3225, 0), new Tile(3253, 3225, 0), new Tile(3255, 3226, 0), new Tile(3258, 3228, 0), new Tile(3259, 3230, 0), new Tile(3259, 3232, 0), new Tile(3259, 3234, 0), new Tile(3259, 3236, 0), new Tile(3259, 3238, 0), new Tile(3259, 3240, 0), new Tile(3259, 3242, 0), new Tile(3259, 3244, 0), new Tile(3257, 3246, 0), new Tile(3256, 3248, 0), new Tile(3254, 3249, 0), new Tile(3252, 3251, 0), new Tile(3250, 3253, 0), new Tile(3250, 3255, 0), new Tile(3250, 3257, 0), new Tile(3250, 3259, 0), new Tile(3249, 3261, 0), new Tile(3250, 3263, 0), new Tile(3250, 3265, 0), new Tile(3250, 3267, 0), new Tile(3252, 3267, 0)};
    public static final Tile[] pathInCowPen = {new Tile(3252, 3267, 0), new Tile(3254, 3267, 0), new Tile(3256, 3267, 0), new Tile(3258, 3267, 0)};
    Area cowPenP1 = new Area(new Tile(3253, 3255, 0), new Tile(3265, 3280, 0));
    Area cowPenP2 = new Area(new Tile(3253, 3255, 0), new Tile(3265, 3281, 0));
    TilePath to2ndFloor = ctx.movement.newTilePath(path2ndFloorCastle);
    TilePath toCowPen = ctx.movement.newTilePath(pathToCowPen);
    TilePath backToBank = ctx.movement.newTilePath(pathToCowPen).reverse();
    TilePath toInCowPen = ctx.movement.newTilePath(pathInCowPen);
    public static final Tile gateLocation = new Tile(3253,3266,0);
    Area lumbBank = new Area(new Tile(3205, 3209, 2), new Tile(3212, 3225, 2));
    Area penEnter = new Area(new Tile(3250, 3269, 0), new Tile(3253, 3266, 0));


    public WalkToCowPenLumbridge(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ((((ctx.inventory.select().count() == 0)) && (lumbBank.contains(ctx.players.local()) || ctx.players.local().tile().equals(floor1Castle[1]) || (!cowPenP2.contains(ctx.players.local()) && !cowPenP1.contains(ctx.players.local())))) || ((ctx.inventory.select().count() > 27) && !ctx.players.local().tile().equals(pathToCowPen[0]))            );
    }

    @Override
    public void execute() {

        if ((ctx.inventory.select().count() == 0 && lumbBank.contains(ctx.players.local())) || (ctx.players.local().tile().equals(path2ndFloorCastle[path2ndFloorCastle.length-1]) && ctx.inventory.select().count() != 28) ){


            while(!ctx.players.local().tile().equals(path2ndFloorCastle[path2ndFloorCastle.length-1])){
                to2ndFloor.traverse();
                Condition.sleep(500);
            }
            Condition.sleep(500);
            final GameObject staircase = ctx.objects.select().id(16673).nearest().poll();
            staircase.interact("Climb-down");

        }
        else if (ctx.inventory.select().count() == 0 && ctx.players.local().tile().equals(floor1Castle[0]) ){
            Condition.sleep(2000);
            final GameObject staircase = ctx.objects.select().id(16672).nearest().poll();
            staircase.interact("Climb-down");

        }
        else if ((ctx.inventory.select().count() == 0 && ctx.players.local().tile().equals(pathToCowPen[0])) || ((ctx.inventory.select().count() == 0 && !cowPenP1.contains(ctx.players.local())) && (!lumbBank.contains(ctx.players.local()) && !ctx.players.local().tile().equals(floor1Castle[1]))        ) ){



            while(!penEnter.contains(ctx.players.local())) {
                toCowPen.traverse();
                System.out.print("1");

            }



            final GameObject closedGate = ctx.objects.select().id(1558).nearest().poll();
            if (closedGate.orientation() == 0){
                ctx.camera.turnTo(closedGate);
                Condition.sleep(1150);
                int pitch = 11;
                ctx.camera.pitch(pitch);
                closedGate.interact("Open", "Gate");

            }

            while (!ctx.players.local().tile().equals(pathInCowPen[pathInCowPen.length-1]) ){

                Condition.sleep(500);
                toInCowPen.traverse();

            }


        }




        if ((ctx.inventory.select().count() == 28 && !ctx.players.local().tile().equals(pathToCowPen[0]) && !ctx.players.local().tile().equals(floor1Castle[1]) && !lumbBank.contains(ctx.players.local())) ){



            while(!ctx.players.local().tile().equals(gateLocation)){

                ctx.movement.findPath(gateLocation).traverse();
                Condition.sleep(500);
            }



            final GameObject closedGate = ctx.objects.select().id(1558).nearest().poll();

            if (closedGate.orientation() == 0){

                ctx.camera.turnTo(closedGate);
                Condition.sleep(1150);
                int pitch = 7;
                ctx.camera.pitch(pitch);
                closedGate.interact("Open", "Gate");

            }


            while(!ctx.players.local().tile().equals(pathToCowPen[0])){
                backToBank.traverse();
                Condition.sleep(1500);
            }




        }
        else if (ctx.inventory.select().count() == 28 && ctx.players.local().tile().equals(pathToCowPen[0]) ){
            Condition.sleep(1000);
            final GameObject staircase = ctx.objects.select().id(16671).nearest().poll();
            staircase.interact("Climb-up");


        }
        else if (ctx.inventory.select().count() == 28 && ctx.players.local().tile().equals(floor1Castle[1]) ){
            Condition.sleep(1750);
            final GameObject staircase = ctx.objects.select().id(16672).nearest().poll();
            staircase.interact("Climb-up");

        }
        else if (ctx.inventory.select().count() == 28 && ctx.players.local().tile().equals(path2ndFloorCastle[path2ndFloorCastle.length-1]) ){
            Condition.sleep(1000);
            while(!ctx.players.local().tile().equals(path2ndFloorCastle[0])) {
                (to2ndFloor.reverse()).traverse();
                Condition.sleep(500);
            }



        }









        }
    }

