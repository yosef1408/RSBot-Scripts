package lilmj12.tasks;

import lilmj12.main.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public class Bank extends Task {

    public Bank(ClientContext ctx){
        super(ctx);
    }

    private final int STEEL_BAR_ID = 2353;

    private final int lowerLeftX = 3269;
    private final int lowerLeftY = 3161;

    private final int upperRightX = 3272;
    private final int upperRightY = 3173;

    Tile lowerLeftTile = new Tile(lowerLeftX, lowerLeftY);
    Tile upperRightTile = new Tile(upperRightX, upperRightY);

    Area bankArea = new Area(lowerLeftTile, upperRightTile);

    Item steelBar = ctx.inventory.select().id(STEEL_BAR_ID).poll();
    final int CANNONBALL_MOULD_ID = 4;

    @Override
    public boolean activate(){
        if(bankArea.contains(ctx.players.local().tile()) && ctx.inventory.select().id(STEEL_BAR_ID).count() < 25) return true;
        return false;
    }

    @Override
    public void execute(){

        if(ctx.bank.opened()){
            ctx.bank.depositAllExcept(CANNONBALL_MOULD_ID);

            if(ctx.bank.select().id(STEEL_BAR_ID).count(true) < 27){
                System.out.println("Killing script");
                ctx.controller.stop();
            }

            ctx.bank.withdraw(STEEL_BAR_ID, 27);
            ctx.bank.close();
        }else{
            if(ctx.bank.inViewport()){
                ctx.bank.open();
            }else{
                ctx.camera.turnTo(ctx.bank.nearest());
            }
        }

    }

}
