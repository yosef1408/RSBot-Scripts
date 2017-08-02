package scripts.ArrowFletcher;

import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.GeItem;
import scripts.ID;

import java.util.Random;
import java.util.concurrent.Callable;

import static java.lang.Math.min;

public class ShaftFletch extends Task<ClientContext> {
    private static int resourceID1 = ID.SHAFT;
    private static int resourceID2 = ID.FEATHER;
    private static int productID = ID.HEADLESS;

    private static int resourceLeft1;   // shaft
    private static int resourceLeft2;   // feather
    private static int productDone;     // headless arrow


    private static int limit = -1;

    private GeItem resourceGE1;
    private GeItem resourceGE2;
    private GeItem productGE;

    public ShaftFletch(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void initialise() {
        resourceGE1 = new GeItem(resourceID1);
        resourceGE2 = new GeItem(resourceID2);
        productGE = new GeItem(productID);
        resourceLeft1 = ctx.inventory.select().id(resourceID1).count(true);
        resourceLeft2 = ctx.inventory.select().id(resourceID2).count(true);
        if (limit > min(resourceLeft1, resourceLeft2) || limit < 0) limit = min(resourceLeft1, resourceLeft2);
    }

    @Override
    public boolean activate() {
        return resourceLeft1 > 0 &&
                resourceLeft2 > 0 &&
                productDone < limit;
    }

    @Override
    public String getName() {
        return "ShaftFletch";
    }

    @Override
    public String getActionName() {
        return "Shafts made " + productDone + "/" + limit;
    }

    @Override
    public void setLimit(int num) {
        limit = num;
    }

    @Override
    public int profit() {
        int margin = productGE.price - (resourceGE2.price + resourceGE1.price);
        int profit = productDone *margin;
        return profit;
    }


    @Override
    public void execute() {
        action();
    }

    @Override
    public void message(MessageEvent me) {
        if (me.text().contains("You attach feathers to")) {
            resourceLeft2 -= 15;
            resourceLeft1 -= 15;
            productDone += 15;
        }
    }

    private void action() {
        closeBank();
        ctx.game.tab(Game.Tab.INVENTORY);
        final Item resource1 = ctx.inventory.select().id(resourceID1).poll();
        final Item resource2 = ctx.inventory.select().id(resourceID2).poll();
        resource1.interact("Use");
        resource2.interact("Use");
        ctx.widgets.component(162, 40).interact("Make 10");
        final int temp = productDone;
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return productDone - temp >= 150;
            }
        }, 1000, 8);
    }
}