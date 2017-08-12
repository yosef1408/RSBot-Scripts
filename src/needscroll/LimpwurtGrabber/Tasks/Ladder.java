package needscroll.LimpwurtGrabber.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import needscroll.LimpwurtGrabber.Task;

public class Ladder extends Task{
	
	final static int LADDER = 12389;

	public Ladder(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		Tile ladder1 = new Tile(3118, 3454, 0); // ladder area
		Tile ladder2 = new Tile(3112, 3449, 0);
		Area ladder = new Area(ladder1, ladder2);
		
		return ctx.backpack.select().count() < 28 && ladder.contains(ctx.players.local());
	}

	@Override
	public void execute() 
	{
		GameObject ladder = ctx.objects.select().id(LADDER).nearest().poll();
		
		ladder.interact("Climb-Down");
		Condition.sleep(1000);
		while(ctx.players.local().inMotion() || ctx.players.local().animation() == 827)
		{
			Condition.sleep(1000);
		}
		
	}

}
