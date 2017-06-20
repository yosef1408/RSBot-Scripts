package powerkyle628.scripts;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import z.Con;


import javax.swing.*;
import java.util.Hashtable;
import java.util.concurrent.Callable;


@Script.Manifest(name="Log Incinerator",description="Burns all types of logs",
        properties="author=powerkyle628; topic=1334429-freeos-log-incinerator; client=4;")


public class LogIncinerator extends PollingScript<ClientContext> {

    final static Hashtable<String, Integer> logLookup = new Hashtable();

    final static int normaLogId = 1511;
    final static int willowLogId = 1519;
    final static int yewLogId = 1515;
    final static int oakLogId = 1521;
    final static int mapleLogId = 1517;
    final static int mahoganyLogId = 6332;
    final static int teakLogId = 6333;
    final static int magicLogId = 1513;

    final static int tinder_id = 590;

    final static int arbitraryLargeNumber = 999;

    static Tile startTiles[] = new Tile[4];

    static Area bankArea;

    static int logChoiceID;


    @Override
    public void start() {
        populateStartTiles();

        bankArea = new Area(new Tile(3179, 3447), new Tile(3191,3433));

        populateLogLookup();

        String userOptions[] = {"Normal","Willow","Yew","Oak","Maple","Mahogany","Teak"};
        String userChoice = (String) JOptionPane.showInputDialog(null,
                "select log type",
                "firemaker",
                JOptionPane.PLAIN_MESSAGE,
                null,userOptions,
                userOptions[0]);

        logChoiceID = logLookup.get(userChoice);

        System.out.println("started");
    }

    @Override
    public void stop() {
        System.out.println("stopped");
        ctx.controller.stop();
    }
    @Override
    public void poll() {
        if (getLogCount() > 0 && !inBank()) {
            burn();
        } else if (!inBank() && getLogCount() == 0){
            walkToBank();
        } else if (inBank() && getLogCount() > 0) {
            leaveBank();
        } else if (inBank() && getLogCount() == 0){
            bank();
        }

    }

    public void populateStartTiles() {
        int j = 0;
        for (int i = 28; i <= 31; i++) {
            startTiles[j] = new Tile(3183, 3400 + i);
            j += 1;
        }
        for (Tile t : startTiles) {
            System.out.println(t.toString());
        }
    }

    public void populateLogLookup() {
        logLookup.put("Normal", normaLogId);
        logLookup.put("Oak", oakLogId);
        logLookup.put("Willow", willowLogId);
        logLookup.put("Yew", yewLogId);
        logLookup.put("Magic", magicLogId);
        logLookup.put("Maple", mapleLogId);
        logLookup.put("Mahogany", mahoganyLogId);
        logLookup.put("Teak", teakLogId);
    }

    public int getLogCount() {
        return ctx.inventory.select().id(logChoiceID).count();
    }

    public void leaveBank() {
        ctx.movement.step(startTiles[1]);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !inBank() && !ctx.players.local().inMotion();
            }
        });
    }

    public boolean inBank() {
        return bankArea.contains(ctx.players.local());
    }


    public void burn() {
        //how many logs we start with could be useful
        int ogLogAmount = ctx.inventory.select().id(logChoiceID).count();

        Item tinder = ctx.inventory.select().id(tinder_id).poll();
        Item log = ctx.inventory.select().id(logChoiceID).poll();

        log.interact("Use");
        Condition.sleep(100);
        tinder.interact(("Use"));
        Condition.sleep(700);
        ctx.inventory.select().id(logChoiceID).peek().hover();

        //wait for animation to finish before doing anything else
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() == -1 && !ctx.players.local().inMotion();
            }
        });
        //check to see if we successfully burned anything
        int newLogAmount = ctx.inventory.select().id(logChoiceID).count();
        if (ogLogAmount == newLogAmount) {
            //must have been standing on something, find a new place to continue
            Tile bestOption = calcBestNewStartingSpot();
            ctx.movement.step(bestOption);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.players.local().inMotion();
                }
            });
        }
    }

    public Tile calcBestNewStartingSpot() {
        Tile bestOption = startTiles[0];
        int num_fires = arbitraryLargeNumber;
        for (int t = 0; t < startTiles.length; t++) {
            final int finalT = t;
            int curr_num_fires = ctx.objects.select().select(new Filter<GameObject>() { @Override public boolean accept(GameObject go) {
                if (go.tile().y() == startTiles[finalT].y() && go.name().equals("Fire")) {
                    return true;
                } return false;
            } }).size();
            if (curr_num_fires < num_fires) {
                num_fires = curr_num_fires;
                bestOption = startTiles[t].derive(-1*curr_num_fires, 0);
            }
        }
        return bestOption;
    }

    public void walkToBank() {
        System.out.println("walking");
        final Locatable nearestBank = ctx.bank.nearest();
        ctx.movement.step(nearestBank);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !ctx.players.local().inMotion();
            }
        });
    }

    public void bank() {
        System.out.println("banking");
        ctx.bank.open();
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.bank.opened();
            }
        });

        ctx.bank.depositAllExcept(tinder_id);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.count() == 1;
            }
        });

        boolean successfulWithdraw = ctx.bank.withdraw(logChoiceID, 27);
        if (!successfulWithdraw) {
            boolean tryAgain = ctx.bank.withdraw(logChoiceID, 27);
            if (!tryAgain) {
                System.out.println("failed withdraw twice");
                stop();
            }
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.inventory.count() > 1;
            }
        });
        ctx.bank.close();
    }
}
