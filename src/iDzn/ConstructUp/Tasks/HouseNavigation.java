package iDzn.ConstructUp.Tasks;

import iDzn.ConstructUp.ConstructUp;
import iDzn.ConstructUp.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;

import java.util.concurrent.Callable;

public class HouseNavigation extends Task<ClientContext> {

    ConstructUp main;

    public HouseNavigation(ClientContext ctx, ConstructUp main) {


        super(ctx);
        this.main = main;

    }
    private Npc Butler;
    GameObject DoorHotspot = ctx.objects.select().id(15316, 15313, 15314, 15307, 15308, 15309, 15310, 15311, 15312, 15305, 13506).nearest().poll();

    @Override
    public boolean activate() {
        return  ctx.inventory.select().id(main.nPlanks).count()>0 && ctx.players.local().animation()==4074
                || ctx.inventory.select().id(main.Planks).count()>main.PlanksRequired && !ctx.players.local().inMotion()
                || main.Obj!=13344;
    }

    @Override
    public void execute() {
        Butler = ctx.npcs.select().name("Demon butler", "Butler").nearest().poll();
       if ((!ctx.objects.select().id(main.ObjSpace, main.Obj).nearest().poll().inViewport() && (DoorHotspot.inViewport())
               && ctx.inventory.select().id(main.Planks).count()>main.PlanksRequired)) {
            System.out.println("Locating Object");
            ctx.camera.turnTo(ctx.objects.select().id(main.ObjSpace, main.Obj).nearest().poll());
            ctx.movement.step(ctx.objects.select().id(main.ObjSpace, main.Obj).nearest().poll());
            System.out.println("Moving to Object");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.objects.select().id(main.ObjSpace, main.Obj).nearest().poll().inViewport();
                }
            }, 350, 15);
        }

        if (ctx.players.local().animation()==4074) {
            System.out.println("Stop sleeping on the job!");
            ctx.movement.step(ctx.players.local());
            System.out.println("That's better.");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.objects.select().id(main.ObjSpace, main.Obj).nearest().poll().inViewport();
                }
            }, 150, 5);
            }
    }
}
