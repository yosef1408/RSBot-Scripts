package noobienoobie123;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import osrs.Task;
import noobienoobie123.tasks.Antiban;
import noobienoobie123.tasks.Attack;
import noobienoobie123.tasks.Walk;

import javax.swing.JOptionPane;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by larry on 7/10/2017.
 */
@Script.Manifest(name="LumbridgeSlayer", description = "Kills monsters in Lumbridge", properties = "author=noobienoobie123; topic=1335353; client=4;")

public class LumbridgeSlayer extends PollingScript<ClientContext> implements PaintListener{

    List<osrs.Task> tasklist = new ArrayList<osrs.Task>();
    int startingHPEXP = 0;
    int startingSTREXP = 0;
    int startingATTEXP = 0;
    int startingDEFEXP = 0;
    int startingPRAYEXP = 0;

    int startingHPLvl = 0;
    int startingSTRLvl = 0;
    int startingATTLvl = 0;
    int startingDEFLvl = 0;
    int startingPRAYLvl = 0;
    @Override
    public void start(){

        String userChoices [] = {"Front of Lumbridge", "Ham Hideout", "Front of Lumbridge Swamp" , "Middle of Lumbridge Swamp"};
        String userSelection = (String) JOptionPane.showInputDialog(null, "Where do you wish to slay monsters?","LumbridgeKiller",JOptionPane.PLAIN_MESSAGE, null, userChoices,userChoices[0]);

        if(userSelection.equals("Front of Lumbridge")){
            tasklist.add(new Walk(ctx, PathsTo.pathToLumbridgeFront));
        }
        else if(userSelection.equals("Ham Hideout")){
            tasklist.add(new Walk(ctx, PathsTo.pathToLumbridgeHamHideOut));
        }
        else if(userSelection.equals("Front of Lumbridge Swamp")){
            tasklist.add(new Walk(ctx, PathsTo.pathToLumbridgeSwampStart));
        }
        else if(userSelection.equals("Middle of Lumbridge Swamp")){
            tasklist.add(new Walk(ctx, PathsTo.pathToLumbridgeSwampMiddle));
        }
        tasklist.add(new Attack(ctx));
        tasklist.add(new Antiban(ctx));

        startingHPEXP = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
        startingSTREXP = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        startingATTEXP = ctx.skills.experience(Constants.SKILLS_ATTACK);
        startingDEFEXP = ctx.skills.experience(Constants.SKILLS_DEFENSE);
        startingPRAYEXP = ctx.skills.experience(Constants.SKILLS_PRAYER);

        startingHPLvl = ctx.combat.maxHealth();
        startingSTRLvl = ctx.skills.level(Constants.SKILLS_STRENGTH);
        startingATTLvl = ctx.skills.level(Constants.SKILLS_ATTACK);
        startingDEFLvl = ctx.skills.level(Constants.SKILLS_DEFENSE);
        startingPRAYLvl = ctx.skills.level(Constants.SKILLS_PRAYER);

    }


    @Override
    public void poll() {
        for(Task task : tasklist){
            if(ctx.controller.isStopping()){
                break;
            }


            if(task.activate()){
                task.execute();
                break;
            }
        }
    }


    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds/1000) % 60;
        long minutes = (milliseconds/(1000*60)) % 60;
        long hours = (milliseconds/(1000*60*60)) % 24;

        int hpExpGained = ctx.skills.experience(Constants.SKILLS_HITPOINTS) - startingHPEXP;
        int strExpGained = ctx.skills.experience(Constants.SKILLS_STRENGTH) - startingSTREXP;
        int attExpGained = ctx.skills.experience(Constants.SKILLS_ATTACK) - startingATTEXP;
        int defExpGained = ctx.skills.experience(Constants.SKILLS_DEFENSE) - startingDEFEXP;
        int prayerExpGained = ctx.skills.experience(Constants.SKILLS_PRAYER) - startingPRAYEXP;

        int hpLvlGained = ctx.combat.maxHealth() - startingHPLvl;
        int strLvlGained = ctx.skills.level(Constants.SKILLS_STRENGTH) - startingSTRLvl;
        int attLvlGained = ctx.skills.level(Constants.SKILLS_ATTACK) - startingATTLvl;
        int defLvlGained = ctx.skills.level(Constants.SKILLS_DEFENSE) - startingDEFLvl;
        int prayerLvlGained = ctx.skills.level(Constants.SKILLS_PRAYER) - startingPRAYLvl;

        Graphics2D g = (Graphics2D)graphics;
        g.drawString("LumbridgeSlayer", 20,40);
        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours,minutes,seconds),20,60);
        g.drawString("Hitpoints Exp/Hour "+ (int)(hpExpGained * (3600000 / milliseconds))+ "     Levels(" + hpLvlGained + ")", 20,80);
        g.drawString("Strength Exp/Hour "+ (int)(strExpGained * (3600000 / milliseconds))+ "     Levels(" + strLvlGained + ")", 20,100);
        g.drawString("Attack Exp/Hour "+ (int)(attExpGained * (3600000 / milliseconds))+ "     Levels(" + attLvlGained + ")", 20,120);
        g.drawString("Defense Exp/Hour "+ (int)(defExpGained * (3600000 / milliseconds))+ "     Levels(" + defLvlGained + ")", 20,140);
        g.drawString("Prayer Exp/Hour "+ (int)(prayerExpGained * (3600000 / milliseconds))+ "     Levels(" + prayerLvlGained + ")", 20,160);



    }
}
