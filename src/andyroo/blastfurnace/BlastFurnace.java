package andyroo.blastfurnace;

import andyroo.blastfurnace.ui.BlastFurnaceForm;
import andyroo.util.Antiban;
import org.powerbot.bot.Con;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;


@Script.Manifest(
        name = "Blast Furnace", properties = "author=andyroo; topic=1299183; client=4;",
        description = "v1.4c - Blast furnace (Steel, Mithril, Adamantite only)"
)

/**
 * Changelog
 *
 * v 1.4c
 * fixed attempting to pay foreman when lvl 60+
 * fixed a bug while collecting that should be handled by idle timer
 * added a check for coins subtracted to confirm payment
 * now checks the exact amount of bars collected (assumed full load before)
 *
 */

public class BlastFurnace extends PollingScript<ClientContext> implements PaintListener {

    /********************************
     * CONSTANTS
     ********************************/

    private enum State {
        RUN_TOGGLE, WITHDRAW, DEPOSIT, PUT_ORE, COLLECT, INVALID, PAY_FOREMAN
    }

    public enum BAR {
        STEEL, MITHRIL, ADAMANTITE, RUNITE
    }

    public enum ORE {
        IRON(440),
        COAL(453),
        MITHRIL(447),
        ADAMANTITE(449),
        RUNITE(451);

        private final int id;

        ORE(int id) {
            this.id = id;
        }

        public int getID() {
            return id;
        }
    }

    private static final int[] ORE_IDs = {
            440, // iron
            453, // coal
            447, // mithril
            449, // adamantite
            451  // runite
    };

    private static final int[] BAR_IDs = {
            2353, // steel
            2359, // mithril
            2361  // adamantite
    };

    private static final int BANK_CHEST_ID = 26707;
    private static final Tile BANK_TILE = new Tile(1948, 4957, 0);
    private static final Area BANK_AREA = new Area(new Tile(1947, 4954, 0), new Tile(1950, 4958));

    private static final int BELT_ID = 9100;
    private static final Tile BELT_TILE = new Tile(1942, 4967, 0);
    private static final Area BELT_AREA = new Area(new Tile(1942, 4967, 0), new Tile(1937, 4968, 0));
    private static final int[] BELT_BOUNDS = {-24, 20, -248, -224, -24, 16};

    private static final int ADD_ORE_WIDGET = 219;
    private static final int ADD_ORE_COMPONENT = 0;

    private static final int BELT_BLOCKED_WIDGET = 229;
    private static final int BELT_BLOCKED_COMPONENT = 1;

    private static final int BAR_WIDGET = 28;
    private static final int BAR_CLOSE_COMPONENT = 118;

    private static final int DIALOGUE_WIDGET = 231;

    private static final int BAR_DISPENSER_ID = 9096;
    private static final Tile DISPENSER_TILE = new Tile(1940, 4964, 0);
    private static final Area DISPENSER_AREA = new Area(new Tile(1939, 4962, 0), new Tile(1942, 4965, 0));

    private static final int COIN_ID = 995;
    private static final int FOREMAN_ID = 2923;
    private static final int PAYMENT_COST = 2500;
    private static final int PAYMENT_WIDGET = 219;
    private static final int PAYMENT_COMPONENT = 0;
    private static final int PAYMENT_COMPONENT2 = 1;
    private static final long PAYMENT_INTERVAL = 10 * 60 * 1000; // 10 min

    private static final int WITHDRAW_FAIL_THRESHOLD = 5;
    private static final long IDLE_TIME_THRESHOLD = 1000 * 60 * 2;

    private static final int REPORT_WIDGET = 553;
    private static final int REPORT_COMPONENT = 1;
    private static final int REPORT_CLOSE_COMPONENT = 11;

    private static final int GUI_X = 250;
    private static final int GUI_Y = 339;
    private static final int GUI_WIDTH = 519;
    private static final int GUI_HEIGHT = 138;

    /********************************
     ********************************/

    private BlastFurnaceForm form;

    private long startTime;
    private int startXP;
    private int barsSmelted;
    private int paidCount;
    private static String version = "1.4c";

    private BarInfo barType;

    private int coalCount;
    private int primaryCount;
    private int barCount;
    private int expectedCoalCount;
    private int expectedPrimaryCount;
    private int expectedBarCount;

