package m0tionl3ss.SandRunner.tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Player;
import m0tionl3ss.SandRunner.util.Options;
import m0tionl3ss.SandRunner.util.Tools;

public class FillBuckets extends Task {
	Area portalAndSandpitArea = new Area(new Tile(2539, 3106), new Tile(2548, 3093));
	Player localPlayer = ctx.players.nil();
	long timer1 = 0;
	long timer2 = 0;
	int failCounter = 0;

	public FillBuckets(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		this.localPlayer = ctx.players.local();
		if (portalAndSandpitArea.contains(localPlayer) && !ctx.inventory.select().name("Bucket").isEmpty())
			return true;
		return false;
	}

	@Override
	public void execute() {
		GameObject sandpit = ctx.objects.select().name("Sandpit").poll();
		Item bucket = ctx.inventory.select().name("Bucket").poll();
		Tools.openInventoryIfClosed(ctx);
		if (sandpit.inViewport()) {

				if (localPlayer.animation() == -1 && !localPlayer.inMotion()) {
					timer1 = System.currentTimeMillis();
					long duration = timer1 - timer2;
					if (duration > 1350) {
						// check when min and max visible
						if (ctx.camera.pitch() < 23) {
							if (Options.getInstance().getUseCompass())
							{
								ctx.widgets.widget(548).component(9).component(1).click();
							}
							else
							ctx.camera.pitch(Random.nextInt(27, 49));
						}
						bucket.interact("Use");
						sandpit.interact("Use");
						// What to do when oak tree is the the viewport

						Condition.wait(() -> localPlayer.animation() == 895, 370, 3);
					}
					System.out.println(timer1 - timer2);

				} else {
					timer2 = System.currentTimeMillis();
				}

			//}
		} else {
			// check when min and max visible
			if (ctx.camera.pitch() > 49) {
				if (Options.getInstance().getUseCompass())
				{
					ctx.widgets.widget(548).component(9).component(1).click();
				}
				else
				ctx.camera.pitch(Random.nextInt(27, 49));
			}
		}

	}

	@Override
	public String status() {
		// TODO Auto-generated method stub
		return "Filling buckets...";
	}

}
