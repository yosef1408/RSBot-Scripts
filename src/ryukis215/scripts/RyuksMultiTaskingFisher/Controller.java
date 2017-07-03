package scripts.RyuksMultiTaskingFisher;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.imageio.ImageIO;

import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GroundItem;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Player;

@Script.Manifest(name = "Ryuk's MultiTasking Fisher", description = "Barbarian Village multi tasking and Barbarian Fishing.", properties = "author:ryukis215; topic=1333385; client=4;")
public class Controller extends PollingScript<ClientContext> implements MessageListener, PaintListener, MouseListener {
	
	final Player player = ctx.players.local();
	Boolean currentlyFishing = false;
	Npc chicken;
	String mode;//fight,fish,travel_to_fight,travel_to_fish
	int featherCount;
	int featherCountAim = Random.nextInt(400,600);//feathers we aim to collect
	static int featherCountAimUpper = 600;//upper and lower val of rand to aim for
	static int featherCountAimLower = 400;
	GroundItem feather;
	static String fishingAction = "Use-rod";
	static String fullAction = "drop";
	static String location;
	final String version = "v1.4.1";
	
	/* ---Paint---*/
	final int startExpFishing = ctx.skills.experience(Constants.SKILLS_FISHING);
	final int startExpCooking = ctx.skills.experience(Constants.SKILLS_COOKING);
	int xpDifFishing = 0;
	int xpDifCooking = 0;
	int caughtCounter = 0;
	public static long startTime = System.currentTimeMillis();
	String timeElapsed  = "N/A";
	Font font = new Font("Verdana", Font.BOLD, 15);
	Color textColorGold = new Color(218, 165, 32, 255);
	RenderedImage paintImg;
	URL fishImgURL;
	boolean paintLoaded = false;
	boolean paintToggle = true;
	Point p;
	String status = "N/A";//last action for debugging
	/* --- */
	
	final static Antiban antiban = new Antiban();
	final static Actions action = new Actions();
	final static Checks check = new Checks();
	final static ControllerGUI gui = new ControllerGUI();

	@Override
	public void start(){
		ctx.camera.pitch(true);
		
		if((ctx.varpbits.varpbit(1055) & 131072) > 0){
			action.shiftClickOn = true; 
		}
		
		//mode = "debug";
		if(mode != "debug"){
			if(check.findChicken().valid()){
				mode = "fight";
			}else{
				mode = "fish";
			}
		}
	}
	
	public void stop(){
		System.out.println("last mode: " + mode);
		System.out.println("Time Elapsed: "+timeElapsed);
		System.out.println("Exp p/hr Fishing (gained): " +perHour(xpDifFishing) + "(" + formatNumber(xpDifFishing) + ")");
		System.out.println("Exp p/hr Cooking (gained): " +perHour(xpDifCooking) + "(" + formatNumber(xpDifCooking) + ")");	
		System.out.println("catch p/hr(caught): " + getPerHour(caughtCounter) +"("+caughtCounter + ")");
	}
	
	@Override
	public void poll() {		
		featherCount = check.itemQuantity(314);
		timeElapsed = runTime(startTime);
		xpDifFishing = ctx.skills.experience(Constants.SKILLS_FISHING) - startExpFishing;
		xpDifCooking = ctx.skills.experience(Constants.SKILLS_COOKING) - startExpCooking;

		if(mode == "fish"){
			currentlyFishing = check.checkIfFishing();//establish whether we're currently fishing or not
			if(featherCount == 0 || featherCount == -1){			
				if(location == "barbarianVillage"){
					action.dropFishes();
					mode = "travel_to_fight";
					featherCountAim = Random.nextInt(featherCountAimLower,featherCountAimUpper);
				}else if(location == "barbarianFishing"){
					mode = "derp";//unidentified mode leads to controller.stop() as default
				}			
			}
		}

		if(mode == "fight"){
			feather = check.findFeathers();
			if(featherCount >= featherCountAim){
				mode = "travel_to_fish";
				feather = null;
			}
		}
	
		if(ctx.inventory.select().id(526).count() >= 1)//bury bones
			ctx.inventory.select().id(526).poll().click(true);
			
		
		final State state = getState();
		if (state == null)
			return;
		
		
		switch (state) {
			case FISH:
					status = "FISH";
					if(!currentlyFishing){
						action.castLine();
					}
				break;
			case FULL:
					status = "FULL";
					if(fullAction == "drop")
						action.dropFishes();
					if(fullAction == "cook")
						action.cookFishes();
					if(fullAction == "cook" && ctx.inventory.select().id(331, 335).count() <= 0)
						action.dropFishes();
					
				break;
			case FIGHT:
					status = "FIGHT";
					action.attackChicken();
					if(!check.inFightArea(player.tile())){
						if(!action.isGateOpen()){
							action.openGate();
						}
					}
				break;
			case PICKUP:
					status = "PICKUP";
					if(!feather.inViewport()){
						ctx.camera.turnTo(feather);
						action.gotoTile(feather.tile());
					}
					feather.interact("Take");
					Condition.sleep(Random.nextInt(1000, 1400));
				break;
			case TRAVEL_FIGHT:
					status = "TRAVEL_FIGHT";
					action.travelToFight();
					if(!action.isGateOpen()) action.openGate();
					if(action.isGateOpen() && action.amINearOpenGate()) mode = "fight";	
				break;
			case TRAVEL_FISH:
					status = "TRAVEL_FISH";
					if(check.inFightArea(player.tile()) && !action.isGateOpen()){
						action.openGate();
					}
					if(action.isGateOpen() && check.inFightArea(player.tile()) || !check.inFightArea(player.tile())){
						action.travelToFish();
					}
					if(ctx.npcs.select().name("Fishing spot").nearest().poll().valid()){
						mode = "fish";
					}
					break;
			case RUN:
					status = "RUN";
					ctx.movement.running(true);
					Condition.sleep(Random.nextInt(500, 750));
				break;
			case ANTIBAN:
					status = "ANTIBAN";
					antiban.runAntiban();
				break;
			case STOP:
					ctx.controller.stop();
				break;
			case DEBUG:

				break;
		}
	}
	
