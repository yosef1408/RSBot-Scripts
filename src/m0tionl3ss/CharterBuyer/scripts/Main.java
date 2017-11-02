package m0tionl3ss.CharterBuyer.scripts;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;

import m0tionl3ss.CharterBuyer.gui.Frame;
import m0tionl3ss.CharterBuyer.tasks.BankItems;
import m0tionl3ss.CharterBuyer.tasks.BuyFromCharterShop;
import m0tionl3ss.CharterBuyer.tasks.Quit;
import m0tionl3ss.CharterBuyer.tasks.RunToBank;
import m0tionl3ss.CharterBuyer.tasks.RunToCharterMember;
import m0tionl3ss.CharterBuyer.tasks.Task;
import m0tionl3ss.CharterBuyer.util.Info;
import m0tionl3ss.CharterBuyer.util.Options;

import org.powerbot.script.Script;

@Script.Manifest(description = "Buy items from charter crewmembers in catherby", name = "CharterBuyer" , properties = "author=m0tionl3ss;topic=1339589;client=4;")
public class Main extends PollingScript<ClientContext> implements PaintListener {
	List<Task> tasks = new ArrayList<>();
	Frame frame = new Frame(ctx);
	@Override
	public void start()
	{
		
		BuyFromCharterShop buyFromCharterShopTask = new BuyFromCharterShop(ctx);
		buyFromCharterShopTask.setItemsToBuy(frame.getIds());
		Options.getInstance().setUseEscape(frame.useEscape());
		Options.getInstance().setUseScroll(frame.useMouseWheel());
		tasks.add(new RunToCharterMember(ctx));
		tasks.add(buyFromCharterShopTask);
		tasks.add(new RunToBank(ctx));
		tasks.add(new BankItems(ctx));
		tasks.add(new Quit(ctx));
		
		
		
	}
	@Override
	public void poll() {
		for (Task task : tasks) {
			if (task.activate()) {
				task.execute();
			}
		}
	}
	@Override
	public void repaint(Graphics g) {
		
		int seconds = (int) (this.getRuntime() / 1000) % 60 ;
		int minutes = (int) ((this.getRuntime() / (1000*60)) % 60);
		int hours   = (int) ((this.getRuntime() / (1000*60*60)) % 24);
		String runTime = String.format("%02d:%02d:%02d", hours,minutes,seconds);
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(25, 25, 150, 90);
		g.setColor(Color.GREEN);
		g.drawRect(25, 25, 150, 90);
		g.setColor(Color.WHITE);
		//g.drawString("Items bought :  " + Info.getInstance().getItemsBought(), 30, 50);
		g.drawString("Time running :  " + runTime, 30, 70);
		g.drawString("Made by m0tionl3ss", 30, 90);
		
	}
	

}
