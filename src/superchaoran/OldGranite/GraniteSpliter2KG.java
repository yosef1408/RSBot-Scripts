package superchaoran.OldGranite;

/**
 * Created by chaoran on 5/11/16.
 */

import org.powerbot.script.Script;
import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;
import org.powerbot.script.rt6.Hud;
import org.powerbot.script.rt6.Npc;

import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.powerbot.script.*;
import org.powerbot.script.rt6.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;

import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Created by chaoran on 5/10/16.
 */
@Script.Manifest(
        name = "Granite Splitter 2KG", properties = "author=Chaoran Wang; topic=-1; client=6;",
        description = "Splitting Granite 2kg to 500g and make huge profit off it!"
)


public class GraniteSpliter2KG extends PollingScript<ClientContext> implements PaintListener {

    /*500g, 2kg and 5kg*/
    private static int[] graniteIDs = {6979, 6981};
    private static final long startTime = System.currentTimeMillis();
    private static String status ="Starting";
    private static int totalCrafts = 0;
    private static GeItem granite500g = new GeItem(graniteIDs[0]);
    private static GeItem granite2kg = new GeItem(graniteIDs[1]);

    private Npc banker;

    @Override
    public void start() {
//        for (final Item i : ctx.backpack.select()) {
//            log.info(""+ i.id());
//
//        }
//
//        ctx.backpack.select().id(graniteIDs[0]).each(new Filter<Item>() {
//            @Override
//            public boolean accept(Item item) {
//                return item.interact("Craft");
//            }
//        });
        log.info("Find nearest Banker");
        status = "Find nearest Banker";
        banker = ctx.npcs.select().name("Banker").nearest().poll();
        if (!banker.inViewport()) {
            log.info("Banker not in viewport");
            status = "Banker not in viewport";
            ctx.movement.step(banker.tile());
            log.info("Walking to banker");
            status = "Walking to banker";
            if (banker.valid()) {
                log.info("Turn to banker");
                status = "Turn to banker";
                ctx.camera.turnTo(banker);
            } else {
                log.info("Banker not valid");
                status = "Banker not valid";
            }
        }
    }

    @Override
    public void poll() {

        switch (state()) {
            case Bank:

                //                if (!ctx.bank.inViewport()) {
//                    ctx.movement.step(BANK_TILE);
//                    ctx.camera.turnTo(BANK_TILE);
//                    Condition.wait(new Callable<Boolean>() {
//                        @Override
//                        public Boolean call() throws Exception {
//                            return BANK_TILE.distanceTo(ctx.players.local()) < 5;
//                        }
//                    }, 250, 10);
                log.info("Wait for Bank to open");
                status = "Wait for Bank to open";
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return ctx.bank.open();
                    }
                }, 20, 50 * 3);

                log.info("Wait for DepositInventory");
                status = "Wait for DepositInventory";
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        return ctx.bank.depositInventory();
                    }
                });


                //Not enough granite 5kg, stopping script..."
//                if (ctx.bank.select().id(graniteIDs[2]).count() < 3) {
//                    log.info("Not enough granite");
//                    ctx.controller.stop();
//                } else {
                log.info("Withdrawing...");
                status = "Withdrawing...";
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        return ctx.bank.withdraw(graniteIDs[1], 7);
                    }
                });
//                }


                log.info("Wait for bank close");
                status = "Wait for bank close...";
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return ctx.bank.close();
                    }
                }, 20, 50 * 3);

                break;

            case Split:

                log.info("Open Backpack");
                status = "Open Backpack";
                if(!ctx.hud.opened(Hud.Window.BACKPACK)){
                    ctx.hud.open(Hud.Window.BACKPACK);
                }

                //craft 2kg granite
                log.info("Craft 2kg");
                status = "Craft 2kg";
                ctx.backpack.select().id(graniteIDs[1]).poll().interact("Craft");

                //Confirm
                status = "Confirm crafting";
                if (Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        return ctx.widgets.component(1370, 20).visible();
                    }
                }, 20, 50*3)) {
                    ctx.input.send(" ");
                }

                //wait for completion
                status = "Wait for crafting 2kg";
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return ctx.backpack.select().count() == 28;
                    }
                }, 20, 50 * 10);

                log.info("completed");
                status = "Completed a backpack cycle";
                totalCrafts += 7;
                break;
        }
    }

    private State state() {
        int count = ctx.backpack.select().count();
        if(ctx.backpack.select().count()!=7 || ctx.backpack.select().id(graniteIDs[1]).count() != 7){
            return State.Bank;
        } else {
            return State.Split;
        }
    }

    private enum State {
        Split, Bank
    }

    Font font = new Font("Arial", Font.PLAIN, 10);
    Color background = new Color(0, 0, 0, 150);
    public void repaint(Graphics graphics) {
        int unitProfit = granite500g.price *4 - granite2kg.price;
        graphics.setFont(font);
        graphics.setColor(background);
        graphics.drawRect(0, 0, 200, 100);
        graphics.fillRect(0, 0, 200, 100);
        graphics.setColor(Color.WHITE);
        graphics.getFont();
        graphics.drawString("Granite Splitter", 5, 15);
        int runtime = Integer.parseInt("" + (System.currentTimeMillis()- startTime));
        graphics.drawString("Run time: " + timeFormat(runtime), 105, 15);
        graphics.drawString("Status: " + status, 5, 40);
        graphics.drawString("Total Craft: " + totalCrafts, 5, 53);
        graphics.drawString("Crafts/h: " + (int)((3600000D*totalCrafts) / (System.currentTimeMillis() - startTime)), 5, 68);
        graphics.drawString("Profit/Granite(2kg): " + unitProfit, 105, 53);
        graphics.drawString("Total profit: " + unitProfit*totalCrafts, 105, 68);
        graphics.drawString("Profit/h: " + (int)((3600000D*(unitProfit*totalCrafts)) / (System.currentTimeMillis() - startTime)), 105, 83);
    }

    private String timeFormat(long duration) {
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        if (days == 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
    }


}
