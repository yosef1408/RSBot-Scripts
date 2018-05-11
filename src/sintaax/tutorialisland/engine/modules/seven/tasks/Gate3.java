package sintaax.tutorialisland.engine.modules.seven.tasks;

import sintaax.tutorialisland.engine.constants.GameObjects;
import sintaax.tutorialisland.engine.constants.NPCs;
import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class Gate3 extends Task<ClientContext> {
    public Gate3(ClientContext ctx) {
        super(ctx);
    }

    private GameObjects gameObjects = new GameObjects(ctx);
    private NPCs npcs = new NPCs(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.get(281) == 440 || varps.get(281) == 470;
    }

    @Override
    public void execute() {
        if (varps.get(281) == 440)
            gameObjects.open(gameObjects.GATE3);
        else {
            if (!ctx.movement.reachable(ctx.players.local().tile(), new Tile(3111, 9519)))
                gameObjects.open(gameObjects.GATE3);
            else
                npcs.talk(npcs.COMBAT_INSTRUCTOR);
        }
    }
}
