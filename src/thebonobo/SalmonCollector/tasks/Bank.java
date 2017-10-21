package thebonobo.SalmonCollector.tasks;
import org.powerbot.bot.rt4.TClientState;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import thebonobo.SalmonCollector.utils.Info;
import thebonobo.SalmonCollector.utils.Items;
import thebonobo.SalmonCollector.utils.Objects;
import thebonobo.SalmonCollector.utils.UserProfile;

import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

public class Bank extends Task<ClientContext> {
    private Properties userProperties;

    public Bank(ClientContext ctx, Properties userProperties) {
        super(ctx);
        this.userProperties = userProperties;
    }

    @Override
    public boolean activate() {
        return ctx.objects.select(3).id(Objects.BANK_BOOTH).nearest().poll().valid() &&
                ctx.inventory.select().count() == 28;
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Banking");
        GameObject bank = ctx.objects.select(3).id(Objects.BANK_BOOTH).nearest().poll();
        if (bank.inViewport()) {

            if (ctx.bank.opened()) {
                depositInventory();
                System.out.println("deposited all items");
                ctx.bank.close();

            } else {
                bank.interact("Bank", bank.name());
                Condition.wait(() -> ctx.bank.opened(),200,10);
            }

        } else {
            Condition.wait(() -> {ctx.camera.turnTo(bank); return bank.inViewport();}, 300, 5);
        }
    }

    private void depositInventory(){
        if(userProperties.getProperty("deposit-inventory") == "1")
            ctx.bank.depositInventory();
        else {
            while (ctx.inventory.select().count() > 0  && ctx.game.loggedIn()){
                org.powerbot.script.rt4.Bank.Amount amount = org.powerbot.script.rt4.Bank.Amount.valueOf(userProperties.getProperty("deposit-amount"));
                ctx.bank.deposit(ctx.inventory.select().shuffle().poll().id(), amount);
                Condition.sleep(300);
            }
        }
    }
}

