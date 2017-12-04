package iDzn.NoobNester;

import iDzn.NoobNester.Tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Script.Manifest(name= "NoobNester", description="Empties nets of seeds and jewellery.", properties="client=4; author=iDzn; topic=1340464;")


public class NoobNester extends PollingScript<ClientContext> implements PaintListener {

    List<Task> taskList = new ArrayList<Task>();
    private int nestsOpened, nestCount, nestPrice, SkinPrice, ClawPrice,  ge, Counts;

    @Override
    public void start() {
        int[] IDs = {5302, 5303, 5295, 5304, 5300, 5313, 5316, 5312, 5290, 5287, 21486, 21488, 5321, 5323, 5320, 5100, 5293, 5292, 5299, 5288, 5314, 5289, 1635, 1643, 1639, 1641, 1637};
        for (int id : IDs) {
            ge = new org.powerbot.script.rt4.GeItem(id).price;
            SkinPrice = new org.powerbot.script.rt4.GeItem(7416).price;
            ClawPrice = new org.powerbot.script.rt4.GeItem(7418).price;
            nestPrice = new org.powerbot.script.rt4.GeItem(5075).price;
        }
        Map<Integer, Integer> itemCounts = new HashMap<Integer, Integer>();
        for (int id : IDs) {
           itemCounts.put(id, ctx.inventory.select().id(id).count());

        }
        nestCount = ctx.inventory.select().id(5075).count();

        String userOptions[] = {"Ring Nests", "Seed Nests"};
        String userChoice = "" + (String) JOptionPane.showInputDialog(null, "Seed Nests or Ring Nests?", "iDzn/NoobNester", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[1]);

        if (userChoice.equals("Seed Nests")) {
            taskList.add(new Bank(ctx));
            taskList.add(new Withdraw(ctx));
            taskList.add(new OpenNests(ctx));
        } else if (userChoice.equals("Ring Nests")) {
            taskList.add(new Bank(ctx));
            taskList.add(new WithdrawRings(ctx));
            taskList.add(new OpenRings(ctx));
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
                updateNests();

            }


        }

    }

//updateNests runs to see what the starting inventory
    private void updateNests(){

        int newNestCount = ctx.inventory.select().id(5075).count();
        if(newNestCount > nestCount){
            int difference = newNestCount - nestCount;
            nestsOpened += difference;
        }
        nestCount = newNestCount;
    }

    @Override
            public void repaint (Graphics graphics){
                long Milliseconds = this.getTotalRuntime();
                long Seconds = (Milliseconds / 1000) % 60;
                long Minutes = (Milliseconds / (1000 * 60)) % 60;
                long Hours = (Milliseconds / (1000 * 60 * 60)) % 24;
                //int profit =((nestsOpened * (int)0.15)*nestPrice)-(((SkinPrice+ClawPrice)/2)*nestsOpened);
                int profit = ge;


        Graphics2D g = (Graphics2D) graphics;

                g.setColor(new Color(35, 58, 70));
                g.fillRect(6, 345, 507, 129);

                g.setColor(new Color(57, 185, 255));
                g.drawRect(6, 345, 507, 129);
                g.setFont(new Font("Impact", Font.BOLD, 30));
                g.drawString("NoobNester", 200, 375);
                g.setFont(new Font("Impact", Font.PLAIN, 20));
                g.setColor(new Color(255, 255, 255));
                g.drawString("Run Time: " + String.format("%02d:%02d:%02d", Hours, Minutes, Seconds), 22, 400);
                g.drawString("Nests Opened: " + nestsOpened, 22, 420);
                g.drawString("Profit: " + profit, 22, 440);
                g.drawString("Profit/Hr: " + (profit/Seconds)*3600, 22, 460);


            }

        }
