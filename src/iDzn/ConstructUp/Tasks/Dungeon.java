package iDzn.ConstructUp.Tasks;

import iDzn.ConstructUp.ConstructUp;
import iDzn.ConstructUp.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Dungeon extends Task<ClientContext> {

    ConstructUp main;

    public Dungeon(ClientContext ctx, ConstructUp main) {


        super(ctx);
        this.main = main;

    }
    GameObject Dungeon = ctx.objects.select().id(4529).nearest().poll();
    GameObject DungeonDoor = ctx.objects.select().id(15317).nearest().poll();

    @Override
    public boolean activate() {
        return Dungeon.valid();
    }

    @Override
    public void execute() {

        if (!Dungeon.inViewport()) {
            System.out.println("Locating Dungeon");
            ctx.camera.turnTo(Dungeon);
            ctx.movement.step(Dungeon);
            System.out.println("Moving to Dungeon");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.objects.select().id(Dungeon).nearest().poll().inViewport();
                }
            }, 350, 15);
        }

        if (Dungeon.inViewport()) {
            System.out.println("Climbing down dungeon");
            Dungeon.interact("Enter");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return DungeonDoor.inViewport();
                }
            }, 150, 5);
        }
    }
}
