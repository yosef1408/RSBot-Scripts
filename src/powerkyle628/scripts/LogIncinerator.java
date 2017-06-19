package powerkyle628.scripts;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;


import javax.swing.*;
import java.util.Hashtable;
import java.util.concurrent.Callable;


@Script.Manifest(name="Log Incinerator",description="Burns all types of logs",properties="author=powerkyle628; topic=999; client=4;")


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
    final static int elderLogId = 29556;

    final static int tinder_id = 590;

    static int logChoiceID;



    @Override
    public void start() {
        populateLogLookup();

        String userOptions[] = {"Normal","Willow","Yew","Oak","Maple","Mahogany","Teak","Elder"};
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
    }
    @Override
    public void poll() {
        if (getLogCount() > 0 && !inBank()) {
            burn();
        } else if (!inBank()){
            walkToBank();

        } else if (inBank() && getLogCount() > 0) {
            leaveBank();
        } else {
            bank();
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
        logLookup.put("Elder", elderLogId);
    }

    public int getLogCount() {
        return ctx.inventory.select().id(logChoiceID).count();
    }

    public void leaveBank() {
        Tile outsideOfBank = ctx.players.local().tile().derive(0, -5,0);
        ctx.movement.step(outsideOfBank);
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !inBank();
            }
        });
    }

    public boolean inBank() {
        Area bankArea = new Area(new Tile(3179, 3447), new Tile(3191,3433));
        return bankArea.contains(ctx.players.local());
    }


    public void burn() {
        //how many logs we start with could be useful
        int ogLogAmount = ctx.inventory.select().id(logChoiceID).count();

        Item tinder = ctx.inventory.select().id(tinder_id).poll();
        Item log = ctx.inventory.select().id(logChoiceID).poll();

        log.interact("Use");
        tinder.interact(("Use"));

        //wait for animation to finish before doing anything else
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() == -1;
            }
        }); // hacky sol'n to problem where animation was stopping before it was really done
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().animation() == -1;
            }
        });
        //check to see if we successfully burned anything
        int newLogAmount = ctx.inventory.select().id(logChoiceID).count();
        if (ogLogAmount == newLogAmount) {
            //must have been standing on something, move somewhere random nearby
            int randomX = org.powerbot.script.Random.nextInt(0,5);
            int randomY = org.powerbot.script.Random.nextInt(-2, 2);
            Tile randomTile = ctx.players.local().tile().derive(randomX,randomY);
            ctx.movement.step(randomTile);
        }


    }

    public void walkToBank() {
        System.out.println("walking");
        final Locatable nearestBank = ctx.bank.nearest();
        ctx.movement.step(nearestBank);
    }

    public void bank() {
        System.out.println("banking");
        ctx.bank.open();
        ctx.bank.depositAllExcept(tinder_id);
        ctx.bank.withdraw(logChoiceID, 27);
        ctx.bank.close();
    }
}
