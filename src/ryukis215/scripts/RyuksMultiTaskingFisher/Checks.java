package ryukis215;

import org.powerbot.script.Filter;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.GroundItem;
import org.powerbot.script.rt4.Npc;

/**
 * Contains methods which will check for something and return data
 * @author Ryukis215
 */
public class Checks extends Controller {
	
	final private int[] chickenIds = {2692, 2693};
	
	/**
	 * finds chicken which are in the fight area defined
	 * and are either interacting with local player OR 
	 * are not interacting with anyone and their healthpercent
	 * is above 0
	 * @return Npc chicken which is found
	 */
	public Npc findChicken(){
		final Npc chicken = ctx.npcs.select().id(chickenIds).select(new Filter<Npc>()
		        {
		            @Override
		            public boolean accept(Npc npc)
		            {
		                return  inFightArea(npc.tile()) && npc.interacting().equals(ctx.players.local()) || (!npc.interacting().valid() && npc.healthPercent() > 0);
		            }
		}).nearest().poll();
		return chicken;
	}
	
	/**
	 * Will return any chicken in the area which are interacting
	 * with local player
	 * @return Npc chicken which is found
	 */
	public Npc chickenInteractingWithMe(){
		final Npc chicken = ctx.npcs.select().id(chickenIds).select(new Filter<Npc>()
		        {
		            @Override
		            public boolean accept(Npc npc)
		            {
		                return npc.interacting().equals(ctx.players.local());
		            }
		}).nearest().poll();
		return chicken;
	}
	
	/**
	 * finds and returns feather which is within the area in which we can fight
	 * @return GroundItem feathers within defined fight area
	 */
	public GroundItem findFeathers(){		
		final GroundItem feather = ctx.groundItems.select().id(314).select(new Filter<GroundItem>()
		        {
					@Override
					public boolean accept(GroundItem i) {
							
						return inFightArea(i.tile());
					}
		}).nearest().poll();
		return feather;
	}

