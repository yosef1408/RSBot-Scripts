package is0lates.GrandExchangeAlcher;

import is0lates.GrandExchangeAlcher.model.AlchItem;
import org.powerbot.script.*;
import org.powerbot.script.GeItem;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Hud;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.Player;
import org.powerbot.script.rt6.Component;

import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Script.Manifest(name = "Grand Exchange Alcher",description = "Buys and alchs items at the Grand Exchange for profit and Magic XP.", properties = "topic=1307713;author=Is0lates;")
public class GrandExchangeAlcher extends PollingScript<ClientContext> implements MessageListener, PaintListener, MouseMotionListener {

    public static boolean started = false;
    private static int NATURE_RUNE_ID = 561;
    public static int natureRunePrice;
    private static String status = "Starting";
    public static ArrayList<AlchItem> alchItemList = new ArrayList<AlchItem>();
    private static AlchItem currentAlchItem;
    private static int currentAlchItemIndex = 0;
    private static final long startTime = System.currentTimeMillis();
    private static int startXp = -1;
    private static int totalAlchs;
    private static int totalGp;
    public static GUI form;
    public static int buyNatureRunePrice;
    public static int minProfit;
    public static boolean f2pItemsOnly = false;
    public static String sortBy = "Profit";
    public static int waitForOrders = 0;
    public static int lastTotalOrderProgress = 0;
    private static AlchItem lastAlchItem;

    private static GrandExchange grandExchange;

    public static String downloadedItems = "";
    public String downloadItems() {
        status = "Fetching items...";
        downloadedItems = downloadString("http://83.84.15.117/");
        return downloadedItems;
    }

    private AlchItem getAlchItemById(int id) {
        for(AlchItem alchItem : alchItemList) {
            if(alchItem.id == id || alchItem.id == (id-1)) {
                return alchItem;
            }
        }
        return null;
    }

    public void start() {
        downloadItems();
        GeItem natureRune = new org.powerbot.script.rt6.GeItem(NATURE_RUNE_ID);
        natureRunePrice = natureRune.price;
        status = "Waiting to start";
        form = new GUI(this);
        form.setLocationRelativeTo(null);
        form.setVisible(true);
        status = "Waiting to start";
        while(!form.valid) {
            Condition.sleep(500);
        }
        grandExchange = new GrandExchange(ctx);
    }

    private int countOpenOrders() {
        int openOrders = 0;
        for (int s = 0; s < (isMember() ? 8 : 3); s++) {
            String orderType = ctx.widgets.component(grandExchange.WIDGET, GrandExchange.FIRST_SLOT_COMPONENT + (s)).component(1).text();
            if (orderType.equals("Buy") || orderType.equals("Sell")) {
                openOrders++;
            }
        }
        return openOrders;
    }

    private int countAvailableOrders() {
        return (isMember() ? 8 : 3) - countOpenOrders();
    }

    private boolean abortOrder() {
        int s = (int) (Math.random() * (isMember() ? 8 : 3));
        return abortOrder(s);
    }

