package manbearpigcat.scripts;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import java.util.concurrent.Callable;

/**
 * Created by Shan on 2016-08-17.
 */
public class WalkToBank extends Task<ClientContext> {


    public static final Tile VARROCK_BANK_TILE = new Tile(3185, 3435, 0);
    public static final Tile ALKHARID_BANK_TILE = new Tile(3270, 3168, 0);
    public static final Tile ALKHARID_SUB_TILE = new Tile(3277, 3183, 0); //to avoid entering the palace

    public static final int homeWidget = 1465, homeComponent = 56;
    public static final int teleWidget = 1092, varrockComponent = 21, alkharidComponent = 10;


    public static final Tile vLodestone = new Tile(3214, 3376, 0);
    public static final Tile aLodestone = new Tile(3297, 3184, 0);

    public static Stats sPots = PotatoPicker.sPots;

    public static final Tile T1 = new Tile(3265, 3321, 0);
    public static final Tile T2 = new Tile(3241, 3299, 0);
    public static Area area = new Area(T1, T2);

    public WalkToBank(ClientContext ctx){
        super(ctx);
    }

    public boolean activate(){
        return ctx.backpack.select().count() == 28;
    }

    public void execute(){
        sPots.setState("Walking to bank.");
        if(ctx.movement.energyLevel() > 80){
            ctx.movement.running(true);
        }

        //Varrock banking
        if(sPots.getBank() == 1) {

            final Component home = ctx.widgets.component(homeWidget, homeComponent);
            final Component tele = ctx.widgets.component(teleWidget, varrockComponent);

            if(area.contains(ctx.players.local())) {
                if (home.valid() && home.visible()) {
                    if (home.click()) {

                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return tele.valid() && tele.visible();
                            }
                        }, 1000, 4);
                        tele.click();

                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return vLodestone.distanceTo(ctx.players.local()) == 0;
                            }
                        }, 1000, 15);
                    }
                }
            }

            ctx.movement.step(VARROCK_BANK_TILE);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return VARROCK_BANK_TILE.distanceTo(ctx.players.local()) < 5;
                }
            }, 1000, 15);

        }
        //Al-Kharid banking
        else{
            final Component home = ctx.widgets.component(homeWidget, homeComponent);
            final Component tele = ctx.widgets.component(teleWidget, alkharidComponent);

            if(area.contains(ctx.players.local())) {
                if (home.valid() && home.visible()) {
                    if (home.click()) {

                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return tele.valid() && tele.visible();
                            }
                        }, 1000, 4);
                        tele.click();

                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return aLodestone.distanceTo(ctx.players.local()) == 0;
                            }
                        }, 1000, 15);
                    }
                }
            }

            ctx.movement.step(ALKHARID_SUB_TILE);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ALKHARID_SUB_TILE.distanceTo(ctx.players.local()) < 5;
                }
            }, 1000, 8);

            ctx.movement.step(ALKHARID_BANK_TILE);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ALKHARID_BANK_TILE.distanceTo(ctx.players.local()) < 5;
                }
            }, 1000, 4);
        }
    }
}