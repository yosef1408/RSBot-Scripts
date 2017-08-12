package needscroll.LimpwurtGrabber.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Equipment;
import org.powerbot.script.rt6.Item;

import needscroll.LimpwurtGrabber.Task;

public class Teleport extends Task{
	
	public static String STATUS = "";

	public Teleport(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		//Tile bank = new Tile(3162, 3465, 0); // ge teleport area
		Tile bank1 = new Tile(3169, 3457, 0);
		Tile bank2 = new Tile(3144, 3484, 0);
		Area bank_tele_area = new Area(bank1, bank2);
		
		return ctx.backpack.select().count() == 28 && !bank_tele_area.contains(ctx.players.local());
	}

	@Override
	public void execute() 
	{
		STATUS = "Teleporting Bank";
		Item ring_w = ctx.equipment.itemAt(Equipment.Slot.RING);
		
		ring_w.interact("Grand Exchange");
		Condition.sleep(6000);
		STATUS = "";
	}

	public String get_status()
	{
		return STATUS;
	}
}
