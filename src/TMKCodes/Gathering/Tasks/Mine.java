package Gathering.Tasks;

import Gathering.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Callable;

public class Mine extends Task {

    ArrayList<Integer> ores = new ArrayList<Integer>();
    private Random rand = new Random();
    private Tile rockTile = Tile.NIL;
    private int rockId = 0;

    public Mine(ClientContext ctx, String oreName) {
        super(ctx);
        if(oreName.equals("Clay")) {

        } else if(oreName.equals("Copper")) {
            ores.add(7453);
            ores.add(7469);
            ores.add(7484);
        } else if(oreName.equals("Tin")) {
            ores.add(7485);
            ores.add(7486);
        } else if(oreName.equals("Silver")) {

        } else if(oreName.equals("Iron")) {
            ores.add(7488);
            ores.add(7455);
            ores.add(7469);
        } else if(oreName.equals("Gold")) {

        } else if(oreName.equals("Coal")) {
            ores.add(7489);
            ores.add(7456);
        } else if(oreName.equals("Mithril")) {

        } else if(oreName.equals("Adamantite")) {

        } else if(oreName.equals("Runite")) {

        }
    }

    @Override
    public boolean activate() {
        System.out.println("Mining");
        return ctx.players.local().animation() == -1 && ctx.inventory.select().count() < 28;

    }

    @Override
    public void execute() {
        for (int id : ores) {
            for(GameObject ore : ctx.objects.select().id(id).nearest()) {
                if(ore.valid()) {
                    rockTile = ore.tile();
                    rockId = ore.id();
                    if(!ore.inViewport()) {
                        ctx.camera.turnTo(ore);
                        ctx.camera.pitch(rand.nextInt(100) +  60);
                    }
                    ore.interact("Mine", "Rocks");
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            System.out.println("Waiting animation to stop");
                            if(ctx.objects.select().at(rockTile).id(rockId).poll().equals(ctx.objects.nil())) {
                                return true;
                            }
                            return ctx.players.local().animation() != -1;
                        }
                    }, rand.nextInt(350) + 250, rand.nextInt(25) + 15);
                    if (ctx.players.local().animation() != -1) {
                        break;
                    }
                }
            }
            if (ctx.players.local().animation() != -1) {
                break;
            }
        }
    }
}
