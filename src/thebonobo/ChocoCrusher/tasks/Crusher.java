package thebonobo.ChocoCrusher.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;
import thebonobo.ChocoCrusher.utils.Antiban;
import thebonobo.ChocoCrusher.utils.Info;
import thebonobo.ChocoCrusher.utils.Items;

import java.awt.*;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 16/09/17
 */

public class Crusher extends Task<ClientContext> {
    private Item knife;
    private Item chocolateBar;
    private int xKnife;
    private int yKnife;
    private int xBar;
    private int yBar;

    public Crusher(ClientContext ctx, Item knife) {
        super(ctx);
        this.knife = knife;
    }

    @Override
    public boolean activate() {
        return !ctx.bank.opened() && ctx.inventory.select().id(Items.CHOCOLATE_BAR).count() > 0 &&
                ctx.inventory.select().id(Items.KNIFE).count() == 1;
    }

    @Override
    public void execute() {
        Info.getInstance().setCurrentTask("Crushing");

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                UseKnifeOnBar();
                return ctx.inventory.select().id(Items.CHOCOLATE_BAR).count() == 0;
            }
        }, 50, 10000);


    }

    private void UseKnifeOnBar() {
        if (Random.nextInt(0, 50) == 3) {
            Antiban.MoveMouseOffScreen(ctx);
            Antiban.Wait(700, 9000);
        }

        if (Random.nextInt(0, 50) == 5) {
            Antiban.MissClick(ctx, Items.KNIFE);
        }

        xKnife = knife.centerPoint().x - 11 + Random.nextInt(0, 25);
        yKnife = knife.centerPoint().y - 12 + Random.nextInt(0, 25);
        ctx.input.click(new Point(xKnife, yKnife), true);

        Antiban.Wait(1, 15);

        chocolateBar = ctx.inventory.select().id(Items.CHOCOLATE_BAR).reverse().poll();
        xBar = chocolateBar.centerPoint().x - 11 + Random.nextInt(0, 25);
        yBar = chocolateBar.centerPoint().y - 12 + Random.nextInt(0, 25);
        if (ctx.inventory.selectedItem().valid())
            ctx.input.click(new Point(xBar, yBar), true);
    }
}
