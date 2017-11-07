package m0tionl3ss.CharterBuyer.tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Player;


public class RunToCharterMember extends Task {
	private Area docks2 = new Area(new Tile(2803,3416), new Tile(2791,3413));
	public  final Tile[] path = {new Tile(2809, 3440, 0), new Tile(2809, 3437, 0), new Tile(2807, 3434, 0), new Tile(2804, 3431, 0), new Tile(2803, 3428, 0), new Tile(2803, 3425, 0), new Tile(2803, 3422, 0), new Tile(2804, 3419, 0), new Tile(2803, 3416, 0), new Tile(2800, 3414, 0)};
	public RunToCharterMember(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		Player localPlayer = ctx.players.local();
		if (!docks2.contains(localPlayer) && ctx.inventory.select().name("Coins").count(true) > 50 && ctx.inventory.select().count() < 27)
		{
			return true;
		}
		return false;
		
	}

	@Override
	public void execute() {
		System.out.println(this.getClass().getSimpleName());
		ctx.movement.newTilePath(path).randomize(1, 1).traverse();
		}

	@Override
	public String status() {
		return "Running to shop..";
	}

}
