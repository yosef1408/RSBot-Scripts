package thebonobo.SalmonCollector;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import thebonobo.SalmonCollector.listeners.EventDispatcher;
import thebonobo.SalmonCollector.listeners.InventoryEvent;
import thebonobo.SalmonCollector.listeners.InventoryListener;
import thebonobo.SalmonCollector.tasks.*;
import thebonobo.SalmonCollector.utils.Info;
import thebonobo.SalmonCollector.utils.Items;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

@Script.Manifest(name = "SalmonCollector",
        description = "Goes to Barbarian Village, picks up cooked fish and banks it at Edgeville Bank",
        properties = "client=4; author=thebonobo;")

public class Main extends PollingScript<ClientContext> implements MessageListener, PaintListener, InventoryListener {
    public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);


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
        eventDispatcher = new EventDispatcher(ctx);
        eventDispatcher.addListener(this);
        taskList.addAll(Arrays.asList(new WalkToBarbVillage(ctx), new Loot(ctx), new WalkToBank(ctx), new Bank(ctx)));
        Info.getInstance().setStartTime(System.currentTimeMillis() / 1000);
    }

    @Override
    public void poll() {
        dismissRandomEvent();
        for (Task t : taskList) {
            if (t.activate()) {
                t.execute();
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

    public void dismissRandomEvent() {
        /* Credit to @laputa.  URL: https://www.powerbot.org/community/topic/1292825-random-event-dismisser/  */
        Npc randomNpc = ctx.npcs.select().within(4.4).select(new Filter<Npc>() {

            @Override
            public boolean accept(Npc npc) {
                return npc.overheadMessage().contains(ctx.players.local().name());
            }
        }).poll();

        if (randomNpc.valid()) {
            String action = randomNpc.name().equalsIgnoreCase("genie") ? "Talk-to" : "Dismiss";
            try {
                TimeUnit.MILLISECONDS.sleep((long) (org.powerbot.script.Random.nextDouble(3, 3.5) * 1000));
            } catch (InterruptedException e) {
                e.getMessage();
            }
            randomNpc.interact(action);
        }
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        final String msg = messageEvent.text().toLowerCase();
        //when it missclicks a tree."
        if (msg.contains("You do not have an axe")) {
            //turn camera
            ctx.camera.angle((ctx.camera.x() + Random.nextInt(45, 90)) % 360);
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

