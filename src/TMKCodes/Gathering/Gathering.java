package Gathering;

import Gathering.Tasks.*;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name = "Gathering", description = "Let the bot do anything anywhere.", properties = "client=4; author=TMKCodes; topic=999;")

public class Gathering extends PollingScript<ClientContext> implements PaintListener {

    List<Task> taskList = new ArrayList<Task>();

    int startExp = 0;

    String skillChoice;

    @Override
    public void start() {
        ctx.input.speed(25);
        String skillUserOptions[] = { "Fishing", "Woodcutting", "Mining" };
        this.skillChoice = (String) JOptionPane.showInputDialog(null, "What to do?", "Gathering", JOptionPane.PLAIN_MESSAGE, null, skillUserOptions, skillUserOptions[0]);
        String antibanUserOptions[] = {"Yes", "No"};
        String antibanUserChoice = (String) JOptionPane.showInputDialog(null, "Enable antiban?", "Gathering", JOptionPane.PLAIN_MESSAGE, null, antibanUserOptions, antibanUserOptions[0]);
        if(this.skillChoice.equals("Fishing")) {
            String fishingUserOptions[] = {"Small Net", "Bait", "Cage", "Harpoon"};
            String fishingUserChoice = (String) JOptionPane.showInputDialog(null, "What to fish?", "Gathering", JOptionPane.PLAIN_MESSAGE, null, fishingUserOptions, fishingUserOptions[0]);
            String bankingUserOptions[] = {"Bank", "Drop"};
            String bankingUserChoice = (String) JOptionPane.showInputDialog(null, "Bank or drop?", "Gathering", JOptionPane.PLAIN_MESSAGE, null, bankingUserOptions, bankingUserOptions[0]);
            if (bankingUserChoice == "Drop") {
                taskList.add(new Drop(ctx, "fish"));
                taskList.add(new Fish(ctx, fishingUserChoice));
            } else if (bankingUserChoice == "Bank") {
                String locationUserOptions[] = {"Musa point"};
                String locationUserChoice = (String) JOptionPane.showInputDialog(null, "Where to fish?", "Gathering", JOptionPane.PLAIN_MESSAGE, null, locationUserOptions, locationUserOptions[0]);
                taskList.add(new Deposit(ctx, skillChoice));
                taskList.add(new PayFare(ctx, locationUserChoice));
                taskList.add(new Gangplank(ctx));
                taskList.add(new Walk(ctx, locationUserChoice));
                taskList.add(new Fish(ctx, fishingUserChoice));
            }
            startExp = ctx.skills.experience(Constants.SKILLS_FISHING);
        } else if(this.skillChoice.equals("Woodcutting")) {
            String cuttingUserOptions[] = {"Normal", "Oak", "Willow", "Maple", "Yew"}; // 1278
            String cuttingUserChoice = (String) JOptionPane.showInputDialog(null, "What to cut?", "Gathering", JOptionPane.PLAIN_MESSAGE, null, cuttingUserOptions, cuttingUserOptions[0]);
            String bankingUserOptions[] = {"Bank", "Drop"};
            String bankingUserChoice = (String) JOptionPane.showInputDialog(null, "Bank or drop?", "Gathering", JOptionPane.PLAIN_MESSAGE, null, bankingUserOptions, bankingUserOptions[0]);
            if(bankingUserChoice.equals("Drop")) {
                taskList.add(new Drop(ctx, "wood"));
                taskList.add(new Woodcut(ctx, cuttingUserChoice));
            } else if(bankingUserChoice.equals("Bank")) {
                ArrayList<String> locationUserOptions = new ArrayList<String>();
                if(cuttingUserChoice.equals("Normal") || cuttingUserChoice.equals("Oak")) {
                    locationUserOptions.add("Lumbridge");
                } else if(cuttingUserChoice.equals("Willow")) {
                    locationUserOptions.add("Draynor");
                } else if(cuttingUserChoice.equals("Yew")) {
                    locationUserOptions.add("Edgeville");
                } else {
                    ctx.controller.stop();
                }
                String locationUserChoice = (String) JOptionPane.showInputDialog(null, "Where to cut?", "Gathering", JOptionPane.PLAIN_MESSAGE, null, locationUserOptions.toArray(), locationUserOptions.get(0));
                taskList.add(new Bank(ctx, skillChoice));
                taskList.add(new Walk(ctx, locationUserChoice + " " + cuttingUserChoice));
                taskList.add(new Woodcut(ctx, cuttingUserChoice));
            }
            startExp = ctx.skills.experience(Constants.SKILLS_WOODCUTTING);
        } else if(this.skillChoice.equals("Mining")) {
            String miningUserOptions[] = {"Clay", "Copper", "Tin", "Iron", "Silver", "Coal", "Gold", "Mithril", "Adamantite", "Runite"}; // 1278
            String miningUserChoice = (String) JOptionPane.showInputDialog(null, "What to mine?", "Gathering", JOptionPane.PLAIN_MESSAGE, null, miningUserOptions, miningUserOptions[0]);
            String bankingUserOptions[] = {"Bank", "Drop"};
            String bankingUserChoice = (String) JOptionPane.showInputDialog(null, "Bank or drop?", "Gathering", JOptionPane.PLAIN_MESSAGE, null, bankingUserOptions, bankingUserOptions[0]);
            if(bankingUserChoice.equals("Drop")) {
                taskList.add(new Drop(ctx, "ore"));
                taskList.add(new Mine(ctx, miningUserChoice));
            } else if(bankingUserChoice.equals("Bank")) {
                ArrayList<String> locationUserOptions = new ArrayList<String>();
                if(miningUserChoice.equals("Copper") || miningUserChoice.equals("Tin")) {
                    locationUserOptions.add("Lumbridge East");
                    locationUserOptions.add("Varrock");
                } else if(miningUserChoice.equals("Tin")) {
                    locationUserOptions.add("Barbarian Village");
                } else if(miningUserChoice.equals("Iron")) {
                    locationUserOptions.add("Varrock");
                    locationUserOptions.add("Falador");
                } else if(miningUserChoice.equals("Coal")) {
                    locationUserOptions.add("Barbarian Village");
                    locationUserOptions.add("Falador");
                } else {
                    ctx.controller.stop();
                }
                String locationUserChoice = (String) JOptionPane.showInputDialog(null, "Where to mine?", "Gathering", JOptionPane.PLAIN_MESSAGE, null, locationUserOptions.toArray(), locationUserOptions.get(0));
                taskList.add(new Bank(ctx, skillChoice));
                taskList.add(new Walk(ctx, locationUserChoice + " Mine"));
                taskList.add(new Mine(ctx, miningUserChoice));
            }
            startExp = ctx.skills.experience(Constants.SKILLS_MINING);

        }
        if (antibanUserChoice.equals(antibanUserOptions[0])) {
            taskList.add(new Antiban(ctx));
        }
    }

