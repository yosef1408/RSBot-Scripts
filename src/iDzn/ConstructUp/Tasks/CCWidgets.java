package iDzn.ConstructUp.Tasks;

import iDzn.ConstructUp.ConstructUp;
import iDzn.ConstructUp.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;


public class CCWidgets  extends Task<ClientContext> {

    ConstructUp main;

    public CCWidgets(ClientContext ctx, ConstructUp main) {

        super(ctx);
        this.main = main;

    }

    @Override
    public boolean activate() {
        return main.ButlerText1.visible()
                || ctx.widgets.widget(458).component(0).visible() || ctx.widgets.widget(212).component(2).visible();
    }

    @Override
    public void execute() {
        GameObject Object = ctx.objects.select().id(main.Obj).nearest().poll();
        GameObject ObjectSpace = ctx.objects.select().id(main.ObjSpace).nearest().poll();
        Object.bounds(main.ObjBounds);
        ObjectSpace.bounds(main.ObjBounds);
        if (main.BuildNameWidget.text().equalsIgnoreCase(main.WidgetText)) {
            Random rando = new Random();
            System.out.println("Solving Widget");
            main.BuildWidget.interact("Build");
            System.out.println("Waiting to finish the long tiring build.");
            Condition.sleep(rando.nextInt(main.SleepX, main.SleepY));
            System.out.println("All finished");
        }
        if (ctx.widgets.widget(212).component(1).component(1).text().contains("Room Creation")) {
            ctx.widgets.widget(212).component(1).component(11).click();
            System.out.println("Wrong interface.");
            ctx.camera.pitch(80);
        } else if (main.WrongWidget.visible()){
            ctx.widgets.widget(458).component(1).component(11).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.widgets.widget(458).component(1).visible();
                }
            }, 85, 10);
        }

        if (main.ButlerText1.component(1).text().equalsIgnoreCase("Yes")) {
            main.ButlerText1.component(1).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return (ctx.players.local().animation() == 3685);
                }
            }, 75, 10);

        }
    }
}
