package iDzn.KegBalancer.Tasks;

import iDzn.KegBalancer.Task;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;


public class KegMe  extends Task<org.powerbot.script.ClientContext<org.powerbot.bot.rt4.client.Client>> {

    public KegMe(ClientContext ctx) {
        super(ctx);
    }

    private int[] EnergyP = {3008, 3010, 3012, 3014};
    private final Area KegArea = new Area(new Tile(2861, 3543, 1), new Tile(2870, 3536, 1));
    private final int[] kegBounds = {-29, 20, -57, 0, -14, 28};

    @Override
    public boolean activate() {
        return ((ctx.equipment.itemAt(Equipment.Slot.HEAD).id() == -1) && ctx.inventory.select().id(EnergyP).count() > 0 && KegArea.contains(ctx.players.local()));

    }

    @Override
    public void execute() {
        GameObject Keg = ctx.objects.select().id(15668).nearest().poll();
        Keg.bounds(kegBounds);
        if ((ctx.equipment.itemAt(Equipment.Slot.HEAD).id() == -1)) {

            Keg.interact("Pick-up", "Keg");
            System.out.println("Lifting Keg");

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return (ctx.equipment.itemAt(Equipment.Slot.HEAD).id() == 8860);
                }
            }, 200, 10);
        }

    }
}