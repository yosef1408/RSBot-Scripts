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
import org.powerbot.script.rt4.Worlds;

import m0tionl3ss.CharterBuyer.util.Info;
import m0tionl3ss.CharterBuyer.util.Options;
import m0tionl3ss.CharterBuyer.util.Tools;

public class BuyFromCharterShop extends Task implements MessageListener {

	private Area charterMemberArea = new Area(new Tile(2803, 3416), new Tile(2791, 3413));
	private int[] charterNpcs = { 1334, 1331 };
	private int[] itemsToBuy;
	private int oldAmount;

	public BuyFromCharterShop(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		Player localPlayer = ctx.players.local();
		// OR nakijken
		// member area veranderen naar docks2

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
			if (!shopInterface.visible() && !ctx.players.local().inMotion())
			{
				charterMember.interact("Trade");
			}
			else
			{		
				Component[] shopItems = ctx.widgets.widget(300).component(2).components();
				for (int itemToBuy : itemsToBuy)
				{
					for (Component shopItem : shopItems)
					{
						if (itemToBuy == shopItem.itemId() && shopItem.itemStackSize() > 1)
						{
							while(shopItem.itemStackSize() > 0 && ctx.inventory.select().count() < 28 && ctx.inventory.select().name("Coins").count(true) > 4)
							{
								if (shopItem.interact("Buy 10"))	
								{
									// add a delay?
									//									int newAmount = ctx.inventory.select().id(itemsToBuy).count();
									//									if (oldAmount != newAmount)
									//									{
									//										int x = newAmount - oldAmount;
									//										Info.getInstance().addItemsBought(x);
									//										oldAmount = newAmount;
									//									}
									//									


								}
							}


						}
						else if(itemToBuy == shopItem.itemId() && shopItem.itemStackSize() < 2)
						{
							// Add option so user can select true or false
							boolean isShopInterfaceClosed = ctx.widgets.close(ctx.widgets.widget(300).component(1).components(),Options.getInstance().getUseEscape());
							if (isShopInterfaceClosed)
								hopWorld();
							break;
						}


					}
				}
			}
		}
		else
		{
			ctx.camera.turnTo(charterMember);
		}


	}
	private void hopWorld()
	{

		ctx.worlds.open();

		//boolean status =ctx.widgets.scroll(list(), ctx.widgets.widget(69).component(7).component(396), bar());
		//boolean status = ctx.widgets.scroll(ctx.widgets.widget(69).component(7).component(396),ctx.widgets.widget(69).component(7), bar(), true);
		//boolean status = ctx.widgets.scroll(ctx.widgets.widget(69).component(7).component(396),ctx.widgets.widget(69).component(7), ctx.widgets.widget(69).component(15), false);
		//System.out.println("Scrolled or in view :" + status);
		World world = ctx.worlds.select().joinable().types(World.Type.MEMBERS).shuffle().poll();
		boolean worldIsVisible = ctx.widgets.scroll(worldComponent(world.id()), ctx.widgets.widget(69).component(7), ctx.widgets.widget(69).component(15), Options.getInstance().getUseScroll());
		Condition.wait(() -> worldIsVisible,450,3);
		//System.out.println(world.id());
		//System.out.println(world.valid());

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
		oldAmount = ctx.inventory.select().id(itemsToBuy).count();
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
