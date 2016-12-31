package Dirtsleeper.scripts.RuneEssenceMiner;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Script.Manifest(
        name = "Rune Essence Miner", properties = "author=Dirtsleeper; topic=1325249; client=6;",
        description = "Mines and Banks Rune Essence in Varrock"
)

public class RuneEssenceMiner extends PollingScript<ClientContext>
{
    private List<Task> taskList = new ArrayList<Task>();
    static public STATE STATUS = STATE.IDLE;

    @Override
    public void start() {
        taskList.addAll(Arrays.asList(new Mine(ctx), new BankEssence(ctx), new CloseBank(ctx), new OpenDoor(ctx), new MoveToAubury(ctx), new MoveNearAubury(ctx), new MoveToBank(ctx), new MoveToPortal(ctx)));
        //taskList.addAll(Arrays.asList(new OpenDoor(ctx)));
    }

    @Override
    public void poll() {
        for (Task task : taskList)
        {
            if (task.activate())
            {
                task.execute();
            }
        }
        System.out.println(STATUS);
    }

}