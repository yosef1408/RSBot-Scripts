package PMiner.Tasks;

import PMiner.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Item;


import java.util.regex.Pattern;

public class Drop extends Task {

    public Drop(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {

        return ctx.inventory.select().count()>27;
    }

    @Override
    public void execute() {

        for(Item ore : ctx.inventory.select().name(Pattern.compile("(.*ore)|(Clay)|(Coal)"))){
            if(ctx.controller.isStopping()){
                break;
            }
            final int currentAmountOfOre = ctx.inventory.select().count();
            boolean shift = ctx.inventory.shiftDroppingEnabled();

            ctx.inventory.drop(ore, shift);
            Condition.wait(() -> ctx.inventory.select().count() != currentAmountOfOre,25,20);
        }

    }
}
