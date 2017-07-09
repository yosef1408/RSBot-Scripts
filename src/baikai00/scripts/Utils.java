package scripts;

import org.powerbot.script.*;
import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;

import java.util.Iterator;
import java.util.concurrent.Callable;

public class Utils extends ClientAccessor{
	private Walker walker = null;

    public Utils(ClientContext ctx) {
        super(ctx);
        walker = new Walker(ctx);
    }

    public int count(int id){
        return ctx.backpack.select().id(id).count();
    }
    
    public void walk(Tile... path){
        for (int i = 0; i < path.length; i++){
            running();

            final Tile currentTile = path[i];
            if (currentTile == null){
                break;
            }

            if (i != path.length-1){
            	// 到第一个点的距离
                int distance1 = ctx.movement.distance(path[i]);
                // 到第二个点的距离
                int distance2 = ctx.movement.distance(path[i+1]);
                // 到最后一个点的记距离
                int end = ctx.movement.distance(path[path.length-1]);
                if (distance1 > end){
                	continue;
                }
                if (distance1 > distance2){
                    continue;
                }
            }

            ctx.movement.step(currentTile);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return distance(currentTile, Random.nextInt(2,4));
                }
            }, 500,5);
        }
    }

    public void running(){
        if (ctx.movement.energyLevel() > 50 && !ctx.movement.running()){
            ctx.movement.running(true);
        }
    }

    public Tile getTile(){
        return ctx.players.local().tile();
    }

    public int getAnimation(){
        return ctx.players.local().animation();
    }
    
    public boolean distance(Tile t1, Tile t2, int offset){
        int step = ctx.movement.distance(t1, t2);
        return step <= offset;
    }
    
    public boolean distance(Tile t, int offset){
        int step = ctx.movement.distance(t, getTile());
        return step <= offset;
    }

    public GameObject findObject(int id){
        return ctx.objects.select().id(id).nearest().poll();
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean findDialog(){
        return false;
    }

    public void dropAllExcept(final int exceptId){
        Iterator<Item> items = ctx.backpack.select().select(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return item.id() != exceptId;
            }
        }).iterator();
//        int deleteCount = 0;
        while(items.hasNext()){
            final Item item = items.next();
            if (!item.interact(false,"Drop")){
                if (item.interact(false,"Destroy")){
                    if (findDialog()){
                        ctx.input.click(509,423,true);
                    }
                }
            }else{
                if (findDialog()){
                    ctx.input.click(509,423,true);
                }
            }
//            deleteCount++;
        }
    }

    public void setCamera(){
        ctx.camera.angle(0);
        ctx.camera.pitch(47);
    }

    public static final long TIME_OFFSET = 8 * 60 * 60 * 1000;

    public long getThisTime(){
        return timeStart == 0 ? 0 - TIME_OFFSET : now() - timeStart - TIME_OFFSET;
    }

    private long now(){
        return System.currentTimeMillis();
    }

    private long timeStart = 0;
    private void recordTime(){
        if (timeStart == 0){
            timeStart = now();
        }else{
            long timeEnd = now();
            if (timeEnd-timeStart > 30000){
                lastTime = timeEnd-timeStart - TIME_OFFSET;
            }
            timeStart = timeEnd;
        }
    }
    public long lastTime = 0 - TIME_OFFSET;

    /******************火片制作开始******************/
    public static final int ESS_ID = 7936;
    public static final int FIRE_RUNE_ID = 554;
    public static final Area pathArea = new Area(new Tile(3289, 3205, 0),
            new Tile(3391, 3271, 0));
    public static final Area makeArea = new Area(new Tile(2572, 4832, 0),
            new Tile(2592, 4850, 0));
    
    public static final Tile[] PATH_FIRE_BANK = new Tile[]{
            new Tile(3309,3241,0),
            new Tile(3323,3233,0),
            new Tile(3340,3233,0),
            new Tile(3344,3235,0),
            new Tile(3345,3236,0)
    };
    public static final Tile[] PATH_FIRE_MAKE = new Tile[]{
            new Tile(3338,3232,0),
            new Tile(3321,3236,0),
            new Tile(3315,3247,0),
            new Tile(3314,3253,0)
    };

    public static final Tile FIRE_MAKE_TILE = new Tile(2583,4839,0);
    public static final Tile FIRE_OUT_TILE = new Tile(2577,4846,0);
    public static final Tile FIRE_ENTER_TILE = new Tile(3314,3253,0);
    public static final Tile FIRE_BANK_TILE = new Tile(3347,3239,0);
    public static final Tile FIRE_PATH_HALF_TILE = new Tile(3325,3232,0);
    public static final Area AREA_ENTER_HALF = new Area(FIRE_ENTER_TILE, FIRE_PATH_HALF_TILE);
    public static final Area AREA_BANK_HALF = new Area(FIRE_BANK_TILE, FIRE_PATH_HALF_TILE);

    public static final int FIRE_BANK_ID = 83954;
    public static final int MAKE_FIRE_ID = 2482;
    public static final int ENTER_FIRE_ID = 2456;
    public static final int OUT_FIRE_ID = 2469;

    public String status = "init";

    public void fireBanking(){
        status = "BANKING";
        GameObject gameObject;
        if ((gameObject = findObject(OUT_FIRE_ID)).floor() == 0){
        	gameObject.interact("Enter");
        }else{
        	if (ctx.bank.nearest().tile().distanceTo(ctx.players.local())<10){
        		if(ctx.bank.opened()){
                	if(ctx.backpack.select().id(ESS_ID).count() != 0){
                		ctx.bank.close();
                		return;
                	}
                    if(ctx.bank.depositInventory()){
                    	Condition.wait(new Callable<Boolean>(){
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.backpack.select().id(FIRE_RUNE_ID).count() == 0;
                                
                            }
                        }, 250, 20);
                    }
                    if (ctx.bank.withdraw(ESS_ID, 28)){
                    	 Condition.wait(new Callable<Boolean>() {
         					@Override
         					public Boolean call() throws Exception {
         						return ctx.backpack.select().id(ESS_ID).count() > 0;
         					}
         				}, 250, 20);
                    	recordTime();
                    }
                    ctx.bank.close();
                } else {
                    if(ctx.bank.inViewport()) {
                        if(ctx.bank.open()){
                            Condition.wait(new Callable<Boolean>(){
                                @Override
                                public Boolean call() throws Exception {
                                    return ctx.bank.opened();
                                }
                            }, 250, 20);
                        }
                    }
                }
        	}else {
//        		walk(PATH_FIRE_BANK);
        		walker.walkPath(PATH_FIRE_BANK);
        	}
        }
    }

    public void fireMaking(){
    	status = "MAKING";
    	if (findObject(MAKE_FIRE_ID).floor() == 0){
    		if (distance(FIRE_MAKE_TILE, 3)){
    			findObject(MAKE_FIRE_ID).interact("Craft-rune");
                sleep(500);
    		}else{
//    			walk(FIRE_MAKE_TILE);
    			walker.walkPath(FIRE_MAKE_TILE);
    		}
    	}else{
    		if (distance(FIRE_ENTER_TILE, 7)){
    			findObject(ENTER_FIRE_ID).interact("Enter");
    		}else{
//    			walk(PATH_FIRE_MAKE);
    			walker.walkPath(PATH_FIRE_MAKE);
        	}
    	}
    }
    /******************火片制作结束******************/
}
