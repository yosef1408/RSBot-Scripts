package needscroll.BanannaGrabber.Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Item;
import needscroll.BanannaGrabber.Task;

public class Fill extends Task{
	
	final static int[] BASKETS = {5376, 5408, 5410, 5412, 5414};
	public static int baskets_collected = 0;

	public Fill(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().id(BASKETS).count() != 0;
	}

	@Override
	public void execute() {
		
		Item basket = ctx.backpack.select().id(BASKETS).poll();
		basket.interact("Fill");
		Condition.sleep(1500);
		baskets_collected++;
	}
	
	public int get_amount()
	{
		return baskets_collected;
	}

}
