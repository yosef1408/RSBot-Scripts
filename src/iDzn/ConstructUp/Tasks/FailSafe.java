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
    GameObject Doors = ctx.objects.select().id(13119, 13118).nearest().poll();
    GameObject PortalInHouse = ctx.objects.select().id(4525).nearest().poll();

    @Override
    public boolean activate() {
        return Doors.valid();
    }

    @Override
    public void execute() {
        ctx.game.tab(Game.Tab.OPTIONS);
        Random rando = new Random();
        if (PortalInHouse.valid() && ctx.widgets.widget(261).component(75).visible()){
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
