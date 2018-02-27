package lilmj12.tasks;

import lilmj12.main.Task;

import lilmj12.misc.Timer;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.GameObject;

public class Smelt extends Task {

    private int rightUpperX = 3279;
    private int rightUpperY = 3188;

    private int leftLowerX = 3274;
    private int leftLowerY = 3184;

    Tile rightUpperTile = new Tile(rightUpperX, rightUpperY);
    Tile leftLowerTile = new Tile(leftLowerX, leftLowerY);

    Area smeltingArea = new Area(rightUpperTile, leftLowerTile);

    final int STEEL_BAR_ID = 2353;



    public Smelt(ClientContext ctx){
        super(ctx);
    }

    @Override
    public boolean activate(){

        if(smeltingArea.contains(ctx.players.local().tile()) && ctx.inventory.select().id(STEEL_BAR_ID).count() > 0){
            return true;
        }
        return false;
    }

    @Override
    public void execute(){


        Timer tracker = new Timer();

        if(ctx.inventory.select().id(STEEL_BAR_ID).count() < 27) {
            tracker.setEndIn(6500);
        }
        int experience = ctx.skills.experience(13);

        final int FURNACE_ID = 24009;
        GameObject furnace = ctx.objects.select().id(FURNACE_ID).poll();

        while(tracker.getRemaining() > 0 && ctx.inventory.select().id(STEEL_BAR_ID).count() > 0){
            if(ctx.skills.experience(13) != experience){
                tracker.reset();
                experience = ctx.skills.experience(13);
            }
        }



        if(ctx.players.local().animation() == -1 && smeltingArea.contains(ctx.players.local())){
            ctx.inventory.select().id(STEEL_BAR_ID).poll().interact("Use");
            furnace.interact("Use");
            Condition.wait(ctx.widgets.widget(270).component(14)::inViewport, 30, 45);
            ctx.widgets.widget(270).component(14).interact("Make sets:");
            Condition.sleep(6000);
        }

    }
}
