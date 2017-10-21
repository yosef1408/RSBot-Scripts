package thebonobo.SalmonCollector;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Magic;
import thebonobo.listeners.EventDispatcher;
import thebonobo.listeners.InventoryEvent;
import thebonobo.listeners.InventoryListener;
import thebonobo.SalmonCollector.tasks.*;
import thebonobo.SalmonCollector.utils.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

@Script.Manifest(name = "SalmonCollector",
        description = "Goes to Barbarian Village, picks up salmon and banks it at Edgeville Bank",
        properties = "client=4; topic=1337839; author=thebonobo;")

public class SalmonCollector extends PollingScript<ClientContext> implements MessageListener, PaintListener, InventoryListener {
    private static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);
    private Properties userProperties;
    private boolean firstLogin = true;


    private List<Task> taskList = new ArrayList<>();
    private EventDispatcher eventDispatcher;

    private static String timeConversion(long seconds) {
        long minutes = seconds / 60;
        long hours = minutes / 60;
        seconds -= minutes * 60;
        minutes -= hours * 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void start() {
        userProperties = new UserProfile(ctx).getUserProperties();
        eventDispatcher = new EventDispatcher(ctx);
        eventDispatcher.addListener(this);
        Info.getInstance().setStartTime(System.currentTimeMillis() / 1000);
        taskList.addAll(Arrays.asList(new WalkToBarbVillage(ctx), new Loot(ctx, userProperties), new WalkToBank(ctx), new Bank(ctx, userProperties)));
        ctx.input.speed(Integer.parseInt(userProperties.getProperty("click-speed", Integer.toString(Random.nextInt(0,100)))));
    }

    @Override
    public void poll() {
        if (firstLogin && ctx.game.loggedIn()){
            if (!thebonobo.Paths.LUMBRIDGE_CASTLE_AREA.contains(ctx.players.local()) && !thebonobo.Paths.BARBARIAN_AREA.contains(ctx.players.local())){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        ctx.magic.cast(Magic.Spell.HOME_TELEPORT);
                        ctx.widgets.select().id(218).poll().component(1).click();
                        System.out.println("Teleporting to Lumbridge.");
                        return (thebonobo.Paths.LUMBRIDGE_CASTLE_AREA.contains(ctx.players.local()));
                    }
                },30000,3);

            }
            this.firstLogin = false;
        } else {
            Antiban.dismissRandomEvent(ctx);
            Antiban.degradationSleep();
            for (Task t : taskList) {
                if (t.activate()) {
                    t.execute();
                }
            }
        }
    }

    @Override
    public void stop() {
        log.info("finished");
        eventDispatcher.setRunning(false);
    }

    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;
        g.setFont(TAHOMA);

        int gp = Info.getInstance().getSalmonCollected() * Info.getInstance().getSalmonPrice() + Info.getInstance().getRawSalmonCollected() * Info.getInstance().getRawSalmonPrice();
        int gpPerHour = (int) ((gp * 3600000D) / getRuntime());

        g.drawString("Runtime: " + timeConversion(getRuntime() / 1000), 10, 30);
        g.drawString("Current Task: " + Info.getInstance().getCurrentTask(), 10, 45);
        g.drawString(String.format("Salmon collected: %,d", Info.getInstance().getSalmonCollected()), 10, 60);
        g.drawString(String.format("Raw Salmon collected: %,d", Info.getInstance().getRawSalmonCollected()), 10, 75);
        g.drawString(String.format("GP (GP/HR): %,d (%,d)", gp, gpPerHour), 10, 90);
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        final String msg = messageEvent.text().toLowerCase();
        //when it missclicks a tree."
        if (msg.contains("You do not have an axe")) {
            //turn camera
            ctx.camera.angle((ctx.camera.x() + Integer.parseInt(userProperties.getProperty("camera-turn-rate"))) % 360);
        }
    }

    @Override
    public void onInventoryChange(InventoryEvent inventoryEvent) {
        int id = inventoryEvent.getNewItem().id();
        if(id == Items.SALMON)
            Info.getInstance().incrementSalmon(1);
        else if(id == Items.RAW_SALMON)
            Info.getInstance().incrementRawSalmon(1);
    }
}

