package TMKCodes.Gathering.Tasks;

import TMKCodes.Gathering.Task;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.Random;


public class PayFare extends Task {

    private int musaPointSeamen[] = {3644, 3645, 3646, 3648};
    private String location;

    public PayFare(ClientContext ctx, String location) {
        super(ctx);
        this.location = location;
    }

    @Override
    public boolean activate() {
        if(location.equals("Musa point")) {
            if(new Tile(2952, 3146, 0).distanceTo(ctx.players.local()) < 6 && ctx.inventory.select().count() == 28) {
                System.out.println("Musa Point dock Pay-fare activate true");
                return true;
            } else if(new Tile(3029, 3217, 0).distanceTo(ctx.players.local()) < 6 && ctx.inventory.select().count() < 28) {
                System.out.println("Port Sarim dock Pay-fare activate true");
                return true;
            }
        }
        return false;
    }

    @Override
    public void execute() {
        if(new Tile(2952, 3146, 0).distanceTo(ctx.players.local()) < 6 && ctx.inventory.select().count() == 28) {
            for(int id : musaPointSeamen) {
                Npc seamen = ctx.npcs.select().id(id).nearest().poll();
                ctx.camera.turnTo(seamen);
                seamen.interact("Pay-fare");
                try {
                    Thread.sleep(Random.nextInt(1200, 600));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(ctx.players.local().animation() != -1) {
                    break;
                }
            }
        } else if(new Tile(3029, 3217, 0).distanceTo(ctx.players.local()) < 6 && ctx.inventory.select().count() < 28) {
            for(int id : musaPointSeamen) {
                Npc seamen = ctx.npcs.select().id(id).nearest().poll();
                ctx.camera.turnTo(seamen);
                seamen.interact("Pay-fare");
                try {
                    Thread.sleep(Random.nextInt(1200, 600));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(ctx.players.local().animation() != -1) {
                    break;
                }
            }
        }
    }
}
