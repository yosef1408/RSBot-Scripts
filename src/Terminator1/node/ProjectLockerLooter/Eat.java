package Terminator1.node.ProjectLockerLooter;

import org.powerbot.script.rt4.ClientContext;

/**
 * Created by Genoss on 12/25/2016:11:18 AM
 */
import Terminator1.api.Node;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game;
import Terminator1.ProjectLockerLooter;

public class Eat extends Node<ClientContext>{

    private ProjectLockerLooter pll = null;
    public Eat(ClientContext ctx, ProjectLockerLooter mc) {
        super(ctx);
        pll = mc;
    }

    @Override
    public void executeBlock() {
        pll.setStatus("Eating...");
        if(!ctx.game.tab().name().equals(Game.Tab.INVENTORY.name()))
            ctx.game.tab(Game.Tab.INVENTORY);
        ctx.inventory.select().id(pll.getFoodID()).poll().click();
    }

    @Override
    public boolean isReady() {
        return ((!(ctx.skills.level(Constants.SKILLS_HITPOINTS) > pll.getHealthLimt()) && (ctx.inventory.select().id(pll.getFoodID()).count() > 0) && !(ctx.bank.opened())) || ((ctx.inventory.select().id(pll.getFoodID()).count()>0)&&(pll.getEatFood())&&(ctx.inventory.select().count()==28)));
    }
}
