package Dirtsleeper.scripts.RuneEssenceMiner;

import org.powerbot.script.rt6.Interactive;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Npc;


public class OpenDoor extends Task
{
    private int[] doorId = {24381};
    private int[] auburyId = {5913};
    private int[] bankId = {782};
    private int[] doorBounds = {-240, 240, -1000, 0, 200, 300};
    private Tile doorTile = new Tile(3253, 3399, 0);


    public OpenDoor(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        // headed to bank
        if (RuneEssenceMiner.STATUS == STATE.BANK || RuneEssenceMiner.STATUS == STATE.PORTAL)
        {
            System.out.println("Moving to Bank");
            ctx.objects.select().id(bankId);
            GameObject bank = ctx.objects.nearest().poll();
            return !ctx.objects.select(doorTile, 1).id(doorId).isEmpty()
                    && !ctx.movement.reachable(ctx.players.local().tile(), bank.tile());
        }
        // headed to aubury
        else if (RuneEssenceMiner.STATUS == STATE.AUBURY)
        {
            System.out.println("Moving to Aubury");
            ctx.npcs.select().id(auburyId);
            Npc aubury = ctx.npcs.nearest().poll();
            return !ctx.objects.select(doorTile, 1).id(doorId).isEmpty()
                    && !ctx.movement.reachable(ctx.players.local().tile(), aubury.tile());
        }
        else
        {
            return false;
        }
    }

    @Override
    public void execute()
    {
        GameObject door = ctx.objects.each(Interactive.doSetBounds(doorBounds)).nearest().poll();
        if (door.inViewport())
        {
            if (door.click())
                RuneEssenceMiner.STATUS = STATE.DOOR;
        }
        else
        {
            ctx.movement.step(door);
            ctx.camera.turnTo(door);
        }
    }
}
