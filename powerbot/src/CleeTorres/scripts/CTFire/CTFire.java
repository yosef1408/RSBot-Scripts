package scripts.CTFire;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import sun.rmi.runtime.Log;


import javax.swing.*;
import java.awt.*;

import java.util.concurrent.Callable;

@Script.Manifest(
        name = "CTFire",
        properties = "author=CleeTorres; topic=1330365; client=4;",
        description = "Firemaking, bro" )

public class CTFire extends PollingScript<ClientContext> implements PaintListener, MessageListener{


    private String location;

    private static Tile varrockEastStart = new Tile(3262,3429,0);
    private static Tile varrockEastBank = new Tile(3252,3421,0);


    private static Tile varrockWestStart = new Tile(3197,3430,0);
    private static Tile varrockWestBank = new Tile(3182, 3433, 0);

    
    public enum LogType {
        REGULAR(1511, 40), ACHEY(2862, 40),OAK(1521, 60),
        WILLOW(1519, 90), TEAK(6333,105), ARCTIC(10810,125),
        MAPLE(1517, 135), MAHOGANY(6332, 157), YEW(1515, 202.5),
        MAGIC(1513, 303.8), REDWOOD(19669, 350);

        private int value;
        private double xp;

        private LogType(int value, double xp) {
            this.value = value;
            this.xp = xp;
        }

        public int getValue() {
            return this.value;
        }

        public double getXp() {
            return this.xp;
        }
    }

    private volatile boolean firstDone = false;
    private enum State{WALK,BANK,MAKE,QUIT;}
    private int lastY = -1;
    private int totalfires = 0;
    private static int TINDERBOX = 590;
    private static int FIRE = 2260;
    private LogType currentLog;
    private volatile State state;

    private Random rand;
    @Override
    public void start(){
        rand = new Random();
        state = State.BANK;
        currentLog = promptUser();
    }

    @Override
    public void poll(){
        switch (state){
            case BANK:
                bank();
                break;

            case WALK:
                walk();
                break;

            case MAKE:
                make();
                break;
        }
    }

    private Tile getRandomTile(Tile start, int variationX, int variationY){
        return new Tile(start.x() + rand.nextInt((0-variationX), variationX), start.y() + rand.nextInt((0-variationY),variationY));
    }


