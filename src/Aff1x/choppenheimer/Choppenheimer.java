package Aff1x.choppenheimer;

import Aff1x.choppenheimer.Util.Config;
import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;

import Aff1x.choppenheimer.Util.TreeEnum;
import Aff1x.choppenheimer.Util.GUI;

import Aff1x.choppenheimer.Tasks.*;
import org.powerbot.script.rt6.Constants;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

@Script.Manifest(
        name = "Choppenheimer",
        properties = "author=Affix; topic=1286161; client=6;",
        description = "Chopping logs at Seers Village")

public class Choppenheimer extends PollingScript<ClientContext> implements MessageListener, PaintListener {

    public static List<Task> taskList = new ArrayList<Task>();
    private int numLogs = 0;
    public static long startTime;
    private TreeEnum.TreeType treeType;
    private GUI gui = new GUI(ctx);
    public static Config config = new Config();


    private int exp, level, expGained, lvsGained;

    @Override
    public void start() {
        gui.setVisible(true);

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return gui.guiDone;
            }
        }, 500, 500);

        System.out.println(config.getTreeType().name[0]);

        startTime = System.currentTimeMillis();

        exp = ctx.skills.experience(Constants.SKILLS_WOODCUTTING);
        level = ctx.skills.level(Constants.SKILLS_WOODCUTTING);
    }

    @Override
    public void poll() {
        for (Task task: taskList)
        {
            if(task.activate())
            {
                task.execute();
            }
        }
    }

    @Override
    public void messaged(MessageEvent e) {
        String msg = e.text().toLowerCase();
        if (msg.contains("you get some")) {
            numLogs++;
        }
    }

    @Override
    public void repaint(Graphics g) {

        g.setColor(new Color(90, 12, 8,180));
        g.fillRect(9, 26, 150, 20);
        g.fillRect(9, 27, 150, 126);
        g.setColor(Color.BLACK);
        g.drawRect(8, 25, 151, 21);
        g.drawRect(8, 25, 151, 127);

        g.setColor(Color.WHITE);
        g.drawString("Choppenheimer!", 12, 41);

        g.setColor(Color.WHITE);

        expGained = ctx.skills.experience(Constants.SKILLS_WOODCUTTING) - exp;
        lvsGained = ctx.skills.level(Constants.SKILLS_WOODCUTTING) - level;

        g.drawString("Logs Chopped: " + numLogs, 12, 66);
        g.drawString("Logs/Hour: " + perHour(numLogs), 12, 82);
        g.drawString("Runtime: " + runTime(startTime), 12, 98);
        g.drawString("XP Gained: " + expGained, 12, 114);
        g.drawString("Levels Gained: " + lvsGained , 12, 128);

        int mX = ctx.input.getLocation().x;
        int mY = ctx.input.getLocation().y;

        int pX[] = {mX, mX + 10, mX + 5, mX + 9, mX + 7, mX + 3, mX, mX};
        int pY[] = {mY, mY + 8, mY + 8, mY + 14, mY + 16, mY + 9, mY + 13, mY};

        g.setColor(Color.WHITE);
        g.fillPolygon(pX,pY,8);
        g.setColor(Color.BLACK);
        g.drawPolygon(pX,pY,8);


    }

    public TreeEnum.TreeType getTreeType() {
        return treeType;
    }

    public String runTime(long i) {
        DecimalFormat nf = new DecimalFormat("00");
        long millis = System.currentTimeMillis() - i;
        long hours = millis / (1000 * 60 * 60);
        millis -= hours * (1000 * 60 * 60);
        long minutes = millis / (1000 * 60);
        millis -= minutes * (1000 * 60);
        long seconds = millis / 1000;
        return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
    }

    public String perHour(int gained) {
        return formatNumber((int) ((gained) * 3600000D / (System.currentTimeMillis() - startTime)));
    }

    public String formatNumber(int start) {
        DecimalFormat nf = new DecimalFormat("0.0");
        double i = start;
        if(i >= 1000000) {
            return nf.format((i / 1000000)) + "M";
        }
        if(i >=  1000) {
            return nf.format((i / 1000)) + "K";
        }
        return ""+start;
    }


}