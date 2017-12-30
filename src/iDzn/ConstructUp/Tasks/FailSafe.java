package iDzn.ConstructUp.Tasks;

import iDzn.ConstructUp.ConstructUp;
import iDzn.ConstructUp.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.GameObject;

public class FailSafe extends Task<ClientContext> {

    ConstructUp main;

    public FailSafe(ClientContext ctx, ConstructUp main) {


        super(ctx);
        this.main = main;

    }
    GameObject DoorHotspot = ctx.objects.select().id(15316, 15313, 15314, 15307, 15308, 15309, 15310, 15311, 15312, 15305, 13506).nearest().poll();
    GameObject PortalInHouse = ctx.objects.select().id(4525).nearest().poll();

    @Override
    public boolean activate() {
        return !DoorHotspot.valid() && PortalInHouse.valid();
    }

    @Override
    public void execute() {
        ctx.game.tab(Game.Tab.OPTIONS);
        Random rando = new Random();
        if (ctx.widgets.widget(261).component(75).visible()){
            System.out.println("Moving to house options");
            ctx.widgets.widget(261).component(75).click();
            Condition.sleep(rando.nextInt(500,1000));
        }
        if (ctx.widgets.widget(370).component(5).visible() && ctx.widgets.widget(370).component(5).textureId()!=669){
            System.out.println("Turning building mode on.");
            ctx.widgets.widget(370).component(5).click();
            Condition.sleep(rando.nextInt(800,1000));
            ctx.game.tab(Game.Tab.INVENTORY);
        }

    }
}
