package iDzn.KegBalancer.Tasks;

import iDzn.KegBalancer.Task;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Energy extends Task<org.powerbot.script.ClientContext<org.powerbot.bot.rt4.client.Client>> {
    private final Area KegArea = new Area(new Tile(2868, 3541, 1), new Tile(2864, 3537, 1));
    private int [] EnergyP = {3008, 3010, 3012, 3014};

    public Energy(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.movement.energyLevel()<99 && ctx.inventory.select().id(EnergyP).count()>0 && KegArea.contains(ctx.players.local());
    }

    @Override
    public void execute() {
        Item ENERGY = ctx.inventory.select().id(3014, 3012, 3010, 3008).poll();
        System.out.println("Needs Re-Fueling");
        if (!ctx.game.tab(Game.Tab.INVENTORY)){
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        if ((ctx.equipment.itemAt(Equipment.Slot.HEAD).id() == 8860) && ctx.inventory.select().id(ENERGY).count() > 0 && ctx.movement.energyLevel() < 99) {
            ENERGY.interact("Drink");

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return (ctx.movement.energyLevel() > 99);
                }
            }, 50, 25);
        }

    }
}