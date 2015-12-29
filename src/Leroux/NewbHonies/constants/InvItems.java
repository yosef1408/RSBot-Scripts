package Leroux.NewbHonies.constants;

import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;

public class InvItems extends ClientAccessor {

	public InvItems(ClientContext ctx) {
		super(ctx);
	}
	
	private final int honey = 12156;
	private final int repel = 28;
	
	private final Item repellant = ctx.backpack.select().id(repel).poll();	
	private final Item honeyComb = ctx.backpack.select().id(honey).poll();
	
	public int getHoney() {
		return honey;
	}
	
	public int getRepel() {
		return repel;
	}

	public Item getRepellant() {
		return repellant;
	}

	public Item getHoneyComb() {
		return honeyComb;
	}
	
	
}
