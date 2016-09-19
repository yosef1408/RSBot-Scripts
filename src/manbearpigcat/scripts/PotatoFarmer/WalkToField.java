package manbearpigcat.scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.*;
import org.powerbot.script.rt6.Component;

import java.util.concurrent.Callable;



/**
 * Created by Shan on 2016-08-17.
 */
public class WalkToField extends Task<ClientContext> {

    public static final Tile FIELD_TILE = new Tile(3262, 3322, 0);

    public static Stats sPots = PotatoPicker.sPots;

    public static final int homeWidget = 1465, homeComponent = 56;
    public static final int teleWidget = 1092, varrockComponent = 21;

    public static final Tile T1 = new Tile(3265, 3320, 0);
    public static final Tile T2 = new Tile(3244, 3300, 0);
    public static Area potArea = new Area(T1, T2);

    public static final Tile lodestone = new Tile(3214, 3376, 0);

    public static final Tile GATE_AREA_T1 = new Tile(3267, 3325, 0);
    public static final Tile GATE_AREA_T2 = new Tile(3258, 3320, 0);
    public static final Area area = new Area(GATE_AREA_T1, GATE_AREA_T2);

    public static final Tile FIELD_AREA_T1 = new Tile(3211, 3378, 0); //varrock lodestone
    public static final Tile FIELD_AREA_T2 = new Tile(3267, 3299, 0); //edge of potato farm
    public static final Area fieldArea = new Area(FIELD_AREA_T1, FIELD_AREA_T2);

    public WalkToField(ClientContext ctx){
        super(ctx);
    }

    //If bag is empty, and the player is not standing in the area from the varrock lodestone to the potato farm
    public boolean activate(){
        return ctx.backpack.select().count() < 28 &&
                (!area.contains(ctx.players.local()) && !potArea.contains(ctx.players.local()));
    }


    public void execute(){
        sPots.setState("Walking to field.");

        if(ctx.movement.energyLevel() > 80){
            ctx.movement.running(true);
        }

        final Component home = ctx.widgets.component(homeWidget, homeComponent);
        final Component tele = ctx.widgets.component(teleWidget, varrockComponent);

        //If the player is not in the general area south of varrock, teleport to varrock lodestone
        if(!fieldArea.contains(ctx.players.local())) {
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
                            return lodestone.distanceTo(ctx.players.local()) == 0;
                        }
                    }, 1000, 15);

                }
            }
        }

        ctx.movement.step(FIELD_TILE);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return FIELD_TILE.distanceTo(ctx.players.local()) < 5;
            }
        }, 1000, 2);
    }
}