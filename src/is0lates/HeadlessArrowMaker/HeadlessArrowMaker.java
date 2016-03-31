package HeadlessArrowMaker; /**
 * Created by Chris on 21-3-2016.
 */

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.*;

@Script.Manifest(name = "Arrow Shaft Maker", description = "Makes arrow shafts", properties = "client=6; topic=0")
public class HeadlessArrowMaker extends PollingScript<ClientContext> {

    private int shaftId = 52;

    @Override
    public void poll() {
        if(ctx.widgets.component(1370,12).visible()) {
            Condition.sleep(50);
            ctx.input.send("{VK_SPACE down}");
            Condition.sleep(50);
            ctx.input.send("{VK_SPACE up}");
            Condition.sleep(11000);
        } else {
            ctx.backpack.select().id(shaftId).poll().click();
        }

    }
}
