package m0tionl3ss.CharterBuyer.tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game.Tab;

import m0tionl3ss.CharterBuyer.util.Tools;

public class Quit extends Task{

	private Area docks2 = new Area(new Tile(2803,3416), new Tile(2791,3413));
	public  final Tile[] path = {new Tile(2809, 3440, 0), new Tile(2809, 3437, 0), new Tile(2807, 3434, 0), new Tile(2804, 3431, 0), new Tile(2803, 3428, 0), new Tile(2803, 3425, 0), new Tile(2803, 3422, 0), new Tile(2804, 3419, 0), new Tile(2803, 3416, 0), new Tile(2800, 3414, 0)};

	public Quit(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		return ctx.inventory.isEmpty();
	}

	@Override
	public void execute() {
		System.out.println(getClass().getSimpleName());
		Tools.logout(ctx);
		ctx.controller.stop();
		
	}

}
