package needscroll.GrapeGrabber.Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Interactive;

import needscroll.GrapeGrabber.Task;

public class Banking extends Task{
	
	final static int DOOR_ID = 2712;
	final static int[] STAIRS = {24073, 24074, 24075};
	final static int BANK_ID = 87989;
	final int[] bounds = {-240, 232, 0, -900, 200, 250};

	public Banking(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().count() == 28;
	}

	@Override
	public void execute() {
		go_ground();
		open_door();
		walk_bank();
		use_bank();
		walk_guild();
		open_door();
		goup();
	}
	
	private void go_ground()
	{
		int floor = ctx.players.local().tile().floor();
		
		if (floor == 1)
		{
			godown();
		}
		if (floor == 2)
		{
			godown();
			godown();
		}
	}
	
	private void godown()
	{
		GameObject stairs = ctx.objects.select().id(STAIRS).nearest().poll();
		stairs.interact("Climb-down");
		Condition.sleep(3000);
	}

	private void open_door()
	{
		GameObject door = ctx.objects.select().id(DOOR_ID).each(Interactive.doSetBounds(bounds)).nearest().limit(1).poll();
		
		if (door.valid())
		{
			if (bad_cam_door())
			{
				fix_cam_door();
			}
			
			door.interact(false, "Open");
			Condition.sleep(1000);
			while (ctx.players.local().inMotion())
			{
				Condition.sleep(1000);
			}
		}
	}
	
	private boolean bad_cam_door()
	{
		boolean bad = false;
		
		int yaw = ctx.camera.yaw();
		int pitch = ctx.camera.pitch();
		
		if (yaw < 160 || yaw > 210)
		{
			bad = true;
		}
		
		if (pitch < 56 || pitch > 45)
		{
			bad = true;
		}
		
		return bad;
	}
	
	private void fix_cam_door()
	{
		ctx.camera.angle('s');
		Condition.sleep(1000);

		ctx.camera.pitch(47);
		Condition.sleep(1000);
	}
	
	private void walk_bank()
	{
		Tile[] pathToBank = new Tile[] {new Tile(3153, 3448, 0), new Tile(3160, 3451, 0)};
		
		for (int counter = 0; counter < pathToBank.length; counter++)
		{
			ctx.movement.step(pathToBank[counter]);
			Condition.sleep(1000);
			
			while(ctx.players.local().inMotion())
			{
				Condition.sleep(1000);
			}
		}
	}
	
	private void use_bank()
	{
		GameObject bank = ctx.objects.select().id(BANK_ID).nearest().poll();
		bank.interact("Open Bank");
		
		Condition.sleep(1000);
		while (ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
		
		while(!ctx.bank.open())
		{
			bank.interact("Open Bank");
			Condition.sleep(5000);
		}
		
		if (ctx.bank.open())
		{
			ctx.bank.depositInventory();
			Condition.sleep(1000);
			ctx.bank.close();
		}
	}
	
	private void walk_guild()
	{
		Tile[] pathToBank = new Tile[] {new Tile(3156, 3448, 0), new Tile(3147, 3444, 0), new Tile(3143, 3443, 0)};
		
		for (int counter = 0; counter < pathToBank.length; counter++)
		{
			ctx.movement.step(pathToBank[counter]);
			Condition.sleep(1000);
			
			while(ctx.players.local().inMotion())
			{
				Condition.sleep(1000);
			}
		}
	}
	
	private void goup()
	{
		GameObject stairs = ctx.objects.select().id(STAIRS).nearest().poll();
		stairs.interact("Climb-up");
		Condition.sleep(3000);
	}
}
