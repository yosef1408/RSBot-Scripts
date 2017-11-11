package m0tionl3ss.SandRunner.tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Magic;
import org.powerbot.script.rt4.Player;

public class TeleportToHouse extends Task {

	Area bankArea = new Area(new Tile(2761,3475), new Tile(2753,3483));
	Area portalAndSandpitArea = new Area(new Tile(2539,3106),new Tile(2548,3093));
	public TeleportToHouse(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		Player localPlayer = ctx.players.local();
		//if (Options.getInstance().getMode() == )
		if (!ctx.inventory.select().name("Bucket").isEmpty() && !ctx.inventory.select().name("Law rune").isEmpty() && !ctx.inventory.select().name("Earth rune").isEmpty() && !portalAndSandpitArea.contains(localPlayer))
			return true;
		return false;
	}

	@Override
	public void execute() {
		ctx.magic.cast(Magic.Spell.TELEPORT_TO_HOUSE);
		Condition.wait(() -> portalAndSandpitArea.contains(ctx.players.local()),300,3);
	}

	@Override
	public String status() {
		return "Teleporting to house...";
	}

}
