package m0tionl3ss.SandRunner.tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Equipment;
import org.powerbot.script.rt4.Game.Tab;
import org.powerbot.script.rt4.Magic;
import org.powerbot.script.rt4.Player;
import m0tionl3ss.SandRunner.util.Options;

public class TeleportToBank extends Task{
	Area bankArea = new Area(new Tile(2761,3475), new Tile(2753,3483));
	Area cwarsArea = new Area(new Tile(2438,3082), new Tile(2446,3097));
	public TeleportToBank(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		Player localPlayer = ctx.players.local();
		if (Options.getInstance().getMode() == Options.Mode.PVP)
		{
			if (ctx.inventory.select().name("Bucket").isEmpty() && !bankArea.contains(localPlayer))
				return true;
		}
		else
		{
			if (ctx.inventory.select().name("Bucket").isEmpty() && !cwarsArea.contains(localPlayer))
				return true;
		}
		
		return false;
	}

	@Override
	public void execute() {
	
		if (Options.getInstance().getMode() == Options.Mode.PVP)
		{
			ctx.magic.cast(Magic.Spell.CAMELOT_TELEPORT);
			//ctx.inventory.select().name("Camelot teleport").poll().interact("Break");
			Condition.wait(() -> bankArea.contains(ctx.players.local()),1000,3);
		}
		else
		{
			ctx.game.tab(Tab.EQUIPMENT);
			ctx.equipment.itemAt(Equipment.Slot.RING).interact("Castle Wars");
			Condition.wait(() -> cwarsArea.contains(ctx.players.local()),1000,3);
		}
		
		
	}

	@Override
	public String status() {
		return "Teleporting to bank...";
	}

}
