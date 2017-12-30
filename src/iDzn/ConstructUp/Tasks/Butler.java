package iDzn.ConstructUp.Tasks;

import iDzn.ConstructUp.ConstructUp;
import iDzn.ConstructUp.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;

import java.util.concurrent.Callable;

public class Butler extends Task<ClientContext> {
    ConstructUp main;

    public Butler(ClientContext ctx, ConstructUp main) {

        super(ctx);
        this.main = main;

    }

    private Npc Butler;

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(main.Planks).count() < main.PlanksRequired
                || ctx.widgets.widget(162).component(33).visible()
                || ctx.widgets.widget(231).component(0).visible()
                || ctx.widgets.widget(229).component(0).visible();
    }

    @Override
    public void execute() {
        Random rando = new Random();
        Item nP = ctx.inventory.select().id(main.nPlanks).poll();
        Item P = ctx.inventory.select().id(main.Planks).poll();
        Butler = ctx.npcs.select().name("Demon butler", "Butler").nearest().poll();

        if (!Butler.inViewport() && main.Waiting==0) {
            System.out.println("Calling slave");
            ctx.game.tab(Game.Tab.OPTIONS);
            ctx.widgets.widget(261).component(75).click();
            Condition.sleep(rando.nextInt(800, 1000));
            ctx.widgets.widget(370).component(15).component(0).click();
            Condition.sleep(rando.nextInt(800, 1000));
            ctx.game.tab(Game.Tab.INVENTORY);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return Butler.inViewport();
                }
            }, 50, 15);
        }
        if ((main.Waiting==0 && Butler.inViewport() && nP.stackSize() > main.PlanksRequired
                && ctx.widgets.widget(162).component(43).visible())){
            Butler.interact("Talk-to");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(219).component(0).visible();
                }
            }, 50, 15);
        }

        if (ctx.widgets.widget(219).component(0).component(1).text().contains(main.NotedWidgetText)) {
            ctx.widgets.widget(219).component(0).component(1).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(219).component(3).visible();
                }
            }, 150, 15);
        }

        if (ctx.widgets.widget(219).component(0).component(1).visible()
                && !ctx.widgets.widget(219).component(0).component(1).text().contains(main.NotedWidgetText)
                && !ctx.widgets.widget(219).component(0).component(1).text().equalsIgnoreCase("Yes")
                && ctx.inventory.select().id(P).count() < main.PlanksRequired){
                System.out.println("Using planks on butler");
                ctx.camera.turnTo(Butler);
                nP.interact("Use");
                Butler.interact("Use");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.widget(231).component(1).visible();
                    }
                }, 150, 15);
            }
        if (ctx.widgets.widget(231).component(3).visible()
                && (ctx.widgets.widget(231).component(3).text().contains("certificate")
                || ctx.widgets.widget(231).component(3).text().contains("Thank")
                || ctx.widgets.widget(231).component(3).text().contains("command")
                || ctx.widgets.widget(231).component(3).text().contains("bidding")
                || ctx.widgets.widget(231).component(3).text().contains("Yes, sir")
                || ctx.widgets.widget(231).component(3).text().contains("You rang"))) {
            System.out.println("Clicking to continue");
            ctx.widgets.widget(231).component(2).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(219).component(1).text().equalsIgnoreCase("Yes");
                }
            }, 150, 15);
        }

        if (ctx.widgets.widget(219).component(1).visible()
                && ctx.widgets.widget(219).component(1).text().equalsIgnoreCase("Yes")) {
            System.out.println("Yes, please unnote my planks");
            ctx.widgets.widget(219).component(1).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.widgets.widget(219).component(1).visible();
                }
            }, 150, 15);

        }
        if (ctx.widgets.widget(162).component(34).visible()
                && ctx.widgets.widget(162).component(33).text().contains("Enter")) {
            ctx.input.sendln("33");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(231).component(2).visible();
                }
            }, 150, 15);
        }

        if (ctx.widgets.widget(231).component(3).visible()
                && ctx.widgets.widget(231).component(3).text().contains("coins")) {
            System.out.println("Asking for payment");
            ctx.widgets.widget(231).component(2).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(219).component(1).visible();
                }
            }, 80, 15);
        }
        if (ctx.widgets.widget(219).component(0).component(1).visible()
                && ctx.widgets.widget(219).component(0).component(1).text().contains("coins")) {
            System.out.println("Agreeing to payment");
            ctx.widgets.widget(219).component(0).component(1).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(219).component(1).visible();
                }
            }, 80, 15);
        }

        if (ctx.widgets.widget(193).component(1).visible()) {
            ctx.widgets.widget(193).component(2).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.widgets.widget(193).component(1).visible();
                }
            }, 80, 15);
        }
        if (ctx.widgets.widget(219).component(0).component(2).text().equalsIgnoreCase("Thanks")) {
            ctx.widgets.widget(219).component(0).component(2).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(217).component(2).visible();
                }
            }, 80, 15);
        }
        if (ctx.widgets.widget(217).component(2).visible()
                && ctx.widgets.widget(217).component(3).text().equalsIgnoreCase("Thanks.")){
            ctx.widgets.widget(217).component(2).click();
        }
        if (ctx.widgets.widget(229).component(0).visible()){
            ctx.widgets.widget(229).component(1).click();
        }

    }

}
