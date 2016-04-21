package lugge.scripts.rs3.luckykebbit;

import lugge.scripts.rs3.luckykebbit.task.Actions;
import lugge.scripts.rs3.luckykebbit.task.Conditions;
import lugge.scripts.rs3.luckykebbit.util.Paint;
import lugge.scripts.rs3.luckykebbit.util.Time;
import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import java.awt.*;

@Script.Manifest(name = "LuckyKebbit", description = "hunts polar kebbits for profit", properties = "client=6; topic=1309379;")

public class LuckyKebbit extends PollingScript<ClientContext> implements PaintListener, MessageListener {

    Conditions Conditions = new Conditions(ctx);
    Actions Actions = new Actions(ctx);
    Paint Paint = new Paint();
    Time Time = new Time();

    Image paintImage;

    @Override
    public void start() {
        Actions.status = "starting";
        paintImage = Paint.getImage("http://img1.picload.org/image/rgrwcroi/final.jpg");
    }

    @Override
    public void poll() {
        switch (Conditions.getCurrentState()) {
            case START_TRACK:
                Actions.startTrack();
                break;
            case FOLLOW_TRACK:
                Actions.followTrack();
                break;
            case CLEAN_INVENTORY:
                Actions.cleanInventory();
                break;
            case WALK_BANK:
                Actions.walkBank();
                break;
            case BANK:
                Actions.bank();
                break;
            case WALK_LOCATION:
                Actions.walkLocation();
                break;
        }
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        if (messageEvent.text().contains("You manage to noose")) {
            Paint.huntedKebbits++;
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        for (GameObject gameObject : Actions.objects) {
            if (gameObject == Actions.nextObject) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.RED);
            }
            Point p = gameObject.centerPoint();
            g.fillOval(p.x, p.y, 25, 25);
        }

        g.setFont(Paint.MAIN_FONT);
        g.setColor(Color.BLACK);
        g.drawImage(paintImage, 0, 0, null);
        Paint.drawMouse(g, ctx.input.getLocation());

        Paint.huntedKebbitsPerHour = (int) (Paint.huntedKebbits * 3600000.0D / getRuntime());

        g.drawString("" + Time.runtime(getRuntime()), 117, 45);
        g.drawString("" + Actions.status, 117, 80);
        g.drawString("1.0", 269, 43);
        g.drawString("" + Paint.huntedKebbits, 363, 45);
        g.drawString("" + Paint.huntedKebbitsPerHour, 363, 80);
    }
}