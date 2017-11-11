package thebonobo.SalmonCollector.utils;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.World;
import org.powerbot.script.rt4.ClientContext;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

public class WorldHelper {
    private ClientContext ctx;
    public WorldHelper(ClientContext ctx){
        this.ctx = ctx;
    }

    public boolean shouldHop(){
        long sessionTime = (System.currentTimeMillis() / 1000) - Info.getInstance().getStartTime();
        int collectedFish = Info.getInstance().getSessionRawSalmonCollected() + Info.getInstance().getSessionSalmonCollected();
        boolean notEnoughFish = collectedFish < ((sessionTime / 60.0 / 60) * 500); // more than 500 fish expected per hour

        if (sessionTime > 900 && notEnoughFish){
            System.out.println("not enough fish, hopping world");
            return true;
        } else {
            return false;
        }
    }

    public void randomWorldHop(){
        Condition.wait(() -> ctx.worlds.open(),1000,3);
        World world =  ctx.worlds.select()
                .joinable() // Selects and filters all worlds which are not PVP, DMM, or skill-required.
                .types(World.Type.FREE).shuffle().poll(); // Filters all worlds which are free
                //.servers(World.Server.GERMANY) ; // Only filters down to north american servers
                //.population(1000); // Filters down to worlds with less than or equal to 1000 players on
        Condition.wait(() -> world.hop(),1000,3);
        Info.getInstance().setStartTime(System.currentTimeMillis() / 1000);
        Info.getInstance().setSessionRawSalmonCollected(0);
        Info.getInstance().setSessionSalmonCollected(0);
        ctx.game.tab(Game.Tab.INVENTORY);
    }

}
