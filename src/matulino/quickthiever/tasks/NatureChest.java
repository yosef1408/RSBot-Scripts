package matulino.quickthiever.tasks;

import org.powerbot.script.rt4.Objects;
import java.util.Iterator;
import java.util.concurrent.Callable;


import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;
import org.powerbot.script.rt4.Npc;


public class NatureChest extends Task<ClientContext> {

	//animations - 537,536
	private QuickThiever main;
	private final Component currHealth = ctx.widgets.widget(160).component(5);
	private int fullChest = 11736;
	private int emtpyChest = 11740;
	private GameObject chest;
	
	public NatureChest(ClientContext ctx, QuickThiever main) {
		super(ctx);
		this.main = main;
	}

	@Override
	public boolean activate() {
		chest = ctx.objects.select().id(fullChest).nearest().poll();
		return  chest.valid();
	}

	@Override
	public void execute() {
		main.status = "Opening chest..";
		
		
		if ( chest != null) {
			chest.interact("Search for traps");
			Condition.sleep(Random.nextInt(3500, 5500));
			/*Condition.wait(new Callable<Boolean>(){

				@Override
				public Boolean call() throws Exception {
					
					return ctx.objects.select().id(11743).nearest().poll().valid();
				}
				
			},2000,2);*/
		}
		

	}
	
}
