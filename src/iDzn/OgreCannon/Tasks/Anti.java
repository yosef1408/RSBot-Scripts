package iDzn.OgreCannon.Tasks;

import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Npc;

import java.awt.*;


public class Anti extends iDzn.OgreCannon.Task<org.powerbot.script.ClientContext<Client>>{
    public Anti(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        Random RANDO= new Random();
        int rando = RANDO.nextInt(1,400);
        if (rando == 12 || rando ==243 || rando == 101)
            System.out.println("Antiban Random # Gen: " + rando);
        return rando == 12 || rando == 243 || rando == 101;


    }

    @Override
    public void execute() {
        GameObject Stool = ctx.objects.select().id(1102).poll();
        GameObject obj = ctx.objects.select().id(855).poll();
        Npc npc = ctx.npcs.select().id(1153).poll();
        Random RANDO = new Random();
        int rando = RANDO.nextInt(1,6);
        switch (rando) {
            case 1:
                ctx.camera.turnTo(Stool);
                System.out.println("Anti-Stool");
                break;
            case 2:
                int s = Random.nextInt(555, 570);
                int u = Random.nextInt(10, 30);
                ctx.input.move(s, u);
                ctx.input.click(new Point(s, u), true);
                ctx.camera.pitch(80);
                System.out.println("Anti-Compass");
                break;
            case 3:
                ctx.camera.turnTo(npc);
                System.out.println("Anti-Npc");
                break;
            case 4:
                int x = Random.nextInt(2000, -1000);
                int y = Random.nextInt(2000, -1000);
                ctx.input.move(x, y);
                System.out.println("Anti-Mouse");
                break;
            case 5:
                int h = Random.nextInt(551, 604);
                int v = Random.nextInt(306, 326);
                ctx.game.tab(Game.Tab.STATS);
                System.out.println("Anti-Stats");
                ctx.input.move(h, v);
                Condition.sleep(RANDO.nextInt(1000, 2000));
                ctx.game.tab(Game.Tab.INVENTORY);
                break;
            default:
                ctx.camera.turnTo(obj);
                System.out.println("Anti-Obj");

        }

    }
}
