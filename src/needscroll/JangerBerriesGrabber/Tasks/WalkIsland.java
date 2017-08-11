package needscroll.JangerBerriesGrabber.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Interactive;
import org.powerbot.script.rt6.Item;

import needscroll.JangerBerriesGrabber.Task;

public class WalkIsland extends Task{
	
	final static int ROPE = 954;
	final static int BRANCH = 2326;
	final static int[] BOUNDS = {-128, 128, -1700, -1900, -128, 128};

	public WalkIsland(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		Tile island1 = new Tile(2504, 3094, 0);
		Tile island2 = new Tile(2520, 3078, 0);
		Area island_area = new Area(island1, island2);
		
		return ctx.backpack.select().count() != 28 && !island_area.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		Tile island_tree = new Tile(2493, 3086, 0);
		Tile island_tree1 = new Tile(2491, 3090, 0);
		Tile island_tree2 = new Tile(2495, 3084, 0);
		Area island_tree_area = new Area(island_tree1, island_tree2);
		
		if (!island_tree_area.contains(ctx.players.local()))
		{
			walk_tree();
			System.out.println("walking island");
		}
		if (island_tree_area.contains(ctx.players.local()))
		{
			swing_island();
			System.out.println("swinging");
		}
	}
	
	private void walk_tree()
	{
		Tile island_tree = new Tile(2493, 3086, 0);
		
		ctx.movement.step(island_tree);
		Condition.sleep(1000);
		while(ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
	}

	private void swing_island()
	{
		Item rope = ctx.backpack.select().id(ROPE).poll();
		GameObject branch = ctx.objects.select().id(BRANCH).each(Interactive.doSetBounds(BOUNDS)).nearest().poll();
		
		rope.interact("Use");
		if (ctx.backpack.itemSelected())
		{
			ctx.camera.turnTo(branch.tile());
			branch.interact("Use");
			Condition.sleep(5000);
			while(ctx.players.local().inMotion() || ctx.players.local().animation() == 775|| ctx.players.local().animation() == 751)
			{
				Condition.sleep(1000);
			}
		}
	}
}
