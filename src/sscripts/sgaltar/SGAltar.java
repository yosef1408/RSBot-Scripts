package sscripts.sgaltar;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import sscripts.sgaltar.gui.Gui;
import sscripts.sgaltar.tasks.Task;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Script.Manifest(name = "SGAltar", description = "Gilded Altar Bot for Yanille! Fast Prayer EXP", properties = "author=sscripts; topic=1338021; client=4")
public class SGAltar extends PollingScript<ClientContext> implements PaintListener, MessageListener{

    public static int boneID, bonesBurned, startEXP, startLVL, expGain, failSaves, floor;

    public final List<Task> tasks = Collections.synchronizedList(new ArrayList<Task>());

    public static String status = "Waiting for Input";
    public static String playername;
    public static boolean failSave = false;
    public static boolean stop = false;
    private long startTime;

    @Override
    public void start() {
        startLVL = ctx.skills.level(Constants.SKILLS_PRAYER);
        startEXP = ctx.skills.experience(Constants.SKILLS_PRAYER);
        startTime = System.currentTimeMillis();
        floor = ctx.client().getFloor();

            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Gui(ctx, tasks);
                }
            });
    }

    @Override
    public void stop() {

    }

    @Override
    public void messaged(MessageEvent msg) {
        if (msg.text().contains("The gods are")){
            bonesBurned++;
            failSave = false;
        }
        if (msg.text().contains("Nothing interesting")){
            failSave = true;
            failSaves++;
            ctx.camera.angle(org.powerbot.script.Random.nextInt(100,250));
            ctx.camera.pitch(99);
        }
        if (msg.text().contains("That player is offline")){
            stop = true;
        }
    }

    @Override
    public void poll() {

        synchronized(tasks) {
            if (tasks.size() == 0) {
                try {
                    tasks.wait();
                } catch (InterruptedException ignored) {}
            }
        }
        for (Task task : tasks) {
            if (task.activate()) {
                task.execute();
            }
        }
    }

    private String formatTime(final long time) {
        final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
        return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
    }
    private int perHour(int value) {
        return (int) ((value) * 3600000D / (System.currentTimeMillis() - startTime));
    }



    @Override
    public void repaint (Graphics g) {
        expGain =  ctx.skills.experience(Constants.SKILLS_PRAYER) - startEXP;
        g.setColor(Color.BLUE);
        g.fillRect(10,10,175,170);

        g.setColor(Color.WHITE);
        g.drawString("RunTime: " + formatTime(getTotalRuntime()),20,30);
        g.drawString("Status: " + status, 20, 50);
        g.drawString("Bone-ID: " + boneID, 20, 70);
        g.drawString("Host-Name: " + playername, 20, 90);
        g.drawString("Bones used: " + bonesBurned, 20, 110);
        g.drawString("Prayer Lvl´s: " + (ctx.skills.level(Constants.SKILLS_PRAYER)-startLVL),20,130);
        g.drawString("Prayer EXP (H): "+ expGain + " ("+perHour(expGain)+")",20,150);
        g.drawString("Version: 1.0 ", 20, 170);
        //g.drawString("Fail-Save = " + failSave + " ("+failSaves+")",50,190);
        g.setColor(Color.WHITE);
        g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y - 5, ctx.input.getLocation().x, ctx.input.getLocation().y + 5);
        g.drawLine(ctx.input.getLocation().x - 5, ctx.input.getLocation().y, ctx.input.getLocation().x + 5, ctx.input.getLocation().y);
    }


}
