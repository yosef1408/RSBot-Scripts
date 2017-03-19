package matulino.quickthiever.tasks;


import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;

public class OpenBank extends Task<ClientContext> {
	QuickThiever main;
	private int hpToEat;
	private ItemQuery<Item> food;
	private final Component currHealth = ctx.widgets.widget(160).component(5);
	
	public OpenBank(ClientContext ctx, QuickThiever main) {
		super(ctx);
		this.main = main;
	}

	@Override
	public boolean activate() {
		food = food = ctx.inventory.select().action("Eat");
			
		return !ctx.bank.opened()
				&& ctx.bank.present()
				&& (food.size() < 1);
		
	}

	@Override
	public void execute() {

		main.status = "Openning bank...";
	
		if (ctx.bank.inViewport()) {
			ctx.bank.open();
			Condition.wait(new Callable<Boolean>() {
			     @Override
			     public Boolean call() {
			         return ctx.bank.opened();
			     }
			}, 1000, 2);
		} else {
			ctx.movement.step(ctx.bank.nearest().tile());
			ctx.camera.turnTo(ctx.bank.nearest());			
		}

	
	}
	

}
