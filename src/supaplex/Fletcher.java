package supaplex;

import supaplex.exceptions.BankException;
import supaplex.exceptions.ResourceException;
import supaplex.exceptions.QueueEmptyException;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Game;
import supaplex.tasks.stringing.String;
import supaplex.tasks.cutting.DepositUnfBows;
import supaplex.tasks.cutting.Fletch;
import supaplex.tasks.Task;
import supaplex.tasks.cutting.WithdrawLogs;
import supaplex.tasks.stringing.DepositBows;
import supaplex.tasks.stringing.WithdrawUnfBowsAndStrings;
import supaplex.util.GlobalVariables;
import supaplex.util.TaskHandler;
import supaplex.util.TaskType;
import supaplex.views.Gui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andreas on 20.06.2016.
 */
@Script.Manifest(
        name = "SUpaFletch",
        description = "Cuts and strings bows",
        properties = "author=supaplex;topic=1317592;client=4;"
)
public class Fletcher extends PollingScript<ClientContext> implements PaintListener {

    private List<Task> taskList;
    private static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);
    private int initLevel = ctx.skills.level(Constants.SKILLS_FLETCHING);
    private int initExp = ctx.skills.experience(Constants.SKILLS_FLETCHING);
    private long initTime = System.currentTimeMillis();

    @Override
    public void start() {
        System.out.println("Script Started!");
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Gui();
            }
        });
    }

    @Override
    public void poll() {

        // Checks if logged in
        if (!ctx.game.loggedIn()) {
            ctx.controller.stop();
            return;
        }

        // Checks if Gui is closed, and script ready to run
        if (GlobalVariables.startScriptFlag == false) {
            return;
        }

        // Checks the task queue, if empty stop the script
        if (GlobalVariables.taskFinished) {
            try {
                TaskHandler.getInstance().checkTask();
                taskList = new ArrayList<>();
            } catch (QueueEmptyException e) {
                System.out.println(e.getMessage());
                ctx.controller.stop();
                return;
            }
        }

        // Checks if required level is met
        if (ctx.skills.level(Constants.SKILLS_FLETCHING) < GlobalVariables.minimumLevel) {
            System.out.println("Not high enough level, aborting..");
            ctx.controller.stop();
            return;
        }

        // Switches to inventory tab if needed
        if (ctx.game.tab() != Game.Tab.INVENTORY) {
            ctx.game.tab(Game.Tab.INVENTORY);
        }

        // Starts cut logs task
        if (GlobalVariables.taskType == TaskType.CUT_BOWS) {
            taskList.addAll(Arrays.asList(new Fletch(ctx), new DepositUnfBows(ctx), new WithdrawLogs(ctx)));
            execute();

        }
        // Starts string bows task
        else if (GlobalVariables.taskType == TaskType.STRING_BOWS) {
            taskList.addAll(Arrays.asList(new String(ctx), new DepositBows(ctx), new WithdrawUnfBowsAndStrings(ctx)));
            execute();

        }
        // If something else, then abort
        else {
            System.out.println("NO TASK TYPE, ABORTING..");
            ctx.controller.stop();
            return;
        }
    }

    @Override
    public void stop() {
        System.out.println("Script Stopped!");
    }

    private void execute() {
        for (Task task : taskList) {
            if (task.activate()) {
                try {
                    task.execute();
                } catch (ResourceException e) {
                    switch (e.resourceType) {
                        // If no knife in bank nor inventory, then stop script
                        case KNIFE:
                            System.out.println(e.getMessage());
                            ctx.controller.stop();
                            break;
                        // If out of resources, then stop script
                        default:
                            if (TaskHandler.getInstance().getQueue().isEmpty()) {
                                System.out.println(e.getMessage());
                                ctx.controller.stop();
                            }
                            break;

                    }
                    return;
                }
                // If no bank nearby, then stop script
                catch (BankException e) {
                    System.out.println(e.getMessage());
                    ctx.controller.stop();
                    return;
                }
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;

        Point mouse = ctx.input.getLocation();

        g.setColor(Color.RED);
        // Horizontal line
        g.drawLine((int) mouse.getX() - 10, (int) mouse.getY(), (int) mouse.getX() + 10, (int) mouse.getY());
        // Vertical line
        g.drawLine((int) mouse.getX(), (int) mouse.getY() - 10, (int) mouse.getX(), (int) mouse.getY() + 10);

        g.setFont(TAHOMA);
        Color color = new Color(0, 0, 0, 160);
        g.setColor(color);
        g.fillRect(5, 5, 160, 105);
        g.setColor(Color.WHITE);

        long runtime = System.currentTimeMillis() - initTime;
        java.lang.String hms = java.lang.String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(runtime),
                TimeUnit.MILLISECONDS.toMinutes(runtime) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(runtime) % TimeUnit.MINUTES.toSeconds(1));

        g.drawString("SUpaFletch", 10, 20);
        g.drawString("Runtime: " + hms, 10, 40);
        g.drawString("Level: " + initLevel + " + " + "(" + (ctx.skills.level(Constants.SKILLS_FLETCHING) - initLevel) + ")", 10, 60);
        int expGained = ctx.skills.experience(Constants.SKILLS_FLETCHING) - initExp;
        g.drawString("Total XP: " + expGained, 10, 80);
        g.drawString("XP/Hour: " + Math.round(((float) expGained / runtime) * 3600000), 10, 100);
    }
}
