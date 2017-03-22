package matulino.quickthiever.tasks;

import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Skills;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;

import java.util.concurrent.Callable;

import org.powerbot.bot.rt6.client.Skill;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;


public class Pickpocket extends Task<ClientContext>   {

	private QuickThiever main;
	private String thiefTarg = null;
	private ItemQuery<Item> food;
	private Npc target;
	private int hpBefore;
	private final Component currHealth = ctx.widgets.widget(160).component(5);

	public Pickpocket(ClientContext ctx, QuickThiever main) {
		super(ctx);
		this.main = main;
		this.thiefTarg = main.thievTarg;
	}

	@Override
	public boolean activate() {
		food = ctx.inventory.select().action("Eat");
			
		return  //!inventoryFull()
				(ctx.players.local().healthPercent() >= main.percentToEat)
				&& (ctx.players.local().speed() < 2);
	}

	@Override
	public void execute() {
		main.status = "Pickpocketing...";
		
		target = ctx.npcs.select().name(thiefTarg).nearest().poll();
		if (target != null) {
			if(target.inViewport()) {
				
					target.interact("Pickpocket");
					Condition.wait(new Callable<Boolean>() {
					     @Override
					     public Boolean call() {
					    	 
					        return main.pickSuccess;
					         
					     }
					}, 450, 2);
					main.pickSuccess = false;
					if (main.pickFailed) {
						main.status = "Stunned!";
						Condition.sleep(Random.nextInt(3000, 3500));
						main.pickFailed = false;
					}
				} else { ctx.movement.step(target.tile()); }
			} 
		
	}
	
	private boolean inventoryFull() {
		 return (ctx.inventory.select().count() > 27);
	}
	

}
