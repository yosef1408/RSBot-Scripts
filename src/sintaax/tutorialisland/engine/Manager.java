package sintaax.tutorialisland.engine;

import sintaax.tutorialisland.engine.constants.Components;
import sintaax.tutorialisland.engine.handlers.TaskHandler;
import sintaax.tutorialisland.engine.modules.Controller;
import sintaax.tutorialisland.engine.objects.*;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import utilities.VarpbitSpy;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Manager extends Context<ClientContext> {
    public Manager(ClientContext ctx) {
        super(ctx);
    }

    public static int activeQuest = 0;

    public static TaskHandler th = new TaskHandler();
    private VarpbitSpy vbs = new VarpbitSpy(ctx);

    public void execute() {
        vbs.pollChanges();
        refresh();
        //Condition.sleep(Random.nextInt(1024, 2048));
    }

    private void refresh() {
        updateTasks(Arrays.asList(new Controller(ctx)));
    }

    public static void updateTasks(List<Task> tasks) {
        th.reset();
        th.tasks.addAll(tasks);
        th.execute();
    }

    public static boolean inactive(ClientContext ctx) {
        return ctx.players.local().animation() == -1 && !ctx.players.local().inMotion()
                && !ctx.chat.chatting() && !ctx.chat.pendingInput();
    }
}
