package ryukis215;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Game.Tab;

/**
 * Utility methods for reusability 
 * 
 * @author Ryukis215
 *
 */
public class Utilities extends Controller{
	
	/**
	 * interacts with the npc using the action provided
	 * 
	 * @param id int id of the npc
	 * @param action String the action to carry out, e.g. "Talk-to"
	 */
	public void interactWithNpc(int id, String action){
		final Npc npc = ctx.npcs.select().id(id).nearest().poll();
		if(!npc.inViewport()){
			ctx.camera.turnTo(npc);
			ctx.movement.step(npc.tile());
		}		
		npc.interact(action);
		Condition.sleep(Random.nextInt(500, 700));
	}
	
	/**
	 * interacts with the object using the action provided
	 * 
	 * @param id int the id of the npc
	 * @param action String the action to carry out, e.g. "Open"
	 */
	public void interactWithObject(int id, String action){
		
		final GameObject object = ctx.objects.select().id(id).nearest().poll();
		if(!object.inViewport()){
			ctx.camera.turnTo(object);
			ctx.movement.step(object.tile());
		}		
		object.interact(action);
		Condition.sleep(Random.nextInt(500, 700));
	}
	
	/** 
	 * uses an item on another item in the inventory
	 * 
	 * @param item1_name name of the first item
	 * @param item2_name name of the second item
	 */
	public void fuseItems(String item1_name, String item2_name){
		Item item1 = ctx.inventory.select().name(item1_name).poll();
		Item item2 = ctx.inventory.select().name(item2_name).poll();
		
		item1.click();
        Condition.sleep(Random.nextInt(130, 333));
        item2.click();
	}
	
	/**
	 *  the nearest npc with the id provided is traversed to using findPath
	 * 
	 * @param id id of the npc
	 */
	public void travelToNpc(int id){
		final Npc npc = ctx.npcs.select().id(id).nearest().poll();	
		if(!npc.inViewport()){
			ctx.movement.findPath(npc).traverse();
		}
	}
	
	/**
	 * travels to tile using x,y provided
	 * 
	 * @param x 
	 * @param y
	 */
	public void travelToTile(int x, int y){
		Tile t = new Tile(x,y,0);
		ctx.movement.findPath(t).traverse();
	}
	
	/**
	 * checks to see if the npc with the id provided is inViewPort()
	 * 
	 * @param id
	 * @return
	 */
	public boolean canISeeNpc(int id){
		if(!ctx.npcs.select().id(id).nearest().poll().inViewport())
			return false;
		return true;	
	}
	
	/**
	 * Scans the inventory for an item with same as provided
	 * 
	 * @param itemID id of the item to search for
	 * @return boolean true of found
	 */
	public boolean doIHaveItem(int itemID) {
		for(Item item : ctx.inventory.items()){
			if(item.id() == itemID)
				return true;
		}
		return false;
	}
	
