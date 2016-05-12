package superchaoran.Mining;

import org.powerbot.script.Filter;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;

/**
 * Created by chaoran on 5/11/16.
 */

@Script.Manifest(
        name = "Power Mining", properties = "author=superchaoran; topic=-5; client=6;",
        description = "Mine and drop ores"
)
public class PowerMining extends PollingScript<ClientContext>{
    private int rockIds[] = {11955, 11956};
    private int oreId = 440;

    @Override
    public void poll(){
        switch (state()) {
            case Mining:
                final GameObject rock = ctx.objects.select().id(rockIds).nearest().poll();
                rock.interact("Mine");
//                    if(rock.inViewport()) {
//                        rock.interact("Mine");
//                    }
//                    } else {
//                        ctx.movement.step(rockIds[0]);
//                        ctx.camera.turnTo(rockIds[1]);
//                    }
                break;
            case DROP:
                ctx.backpack.select().id(oreId).each(new Filter<Item>() {
                    @Override
                    public boolean accept(Item item) {
                        return item.interact("Drop");
                    }
                });
                break;
        }

    }

    private State state() {
        if (ctx.backpack.select().id(oreId).isEmpty()) {
            return State.Mining;
        } else {
            return State.DROP;
        }
    }

    private enum State {
        Mining, DROP
    }
}
