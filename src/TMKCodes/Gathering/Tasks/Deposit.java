package Gathering.Tasks;

import Gathering.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.DepositBox;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.GameObject;

import java.util.Random;
import java.util.concurrent.Callable;

public class Deposit extends Task {

    private int depositBox = 26254;

    private int[] fishes = { 317, 327, 331, 345, 321, 335, 341, 349, 353, 359, 363, 371, 377, 383, 389, 5001, 3379, 7944, 10138, 11328, 11330 };
    private int woods[] = { 1511, 1521, 1519 };

    private int items[];
    Random rand  = new Random();
    private String skill;

    public Deposit(ClientContext ctx, String skill) {
        super(ctx);
        this.skill = skill;
    }

    @Override
    public boolean activate() {
        System.out.println(skill);
        if(skill.equals("Fishing")) {
            items = fishes;
        } else if(skill.equals("Woodcutting")) {
            items = woods;
        }
        System.out.println(items);
        GameObject deposit = ctx.objects.select().id(depositBox).nearest().poll();
        if(ctx.players.local().tile().distanceTo(deposit) < 5) {
            for(int id : items) {
                if(ctx.inventory.select().id(id).count() > 0) {
                    System.out.println("deposit box = true");
                    return true;
                }
            }
        }
        System.out.println("deposit box = false");
        return false;
    }

    @Override
    public void execute() {
        GameObject deposit = ctx.objects.select().id(depositBox).nearest().poll();
        if(!ctx.depositBox.opened()) {
            if(deposit.interact("Deposit") && ctx.game.crosshair() == Game.Crosshair.ACTION) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.depositBox.opened();
                    }
                }, 350, rand.nextInt(20) + 10);
                for (int id : items) {
                    System.out.println("item " + id + " count " + ctx.inventory.select().id(id).count());
                    if (ctx.inventory.select().id(id).count() > 0) {
                        System.out.println("Deposit all of item");
                        ctx.depositBox.deposit(id, DepositBox.Amount.ALL);
                    }
                }
                System.out.println("Close deposit box");
                ctx.depositBox.close();
            }
        } else {
            System.out.println("Close deposit box");
            ctx.depositBox.close();
        }
    }
}
