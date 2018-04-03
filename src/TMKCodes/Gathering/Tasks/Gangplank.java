package Gathering.Tasks;

import Gathering.Task;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.Random;

public class Gangplank extends Task {
    final static int gangplanks[] = {2082, 2084};

    private Random rand;

    public Gangplank(ClientContext ctx) {
        super(ctx);
        rand = new Random();
    }

    @Override
    public boolean activate() {
        for(int id : gangplanks) {
            GameObject gangplank = ctx.objects.select().id(id).nearest().poll();
            if(ctx.players.local().tile().distanceTo(gangplank) < 5) {
                System.out.println("Crossing gangplank = true");
                return true;
            }
        }
        System.out.println("Crossing gangplank = false");
        return false;
    }

    @Override
    public void execute() {
        for(int id : gangplanks) {
            GameObject gangplank = ctx.objects.select().id(id).nearest().poll();
            ctx.camera.turnTo(gangplank);
            gangplank.interact("Cross");
            try {
                Thread.sleep(rand.nextInt(200) + 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(ctx.players.local().animation() != -1) {
                break;
            }
        }
    }
}
