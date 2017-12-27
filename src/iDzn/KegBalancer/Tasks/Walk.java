package iDzn.KegBalancer.Tasks;

import iDzn.KegBalancer.Task;
import iDzn.KegBalancer.Walker;
import org.powerbot.bot.rt4.client.Client;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Equipment;
import org.powerbot.script.rt4.GameObject;

public class Walk extends Task<org.powerbot.script.ClientContext<Client>> {
    public static final Tile pathToBank[] = {new Tile(2867, 3539, 1), new Tile(2864, 3541, 1), new Tile(2861, 3542, 1), new Tile(2858, 3542, 1),
            new Tile(2857, 3545, 1), new Tile(2857, 3548, 1), new Tile(2855, 3551, 1), new Tile(2852, 3552, 1),
            new Tile(2851, 3549, 1), new Tile(2849, 3546, 1), new Tile(2849, 3543, 1), new Tile(2846, 3540, 1),
            new Tile(2843, 3539, 1), new Tile(2841, 3538, 0), new Tile(2843, 3543, 0)};

    GameObject hDoor = ctx.objects.select().id(15658, 15660).poll();
    public static int ENERGY[] = {3008, 3010, 3012, 3014};
    private final Walker walker = new Walker(ctx);


    public Walk(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {

        return (ctx.equipment.itemAt(Equipment.Slot.HEAD).id() == -1) && (ctx.inventory.select().id(ENERGY).count() == 0) && !ctx.bank.opened()
                || (ctx.inventory.select().count() == 28 && pathToBank[0].distanceTo(ctx.players.local()) > 6) && !ctx.bank.opened();
    }

    @Override
    public void execute() {
        if (!ctx.players.local().inMotion()
                || ctx.movement.destination().equals(Tile.NIL)) {
            if (ctx.inventory.select().id(ENERGY).count() == 0) {
                walker.walkPath(pathToBank);
                System.out.println("TO THE BANK!");

            } else {
                walker.walkPathReverse(pathToBank);
                System.out.println("TO THE KEG!");
            }
        }
    }
}