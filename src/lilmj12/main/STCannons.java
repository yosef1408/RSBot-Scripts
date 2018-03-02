package lilmj12.STCannonballs.main;


import lilmj12.STCannonballs.misc.Walker;
import lilmj12.STCannonballs.tasks.Bank;
import lilmj12.STCannonballs.tasks.Smelt;
import lilmj12.STCannonballs.tasks.Walk;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.util.ArrayList;

@Script.Manifest(name = "STCannonballs", description = "Smelts Cannonballs in Al-Kharid. Start near the Furnace or Bank.", properties = "author=Lilmj12; topic = 1343190; client = 4;")

public class STCannons extends PollingScript<ClientContext> implements PaintListener{

    ArrayList<Task> taskList = new ArrayList<Task>();
    private int startingXp = ctx.skills.experience(13);

    Tile rightUpperTileKharid = new Tile(3282, 3190);
    Tile leftLowerTileKharid = new Tile(3263, 3160);
    Area kharidArea = new Area(leftLowerTileKharid, rightUpperTileKharid);

    Tile leftLowerTileEdgeville = new Tile(3087, 3485);
    Tile rightUpperTileEdgeville = new Tile(3112, 3503);
    Area edgevilleArea = new Area(leftLowerTileEdgeville, rightUpperTileEdgeville);

    public static final Tile[] pathFromFurnaceEdge = {new Tile(3107, 3498, 0), new Tile(3103, 3499, 0), new Tile(3099, 3497, 0), new Tile(3095, 3494, 0)};
    public static final Tile[] pathFromFurnaceKharid = {new Tile(3276, 3186, 0), new Tile(3280, 3185, 0), new Tile(3280, 3181, 0), new Tile(3280, 3177, 0), new Tile(3277, 3173, 0), new Tile(3275, 3169, 0), new Tile(3271, 3167, 0)};

    Tile lowerLeftTileBankKharid = new Tile(3269, 3161);
    Tile upperRightTileBankKharid = new Tile(3272, 3173);

    Area bankAreaKharid = new Area(lowerLeftTileBankKharid, upperRightTileBankKharid);

    Tile lowerLeftTileBankEdge = new Tile(3091, 3488);
    Tile upperRightTileBankEdge = new Tile(3098, 3498);
    Area bankAreaEdge = new Area(lowerLeftTileBankEdge, upperRightTileBankEdge);

    Tile lowerLeftTileSmeltEdge = new Tile(3105, 3497);
    Tile upperRightTileSmeltEdge = new Tile(3110, 3501);
    Area smeltAreaEdge = new Area(lowerLeftTileSmeltEdge, upperRightTileSmeltEdge);


    Tile rightUpperTileSmeltKharid = new Tile(3279, 3188);
    Tile leftLowerTileSmeltKharid = new Tile(3274, 3184);
    Area smeltingAreaKharid = new Area(rightUpperTileSmeltKharid, leftLowerTileSmeltKharid);


    int FURNACE_ID;
    Area scriptRunningArea;
    Tile[] pathFromFurnace;
    Area bankArea;
    Area smeltArea;

    final int FURNACE_ID_KHARID = 24009;
    final int FURNACE_ID_EDGE = 16469;



    @Override
    public void start(){

        checkStart();
        taskList.add(new Smelt(ctx, smeltArea, FURNACE_ID));
        taskList.add(new Bank(ctx, bankArea));
        taskList.add(new Walk(ctx, bankArea, smeltArea, pathFromFurnace));



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

        if(edgevilleArea.contains(ctx.players.local().tile())){

            scriptRunningArea = edgevilleArea;
            pathFromFurnace = pathFromFurnaceEdge;
            bankArea = bankAreaEdge;
            smeltArea = smeltAreaEdge;
            FURNACE_ID = FURNACE_ID_EDGE;

        }else if(kharidArea.contains(ctx.players.local().tile())){

            scriptRunningArea = kharidArea;
            pathFromFurnace = pathFromFurnaceKharid;
            bankArea = bankAreaKharid;
            smeltArea = smeltingAreaKharid;
            FURNACE_ID = FURNACE_ID_KHARID;

        }else{
            System.out.println("Stopping");
            ctx.controller.stop();
        }




        if(ctx.inventory.select().id(CANNONBALL_MOULD_ID).count() == 0){

            final Walker walker = new Walker(ctx);

            if(!bankArea.contains(ctx.players.local().tile())){
                walker.walkPath(pathFromFurnace);
                Condition.wait(() -> bankArea.contains(ctx.players.local().tile()));
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


    @Override
    public void repaint(Graphics graphics) {

        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds/1000) % 60;
        long minutes = seconds / 60 % 60;
        long hours = minutes / 60;

        int steelBarsUsed = (int)((ctx.skills.experience(13) - startingXp) / 25.5);
        int cannonballsCreated = steelBarsUsed * 4;


        Graphics2D g = (Graphics2D)graphics;

        g.setColor(new Color(0, 0, 0));
        g.fillRect(30,30,180, 37);


        g.setColor(new Color(255,255,255));
        g.drawRect(30,30,180,37);

        g.drawString("Time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 36, 46);
        g.drawString("Cannonballs Created: " + cannonballsCreated, 36, 58);


    }
}
