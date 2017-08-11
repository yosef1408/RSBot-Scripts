package needscroll.JangerBerriesGrabber.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Equipment;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;

import needscroll.JangerBerriesGrabber.Task;

public class Banking extends Task{
	
	final static int[] DUELING = {2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566};
	final static int CASTLE_BANK = 83634;
	final static int ROPE = 954;

	public Banking(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().count() == 28;
	}

	@Override
	public void execute() {
		
		Tile bank1 = new Tile(2450, 3097, 0);
		Tile bank2 = new Tile(2437, 3082, 0);
		Area bank_area = new Area(bank1, bank2);
		
		if (!bank_area.contains(ctx.players.local()))
		{
			go_bank();
			System.out.println("tele bank");
		}
		if (bank_area.contains(ctx.players.local()) && !ctx.bank.open())
		{
			open_bank();
			System.out.println("open bank");
		}
		if (bank_area.contains(ctx.players.local()) && ctx.bank.open())
		{
			get_items();
			System.out.println("get items");
		}
		
	}
	
	private void go_bank()
	{
		Component ring = ctx.widgets.widget(1464).component(14).component(12);
		
		ring.interact("Castle Wars");
		Condition.sleep(6000);
	}
	
	private void open_bank()
	{
		if (ctx.bank.open())
		{
			return;
		}
			
		GameObject bank = ctx.objects.select().id(CASTLE_BANK).nearest().poll();
		ctx.camera.turnTo(bank.tile());
		bank.interact("Use");
		Condition.sleep(4000);
		while (ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
	}
	
	private void get_items()
	{
		Item ring_w = ctx.equipment.itemAt(Equipment.Slot.RING);
		boolean ring_fail = ring_failing(ring_w);
		
		ctx.bank.depositInventory();
		Condition.sleep(2000);
		ctx.bank.withdraw(ROPE, 1);
		Condition.sleep(2000);
		
		if (ring_fail)
		{
			ctx.bank.withdraw(DUELING[0], 1);
			Condition.sleep(2000);
			Item ring = ctx.backpack.select().id(DUELING).poll();
			ring.interact("Wear");
		}
		ctx.bank.close();
	}
	
	private boolean ring_failing(Item ring)
	{
		boolean fail = true;

		for (int counter = 0; counter < DUELING.length - 1; counter++)
		{
			if (ring.id() == DUELING[counter])
			{
				fail = false;
			}
		}
		
		return fail;
	}

}
