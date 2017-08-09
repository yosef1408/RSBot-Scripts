package needscroll.BanannaGrabber.Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;

import needscroll.BanannaGrabber.Task;

public class Antiban extends Task{

	public Antiban(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		int random_chance = (int) (Math.random() * 5000);
		int yaw = ctx.camera.yaw();
		int pitch = ctx.camera.pitch();
		
		if (random_chance < 50)
		{
			return true;
		}
		else if (yaw < 160 || yaw > 210)
		{
			return true;
		}
		
		else if (pitch < 30 || pitch > 60)
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
		check_cam();
		Condition.sleep(random_sleep);
	}
	
	private void check_cam()
	{
		int yaw = ctx.camera.yaw();
		int pitch = ctx.camera.pitch();
		
		if (yaw < 160 || yaw > 210)
		{
			ctx.camera.angle('s');
			Condition.sleep(1000);
		}
		
		if (pitch < 30 || pitch > 60)
		{
			ctx.camera.pitch(47);
			Condition.sleep(1000);
		}
	}

}
