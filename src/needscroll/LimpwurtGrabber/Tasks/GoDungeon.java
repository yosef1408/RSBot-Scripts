package needscroll.LimpwurtGrabber.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import needscroll.LimpwurtGrabber.Task;

public class GoDungeon extends Task{
	
	final static int ENTRANCE = 52853;
	public static String STATUS = "";

	public GoDungeon(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		Tile dungeon1 = new Tile(3090, 9820, 0); // overworld area
		Tile dungeon2 = new Tile(3130, 9856, 0);
		Area dungeon = new Area(dungeon1, dungeon2);
		
		return ctx.backpack.select().count() < 28 && dungeon.contains(ctx.players.local());
	}

	@Override
	public void execute() 
	{
		STATUS = "Walking to Roots";
		//Tile entrance = new Tile(3104, 9826, 0); // entrance area
		Tile entrance1 = new Tile(3101, 9829, 0);
		Tile entrance2 = new Tile(3108, 9824, 0);
		Area entrance_area = new Area(entrance1, entrance2);
		
		if (!entrance_area.contains(ctx.players.local()))
		{
			go_entrance();
		}
		if (entrance_area.contains(ctx.players.local()))
		{
			enter_entrance();
		}
		STATUS = "";
	}
	
	private void go_entrance()
	{
		Tile entrance = new Tile(3104, 9826, 0); // entrance area
		
		ctx.movement.step(entrance);
		Condition.sleep(1000);
		while(ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
	}
	
	private void enter_entrance()
	{
		GameObject entrance = ctx.objects.select().id(ENTRANCE).nearest().poll();
		
		entrance.interact("Enter");
		Condition.sleep(1000);
		while(ctx.players.local().inMotion() || ctx.players.local().animation() == 13288)
		{
			Condition.sleep(1000);
		}
	}
	
	public String get_status()
	{
		return STATUS;
	}

}
