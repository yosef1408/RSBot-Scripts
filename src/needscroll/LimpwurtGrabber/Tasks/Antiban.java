package needscroll.LimpwurtGrabber.Tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;

import needscroll.LimpwurtGrabber.Task;

public class Antiban extends Task{

	public static String STATUS = "";
	
	public Antiban(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		int random_chance = (int) (Math.random() * 5000);
		
		return ctx.camera.pitch() > 70 || ctx.camera.pitch() < 40 || random_chance < 50;
	}

	@Override
	public void execute() 
	{
		STATUS = "Antiban";
		ctx.camera.angle('n');
		ctx.camera.pitch(55);
		
		int random_sleep = (int) (Math.random() * 10000);
		Condition.sleep(random_sleep);
		STATUS = "";
	}
	
	public String get_status()
	{
		return STATUS;
	}
}
