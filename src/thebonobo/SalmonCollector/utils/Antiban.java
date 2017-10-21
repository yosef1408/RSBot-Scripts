package thebonobo.SalmonCollector.utils;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

public class Antiban {

    public static void wait(int min,int max){
        try {

            Thread.sleep((long) (Random.nextInt(min, max)));

        } catch (InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void walkWait(){
        try {
            Thread.sleep(Random.nextInt(0, 200));
        } catch (InterruptedException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    public static void runAntiban(ClientContext ctx){
        int randNum = Random.nextInt(1, 60);
        if(randNum == 7) rightClickObject(ctx);
        if(randNum == 6) moveMouseOffScreen(ctx);
        if(randNum == 5) rightClickPlayer(ctx);
        if(randNum <= 3) randomMouseMovement(ctx,100,300);

    }

    public static void rightClickObject(ClientContext ctx) {
        GameObject object = ctx.objects.select(5).nearest().poll();
        if (object.valid()){
            object.hover();
            ctx.input.click(false);
        }
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
    public static void randomMouseMovement(ClientContext ctx, int minDistance, int maxDistance) {
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
    public static void moveMouseOffScreen(ClientContext ctx){
        ctx.input.hop((int) (Random.nextInt(10, 100) + ctx.game.dimensions().getWidth()),
                Random.nextInt(-10, (int)(ctx.game.dimensions().getHeight() + 10)));
    }

    public static void rightClickPlayer(ClientContext ctx){
        Condition.sleep(100);
        ctx.players.select().within(7).shuffle().poll().click(false);
        Condition.sleep(1300);
        randomMouseMovement(ctx,100,250);
    }

    public static void dismissRandomEvent(ClientContext ctx) {
        /* Credit to @laputa.  URL: https://www.powerbot.org/community/topic/1292825-random-event-dismisser/  */
        Npc randomNpc = ctx.npcs.select().within(4).select(npc -> npc.overheadMessage().contains(ctx.players.local().name())).poll();
        if (randomNpc.valid()) {
            String action = randomNpc.name().equalsIgnoreCase("genie") ? "Talk-to" : "Dismiss";
            try {
                TimeUnit.MILLISECONDS.sleep((long) (Random.nextDouble(2, 7.5) * 1000));
            } catch (InterruptedException e) {
                e.getMessage();
            }
            randomNpc.interact(action);
        }
    }

    public static void degradationSleep(){
        // degredation
        long sessionTime = (System.currentTimeMillis() / 1000) - Info.getInstance().getStartTime();
        // After 1 hour, every poll (300 ms) wait 100ms, after 3 hours slowest at -50% efficiency
        Condition.sleep(((int) sessionTime / 36) % 300);
    }
}



