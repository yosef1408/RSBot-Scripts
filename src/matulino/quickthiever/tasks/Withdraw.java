package matulino.quickthiever.tasks;


import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.Bank;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;


public class Withdraw extends Task<ClientContext> {
	private QuickThiever main;
	
	public Withdraw(ClientContext ctx, QuickThiever main) {
		super(ctx);
		this.main = main;
	}

	@Override
	public boolean activate() {
		ItemQuery<Item> food = ctx.inventory.select().action("Eat");
			
		return ctx.bank.opened() 				
				&& (food.size() < 1);
				
	}

	@Override
	public void execute() {
		main.status = "Withdraving food...";
		if (!ctx.bank.select().id(main.foodId).isEmpty()) {
						
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() {
					return ctx.bank.withdraw(main.foodId, Bank.Amount.ALL);
				}
			}, 400, 2);
			
			ctx.bank.close();
		} else {
			System.out.println("No food in bank");
			ctx.controller.stop();
		}
		
		
		
	}
	

}