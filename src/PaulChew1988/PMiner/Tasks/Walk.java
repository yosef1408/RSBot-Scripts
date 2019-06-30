package PMiner.Tasks;

import PMiner.PMinerConst;
import PMiner.Task;
import PMiner.Walker;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.TilePath;

public class Walk extends Task {


    public Tile[] path = {};
    private final Walker walker = new Walker(ctx);
    int pathNo = 0;
    boolean chickens = false;
    boolean clay = false;




    public Walk(ClientContext ctx, Tile[] path) {
        super(ctx);
        this.path = path;
    }

    @Override
    public boolean activate() {
        if ((path.equals(PMinerConst.pathToChickens)) && !chickens) {
            pathNo = 1;
            return (path[path.length - 1].distanceTo(ctx.players.local()) > 9);

        } else if ((path.equals(PMinerConst.chickenToClay)) && !clay && ctx.players.local().combatLevel() > 12) {
            pathNo = 2;
            return (path[path.length - 1].distanceTo(ctx.players.local()) > 5);

        }
        else if(path.equals(PMinerConst.EastBankToGE)&&ctx.inventory.select().id(441).poll().stackSize() >=1){
            pathNo = 3;
            return path[path.length - 1].distanceTo(ctx.players.local())>5;
        }else if (!path.equals(PMinerConst.pathToChickens) && (!path.equals(PMinerConst.chickenToClay) && (!path.equals(PMinerConst.EastBankToGE)))) {
            return ctx.inventory.select().count() > 27 || ctx.inventory.select().count() < 28 && path[0].distanceTo(ctx.players.local()) > 10;
        }


        return false;
    }

    @Override
    public void execute() {
        if (!ctx.movement.running() && ctx.movement.energyLevel() > Random.nextInt(35, 56)) {
            ctx.movement.running(true);
        }

        if(pathNo == 1) {

            if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
                System.out.println("Walk = Chicken");

                if (path[path.length - 1].distanceTo(ctx.players.local()) <= 10 && ctx.players.local().combatLevel() >= 13) {
                    System.out.println("Boolean changed Chicken");
                    chickens = true;
                }else{
                    walker.walkPath(path);
                }

            }
        }
        if(pathNo == 2) {
            if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
                System.out.println("Walk = clay");


                if (path[path.length - 1].distanceTo(ctx.players.local()) <= 10) {
                    System.out.println("Boolean changed Clay");
                    clay = true;
                } else {
                    walker.walkPath(path);
                }

            }
        }
        else if(pathNo == 3){
            if (!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
                TilePath bankToGe = new TilePath(ctx,path);
                bankToGe.randomize(2,2);
                if (ctx.inventory.select().id(441).poll().stackSize() >=1){
                     bankToGe.traverse();
                }
            }
        }

     else{
         if ((!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 7)) {
           TilePath bank = new TilePath(ctx, path);
           bank.randomize(2,2);
            if (ctx.inventory.select().count() > 27) {

                bank.traverse();

            }
            else {
                bank = bank.reverse();
                bank.traverse();

            }

        }

    }

    }
}