    private boolean abortOrder(int s) {
        ctx.widgets.component(GrandExchange.WIDGET, GrandExchange.FIRST_SLOT_COMPONENT + s).click();
        return Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ctx.widgets.component(GrandExchange.WIDGET, GrandExchange.ABORT_OFFER_COMPONENT).visible() &&
                        ctx.widgets.component(GrandExchange.WIDGET, GrandExchange.ABORT_OFFER_COMPONENT).click();
            }
        },1000,new Random().nextInt(5,50)) &&
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                status = "Collecting items to inventory.";
                return grandExchange.collectToInventory();
            }
        },1000,new Random().nextInt(5,50));
    }

    private int countOrdersHaveProgress() {
        int count = 0;
        for(int i = 1; i <= (isMember() ? 8 : 3); i++) {
            if(grandExchange.getProgress(i) > 0) {
                count++;
            }
        }
        return count;
    }

    private int countTotalProgress() {
        int totalProgress = 0;
        for(int i = 0; i < (isMember() ? 8 : 3); i++) {
            if(grandExchange.getProgress(i) > 0) {
                totalProgress += Math.ceil(grandExchange.getProgress(i)*100);
            }
        }
        return totalProgress;
    }

    private boolean isMember(){
        return !ctx.widgets.component(1484, 5).visible();
    }

    private void escape() {
        ctx.input.send("{VK_ESCAPE down}");
        Condition.sleep(new Random().nextInt(50,100));
        ctx.input.send("{VK_ESCAPE up}");
        Condition.sleep(new Random().nextInt(1000,2000));
    }

    int loop = 0;
    public void poll() {
        if (loop == 0) {
            if (startXp == -1) {
                startXp = ctx.skills.experience(6);
            }
        }
        loop++;

        if(!ctx.hud.opened(Hud.Window.BACKPACK)) {
            status = "Opening Backpack.";
            ctx.hud.open(Hud.Window.BACKPACK);
        }

        if(ctx.widgets.component(594,217).visible()) {
            ctx.widgets.component(594,217).click();
        }
        if(ctx.widgets.component(1560,22).visible()) {
            ctx.widgets.component(1560, 22).click();
        }

        if (currentAlchItem == null) {
            currentAlchItem = alchItemList.get(currentAlchItemIndex);
        }

        if (ctx.widgets.component(grandExchange.WIDGET, 15).visible()) {
            Condition.wait(new Callable<Boolean>() {
                public Boolean call() {
                    return ctx.widgets.component(grandExchange.WIDGET, 15).click();
                }
            }, 1000, 5);
            Condition.sleep(new Random().nextInt(1000,3500));
            return;
        }
        if (ctx.widgets.component(grandExchange.WIDGET, GrandExchange.CONFIRM_COMPONENT).visible() ||
                ctx.widgets.component(grandExchange.WIDGET, 70).visible() ||
                ctx.widgets.component(1433, 0).visible() ||
                ctx.bank.opened()) {
            status = "Stuck on screen. Escaping.";
            switch (new Random().nextInt(1,2)) {
                case 1:
                    grandExchange.close();
                    break;
                case 2:
                    escape();
                    break;
            }
            return;
        }

        if (ctx.backpack.select().id(561).count() == 0) {
            if(ctx.widgets.component(1560,22).visible()) {
                ctx.widgets.component(1560, 22).click();
            }
            status = "No nature runes, buying nature runes.";
            if (!grandExchange.opened()) {
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() {
                        return grandExchange.open();
                    }
                }, 1000, 5);
                return;
            } else {
                if (countAvailableOrders() == 0) {
                    abortOrder();
                    Condition.sleep(new Random().nextInt(1000,3500));
                    return;
                }
                if (Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        int coins = ctx.backpack.moneyPouchCount();
                        int divider = isMember() ? 8 : 3;
                        int amount;
                        if (divider == 0) {
                            amount = 0;
                        } else {
                            amount = Integer.parseInt(Double.toString(Math.floor((coins < 1 ? 1 : coins) / (buyNatureRunePrice < 1 ? 1 : buyNatureRunePrice) / divider)).replace(".0", ""));
                        }
                        return grandExchange.buy(NATURE_RUNE_ID, amount, buyNatureRunePrice);
                    }
                })) {
                    Condition.sleep(new Random().nextInt(2000,3500));
                    status = "Collecting items to inventory.";
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            return grandExchange.collectToInventory();
                        }
                    }, 100, 30);
                    Condition.sleep(new Random().nextInt(1000,2000));
                    return;
                }
            }
        } else if (ctx.backpack.select().count() == 1) {
            if(ctx.widgets.component(1560,22).visible()) {
                ctx.widgets.component(1560, 22).click();
            }
            status = "No items to alch, buying items.";
            lastAlchItem = null;
            if (!grandExchange.opened()) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        return grandExchange.open();
                    }
                }, 100, 30);
                return;
            } else {
                if (countAvailableOrders() == 0) {
                    waitForOrders++;
                    if (countOrdersHaveProgress() > 0 && lastTotalOrderProgress != countTotalProgress()) {
                        status = "Collecting items to inventory.";
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() {
                                return grandExchange.collectToInventory();
                            }
                        }, 100, new Random().nextInt(30,300));
                        lastTotalOrderProgress = countTotalProgress();
                    }
                    Condition.sleep(new Random().nextInt(1000,3500));
                    if (waitForOrders > 4) {
                        status = "Aborting order.";
                        if (Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() {
                                return abortOrder();
                            }
                        })) {
                            Condition.sleep(new Random().nextInt(1000,3500));
                            waitForOrders = 0;
                            lastTotalOrderProgress = 0;
                        }
                    }
                    return;
                } else {
                    status = "Buying item " + currentAlchItem.name + ".";
                    if (Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            int divider = countAvailableOrders() < 1 ? 1 : countAvailableOrders();
                            int coins = ctx.backpack.moneyPouchCount();
                            int price = (currentAlchItem.buyPrice) < 1 ? 1 : (currentAlchItem.buyPrice);
                            int canByAmount = (coins < 1 ? 1 : coins) / price / divider;
                            int amount = Integer.parseInt(Double.toString(Math.floor(canByAmount > currentAlchItem.limit ? currentAlchItem.limit : canByAmount)).replace(".0", ""));
                            grandExchange.buy(currentAlchItem.id, amount, price);
                            currentAlchItem.ordered += amount;
                            return true;
                        }
                    })) {
                        currentAlchItemIndex++;
                        if (currentAlchItemIndex >= alchItemList.size()) {
                            currentAlchItemIndex = 0;
                        }
                        currentAlchItem = alchItemList.get(currentAlchItemIndex);
                        waitForOrders = 0;
                        lastTotalOrderProgress = 0;
                        Condition.sleep(new Random().nextInt(2000,3500));
                        return;
                    }
                }
            }
        } else {
            waitForOrders = 0;
            lastTotalOrderProgress = 0;
            if (grandExchange.opened()) {
                switch (new Random().nextInt(1,2)) {
                    case 1:
                        grandExchange.close();
                        break;
                    case 2:
                        escape();
                        break;
                }
            }

            if(ctx.widgets.component(1477,509).component(1).visible()) {
                ctx.widgets.component(1477,509).component(1).click();
            }

            status = "Alching.";
            for(Item item : ctx.backpack.items()) {
                if(item.id() > 0 && item.id() != NATURE_RUNE_ID) {
                    Condition.sleep(new Random().nextInt(100,500));
                    ctx.input.send("1");
                    Condition.sleep(new Random().nextInt(100,500));
                    item.click();
                    lastAlchItem = getAlchItemById(item.id());
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            return !(ctx.players.local().animation() == 24458 || ctx.players.local().animation() == 17099);
                        }
                    }, 1, 2000);
                    Condition.sleep(new Random().nextInt(100,500));
                    break;
                }
            }
        }
    }

    public void messaged(MessageEvent messageEvent) {
        if(messageEvent.text().contains("coins have been added to your money pouch.") && messageEvent.source().equals("")) {
            if (status.equals("Alching.")) {
                totalAlchs++;
                totalGp += lastAlchItem.calcProfit;
                lastAlchItem.alched++;
            }
        }
    }

    private String timeFormat(long duration) {
        String res = "";
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration));
        if (days == 0) {
            res = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            res = String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
        }
        return res;
    }

    /**
     * Paint
     */

    int positionX = 5;
    int positionY = 5;
    int columnWidth = 50;
    int rowHeight = 15;
    int rowPadding = 0;
    int columnPadding = 3;
    float span = 1;
    final static int defaultOpacity = 100;
    int opacity = defaultOpacity;

    Color color =  Color.WHITE;
    Font font = new Font("Arial", Font.PLAIN, 12);
    Color background = new Color(0, 0, 0, opacity);
    Graphics _graphics = null;

    void drawColumn(String text, int row, double column, double span, Color color, Font font, Color background) {
        int rectHeight = rowHeight+(rowPadding*2);
        int rectWidth = columnWidth+(columnPadding*2);
        int rectX = positionX + (int)(column * rectWidth);
        int rectY = positionY + (row * rectHeight);
        int textX = rectX + columnPadding;
        int textY = rectY + rowHeight - columnPadding;
        _graphics.setColor(background);
        _graphics.drawRect(rectX, rectY, (int)(rectWidth*span), rectHeight);
        _graphics.fillRect(rectX, rectY, (int)(rectWidth*span), rectHeight);
        _graphics.setFont(font);
        _graphics.setColor(color);
        _graphics.drawString(text, textX, textY);
    }

    void drawColumn(String text, int row, double column, double span, Color color) {
        drawColumn(text, row, column, span, color, font, background);
    }

    void drawColumn(String text, int row, double column, Color color) {
        drawColumn(text, row, column, span, color, font, background);
    }

    void drawColumn(String text, int row, double column, Color color, Color background) {
        drawColumn(text, row, column, span, color, font, background);
    }

    void drawColumn(String text, int row, double column, double span, Color color, Color background) {
        drawColumn(text, row, column, span, color, font, background);
    }

    void drawColumn(String text, int row, double column, double span) {
        drawColumn(text, row, column, span, color, font, background);
    }

    void drawColumn(String text, int row, double column) {
        drawColumn(text, row, column, span, color, font, background);
    }


    private boolean mouseOverPaint(MouseEvent e) {
        return e.getX() > positionX && e.getX() < 11*(columnWidth+(columnPadding*2)) && e.getY() > positionY && e.getY() < 17*rowHeight;
    }

    public void mouseMoved(MouseEvent e) {
        if(mouseOverPaint(e)){
            opacity = 255;
        } else {
            opacity = defaultOpacity;
        }
        background = new Color(0, 0, 0, opacity);
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void repaint(Graphics graphics) {
        _graphics = graphics;
        float runtime = System.currentTimeMillis() - startTime;
        drawColumn("Grand Exchange Alcher", 0, 0, 8, Color.GREEN);
        drawColumn("Run Time:", 0, 8, 2, Color.lightGray);
        drawColumn(timeFormat((int)runtime), 0, 10);

        drawColumn("Status:", 1,0, Color.lightGray);
        drawColumn(status, 1,1,10);

        drawColumn("Alchs", 2, 0, 1.83, Color.lightGray);
        drawColumn("Alchs/H", 2, 1.83, 1.83, Color.lightGray);
        drawColumn("Min Profit", 2, 3.66, 1.87, Color.lightGray);
        drawColumn("Min Profit/H", 2, 5.5, 1.83, Color.lightGray);
        drawColumn("XP", 2, 7.333333333, 1.83, Color.lightGray);
        drawColumn("XP/H", 2, 9.16, 1.87, Color.lightGray);

        drawColumn(""+totalAlchs, 3, 0, 1.83);
        drawColumn("" + (int)((3600000D*totalAlchs) / runtime), 3, 1.83,1.83);
        drawColumn(""+totalGp, 3, 3.66, 1.87);
        drawColumn("" + (int)((3600000D*totalGp) / runtime), 3, 5.5, 1.83);
        drawColumn(""+(ctx.skills.experience(6) - startXp), 3, 7.3333333,1.83);
        drawColumn("" + (int) ((3600000D * (ctx.skills.experience(6) - startXp)) / (System.currentTimeMillis() - startTime)), 3, 9.16,1.87);

        drawColumn("Nat Price:", 4, 0, Color.lightGray);
        drawColumn(""+natureRunePrice, 4, 1, 4.5);
        drawColumn("Buy Nat:", 4, 5.5, Color.lightGray);
        drawColumn(""+buyNatureRunePrice, 4,6.5,4.5);

        drawColumn("#", 5, 0, Color.lightGray);
        drawColumn("Name", 5, 1, 4, Color.lightGray);
        drawColumn("Limit", 5,5, Color.lightGray);
        drawColumn("High Alch", 5, 6, Color.lightGray);
        drawColumn("Buy Price",5,7, Color.lightGray);
        drawColumn("Guide Price",5,8, Color.lightGray);
        drawColumn("Min Profit", 5, 9, Color.lightGray);
        drawColumn("Alched", 5, 10, Color.lightGray);

        if(started) {
            int alchItemCount = alchItemList.size()-1;
            int lastAlchItemIndex = alchItemList.indexOf(lastAlchItem);
            int reloopItemIndex = 0;
            for (int i = (alchItemCount < 10 ? (alchItemCount/2) * -1 : -5); i <= (alchItemCount < 10 ? (alchItemCount/2) : 5); i++) {
                int index = lastAlchItem instanceof AlchItem ? lastAlchItemIndex : currentAlchItemIndex;
                int alchtItemIndex = index+i < 0 ? alchItemCount+index+i+1 : index+i > alchItemCount ? reloopItemIndex : index+i;
                if(index+i > alchItemCount) {
                    reloopItemIndex++;
                }
                AlchItem ai = alchItemList.get(alchtItemIndex);
                int row = (alchItemCount < 10 ? (alchItemCount/2) + 6 : 11) + i;
                int alpha = (i == 0 ? opacity : i < 0 ? (opacity-(-1*(i*20))) > opacity ? opacity : (opacity-(-1*(i*20))) : (opacity-(i*20)) > opacity ? opacity : (opacity-(i*20)));
                Color color = new Color(255, 255, 255, alpha);
                if(currentAlchItem instanceof AlchItem && currentAlchItem.id == ai.id) {
                    color = Color.ORANGE;
                    alpha = opacity;
                }
                if(lastAlchItem instanceof AlchItem && lastAlchItem.id == ai.id) {
                    color = Color.GREEN;
                    alpha = opacity;
                }
                Color background = new Color(0, 0, 0, alpha);

                drawColumn("" + (alchtItemIndex + 1), row, 0, color, background);
                drawColumn(ai.name, row, 1, 4, color, background);
                drawColumn("" + ai.limit, row, 5, color, background);
                drawColumn("" + ai.alchPrice, row, 6, color, background);
                drawColumn("" + ai.buyPrice, row, 7, color, background);
                drawColumn("" + ai.price, row, 8, color, background);
                drawColumn("" + ai.calcProfit, row, 9, color, background);
                drawColumn("" + ai.alched, row, 10, color, background);
            }
        }
    }
}
