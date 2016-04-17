package mistermaru.CanifisAgilityCourse.Tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import mistermaru.CanifisAgilityCourse.Enums.Gaps;
import mistermaru.CanifisAgilityCourse.Task;

public class JumpGap extends Task<ClientContext> {

	private int[] gapBounds = { -32, 32, -64, 0, -32, 32 };
	private int markOfGraceID = 11849;
	private Gaps[] gapsArray = Gaps.values();
	private int gapID;
	private Area roofArea;
	private static int laps = 0;

	public JumpGap(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		for (Gaps i : gapsArray) {
			if (i.getRoofArea().contains(ctx.players.local())
					&& !ctx.movement.reachable(ctx.groundItems.select().id(markOfGraceID).peek(), ctx.players.local())
					&& !ctx.objects.select().id(i.getGapID()).each(Interactive.doSetBounds(gapBounds)).isEmpty()) {
				gapID = i.getGapID();
				roofArea = i.getRoofArea();
				return true;
			}
		}

		return false;
	}

	@Override
	public void execute() {
		GameObject gap = ctx.objects.nearest().poll();
		ctx.camera.turnTo(gap);
		if (!gap.inViewport()) {
			ctx.camera.pitch(55);
			if (gapID == Gaps.GAP5.getGapID()) {
				ctx.movement.step(gap);
			}		
		}

		if (gap.inViewport() && ctx.players.local().speed() == 0 && ctx.players.local().animation() == -1) {
			gap.interact(true, "Jump");
			Condition.wait(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return !roofArea.contains(ctx.players.local());
				}

			});
		}
		
		if(gapID == Gaps.GAP6.getGapID()){
			setLaps(getLaps() + 1);
		}
	}

	public static int getLaps() {
		return laps;
	}

	public static void setLaps(int laps) {
		JumpGap.laps = laps;
	}

}