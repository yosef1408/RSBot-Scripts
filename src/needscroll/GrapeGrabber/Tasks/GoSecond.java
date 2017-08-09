package needscroll.GrapeGrabber.Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

import needscroll.GrapeGrabber.Task;

public class GoSecond extends Task{
	
	private int[] STAIRS = {24073, 24074, 24075};

	public GoSecond(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().count() != 28 && ctx.players.local().tile().floor() == 0;
	}

	@Override
	public void execute() {
		GameObject stairs = ctx.objects.select().id(STAIRS).nearest().poll();
		stairs.interact("Climb-up");
		Condition.sleep(3000);
	}

}
