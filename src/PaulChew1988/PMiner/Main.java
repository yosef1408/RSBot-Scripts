package PMiner;


import PMiner.CombatTasks.*;
import PMiner.Tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


@Script.Manifest( name = "P Miner", description = "Power mines iron ore at mining guild",properties = " author = PaulChew1988; topic=https://www.powerbot.org/community/topic/1352105-pminer/; client = 4;")

public class Main extends PollingScript<ClientContext>implements PaintListener  {

    List<Task> taskList = new ArrayList<Task>();
    int startExp = 0;


    String userMineOptions[] = {"Bank", "PowerMine"};
    String userRockOptions[] = {"Clay", "Iron"};
    String userLevelCbOptions[] = {"Yes", "No"};

    @Override
    public void start() {




        String userMineChoice = "" + (String) JOptionPane.showInputDialog(null, "Bank or PowerMine?", "P Mining", JOptionPane.PLAIN_MESSAGE, null, userMineOptions, userMineOptions[0]);
        String userRockChoice = "" + (String) JOptionPane.showInputDialog(null, "Clay or Iron?", "P Mining", JOptionPane.PLAIN_MESSAGE, null, userRockOptions, userRockOptions[1]);


        if (userMineChoice.equals("PowerMine")) {
            taskList.add(new Drop(ctx));
        }
        if (ctx.players.local().combatLevel() <13) {
            String userLevelCbChoice = "" + (String) JOptionPane.showInputDialog(null, "Level cb?", "P Mining", JOptionPane.PLAIN_MESSAGE, null, userLevelCbOptions, userLevelCbOptions[0]);
            if (userLevelCbChoice.equals("Yes")) {
                taskList.add(new Walk(ctx, PMinerConst.pathToChickens));
                taskList.add(new SelectStyles(ctx));
                taskList.add(new AttackChicken(ctx));
                taskList.add(new Walk(ctx, PMinerConst.chickenToClay));
            }
        }





       if (userRockChoice.equals("Clay")) {
            if (userMineChoice.equals("Bank")) {
                taskList.add(new Banking(ctx));
                taskList.add(new Walk(ctx, PMinerConst.pathToWestBank));

            }
            taskList.add(new Mine(ctx,PMinerConst.CLAY_ROCK_IDs));
        } else if (userRockChoice.equals("Iron")) {

            if (userMineChoice.equals("Bank")) {
                taskList.add(new AutoPick(ctx,PMinerConst.PICK_IDS));
                taskList.add(new Banking(ctx));
                taskList.add(new Walk(ctx,PMinerConst.EastBankToGE));
                taskList.add(new Sell(ctx));
                taskList.add(new Walk(ctx, PMinerConst.pathToEastBank));

            }
            taskList.add(new Mine(ctx, PMinerConst.IRON_ROCK_IDs));
        }





        startExp = ctx.skills.experience(Constants.SKILLS_MINING);
    }

    @Override
    public void poll() {
        for (Task t : taskList) {
            if (ctx.controller.isStopping()) {
                break;
            }
            if (t.activate()) {
                    t.execute();


                    break;
                }
            }
        }


   @Override
   public void repaint(Graphics graphics) {

        long milliseconds =this.getTotalRuntime();
        long second = (milliseconds/1000 %60);
        long minutes = (milliseconds / (1000 *60)%60);
        long hours = (milliseconds / (1000 * 60 *60) % 24);
        int expGained = ctx.skills.experience(Constants.SKILLS_MINING) - startExp;


        Graphics2D g = (Graphics2D)graphics;

        g.setColor(new Color(0,0,0,180));
        g.fillRect(0,0,150,100);

        g.setColor(new Color(255,255,255));
        g.drawRect(0,0,150,100);


        g.drawString("P Miner", 20 ,20);
        g.drawString("Running for: " + String.format("%02d:%02d:%02d",hours,minutes,second), 20,40);

        g.drawString("Exp/Hr "+ (int)(expGained *(3600000/milliseconds)),20,60);
    }

}