	private enum State {
		FISH, FULL, ANTIBAN, RUN, FIGHT, PICKUP, STOP, TRAVEL_FIGHT, TRAVEL_FISH, DEBUG
	}
	
	private State getState() {
		
		if(ctx.movement.energyLevel() > Random.nextInt(13,23) && !ctx.movement.running(true)) return State.RUN;
		
		if(mode == "fight"){
			if(feather.valid() && feather != null && !player.interacting().equals(chicken)){
				return State.PICKUP;
			}else{
				return State.FIGHT;
			}
		}else if(mode == "travel_to_fight"){
			return State.TRAVEL_FIGHT;
		}else if(mode == "travel_to_fish"){
			return State.TRAVEL_FISH;
		}else if(mode == "fish"){
			if (check.inventoryLength() == 28) return State.FULL;
			if(currentlyFishing && Random.nextInt(1, 1000) > 980){
				return State.ANTIBAN;
			}else{
				return State.FISH;
			}
		}else if(mode == "debug"){
			return State.DEBUG;
		}else{
			
			return State.STOP;
		}	
	}

	
	/* -------------------Paint/statistics--------------------- */
	@Override
	public void messaged(MessageEvent msg) {
		if(msg.toString().contains("You catch")){
			caughtCounter++;
		}
	}
	
	/**
	 * Author: Kenneh 
	 */
	private String runTime(long i) {
		DecimalFormat nf = new DecimalFormat("00");
		long millis = System.currentTimeMillis() - i;
		long hours = millis / (1000 * 60 * 60);
		millis -= hours * (1000 * 60 * 60);
		long minutes = millis / (1000 * 60);
		millis -= minutes * (1000 * 60);
		long seconds = millis / 1000;
		return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
	}
	
	/**
	 * Author: Kenneh 
	 */
	private String perHour(int gained) {
		return formatNumber((int) ((gained) * 3600000D / (System
				.currentTimeMillis() - startTime)));
	}
	
	/**
	 * Author: Kenneh 
	 */
	private String formatNumber(int start) {
		DecimalFormat nf = new DecimalFormat("0.0");
		double i = start;
		if (i >= 1000000) {
			return nf.format((i / 1000000)) + "m";
		}
		if (i >= 1000) {
			return nf.format((i / 1000)) + "k";
		}
		return "" + start;
	}
	
	private String getPerHour(long arg) {
		String num = NumberFormat.getIntegerInstance().format(
				arg * 3600000D / (startTime - System.currentTimeMillis()));
		num = num.substring(1);
		return num;
	}
	
	private void loadPaint(){
	    try {
	    	URL fishImgURL = new URL("http://i.imgur.com/raz1gLW.png");
	    	paintImg = ImageIO.read(fishImgURL);
			paintLoaded = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void repaint(Graphics gfx) {

		Graphics2D g2d = (Graphics2D) gfx;
		if(!paintLoaded) loadPaint();
		if(paintToggle){
			gfx.drawString(status, 250, 15);
			
		    if(paintImg != null)gfx.drawImage((Image) paintImg, 2, 340, null);
		   			    
			gfx.setFont(font);
			gfx.setColor(textColorGold);
			gfx.getFont();

			gfx.setFont(new Font("TimesRoman", Font.PLAIN, 27)); 
			gfx.drawString(timeElapsed, 235, 400);
			
			gfx.setFont(new Font("TimesRoman", Font.PLAIN, 18)); 
			gfx.drawString("Cooking EXP/hr ", 109, 423);
			gfx.drawString(perHour(xpDifCooking) + "(" + formatNumber(xpDifCooking) + ")", 109, 442);
			
			gfx.drawString("Fishing EXP/hr ", 244, 423);
			gfx.drawString(perHour(xpDifFishing) + "(" + formatNumber(xpDifFishing) + ")", 244, 442);
			
			gfx.drawString("Catch/hr ", 378, 423);		
			gfx.drawString(getPerHour(caughtCounter) + "(" + Integer.toString(caughtCounter) + ")", 378, 442);
			
			gfx.setFont(new Font("TimesRoman", Font.PLAIN, 12)); 
			gfx.drawString(version, 472, 468);
		}		
		gfx.setFont(new Font("TimesRoman", Font.BOLD, 16)); 
		gfx.setColor(Color.BLACK);	
		gfx.fillOval(488, 347, 25, 25);
		gfx.setColor(textColorGold);
		gfx.drawString("X", 495, 365);
	}

	
	@Override
	public void mouseClicked(MouseEvent e) {
		p = e.getPoint();
		if (p.getX() >= 488 && p.getX() <= 511 && p.getY() >= 347 && p.getY() <= 365 && !paintToggle){
			paintToggle = true;
		} else if (p.getX() >= 488 && p.getX() <= 511 && p.getY() >= 347 && p.getY() <= 365 && paintToggle){
			paintToggle = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}@Override
	public void mouseExited(MouseEvent arg0) {
	}@Override
	public void mousePressed(MouseEvent arg0) {
	}@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
}
