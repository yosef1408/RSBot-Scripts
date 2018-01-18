package Aff1x.choppenheimer.Tasks;

import Aff1x.choppenheimer.Util.TreeEnum;
import org.powerbot.script.Tile;
import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import Aff1x.choppenheimer.Task;


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
                        new Tile(1,1)
                };
            case REGULAR:
                return new Tile[] {
                        new Tile(1,1)
                };
            case MAGIC:
                return new Tile[] {
                        new Tile(1,1)
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
        return (ctx.backpack.select().count()==28 || ctx.backpack.select().isEmpty())
                &&!bankArea.contains(ctx.players.local())
                &&bankArea.getClosestTo(ctx.players.local()).distanceTo(ctx.players.local())>12;
    }

    @Override
    public void execute(){
        if(!ctx.players.local().inMotion())
            if(!ctx.backpack.isEmpty())
                ctx.movement.newTilePath(BuildPath(TreeEnum.TreeType.MAPLE)).reverse().randomize(1, 1).traverse();
            else
                ctx.movement.newTilePath(BuildPath(TreeEnum.TreeType.MAPLE)).randomize(1, 1).traverse();
    }
}
