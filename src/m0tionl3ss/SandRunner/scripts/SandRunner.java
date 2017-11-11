package m0tionl3ss.SandRunner.scripts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.List;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.TileMatrix;

import m0tionl3ss.SandRunner.gui.GUI;
import m0tionl3ss.SandRunner.tasks.Bank;
import m0tionl3ss.SandRunner.tasks.Dead;
import m0tionl3ss.SandRunner.tasks.FillBuckets;
import m0tionl3ss.SandRunner.tasks.Task;
import m0tionl3ss.SandRunner.tasks.TeleportToBank;
import m0tionl3ss.SandRunner.tasks.TeleportToHouse;
import m0tionl3ss.SandRunner.util.Info;
import m0tionl3ss.SandRunner.util.Options;
import m0tionl3ss.SandRunner.util.Tools;

@Script.Manifest(description = "Fills buckets!", name = "SandRunner", properties = "author=m0tionl3ss;topic=1339772;client=4;")
public class SandRunner extends PollingScript<ClientContext> implements MessageListener, PaintListener {
	private List<Task> tasks = new ArrayList<>();
	private String status = "";
	private GUI gui = new GUI(ctx);
	Area bankArea = new Area(new Tile(2761,3475), new Tile(2753,3483));
	@Override
	public void start() {
		Options.getInstance().setUseEscape(gui.useEscape());
		Options.getInstance().setMode(gui.getMode());
		Options.getInstance().setUseCompass(gui.useCompass());
		tasks.add(new TeleportToHouse(ctx));
		tasks.add(new FillBuckets(ctx));
		tasks.add(new TeleportToBank(ctx));
		tasks.add(new Bank(ctx));
		tasks.add(new Dead(ctx));
	}

	@Override
	public void poll() {
		for (Task t : tasks) {
			if (t.activate()) {
				t.execute();
				this.status = t.status();
			}
		}

	}
	public void drawArea(Area area, Graphics g)
	{
		int bufferX[] =  area.getPolygon().xpoints;
		int bufferY[] = area.getPolygon().ypoints;

		int lenght = 0;
		Tile tileArray[] = new Tile[bufferX.length];
		List<TileMatrix> tileMatrices = new ArrayList<>();
		for (int i = 0 ; i < tileArray.length ; i++)
		{
			tileArray[i] = new Tile(bufferX[i], bufferY[i]);
		}
		for (int i = 0; i < tileArray.length; i++)
		{
			tileMatrices.add(new TileMatrix(ctx, tileArray[i]));
		}
		lenght = tileMatrices.get(0).bounds().xpoints.length;
		int screenX[] =  new int[lenght];
		int screenY[] = new int[lenght];

		for (int i = 0; i < tileMatrices.size(); i++)
		{
			screenX[i] = tileMatrices.get(i).triangles()[0].xpoints[0];
			screenY[i] = tileMatrices.get(i).triangles()[0].ypoints[0];

		}
		Polygon pol = new Polygon(screenX, screenY, lenght);
		for (int i = 0; i < lenght ; i++)
		{
			//System.out.println("X :" + screenX[i] + "[" + i + "]");
			//System.out.println("Y :" + screenY[i]+ "[" + i + "]");
		}
		g.drawPolygon(pol);

	}
	@Override
	public void messaged(MessageEvent message) {

		if (message.text().contains("crumbles"))
			Info.getInstance().setWithdrawDuelRing(true);
		if (message.text().contains("dead"))
			Info.getInstance().setDead(true);
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
		g2.drawString("M0tionl3ss SandRunner V1.0", 58, 375);
		g2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		g2.drawString("Deaths : " + Info.getInstance().getDeadCounter(), 28, 400);
		g2.drawString("Status : " + this.status, 28, 420);
		g2.drawString("Time running : " + Tools.getTimeRunning(getTotalRuntime()), 28, 440);

	}

}
