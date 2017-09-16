package Elt.AlKharidFishNCook;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import Elt.AlKharidFishNCook.Task;
import Elt.AlKharidFishNCook.tasks.*;

@Script.Manifest(name="AlKharidFishNCook", description="This bot fishes, cooks, and banks or drops in Al Kharid.", properties="author=Elt; client=4;")

public class AlKharidFisher extends PollingScript<ClientContext> implements PaintListener, MessageListener {

    private int numFishCaught = 0;
    private int fishingLevelsGained = 0;
    private int cookingLevelsGained = 0;
    private List<Task> taskList = new ArrayList<Task>();
    private Task handleCombatTask;
    private Task fishTask;
    private Task cookTask;
    private Task bankTask;
    private Task dropTask;

    @Override
    public void start() {
        System.out.println("AlKharidFishNCook starting. TEST NEW START");
        String userOptions[] = {"Bank", "Drop"};
        String userChoice = ""+(String)JOptionPane.showInputDialog(null, "Bank or Drop cooked fish?", "Fishing", JOptionPane.PLAIN_MESSAGE, null, userOptions, userOptions[0]);

        this.handleCombatTask = new handleCombat(ctx);
        this.fishTask = new Fish(ctx);
        this.cookTask = new Cook(ctx);

        taskList.add(handleCombatTask);
        taskList.add(fishTask);
        taskList.add(cookTask);
        if (userChoice.equals("Bank")) {
            this.bankTask = new Banking(ctx, true);
            taskList.add(bankTask);
        } else {
            this.dropTask = new Drop(ctx);
            taskList.add(dropTask);
        }
    }

    @Override
    public void stop() {
        System.out.println("AlKharidFishNCook stopping.");
    }

    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000*60) % 60);
        long hours = (milliseconds / (1000*60*60)) % 24;

        Graphics2D g = (Graphics2D)graphics;
        g.setColor(new Color(255, 107, 107, 180));
        g.fillRect(0, 0, 170, 130);
        g.setColor(new Color(255, 45, 45));
        g.drawRect(0, 0, 170, 130);
        g.setColor(new Color(255, 255, 255));
        g.drawString("AlKharidFishNCook", 10, 20);
        g.drawString("Run time: " + String.format("%02d:%02d:%02d", hours, minutes, seconds), 10, 40);
        g.drawString("Fish caught: " + this.numFishCaught, 10, 80);
        g.drawString("Fishing levels gained: " + this.fishingLevelsGained, 10, 100);
        g.drawString("Cooking levels gained: " + this.cookingLevelsGained, 10, 120);
    }

    @Override
    public void poll() {
        if (ctx.players.local().animation() == -1) {
            for (Task task : taskList) {
                if (task.activate()) {
                    task.execute();
                    break;
                }
            }
        }
    }

    @Override
    public void messaged(MessageEvent e) {
        if (e.text().contains("You catch a")) {
            ++numFishCaught;
        } else if (e.text().contains("you've just advanced your Fishing level")) {
            ++fishingLevelsGained;
        } else if (e.text().contains("you've just advanced your Cooking level")) {
            ++cookingLevelsGained;
        }
    }

}