    private int primaryRemaining;
    private int coalRemaining;

    private int energyThreshold;
    private int fullLoad;

    private boolean paid; // dependable after first payment
    private Timer foremanTimer;
    private boolean foremanTimerRunning;

    private boolean waitingForBars;
    private Timer idleTimer;
    private int withdrawFailCount;

    private boolean scriptInit;

    /********************************
     ********************************/


    @Override
    public void repaint(Graphics graphics) {
        int x = GUI_X;
        int y = GUI_Y;
//        graphics.setColor(new Color(0, 0, 0));
//        graphics.fillRect(x, y, GUI_WIDTH - GUI_X, GUI_HEIGHT);
        graphics.setColor(new Color(0, 0, 0));
        x += 10;
        graphics.drawString("Version: " + version, x, y += 15);
        graphics.drawString(getTimeElapsed(System.currentTimeMillis() - startTime), x, y += 15);
        graphics.drawString("Total smelted: " + Integer.toString(barsSmelted), x, y += 15);
        graphics.drawString("Coal left: " + Integer.toString(coalRemaining), x, y += 15);
        graphics.drawString("Ores left: " + Integer.toString(primaryRemaining), x, y += 15);
        graphics.drawString("Coal in furnace (expected): " + Integer.toString(coalCount) + " (" + Integer.toString(expectedCoalCount) + ")", x, y += 15);
        graphics.drawString("Primary in furnace (expected): " + Integer.toString(primaryCount) + " (" + Integer.toString(expectedPrimaryCount) + ")", x, y += 15);
        graphics.drawString("Bars ready (expected): " + Integer.toString(barCount) + " (" + Integer.toString(expectedBarCount) + ")", x, y += 15);
    }

    private void initialize() {
        ctx.camera.pitch(true);
        ctx.game.tab(Game.Tab.INVENTORY);

        energyThreshold = Random.nextInt(30, 60);


        log.info(barType.toString());

        waitingForBars = false;
        updateFurnaceStatus();
        primaryRemaining = -1;
        coalRemaining = -1;
        expectedCoalCount = coalCount;
        expectedPrimaryCount = primaryCount;
        withdrawFailCount = 0;

        if(ctx.skills.level(Constants.SKILLS_SMITHING) < 60) {
            fullLoad = 27;
            foremanTimerRunning = false;
        }
        else fullLoad = 28;
        paid = true;

        scriptInit = true;
    }

    public void start() {
        form = new BlastFurnaceForm();
        form.setLocationRelativeTo(Frame.getFrames()[0]);

        startXP = ctx.skills.experience(Constants.SKILLS_SMITHING);
        startTime = System.currentTimeMillis();
        barsSmelted = 0;
        paidCount = 0;

        while (!form.start) {
            Condition.sleep(500);
        }
        barType = form.getBarType();

        if (barType == null
                || !(barType.getBarType() == BAR.MITHRIL
                || barType.getBarType() == BAR.ADAMANTITE
                || barType.getBarType() == BAR.RUNITE
                || barType.getBarType() == BAR.STEEL)) {
            log.info("Invalid bar type");
            log.info(barType.toString());
            ctx.controller.stop();
            return;
        }

        if(!ctx.game.loggedIn()) {
            scriptInit = false;
        }
    }

