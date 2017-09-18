package sscripts.sgaltar;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import sscripts.sgaltar.gui.Gui;
import sscripts.sgaltar.tasks.Task;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Script.Manifest(name = "SGAltar", description = "Gilded Altar Bot for Yanille!", properties = "author=sscripts; topic=1338021; client=4")
public class SGAltar extends PollingScript<ClientContext> implements PaintListener, MessageListener{

    public static int boneID, bonesBurned, startEXP, startLVL, expGain, failSaves;

    public List<Task> tasks = Collections.synchronizedList(new ArrayList<Task>());

    public static String status = "Waiting for Input";
    public static String playername;
    public static boolean inHouse = false;
    public static boolean failSave = false;
    public static boolean stop = false;
    private long startTime;

    @Override
    public void start() {
        startLVL = ctx.skills.level(Constants.SKILLS_PRAYER);
        startEXP = ctx.skills.experience(Constants.SKILLS_PRAYER);
        startTime = System.currentTimeMillis();
        final GameObject altar = ctx.objects.select().id(13199).nearest().poll();
        final GameObject portal = ctx.objects.select().id(4525).nearest().poll();

        if (altar.inViewport() || portal.inViewport()){
            inHouse = true;
        }
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
        if (msg.text().contains("private")){
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
        g.setColor(Color.RED);
        g.drawString("RunTime: " + formatTime(getTotalRuntime()),50,30);
        g.drawString("Status: " + status, 50, 50);
        g.drawString("Bone-ID: " + boneID, 50, 70);
        g.drawString("Host-Name: " + playername, 50, 90);
        g.drawString("inHouse = " + inHouse, 50, 110);
        g.drawString("Bones used: " + bonesBurned, 50, 130);
        g.drawString("Prayer Lvl´s: " + (ctx.skills.level(Constants.SKILLS_PRAYER)-startLVL),50,150);
        g.drawString("Prayer EXP (H): "+ expGain + " ("+perHour(expGain)+")",50,170);
        g.drawString("Version: 1.0 new Paint/Gui coming soon", 50, 190);
        //g.drawString("Fail-Save = " + failSave + " ("+failSaves+")",50,190);
    }
}
