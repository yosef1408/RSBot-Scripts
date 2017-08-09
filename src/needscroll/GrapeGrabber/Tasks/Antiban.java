package needscroll.GrapeGrabber.Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;

import needscroll.GrapeGrabber.Task;

public class Antiban extends Task{

	public Antiban(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		int random_chance = (int) (Math.random() * 5000);
		
		if (random_chance < 50)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void execute() {
		int random_sleep = (int) (Math.random() * 10000);
		Condition.sleep(random_sleep);
	}

}
