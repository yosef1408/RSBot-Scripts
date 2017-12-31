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
        return main.PlanksCount < main.PlanksRequired
                || ctx.widgets.widget(162).component(33).visible()
                || ctx.widgets.widget(231).component(0).visible()
                || ctx.widgets.widget(229).component(0).visible();
    }

    @Override
    public void execute() {
        Random rando = new Random();
        Item nP = ctx.inventory.select().id(main.nPlanks).poll();
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
                && main.Username.visible())){
            Butler.interact("Talk-to");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return main.ButlerText1.visible();
                }
            }, 50, 15);
        }

        if (main.ButlerText1.component(1).text().contains(main.NotedWidgetText)) {
            main.ButlerText1.component(1).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.widget(219).component(3).visible();
                }
            }, 150, 15);
        }

        if (main.ButlerText1.component(1).visible()
                && !main.ButlerText1.component(1).text().contains(main.NotedWidgetText)
                && !main.ButlerText1.component(1).text().equalsIgnoreCase("Yes")
                && main.PlanksCount < main.PlanksRequired){
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
        if (main.ButlerText3.visible()
                && (main.ButlerText3.text().contains("certificate")
                || main.ButlerText3.text().contains("Thank")
                || main.ButlerText3.text().contains("command")
                || main.ButlerText3.text().contains("bidding")
                || main.ButlerText3.text().contains("Yes, sir")
                || main.ButlerText3.text().contains("You rang"))) {
            System.out.println("Clicking to continue");
            main.ButlerContinue2.click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return main.ButlerAgree1.text().equalsIgnoreCase("Yes");
                }
            }, 150, 15);
        }

        if (main.ButlerAgree1.visible()
                && main.ButlerAgree1.text().equalsIgnoreCase("Yes")) {
            System.out.println("Yes, please unnote my planks");
            main.ButlerAgree1.click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !main.ButlerAgree1.visible();
                }
            }, 150, 15);

        }
        if (ctx.widgets.widget(162).component(34).visible()
                && ctx.widgets.widget(162).component(33).text().contains("Enter")) {
            ctx.input.sendln("33");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return main.ButlerContinue2.visible();
                }
            }, 150, 15);
        }

        if (main.ButlerText3.visible()
                && main.ButlerText3.text().contains("coins")) {
            System.out.println("Asking for payment");
            main.ButlerContinue2.click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return main.ButlerAgree1.visible();
                }
            }, 80, 15);
        }
        if (main.ButlerText1.component(1).visible()
                && main.ButlerText1.component(1).text().contains("coins")) {
            System.out.println("Agreeing to payment");
            main.ButlerText1.component(1).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return main.ButlerAgree1.visible();
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
        if (main.ButlerText1.component(2).text().equalsIgnoreCase("Thanks")) {
            main.ButlerText1.component(2).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return main.ButlerContinue1.visible();
                }
            }, 80, 15);
        }
        if (main.ButlerContinue1.visible()
                && main.ButlerText2.text().equalsIgnoreCase("Thanks.")){
            main.ButlerContinue1.click();
        }
        if (ctx.widgets.widget(229).component(0).visible()){
            ctx.widgets.widget(229).component(1).click();
        }

    }

}
