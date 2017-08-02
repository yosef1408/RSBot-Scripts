package scripts.ArrowFletcher;

import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.GeItem;
import scripts.ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import static java.lang.Math.min;

public class ArrowFletch extends Task<ClientContext> {
    private static int resourceID1 = ID.HEADLESS;
    private static int resourceID2;
    private static int[] resourceIDARRAY2 = {ID.ARROWTIPDRAGON, ID.ARROWTIPAMETHYST, ID.ARROWTIPRUNE, ID.ARROWTIPMITHRIL, ID.ARROWTIPSTEEL, ID.ARROWTIPIRON, ID.ARROWTIPBRONZE};
    private static int productID;

    private static int resourceLeft1;   // shaft
    private static int resourceLeft2;   // feather
    private static int productDone;     // headless arrow

    private static int startingExp;
    private static int expGained;
    private static int expGainedHr;

    private static int SKILLLEVEL;


    private static int limit = -1;


    public ArrowFletch(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void initialise() {
        for (int selectResource : resourceIDARRAY2) {
            if (ctx.inventory.select().id(selectResource).count(true) > 0) {
                resourceID2 = selectResource;
                break;
            }
        }
        startingExp = ctx.skills.experience(Constants.SKILLS_FLETCHING);
    }

    @Override
    public boolean activate() {
        resourceLeft1 = ctx.inventory.select().id(resourceID1).count(true);
        for (int selectResource : resourceIDARRAY2) {
            if (ctx.inventory.select().id(selectResource).count(true) > 0) {
                resourceID2 = selectResource;
                break;
            }
        }
        resourceLeft2 = ctx.inventory.select().id(resourceID2).count(true);
        return resourceLeft1 > 0 &&
                resourceLeft2 > 0;
    }

    @Override
    public String getName() {
        return "ArrowFletch";
    }

    @Override
    public String getActionName() {
        return "Arrows made " + productDone;
    }

    public static int getExperience() {
        return expGained;
    }

    @Override
    public void setLimit(int num) {
        limit = num;
    }

    @Override
    public int profit() {
//        int margin = productGE.price - (resourceGE2.price + resourceGE1.price);
//        int profit = productDone *margin;
        return 0;
    }


    @Override
    public void execute() {
        action();
        if (resourceLeft1 == 0 || resourceLeft2 == 0) epilogue();
    }

    @Override
    public void message(MessageEvent me) {
        if (me.text().contains("arrowtips to")) {
            resourceLeft2 -= 15;
            resourceLeft1 -= 15;
            productDone += 15;
            expGained = ctx.skills.experience(Constants.SKILLS_FLETCHING) - startingExp;
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