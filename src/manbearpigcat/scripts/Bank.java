package manbearpigcat.scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

import java.util.concurrent.Callable;

/**
 * Created by Shan on 2016-08-17.
 */
public class Bank extends Task<ClientContext> {

    public static final Tile VARROCK_BANK_TILE = new Tile(3185, 3435, 0);
    public static final Tile ALKHARID_BANK_TILE = new Tile(3270, 3168, 0);
    public static Stats sPots = PotatoPicker.sPots;


    public Bank(ClientContext ctx){
        super(ctx);
    }

    public boolean activate(){ return (VARROCK_BANK_TILE.distanceTo(ctx.players.local()) < 20 ||
            ALKHARID_BANK_TILE.distanceTo(ctx.players.local()) < 20) && ctx.backpack.select().count() == 28;}

    public void execute(){
        sPots.setState("Banking.");
        if(!ctx.bank.inViewport()){
            if(sPots.getBank() == 1) {
                ctx.movement.step(VARROCK_BANK_TILE);
                ctx.camera.turnTo(VARROCK_BANK_TILE);

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return VARROCK_BANK_TILE.distanceTo(ctx.players.local()) < 5;
                    }
                }, 1000, 3);
            }
            else{
                ctx.movement.step(ALKHARID_BANK_TILE);
                ctx.camera.turnTo(ALKHARID_BANK_TILE);

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ALKHARID_BANK_TILE.distanceTo(ctx.players.local()) < 5;
                    }
                }, 1000, 3);
            }
        }
        else if(ctx.bank.open()){
            if(ctx.bank.depositInventory()){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.bank.opened();
                    }
                }, 1000, 3);
            }
            sPots.setBag(0);
        }
    }
}