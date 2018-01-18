package Aff1x.choppenheimer.Tasks;

import Aff1x.choppenheimer.Util.Config;
import Aff1x.choppenheimer.Util.TreeEnum;
import org.powerbot.script.Tile;
import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import Aff1x.choppenheimer.Task;
import z.Con;


public class Walk extends Task<ClientContext> {

    private final Area bankArea = new Area(
            new Tile(2719, 3496, 0),
            new Tile(2730, 3496, 0),
            new Tile(2730, 3488, 0),
            new Tile(2719, 3488, 0)
    );

    private final Tile[] BuildPath(TreeEnum.TreeType type){
        switch (type) {
            case MAPLE:
                return new Tile[] {
                    new Tile(2725, 3491, 0),
                    new Tile(2717, 3485, 0),
                    new Tile(2714, 3495, 0),
                    new Tile(2719, 3503, 0)
                };
            case OAK:
                return new Tile[] {
                        new Tile(2727, 3488, 0),
                        new Tile(2725, 3478, 0)
                };
            case REGULAR:
                return new Tile[] {
                        new Tile(2725, 3487, 0),
                        new Tile(2721, 3477, 0),
                        new Tile(2719, 3467, 0),
                        new Tile(2722, 3457, 0),
                        new Tile(2726, 3447, 0),
                        new Tile(2726, 3437, 0),
                        new Tile(2731, 3428, 0)
                };
            case WILLOW:
                return new Tile[] {
                        new Tile(2725, 3491, 0),
                        new Tile(2717, 3485, 0),
                        new Tile(2714, 3495, 0),
                        new Tile(2719, 3503, 0)
                };
            case MAGIC:
                return new Tile[] {
                        new Tile(2725, 3487, 0),
                        new Tile(2724, 3476, 0),
                        new Tile(2723, 3466, 0),
                        new Tile(2723, 3456, 0),
                        new Tile(2720, 3446, 0),
                        new Tile(2715, 3437, 0),
                        new Tile(2705, 3434, 0),
                        new Tile(2700, 3425, 0),
                        new Tile(2690, 3425, 0),
                        new Tile(2685, 3416, 0)
                };
        }

        return new Tile[] {
                new Tile(0,0)
        };
    }

    public Walk(ClientContext ctx) {
        super(ctx);
    }


    @Override
    public boolean activate(){
        return (ctx.backpack.select().isEmpty() || ctx.backpack.select().count() == 28)
                &&!bankArea.contains(ctx.players.local())
                &&bankArea.getClosestTo(ctx.players.local()).distanceTo(ctx.players.local())>5;
    }

    @Override
    public void execute(){
        System.out.println(Config.getTreeType());
        if(!ctx.players.local().inMotion())
            if(ctx.backpack.select().count() == 28) {
                ctx.movement.newTilePath(BuildPath(Config.getTreeType())).reverse().randomize(1, 1).traverse();
            }
            else {
                ctx.movement.newTilePath(BuildPath(Config.getTreeType())).randomize(1, 1).traverse();
            }
    }
}
