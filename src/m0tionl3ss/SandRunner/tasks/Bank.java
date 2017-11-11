package m0tionl3ss.SandRunner.tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Player;

import m0tionl3ss.SandRunner.util.Info;
import m0tionl3ss.SandRunner.util.Options;
import m0tionl3ss.SandRunner.util.Tools;
import m0tionl3ss.SandRunner.util.Options.Mode;

public class Bank extends Task {
	Area bankArea = new Area(new Tile(2761,3475), new Tile(2753,3483));
	Area cwarsArea = new Area(new Tile(2438, 3082), new Tile(2446, 3097));

	public Bank(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		Player localPlayer = ctx.players.local();
		if (Options.getInstance().getMode() == Options.Mode.PVP) {
			if (bankArea.contains(localPlayer)
					&& (!ctx.inventory.select().name("Bucket of sand").isEmpty() || ctx.inventory.select().count() < 3))
				return true;
		} else {
			if (cwarsArea.contains(localPlayer)
					&& (!ctx.inventory.select().name("Bucket of sand").isEmpty() || ctx.inventory.select().count() < 3))
				return true;
		}

		return false;
	}

	@Override
	public void execute() {
		System.out.println(ctx.bank.inViewport());
		if (ctx.bank.inViewport()) {
			if (ctx.bank.opened()) {
				ctx.bank.depositAllExcept("Law rune", "Earth rune", "Camelot teleport");

				// Duel
				if (Options.getInstance().getMode() == Options.Mode.CWARS && Info.getInstance().getWithdrawDuelRing()) {
					if (!ctx.bank.select().id(2552).isEmpty()) {
						ctx.bank.withdraw(2552, 1);
						Tools.closeBank(ctx, Options.getInstance().getUseEscape());
						Tools.openInventoryIfClosed(ctx);
						Info.getInstance().setWithdrawDuelRing(false);
						ctx.inventory.select().id(2552).poll().interact("Wear");
						ctx.bank.open();
						Condition.wait(() -> ctx.bank.opened(), 200, 4);
					} else
						closeBankAndQuit();

				}
				if (!ctx.bank.select().name("Bucket").isEmpty()) {
					if (ctx.inventory.select().name("Law rune").isEmpty()
							&& !ctx.bank.select().name("Law rune").isEmpty()) {
						if (Options.getInstance().getMode() == Mode.PVP)
							ctx.bank.withdraw(ctx.bank.select().name("Law rune").poll().id(), 2);
						else
							ctx.bank.withdraw(ctx.bank.select().name("Law rune").poll().id(), 1);
					} else
						closeBankAndQuit();
					if (ctx.inventory.select().name("Earth rune").isEmpty()
							&& !ctx.bank.select().name("Earth rune").isEmpty()) {
						ctx.bank.withdraw(ctx.bank.select().name("Earth rune").poll().id(), 1);
					} else
						closeBankAndQuit();

					ctx.bank.withdraw(ctx.bank.select().name("Bucket").poll().id(),
							org.powerbot.script.rt4.Bank.Amount.ALL);
				} else {
					closeBankAndQuit();
				}

				Tools.closeBank(ctx, Options.getInstance().getUseEscape());

			} else {
				ctx.bank.open();
			}
		} else {
			ctx.movement.step(ctx.bank.nearest());
			// ctx.camera.turnTo(ctx.bank.nearest());
		}
	}

	private void closeBankAndQuit() {
		Tools.closeBank(ctx, Options.getInstance().getUseEscape());
		Tools.logout(ctx);
		ctx.controller.stop();
	}

	@Override
	public String status() {
		return "Banking...";
	}

}
