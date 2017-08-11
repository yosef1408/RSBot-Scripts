package needscroll.JangerBerriesGrabber.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GroundItem;

import needscroll.JangerBerriesGrabber.Task;

public class Gather extends Task{
	
	final static int BERRY = 247;
	public static int berries_collected = 0;

	public Gather(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		
		Tile island1 = new Tile(2504, 3094, 0);
		Tile island2 = new Tile(2520, 3078, 0);
		Area island_area = new Area(island1, island2);
		
		return ctx.backpack.select().count() != 28 && island_area.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		GroundItem berry = ctx.groundItems.select().id(BERRY).nearest().poll();
		
		if (!berry.inViewport())
		{
			ctx.camera.turnTo(berry.tile());
		}
		
		berry.interact("Take");
		Condition.sleep(1000);
		while(ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
		berries_collected++;
		System.out.println("getting berries");
	}

	public int get_berries()
	{
		return berries_collected;
	}
}
