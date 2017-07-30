package noobienoobie123.FaladorCowKiller.tasks;

import noobienoobie123.FaladorCowKiller.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;

/**
 * Created by larry on 7/27/2017.
 */
public class Tan extends Task {
    Area tanHouse = new Area(new Tile(3278,3194,0),new Tile(3270,3190,0));

    final static int coins =  995;
    final static int cowHides = 1739;
    final static int softLeather =  1741;
    final static int hardLeather =  1743;

    public Tan(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {

        return tanHouse.contains(ctx.players.local()) && ctx.inventory.select().id(hardLeather).count() < 1;
    }

    @Override
    public void execute() {
        final Npc ellis = ctx.npcs.select().id(3231).poll();
        ellis.interact("Trade");
        Condition.sleep(1500);

        if(ctx.widgets.widget(324).component(88).visible()){
            Condition.sleep(1000);
            ctx.widgets.widget(324).component(101).interact("Tan All");
            Condition.sleep(500);
        }



    }
}
