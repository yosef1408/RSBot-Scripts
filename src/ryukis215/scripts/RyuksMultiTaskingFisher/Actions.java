package ryukis215.scripts.RyuksMultiTaskingFisher;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.TileMatrix;
import org.powerbot.script.rt4.TilePath;

/**
 * Contains methods which will generally carry out actions
 * in a sophisticated manner.
 * @author Ryukis215
 *
 */
public class Actions extends Controller {
		
	final int[] fishList = new int[]{335, 331, 317, 321, 359, 377, 329, 343, 333, 11332, 11328, 11330};
	final static int[] fishingAnimationList = new int[]{621, 622, 623, 619};
	long lastChecked;
	boolean shiftClickOn = false;
	
	/**
	 * when provided a npc and and an action, will 
	 * turn camera to and if not in viewport goto the 
	 * npc and carry out an action with a random sleep at the end.
	 * @param theOne the npc to interact with
	 * @param action the action to carry out in the interaction
	 */
	public void interactWithNpc(org.powerbot.script.rt4.Npc theOne, String action){
		if(!theOne.inViewport()){
			ctx.camera.turnTo(theOne);
			
			if(player.tile().distanceTo(theOne.tile()) > 6)
				gotoTile(theOne.tile());
			else
			theOne.tile().matrix(ctx).interact("Walk Here"); 
		}
		theOne.interact(action);
		Condition.sleep(Random.nextInt(400, 600));
	}
	
	/**
	 * find a chicken and attack it
	 * prioritize chicken that are attacking us
	 * 
	 */
	public void attackChicken(){
		Npc ciwm = check.chickenInteractingWithMe();
		if(ciwm.valid() && !player.interacting().equals(ciwm)){
			ciwm.interact("Attack");
			Condition.sleep(Random.nextInt(250, 500));
		}else{
			chicken = check.findChicken();
			if(!player.interacting().equals(chicken) && !player.interacting().valid()) chicken.interact("Attack");
			Condition.sleep(Random.nextInt(250, 500));
		}
	}
	
	public void castLine(){
		if (ctx.inventory.selectedItemIndex() > 0)
			ctx.inventory.itemAt(ctx.inventory.selectedItemIndex()).click();
		
		Npc fishspot = ctx.npcs.select().name("Fishing spot").nearest().poll();
		interactWithNpc(fishspot, fishingAction);
		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return player.animation() != -1;
			}
		}, 200, 10);
	}
	
	/**
	 * Drops all fishes in inventory
	 * see Array fishList for list of fish
	 * it will drop.
	 * can be expanded.
	 */
	public void dropFishes(){
		if(shiftClickOn)
			ctx.input.send("{VK_SHIFT down}");
		
		openInventory();
		
		for(Item item : ctx.inventory.items()){
			for(int id: fishList){
				if(item.id() == id){
							
					if (shiftClickOn) {
					    item.click(true);			
					}else{
						item.interact("Drop");
					}
					
					if(ctx.inventory.selectedItemIndex() != -1)
						ctx.inventory.itemAt(ctx.inventory.selectedItemIndex()).click();
				}
			}
		}
		Condition.sleep(Random.nextInt(100, 250));
		if(shiftClickOn)ctx.input.send("{VK_SHIFT up}");
		
		if (ctx.inventory.selectedItemIndex() > 0){
			ctx.inventory.itemAt(ctx.inventory.selectedItemIndex()).click();
			dropFishes();
		}
		
	}
	
	public void cookFishes(){
		if (ctx.players.local().animation() != -1) {
			lastChecked = System.currentTimeMillis();
		}

		if (System.currentTimeMillis() - lastChecked > Random.nextInt(1000, 1700)) {
			final GameObject fire = ctx.objects.select().id(26185).nearest().poll();
			if(!fire.inViewport())
				gotoTile(fire.tile());
			
			final Item raw = ctx.inventory.select().id(335, 331).poll();	
			final Component cookingInterface = ctx.widgets.component(307, 5);
			
			openInventory();
			
			if (!cookingInterface.visible() && raw.interact("Use", raw.name())
					&& fire.interact("Use", fire.name())) {
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return cookingInterface.visible();
					}
				}, 250, 10);
			}
			if (cookingInterface.visible()) {
				cookingInterface.interact("Cook All");
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return !cookingInterface.visible()
								&& ctx.players.local().animation() != -1;
					}
				}, 250, 10);
			}
		}
	}
	
	public void openInventory(){
		if(!ctx.inventory.component().visible()){
			ctx.widgets.widget(548).component(50).click();
			
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return ctx.inventory.component().visible();
				}
			}, 250, 10);
		}
	}
	
	public void dropItem(Item item){
		if((ctx.varpbits.varpbit(1055) & 131072) > 0){
			 ctx.input.send("{VK_SHIFT down}");
			 item.click(true);
			 ctx.input.send("{VK_SHIFT up}");
		}else{
			item.interact("Drop");
		}
		
	}
	
	/**
	 * step to a tile, turn camera then sleep
	 * @param spot Tile you want to step to
	 */
	public void gotoTile(Tile spot){
		ctx.movement.step(spot);
		ctx.camera.turnTo(spot);
		Condition.sleep(Random.nextInt(300, 500));
	}
	
	/**
	 * will travel to the farm where the chickens are
	 */
	public void travelToFight(){
		TilePath path = ctx.movement.newTilePath(travelPath).reverse();
		path.traverse();
	}
	
	/**
	 * will travel to the barbarian village fishing spot
	 */
	public void travelToFish(){
		TilePath path = ctx.movement.newTilePath(travelPath);
		path.traverse();
	}
	
	
	/**
	 * will see if the gate is open in the chicken farm
	 * if not, it will open it
	 */
	public void openGate(){
		if(!isGateOpen()){
			int[] bounds = {-16, 8, -116, 0, -116, 100};			
			final GameObject closedGate = ctx.objects.select().id(1558).each(Interactive.doSetBounds(bounds)).select(new Filter<GameObject>()
			        {
						@Override
						public boolean accept(GameObject obj) {
							return obj.tile().distanceTo(Checks.insideGate[0])<2;
						}
			}).nearest().poll();
			if(closedGate.valid() && closedGate.tile().distanceTo(ctx.players.local()) < 7){
				gotoTile(closedGate.tile());
				ctx.camera.pitch(0);
				ctx.camera.angle('w');
				closedGate.interact("Open");
				ctx.camera.pitch(170);
			}

		}
	}
	
	/**
	 * checks to see if the gate at the chicken farm is open
	 * @return boolean true open, false closed
	 */
	public boolean isGateOpen(){
		final GameObject gate = ctx.objects.select().id(1559).select(new Filter<GameObject>()
		        {
					@Override
					public boolean accept(GameObject obj) {
						return obj.tile().distanceTo(Checks.insideGate[0])<2;
					}
		}).nearest().poll();
		if(gate.tile().x() != -1){
			return true;
		}
		return false;
	}
	
	public boolean amINearOpenGate(){
		final GameObject gate = ctx.objects.select().id(1559).select(new Filter<GameObject>()
		        {
					@Override
					public boolean accept(GameObject obj) {
						return obj.tile().distanceTo(Checks.insideGate[0])<2;
					}
		}).nearest().poll();
		if(gate.tile().x() != -1){
			if(gate.tile().distanceTo(ctx.players.local()) < 8)
			return true;
		}
		return false;
	}
	
