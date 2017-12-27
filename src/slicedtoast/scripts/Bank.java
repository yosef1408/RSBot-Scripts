package scripts;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Bank extends Task
{
    public Bank(ClientContext ctx) {
        super(ctx);
    }

    final int[] bankBounds = {-48, 56, -208, 0, -64, 44};

    @Override
    public boolean activate()
    {
        return ctx.inventory.select().count() == 28 || ctx.inventory.select().id(995).count() == 0; //activate if inventory is full, or out of coins
    }

    @Override
    public void execute()
    {
        ctx.camera.pitch(0); //make sure camera pitch is all the way down
        GameObject bank = ctx.objects.select().id(6943).nearest().poll(); //select a bank booth that is viewable
        bank.bounds(bankBounds);
        ctx.camera.turnTo(bank);
        bank.interact("Bank");
        sleep(1000);
        while(!ctx.players.local().inMotion() && !ctx.widgets.widget(231).component(2).visible())
        {
            ctx.camera.turnTo(bank); //turn towards it
            ctx.movement.step(bank); //walk towards it
            bank.interact("Bank");
        }
        Condition.wait(new Callable<Boolean>(){ //wait until bank is opened
            @Override
            public Boolean call() throws Exception
            {
                return ctx.bank.opened();
            }
        }, 1000, 20); //check to see if bank is open once every second for 20 seconds
        ctx.bank.depositAllExcept(995); //deposit everything except coins
        sleep(getRand(400, 700));
        ctx.bank.close();
    }
}
