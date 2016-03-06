package andyroo.util;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

@Script.Manifest(
        name = "Distance Logger", properties = "author=andyroo; topic=123; client=4;",
        description = "Outputs distance to target tile"
)

public class DistanceLogger extends PollingScript<ClientContext> {
    public static Tile t = new Tile(1942, 4967, 0);

    public void poll() {
        System.out.println(t.distanceTo(ctx.players.local()));
        Condition.sleep(1000);
    }
}
