package Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import needscroll.BanannaGrabber.Task;

public class Gather extends Task{
	
	final static int[] BANANNA_TREES = {2073, 2074, 2075, 2076, 2077};
	public static int amount_collected = 0;
	
	public Gather(ClientContext ctx) 
	{
		super(ctx);
	}

	@Override
	public boolean activate()
	{
		return !ctx.players.local().inMotion() && ctx.backpack.select().count() != 28;
	}
	
	@Override
	public void execute()
	{
		GameObject tree = ctx.objects.select().id(BANANNA_TREES).nearest().poll();
		
		tree.interact("Pick");
		Condition.sleep(1000);
		while (ctx.players.local().inMotion())
		{
			Condition.sleep(1000);
		}
		amount_collected++;
	}
	
	public int get_amount()
	{
		return amount_collected;
	}

}
