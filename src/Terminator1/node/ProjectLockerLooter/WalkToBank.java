package Terminator1.node.ProjectLockerLooter;

/**
 * Created by Genoss on 12/26/2016:3:50 PM
 */
import api.Node;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Npc;
import scripts.ProjectLockerLooter;

import java.util.logging.Logger;

public class WalkToBank extends Node<ClientContext> {

    private ProjectLockerLooter pll;
    private Npc banker = null;

    public WalkToBank(ClientContext ctx,ProjectLockerLooter mc) {
        super(ctx);
        pll = mc;
    }

    @Override
    public void executeBlock() {
        pll.setStatus("Walking to the bank...");
        ctx.movement.findPath(banker).traverse();
    }

    @Override
    public boolean isReady() {
        banker = ctx.npcs.select().id(pll.getBankerID()).nearest().poll();
        return (!ctx.players.local().inMotion()) && ((ctx.inventory.select().id(pll.getFoodID()).count() == 0)||((!pll.getEatFood())&&(ctx.inventory.select().count()==28))) && (banker.tile().distanceTo(ctx.players.local().tile())>2) && ((ctx.skills.level(Constants.SKILLS_HITPOINTS) <= pll.getHealthLimt())||(ctx.inventory.select().count() == 28));
    }
}
