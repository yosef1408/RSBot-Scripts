package Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Equipment;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;

import needscroll.BanannaGrabber.Task;

public class EdgevilleBank extends Task{
	
	final static int EDGE_BANK = 42378;
	final static int[] BASKETS = {5376, 5408, 5410, 5412, 5414, 5416};
	final static int[] GLORY = {1712, 1710, 1708, 1706, 1704};

	public EdgevilleBank(ClientContext ctx) {
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
		Item amulet_w = ctx.equipment.itemAt(Equipment.Slot.NECK);
		boolean amulet_fail = amulet_w.id() == GLORY[4];
		
		if (amulet_fail)
		{
			ctx.input.send("t");
			Condition.sleep(2000);
			ctx.input.send("e");
			Condition.sleep(25000);
			walk_tobank();
		}
		else
		{
			amulet_w.interact("Edgeville");
			Condition.sleep(6000);
		}
		
	}
	
	private void use_bank()
	{
		if (ctx.bank.open())
		{
			return;
		}
			
		GameObject bank = ctx.objects.select().id(EDGE_BANK).nearest().poll();
		bank.interact("Open Bank");
		Condition.sleep(4000);
		while (ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
	}
	
	private void get_items()
	{
		Item amulet_w = ctx.equipment.itemAt(Equipment.Slot.NECK);
		boolean amulet_fail = amulet_w.id() == GLORY[4];
		
		ctx.bank.depositInventory();
		Condition.sleep(2000);
		ctx.bank.withdraw(BASKETS[0], 23);
		Condition.sleep(2000);
		
		if (amulet_fail)
		{
			ctx.bank.depositEquipment();
			Condition.sleep(2000);
			
			ctx.bank.withdraw(GLORY[0], 1);
		}
		Item[] inv = ctx.backpack.items();
		
		inv[23].interact("Wear");
		Condition.sleep(2000);
		
		ctx.bank.close();
	}
	
	private void walk_tobank()
	{
		Tile[] pathToBank = new Tile[] {new Tile(3081, 3500, 0), new Tile(3092, 3497, 0)};
		
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
	
	private void go_back()
	{
		Component amulet = ctx.widgets.widget(1464).component(14).component(2);
		amulet.interact("Karamja");
	}

}