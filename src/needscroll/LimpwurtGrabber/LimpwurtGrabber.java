package needscroll.LimpwurtGrabber;

import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import needscroll.LimpwurtGrabber.Tasks.Antiban;
import needscroll.LimpwurtGrabber.Tasks.Banking;
import needscroll.LimpwurtGrabber.Tasks.Gather;
import needscroll.LimpwurtGrabber.Tasks.Teleport;
import needscroll.LimpwurtGrabber.Tasks.GoDungeon;
import needscroll.LimpwurtGrabber.Tasks.Ladder;
import needscroll.LimpwurtGrabber.Tasks.WalkHills;

@Script.Manifest(name = "Limpwurtroot Grabber", description = "Collects Limpwurtroots", properties = "author=needscroll; topic=1336613; client=6;")

public class LimpwurtGrabber extends PollingScript<ClientContext> implements PaintListener{
	
	List<Task> taskList = new ArrayList<Task>();
	Gather gather = new Gather(ctx);
	Teleport gobank = new Teleport(ctx);
	WalkHills walkhills = new WalkHills(ctx);
	Banking banking = new Banking(ctx);
	Antiban antiban = new Antiban(ctx);
	GoDungeon godungeon = new GoDungeon(ctx);

	@Override
	public void start()
	{
		log.info("limpwurt grabber has started");
		
		taskList.add(new Antiban(ctx));
		taskList.add(new Ladder(ctx));
		taskList.add(new WalkHills(ctx));
		taskList.add(new GoDungeon(ctx));
		taskList.add(new Gather(ctx));
		taskList.add(new Teleport(ctx));
		taskList.add(new Banking(ctx));
	}
	
	@Override
	public void stop()
	{
		log.info("bananna grabber has stopped");
		ctx.controller.stop();
	}
	
	@Override
	public void poll() {
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
        g.drawString("Limpwurt Grabber", 50, 50);
        g.drawString("Roots Grabbed = " + gather.get_roots(), 50, 65);
        g.drawString("Status = " + (gather.get_status() + gobank.get_status() + walkhills.get_status() + banking.get_status() + antiban.get_status() + godungeon.get_status()), 50, 80);
	}

}
