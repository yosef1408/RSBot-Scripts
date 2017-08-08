package needscroll.BanannaGrabber;

import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Tasks.Antiban;
import Tasks.CastleWarsBank;
import Tasks.EdgevilleBank;
import Tasks.Fill;
import Tasks.Gather;
import Tasks.GeBank;


@Script.Manifest(name = "Banana Grabber", description = "Collects bananas for 500k/h", properties = "author=needscroll; topic=1335857; client=6;", )

public class BanannaGrabber extends PollingScript<ClientContext> implements PaintListener{
	
	final static int[] BANANNA_TREES = {2073, 2074, 2075, 2076, 2077};
	final static int[] BASKETS = {5376, 5408, 5410, 5412, 5414, 5416};
	final static int BANANNA = 1963;
	final static int[] WEALTH = {20659, 20657, 20655, 20653, 2572};
	final static int[] GLORY = {1712, 1710, 1708, 1706, 1704};
	final static int[] DUELING = {2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566};
	final static int GE_BANK = 87989;
	final static int CASTLE_BANK = 83634;
	final static int EDGE_BANK = 42378;
	
	private Fill filler = new Fill(ctx);
	private Gather gatherer = new Gather(ctx);
	
	List<Task> taskList = new ArrayList<Task>();
	
	@Override
	public void start()
	{
		log.info("bananna grabber has started");
		
		String banks [] = {"Castle Wars", "Grand Exchange", "Edgeville"};
		String bank_choice = (String) JOptionPane.showInputDialog(null, "Where do you wish to bank?","Bank",JOptionPane.PLAIN_MESSAGE, null, banks,banks[0]);
		
		taskList.add(new Antiban(ctx));
		taskList.add(new Gather(ctx));
		taskList.add(new Fill(ctx));

		if (bank_choice == "Castle Wars")
		{
			taskList.add(new CastleWarsBank(ctx));
		}
		if (bank_choice == "Grand Exchange")
		{
			taskList.add(new GeBank(ctx));
		}
		if (bank_choice == "Edgeville")
		{
			taskList.add(new EdgevilleBank(ctx));
		}
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
        g.drawString("Bananna Grabber", 50, 50);
        g.drawString("Bananas Collected = " + gatherer.get_amount(), 50, 65);
        g.drawString("Baskets Collected = " + filler.get_amount(), 50, 80);
    }



}