	/**
	 * goes through the fishing animation array list 
	 * to see if the current animation is a fishing one.
	 * Using array so if it ever needs to be expanded for
	 * other fishing, it can be done so easily. 
	 * @return boolean true if fishing, false if not
	 */
	public boolean checkIfFishing(){
		for(int i : Actions.fishingAnimationList){
			if(ctx.players.local().animation() == i){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * returns the inventory count
	 * @return int count of inv
	 */
	public int inventoryLength(){
		int invCount =  ctx.inventory.select().count();
		return invCount;
	}
	
	/**
	 * returns the quantity of an item in inventory
	 * @param itemID id of the item to check
	 * @return int quantity of the item
	 */
	public int itemQuantity(int itemID) {
	      return ctx.inventory.select().id(itemID).poll().stackSize();
	}
	
	/**
	 * checks to see if the tile given is within
	 * our defined fight area and not outside it.
	 * @param tile tile you want to test
	 * @return boolean true if within area, false if not
	 */
	public boolean inFightArea(Tile tile){
		for(int i = 0; i<fightArea.length; i++){
			if(tile.x() == fightArea[i].x() && tile.y() == fightArea[i].y()){
				return true;
			}
		}
		return false;
	}
		
	public static final Tile[] insideGate = { new Tile(3236, 3296, 0),
			new Tile(3236, 3295, 0) };
	
	public static final Tile[] outsideGate = { new Tile(3237, 3295, 0),
			new Tile(3237, 3296, 0) };
		
	public static final Tile[] fightArea = { new Tile(3234, 3301, 0),
			new Tile(3234, 3300, 0), new Tile(3234, 3299, 0),
			new Tile(3234, 3298, 0), new Tile(3234, 3297, 0),
			new Tile(3234, 3296, 0), new Tile(3234, 3295, 0),
			new Tile(3234, 3294, 0), new Tile(3234, 3293, 0),
			new Tile(3234, 3292, 0), new Tile(3234, 3291, 0),
			new Tile(3234, 3290, 0), new Tile(3233, 3290, 0),
			new Tile(3233, 3291, 0), new Tile(3233, 3292, 0),
			new Tile(3233, 3293, 0), new Tile(3233, 3294, 0),
			new Tile(3233, 3295, 0), new Tile(3233, 3296, 0),
			new Tile(3233, 3297, 0), new Tile(3233, 3297, 0),
			new Tile(3232, 3297, 0), new Tile(3231, 3297, 0),
			new Tile(3231, 3296, 0), new Tile(3231, 3295, 0),
			new Tile(3232, 3295, 0), new Tile(3232, 3294, 0),
			new Tile(3232, 3293, 0), new Tile(3232, 3292, 0),
			new Tile(3232, 3291, 0), new Tile(3232, 3298, 0),
			new Tile(3232, 3299, 0), new Tile(3233, 3299, 0),
			new Tile(3233, 3300, 0), new Tile(3232, 3300, 0),
			new Tile(3231, 3300, 0), new Tile(3230, 3300, 0),
			new Tile(3229, 3300, 0), new Tile(3228, 3300, 0),
			new Tile(3227, 3300, 0), new Tile(3227, 3299, 0),
			new Tile(3228, 3299, 0), new Tile(3229, 3299, 0),
			new Tile(3230, 3299, 0), new Tile(3231, 3299, 0),
			new Tile(3232, 3299, 0), new Tile(3232, 3298, 0),
			new Tile(3231, 3298, 0), new Tile(3230, 3298, 0),
			new Tile(3229, 3298, 0), new Tile(3228, 3298, 0),
			new Tile(3228, 3297, 0), new Tile(3229, 3297, 0),
			new Tile(3230, 3297, 0), new Tile(3230, 3296, 0),
			new Tile(3229, 3296, 0), new Tile(3229, 3295, 0),
			new Tile(3228, 3295, 0), new Tile(3229, 3295, 0),
			new Tile(3228, 3296, 0), new Tile(3228, 3297, 0),
			new Tile(3227, 3297, 0), new Tile(3228, 3297, 0),
			new Tile(3228, 3296, 0), new Tile(3229, 3295, 0),
			new Tile(3228, 3295, 0), new Tile(3227, 3295, 0),
			new Tile(3227, 3296, 0), new Tile(3226, 3296, 0),
			new Tile(3225, 3296, 0), new Tile(3225, 3297, 0),
			new Tile(3225, 3298, 0), new Tile(3225, 3299, 0),
			new Tile(3225, 3300, 0), new Tile(3226, 3300, 0),
			new Tile(3226, 3301, 0), new Tile(3226, 3300, 0),
			new Tile(3226, 3299, 0), new Tile(3226, 3298, 0),
			new Tile(3226, 3297, 0), new Tile(3235, 3301, 0),
			new Tile(3235, 3300, 0), new Tile(3236, 3300, 0),
			new Tile(3236, 3299, 0), new Tile(3237, 3299, 0),
			new Tile(3237, 3298, 0), new Tile(3236, 3298, 0),
			new Tile(3236, 3297, 0), new Tile(3235, 3297, 0),
			new Tile(3235, 3298, 0), new Tile(3235, 3299, 0),
			new Tile(3235, 3298, 0), new Tile(3235, 3297, 0),
			new Tile(3235, 3296, 0), new Tile(3236, 3296, 0),
			new Tile(3236, 3295, 0), new Tile(3235, 3295, 0),
			new Tile(3235, 3294, 0), new Tile(3235, 3293, 0),
			new Tile(3235, 3292, 0), new Tile(3235, 3291, 0),
			new Tile(3235, 3290, 0), new Tile(3235, 3289, 0),
			new Tile(3235, 3288, 0), new Tile(3235, 3287, 0),
			new Tile(3235, 3288, 0), new Tile(3236, 3288, 0),
			new Tile(3236, 3289, 0), new Tile(3236, 3290, 0),
			new Tile(3236, 3291, 0), new Tile(3237, 3291, 0),
			new Tile(3237, 3292, 0), new Tile(3236, 3292, 0),
			new Tile(3236, 3293, 0), new Tile(3236, 3294, 0),
			new Tile(3236, 3295, 0), new Tile(3236, 3296, 0),
			new Tile(3236, 3297, 0), new Tile(3236, 3298, 0)};

	
}
