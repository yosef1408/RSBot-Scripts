package manbearpigcat.scripts;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.util.concurrent.Callable;

/**
 * Created by Shan on 2016-08-17.
 */
public class Pick extends Task<ClientContext> {
    public static final int POTATO = 312;
    public static Stats sPots = PotatoPicker.sPots;

    public static final Tile T1 = new Tile(3273, 3329, 0);
    public static final Tile T2 = new Tile(3241, 3297, 0);
    public static Area area = new Area(T1, T2);

    public Pick(ClientContext ctx){
        super(ctx);
    }

    public boolean activate(){
        return ctx.backpack.select().count() < 28 && area.contains(ctx.players.local());
    }

    public void execute(){
        sPots.setState("Picking Potatoes.");
        GameObject pot = ctx.objects.select().id(POTATO).nearest().poll();
        if(pot.inViewport()){
            boolean check;
            if(pot.interact("Pick", pot.name())){
                check = Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.players.local().animation() != -1;
                    }
                }, 500, 8);

                if(check){
                    sPots.addPot();
                }
            }
        }
        else{
            ctx.movement.step(pot);
            ctx.camera.turnTo(pot);
        }
    }
}
