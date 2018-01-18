package Aff1x.choppenheimer.Tasks;

import Aff1x.choppenheimer.Task;
import org.powerbot.script.Locatable;
import org.powerbot.script.rt6.ClientContext;

public class Bank extends Task<ClientContext> {

    public Bank(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate(){
        return (ctx.backpack.select().count()==28
                && !ctx.objects.select().name("Bank Booth").isEmpty()) || ctx.bank.opened();
    }

    @Override
    public void execute() {
        if (ctx.bank.inViewport() || ctx.bank.opened()) {
            System.out.println("Opening Bank!");
            if (ctx.bank.open()) {
                if (ctx.backpack.select().count() == 28)
                    ctx.bank.depositInventory();
                else
                    ctx.bank.close();
            }
        } else {
            Locatable bank = ctx.bank.nearest();
            if (bank.tile().distanceTo(ctx.players.local()) >= 7) {
                ctx.movement.step(bank);
            } else {
                ctx.camera.turnTo(bank);
            }
        }
    }

}
