package TMKCodes.Gathering.Tasks;

import TMKCodes.Gathering.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.Random;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Woodcut extends Task {

    private ArrayList<Integer> wood = new ArrayList<Integer>();
    private String tree;

    public Woodcut(ClientContext ctx, String wood) {
        super(ctx);
        if(wood.equals("Normal")) {
            this.wood.add(1276);
            this.wood.add(1278);
            this.tree = "Tree";
        } else if(wood.equals("Oak")) {
            this.wood.add(1751);
            this.tree = "Oak";
        } else if(wood.equals("Willow")) {
            this.wood.add(1760);
            this.wood.add(1756);
            this.wood.add(1750);
            this.tree = "Willow";
        } else if(wood.equals("Maple")) {

        } else if(wood.equals("Yew")) {
            this.wood.add(1753);
            this.tree = "Yew";
        }
    }

    @Override
    public boolean activate() {
        System.out.println("Woodcutting");
        return ctx.players.local().animation() == -1 && ctx.inventory.select().count() < 28 && ctx.players.local().inCombat() == false;
    }

    @Override
    public void execute() {
        for (int id : wood) {
            for(GameObject tree : ctx.objects.select().id(id).nearest()) {
                System.out.println(tree.id());
                try {
                    Thread.sleep(Random.nextInt(4800, 2400));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(tree.valid()) {
                    if(!tree.inViewport()) {
                        ctx.camera.turnTo(tree);
                        ctx.camera.pitch(0);
                    }
                    tree.interact("Chop Down", this.tree);
                    try {
                        Thread.sleep(Random.nextInt(4800, 2400));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.players.local().animation() != -1;
                        }
                    }, Random.nextInt(120, 60), Random.nextInt(20, 10));
                    if (ctx.players.local().animation() != -1) {
                        break;
                    }
                }
            }
        }
    }
}
