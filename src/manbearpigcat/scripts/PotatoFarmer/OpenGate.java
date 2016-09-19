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
public class OpenGate extends Task<ClientContext> {
    public static final int POTATO = 312;
    public static final int[] GATE = {45208, 45206};
    public static final Tile GATE_AREA_T1 = new Tile(3270, 3329, 0);
    public static final Tile GATE_AREA_T2 = new Tile(3245, 3309, 0);
    public static final Area area = new Area(GATE_AREA_T1, GATE_AREA_T2);
    public static final Tile after = new Tile(3262, 3314,0);
    public static final Tile after1 = new Tile(3255, 3312, 0);
    public static final Tile after2 = new Tile(3251, 3306, 0);


    public static Stats sPots = PotatoPicker.sPots;

    public OpenGate(ClientContext ctx){
        super(ctx);
    }

    //if pathway is blocked, open gate
    public boolean activate(){
        return !ctx.movement.reachable(ctx.players.local().tile(), after) && area.contains(ctx.players.local());
    }

    public void execute(){
        sPots.setState("Opening Gate.");
        final GameObject closedGate = ctx.objects.select().id(GATE).nearest().poll();
        if(closedGate.valid()) {
            final GameObject pot = ctx.objects.select().id(POTATO).nearest().poll();
            ctx.camera.turnTo(pot);
            Random r = new Random();      //use random so camera doesn't always set pitch to exactly 45 deg. every time
            int pitch = r.nextInt(40,48); //angle the camera to make sure the mouse doesn't miss the gate
            ctx.camera.pitch(pitch);
            boolean check;
            check = Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return closedGate.interact("Open", closedGate.name());
                }
            }, 1000, 5);

            //reset overhead view
            if(check){
                ctx.camera.pitch(90);
                Random rand = new Random();
                int x = rand.nextInt(0,4);
                switch(x){
                    case 0:
                        ctx.movement.step(after);
                        break;
                    case 1:
                        ctx.movement.step(after1);
                        break;
                    case 2:
                        ctx.movement.step(after2);
                        break;
                    default:
                        ctx.movement.step(after);
                        break;
                }
            }
        }
    }
}
