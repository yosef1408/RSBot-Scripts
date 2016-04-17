package mistermaru.CanifisAgilityCourse.Tasks;

import mistermaru.CanifisAgilityCourse.Task;

import java.util.concurrent.Callable;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;

public class JumpPole extends Task<ClientContext> {
	private int poleID = 10831;
	private int[] poleBounds = { -80, 20, -28, 0, -4, 4 };
	private int markOfGraceID = 11849;
	private Area roofArea = new Area(new Tile(3478, 3487, 2), new Tile(3481, 3481, 2));

	public JumpPole(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return !ctx.objects.select().id(poleID).each(Interactive.doSetBounds(poleBounds)).isEmpty()
				&& !ctx.movement.reachable(ctx.groundItems.select().id(markOfGraceID).peek(), ctx.players.local())
				&& roofArea.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		GameObject pole = ctx.objects.nearest().poll();
		if (pole.inViewport() && ctx.players.local().speed() == 0 && ctx.players.local().animation() == -1) {
			ctx.camera.turnTo(pole);
			if (!pole.interact(true, "Vault")) {
				pole.interact(true, "Vault");
			}
			Condition.wait(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return !roofArea.contains(ctx.players.local());
				}

			});
		}
	}

}
