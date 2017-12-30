package iDzn.ConstructUp.Tasks;

import iDzn.ConstructUp.ConstructUp;
import iDzn.ConstructUp.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.awt.*;
import java.util.concurrent.Callable;


public class CCWidgets  extends Task<ClientContext> {

    ConstructUp main;

    public CCWidgets(ClientContext ctx, ConstructUp main) {

        super(ctx);
        this.main = main;

    }

    @Override
    public boolean activate() {
        return ctx.widgets.widget(219).component(0).visible()
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
        if (main.WrongWidget.visible() || ctx.widgets.widget(212).component(1).component(1).text().contains("Room Creation")) {
            Random rando = new Random();
            int h = Random.nextInt(480, 490);
            int v = Random.nextInt(32, 42);
            ctx.input.click(new Point(h, v), true);
            System.out.println("Wrong interface.");
            Condition.sleep(rando.nextInt(500, 1000));
            System.out.println("Trying again.");
            ctx.camera.pitch(80);
        }

        if (ctx.widgets.widget(219).component(0).component(1).text().equalsIgnoreCase("Yes")) {
            ctx.widgets.widget(219).component(0).component(1).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return (ctx.players.local().animation() == 3685);
                }
            }, 75, 10);

        }
    }
}
