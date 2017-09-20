package sscripts.sgaltar.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import sscripts.sgaltar.SGAltar;

import java.util.concurrent.Callable;

public class PortalChat extends Task {
    public PortalChat(ClientContext arg0) {
        super(arg0);
    }

    @Override
    public boolean activate() {
        return ctx.widgets.component(162,32).valid();
    }

    @Override
    public void execute() {
        if (ctx.widgets.component(162, 32).component(0).valid() && ctx.widgets.component(162,32).component(0).visible()) {
            ctx.widgets.component(162, 32).component(0).click();
            SGAltar.inHouse = true;
        } else if (SGAltar.stop){
           //Stop Script
        }
        else {
            if (ctx.widgets.component(162, 33).visible()) {
                if (ctx.input.sendln(SGAltar.playername)) {
                    SGAltar.inHouse = true;
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
