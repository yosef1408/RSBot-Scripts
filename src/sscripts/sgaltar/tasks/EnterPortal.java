package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import sscripts.sgaltar.SGAltar;

import java.util.concurrent.Callable;

public class EnterPortal extends Task {
    public EnterPortal(ClientContext arg0) {
        super(arg0);
    }

    @Override
    public boolean activate() {
        final GameObject portal = ctx.objects.select().id(30172).poll();

        return portal.inViewport() && ctx.inventory.select().count() == 28 && !isWidgetOpend();
    }

    private boolean isWidgetOpend() {
        return ctx.widgets.component(162,33).visible();
    }

    @Override
    public void execute() {
        SGAltar.status = "Entering Portal";
        final GameObject portal = ctx.objects.select().id(30172).nearest().poll();
            if (portal.interact("Friend's house", "Portal")){
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !portal.inViewport() && ctx.widgets.component(162, 33).visible();
                    }
                }, 2500, 2);
            }
        }
    }


