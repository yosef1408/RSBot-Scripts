package sintaax.tutorialisland.engine.handlers;

import sintaax.tutorialisland.engine.objects.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskHandler {
    public List<Task> tasks = new ArrayList<Task>();

    public void execute() {
        for (Task task : tasks) {
            if (task.activate()) {
                task.execute();
                System.out.println(task);
            }
        }
    }

    public void reset() {
        tasks = new ArrayList<>();
    }
}
