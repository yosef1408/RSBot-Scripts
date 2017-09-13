package thebonobo.SalmonCollector.tasks;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import thebonobo.SalmonCollector.utils.Info;
import thebonobo.SalmonCollector.utils.Objects;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

public class Bank extends Task<ClientContext> {

    private Calendar calendar = new GregorianCalendar();


    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.objects.select(3).id(Objects.BANK_BOOTH).nearest().poll().valid() &&
                ctx.inventory.count() == 28;
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Banking");
        GameObject bank = ctx.objects.select(3).id(Objects.BANK_BOOTH).nearest().poll();
        if (bank.inViewport()) {

            if (ctx.bank.opened()) {
                ctx.bank.depositInventory();
                ctx.bank.close();

            } else {
                bank.interact("Bank");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
                },200,10);
            }

        } else {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() {
                    ctx.camera.pitch();
                    return bank.inViewport();
                }
            }, 200, 5);
        }
    }
}