public static final Tile[] travelPath = { new Tile(3238, 3297, 0),
		new Tile(3238, 3299, 0), new Tile(3238, 3301, 0),
		new Tile(3237, 3303, 0), new Tile(3236, 3305, 0),
		new Tile(3235, 3307, 0), new Tile(3233, 3307, 0),
		new Tile(3232, 3309, 0), new Tile(3230, 3310, 0),
		new Tile(3228, 3311, 0), new Tile(3226, 3312, 0),
		new Tile(3225, 3314, 0), new Tile(3223, 3316, 0),
		new Tile(3221, 3318, 0), new Tile(3220, 3320, 0),
		new Tile(3218, 3321, 0), new Tile(3218, 3323, 0),
		new Tile(3217, 3325, 0), new Tile(3217, 3327, 0),
		new Tile(3216, 3329, 0), new Tile(3214, 3331, 0),
		new Tile(3213, 3333, 0), new Tile(3211, 3335, 0),
		new Tile(3209, 3337, 0), new Tile(3207, 3339, 0),
		new Tile(3206, 3341, 0), new Tile(3206, 3343, 0),
		new Tile(3206, 3345, 0), new Tile(3206, 3347, 0),
		new Tile(3206, 3349, 0), new Tile(3206, 3351, 0),
		new Tile(3205, 3353, 0), new Tile(3203, 3355, 0),
		new Tile(3202, 3357, 0), new Tile(3200, 3359, 0),
		new Tile(3198, 3360, 0), new Tile(3196, 3362, 0),
		new Tile(3194, 3363, 0), new Tile(3192, 3364, 0),
		new Tile(3192, 3366, 0), new Tile(3192, 3368, 0),
		new Tile(3192, 3370, 0), new Tile(3190, 3372, 0),
		new Tile(3188, 3374, 0), new Tile(3186, 3376, 0),
		new Tile(3184, 3378, 0), new Tile(3182, 3380, 0),
		new Tile(3180, 3380, 0), new Tile(3178, 3380, 0),
		new Tile(3176, 3380, 0), new Tile(3174, 3381, 0),
		new Tile(3172, 3382, 0), new Tile(3170, 3382, 0),
		new Tile(3168, 3382, 0), new Tile(3166, 3382, 0),
		new Tile(3164, 3382, 0), new Tile(3162, 3382, 0),
		new Tile(3160, 3382, 0), new Tile(3159, 3384, 0),
		new Tile(3158, 3386, 0), new Tile(3156, 3388, 0),
		new Tile(3154, 3390, 0), new Tile(3152, 3391, 0),
		new Tile(3150, 3392, 0), new Tile(3148, 3392, 0),
		new Tile(3146, 3392, 0), new Tile(3144, 3392, 0),
		new Tile(3142, 3392, 0), new Tile(3140, 3392, 0),
		new Tile(3138, 3392, 0), new Tile(3136, 3392, 0),
		new Tile(3134, 3392, 0), new Tile(3132, 3392, 0),
		new Tile(3130, 3393, 0), new Tile(3128, 3395, 0),
		new Tile(3126, 3397, 0), new Tile(3124, 3398, 0),
		new Tile(3122, 3399, 0), new Tile(3122, 3401, 0),
		new Tile(3121, 3403, 0), new Tile(3119, 3405, 0),
		new Tile(3117, 3406, 0), new Tile(3115, 3408, 0),
		new Tile(3115, 3410, 0), new Tile(3114, 3412, 0),
		new Tile(3112, 3414, 0), new Tile(3110, 3415, 0),
		new Tile(3110, 3417, 0), new Tile(3110, 3419, 0),
		new Tile(3108, 3420, 0), new Tile(3106, 3420, 0),
		new Tile(3104, 3420, 0), new Tile(3102, 3420, 0),
		new Tile(3100, 3421, 0), new Tile(3099, 3423, 0),
		new Tile(3099, 3425, 0), new Tile(3101, 3426, 0) };

}
