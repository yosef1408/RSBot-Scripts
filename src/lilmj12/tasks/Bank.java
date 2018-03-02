package lilmj12.STCannonballs.tasks;

import lilmj12.STCannonballs.main.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;

public class Bank extends Task {

    public Bank(ClientContext ctx, Area bankArea){
        super(ctx);
        this.bankArea = bankArea;
    }

    private final int STEEL_BAR_ID = 2353;

    Area bankArea;

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
            Condition.wait(() -> ctx.inventory.select().id(STEEL_BAR_ID).count() == 27);
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
