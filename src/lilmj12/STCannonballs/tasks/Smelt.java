package lilmj12.STCannonballs.tasks;

import lilmj12.STCannonballs.misc.Timer;
import lilmj12.STCannonballs.main.Task;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.GameObject;

public class Smelt extends Task {

    Area smeltingArea;

    final int STEEL_BAR_ID = 2353;
    final int FURNACE_ID;



    public Smelt(ClientContext ctx, Area smeltingArea, int FURNACE_ID) {
        super(ctx);
        this.smeltingArea = smeltingArea;
        this.FURNACE_ID = FURNACE_ID;
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

        GameObject furnace = ctx.objects.select().id(FURNACE_ID).poll();


        while(tracker.getRemaining() > 0 && ctx.inventory.select().id(STEEL_BAR_ID).count() > 0){
            if(ctx.skills.experience(13) != experience){
                tracker.reset();
                experience = ctx.skills.experience(13);

            }
        }



        if(ctx.players.local().animation() == -1 && smeltingArea.contains(ctx.players.local())){
            ctx.inventory.select().id(STEEL_BAR_ID).poll().interact("Use");
            int startingExp = ctx.skills.experience(13);

            if(!furnace.inViewport()) ctx.camera.turnTo(furnace);

            ctx.objects.select().id(FURNACE_ID).poll().interact("Use", "Furnace");
            Condition.wait(ctx.widgets.widget(270).component(14)::inViewport, 20, 45);
            ctx.widgets.widget(270).component(14).interact("Make sets:");
            Condition.wait(() -> startingExp != ctx.skills.experience(13));
        }

    }
}
