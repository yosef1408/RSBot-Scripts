package m0tionl3ss.SandRunner.tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Equipment;
import org.powerbot.script.rt4.Game.Tab;
import org.powerbot.script.rt4.Magic;

import m0tionl3ss.SandRunner.util.Info;
import m0tionl3ss.SandRunner.util.Options;
import m0tionl3ss.SandRunner.util.Options.Mode;

public class Dead extends Task {
	Area bankArea = new Area(new Tile(2761,3477), new Tile(2753,3483));
	Area cwarsArea = new Area(new Tile(2438,3082), new Tile(2446,3097));
	private int[] rings = {2552,2554,2556,2558,2560,2562,2564,2566};
	public Dead(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		return Info.getInstance().isDead();
	}

	@Override
	public void execute() {
		if(Options.getInstance().getMode() == Mode.PVP)
		{
			ctx.inventory.select().name("Staff of air").poll().interact("Wield");
			
			ctx.magic.cast(Magic.Spell.CAMELOT_TELEPORT);
			Condition.wait(() -> bankArea.contains(ctx.players.local()),500,3);
		}
		else
		{
			ctx.inventory.select().id(rings).poll().interact("Wear");
			ctx.game.tab(Tab.EQUIPMENT);
			ctx.equipment.itemAt(Equipment.Slot.RING).interact("Castle Wars");
			Condition.wait(() -> cwarsArea.contains(ctx.players.local()),1000,3);
		}
		Info.getInstance().incrementDeadCounter();
		Info.getInstance().setDead(false);
		
	}

	@Override
	public String status() {
		// TODO Auto-generated method stub
		return null;
	}

}
