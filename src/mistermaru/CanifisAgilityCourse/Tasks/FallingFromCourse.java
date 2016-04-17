package mistermaru.CanifisAgilityCourse.Tasks;

import mistermaru.CanifisAgilityCourse.Task;
import org.powerbot.script.rt4.ClientContext;

public class FallingFromCourse extends Task<ClientContext> {
	
	private static int timesFallen = 0;

	public FallingFromCourse(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.players.local().animation() == 1332;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute() {
		timesFallen++;
		if(ctx.players.local().health() < 10){
			ctx.controller().stop();
		}
	}

	public static int getTimesFallen() {
		return timesFallen;
	}

	public static void setTimesFallen(int timesFallen) {
		FallingFromCourse.timesFallen = timesFallen;
	}

	
}
