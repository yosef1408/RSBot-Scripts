package m0tionl3ss.CharterBuyer.util;

import org.powerbot.script.Locatable;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Game.Tab;
import org.powerbot.script.rt4.Inventory;

// Player specific patterns
public class Tools {

	public static void closeBank(ClientContext ctx, boolean useEscape) {

		if (useEscape) {
			ctx.input.send("{VK_ESCAPE}");
		} else {
			ctx.bank.close();
		}

	}

	public static boolean logout(ClientContext ctx) {
		boolean clickLogout = ctx.game.tab(Tab.LOGOUT);
		if (clickLogout) {
			if (ctx.widgets.widget(69).component(19).visible()) {
				return ctx.widgets.widget(69).component(19).click();
			} else {
				return ctx.widgets.widget(182).component(10).click();
			}
		}
		return false;

	}

	public static void turnCamera(ClientContext ctx, Locatable locatable, boolean useScroll) {

	}

	public static void make(ClientContext ctx, int number) {
		ctx.input.send(String.valueOf(number));
	}

	public static void dropLeftRightAll(ClientContext ctx, boolean useShift) {
		Inventory inventory = ctx.inventory;
		while (inventory.select().count() != 0 && !ctx.controller.isStopping()) {

			for (int index = 0; index <= 26; index += 4) {
				if (useShift) {
					ctx.input.send("{VK_SHIFT down}");
					inventory.itemAt(index).click();
					inventory.itemAt(index + 1).click();
				} else {
					inventory.itemAt(index).interact("Drop");
					inventory.itemAt(index + 1).interact("Drop");

				}
				if (index == 24) {
					index = -2;
				}
			}

		}
		if (useShift && inventory.select().count() == 0) {
			ctx.input.send("{VK_SHIFT up}");
		}
	}

	public static void regularDropAll(ClientContext ctx, boolean useShift) {
		ctx.inventory.select().forEach(item -> {
			if (useShift) {
				ctx.input.send("{VK_SHIFT down}");
				item.click();
			} else {
				item.interact("Drop");
			}
		});
		if (useShift) {
			ctx.input.send("{VK_SHIFT up}");
		}
	}
	
	public static String getTimeRunning(long runtime) {
		int seconds = (int) (runtime / 1000) % 60;
		int minutes = (int) ((runtime / (1000 * 60)) % 60);
		int hours = (int) ((runtime / (1000 * 60 * 60)) % 24);
		String runtimeFormated = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		return runtimeFormated;
	}

	public static int getTimeRunningMinutes(long runtime) {
		return (int) ((runtime / (1000 * 60)) % 60);
	}
	public static int getCurrentWorld(ClientContext ctx)
	{
		Component component = ctx.widgets.widget(69).component(2);
		String worldID = component.text().substring(17);
		if (worldID.indexOf(0) == '0')
		{
			
			return (int) worldID.indexOf(1);
		}
		else
		{
			return Integer.parseInt(worldID);
		}
		
	}
	// TODO :
	public static void dropDownUpAll(ClientContext ctx, boolean useShift) {
		int counter = 0;
		Inventory inventory = ctx.inventory;
		for (int cols = 0; cols < 4; cols++) {
			for (int rows = 0; rows < 7; rows++) {
				inventory.itemAt(cols).interact("Drop");
				System.out.println("Col" + counter++);
			}
		}
	}
}
