package Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;

import needscroll.FreeRunecrafter.Task;

public class Craft extends Task{
	
	final static int ECCENCE = 7936;

	public Craft(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}
	
	protected void walk_alter(Tile alter_tile)
	{	
		ctx.movement.step(alter_tile);
		Condition.sleep(1000);
		while(ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
	}
	
	protected void enter_alter(int alter_id)
	{
		GameObject alter_r = ctx.objects.select().id(alter_id).nearest().poll();
		
		alter_r.interact("Enter");
		Condition.sleep(5000);
		while(ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
	}

	protected int craft(int alter_inside_id, int rune_id)
	{
		GameObject alter_i = ctx.objects.select().id(alter_inside_id).nearest().poll();
		ctx.camera.turnTo(alter_i.tile());
		
		alter_i.interact("Craft-rune");
		Condition.sleep(1000);
		while(ctx.players.local().inMotion() || ctx.players.local().animation() == 23250)
		{
			Condition.sleep(1000);
		}
		
		Item air_rune = ctx.backpack.select().id(rune_id).poll();
		return air_rune.stackSize();
	}
	
	protected void leave(int portal_id)
	{
		GameObject exit = ctx.objects.select().id(portal_id).nearest().poll();
		ctx.camera.turnTo(exit.tile());
		
		exit.interact("Enter");
		Condition.sleep(5000);
		while(ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
	}
	
	protected void go_bank(Tile bank_tile)
	{	
		ctx.movement.step(bank_tile);
		Condition.sleep(1000);
		while(ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
	}
	
	private void banking(int bank_id)
	{
		GameObject bank = ctx.objects.select().id(bank_id).nearest().poll();
		ctx.camera.turnTo(bank.tile());
		
		bank.interact("Bank");
		Condition.sleep(1000);
		while(ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
	}
	
	protected void get_items(int bank_id)
	{
		while (!ctx.bank.open())
		{
			banking(bank_id);
		}
		
		if (ctx.bank.open())
		{
			ctx.bank.depositInventory();
			Condition.sleep(2000);
			ctx.bank.withdraw(ECCENCE, 28);
			Condition.sleep(2000);
			ctx.bank.close();
		}
	}
}
