package Gathering.Tasks;

import Gathering.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class Fish extends Task {

    List<Integer> FISHING_SPOT_IDS = new ArrayList<Integer>();
    Random rand;
    String tool;

    public Fish(ClientContext ctx, String tool) {
        super(ctx);
        this.tool = tool;
        if(tool == "Cage") {
            FISHING_SPOT_IDS.add(1522);
        } else if(tool == "Harpoon") {
            FISHING_SPOT_IDS.add(1522);
        } else if(tool == "Small Net") {
            FISHING_SPOT_IDS.add(1521);
        } else if(tool == "Bait") {
            FISHING_SPOT_IDS.add(1521);
        }
        rand = new Random();
    }

    @Override
    public boolean activate() {
        System.out.println("Fishing");
        return ctx.players.local().animation() == -1 && ctx.inventory.select().count() < 28;
    }

    @Override
    public void execute() {
        for(int id : FISHING_SPOT_IDS) {
            Npc spotToFish = ctx.npcs.select().id(id).nearest().poll();
            spotToFish.interact(tool);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().animation() != -1;
                }
            }, rand.nextInt(250) + 200, rand.nextInt(15) + 5);
            ctx.camera.angle(rand.nextInt(360) + 0);
            ctx.camera.pitch(rand.nextInt(100) + 30);
            if(ctx.players.local().animation() != -1) {
                break;
            }
        }
    }
}
