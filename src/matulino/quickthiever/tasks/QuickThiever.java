package matulino.quickthiever.tasks;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

@Script.Manifest(name="QuickThiever", description = "AIO Thieving script.", properties = "authors=Matulino; topic=1329226; client=4;")
public class QuickThiever  extends PollingScript<ClientContext> implements PaintListener, MessageListener  {

	
	public ArrayList<Task> taskList = new ArrayList<Task>();
	
	public String thievTarg;
	public int percentToEat;
	public Tile safeSpot;
	public List<String> keptItems;
	public boolean pickFailed = false;
	public final String failText = "You fail to pick";
	public boolean pickSuccess = false;
	public final String successText = "You steal";
	public String status;	
	public boolean isBanking = false;
	public int foodId;
	
	private int currXp, startXp;
	private GUI gui;
	public Tile[] bankPath;
	public List<Tile> tileToColor;
	public boolean isPathDone = false;
	
    public void start() {
    	status = "Waiting...";
    	keptItems = new ArrayList();
    	tileToColor = new ArrayList<Tile>();
    	startXp = ctx.skills.experience(Constants.SKILLS_THIEVING);
    	
    	
    	
			EventQueue.invokeLater((new Runnable() {
			    @Override
			    public void run() {
			    	gui = new GUI(ctx,QuickThiever.this); 
			    }
			}));
	
    	
    	
    		
    }
	@Override
	public void poll() {
		 for (Task task : taskList) {
             if (task.activate()) {
                 task.execute();
                 
             }
         }
		
	}
	
	private final Color color1 = new Color(0, 0, 0, 167);
	private final Color color2 = new Color(255, 255, 255);
	private final Color color3 = new Color(204, 0, 204);
	private final BasicStroke stroke1 = new BasicStroke(1);
	private final Font font1 = new Font("Perpetua", 1, 15);
	private final Font font2 = new Font("Consolas", 1, 14);
	
	@Override
	public void repaint(Graphics gr) {
		currXp = ctx.skills.experience(Constants.SKILLS_THIEVING);
		int gainedXp = currXp-startXp;
		Graphics2D g = (Graphics2D) gr;
		
		/*if (!isPathDone)
			try {
				g.setColor(Color.RED);
				for ( Tile t: tileToColor) {
					Point p = new Point();
					p = t.matrix(ctx).mapPoint();
					g.fillRect(p.x-1,p.y-1,4,4);
					
				}
			}
			catch (Exception e) {
				System.out.println("List is empty");
			} */
		
		g.setColor(color1);
		g.fillRect(0, 220, 220, 120);
		g.setColor(color2);
		g.setStroke(stroke1);
		g.drawRect(0, 220, 220, 120);
		g.drawLine(0, 240, 220, 240);
		g.setFont(font1);
		g.drawString("Time: " + formated(this.getRuntime()), 1, 255);
		g.drawString("Xp_gained: " + gainedXp, 1, 270);
		g.drawString("Xp/hour " + getPerHour(gainedXp,this.getRuntime()), 1, 285);
		g.drawString("Status: "+ status, 1, 300);
		g.setFont(font2);
		g.setColor(color3);
		g.drawString("QuickThiever, by Matulino", 4, 236);
	}
	
	String formated(long time) {
		final int sec = (int) (time / 1000),
				  h = sec / 3600,
				  m = sec / 60 % 60,
				  s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}
	
	public static int getPerHour(int in, long time) {
		return (int) ((in) * 3600000D / time);
	}
	
	@Override
	public void messaged(MessageEvent msg) {
		
		if (msg.text().contains(failText)) {
			pickFailed = true;
		}
		else if (msg.text().contains(successText)) {
			pickSuccess = true;
		}
		
	}

}
