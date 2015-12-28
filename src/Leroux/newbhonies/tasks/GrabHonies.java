package Leroux.newbhonies.tasks;

import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Interactive;

import Leroux.newbhonies.script.Task;
import Leroux.newbhonies.constants.Areas;

public class GrabHonies extends Task {

	public GrabHonies(ClientContext ctx) {
		super(ctx);
	}
	
	private Areas area = new Areas();
	
	private final int[] hiveBounds = {-264, 196, -476, 0, -212, 204};

	@Override
	public boolean activate() {
		return area.getHoneyArea().contains(ctx.players.local())
				&& !(ctx.backpack.select().count() == 28)
					&& !ctx.objects.select().id(68).each(Interactive.doSetBounds(hiveBounds)).isEmpty();
	}

	@Override
	public void execute() {
		
		GameObject hive = ctx.objects.nearest().poll();
		
		if(ctx.players.local().animation() == -1) {
			hive.interact("Take-honey", "Beehive");	
		}	
			
		
	}
}
