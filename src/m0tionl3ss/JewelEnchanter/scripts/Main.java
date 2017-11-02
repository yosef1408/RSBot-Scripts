package m0tionl3ss.JewelEnchanter.scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import org.powerbot.script.Condition;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import m0tionl3ss.JewelEnchanter.gui.GUI;
import m0tionl3ss.JewelEnchanter.tasks.Bank;
import m0tionl3ss.JewelEnchanter.tasks.EnchantJewelery;
import m0tionl3ss.JewelEnchanter.tasks.Task;
import m0tionl3ss.JewelEnchanter.util.Antiban;
import m0tionl3ss.JewelEnchanter.util.Experience;
import m0tionl3ss.JewelEnchanter.util.Info;
import m0tionl3ss.JewelEnchanter.util.Tools;
@Script.Manifest(description = "Enchants jewels!", name = "M0tionl3ss Jewel Enchanter" , properties = "author=m0tionl3ss;topic=1339601;client=4;")
public class Main extends PollingScript<ClientContext> implements PaintListener
{
	private GUI gui = new GUI(ctx);
	private List<Task> tasks = new ArrayList<>();
	private int jeweleryEnchanted = 0;
	@Override 
	public void start()
	{
		Info.getInstance().setItemToWithdrawId(gui.getId());
		Info.getInstance().setSpellToUse(gui.getSpell());
		Info.getInstance().setCloseBankUsingeEscape(gui.closeBankUsingEscape());
		Experience.getInstance().init(ctx, Constants.SKILLS_MAGIC);
		tasks.add(new EnchantJewelery(ctx));
		tasks.add(new Bank(ctx));
	}
	@Override
	public void poll() 
	{
		Antiban.getInstance().setTimeRunning(getTotalRuntime());
		Experience.getInstance().setTimeRunning(getTotalRuntime());
		for (Task t : tasks)
		{
			if (t.activate())
				t.execute();
		}
	}
	@Override
	public void repaint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		int x = 8;
		int y = 347;
		int w = 505;
		int h = 126;
		g2.setColor(Color.BLACK);
		g2.fillRect(x, y, w, h);
		g2.setColor(Color.WHITE);
		g2.drawRect(x, y, w, h);
		g2.setColor(Color.green);
		g2.setFont(new Font("Bauhaus 93", Font.BOLD, 24));
		g2.drawString("M0tionl3ss Jewel Enchanter V1.0", x + 50, 375);
		g2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		if (ctx.controller.isSuspended())
		{
			Condition.wait(() -> ctx.controller.isSuspended(),1000,5);
		}
		else
		{
			int beginExp = Experience.getInstance().getStartExp();
			int currentExp = Experience.getInstance().getCurrentExp();
			if (beginExp != currentExp)
			{
				jeweleryEnchanted++;
				Experience.getInstance().update(currentExp);
			}
			g2.drawString("Current level : " + Experience.getInstance().getCurrentLevel(), 28, 400);
			g2.drawString("Levels gained : " + Experience.getInstance().levelsGained() , 28, 420);
			g2.drawString("XP gained : " + Experience.getInstance().expGained(), 28, 440);
			g2.drawString("XP needed for level " + Experience.getInstance().getNextLevel() +  " : " + Experience.getInstance().expTillLevelUp(), 28, 460);
			g2.drawString("Jewelery enchanted : " + jeweleryEnchanted, 328, 400);
			g2.drawString("XP/H :" + Experience.getInstance().expPerHour(), 328, 420);
			g2.drawString("Time running : " + Tools.getTimeRunning(getTotalRuntime()), 328, 460);
		}
	
				
		
		
	}

}
