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
        name = "Herb Cleaner", properties = "author=superchaoran; topic=-3; client=6;",
        description = "Cleaning grimy herb"
)
public class HerbClean extends PollingScript<ClientContext> implements PaintListener {

    /*500g, 2kg and 5kg*/
    private static final long startTime = System.currentTimeMillis();
    private static String status ="Starting";
    private static int totalCleans = 0;
    private static int grimyHerbID = 207;
    private static int cleanHerbID = 257;

    private static GeItem grimyHerb = new GeItem(grimyHerbID);
    private static GeItem cleanHerb = new GeItem(cleanHerbID);
    private static int fillCount = 0;

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
                ctx.bank.withdraw(grimyHerbID, 28);

                log.info("Wait for bank close");
                status = "Wait for bank close...";
                ctx.bank.close();
                break;

            case Clean:

                log.info("Open Backpack");
                status = "Open Backpack";
                if(!ctx.hud.opened(Hud.Window.BACKPACK)){
                    ctx.hud.open(Hud.Window.BACKPACK);
                }

                //cleaning herb
                log.info("Clearning herb");
                status = "Clearning herb";
                ctx.backpack.select().id(grimyHerbID).poll().interact("Clean");

                //Confirm
                status = "Confirm cleaning";
                if (Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        return ctx.widgets.component(1370, 20).visible();
                    }
                }, 20, 50*3)) {
                    ctx.input.send(" ");
                }

                //wait for completion
                status = "Wait for cleaning...";
                Condition.wait(new Condition.Check() {
                    @Override
                    public boolean poll() {
                        return ctx.backpack.select().id(cleanHerbID).count() == 28;
                    }
                }, 20, 50 * 15);

                totalCleans += 28;
                break;
        }
    }

    private State state() {
        int count = ctx.backpack.select().count();
        if(ctx.backpack.select().count()!=28 || ctx.backpack.select().id(grimyHerbID).count() != 28){
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
        int unitProfit = cleanHerb.price - grimyHerb.price;
        graphics.setFont(font);
        graphics.setColor(background);
        graphics.drawRect(0, 0, 200, 100);
        graphics.fillRect(0, 0, 200, 100);
        graphics.setColor(Color.WHITE);
        graphics.getFont();
        graphics.drawString("Herb Cleaner", 5, 15);
        int runtime = Integer.parseInt("" + (System.currentTimeMillis()- startTime));
        graphics.drawString("Run time: " + timeFormat(runtime), 105, 15);
        graphics.drawString("Status: " + status, 5, 40);
        graphics.drawString("Total Clean: " + totalCleans, 5, 53);
        graphics.drawString("Cleans/h: " + (int)((3600000D*totalCleans) / (System.currentTimeMillis() - startTime)), 5, 68);
        graphics.drawString("Profit/Clean: " + unitProfit, 105, 53);
        graphics.drawString("Total profit: " + unitProfit*totalCleans, 105, 68);
        graphics.drawString("Profit/h: " + (int)((3600000D*(unitProfit*totalCleans)) / (System.currentTimeMillis() - startTime)), 105, 83);
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
