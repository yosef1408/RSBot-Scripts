package needscroll.BanannaGrabber.Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Equipment;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;
import needscroll.BanannaGrabber.Task;

public class CastleWarsBank extends Task{
	
	final static int CASTLE_BANK = 83634;
	final static int[] BASKETS = {5376, 5408, 5410, 5412, 5414, 5416};
	final static int[] GLORY = {1712, 1710, 1708, 1706, 1704};
	final static int[] DUELING = {2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566};

	public CastleWarsBank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().count() == 28;
	}

	@Override
	public void execute() 
	{
		go_bank();

		while (!ctx.bank.open())
		{
			use_bank();
		}
		
		if (ctx.bank.open())
		{
			get_items();
		}
		
		go_back();
	}
	
	private void go_bank()
	{
		Component ring = ctx.widgets.widget(1464).component(14).component(12);
		
		ring.interact("Castle Wars");
		Condition.sleep(6000);
	}
	
	private void use_bank()
	{
		if (ctx.bank.open())
		{
			return;
		}
			
		GameObject bank = ctx.objects.select().id(CASTLE_BANK).nearest().poll();
		bank.interact("Use");
		Condition.sleep(4000);
		while (ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
	}
	
	private void get_items()
	{
		Item amulet_w = ctx.equipment.itemAt(Equipment.Slot.NECK);
		Item ring_w = ctx.equipment.itemAt(Equipment.Slot.RING);
		boolean ring_fail = ring_failing(ring_w);
		boolean amulet_fail = amulet_w.id() == GLORY[4];
		
		ctx.bank.depositInventory();
		Condition.sleep(2000);
		ctx.bank.withdraw(BASKETS[0], 23);
		Condition.sleep(2000);
		
		if (ring_fail || amulet_fail)
		{
			ctx.bank.depositEquipment();
			Condition.sleep(2000);
			
			if (ring_fail)
			{
				ctx.bank.withdraw(DUELING[0], 1);
			}
			else
			{
				ctx.bank.withdraw(ring_w.id(), 1);
			}
			
			if (amulet_fail)
			{
				ctx.bank.withdraw(GLORY[0], 1);
			}
			else
			{
				ctx.bank.withdraw(amulet_w.id(), 1);
			}
		}
		Item[] inv = ctx.backpack.items();
		
		inv[23].interact("Wear");
		Condition.sleep(2000);
		inv[24].interact("Wear");
		Condition.sleep(2000);
		
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
	
	private void go_back()
	{
		Component amulet = ctx.widgets.widget(1464).component(14).component(2);
		amulet.interact("Karamja");
	}

}
