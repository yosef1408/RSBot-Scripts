package m0tionl3ss.JewelEnchanter.tasks;

import java.util.Arrays;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Magic;

import m0tionl3ss.JewelEnchanter.util.Info;
import m0tionl3ss.JewelEnchanter.util.Tools;

public class Bank extends Task {
	private int item = Info.getInstance().getItemToWithdrawId();
	private String[] itemsToKeep = { "Cosmic rune", "Water rune", "Air rune", "Fire rune", "Earth rune", "Soul Rune",
			"Blood rune" };

	public Bank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.inventory.select().id(item).isEmpty();
	}

	@Override
	public void execute() {
		System.out.println(getClass().getSimpleName());
		if (ctx.menu.items().length == 1) {
			ctx.input.click(true);
		}
		if (ctx.bank.inViewport()) {
			if (ctx.bank.opened()) {
				if (ctx.inventory.select().name("Cosmic rune").count() == 0
						&& ctx.bank.select().name("Cosmic rune").count() == 1) {
					ctx.bank.withdraw(ctx.bank.select().name("Cosmic rune").poll().id(),
							org.powerbot.script.rt4.Bank.Amount.ALL);
				}
				ctx.bank.depositAllExcept(itemsToKeep);
				boolean canWithdrawItems = ctx.bank.withdraw(item, org.powerbot.script.rt4.Bank.Amount.ALL);
				if (canWithdrawItems) {
					Tools.closeBank(ctx, Info.getInstance().getCloseBankUsingeEscape());
				} else {
					Tools.closeBank(ctx, Info.getInstance().getCloseBankUsingeEscape());
					Tools.logout(ctx);
					ctx.controller.stop();
				}

			} else {
				ctx.bank.open();
			}
		} else {
			ctx.camera.turnTo(ctx.bank.nearest());
		}

	}

}