    public void stop() {
        ctx.game.tab(Game.Tab.INVENTORY);
        long stopTime = System.currentTimeMillis();
        int stopXP = ctx.skills.experience(Constants.SKILLS_SMITHING);

        String totalTime = getTimeElapsed(stopTime - startTime);

        log.info("---------------------");
        log.info("Version " + version);
        log.info("Time run: " + totalTime);
        log.info("Gained XP: " + Integer.toString(stopXP - startXP));
        log.info("Smelted " + barsSmelted + " bars");
        log.info("Paid " + paidCount + " times");

        //https://www.powerbot.org/community/topic/1228458-saving-script-screenshots/
        final int width = ctx.game.dimensions().width, height = ctx.game.dimensions().height;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        repaint(img.createGraphics());
        img = img.getSubimage(GUI_X, GUI_Y, GUI_WIDTH, GUI_HEIGHT);
        System.out.print(getStorageDirectory().toString());
        final File screenshot = new File(getStorageDirectory(), String.valueOf(System.currentTimeMillis()).concat(".png"));
        try {
            ImageIO.write(img, "png", screenshot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void poll() {
        updateFurnaceStatus();

        if (ctx.game.tab() != Game.Tab.INVENTORY)
            ctx.game.tab(Game.Tab.INVENTORY);

        if (!handleDialogue()) {
            log.info("Failed to handle dialogue");
            return;
        }

        handleReportWidget();

        State s = state();

        switch (s) {
            case PAY_FOREMAN: {
                if(payForeman()) {
                    log.info("Paid foreman");
                    paidCount++;
                    paid = true;
                    foremanTimer = new Timer();
                    foremanTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            log.info("10 min timer for foreman");
                            foremanTimerRunning = false;
                            paid = false;
                        }
                    }, PAYMENT_INTERVAL);
                    foremanTimerRunning = true;
                }
            }
            break;

            case WITHDRAW: {
                if (openBank()) {
                    log.info("Opened bank");
                    if (withdrawOres() < fullLoad) {
                        if (primaryRemaining < fullLoad || coalRemaining < fullLoad) {    // change this for bronze/gold/silver/iron
                            log.info("Expected " + fullLoad + " ores, ran out of ores");
                            withdrawFailCount++;
                            log.info("Attempted " + withdrawFailCount + " times");
                            if(withdrawFailCount >= WITHDRAW_FAIL_THRESHOLD)
                                ctx.controller.stop();
                        }
                    }
                    else {
                        withdrawFailCount = 0;
                    }
                }
            }
            break;

            case COLLECT: {
                moveToDispenser();
                int numCollected = collectBars();
                if (numCollected > 0) {
                    log.info("Took bars");

                    if (waitingForBars)
                        resetIdleTimer();
                    barsSmelted += numCollected;
                    expectedCoalCount = coalCount();
                    expectedPrimaryCount = primaryCount();
                    expectedBarCount = barCount();
                }
            }
            break;

            case PUT_ORE: {
                String recentOre = ctx.inventory.select().id(ORE_IDs).poll().name();
                if (moveToConveyorBelt()) {
                    if (putOres()) {
                        if (recentOre.compareTo("Coal") == 0)
                            expectedCoalCount += fullLoad;
                        else if (recentOre.compareTo("Iron ore") == 0 ||
                                recentOre.compareTo("Mithril ore") == 0 ||
                                recentOre.compareTo("Adamantite ore") == 0 ||
                                recentOre.compareTo("Runite ore") == 0)
                            expectedPrimaryCount += fullLoad;
                    }
                }
            }
            break;

            case DEPOSIT: {
                if (openBank()) {
                    log.info("Opened bank");
                    ctx.bank.depositAllExcept(COIN_ID);

                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().count() <= 1; //
                        }
                    }, 250, 4);
                }
            }
            break;

            case RUN_TOGGLE: {
                ctx.movement.running(true);
                energyThreshold = Random.nextInt(30, 60);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.movement.running();
                    }
                }, 250, 4);
            }
            break;

            default: {
                log.info("huh");
                Condition.sleep(500);
            }
            break;
        }
    }

    private State state() {

        if(!scriptInit && ctx.game.loggedIn()) {
            initialize();
        }

        if (form.isVisible()) {
            log.info("----INVALID----");
            return State.INVALID;
        }

        if (!ctx.movement.running() && (ctx.movement.energyLevel() > energyThreshold)) {
            log.info("Toggle run");
            return State.RUN_TOGGLE;
        }

        if (barCount > 28 - ctx.inventory.select().count() ||
                expectedBarCount > 28 - ctx.inventory.select().count() ||
                ctx.inventory.select().id(BAR_IDs).count() > 0) {
            log.info("----DEPOSIT----");
            return State.DEPOSIT;
        }

        if (expectedBarCount > 0 || barCount > 0) {
            log.info("----COLLECT----");
            return State.COLLECT;
        }

        if(!paid) {
            log.info("----PAY_FOREMAN----");
            return State.PAY_FOREMAN;
        }

        if (ctx.inventory.select().id(ORE_IDs).count() < fullLoad &&
                (expectedCoalCount < fullLoad * barType.getRatio() || expectedPrimaryCount < fullLoad)) {
            log.info("----WITHDRAW----");
            return State.WITHDRAW;
        }

        if (ctx.inventory.select().id(ORE_IDs).count() >= fullLoad) {
            log.info("----PUT_ORE----");
            return State.PUT_ORE;
        }

        return State.INVALID;
    }

    /**
     * Update coal count, primary ore count, and bar count using game data
     * Update expected bar count based on expected ore counts
     * Adjust expected counts if less than the official amount
     */
    private void updateFurnaceStatus() {
        coalCount = coalCount();
        primaryCount = primaryCount();
        barCount = barCount();

        if (coalCount > expectedCoalCount)
            expectedCoalCount = coalCount;
        if (primaryCount > expectedPrimaryCount)
            expectedPrimaryCount = primaryCount;

        if (expectedCoalCount >= expectedPrimaryCount * barType.getRatio())
            expectedBarCount = expectedPrimaryCount;
    }

    private int coalCount() {
        return ctx.varpbits.varpbit(BarInfo.ORE_INFO_VARPBIT1) & 0xFF; // 0xFF is mask for coal
    }

    private int primaryCount() {
        return (ctx.varpbits.varpbit(barType.getOreVarpbit()) & barType.getPrimaryMask()) >> barType.getPrimaryShift();
    }

    private int barCount() {
        return (ctx.varpbits.varpbit(barType.getBarVarpbit()) & barType.getPrimaryMask()) >> barType.getPrimaryShift();
    }


    /**
     * Moves to bank chest if not inViewport and clicks on it
     *
     * @return true if bank is open
     */
    private boolean openBank() {
        if (ctx.bank.opened())
            return true;

        GameObject bankChest = ctx.objects.select(15).id(BANK_CHEST_ID).poll();

        if (bankChest.inViewport()) {
            if (bankChest.click("Use", Game.Crosshair.ACTION)) {
                return Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.bank.opened();
                    }
                }, 250, 8);
            }
        } else {
            log.info("Bank chest not in view");
            moveToBank();
        }
        return false;
    }


    /**
     * Uses minimap to move to bank chest if it is not in viewport
     * clicks on random tile in BANK_AREA
     *
     * @return true if bank chest is in viewport
     */
    private boolean moveToBank() {
        if ((BANK_TILE.distanceTo(ctx.players.local()) > 5 || !BANK_TILE.matrix(ctx).inViewport()) && !ctx.players.local().inMotion()) {
            ctx.camera.pitch(true);
            ctx.movement.step(BANK_AREA.getRandomTile());
            if(Random.nextInt(0, 2) == 0)
                ctx.camera.turnTo(BANK_AREA.getRandomTile(), 20);
            log.info("move to bank");
        }

        return Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return BANK_TILE.matrix(ctx).inViewport();
            }
        }, 250, 4);
    }


    /**
     * Deposits everything in inventory
     * Finds number of ores remaining in bank
     * Withdraws coal and primary ore
     *
     * @return number of ores withdrawn, -1 if failed
     */
    private int withdrawOres() {
        if (ctx.inventory.select().count() > 0) {
            ctx.bank.depositAllExcept(COIN_ID);
            if (!Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.count() <= 1;
                }
            }, 250, 4)) // better: check if remaining item is coin
                return -1;
        }

        primaryRemaining = ctx.bank.select().id(barType.getPrimary().getID()).poll().stackSize();
        coalRemaining = ctx.bank.select().id(ORE.COAL.getID()).poll().stackSize();

        if (expectedCoalCount < fullLoad * barType.getRatio()) {
            ctx.bank.withdraw(ORE.COAL.getID(), Bank.Amount.ALL); // withdraw coal
            log.info("Withdraw coal");
            if (Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(ORE.COAL.getID()).count() > 0;
                }
            }, 250, 4)) {
                return ctx.inventory.select().id(ORE.COAL.getID()).count();
            } else return -1;

        } else {
            ctx.bank.withdraw(barType.getPrimary().getID(), Bank.Amount.ALL); // withdraw primary
            log.info("Withdraw primary");
            if (Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(barType.getPrimary().getID()).count() > 0;
                }
            }, 250, 4)) {
                return ctx.inventory.select().id(barType.getPrimary().getID()).count();
            } else return -1;

        }
    }


    /**
     * Walks to belt
     * uses minimap if distance > 5
     * else clicks tile
     *
     * @return true if belt is in viewport
     */
    private boolean moveToConveyorBelt() {
        if (BELT_TILE.distanceTo(ctx.players.local()) > 5) {
            if (!ctx.players.local().inMotion()) {
                log.info("Walk to conveyor belt");
                ctx.movement.step(BELT_AREA.getRandomTile());
                if(Random.nextInt(0, 2) == 0) {
                    ctx.camera.turnTo(BELT_TILE, 10);
                }
            }

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return BELT_TILE.distanceTo(ctx.players.local()) <= 5;
                }
            }, 250, 8);

            log.info("debug line 592");
            return false;
        } else if (ctx.objects.select(6).id(BELT_ID).viewable().peek().valid()) {
            return true;
        } else {
            ctx.camera.pitch(true);
            ctx.camera.turnTo(BELT_TILE, 10);
            BELT_AREA.getRandomTile().matrix(ctx).click("Walk here");
            return false;
        }
    }

    /**
     * Checks for ore depositing widget and confirms
     *
     * waits until player is not moving
     *
     * @return true if inventory is empty and widget is not visible
     */
    private boolean putOres() {
        final Component putOreWidget = ctx.widgets.component(ADD_ORE_WIDGET, ADD_ORE_COMPONENT).component(1);

        if(ctx.widgets.component(BELT_BLOCKED_WIDGET, BELT_BLOCKED_COMPONENT).visible()) {
            if(ctx.skills.level(Constants.SKILLS_SMITHING) < 60 && foremanTimerRunning == false) {
                paid = false;
            }
            else {
                log.info("Furnace full; take bars");
                expectedPrimaryCount = fullLoad;
            }
        }
        else if (!putOreWidget.valid()) {
            if (Condition.wait(new Callable<Boolean>() { // wait until walking
                @Override
                public Boolean call() throws Exception {
                    return !ctx.players.local().inMotion();
                }
            }, 250, 4)) { // if not walking
                // fails if a random is on top of conveyor
                ctx.objects.peek().bounds(BELT_BOUNDS);
                if (ctx.objects.peek().click("Put-ore-on", Game.Crosshair.ACTION) || ctx.objects.peek().interact(false, "Put-ore-on")) {
                    log.info("Click conveyor belt");
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return putOreWidget.valid();
                        }
                    }, 250, 10); // debug here
                } else {
                    ctx.camera.turnTo(ctx.objects.peek(), 20); // redundant?
                    log.info("No conveyor belt");
                }
            }
        }

        else {
            log.info("Add ore to furnace");

            if (Random.nextInt(0, 5) == 0) // 20% chance to mouse click
                putOreWidget.click();
            else
                ctx.input.send("1");

            return Condition.wait(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    return !putOreWidget.visible() && ctx.inventory.select().id(ORE_IDs).count() == 0;
                }
            }, 250, 8);
        }
        return false;
    }


    /**
     * Walk to the dispenser
     *
     * if the dispenser is in the viewport, then click to walk
     * otherwise, use the minimap
     *
     * waits until the player is not moving
     *
     * @return true if dispenser tile is in viewport
     */
    private boolean moveToDispenser() {
        if (!ctx.players.local().inMotion()) {
            if (DISPENSER_TILE.distanceTo(ctx.players.local()) > 3) {
                log.info("Move to dispenser");
                ctx.camera.pitch(true);

                Tile randomDispenserTile = DISPENSER_AREA.getRandomTile();
                if (randomDispenserTile.matrix(ctx).inViewport())
                    randomDispenserTile.matrix(ctx).click("Walk here");
                else ctx.movement.step(randomDispenserTile);

                if(Random.nextInt(0, 2) == 0)
                    ctx.camera.turnTo(DISPENSER_TILE, 10);
            }
        }

        return Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return DISPENSER_TILE.distanceTo(ctx.players.local()) <= 3;
            }
        }, 250, 8);
    }

    /**
     * @return
     */
    private int collectBars() {
        final GameObject barDispenser = ctx.objects.select(10).id(BAR_DISPENSER_ID).peek();
        final Component barWidget = ctx.widgets.component(BAR_WIDGET, barType.getWidgetComponent());

        if (barDispenser.valid() && barDispenser.inViewport()) {
            if (barWidget.visible()) {
                log.info("Bar widget");
                final int prevBarCount = barCount();

                barWidget.click();

                if(Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return barCount() == 0 && ctx.inventory.select().id(BAR_IDs).count() == prevBarCount;
                    }
                }, 250, 4) == true)
                    return prevBarCount;
            } else if (barDispenser.click("Take", Game.Crosshair.ACTION) || barDispenser.interact(false, "Take")) {
                log.info("Use dispenser");

                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return barWidget.visible();
                    }
                }, 250, 4);
            }
        } else {  // wait for bars to be ready

            // start a timer to check whether still idle
            if (!waitingForBars && (primaryCount() < fullLoad || coalCount() < fullLoad * barType.getRatio()) && barCount() < fullLoad) {
                log.info("Anti-idle timer start");
                waitingForBars = true;
                idleTimer = new Timer();
                idleTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        log.info("Idle timer triggered");
                        if (waitingForBars) {
                            log.info("Reset ore counts");
                            expectedPrimaryCount = primaryCount();
                            expectedCoalCount = coalCount();
                            expectedBarCount = barCount();
                            resetIdleTimer();
                        }
                    }
                }, IDLE_TIME_THRESHOLD);
            }

            if (barWidget.visible()) {
                log.info("close menu");
                ctx.widgets.component(BAR_WIDGET, BAR_CLOSE_COMPONENT).click();
                Condition.sleep();
            }
            if (barCount > 0) {
                log.info("move position");
                DISPENSER_AREA.getRandomTile().matrix(ctx).click();
                // rare bug where dispenser is not updated
                Condition.sleep();
            }
            Antiban.run(ctx);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return barDispenser.valid();
                }
            }, 500, 8);
        }

        return 0;
    }


    private boolean payForeman() {
        if(ctx.bank.opened())
            ctx.bank.close();
        if(ctx.inventory.select().id(COIN_ID).peek().stackSize() >= PAYMENT_COST) {
            if(ctx.npcs.select().id(FOREMAN_ID).peek().inViewport()) {
                log.info("Click on foreman");
                ctx.npcs.poll().interact("Pay");
            }
            else {
                ctx.movement.step(ctx.npcs.peek());
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.npcs.poll().tile().distanceTo(ctx.players.local()) < 3;
                    }
                }, 250, 4);
                return false;
            }

            final int beforeCoins = ctx.inventory.poll().stackSize();
            if(Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.component(PAYMENT_WIDGET, PAYMENT_COMPONENT).component(PAYMENT_COMPONENT2).click();
                }
            }, 250, 4)) {
                return Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        //log.info("Coins not subtracted");
                        return ctx.inventory.select().id(COIN_ID).poll().stackSize() == beforeCoins - PAYMENT_COST;
                    }
                }, 250, 4);
            }
        }
        else {
            log.info("Going to withdraw coins");
            if(openBank()) {
                if(ctx.inventory.select().count() == 28) {
                    ctx.bank.depositInventory();
                }
                ctx.bank.withdraw(COIN_ID, Bank.Amount.ALL); // all but one?
            }
        }
        return false;
    }


    private void resetIdleTimer() {
        waitingForBars = false;
        log.info("Cancel idle timer");
        idleTimer.cancel();
    }

    private boolean handleDialogue() {
        if (ctx.widgets.id(DIALOGUE_WIDGET).peek().valid()) {
            // click on a random tile next to player to close dialogue
            log.info("Handling dialogue");

            Tile currentTile = ctx.players.local().tile();
            Tile newTile = new Tile(currentTile.x() + Random.nextInt(-1, 2), currentTile.y() + Random.nextInt(-1, 2));

            if (!newTile.matrix(ctx).click("Walk here"))
                newTile.matrix(ctx).interact(false, "Walk here");
            return Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.widgets.peek().valid();
                }
            }, 250, 4);
        }
        return true;
    }

    private void handleReportWidget() {
        ctx.widgets.component(REPORT_WIDGET, REPORT_COMPONENT).component(REPORT_CLOSE_COMPONENT).click();
    }

    /**
     * Output time elapsed
     *
     * @param ms - current time in miliseconds
     * @return String with format hh:mm:ss
     */
    public static String getTimeElapsed(long ms) {
        long sec, min, hr;

        sec = (ms / 1000) % 60;
        min = ((ms / 1000) / 60) % 60;
        hr = ((ms / 1000) / 60) / 60;


        return String.format("%02d:%02d:%02d", hr, min, sec);
    }

    public void setBarType(BarInfo b) {
        barType = b;
    }
}
