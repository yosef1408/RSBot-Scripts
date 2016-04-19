package mistermaru.CanifisAgilityCourse.Tasks;

import mistermaru.CanifisAgilityCourse.Task;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;

public class ClimbTallTree extends Task<ClientContext>{
	
	private int tallTreeID = 10819;
	private int[] tallTreeBounds = {152, 196, -260, 0, -36, 16};
	private int markOfGraceID = 11984;

	public ClimbTallTree(ClientContext ctx) {
		super(ctx);

	}

	@Override
	public boolean activate() {
		return !ctx.objects.select().id(tallTreeID).each(Interactive.doSetBounds(tallTreeBounds)).isEmpty() 
				&& ctx.groundItems.select().id(markOfGraceID).isEmpty();
	}

	@Override
	public void execute() {
		if (!ctx.movement.running() && ctx.movement.energyLevel() > 15) {
			ctx.movement.running(true);
		}
		
		GameObject tallTree = ctx.objects.nearest().poll();
		if(!tallTree.inViewport()){
			ctx.camera.pitch(Random.nextInt(55, 99));
			ctx.movement.step(new Tile(3506, 3487, 0));	
			ctx.camera.turnTo(tallTree);
		}
		if(tallTree.inViewport() && ctx.players.local().speed() == 0 && ctx.players.local().animation() == -1){
			
			if(!tallTree.interact(true, "Climb")){
				tallTree.interact(true, "Climb");
			}
		}
	}	
	
}
