package iDzn.OgreCannon;

import iDzn.OgreCannon.Tasks.*;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.Callable;


@Script.Manifest(name= "OgreCannon", description="Reloads cannon at the Combat Training Camp ogres, includes different looting types", properties="client=4; author=iDzn; topic=1340538;")

public class OgreCannon extends PollingScript<ClientContext> implements PaintListener {

    private Image bg = null;
    private GeItem ge;
    public int AttackTrue = 0, idleTimer = 0, SeedCount, TorstolCount, TorstolGained, TorstolPrice, RanarrCount, RanarrGained, RanarrPrice, SnapdragonCount, SnapdragonGained, SnapdragonPrice,
            BallsCount, BallsUsed, BallsPrice, startTime = 0, time = 0, time1 = 0, time2 = 0, time3 = 0, time4 = 0, xpGained, xpStart, xpCurrent, levelCurrent, telek=0;
    private int Seeds [] = {5295, 5300, 5304};
    public long getRunTime() {
        return getRuntime();
    }

    private ArrayList<Task> taskList = new ArrayList<Task>();

    @Override
    public void start() {

        bg = downloadImage("http://i.imgur.com/a5l0UaK.png");
        BallsCount = ctx.inventory.select().id(2).poll().stackSize();
        xpStart = ctx.skills.experience(Constants.SKILLS_RANGE);
        SeedCount = ctx.inventory.select().id(Seeds).poll().stackSize();
        RanarrCount = ctx.inventory.select().id(5295).poll().stackSize();
        TorstolCount = ctx.inventory.select().id(5304).poll().stackSize();
        SnapdragonCount = ctx.inventory.select().id(5300).poll().stackSize();


        ge = new org.powerbot.script.rt4.GeItem(5295);
        RanarrPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5304);
        TorstolPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(5300);
        SnapdragonPrice = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(2);
        BallsPrice = ge.price;

        final JFrame frame = new JFrame();

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        frame.getContentPane().add(panel);

        final JCheckBox Attack = new JCheckBox("Attack Ogres too?");
        panel.add(Attack);

        final JCheckBox NoLoot = new JCheckBox("No Looting");
        panel.add(NoLoot);

        final JCheckBox RegularLoot = new JCheckBox("Regular Looting");
        panel.add(RegularLoot);

        final JCheckBox TeleLoot = new JCheckBox("Tele Grab Looting");
        panel.add(TeleLoot);

        JButton button = new JButton("Start");
        panel.add(button);

        JLabel label = new JLabel("Please select whether you wish");
        panel.add(label);

        JLabel label2 = new JLabel("to attack ogres and looting type.");
        panel.add(label2);



        frame.setSize(new Dimension(225, 300));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Select Options");
        frame.setVisible(true);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!Attack.isSelected() && !NoLoot.isSelected() && !RegularLoot.isSelected() && !TeleLoot.isSelected()) {
                    return;
                }
                if (Attack.isSelected()) {
                    AttackTrue = 1;
                }
                taskList.add(new deployCannon(ctx));
                taskList.add(new fireCannon(ctx, OgreCannon.this));
               // taskList.add(new repairCannon(ctx));
                taskList.add(new Anti(ctx));

                if (NoLoot.isSelected()) {

                }
               else if (RegularLoot.isSelected()) {

                    taskList.add(new railSqueeze(ctx));
                    taskList.add(new lootSeeds(ctx));
                    taskList.add(new leaveRails(ctx));

                }
               else if (TeleLoot.isSelected()) {
                    taskList.add(new Telek(ctx, OgreCannon.this));

                } else {
                    ctx.controller.stop();

                }
                frame.dispose();
            }
        });
    }

    private void split(long milliseconds) {
        time1 = (int) ((milliseconds / (1000 * 60 * 60 * 24)) % 7);
        time2 = (int) (milliseconds / 1000) % 60;
        time3 = (int) ((milliseconds / (1000 * 60)) % 60);
        time4 = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
    }

    @Override
    public void poll() {
        Attack();
        idleTimer();
      for (Task task : taskList) {
         if (ctx.controller.isStopping()) {
             break;
          }
            if (task.activate()) {
                task.execute();
                updateBalls();
                updateSeeds();
            }
        }

    }
    private void idleTimer() {
        if (ctx.players.local().animation() == -1) {
            idleTimer++;
        } else if (!(ctx.players.local().animation() == -1)) {
            idleTimer = 0;
        }
    }
    private void Attack() {
        GameObject Cannon = ctx.objects.select().id(6).poll();
        Npc Ogres = ctx.npcs.select().id(1153).nearest().poll();
        if (idleTimer>10 && AttackTrue==1 && Cannon.valid()) {
            Ogres.interact("Attack");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !(ctx.players.local().animation() == -1);
                }
            }, 100, 5);
        } else if (!(ctx.players.local().animation() == -1)) {
            idleTimer = 0;
        }
    }

    private void updateSeeds(){
        int newRanarrCount = ctx.inventory.select().id(5295).poll().stackSize(),
            newTorstolCount = ctx.inventory.select().id(5304).poll().stackSize(),
            newSnapdragonCount = ctx.inventory.select().id(5300).poll().stackSize(),
            newSeedCount = ctx.inventory.select().id(Seeds).poll().stackSize();
        if(newSeedCount > SeedCount){
            int rDifference = newRanarrCount - RanarrCount,
                sDifference = newSnapdragonCount - SnapdragonCount,
                tDifference = newTorstolCount - TorstolCount;
            RanarrGained += rDifference;
            SnapdragonGained += sDifference;
            TorstolGained += tDifference;
        }
        RanarrCount = newRanarrCount;
        SnapdragonCount = newSnapdragonCount;
        TorstolCount = newTorstolCount;
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
        levelCurrent = ctx.skills.realLevel(Constants.SKILLS_RANGE);
        xpCurrent = (ctx.skills.experience(Constants.SKILLS_RANGE));
        xpGained = xpCurrent - xpStart;
        double xpPerHour = (long) ((xpGained * 3600000D) /time);
        int gpGained = (RanarrGained*RanarrPrice)+(SnapdragonGained*SnapdragonPrice)+(TorstolGained*TorstolPrice);
        int gpSpent = (BallsUsed*BallsPrice);
        int totalDiffGP = (gpSpent-gpGained);


        time = (int) (getRuntime() - startTime);
        time1 = time;

        Graphics2D g = (Graphics2D) graphics;

        g.drawImage(bg, 6, 269,null);
        g.setFont(new Font("Impact", Font.PLAIN, 17));
        g.setColor(new Color(255, 248, 241));
        if(time>0){
            split(time);
            g.drawString("" + String.format("%02d:%02d:%02d", time4, time3, time2), 355, 400);
        }
        if (xpGained!=0 && time>0) {

        g.setColor(new Color(255, 244, 231));
            g.drawString(String.format(" " + xpGained), 130, 415);
            g.setColor(new Color(239, 219, 196));
            g.drawString(" " +(long) xpPerHour,  123, 446);
            g.drawString("" + gpGained, 370, 430);
            g.setColor(new Color(255, 228, 231));
            g.drawString("" + totalDiffGP / xpGained, 390, 460);

        }
    }
}