	/**
	 * chops a tree with waiting
	 */
	public void chopTree(){
		interactWithObject(9730, "Chop down");
		
		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return player.animation() == -1;
			}
		}, 250, 10);	
	}
	
	/**
	 * makes a fire with tinderbox and log in inventory
	 */
	public void makeFire(){
		if(!ctx.objects.select().at(player.tile()).isEmpty())
			stepToRandomEmptyTile(5);
			openInventory();
			fuseItems("Tinderbox", "Logs");
		
	}
	
	/**
	 * scans the radius around the player to find a tile which doesnt have a fire on it
	 * if empty tile found, steps to it
	 * 
	 * @param radius the radius to search and move to
	 */
	public void stepToRandomEmptyTile(int radius){
		if(!isTileEmpty(player.tile())){
			int x = player.tile().x();
			int y = player.tile().y();
			Tile t1 = new Tile(x, y);			
			Tile t2 = new Tile(x, y);
			Tile t3 = new Tile(x, y);
			Tile t4 = new Tile(x, y);
			Tile t5 = new Tile(x, y);
			Tile t6 = new Tile(x, y);
			Tile t7 = new Tile(x, y);
			Tile t8 = new Tile(x, y);
			
			
			for(int i = 1; i< radius; i++){
				 t1 = new Tile(x+i, y+i, 0);
				 t2 = new Tile(x-i, y-i, 0);				 
				 t3 = new Tile(x+i, y, 0);
				 t4 = new Tile(x, y+i, 0);			 
				 t5 = new Tile(x-i, y+i, 0);
				 t6 = new Tile(x+i, y-i, 0); 
				 t7 = new Tile(x-i, y, 0);
				 t8 = new Tile(x, y-i, 0);
				 
				 Tile[] tileL = { t1, t2, t3, t4, t5, t6, t7, t8};
				 
				 for(Tile t : tileL){
					 if(isTileEmpty(t)){
						 t.matrix(ctx).click();
						 return;
					 }
				 }
			}			
		}
	}
	
	/**
	 * checks to see if the tile has a fire object on it or not
	 * 
	 * @param tile the tile to check in
	 * @return boolean true if is empty
	 */
	public boolean isTileEmpty(Tile tile){
		if(ctx.objects.select().id(26185).at(tile).isEmpty())
			return true;
		return false;
	}
	
	/**
	 * uses an item on an object 
	 * 
	 * @param itemId item id
	 * @param objectId object id
	 */
	public void useItemOnObject(int itemId, int objectId){
		openInventory();
		final GameObject object = ctx.objects.select().id(objectId).nearest().poll();
		final Item item = ctx.inventory.select().id(itemId).poll();
		
		if(!object.inViewport()){
			ctx.movement.step(object.tile());
			ctx.camera.turnTo(object);
		}
		
		item.click();
		Condition.sleep(Random.nextInt(130, 333));
		object.interact("Use");
	}
	
	/**
	 * opens inventory if not open
	 */
	public void openInventory(){
		ctx.game.tab(Tab.INVENTORY);
		/*
		if(!ctx.inventory.component().visible()){
			ctx.widgets.widget(548).component(50).click();
			
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return ctx.inventory.component().visible();
				}
			}, 250, 10);
		}
		*/
	}
	
	/**
	 * clicks an item in the inventory
	 * 
	 * @param id the id of the item
	 */
	public void clickItemInInv(int id){
		final Item i = ctx.inventory.select().id(id).poll();
		i.click();
	}
	
	/**
	 * returns an attackable npc with the id provided
	 * 
	 * @param id the id of the npc
	 * @return Npc attackable npc
	 */
	public Npc attackableNpc(int id){
		final Npc attackable = ctx.npcs.select().id(id).select(new Filter<Npc>()
		        {
		            @Override
		            public boolean accept(Npc npc)
		            {
		                return  !npc.inCombat() && npc.interacting().equals(player) || (!npc.interacting().valid() && npc.healthPercent() > 0);
		            }
		}).nearest().poll();
		return attackable;
	}
	
	/**
	 * checks to see if the player is in the fightArea
	 * fight area is rat pit 
	 * 
	 * @return boolean true if in fight area
	 */
	public boolean inFightArea(){
		Tile tile = player.tile();
		for(int i = 0; i<fightArea.length; i++){
			if(tile.x() == fightArea[i].x() && tile.y() == fightArea[i].y()){
				return true;
			}
		}
		return false;
	}
	
	public static final Tile[] fightArea = { new Tile(3105, 9511, 0),
		new Tile(3110, 9518, 0), new Tile(3109, 9517, 0),
		new Tile(3108, 9516, 0), new Tile(3107, 9515, 0),
		new Tile(3106, 9514, 0), new Tile(3105, 9513, 0),
		new Tile(3104, 9512, 0), new Tile(3110, 9519, 0),
		new Tile(3109, 9516, 0), new Tile(3108, 9517, 0),
		new Tile(3107, 9514, 0), new Tile(3105, 9512, 0),
		new Tile(3106, 9515, 0), new Tile(3104, 9513, 0),
		new Tile(3109, 9519, 0), new Tile(3108, 9518, 0),
		new Tile(3106, 9512, 0), new Tile(3105, 9515, 0),
		new Tile(3104, 9514, 0), new Tile(3109, 9518, 0),
		new Tile(3108, 9519, 0), new Tile(3106, 9513, 0),
		new Tile(3105, 9514, 0), new Tile(3104, 9515, 0),
		new Tile(3107, 9519, 0), new Tile(3106, 9518, 0),
		new Tile(3105, 9517, 0), new Tile(3104, 9516, 0),
		new Tile(3107, 9518, 0), new Tile(3105, 9516, 0),
		new Tile(3106, 9519, 0), new Tile(3104, 9517, 0),
		new Tile(3109, 9515, 0), new Tile(3108, 9514, 0),
		new Tile(3107, 9517, 0), new Tile(3106, 9516, 0),
		new Tile(3105, 9519, 0), new Tile(3104, 9518, 0),
		new Tile(3108, 9515, 0), new Tile(3107, 9516, 0),
		new Tile(3106, 9517, 0), new Tile(3104, 9519, 0),
		new Tile(3105, 9518, 0), new Tile(3106, 9522, 0),
		new Tile(3104, 9520, 0), new Tile(3107, 9522, 0),
		new Tile(3104, 9521, 0), new Tile(3105, 9520, 0),
		new Tile(3107, 9521, 0), new Tile(3106, 9520, 0),
		new Tile(3104, 9522, 0), new Tile(3107, 9520, 0),
		new Tile(3106, 9521, 0), new Tile(3105, 9522, 0),
		new Tile(3104, 9523, 0), new Tile(3109, 9521, 0),
		new Tile(3108, 9520, 0), new Tile(3104, 9524, 0),
		new Tile(3109, 9520, 0), new Tile(3108, 9521, 0),
		new Tile(3108, 9522, 0), new Tile(3102, 9524, 0),
		new Tile(3102, 9525, 0), new Tile(3103, 9524, 0),
		new Tile(3102, 9522, 0), new Tile(3101, 9520, 0),
		new Tile(3102, 9523, 0), new Tile(3102, 9520, 0),
		new Tile(3102, 9521, 0), new Tile(3101, 9522, 0),
		new Tile(3103, 9519, 0), new Tile(3102, 9518, 0),
		new Tile(3103, 9518, 0), new Tile(3102, 9516, 0),
		new Tile(3101, 9519, 0), new Tile(3102, 9517, 0),
		new Tile(3101, 9518, 0), new Tile(3102, 9514, 0),
		new Tile(3102, 9515, 0), new Tile(3102, 9512, 0),
		new Tile(3103, 9512, 0), new Tile(3102, 9513, 0) };

}
