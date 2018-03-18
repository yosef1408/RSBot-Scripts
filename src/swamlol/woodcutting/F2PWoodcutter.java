package swamlol.woodcutting;


import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;
import swamlol.woodcutting.tasks.*;
import swamlol.woodcutting.tasks.Drop;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name="F2P WoodCutter", description = "chops wood at most f2p locations, check thread for more information", properties = ("author=samlol; client=6; topic=999;"))

public class F2PWoodcutter extends PollingScript<ClientContext> implements PaintListener{
    List<Task> taskList = new ArrayList<Task>();
    int startExp =0;
    @Override
    public void start() {
        String userOptions[] = {"Powerchop", "Bank",};
        String userChoice = "" + (String) JOptionPane.showInputDialog(null, "Bank or Powerchop?", "F2P Chopper", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[1]);

        if (userChoice.equals("Bank")){

            String locationOptions[] = {"Varrock and Normal Trees", "Draynor and Oak Trees", "Draynor and Willow Trees"};
            String locationChoice = "" + (String) JOptionPane.showInputDialog(null, "Which location and what tree would you like to chop?", "F2P Chopper", JOptionPane.PLAIN_MESSAGE, null, locationOptions, locationOptions[0]);

            if(locationChoice.equals("Varrock and Normal Trees")){
                    taskList.add(new Bank(ctx));
                    taskList.add(new Chop(ctx, WConstants.TREE_IDS));
                    taskList.add(new Walk(ctx, WConstants.VarrockEastBank));

            }   else if(locationChoice.equals("Draynor and Oak Trees")){
                    taskList.add(new Bank(ctx));
                    taskList.add(new Walk(ctx, WConstants.Draynor_Oak));
                    taskList.add(new Chop(ctx,WConstants.OAK_IDS));

            }   else if(locationChoice.equals("Draynor and Willow Trees")) {
                    taskList.add(new Bank(ctx));
                    taskList.add(new Walk(ctx, WConstants.Draynor_Willow));
                    taskList.add(new Chop(ctx,WConstants.WILLOW_IDS));
            }   else {
                ctx.controller.stop();
            }
        } else if(userChoice.equals("Powerchop")){
            taskList.add(new Drop(ctx));
            taskList.add(new Chop(ctx,WConstants.TREE_IDS));
        } else {
            ctx.controller.stop();
        }

        startExp = ctx.skills.experience(Constants.SKILLS_WOODCUTTING);

    }

    @Override
    public void poll() {

        for(Task task : taskList){
            if(ctx.controller.isStopping()){
                break;
            }

            if(task.activate()){
                task.execute();
                break;
            }
        }
    }

    public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);

    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000*60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60) % 24);

        int expGained = ctx.skills.experience(Constants.SKILLS_WOODCUTTING) - startExp;


        Graphics2D g = (Graphics2D)graphics;

        g.setColor(new Color(0,0,0,180));
        g.fillRect(0,0,150,100);

        g.setColor(new Color(2,255,2));
        g.drawRect(0,0,150,100);

        g.drawString("F2P WoodCutter", 20,20);
        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds),20,40);
        g.drawString("Exp/Hour: " + (int)(expGained * (3600000 / milliseconds)), 20, 60);
    }
}