    @Override
    public void poll() {
        for(Task task : taskList) {
            if(ctx.controller.isStopping()) {
                break;
            }
            if(task.activate()) {
                task.execute();
                break;
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long hours = milliseconds / (1000 * 60 * 60);
        milliseconds -= hours * (1000 * 60 * 60);
        long minutes = milliseconds / (1000 * 60);
        milliseconds -= minutes * (1000 * 60);
        long seconds = milliseconds / 1000;
        int expGained = 0;
        if(this.skillChoice.equals("Fishing")) {
            expGained = ctx.skills.experience(Constants.SKILLS_FISHING) - startExp;
        } else if(this.skillChoice.equals("Woodcutting")) {
            expGained = ctx.skills.experience(Constants.SKILLS_WOODCUTTING) - startExp;
        } else if(this.skillChoice.equals("Mining")) {
            expGained = ctx.skills.experience(Constants.SKILLS_MINING) - startExp;
        }
        float xpsec = 0;
        if((minutes > 0 || hours > 0 || seconds > 0) && expGained > 0) {
            xpsec = ((float) expGained) / (float)(seconds + (minutes * 60) + (hours * 60 * 60));
        }
        float xpmin = xpsec * 60;
        float xphour = xpmin * 60;

        Graphics2D g = (Graphics2D)graphics;

        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, 150, 130);

        g.setColor(new Color(255, 255, 255));
        g.drawRect(0, 0, 150, 130);

        g.drawString("Gathering", 20, 20);
        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 20, 40);
        if(this.skillChoice.equals("Fishing")) {
            g.drawString("Xp to next lvl: " + (ctx.skills.experienceAt(ctx.skills.level(Constants.SKILLS_FISHING) + 1) - ctx.skills.experience(Constants.SKILLS_FISHING)), 20, 60);
            g.drawString("Exp/hour: " + (int) xphour, 20, 80);
            g.drawString("ExpGained: " + expGained, 20, 100);
        } else if(this.skillChoice.equals("Woodcutting")) {
            g.drawString("Xp to next lvl: " + (ctx.skills.experienceAt(ctx.skills.level(Constants.SKILLS_WOODCUTTING) + 1) - ctx.skills.experience(Constants.SKILLS_WOODCUTTING)), 20, 60);
            g.drawString("Exp/hour: " + (int) xphour, 20, 80);
            g.drawString("ExpGained: " + expGained, 20, 100);
        } else if(this.skillChoice.equals("Mining")) {
            g.drawString("Xp to next lvl: " + (ctx.skills.experienceAt(ctx.skills.level(Constants.SKILLS_MINING) + 1) - ctx.skills.experience(Constants.SKILLS_MINING)), 20, 60);
            g.drawString("Exp/hour: " + (int) xphour, 20, 80);
            g.drawString("ExpGained: " + expGained, 20, 100);
        }
        g.drawString("TMKCodes (c) 2018 ", 20, 120);
    }
}
