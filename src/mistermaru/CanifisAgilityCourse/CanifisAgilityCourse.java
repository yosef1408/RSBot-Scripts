package mistermaru.CanifisAgilityCourse;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

import mistermaru.CanifisAgilityCourse.Task;
import mistermaru.CanifisAgilityCourse.Tasks.*;

@Script.Manifest(
		name = "CanifisAgilityCourse - v1.5", description = "Does the Canifis rooftop agility course.", 
		properties = "author = Mistermaru; topic=1308966; client=4"
)

public class CanifisAgilityCourse extends PollingScript<ClientContext> implements PaintListener{

	public ArrayList<Task> taskList = new ArrayList<Task>();
	private long initialTime = 0;
	private int startExpAgility;
	private int totalExpGained;
	private int startLevelAgility;
	private int totalLevelsGained;
	private BufferedImage AgilityImg;
	  
	public CanifisAgilityCourse(){
	}
	
	@Override
	public void start(){
		taskList.addAll(Arrays.asList(new TakeMarkOfGrace(ctx),
				new ClimbTallTree(ctx), 
				new JumpGap(ctx),
				new JumpPole(ctx),							
				new FallingFromCourse(ctx)
				));
		
		if(ctx.game.resizable()){
			ctx.game.tab(Game.Tab.OPTIONS);
			ctx.widgets.component(261, 19).click();
			ctx.game.tab(Game.Tab.INVENTORY);
		}		
		
		ctx.camera.pitch(Random.nextInt(55, 99));
		
		initialTime = System.currentTimeMillis();
		startExpAgility = ctx.skills.experience(16);
		startLevelAgility = ctx.skills.level(16);
		
		AgilityImg = ctx.controller.script().downloadImage("http://i.imgur.com/IxwUmwH.png");
	}

	@Override
	public void poll() {
		for(Task task: taskList){
			if(task.activate()){
				task.execute();
			}
		}
	}
	
	@Override
	public void stop(){
		
	}
	
	@Override
	public void repaint(Graphics gr) {
		
		totalExpGained = (ctx.skills.experience(16) - startExpAgility);
		totalLevelsGained = (ctx.skills.level(16) - startLevelAgility);
		int expPH = (int) (totalExpGained / getRunTime());
		
		Graphics2D g = (Graphics2D) gr;
		g.setColor(new Color(87, 87, 87, 110));
		g.fillRect(0, 220, 315, 120);
		g.setColor(new Color(63, 63, 63, 156));
		g.setStroke(new BasicStroke(5));
		g.drawRect(0, 220, 315, 120);
		g.drawLine(0, 240, 315, 240);
		g.setColor(new Color(41, 41, 41, 210));
		g.setStroke(new BasicStroke(1));
		g.drawRect(0, 220, 315, 120);
		g.drawLine(0, 240, 315, 240);
		g.setColor(new Color(43, 178, 142, 210));
		g.setFont(new Font("Monospaced", 1, 16));
        g.drawString("EXP Gained: " + totalExpGained, 5, 254);
        g.drawString("Levels gained: " + startLevelAgility + "(+" + totalLevelsGained + ")" ,5, 266);
        g.drawString("Exp/h: " + expPH, 5, 278);
        g.drawString("Laps done: " + JumpGap.getLaps(), 5, 290);
        g.drawString("Mark Of Grace taken: " + TakeMarkOfGrace.getMOGTaken(), 5, 302);
        g.drawString("Times fallen: " + FallingFromCourse.getTimesFallen(), 5, 314);
		g.drawString("Runtime: " + getHour() + ":" + getMin() + ":" + getSec(), 5, 336);
		g.drawString("" + runtime(getRuntime()), 5, 45);

		g.setFont(new Font("Verdana", 1, 14));
		g.setColor(new Color(255,0,0, 165));
		g.drawString("CanifisAgilityCourse v1.5, by Maru", 6, 236);
		g.drawImage(AgilityImg, 265 , 286, null);
	}	
	
	public long getSec(){
		long delta = (System.currentTimeMillis() - initialTime) / 1000,
				s = delta % 60;
			return s;
	}
	
	public long getMin(){
		long delta = (System.currentTimeMillis() - initialTime) / 1000,
				m = (delta / 60) % 60;
			return m;
	}
	
	public long getHour(){
		long delta = (System.currentTimeMillis() - initialTime) / 1000,
				h = (delta / (60 * 60)) % 24;
			return h;
	}
	
	public double getRunTime(){
		double runTime = (double) (System.currentTimeMillis() - initialTime) / 3600000;
		return runTime;
	}
	

public String runtime(final long time) {

        final long total_secs = time / 1000;

        final long total_mins = total_secs / 60;

        final long total_hrs = total_mins / 60;

        final long total_days = total_hrs / 24;

        final int secs = (int) total_secs % 60;

        final int mins = (int) total_mins % 60;

        final int hrs = (int) total_hrs % 24;

        final int days = (int) total_days;

        return String.format("%02d:%02d:%02d:%02d", days, hrs, mins, secs);

    }

}
