package NoobieNoobieSlayer.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import osrs.Task;


/**
 * Created by larry on 7/14/2017.
 */
public class Walk extends Task  {


  public Tile[] pathToMonsters = {};

    public Walk(ClientContext ctx, Tile [] path) {
        super(ctx);
        pathToMonsters = path;
    }

    @Override
    public boolean activate() {

        return ctx.combat.health() == 0 || pathToMonsters[pathToMonsters.length-1].distanceTo(ctx.players.local()) >12;
    }

    @Override
    public void execute() {

        Condition.sleep(3500);

        ctx.game.tab(Game.Tab.INVENTORY);

        if(ctx.inventory.count() != 0){
            for (Item allItems : ctx.inventory.items()) {
                allItems.interact("Wield");
            }
        }

        if(ctx.movement.energyLevel()>20 && !ctx.movement.running()){
            ctx.movement.running(true);
        }
        TilePath path = ctx.movement.newTilePath(pathToMonsters);
        path.traverse();




    }


}