    private void bank(){
        log.info("Banking");
        if (ctx.inventory.select().id(currentLog.getValue()).count() <= 0){

            walkBank();
            //final int tmp = rand.nextInt(0, 5);
            //ctx.movement.findPath(nearBank[tmp]).traverse();
            Condition.sleep(rand.nextInt(600,1300));
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return (!ctx.players.local().inMotion());
                }
            }, 300, 20);

            if(!ctx.bank.inViewport()){
                ctx.camera.turnTo(ctx.bank.nearest().tile());
                ctx.bank.open();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return (ctx.bank.opened());
                    }
                }, 300, 5);


            }else{
                ctx.bank.open();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return (ctx.bank.opened());
                    }
                }, 300, 5);
            }

            if(!ctx.bank.opened()){
                ctx.movement.step(ctx.bank.nearest());
                Condition.sleep(rand.nextInt(200,400));
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return (!ctx.players.local().inMotion());
                    }
                }, 300, 5);

                ctx.bank.open();
            }
            getLogs();

        }
        else
            state = State.WALK;

        //Just in case
        firstDone = false;
    }
    private void walk(){

        if(location.equals("Varrock West")){
            Tile t = getRandomTile(varrockWestStart,3,2);

            while(t.y() == lastY)
                t = getRandomTile(varrockWestStart,3,2);

            lastY = t.y();
            ctx.movement.findPath(t).traverse();
        }else if(location.equals("Varrock East")){
            Tile t = getRandomTile(varrockEastStart,3,1);

            while(t.y() == lastY)
                t = getRandomTile(varrockEastStart,3,1);

            lastY = t.y();
            ctx.movement.findPath(t).traverse();
        }


        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return (!ctx.players.local().inMotion());
            }
        }, 600, 10);
        log.info("setting state to make");
        state = State.MAKE;
    }

    private void walkBank(){
      if(location.equals("Varrock West")){
          ctx.movement.findPath(getRandomTile(varrockWestBank, 2, 2)).traverse();
      }else if(location.equals("Varrock East")){
          ctx.movement.findPath(getRandomTile(varrockEastBank, 2, 2)).traverse();
      }
    }



    /*Changed condition wait to include state.Walk*/
    private void make(){
        if(ctx.inventory.select().id(currentLog.getValue()).count() > 0 && !firstDone)
        {
            while(!ctx.inventory.selectedItem().valid())
                ctx.inventory.id(currentLog.getValue()).peek().interact("Use");

            Condition.sleep(rand.nextInt(100,300));

            ctx.inventory.select().id(TINDERBOX).peek().interact("Use");

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return (firstDone || state.equals(State.WALK));
                }
            }, 300, 20);

            firstDone = false;
        }
        else if(ctx.inventory.id(currentLog.getValue()).count() <= 0)
            state = State.BANK;
}

    @Override
    public void repaint(Graphics g){
        Graphics2D graphics = (Graphics2D) g;
        graphics.setFont(new Font("Arial", Font.BOLD, 10));
        graphics.setColor(Color.BLACK);
        graphics.fillRect(5, 5, 170, 80);
        graphics.setColor(Color.BLUE);
        long logCount = ((totalfires * 3600000) / getRuntime());
        String xpGainsPerHour = "XP/HR: " + (long)(logCount * currentLog.getXp());
        String logsHr = "LOGS/HR: " + logCount;

        String runTime = "TIME: " +
                ((getRuntime() / (1000*60*60)) % 24) + ":" +
                ((getRuntime() / (1000*60)) % 60) + ":" +
                (getRuntime() / 1000) % 60 ;
        String xpGained = "TOTAL XP GAINED: " + totalfires*currentLog.getXp();
        graphics.drawString(xpGainsPerHour, 10, 20);
        graphics.drawString(logsHr, 10, 40);
        graphics.drawString(runTime, 10, 60);
        graphics.drawString(xpGained, 10, 80);
    }

    @Override
    public void messaged(MessageEvent evt){
        if(evt.text().toLowerCase().equals("you can't light a fire here.")){
            state = State.WALK;
        }else if (evt.text().toLowerCase().equals("the fire catches and the logs begin to burn.")){
            firstDone = true;
            totalfires++;
        }
    }

    private LogType promptUser() {
        String locations[] = {
                "Varrock West",
                "Varrock East"

        };
        String location = (String) JOptionPane.showInputDialog(
                null,
                "make your choice",
                "Try GUI",
                JOptionPane.PLAIN_MESSAGE,
                null,
                locations,
                locations[0]);
        this.location = location;
        String choices[] = {LogType.REGULAR.toString(),
                LogType.ACHEY.toString(),
                LogType.OAK.toString(),
                LogType.WILLOW.toString(),
                LogType.TEAK.toString(),
                LogType.ARCTIC.toString(),
                LogType.MAPLE.toString(),
                LogType.MAHOGANY.toString(),
                LogType.YEW.toString(),
                LogType.MAGIC.toString(),
                LogType.REDWOOD.toString()};
        String requested = (String) JOptionPane.showInputDialog(
                null,
                "make your choice",
                "Try GUI",
                JOptionPane.PLAIN_MESSAGE,
                null,
                choices,
                choices[0]);

        if(requested.toLowerCase().equals("regular"))
            return LogType.REGULAR;
        if(requested.toLowerCase().equals("oak"))
            return LogType.OAK;
        if(requested.toLowerCase().equals("willow"))
            return LogType.WILLOW;
        if(requested.toLowerCase().equals("maple"))
            return LogType.MAPLE;
        if(requested.toLowerCase().equals("yew"))
            return LogType.YEW;
        if(requested.toLowerCase().equals("magic"))
            return LogType.MAGIC;
        if(requested.toLowerCase().equals("redwood"))
            return LogType.REDWOOD;
        else
            return LogType.REGULAR;
    }

    private void getLogs() {

        if (ctx.bank.opened()) {
            if (ctx.bank.select().id(currentLog.getValue()).count() <= 0)
                state = State.QUIT;
            else if (ctx.inventory.select().id(currentLog.getValue()).count() <= 0){
                ctx.bank.select().id(currentLog.getValue()).peek().interact("Withdraw-All");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return (ctx.inventory.count() > 1);
                    }
                }, 300, 5);
            }

            ctx.bank.close();
        }

    }

}
