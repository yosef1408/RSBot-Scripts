package supaplex.util;

import supaplex.exceptions.QueueEmptyException;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Andreas on 20.07.2016.
 */

/**
 * Singleton task handler, ensures only one queue present during the script execution
 */
public class TaskHandler {
    private static TaskHandler instance = null;

    private Queue q = new LinkedList();

    protected TaskHandler() {
        // Exists only to defeat instantiation.
    }

    public static TaskHandler getInstance() {
        if (instance == null) {
            instance = new TaskHandler();
        }
        return instance;
    }

    /**
     * Adds a task to the queue
     *
     * @param task
     */
    public void addTask(Object task) {
        q.add(task);
    }

    /**
     * Removes a task from the queue
     */
    public void removeTask() {
        q.remove();
    }

    /**
     * Retrieves the task queue
     *
     * @return the task queue
     */
    public Queue getQueue() {
        return q;
    }

    /**
     * Gets the queue's head task, and examines it's properties. Global variables are set to be used during the
     * execution of the script. Throws exception if the queue is empty
     *
     * @throws QueueEmptyException
     */
    public void checkTask() throws QueueEmptyException {
        String task = (String) q.poll();
        if (task == null) {
            throw new QueueEmptyException("The queue is empty, aborting...");
        }
        GlobalVariables.taskFinished = false;
        String[] parts = task.split("\\-");

        String actionType = parts[0].substring(0, parts[0].length() - 1);
        String bowType = parts[1].substring(1);

        if ("Cut bows".equals(actionType)) {
            GlobalVariables.taskType = TaskType.CUT_BOWS;
        } else {
            GlobalVariables.taskType = TaskType.STRING_BOWS;
        }

        switch (bowType) {
            case "Shortbow":
                GlobalVariables.logId = Constants.LOGS;
                GlobalVariables.unfBowId = Constants.SHORTBOW_UNF;
                GlobalVariables.widgetId = Constants.SHORTBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.SHORTBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.SHORTBOW;
                GlobalVariables.minimumLevel = 5;
                break;
            case "Longbow":
                GlobalVariables.logId = Constants.LOGS;
                GlobalVariables.unfBowId = Constants.LONGBOW_UNF;
                GlobalVariables.widgetId = Constants.LONGBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.LONGBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.LONGBOW;
                GlobalVariables.minimumLevel = 10;
                break;
            case "Oak Shortbow":
                GlobalVariables.logId = Constants.OAK_LOGS;
                GlobalVariables.unfBowId = Constants.OAK_SHORTBOW_UNF;
                GlobalVariables.widgetId = Constants.OAK_SHORTBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.OAK_SHORTBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.OAK_SHORTBOW;
                GlobalVariables.minimumLevel = 20;
                break;
            case "Oak Longbow":
                GlobalVariables.logId = Constants.OAK_LOGS;
                GlobalVariables.unfBowId = Constants.OAK_LONGBOW_UNF;
                GlobalVariables.widgetId = Constants.OAK_LONGBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.OAK_LONGBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.OAK_LONGBOW;
                GlobalVariables.minimumLevel = 25;
                break;
            case "Willow Shortbow":
                GlobalVariables.logId = Constants.WILLOW_LOGS;
                GlobalVariables.unfBowId = Constants.WILLOW_SHORTBOW_UNF;
                GlobalVariables.widgetId = Constants.WILLOW_SHORTBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.WILLOW_SHORTBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.WILLOW_SHORTBOW;
                GlobalVariables.minimumLevel = 35;
                break;
            case "Willow Longbow":
                GlobalVariables.logId = Constants.WILLOW_LOGS;
                GlobalVariables.unfBowId = Constants.WILLOW_LONGBOW_UNF;
                GlobalVariables.widgetId = Constants.WILLOW_LONGBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.WILLOW_LONGBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.WILLOW_LONGBOW;
                GlobalVariables.minimumLevel = 40;
                break;
            case "Maple Shortbow":
                GlobalVariables.logId = Constants.MAPLE_LOGS;
                GlobalVariables.unfBowId = Constants.MAPLE_SHORTBOW_UNF;
                GlobalVariables.widgetId = Constants.MAPLE_SHORTBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.MAPLE_SHORTBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.MAPLE_SHORTBOW;
                GlobalVariables.minimumLevel = 50;
                break;
            case "Maple Longbow":
                GlobalVariables.logId = Constants.MAPLE_LOGS;
                GlobalVariables.unfBowId = Constants.MAPLE_LONGBOW_UNF;
                GlobalVariables.widgetId = Constants.MAPLE_LONGBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.MAPLE_LONGBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.MAPLE_LONGBOW;
                GlobalVariables.minimumLevel = 55;
                break;
            case "Yew Shortbow":
                GlobalVariables.logId = Constants.YEW_LOGS;
                GlobalVariables.unfBowId = Constants.YEW_SHORTBOW_UNF;
                GlobalVariables.widgetId = Constants.YEW_SHORTBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.YEW_SHORTBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.YEW_SHORTBOW;
                GlobalVariables.minimumLevel = 65;
                break;
            case "Yew Longbow":
                GlobalVariables.logId = Constants.YEW_LOGS;
                GlobalVariables.unfBowId = Constants.YEW_LONGBOW_UNF;
                GlobalVariables.widgetId = Constants.YEW_LONGBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.YEW_LONGBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.YEW_LONGBOW;
                GlobalVariables.minimumLevel = 70;
                break;
            case "Magic Shortbow":
                GlobalVariables.logId = Constants.MAGIC_LOGS;
                GlobalVariables.unfBowId = Constants.MAGIC_SHORTBOW_UNF;
                GlobalVariables.widgetId = Constants.MAGIC_SHORTBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.MAGIC_SHORTBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.MAGIC_SHORTBOW;
                GlobalVariables.minimumLevel = 80;
                break;
            case "Magic Longbow":
                GlobalVariables.logId = Constants.MAGIC_LOGS;
                GlobalVariables.unfBowId = Constants.MAGIC_LONGBOW_UNF;
                GlobalVariables.widgetId = Constants.MAGIC_LONGBOW_UNF_WIDGET;
                GlobalVariables.componentId = Constants.MAGIC_LONGBOW_UNF_COMPONENT;
                GlobalVariables.bowId = Constants.MAGIC_LONGBOW;
                GlobalVariables.minimumLevel = 85;
                break;
        }
    }

}