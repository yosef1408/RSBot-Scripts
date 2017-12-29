package iDzn.ConstructUp.Tasks;

import iDzn.ConstructUp.ConstructUp;
import iDzn.ConstructUp.Task;
import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import java.awt.*;
import java.util.concurrent.Callable;

public class Phials  extends Task<ClientContext> {

    ConstructUp main;

    public Phials(ClientContext ctx, ConstructUp main) {


        super(ctx);
        this.main = main;

    }
    GameObject PortalOutHouse = ctx.objects.select().id(15478).nearest().poll();
    Area PhialsArea = new Area(new Tile(2945, 3209, 0), new Tile(2955, 3228, 0));
    Area ShopArea = new Area(new Tile(2950, 3219, 0), new Tile(2945, 3211, 0));
    Tile PhialsTiles = new Tile(2949, 3213, 0);
    Tile POHtile = new Tile(2953, 3223, 0);
    private Npc Phials;



    @Override
    public boolean activate() {
        return ShopArea.contains(ctx.players.local()) || PhialsArea.contains(ctx.players.local()) && ctx.inventory.select().count() < 28;
    }

    @Override
    public void execute() {
        Random rando = new Random();
        Phials = ctx.npcs.select().name("Phials").nearest().poll();
        Item nPlank = ctx.inventory.select().id(main.nPlanks).poll();
        if (!ctx.game.tab(Game.Tab.INVENTORY)) {
            ctx.game.tab(Game.Tab.INVENTORY);
        }
        if (ctx.inventory.select().id(995).poll().stackSize() > 2000 && !ShopArea.contains(ctx.players.local())) {
            ctx.movement.step(PhialsTiles);
            ctx.camera.turnTo(Phials);
            System.out.println("Locating Phials");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return (!ctx.players.local().inMotion());
                }
            }, 200, 10);
        }
        if (ShopArea.contains(ctx.players.local())
                && !ctx.widgets.widget(219).component(3).visible()
                && !ctx.players.local().inMotion()
                && !Phials.inMotion()
                && ctx.inventory.select().count() < 28) {
            System.out.println("Selecting planks to un-note");
            nPlank.interact("Use");
            Phials.click();
            System.out.println("Using on Phials");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.component(219, 0).visible();
                }
            }, 150, 10);
        }
        if (ctx.widgets.component(219, 0).visible()) {
            int h = Random.nextInt(185, 337);
            int v = Random.nextInt(412, 421);
            ctx.input.click(new Point(h, v), true);
            System.out.println("Paying the gentleman");
            Condition.sleep(rando.nextInt(800, 1500));
            ctx.camera.turnTo(POHtile);
            ctx.movement.step(POHtile);
            System.out.println("Locating Portal Outside");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return PortalOutHouse.inViewport();
                }
            }, 350, 10);
        }
    }
}
