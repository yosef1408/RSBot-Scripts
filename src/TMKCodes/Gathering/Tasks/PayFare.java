package Gathering.Tasks;

import Gathering.Task;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.Random;

public class PayFare extends Task {

    final static int musaPointSeamen[] = {3644, 3645, 3646, 3648};
    private String location;

    private Random rand;

    public PayFare(ClientContext ctx, String location) {
        super(ctx);
        this.location = location;
        rand = new Random();
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
                    Thread.sleep(rand.nextInt(500) + 1);
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
                    Thread.sleep(rand.nextInt(500) + 1);
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
