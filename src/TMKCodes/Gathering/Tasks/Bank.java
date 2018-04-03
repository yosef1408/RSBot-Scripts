package Gathering.Tasks;

import Gathering.Task;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.Random;

public class Bank extends Task {

    private int fishes[] = { 317, 327, 331, 345, 321, 335, 341, 349, 353, 359, 363, 371, 377, 383, 389, 5001, 3379, 7944, 10138, 11328, 11330 };
    private int woods[] = { 1511, 1521, 1519, 1515 };
    private int ores[] = { 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 444, 445, 453, 454, 447, 448, 449, 450, 451, 452, 1617, 1618, 1619, 1620, 1621, 1622, 1623, 1625, 1626, 1627, 1628, 1629, 1630, 1631, 1632 };
    private int items[];
    private int bankBox[] = { 6943, 6946, 6947, 7409, 18491, 18492, 23740, 23741, 24101 };
    private Random rand = new Random();
    private String skill;

    public Bank(ClientContext ctx, String skill) {
        super(ctx);
        this.skill = skill;
    }

    @Override
    public boolean activate() {
        if(skill.equals("Woodcutting")) {
            items = woods;
        } else if(skill.equals("Fishing")) {
            items = fishes;
        } else if(skill.equals("Mining")) {
            items = ores;
        }
        GameObject bank = ctx.objects.select().id(bankBox).nearest().poll();
        if(ctx.players.local().tile().distanceTo(bank) < 5) {
            System.out.println("bank box = true");
            for(int id : items) {
                if(ctx.inventory.select().id(id).count() > 0) {
                    return true;
                }
            }
        }
        System.out.println("bank box = false");
        return false;
    }

    @Override
    public void execute() {
        GameObject bank = ctx.objects.select().id(bankBox).nearest().poll();
        bank.interact("Bank");
        try {
            Thread.sleep(rand.nextInt(200) + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ctx.bank.opened()) {
            for(int id : items) {
                if(ctx.inventory.select().id(id).count() > 0) {
                    ctx.bank.deposit(id, org.powerbot.script.rt4.Bank.Amount.ALL);
                }
            }
            try {
                Thread.sleep(rand.nextInt(200) + 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.bank.close();
        }
    }
}
