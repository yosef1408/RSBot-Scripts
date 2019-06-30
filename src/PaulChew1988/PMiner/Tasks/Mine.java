package PMiner.Tasks;

import PMiner.Task;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Mine extends Task {

   int ROCK_IDs []= {};


    Tile rockLocation = Tile.NIL;

    public Mine(ClientContext ctx, int [] ROCK_IDs) {

        super(ctx);
        this.ROCK_IDs = ROCK_IDs;
    }



    @Override
    public boolean activate() {
        return ctx.objects.select().at(rockLocation).id(ROCK_IDs).poll().equals(ctx.objects.nil()) ||  ctx.players.local().animation() == -1 ;
    }

    @Override
    public void execute() {


            GameObject rock = ctx.objects.select().id(ROCK_IDs).nearest().poll();

            rockLocation = rock.tile();
            if(!rockLocation.equals(new Tile(3183,3377,0))){ // Ignores a rock which causes the script to run between 2 rocks.

            rock.interact("Mine");
            }

            Condition.wait(new Callable<Boolean>(){
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() != -1;
            }

            },200,10);
    }



}