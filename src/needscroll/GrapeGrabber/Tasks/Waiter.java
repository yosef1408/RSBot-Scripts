package needscroll.GrapeGrabber.Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GroundItem;

import needscroll.GrapeGrabber.Task;

public class Waiter extends Task{
	
	final static int APPLES = 1955;

	public Waiter(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().count() != 28 && ctx.players.local().tile().floor() == 1 && !exist(APPLES);
	}

	@Override
	public void execute() {
		Condition.sleep(10000);
	}
	
	private boolean exist(int id)
	{
		boolean does = false;
		GroundItem thing = ctx.groundItems.select().id(id).nearest().poll();
		
		if (thing.inViewport())
		{
			does = true;
		}
		
		return does;
	}

}
