package mistermaru.CanifisAgilityCourse.Tasks;

import mistermaru.CanifisAgilityCourse.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;

public class TakeMarkOfGrace extends Task<ClientContext> {
	private int markOfGraceID = 11849;
	private static int MOGTaken = 0;
	
	public TakeMarkOfGrace(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return !ctx.groundItems.select().id(markOfGraceID).nearest().isEmpty()
				&& ctx.movement.reachable(ctx.groundItems.select().id(markOfGraceID).peek(), ctx.players.local())
				&& ctx.groundItems.select().id(markOfGraceID).nearest().peek().inViewport();
	}

	@Override
	public void execute() {
		ctx.groundItems.select().id(markOfGraceID).peek().interact("Take");
		Condition.sleep(2000);
		if(!ctx.groundItems.select().id(markOfGraceID).peek().interact("Take")){
			ctx.groundItems.select().id(markOfGraceID).peek().click();
		}
		MOGTaken++;
	}

	public static int getMOGTaken() {
		return MOGTaken;
	}

	public static void setMOGTaken(int mOGTaken) {
		MOGTaken = mOGTaken;
	}
	
	

}
