package needscroll.GrapeGrabber;

import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;

import needscroll.GrapeGrabber.Tasks.Antiban;
import needscroll.GrapeGrabber.Tasks.Banking;
import needscroll.GrapeGrabber.Tasks.GetSecondFloor;
import needscroll.GrapeGrabber.Tasks.GetThirdFloor;
import needscroll.GrapeGrabber.Tasks.GoSecond;
import needscroll.GrapeGrabber.Tasks.Waiter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(description = "gets grapes and apples", name = "Grape Grabber")

public class GrapeGrabber extends PollingScript<ClientContext> implements PaintListener{
	
	private GetSecondFloor second = new GetSecondFloor(ctx);
	private GetThirdFloor third = new GetThirdFloor(ctx);
	
	List<Task> taskList = new ArrayList<Task>();
	
	@Override
	public void start()
	{
		log.info("bananna grabber has started");
		
		taskList.add(new GetSecondFloor(ctx));
		taskList.add(new GetThirdFloor(ctx));
		taskList.add(new Waiter(ctx));
		taskList.add(new Banking(ctx));
		taskList.add(new GoSecond(ctx));
		taskList.add(new Antiban(ctx));
	}
	
	@Override
	public void stop()
	{
		log.info("bananna grabber has stopped");
		ctx.controller.stop();
	}
	
	@Override
	public void poll() 
	{
		for(Task task : taskList)
		{
			if (task.activate())
			{
				task.execute();
				break;
			}
		}
	}

	@Override
    public void repaint(Graphics g1)
    {
        Graphics2D g = (Graphics2D)g1;
        g.setColor(Color.RED);
        g.drawString("Grape Grabber", 50, 50);
        g.drawString("Apples Collected = " + (second.get_apples() + third.get_apples()), 50, 65);
        g.drawString("Grapes Collected = " + third.get_grapes(), 50, 80);
    }



}