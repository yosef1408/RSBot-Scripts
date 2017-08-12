package needscroll.LimpwurtGrabber.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import needscroll.LimpwurtGrabber.Task;

public class WalkHills extends Task{
	
	final static int DOOR = 1804;
	final static int LADDER = 12389;
	public static String STATUS = "";

	public WalkHills(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		Tile overworld1 = new Tile(3107, 3421, 0); // overworld area
		Tile overworld2 = new Tile(3200, 3489, 0);
		Area overworld = new Area(overworld1, overworld2);
		
		return ctx.backpack.select().count() < 28 && overworld.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		STATUS = "Walking to Roots";
		//Tile house_o = new Tile(3117, 3447, 0); // house area
		Tile house_o1 = new Tile(3114, 3449, 0);
		Tile house_o2 = new Tile(3120, 3444, 0);
		Area house_area_o = new Area(house_o1, house_o2);
		
		if(!house_area_o.contains(ctx.players.local()))
		{
			go_house();
		}
		if (house_area_o.contains(ctx.players.local()))
		{
			go_door_ladder();
		}
		STATUS = "";
	}
	
	private void go_house()
	{
		Tile house_o = new Tile(3117, 3447, 0);
		
		ctx.movement.step(house_o);
		Condition.sleep(1000);
		while(ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
	}
	
	private void go_door_ladder()
	{
		GameObject door = ctx.objects.select().id(DOOR).nearest().poll();
		GameObject ladder = ctx.objects.select().id(LADDER).nearest().poll();
		
		ctx.camera.turnTo(door.tile());
		door.interact("Open");
		Condition.sleep(1000);
		while(ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
		
		ladder.interact("Climb-Down");
		Condition.sleep(1000);
		while(ctx.players.local().inMotion() || ctx.players.local().animation() == 827)
		{
			Condition.sleep(1000);
		}
	}

	public String get_status()
	{
		return STATUS;
	}
}
