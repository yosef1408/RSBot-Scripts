package superchaoran.Herbs;

/**
 * Created by chaoran on 5/11/16.
 */

import org.powerbot.script.*;
import org.powerbot.script.rt6.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;

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
        name = "Make Unf Portion", properties = "author=superchaoran; topic=-4; client=6;",
        description = "Make Unf Portion"
)
public class makeUnfPortion extends PollingScript<ClientContext> implements PaintListener {

    /*500g, 2kg and 5kg*/
    private static final long startTime = System.currentTimeMillis();
    private static String status ="Starting";
    private static int totalMix = 0;
    private static int cleanHerbID = 257;
    private static int vialOfWaterID = 227;
    private static int unfID = 99;
    private static GeItem cleanHerb = new GeItem(cleanHerbID);
    private static GeItem vialOfWater = new GeItem(vialOfWaterID);
    private static GeItem unf = new GeItem(unfID);

    private Npc banker;

    @Override
    public void start() {

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
                ctx.bank.depositInventory();



                log.info("Withdrawing...");
                status = "Withdrawing...";
                ctx.bank.withdraw(cleanHerbID, 14);
                ctx.bank.withdraw(vialOfWaterID, 14);
                ctx.bank.close();
                break;

            case Clean:

                log.info("Open Backpack");
                status = "Open Backpack";
                if(!ctx.hud.opened(Hud.Window.BACKPACK)){
                    ctx.hud.open(Hud.Window.BACKPACK);
                }

                //cleaning herb
                log.info("mixing herb with water");
                status = "mixing herb with water";
                ctx.backpack.select().id(cleanHerbID).poll().interact("Use");
                ctx.backpack.select().id(vialOfWaterID).poll().interact("Use");
                //Confirm
                status = "Confirm mixing";
                if (Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        return ctx.widgets.component(1370, 20).visible();
                    }
                }, 20, 50*3)) {
                    ctx.input.send(" ");
                }

                //wait for completion
                status = "Wait for mixing...";
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return ctx.backpack.select().id(unfID).count() == 14;
                    }
                }, 20, 50 * 10);//maybe put a short time here makes it faster to acess bank, which is a buggy good thing

                totalMix += 14;
                break;
        }
    }

    private State state() {
        if(ctx.backpack.select().id(cleanHerbID).count()!=14 || ctx.backpack.select().id(vialOfWaterID).count() != 14){
            return State.Bank;
        } else {
            return State.Clean;
        }
    }

    private enum State {
        Clean, Bank
    }

    Font font = new Font("Arial", Font.PLAIN, 10);
    Color background = new Color(0, 0, 0, 150);
    public void repaint(Graphics graphics) {
        int unitProfit = unf.price - cleanHerb.price - vialOfWater.price;
        graphics.setFont(font);
        graphics.setColor(background);
        graphics.drawRect(0, 0, 200, 100);
        graphics.fillRect(0, 0, 200, 100);
        graphics.setColor(Color.WHITE);
        graphics.getFont();
        graphics.drawString("Herb Mixer", 5, 15);
        int runtime = Integer.parseInt("" + (System.currentTimeMillis()- startTime));
        graphics.drawString("Run time: " + timeFormat(runtime), 105, 15);
        graphics.drawString("Status: " + status, 5, 40);
        graphics.drawString("Total Mix: " + totalMix, 5, 53);
        graphics.drawString("Mixes/h: " + (int)((3600000D*totalMix) / (System.currentTimeMillis() - startTime)), 5, 68);
        graphics.drawString("Profit/Clean: " + unitProfit, 105, 53);
        graphics.drawString("Total profit: " + unitProfit*totalMix, 105, 68);
        graphics.drawString("Profit/h: " + (int)((3600000D*(unitProfit*totalMix)) / (System.currentTimeMillis() - startTime)), 105, 83);
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
