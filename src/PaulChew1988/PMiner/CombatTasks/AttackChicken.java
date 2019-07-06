package PMiner.CombatTasks;
import PMiner.Task;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import org.powerbot.script.Condition;

import java.util.concurrent.Callable;

public class AttackChicken extends Task{
    final static int CHICKEN_IDS []= {1173};
    Tile chickenLocation = Tile.NIL;


    public AttackChicken(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.npcs.select().id(CHICKEN_IDS).poll().equals(ctx.npcs.nil())) || (ctx.players.local().animation() == -1) && (ctx.players.local().combatLevel()<13);
    }

    @Override
    public void execute() {
        System.out.println("Attacking");
        Npc chicken = ctx.npcs.select().id(CHICKEN_IDS).nearest().poll();
        if(!chicken.inCombat()) {
            chickenLocation = chicken.tile();
            chicken.interact("Attack");

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.players.local().interacting().valid() && chicken.animation() == 5389 && !chicken.healthBarVisible();
                }
            },200,10);
        }
    }
}
