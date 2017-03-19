package matulino.quickthiever.tasks;

import org.powerbot.script.rt4.Npc;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;


import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.PaintListener;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.TilePath;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.ItemQuery;
import org.powerbot.script.rt4.Npc;


public class PathGenerator extends ClientAccessor implements Runnable {

	private QuickThiever main;
	private List<Tile> path = new ArrayList<Tile>();
	private Tile lastTile;
	
	
	public boolean pathDone = false;
	
	public PathGenerator(ClientContext ctx, QuickThiever main) {
		super(ctx);
		this.main = main;
		lastTile = getPlayerTile();
		path.add(lastTile);
	}


	@Override
	public void run() {
		generatePath();
		
	}
	
	public void generatePath() {
		main.status = "Generating path...";
	
		while(!pathDone) {
			
			if (distToLastTile() >= 4) {
				Tile t = getPlayerTile();
				main.tileToColor.add(t);
				path.add(t);
				lastTile = t;
			}	
		}
		main.isPathDone = true;
	}
	
	public Tile[] getPath(){
		return path.toArray(new Tile[path.size()]);
	}
	
	private Tile getPlayerTile() {
		return ctx.players.local().tile();
	}
	
	private double distToLastTile() {
		return getPlayerTile().distanceTo(lastTile);
	}
		

	
}
