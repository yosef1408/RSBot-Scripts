package lilmj12.main;


import lilmj12.misc.Walker;
import lilmj12.tasks.Bank;
import lilmj12.tasks.Smelt;
import lilmj12.tasks.Walk;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;

import java.util.ArrayList;

@Script.Manifest(name = "STCannonballs", description = "Smelts Cannonballs in Al-Kharid. Start near the Furnace or Bank.", properties = "author=Lilmj12; topic = 1343190; client = 4;")

public class STCannons extends PollingScript<ClientContext>{

    ArrayList<Task> taskList = new ArrayList<Task>();

    @Override
    public void start(){

        taskList.add(new Smelt(ctx));
        taskList.add(new Bank(ctx));
        taskList.add(new Walk(ctx));
        checkStart();

    }

    //Git test

    @Override
    public void poll(){

        for(Task t: taskList){
            if(t.activate()){
                t.execute();
                break;
            }
        }

    }

    public void checkStart(){

        final int CANNONBALL_MOULD_ID = 4;
        Tile bottomLeft = new Tile(3264, 3160);
        Tile topRight = new Tile(3282, 3195);
        Area scriptRunningArea = new Area(bottomLeft, topRight);

        final int lowerLeftX = 3269;
        final int lowerLeftY = 3161;

        final int upperRightX = 3272;
        final int upperRightY = 3173;

        Tile lowerLeftTile = new Tile(lowerLeftX, lowerLeftY);
        Tile upperRightTile = new Tile(upperRightX, upperRightY);

        Area bankArea = new Area(lowerLeftTile, upperRightTile);

        if(!scriptRunningArea.contains(ctx.players.local().tile())) ctx.controller.stop();


        if(ctx.inventory.select().id(CANNONBALL_MOULD_ID).count() == 0){

            final Tile[] pathFromFurnace = {new Tile(3276, 3186, 0), new Tile(3280, 3185, 0), new Tile(3280, 3181, 0), new Tile(3280, 3177, 0), new Tile(3277, 3173, 0), new Tile(3275, 3169, 0), new Tile(3271, 3167, 0)};
            final Walker walker = new Walker(ctx);

            if(!bankArea.contains(ctx.players.local().tile())){
                walker.walkPath(pathFromFurnace);
            }
            while(!bankArea.contains(ctx.players.local().tile())){
                int randomSleep = Random.nextInt(100, 250);
                Condition.sleep(randomSleep);
                walker.walkPath(pathFromFurnace);
            }

            if(!ctx.bank.opened()) {
                if(!ctx.bank.inViewport()){
                    ctx.camera.turnTo(ctx.bank.nearest());
                }
                ctx.bank.open();
            }

            ctx.bank.withdraw(CANNONBALL_MOULD_ID, 1);
        }
    }



}
