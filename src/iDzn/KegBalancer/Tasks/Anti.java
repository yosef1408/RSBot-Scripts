package iDzn.KegBalancer.Tasks;

import iDzn.KegBalancer.Task;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;




public class Anti extends Task<org.powerbot.script.ClientContext<org.powerbot.bot.rt4.client.Client>> {
    public Anti(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        Random RANDO= new Random();
        int rando = RANDO.nextInt(1,300);
        if (rando == 12 || rando ==243 || rando == 101)
           System.out.println("Antiban Random # Gen: " + rando);
        return rando == 12 || rando == 243 || rando == 101;


    }

    @Override
    public void execute() {
        GameObject Barrel = ctx.objects.select().id(14867).poll();
        GameObject obj = ctx.objects.select().id(15672).poll();
        GameObject Shelves = ctx.objects.select().id(1018).poll();
        Npc Jimmy = ctx.npcs.select().id(6072).poll();
        Random RANDO = new Random();
        int rando = RANDO.nextInt(1,6);
        switch (rando) {
            case 1:
                ctx.camera.turnTo(Barrel);
                System.out.println("Anti-Barrel");
                break;
            case 2:
                ctx.camera.turnTo(Shelves);
                System.out.println("Anti-Shelves");
                break;
            case 3:
                ctx.camera.turnTo(Jimmy);
                System.out.println("Anti-Jimmy");
                break;
            case 4:
                int x = Random.nextInt(999, -100);
                int y = Random.nextInt(999, -100);
                ctx.input.move(x, y);
                System.out.println("Anti-Mouse");
                break;
            case 5:
                int h = Random.nextInt(554, 602);
                int v = Random.nextInt(244, 265);
                ctx.game.tab(Game.Tab.STATS);
                System.out.println("Anti-Stats");
                ctx.input.move(h, v);
                break;
            default:
                ctx.camera.turnTo(obj);
                System.out.println("Anti-Obj");

        }

    }
}