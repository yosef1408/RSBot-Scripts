package TMKCodes.Gathering.Tasks;

import TMKCodes.Gathering.Task;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.Random;

public class Bank extends Task {

    private int fishes[] = { 317, 321, 327, 331, 345, 321, 335, 341, 349, 353, 359, 363, 371, 377, 383, 389, 5001, 3379, 7944, 10138, 11328, 11330 };
    private int foods[] = { 319, 323, 379, 380, 373, 374, 315, 316, 325, 326, 329, 330, 333, 334, 347, 348, 351, 352, 355, 356, 361, 362, 1971, 1993, 1994, 2003, 2004, 2140, 2141, 2142, 2143, 2309, 2310 };
    private int woods[] = { 1511, 1521, 1519, 1515 };
    private int ores[] = { 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 444, 445, 453, 454, 447, 448, 449, 450, 451, 452, 1617, 1618, 1619, 1620, 1621, 1622, 1623, 1625, 1626, 1627, 1628, 1629, 1630, 1631, 1632 };
    private int items[];
    private int bankBox[] = { 6943, 6946, 6947, 6493, 7409, 18491, 23740, 23741, 24101 };
    private String skill;

    public Bank(ClientContext ctx, String skill) {
        super(ctx);
        this.skill = skill;
    }

    @Override
    public boolean activate() {
        System.out.println("Skill:" + skill);
        if(skill.equals("Woodcutting")) {
            items = woods;
        } else if(skill.equals("Fishing")) {
            items = fishes;
        } else if(skill.equals("Mining")) {
            items = ores;
        } else if(skill.equals("Cooking")) {
            items = fishes;
        }
        GameObject bank = ctx.objects.select().id(bankBox).nearest().poll();
        if(ctx.players.local().tile().distanceTo(bank) < 5) {
            for(int id : items) {
                if(skill.equals("Woodcutting") || skill.equals("Mining") || skill.equals("Fishing")) {
                    System.out.println(ctx.inventory.select().id(id).count());
                    if (ctx.inventory.select().id(id).count() > 0) {
                        System.out.println("bank box = true");
                        return true;
                    }
                } else if(skill.equals("Combat") || skill.equals("Cooking")){
                    System.out.println("Combat/Cooking: " + ctx.inventory.select().id(foods).count());
                    if(ctx.inventory.select().id(foods).count() > 0 || ctx.inventory.select().id(fishes).count() == 0) {
                        System.out.println("bank box = true");
                        return true;
                    }
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
            Thread.sleep(Random.nextInt(4800, 2400));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ctx.bank.opened()) {
            if (skill.equals("Mining") || skill.equals("Fishing") || skill.equals("Woodcutting")) {
                for (int id : items) {
                    if (ctx.inventory.select().id(id).count() > 0) {
                        ctx.bank.deposit(id, org.powerbot.script.rt4.Bank.Amount.ALL);
                    }
                }
            } else if(skill.equals("Cooking")) {
                if(ctx.inventory.select().id(foods).count() > 0) {
                    System.out.println("Deposit inventory of cooked items");
                    ctx.bank.depositInventory();
                }
                for(int id : items) {
                    System.out.println("Food:" + id + " " + ctx.bank.select().id(id).poll().stackSize());
                    if(ctx.bank.select().id(id).poll().stackSize() > 0) {
                        ctx.bank.withdraw(id, org.powerbot.script.rt4.Bank.Amount.ALL);
                    }
                    if(ctx.inventory.select().count() == 28) {
                        break;
                    }
                }
            } else if(skill.equals("Combat")) {
                ctx.bank.depositInventory();
                for(int id : foods) {
                    if(ctx.inventory.select().id(id).count() > 0) {
                        ctx.bank.withdraw(id, org.powerbot.script.rt4.Bank.Amount.ALL);
                    }
                    if(ctx.inventory.select().count() == 28) {
                        break;
                    }
                }
            }
            try {
                Thread.sleep(Random.nextInt(2400, 600));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ctx.bank.close();
        }
    }
}
