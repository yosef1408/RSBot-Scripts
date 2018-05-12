package sintaax.tutorialisland.engine.constants;

import sintaax.tutorialisland.engine.objects.Context;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;

public class NPCs extends Context<ClientContext> {
    public NPCs(ClientContext ctx) {
        super(ctx);
    }

    public final Npc RUNESCAPE_GUIDE = ctx.npcs.select().id(3308).nearest().poll();
    public final Npc SURVIVAL_EXPERT = ctx.npcs.select().id(3306).nearest().poll();
    public final Npc FISHING_SPOT = ctx.npcs.select().id(3317).nearest().poll();
    public final Npc MASTER_CHEF = ctx.npcs.select().id(3305).nearest().poll();
    public final Npc QUEST_GUIDE = ctx.npcs.select().id(3312).nearest().poll();
    public final Npc MINING_INSTRUCTOR = ctx.npcs.select().id(3311).nearest().poll();
    public final Npc COMBAT_INSTRUCTOR = ctx.npcs.select().id(3307).nearest().poll();
    public final Npc GIANT_RAT = ctx.npcs.select().id(3313).nearest().poll();
    public final Npc FINANCIAL_ADVISOR = ctx.npcs.select().id(3310).nearest().poll();
    public final Npc BROTHER_BRACE = ctx.npcs.select().id(3319).nearest().poll();
    public final Npc MAGIC_INSTRUCTOR = ctx.npcs.select().id(3309).nearest().poll();
    public final Npc CHICKEN = ctx.npcs.select().id(3316).nearest().poll();

    public boolean exists(Npc npc) {
        return npc.valid();
    }

    public void attack(Npc npc) {
        if (npc.inViewport()) {
            npc.interact("Attack");
            ctx.camera.turnTo(npc);
        } else {
            ctx.movement.step(npc);
            ctx.camera.turnTo(npc);
        }
    }

    public void net(Npc npc) {
        if (npc.inViewport()) {
            npc.interact("Net");
            ctx.camera.turnTo(npc);
        } else {
            ctx.movement.step(npc);
            ctx.camera.turnTo(npc);
        }
    }

    public void talk(Npc npc) {
        if (npc.inViewport()) {
            npc.interact("Talk-to");
            ctx.camera.turnTo(npc);
        } else {
            ctx.movement.step(npc);
            ctx.camera.turnTo(npc);
        }
    }
}
