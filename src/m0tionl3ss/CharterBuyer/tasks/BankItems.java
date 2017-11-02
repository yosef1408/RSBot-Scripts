package m0tionl3ss.CharterBuyer.tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Inventory;
import org.powerbot.script.rt4.Player;

import m0tionl3ss.CharterBuyer.util.Options;
import m0tionl3ss.CharterBuyer.util.Tools;

public class BankItems extends Task
{
	private Area catherbyBankArea = new Area(new Tile(2802,3448), new Tile(2815,3434)); 
	Player localPlayer = ctx.players.local();
	public BankItems(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		Inventory inventory = ctx.inventory;
		Player localPlayer = ctx.players.local();
		// NAKIJKEN
		if ((catherbyBankArea.contains(localPlayer) && inventory.select().count() > 27)  || (ctx.inventory.select().name("Coins").count(true) < 5 && !ctx.inventory.select().isEmpty()))
		{
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		Bank bank = ctx.bank;
		GameObject bankBooth = ctx.objects.select().name("Bank Booth").nearest().poll();
		System.out.println(this.getClass().getSimpleName());
		if(bank.inViewport())
		{
			if (!bank.opened())
			{
				bank.open();
				if (ctx.inventory.select().name("Coins").count(true) < 5)
				{
					bank.depositInventory();
					Tools.closeBank(ctx, Options.getInstance().getUseEscape());
				}	
				else
				bank.depositAllExcept("Coins");
				
			}
			else
			{
				Tools.closeBank(ctx, Options.getInstance().getUseEscape());
			}
		}
		else
		{
			ctx.camera.turnTo(bankBooth);
		}
	}

}
