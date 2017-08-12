package needscroll.LimpwurtGrabber.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GroundItem;

import needscroll.LimpwurtGrabber.Task;

public class Gather extends Task{
	
	final static int ROOT = 225;
	public static int ROOTS_GRABBED = 0;
	public static String STATUS = "";

	public Gather(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		//Tile limp = new Tile(1116, 4574, 0); // limpwurt root area
		Tile limp1 = new Tile(1102, 4560, 0);
		Tile limp2 = new Tile(1140, 4593, 0);
		
		Area limp_area = new Area(limp1, limp2);
		return ctx.backpack.select().count() != 28 && limp_area.contains(ctx.players.local());
	}

	@Override
	public void execute() 
	{	
		STATUS = "Getting Roots";
		Tile limp = new Tile(1116, 4574, 0);
		GroundItem root = ctx.groundItems.select().id(ROOT).nearest().poll();
		
		if (!(root.id() == ROOT))
		{
			Condition.sleep(3000);
			return;
		}
		
		ctx.camera.turnTo(root.tile());
		if (!root.inViewport())
		{
			ctx.movement.step(limp);
			Condition.sleep(1000);
			while(ctx.players.local().inMotion())
			{
				Condition.sleep(1000);
			}
			ctx.camera.turnTo(root.tile());
		}
		
		if (root.interact("Take"))
		{
			Condition.sleep(1000);
			while(ctx.players.local().inMotion())
			{
				Condition.sleep(1000);
			}
		}
		else
		{
			ctx.movement.step(limp);
			Condition.sleep(1000);
			while(ctx.players.local().inMotion())
			{
				Condition.sleep(1000);
			}
			ctx.camera.turnTo(root.tile());
		}
		ROOTS_GRABBED++;
		STATUS = "";
	}
	
	public int get_roots()
	{
		return ROOTS_GRABBED;
	}
	
	public String get_status()
	{
		return STATUS;
	}
}
