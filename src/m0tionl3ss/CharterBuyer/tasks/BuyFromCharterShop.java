package m0tionl3ss.CharterBuyer.tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Player;
import org.powerbot.script.rt4.World;
import m0tionl3ss.CharterBuyer.util.Options;
import m0tionl3ss.CharterBuyer.util.Tools;

public class BuyFromCharterShop extends Task implements MessageListener {

	private Area charterMemberArea = new Area(new Tile(2803, 3416), new Tile(2791, 3413));
	private int[] itemsToBuy;
	private int counter = 0;

	public BuyFromCharterShop(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		Player localPlayer = ctx.players.local();

		if ((charterMemberArea.contains(localPlayer) && ctx.inventory.select().count() < 28) && ctx.inventory.select().name("Coins").count(true) > 4 ) {
			return true;
		}
		return false;
	}

	@Override
	public void execute() {
		System.out.println(this.getClass().getSimpleName());
		Npc charterMember = ctx.npcs.select().name("Trader Crewmember").nearest().poll();
		Component shopInterface = ctx.widgets.widget(300).component(0);
		if (charterMember.inViewport())
		{
			System.out.println(!shopInterface.visible() && !ctx.players.local().inMotion());
			if (!shopInterface.visible() && !ctx.players.local().inMotion())
			{
				charterMember.interact("Trade");
				Condition.wait(() -> shopInterface.visible(),400,5);
			}
			else
			{		
				for(int itemToBuy : itemsToBuy)
				{
					// 401,1781,1783
					Component item = itemComponent(itemToBuy);
					if (item != null)
					{
						if (counter != itemsToBuy.length && item.itemStackSize() > 0 && ctx.inventory.select().count() < 28)
						{
							item.interact("Buy 10");
						}
						if (item.itemStackSize() < 1 && counter < itemsToBuy.length)
						{
							
							counter++;
							System.err.println(counter);
						}
						
					}
					
				}
				if (counter == itemsToBuy.length)
				{
					ctx.widgets.close(ctx.widgets.widget(300),Options.getInstance().getUseEscape());
					hopWorld();
					counter = 0;
				}
				
			}
		}
		else
		{
			ctx.camera.turnTo(charterMember);
		}


	}
	private Component itemComponent(int itemId)
	{
		
		for(Component c : ctx.widgets.widget(300).component(2).components())
		{
			if (c.itemId() == itemId )
			{
				return c;
			}
		}
		return null;
	}
	private void hopWorld()
	{

		ctx.worlds.open();
		World world = ctx.worlds.select(w -> w.id() < 100 && w.id() != Tools.getCurrentWorld(ctx)).joinable().types(World.Type.MEMBERS).poll();
		boolean worldIsVisible = ctx.widgets.scroll(worldComponent(world.id()), ctx.widgets.widget(69).component(7), ctx.widgets.widget(69).component(15), Options.getInstance().getUseScroll());
		Condition.wait(() -> worldIsVisible,450,3);
		world.hop();
	}
	private Component worldComponent(int number)
	{
		for(Component c : ctx.widgets.widget(69).component(7).components())
		{
			if (c.width() == 25 && c.height() == 16 && c.text().equals(String.valueOf(number)))
			{
				return c;
			}
		}
		return null;
	}

	public void setItemsToBuy(int... ids)
	{
		itemsToBuy = ids;
		
	}

	@Override
	public void messaged(MessageEvent message) {
		if (message.text().contains("enough coins"))
		{
			Tools.logout(ctx);
			ctx.controller.stop();
		}

	}

}
