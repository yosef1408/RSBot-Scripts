package TMKCodes.Gathering.Tasks;

import TMKCodes.Gathering.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.Random;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Fish extends Task {

    private ArrayList<Integer> FISHING_SPOT_IDS = new ArrayList<Integer>();
    private String tool;

    public Fish(ClientContext ctx, String tool) {
        super(ctx);
        if(tool == "Cage") {
            FISHING_SPOT_IDS.add(1522);
            this.tool = "Cage";
        } else if(tool == "Harpoon") {
            FISHING_SPOT_IDS.add(1522);
        } else if(tool == "Small Net") {
            FISHING_SPOT_IDS.add(1530);
            FISHING_SPOT_IDS.add(1521);
            this.tool = "Net";
        } else if(tool == "Bait") {
            FISHING_SPOT_IDS.add(1521);
            this.tool = "Bait";
        }
    }

    @Override
    public boolean activate() {
        System.out.println("Fishing");
        return ctx.players.local().animation() == -1 && ctx.inventory.select().count() < 28 && ctx.players.local().inCombat() == false;
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
            }, Random.nextInt(120, 60), Random.nextInt(20, 10));
            ctx.camera.angle(Random.nextInt(360, 0));
            ctx.camera.pitch(Random.nextInt(100, 30));
            if(ctx.players.local().animation() != -1) {
                break;
            }
        }
    }
}
