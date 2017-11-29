package iDzn.OldschoolRS.OgreCannon;

import iDzn.OldschoolRS.OgreCannon.Tasks.*;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name="OgreCannon", description="Reloads cannon at the Combat Training Camp ogres, includes different looting types", properties="Client=4; author=iDzn; topic=999;")

public class OgreCannon extends PollingScript<ClientContext> implements PaintListener {

    private Image bg = null;
    private GeItem ge;
    public int xpGained, xpStart, TorstolCount, TorstolGained, TorstolPrice, RanarrCount, RanarrGained, RanarrPrice, SnapdragonCount, SnapdragonGained, SnapdragonPrice, BallsCount, BallsUsed, BallsPrice;

    List<Task> taskList = new ArrayList<Task>();

    @Override
    public void start() {
        bg = downloadImage("http://i.imgur.com/a5l0UaK.png");
        BallsCount = ctx.inventory.select().id(2).poll().stackSize();
        xpStart = ctx.skills.experience(Constants.SKILLS_RANGE);
        RanarrCount = ctx.inventory.select().id(5295).poll().stackSize();
        TorstolCount = ctx.inventory.select().id(5304).poll().stackSize();
        SnapdragonCount = ctx.inventory.select().id(5300).poll().stackSize();

        ge = new org.powerbot.script.rt4.GeItem(5295); RanarrPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5304); TorstolPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5300); SnapdragonPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(2); BallsPrice = ge.price;

        String userOptions[] = {"No Looting", "Regular Looting", "Tele Grab Looting"};
        String userChoice = "" + (String) JOptionPane.showInputDialog(null, "Choose Looting Type?", "Looting", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);
                if (userChoice.equals("No Looting")) {
            taskList.add(new deployCannon(ctx));
            taskList.add(new fireCannon(ctx));
            taskList.add(new repairCannon(ctx));
        } else if (userChoice.equals("Regular Looting")) {
            taskList.add(new deployCannon(ctx));
            taskList.add(new fireCannon(ctx));
            taskList.add(new repairCannon(ctx));
            taskList.add(new railSqueeze(ctx));
            taskList.add(new lootSeeds(ctx));
            taskList.add(new leaveRails(ctx));
        } else if (userChoice.equals("Tele Grab Looting")) {
            taskList.add(new deployCannon(ctx));
            taskList.add(new fireCannon(ctx));
            taskList.add(new repairCannon(ctx));
            taskList.add(new Telek(ctx));
        } else {
            ctx.controller.stop();

        }

    }

    @Override
    public void poll() {

        for (Task task : taskList) {
            if (ctx.controller.isStopping()) {
                break;
            }
            if (task.activate()) {
                task.execute();
                updateBalls();
                updateRanarr();
                updateTorstol();
                updateSnapdragon();

            }
        }

    }
    private void updateRanarr(){
        int newRanarrCount = ctx.inventory.select().id(5295).poll().stackSize();
        if(newRanarrCount > RanarrCount){
            int difference = newRanarrCount - RanarrCount;
            RanarrGained += difference;
        }
        RanarrCount = newRanarrCount;
    }

    private void updateTorstol(){
        int newTorstolCount = ctx.inventory.select().id(5304).poll().stackSize();
        if(newTorstolCount > TorstolCount){
            int difference = newTorstolCount - TorstolCount;
            TorstolGained += difference;
        }
        TorstolCount = newTorstolCount;
    }

    private void updateSnapdragon(){
        int newSnapdragonCount = ctx.inventory.select().id(5300).poll().stackSize();
        if(newSnapdragonCount > SnapdragonCount){
            int difference = newSnapdragonCount - SnapdragonCount;
            SnapdragonGained += difference;
        }
        SnapdragonCount = newSnapdragonCount;
    }
    private void updateBalls(){
        int newBallsCount = ctx.inventory.select().id(2).poll().stackSize();
        if(newBallsCount < BallsCount){
            int difference = BallsCount - newBallsCount;
            BallsUsed += difference;
        }
        BallsCount = newBallsCount;
    }



    @Override
    public void repaint(Graphics graphics) {
        xpGained= ctx.skills.experience(Constants.SKILLS_RANGE)-xpStart;
        long Milliseconds = this.getTotalRuntime() / 1000,
        Seconds = Milliseconds % 60,
        Minutes = (Milliseconds / 60) % 60,
        Hours = (Milliseconds / (60 * 60)) % 24;
        int gpGained = (RanarrGained*RanarrPrice)+(SnapdragonGained*SnapdragonPrice)+(TorstolGained*TorstolPrice);
        int gpSpent = (BallsUsed*BallsPrice);
        int totalDiffGP = (gpSpent-gpGained);
        Graphics2D g = (Graphics2D) graphics;

            g.drawImage(bg, 6, 269,null);
            g.setFont(new Font("Impact", Font.PLAIN, 17));
            g.setColor(new Color(255, 248, 241));
            g.drawString("" + String.format("%02d:%02d:%02d", Hours, Minutes, Seconds), 355, 400);
            g.setColor(new Color(255, 244, 231));
            g.drawString(String.format(" "+xpGained), 130, 415);
            g.setColor(new Color(239, 219, 196));
            g.drawString(" " + ((xpGained/Milliseconds)*60)*60, 123, 446);
            g.drawString("" + gpGained,370, 430);
            g.setColor(new Color(255, 228, 231));
            g.drawString("" + totalDiffGP/xpGained,390, 460);


        }
    }
