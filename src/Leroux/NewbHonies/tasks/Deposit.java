package Leroux.NewbHonies.tasks;

import Leroux.NewbHonies.constants.Areas;
import Leroux.NewbHonies.constants.InvItems;
import Leroux.NewbHonies.script.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Interactive;
import org.powerbot.script.rt6.Item;

import java.util.concurrent.Callable;

public class Deposit extends Task {

	public Deposit(ClientContext ctx) {
		super(ctx);
	}
	
	private Areas areas = new Areas();
	private InvItems items = new InvItems(ctx);
	
	private final int[] bankBounds = {-256, 256, -512, 0, -256, 256};
	
	@Override
	public boolean activate() {
		
		return !ctx.objects.select().id(25808).each(Interactive.doSetBounds(bankBounds)).isEmpty()
					&& areas.getBankArea().contains(ctx.players.local())
						&& ctx.backpack.select().contains(items.getHoneyComb());
		
	}

	@Override
	public void execute() {
		GameObject bank = ctx.objects.nearest().poll();
		Item honeyComb = ctx.backpack.select().id(items.getHoneyComb().id()).poll();

		if(!bank.inViewport()) {
			ctx.movement.step(bank);
			ctx.camera.turnTo(bank);
		}

		if(!ctx.backpack.itemSelected() && !ctx.widgets.widget(1188).component(20).visible()) {

			if(honeyComb.interact("Use")) {

				bank.interact("Use");

				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.widgets.widget(1188).component(20).visible();
					}
				});

			}

		}


		if(ctx.widgets.widget(1188).component(20).visible()) {

			ctx.widgets.widget(1188).component(20).interact("Continue");

		}



	}

}
