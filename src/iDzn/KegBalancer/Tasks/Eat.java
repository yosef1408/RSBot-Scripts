package iDzn.KegBalancer.Tasks;

import iDzn.KegBalancer.KegBalancer;
import iDzn.KegBalancer.Task;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;


public class Eat extends Task<ClientContext> {
    private int FOOD [] = {379, 361, 1891, 1893, 1895, 373, 7946, 385};

    KegBalancer main;

    public Eat(ClientContext ctx, KegBalancer main) {

        super(ctx);
        this.main = main;

    }


    @Override
    public boolean activate() {
        return main.currentHealth < main.eatPercentage;
    }

    @Override
    public void execute() {
        Item EatME = ctx.inventory.select().id(379, 361, 1891, 1893, 1895, 373,7946, 385).poll();
             if (ctx.inventory.select().id(FOOD).count()>0) {
                 if (!ctx.game.tab(Game.Tab.INVENTORY)){
                     ctx.game.tab(Game.Tab.INVENTORY);
                 }
            EatME.interact("Eat");
            System.out.println("Eating Food");
        }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return (main.currentHealth > main.eatPercentage);
                }
            }, 250, 1);
        }

    }
