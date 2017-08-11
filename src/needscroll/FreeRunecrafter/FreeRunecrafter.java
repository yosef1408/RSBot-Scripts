package needscroll.FreeRunecrafter;

import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientContext;

import Tasks.Air;
import Tasks.Antiban;
import Tasks.Body;
import Tasks.Earth;
import Tasks.Fire;
import Tasks.Mind;
import Tasks.Water;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

@Script.Manifest(name = "Free Runecrafter", description = "Crafts all nonmember runes", properties = "author=needscroll; topic=1336214; client=6;")

public class FreeRunecrafter extends PollingScript<ClientContext> implements PaintListener{
	
	Air air = new Air(ctx);
	Water water = new Water(ctx);
	Fire fire = new Fire(ctx);
	Earth earth = new Earth(ctx);
	Body body = new Body(ctx);
	Mind mind = new Mind(ctx);
	
	List<Task> taskList = new ArrayList<Task>();
	
	@Override
	public void start()
	{
		log.info("FreeRunecrafter has started");
		
		String runes [] = {"Air", "Water", "Earth", "Fire", "Mind", "Body"};
		String rune_choice = (String) JOptionPane.showInputDialog(null, "Which runes to make?","Runes",JOptionPane.PLAIN_MESSAGE, null, runes,runes[0]);
		
		taskList.add(new Antiban(ctx));
		
		if (rune_choice == "Air")
		{
			taskList.add(new Air(ctx));
		}
		if (rune_choice == "Water")
		{
			taskList.add(new Water(ctx));
		}
		if (rune_choice == "Fire")
		{
			taskList.add(new Fire(ctx));
		}
		if (rune_choice == "Earth")
		{
			taskList.add(new Earth(ctx));
		}
		if (rune_choice == "Body")
		{
			taskList.add(new Body(ctx));
		}
		if (rune_choice == "Mind")
		{
			taskList.add(new Mind(ctx));
		}
	}
	
	@Override
	public void stop()
	{
		log.info("FreeRunecrafter has stopped");
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
        g.drawString("Free RuneCrafter", 50, 50);
        g.drawString("Runes Made = " + (air.get_runes() + water.get_runes() + fire.get_runes() + earth.get_runes() + body.get_runes() + mind.get_runes()), 50, 65);
        g.drawString("Status = " + (air.get_status() + water.get_status() + fire.get_status() + earth.get_status() + body.get_status() + mind.get_status()), 50, 80);
    }



}