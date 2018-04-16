package TMKCodes.Gathering.Tasks;

import TMKCodes.Gathering.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Fight extends Task {

    private ArrayList<Integer> enemies = new ArrayList<Integer>();

    public Fight(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.players.local().animation() == -1 && ctx.players.local().healthPercent() > 50;
    }

    @Override
    public void execute() {
        for(int id : enemies) {
            final Npc enemy = ctx.npcs.select().id(id).select(new Filter<Npc>() {
                @Override
                public boolean accept(Npc npc) {
                    return !npc.inCombat();
                }
            }).nearest().poll();
            enemy.interact("Attack", enemy.name());
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return enemy.inCombat();
                }
            }, Random.nextInt(240, 120), Random.nextInt(20, 10));
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().inCombat();
                }
            }, Random.nextInt(240, 120), Random.nextInt(20, 10));
        }
    }
}
