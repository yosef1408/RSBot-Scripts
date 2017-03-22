package matulino.quickthiever.tasks;



import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;

public class Eat extends Task<ClientContext> {

	private QuickThiever main;
	private final Component currHealth = ctx.widgets.widget(160).component(5);
	private ItemQuery<Item> food;
	
	public Eat(ClientContext ctx, QuickThiever main) {
		super(ctx);
		this.main = main;
	}

	@Override
	public boolean activate() {
		
		return  (ctx.players.local().healthPercent() < main.percentToEat);//(Integer.parseInt(currHealth.text()) < main.percentToEat);
	}

	@Override
	public void execute() {
		main.status = "Eating food...";
		
		food = ctx.inventory.select().action("Eat");
			
		if (food != null) {
			if (food.size() < 1 && !main.isBanking) ctx.controller.stop();
			food.poll().interact("Eat");
		}

	}
	
}
