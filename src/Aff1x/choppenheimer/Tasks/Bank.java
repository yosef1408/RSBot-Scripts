package Aff1x.choppenheimer.Tasks;

import Aff1x.choppenheimer.Task;
import org.powerbot.script.rt6.ClientContext;

public class Bank extends Task<ClientContext> {

    public Bank(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate(){

        return ctx.backpack.select().count()==28
                && !ctx.bank.isEmpty()
                && !ctx.bank.opened();

    }

    @Override
    public void execute(){
        if(ctx.bank.inViewport())
        {
            if (ctx.bank.inViewport() || ctx.bank.opened()) {
                if (!ctx.backpack.select().isEmpty())
                    ctx.bank.depositInventory();
                else
                    ctx.bank.close();
            } else {
                ctx.bank.open();
            }
        } else {
            ctx.movement.step(ctx.bank.nearest());
            ctx.camera.turnTo(ctx.bank.nearest());
        }
    }

}
