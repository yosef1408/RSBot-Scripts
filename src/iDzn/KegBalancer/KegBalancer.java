package iDzn.KegBalancer;


import iDzn.KegBalancer.Tasks.*;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


@Script.Manifest(name= "KegBalancer", description="Trains with Kegs in Warriors Guild", properties="client=4; author=iDzn; topic=1340866;")
public class KegBalancer extends PollingScript<ClientContext> implements PaintListener {


    private Image bg = null;

    public int FOOD=0, lvlStart, lvlGained, maxHealth, currentHealth, eatPercentage, xpGained, xpStart, xpCurrent, levelCurrent, PaintXpGained, time = 0, startTime = 0, time1 = 0, time2 = 0, time3 = 0, time4 = 0;

    public long getRunTime() {
        return getRuntime();
    }

    private ArrayList<Task> taskList = new ArrayList<Task>();

    @Override
    public void start() {
        bg = downloadImage("http://i.imgur.com/mKfDqST.png");
        xpStart = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        lvlStart = ctx.skills.realLevel(Constants.SKILLS_STRENGTH);

        String userOptions[] = {"Cake", "Tuna", "Lobster", "Swordfish", "Monkfish", "Shark"};
        String userChoice = "" + (String) JOptionPane.showInputDialog(null, "Choose your food", "KegBalancer", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);

        taskList.add(new KegMe(ctx));
        taskList.add(new Bank(ctx));
        taskList.add(new Walk(ctx));
        taskList.add(new Energy(ctx));
        taskList.add(new Eat(ctx, KegBalancer.this));
        taskList.add(new Anti(ctx));
        taskList.add(new Withdraw(ctx, KegBalancer.this));

        if (userChoice.equals("Cake")) {
           FOOD = 1891;
        } else if (userChoice.equals("Tuna")) {
           FOOD = 361;
        } else if (userChoice.equals("Lobster")) {
            FOOD = 379;
        } else if (userChoice.equals("Monkfish")) {
            FOOD = 7946;
        } else if (userChoice.equals("Swordfish")) {
            FOOD = 373;
        } else if (userChoice.equals("Shark")) {
           FOOD = 385;
        } else {
            ctx.controller.stop();

        }
    }

    private void split(long milliseconds) {
        time1 = (int) ((milliseconds / (1000 * 60 * 60 * 24)) % 7);
        time2 = (int) (milliseconds / 1000) % 60;
        time3 = (int) ((milliseconds / (1000 * 60)) % 60);
        time4 = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
    }

    @Override
    public void poll() {
    maxHealth = ctx.skills.realLevel(Constants.SKILLS_HITPOINTS);
    currentHealth = ctx.combat.health();
    eatPercentage = (int) (maxHealth * 0.7);

    for (Task task : taskList) {
        if (ctx.controller.isStopping()) {
            break;
        }
        if (task.activate()) {
            task.execute();

        }


    }

}


    @Override
    public void repaint(Graphics graphics) {
        levelCurrent = ctx.skills.realLevel(Constants.SKILLS_STRENGTH);
        xpCurrent = (ctx.skills.experience(Constants.SKILLS_STRENGTH));
        xpGained = xpCurrent - xpStart;
        PaintXpGained = xpCurrent - xpStart;
        lvlGained = levelCurrent-lvlStart;
        double xpPerHour = (long) ((xpGained * 3600000D) /time);
        long xpNextLevel = (ctx.skills.experienceAt(levelCurrent + 1) -xpCurrent);
        long xpBetween = (ctx.skills.experienceAt(levelCurrent + 1)-ctx.skills.experienceAt(levelCurrent));
        double xpDone = xpBetween - xpNextLevel;
        double xpPart = xpDone/xpBetween;
        int percentageDone = (int)(xpPart*Math.pow(10,2));



        time = (int) (getRuntime() - startTime);
        time1 = time;

        Graphics2D g = (Graphics2D) graphics;


        g.setColor(new Color(0, 0, 0, 255));
        g.fillRect(381, 442, 100, 15);
        g.drawRect(381, 442, 100, 15);
        g.setColor(new Color(0, 255, 3, 230));
        g.fillRect(381, 442, percentageDone, 15);
        g.drawRect(381, 442, percentageDone, 15);

        g.setColor(new Color(255,255,255, 255));
        g.setFont(new Font("Impact", Font.PLAIN, 14));
        g.drawString(" " + percentageDone +"%", 420, 456);

        g.drawImage(bg, 6, 269, null);
        g.setFont(new Font("Impact", Font.PLAIN, 17));
        g.setColor(new Color(246, 255, 239));
        split(time);
        g.drawString("" + String.format("%02d:%02d:%02d", time4, time3, time2), 354, 408);
        g.drawString(" " + xpGained, 130, 409);
        g.setColor(new Color(237, 255, 220));
        g.drawString(" " + lvlGained, 400, 432);
        g.drawString(" " +(long) xpPerHour, 123, 433);
        g.setColor(new Color(227, 255, 201));
        if (levelCurrent!=0){
            g.drawString(" " +levelCurrent, 352, 457);
            g.drawString(" " +(levelCurrent+1), 488, 457);
        } else {
            g.drawString(" 0" ,353, 457);
            g.drawString(" 0", 487, 457);
        }
        g.drawString(" " + xpNextLevel, 158, 456);

    }
}


