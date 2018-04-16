package Trikkstr.Scripts.Tasks;

import Trikkstr.Scripts.GoblinKiller.CONSTANTS;
import Trikkstr.Scripts.GoblinKiller.GoblinKiller;
import Trikkstr.Scripts.GoblinKiller.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

import java.util.concurrent.Callable;

public class Bank extends Task
{
    private final Walker walker = new Walker(ctx);

    private final int FOOD[] = { 315, 1971, 2309 };

    private int bankBalance;
    private int spendable;
    private int foodAvailable;

    public Bank(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        //will not go to the bank to get food unless at least 30 coins are in the inventory while they are near goblins,
        //and if the player has no food, and  if the player is not in combat
        //this allows the 'Fight' task to execute instead so that the bot can loot nearby coins until it has enough
        return ctx.inventory.select().id(FOOD).count() < 1
                && !ctx.players.local().inCombat()
                && (ctx.inventory.select().id(995).count(true) > 29 ||
                (ctx.inventory.select().id(995).count(true) < 10 && ctx.players.local().tile().x() > 3267));
    }

    @Override
    public void execute()
    {
        System.out.printf("Executing Bank.\n");

        //Walk to the bank
        if(ctx.players.local().tile().distanceTo(ctx.bank.nearest()) > 5)
        {
            walkToBank();
        }

        //if(near bank)
        //  open bank
        //  deposit inventory
        //  withdraw food/coins
        else if(ctx.players.local().tile().distanceTo(ctx.bank.nearest()) < 6)
        {
            makeDeposit();

            if (ctx.bank.opened())
            {
               makeWithdraw();
            }
        }
    }

    private void walkToBank()
    {
        walker.walkPathReverse(CONSTANTS.AK_BANK_TO_GOBLINS);

        //if close to the gate and not already on the other side, then pay the toll
        if(ctx.objects.select().id(2882).poll().tile().distanceTo(ctx.players.local()) < 4
                && ctx.players.local().tile().x() < 3268)
        {
            System.out.printf("Opening Al-Kharid Gate\n");
            if(!ctx.objects.select().id(2882).poll().inViewport())
                ctx.camera.turnTo(ctx.objects.select().id(2882).poll());

            ctx.objects.select().id(2882).poll().interact("Pay-toll(10gp)", "Gate");

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().tile().x() > 3267;
                }
            }, 500, 6);
        }
    }

    private void makeDeposit()
    {
        ctx.camera.turnTo(ctx.bank.nearest());

        //open the bank
        while (!ctx.bank.opened() && ctx.players.local().tile().distanceTo(ctx.bank.nearest().tile()) <= 5)
            ctx.bank.open();

        ctx.bank.depositInventory();

        //wait a moment for items to deposit
        Condition.sleep(Random.nextInt(1500, 2666));
    }

    private void makeWithdraw()
    {
        //check the balance
        bankBalance = ctx.bank.select().id(995).count(true);
        System.out.printf("Balance: %d\n", bankBalance);

        //subtract 10 from the balance (10 coins saved to get back to the goblins
        spendable = bankBalance - 10;
        System.out.printf("Spendable: %d\n", spendable);

        //check to see if there are kebabs in the bank
        foodAvailable = ctx.bank.select().id(FOOD[1]).count(true);
        System.out.printf("Food Available In Bank: %d\n", foodAvailable);

        //if there are 10 coins or less and no food then exit (10 to cross and at least one coin or food to eat)
        if (bankBalance < 10)
        {
            System.out.printf("Not enough coins: 10 are needed to pay the gate toll.\n");
            ctx.controller.stop();
        }
        else if (bankBalance < 11 && foodAvailable < 1)
        {
            System.out.printf("Not enough supplies: Need at least 10 coins and 1 kebab OR just 11 coins.\n");
            ctx.controller.stop();
        }
        else
            ;

        ctx.bank.withdraw(995, 10);

        //if there is no food
        if (foodAvailable < 1)
        {
            //withdraw up to 10 additional coins
            if (spendable < 10)
                ctx.bank.withdraw(995, spendable);
            else
                ctx.bank.withdraw(995, 10);

            ctx.bank.close();
        }
        //if there are less than 10 kebabs then just withdraw what is available
        //otherwise withdraw 10 kebabs
        else if (foodAvailable < 10)
        {
            ctx.bank.withdraw(FOOD[1], foodAvailable);
            ctx.bank.close();
        }
        else
        {
            ctx.bank.withdraw(FOOD[1], 10);
            ctx.bank.close();
        }

        GoblinKiller.setBanked(true);
    }
}
