package thebonobo.SalmonCollector.utils;

import org.powerbot.script.rt4.GeItem;

/**
 * Created with IntelliJ IDEA
 * User: thebonobo
 * Date: 13/09/17
 */

public class Info {

    private static Info currentInfo = new Info();

    private int salmonPrice;
    private int rawSalmonPrice;
    private int salmonCollected;
    private int rawSalmonCollected;

    public int getSessionSalmonCollected() {
        return sessionSalmonCollected;
    }

    public void setSessionSalmonCollected(int sessionSalmonCollected) {
        this.sessionSalmonCollected = sessionSalmonCollected;
    }

    public int getSessionRawSalmonCollected() {
        return sessionRawSalmonCollected;
    }

    public void setSessionRawSalmonCollected(int sessionRawSalmonCollected) {
        this.sessionRawSalmonCollected = sessionRawSalmonCollected;
    }

    private int sessionSalmonCollected;
    private int sessionRawSalmonCollected;
    private String currentTask;
    private long startTime;

    public Info() {
        this.salmonCollected = 0;
        this.currentTask = "";

        this.salmonPrice = new GeItem(329).price;
        this.rawSalmonPrice = new GeItem(331).price;
    }

    public static Info getInstance() {
        return currentInfo;
    }

    public long getStartTime() {return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getSalmonPrice() {
        return salmonPrice;
    }

    public int getRawSalmonPrice() {
        return rawSalmonPrice;
    }

    public int getSalmonCollected() {
        return salmonCollected;
    }

    public int getRawSalmonCollected() {
        return rawSalmonCollected;
    }

    public void clearSalmonCollected() {
        salmonCollected = 0;
        rawSalmonCollected = 0;
    }

    public String getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(String task) {
        currentTask = task;
    }

    public void incrementSalmon(int amount) {
        salmonCollected+= amount;
        sessionSalmonCollected+=amount;
    }

    public void incrementRawSalmon(int amount) {
        rawSalmonCollected+= amount;
        sessionRawSalmonCollected+=amount;
    }
}