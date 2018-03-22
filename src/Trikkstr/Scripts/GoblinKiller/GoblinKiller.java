package Trikkstr.Scripts.GoblinKiller;

import Trikkstr.Scripts.Tasks.Bank;
import Trikkstr.Scripts.Tasks.BuyKebabs;
import Trikkstr.Scripts.Tasks.Fight;
import org.powerbot.script.rt4.*;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(

        name="Goblin Killer",
        description = "Kills Goblins and Loots them",
        properties = "client=4; author=Trikkstr; topic=1343746;"
)

public class GoblinKiller extends PollingScript<ClientContext>
{
    private static boolean bones;
    private static boolean banked = false;

    private int selection;

    private int pollCount = 0;

    private long startTime;
    private long endTime;
    private long runTime;

    private List<Task> taskList = new ArrayList<Task>();

    @Override
    public void start()
    {
        startTime = System.currentTimeMillis();
        System.out.println("Starting.");

        setBones();

        taskList.add(new Bank(ctx));
        taskList.add(new BuyKebabs(ctx));
        taskList.add(new Fight(ctx));

    }

    @Override
    public void stop()
    {
        endTime = System.currentTimeMillis();
        runTime = (endTime - startTime) / 1000;
        System.out.printf("Total Runtime: %ds\n", runTime);
        System.out.printf("Polls: %d\n", pollCount);
        System.out.printf("Polls per second: %d\n", pollCount/runTime);
        System.out.println("Stopped.");
    }

    @Override
    public void poll()
    {
        System.out.printf("Polling...\n");

        for (Task task : taskList)
        {
            if (ctx.controller.isStopping())
            {
                break;
            }

            if (task.activate())
            {
                task.execute();
                break;
            }
        }
        pollCount += 1;
    }

    private void setBones()
    {
        selection = JOptionPane.showConfirmDialog(null, "Would you like to pickup and bury bones?",
                "Bury Bones?", JOptionPane.YES_NO_OPTION);

        if(selection == 0)
        {
            bones = true;
        }
        else
        {
            bones = false;
        }
    }

    public static boolean getBones()
    {
        return bones;
    }

    public static void setBanked(boolean bool)
    {
        banked = bool;
    }

    public static boolean getBanked()
    {
        return  banked;
    }
}
