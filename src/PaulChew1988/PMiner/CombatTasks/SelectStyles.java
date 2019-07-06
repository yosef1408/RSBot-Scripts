package PMiner.CombatTasks;

import PMiner.Task;
import org.powerbot.script.rt4.*;

public class SelectStyles extends Task {
    int attack;
    int strength;
    int defence;


    public SelectStyles(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {

        return  ctx.players.local().combatLevel() <12 && (ctx.skills.level(Constants.SKILLS_ATTACK) >=12 && ctx.combat.style().equals(Combat.Style.ACCURATE) ||(ctx.skills.level(Constants.SKILLS_ATTACK) >=12 && ctx.skills.level(Constants.SKILLS_STRENGTH)>=12&& ctx.combat.style().equals(Combat.Style.AGGRESSIVE)));
    }

    @Override
    public void execute() {
        System.out.println("Should be switching");



        //ctx.combat.style(Combat.Style.ACCURATE);
        if ((ctx.skills.level(Constants.SKILLS_ATTACK)>=12)&& (ctx.skills.level(Constants.SKILLS_STRENGTH)<12)) {
            ctx.combat.style(Combat.Style.AGGRESSIVE);
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        else if((ctx.skills.level(Constants.SKILLS_ATTACK)>=12) && (ctx.skills.level(Constants.SKILLS_STRENGTH)>=12)){
            ctx.combat.style(Combat.Style.DEFENSIVE);
            ctx.game.tab(Game.Tab.INVENTORY);
        }
    }

}
