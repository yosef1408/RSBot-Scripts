package noobienoobie123.FaladorCowKiller;

/**
 * Created by larry on 7/17/2017.
 */


import noobienoobie123.FaladorCowKiller.tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name= "[NOOBIE]AllInOneCow", description = "Kills cows, banks, and tans. All in one cow script", properties = "author=noobienoobie123; topic=1335434; client=4;")


public class FaladorCowKiller extends PollingScript<ClientContext> implements PaintListener {

    List<Task> tasklist = new ArrayList<Task>();
    private Looter cowhide= new Looter(ctx);
    private Tan tanHide = new Tan(ctx);
    private static Boolean tan = false;
    int startingHpExp = 0;
    int startingStrExp = 0;
    int startingAttExp = 0;
    int startingDefExp= 0;
    int startingHpLvl = 0;
    int startingStrLvl = 0;
    int startingAttLvl = 0;
    int startingDefLvl = 0;
    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds/1000) % 60;
        long minutes = (milliseconds/(1000*60)) % 60;
        long hours = (milliseconds/(1000*60*60)) % 24;

        int hpExpGained = ctx.skills.experience(Constants.SKILLS_HITPOINTS) - startingHpExp;
        int strExpGained = ctx.skills.experience(Constants.SKILLS_STRENGTH) - startingStrExp;
        int attExpGained = ctx.skills.experience(Constants.SKILLS_ATTACK) - startingAttExp;
        int defExpGained = ctx.skills.experience(Constants.SKILLS_DEFENSE) - startingDefExp;

        int hpLvlGained = ctx.combat.maxHealth() - startingHpLvl;
        int strLvlGained = ctx.skills.level(Constants.SKILLS_STRENGTH) - startingStrLvl;
        int attLvlGained = ctx.skills.level(Constants.SKILLS_ATTACK) - startingAttLvl;
        int defLvlGained = ctx.skills.level(Constants.SKILLS_DEFENSE) - startingDefLvl;
        int cowhideCounter = cowhide.getCowHideTotal();
        int tanHideCounter = tanHide.getTanTotal();


        if (tan == false) {
            Graphics2D g = (Graphics2D) graphics;
            g.drawString("Cow Slayer", 20, 40);
            g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 20, 60);
            g.drawString("Hitpoints Exp/Hour " + (int) (hpExpGained * (3600000 / milliseconds)) + "     Levels(" + hpLvlGained + ")", 20, 80);
            g.drawString("Strength Exp/Hour " + (int) (strExpGained * (3600000 / milliseconds)) + "     Levels(" + strLvlGained + ")", 20, 100);
            g.drawString("Attack Exp/Hour " + (int) (attExpGained * (3600000 / milliseconds)) + "     Levels(" + attLvlGained + ")", 20, 120);
            g.drawString("Defense Exp/Hour " + (int) (defExpGained * (3600000 / milliseconds)) + "     Levels(" + defLvlGained + ")", 20, 140);
            g.drawString("CowHides/Hour " + (int) (cowhideCounter * (3600000 / milliseconds)) + "     Total(" + cowhideCounter + ")", 20, 160);
        }
        else if(tan == true){
            Graphics2D g = (Graphics2D) graphics;
            g.drawString("Cow Slayer", 20, 40);
            g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 20, 60);
            g.drawString("Tan/Hour " + (int) (tanHideCounter * (3600000 / milliseconds)) + "     Total(" + tanHideCounter + ")", 20, 80);

        }



    }

    @Override
    public void start(){

        String userChoices [] = {"Lumbridge", "Falador", "Tan"};
        String userSelection = (String) JOptionPane.showInputDialog(null, "Where do you wish to kill cows?","CowOptions",JOptionPane.PLAIN_MESSAGE, null, userChoices,userChoices[0]);



        if(userSelection.equals("Lumbridge")){
            tasklist.add(new Bank(ctx));
            tasklist.add(new WalkToCowPenLumbridge(ctx));
            tasklist.add(new Looter(ctx));
            tasklist.add(new CowKiller(ctx));


        }
        if(userSelection.equals("Falador")){
            tasklist.add(new Bank(ctx));
            tasklist.add(new WalkToCowPenFalador(ctx));
            tasklist.add(new Looter(ctx));
            tasklist.add(new CowKiller(ctx));
        }
        if(userSelection.equals("Tan")){
            tasklist.add(new BankForTan(ctx));
            tasklist.add(new WalkToTanner(ctx));
            tasklist.add(new Tan(ctx));
            tan = true;
        }




        startingHpExp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
        startingStrExp = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        startingAttExp = ctx.skills.experience(Constants.SKILLS_ATTACK);
        startingDefExp= ctx.skills.experience(Constants.SKILLS_DEFENSE);
        startingHpLvl = ctx.combat.maxHealth();
        startingStrLvl = ctx.skills.level(Constants.SKILLS_STRENGTH);
        startingAttLvl = ctx.skills.level(Constants.SKILLS_ATTACK);
        startingDefLvl = ctx.skills.level(Constants.SKILLS_DEFENSE);

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
}
