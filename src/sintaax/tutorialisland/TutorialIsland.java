package sintaax.tutorialisland;

import sintaax.tutorialisland.engine.Manager;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;

@Script.Manifest(name = "tutorialisland",
        description = "completes tutorial island",
        properties = "author = sintaax; topic=1345066; client=4;")
public class TutorialIsland extends PollingScript<ClientContext> {
    Manager manager = new Manager(ctx);

    @Override
    public void poll() {
        manager.execute();
    }
}
