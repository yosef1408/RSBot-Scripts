package m0tionl3ss.JewelEnchanter.util;

import static org.powerbot.script.Random.nextInt;

import org.powerbot.script.Condition;
import org.powerbot.script.Input;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

public class Antiban 
{
	private static Antiban antiban = new Antiban();
	private long timeRunning;
	private boolean useAntiban = false;
	private Antiban() {}
	public static Antiban getInstance()
	{
		if (antiban == null)
			return new Antiban();
		else
			return antiban;
	}
	public void setUseAntiban(boolean flag)
	{
		this.useAntiban = flag;
	}
	public void setTimeRunning(long timeRunning) {
		this.timeRunning = timeRunning;
	}
	private void leaveGame(ClientContext ctx) {
		int width = ctx.game.dimensions().width;
		int height = ctx.game.dimensions().height;
		Input mouse = ctx.input;
		switch (nextInt(0, 5)) {
		case 0:
			mouse.move(nextInt(-10, width + 10), nextInt(-100, -10));
			break;
		case 1:
			mouse.move(nextInt(-10, width + 10), height + nextInt(10, 100));
			break;
		case 2:
			mouse.move(nextInt(-100, -10), nextInt(-10, height + 10));
			break;
		case 3:
			mouse.move(nextInt(10, 100) + width, nextInt(-10, height + 10));
			break;
		}
	}
	private void simulateAfk(ClientContext ctx, int minimum, int maximum, double sd)
	{
		leaveGame(ctx);
		int random = Random.nextGaussian(minimum, maximum, sd);
		Condition.sleep(random);
	}
	public void execute(ClientContext ctx)
	{
		if (useAntiban)
		{
			int random = nextInt(0, 100);
			int runtimeMinutes = Tools.getTimeRunningMinutes(timeRunning);
			System.out.println(runtimeMinutes);
			if (runtimeMinutes > 60)
			{
				if (random < 5)
				{
					simulateAfk(ctx, 0, 200000, 2);
				}
			}
		}
		
		
	}
	
}
