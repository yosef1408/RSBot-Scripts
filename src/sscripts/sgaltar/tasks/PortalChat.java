package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import sscripts.sgaltar.SGAltar;

import java.util.concurrent.Callable;

public class PortalChat extends Task {
    public PortalChat(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.widgets.widget(162).component(32).valid();
    }

    @Override
    public void execute() {
        if (SGAltar.stop){
            ctx.controller.stop();
            System.out.println("Stop - Host offline - restart and enter new host name");
        }
        if(ctx.widgets.component(162,32).component(0).visible() && ctx.widgets.component(162, 32).component(0).toString().contains(SGAltar.playername)) {
            ctx.widgets.component(162, 32).component(0).click();
        }
        else {
            if (ctx.widgets.component(162, 33).visible() && !ctx.widgets.component(162,32).toString().contains(SGAltar.playername)) {
                if (ctx.input.sendln(SGAltar.playername)) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return !ctx.widgets.component(162, 33).visible();
                        }
                    }, 500, 2);
                }
            }
        }
    }
}
