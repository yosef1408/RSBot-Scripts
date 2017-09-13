package thebonobo.SalmonCollector.utils;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import thebonobo.SalmonCollector.tasks.Loot;

import java.awt.*;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

public class Antiban extends Loot {
    public Antiban(org.powerbot.script.rt4.ClientContext ctx){
        super(ctx);
    }

    public static void Wait(int min,int max){
        try {

            Thread.sleep((long) (Random.nextInt(min, max)));

        } catch (InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void WalkWait(){
        try {
            Thread.sleep(Random.nextInt(0, 200));
        } catch (InterruptedException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public static void RunAntiban(ClientContext ctx){
        int randNum = Random.nextInt(1, 60);
        if(randNum == 6) MoveMouseOffScreen(ctx);
        if(randNum == 5) RightClickPlayer(ctx);
        if(randNum <= 3) RandomMouseMovement(ctx,100,300);

    }

    /**
     * Author - Enfilade Moves the mouse a random distance between minDistance
     * and maxDistance from the current position of the mouse by generating
     * random vector and then multiplying it by a random number between
     * minDistance and maxDistance. The maximum distance is cut short if the
     * mouse would go off screen in the direction it chose.
     *
     * @param minDistance
     * The minimum distance the cursor will move
     * @param maxDistance
     * The maximum distance the cursor will move (exclusive)
     */
    public static void RandomMouseMovement(ClientContext ctx, int minDistance, int maxDistance) {
        double xvec = Math.random();
        if (Random.nextInt(0, 2) == 1) {
            xvec = -xvec;
        }
        double yvec = Math.sqrt(1 - xvec * xvec);
        if (Random.nextInt(0, 2) == 1) {
            yvec = -yvec;
        }
        double distance = maxDistance;
        Point p = ctx.input.getLocation();
        int maxX = (int) Math.round(xvec * distance + p.x);
        distance -= Math.abs((maxX - Math.max(0,
                Math.min(ctx.game.dimensions().getWidth(), maxX)))
                / xvec);
        int maxY = (int) Math.round(yvec * distance + p.y);
        distance -= Math.abs((maxY - Math.max(0,
                Math.min(ctx.game.dimensions().getHeight(), maxY)))
                / yvec);
        if (distance < minDistance) {
            return;
        }
        distance = Random.nextInt(minDistance, (int) distance);
        ctx.input.move((int) (xvec * distance) + p.x, (int) (yvec * distance) + p.y);
    }
    //source: https://www.powerbot.org/community/topic/895370-mouse-help-please-move-mouse-off-of-client/
    public static void MoveMouseOffScreen(ClientContext ctx){
        ctx.input.hop((int) (Random.nextInt(10, 100) + ctx.game.dimensions().getWidth()),
                Random.nextInt(-10, (int)(ctx.game.dimensions().getHeight() + 10)));
    }

    public static void RightClickPlayer(ClientContext ctx){
        Condition.sleep(100);
        ctx.players.select().within(7).shuffle().poll().click(true);
        Condition.sleep(300);
    }

}



