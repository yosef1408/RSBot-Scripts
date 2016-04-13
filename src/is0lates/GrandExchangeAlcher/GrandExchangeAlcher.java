package GrandExchangeAlcher;

import GrandExchangeAlcher.model.AlchItem;
import GrandExchangeAlcher.org.jsoup.Jsoup;
import GrandExchangeAlcher.org.jsoup.nodes.Document;
import GrandExchangeAlcher.org.jsoup.nodes.Element;
import GrandExchangeAlcher.org.jsoup.select.Elements;
import org.powerbot.script.*;
import org.powerbot.script.GeItem;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Script.Manifest(name = "Grand Exchange Alcher",description = "Alchs for money at Grand Exchange", properties = "topic=https://www.powerbot.org/community/topic/1307713-grand-exchange-alcher/; author=Is0lates; ")
public class GrandExchangeAlcher extends PollingScript<ClientContext> implements MessageListener, PaintListener, Stoppable {

    private static int NATURE_RUNE_ID = 561;
    private static int natureRunePrice;
    private static String status = "Starting";
    private static ArrayList<AlchItem> alchItemList;
    private static AlchItem currentAlchItem;
    private static int currentAlchItemIndex = 0;
    private static int currentAlchItemId;
    private static boolean isStopping = false;
    private static final long startTime = System.currentTimeMillis();
    private static int startXp = -1;
    private static int totalAlchs;
    private static int totalGp;
    private static int totalGuideGp;
    public static GrandExchangeAlcherForm form;
    public static int buyNatureRunePrice;
    public static int minProfit;
    public static boolean f2pItemsOnly = false;
    public static String sortBy = "Profit";
    public static int waitForOrders = 0;

    private static AlchItem lastAlchItem;

    private static GrandExchange grandExchange;

    @Override
    public boolean isStopping() {
        return isStopping;
    }

    private void fetchAlchItems() {
        alchItemList = new ArrayList<AlchItem>();
        final String content = downloadString("http://runescape.wikia.com/wiki/RuneScape:Grand_Exchange_Market_Watch/Alchemy");
        Document doc = Jsoup.parse(content);
        Elements rows = doc.select("div#mw-content-text table tbody tr");
        for (Element row : rows) {
            if(row.select("td").isEmpty()) {
                continue;
            }
            try {
                AlchItem alchItem = new AlchItem();
                alchItem.name = row.select("td").get(1).select("a").text();
                alchItem.price = Integer.parseInt(row.select("td").get(2).text().replace(",", ""));
                alchItem.alchPrice = Integer.parseInt(row.select("td").get(3).text().replace(",", ""));
                alchItem.profit = Integer.parseInt(row.select("td").get(4).text());
                alchItem.maxProfit = Integer.parseInt(row.select("td").get(7).text().replace(",", ""));
                alchItem.limit = Integer.parseInt(row.select("td").get(6).text().replace(",", ""));
                alchItem.members = row.select("td").get(8).select("a").attr("title").contains("Members");
                if((!isMember() || f2pItemsOnly) && alchItem.members == true ) {
                    continue;
                }
                alchItemList.add(alchItem);
                fetchItemId(alchItem);
            } catch (Exception e) {
                status = "Some items could not be parsed";
            }
        }
        Collections.sort(alchItemList, new Comparator<AlchItem>() {
            @Override public int compare(AlchItem i1, AlchItem i2) {
                Integer x3 = i1.maxProfit;
                Integer x4 = i2.maxProfit;
                if(sortBy.equals("Profit")) {
                    x3 = i1.profit;
                    x4 = i2.profit;
                }
                return x4.compareTo(x3);
            }
        });
    }

    private void fetchItemId(AlchItem alchItem) {
        currentAlchItemId = -1;
        status = "Fetching item ID for " + alchItem.name;
        final String content = downloadString("http://runescape.wikia.com/wiki/Exchange:" + alchItem.name.trim().replace(" ", "_").replace(",", "%27"));
        Document doc = Jsoup.parse(content);
        String id = doc.select("span#GEDBID").first().text();
        currentAlchItemId = Integer.parseInt(id);
        alchItemList.get(alchItemList.indexOf(alchItem)).id = Integer.parseInt(id);
        status = "Fetching item ID for " + alchItem.name + " : " + currentAlchItemId;
        while (currentAlchItemId < 0) {
            status += ".";
            Condition.sleep(500);
        }
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
        form = new GrandExchangeAlcherForm(ctx, this);
        form.setLocationRelativeTo(null);
        form.setVisible(true);
        while(!form.valid) {
            Condition.sleep(500);
        }
        grandExchange = new GrandExchange(ctx);
        status = "Fetching items...";
        fetchAlchItems();
        GeItem natureRune = new org.powerbot.script.rt6.GeItem(NATURE_RUNE_ID);
        natureRunePrice = natureRune.price;
    }

    private int countOpenOrders() {
        int openOrders = 0;
        for (int s = 0; s < (isMember() ? 7 : 3); s++) {
            String orderType = ctx.widgets.component(grandExchange.WIDGET, GrandExchange.FIRST_SLOT_COMPONENT + (s)).component(1).text();
            if (orderType.equals("Buy") || orderType.equals("Sell")) {
                openOrders++;
            }
        }
        return openOrders;
    }

    private int countAvailableOrders() {
        return (isMember() ? 7 : 3) - countOpenOrders();
    }

    private boolean abortOrder() {
        return Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                int s = (int) (Math.random() * (isMember() ? 7 : 3));
                return ctx.widgets.component(GrandExchange.WIDGET, GrandExchange.FIRST_SLOT_COMPONENT + s).click();
            }
        },1000,5) &&
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return ctx.widgets.component(GrandExchange.WIDGET, GrandExchange.ABORT_OFFER_COMPONENT).visible() &&
                        ctx.widgets.component(GrandExchange.WIDGET, GrandExchange.ABORT_OFFER_COMPONENT).click();
            }
        },1000,5) &&
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() {
                return grandExchange.collectToInventory();
            }
        },1000,5);
    }

    private int countOrdersHaveProgress() {
        int count = 0;
        for(int i = 1; i <= (isMember() ? 7 : 3); i++) {
            if(grandExchange.getProgress(i) > 0) {
                count++;
            }
        }
        return count;
    }

    private boolean isMember(){
        return !ctx.widgets.component(1484, 5).visible();
    }

    private void escape() {
        ctx.input.send("{VK_ESCAPE down}");
        Condition.sleep(50);
        ctx.input.send("{VK_ESCAPE up}");
        Condition.sleep(1000);
    }

    public void poll() {
        if(startXp == -1) {
            startXp = ctx.skills.experience(6);
        }

        if(currentAlchItem == null) {
            currentAlchItem = alchItemList.get(currentAlchItemIndex);
        }

        if(ctx.widgets.component(grandExchange.WIDGET, 15).visible()) {
            Condition.wait(new Callable<Boolean>() {
                public Boolean call() {
                    return ctx.widgets.component(grandExchange.WIDGET, 15).click();
                }
            }, 1000, 5);
            Condition.sleep(1000);
            return;
        }
        if(ctx.widgets.component(grandExchange.WIDGET, GrandExchange.CONFIRM_COMPONENT).visible() ||
                ctx.widgets.component(grandExchange.WIDGET, 70).visible() ||
                ctx.widgets.component(1433,0).visible() ||
                ctx.bank.opened()) {
            status = "Stuck on screen. Escaping.";
            escape();
            return;
        }

        if(ctx.backpack.select().id(561).count() == 0) {
            status = "No nature runes, buying nature runes";
            if(!grandExchange.opened()) {
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() {
                        return grandExchange.open();
                    }
                }, 1000, 5);
                return;
            } else {
                if(countAvailableOrders() == 0) {
                    abortOrder();
                    Condition.sleep(1000);
                    return;
                }
                if(Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        int coins = ctx.backpack.moneyPouchCount();
                        int divider = isMember() ? 7 : 3;
                        int amount;
                        if (divider == 0) {
                            amount = 0;
                        } else {
                            amount = Integer.parseInt(Double.toString(Math.floor((coins < 1 ? 1 : coins) / (buyNatureRunePrice< 1 ? 1 : buyNatureRunePrice) / divider)).replace(".0", ""));
                        }
                        return grandExchange.buy(NATURE_RUNE_ID, amount, buyNatureRunePrice);
                    }
                })) {
                    Condition.sleep(2000);
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            return grandExchange.collectToInventory();
                        }
                    }, 100, 30);
                    return;
                }
            }
        }
        else if(ctx.backpack.select().count() == 1) {
            status = "No items to alch, buying items";
            if(!grandExchange.opened()) {
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() {
                        return grandExchange.open();
                    }
                }, 100,30);
                return;
            } else {
                if(countAvailableOrders() == 0) {
                    waitForOrders++;
                    if(countOrdersHaveProgress() > 0) {
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() {
                                return grandExchange.collectToInventory();
                            }
                        }, 100, 30);
                    }
                    Condition.sleep(1000);
                    if(waitForOrders > 4) {
                        status = "Aborting order";
                        if(Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() {
                                return abortOrder();
                            }
                        })) {
                            Condition.sleep(1000);
                            waitForOrders = 0;
                        }
                    }
                    return;
                } else {
                    status = "Buying item " + currentAlchItem.name;
                    if(Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            int divider = countAvailableOrders() < 1 ? 1 : countAvailableOrders();
                            int coins = ctx.backpack.moneyPouchCount();
                            int price = ((currentAlchItem.alchPrice - minProfit) - buyNatureRunePrice < 1 ? 1 : (currentAlchItem.alchPrice - minProfit) - buyNatureRunePrice);
                            int canByAmount = (coins < 1 ? 1 : coins) / price / divider;
                            int amount = Integer.parseInt(Double.toString(Math.floor(canByAmount > currentAlchItem.limit ? currentAlchItem.limit : canByAmount)).replace(".0", ""));
                            return grandExchange.buy(currentAlchItem.id, amount, price);
                        }
                    })) {
                        currentAlchItemIndex++;
                        if(currentAlchItemIndex >= alchItemList.size()) {
                            currentAlchItemIndex = 0;
                        }
                        currentAlchItem = alchItemList.get(currentAlchItemIndex);
                        waitForOrders = 0;
                        Condition.sleep(2000);
                        return;
                    }
                }
            }
        } else {
            waitForOrders = 0;
            if(grandExchange.opened()) {
                escape();
            }
            status = "Alching";
            for(int i = 0; i < 28; i++) {
                Component component = ctx.widgets.component(1473,5).component(i);
                if(component.itemId() > 0 && component.itemId() != NATURE_RUNE_ID) {
                    lastAlchItem = getAlchItemById(component.itemId());
                    ctx.input.send("1");
                    component.click();
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            return !(ctx.players.local().animation() == 24458 || ctx.players.local().animation() == 17099);
                        }
                    },10,100);
                    break;
                }
            }
        }
    }

    public void messaged(MessageEvent messageEvent) {
        if(messageEvent.text().contains("coins have been added to your money pouch.") && messageEvent.source().equals("")) {
            if(status.equals("Alching")) {
                totalAlchs++;
                int alchGp = Integer.parseInt(messageEvent.text().replace("coins have been added to your money pouch.","").replace(",","").trim());
                int alchPrice = (lastAlchItem.alchPrice - 100);
                int alchProfit = alchGp-alchPrice;
                totalGp = totalGp + alchProfit;
                totalGuideGp = totalGuideGp + lastAlchItem.profit;
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

    Font font = new Font("Arial", Font.PLAIN, 10);
    Color background = new Color(0, 0, 0, 150);
    public void repaint(Graphics graphics) {
        graphics.setFont(font);
        graphics.setColor(background);
        graphics.drawRect(0, 0, 400, 150);
        graphics.fillRect(0, 0, 400, 150);
        graphics.setColor(Color.WHITE);
        graphics.getFont();
        graphics.drawString("Grand Exchange Alcher", 5, 15);
        int runtime = Integer.parseInt("" + (System.currentTimeMillis()- startTime));
        graphics.drawString("Run time: " + timeFormat(runtime), 300, 15);
        graphics.drawString("Status: " + status, 5, 40);
        graphics.drawString("Total Alchs: " + totalAlchs, 5, 53);
        graphics.drawString("Alchs/h: " + (int)((3600000D*totalAlchs) / (System.currentTimeMillis() - startTime)), 5, 68);

        graphics.drawString("Profit: " + totalGp + " ~ " +totalGuideGp, 105, 53);
        graphics.drawString("Profit/h: " + (int)((3600000D*totalGp) / (System.currentTimeMillis() - startTime)) + " ~ " + (int)((3600000D*totalGuideGp) / (System.currentTimeMillis() - startTime)), 105, 68);

        if(startXp != -1) {
            graphics.drawString("XP gained: " + (ctx.skills.experience(6) - startXp), 266, 53);
            graphics.drawString("XP/h: " + (int)((3600000D*(ctx.skills.experience(6) - startXp)) / (System.currentTimeMillis() - startTime)), 266, 68);
        }

        if(currentAlchItem != null && !status.equals("Alching")) {
            graphics.drawString("Next buy item:", 5, 90);
            graphics.drawString("(#" + currentAlchItem.id + ") " + currentAlchItem.name, 125, 90);

            graphics.drawString("Alch price:", 5, 105);
            graphics.drawString("" + currentAlchItem.alchPrice, 65, 105);

            graphics.drawString("Limit:", 5, 120);
            graphics.drawString("" + currentAlchItem.limit, 65, 120);

            graphics.drawString("Buy price:", 125, 105);
            graphics.drawString("" + ((currentAlchItem.alchPrice - minProfit) - buyNatureRunePrice), 190, 105);

            graphics.drawString("Nat price:", 125, 120);
            graphics.drawString("" + buyNatureRunePrice, 190, 120);

            graphics.drawString("Calc Profit:", 125, 135);
            graphics.drawString("" + (currentAlchItem.alchPrice-((currentAlchItem.alchPrice - minProfit))), 190, 135);

            graphics.drawString("Guide price:", 255, 105);
            graphics.drawString("" + (currentAlchItem.price), 320, 105);

            graphics.drawString("Guide Profit:", 255, 120);
            graphics.drawString("" + currentAlchItem.profit, 320, 120);

            graphics.drawString("Guide Max:", 255, 135);
            graphics.drawString("" + currentAlchItem.maxProfit, 320, 135);

        }else if(lastAlchItem != null && status.equals("Alching")) {
            graphics.drawString("Alching item:", 5, 90);
            graphics.drawString("(#" + lastAlchItem.id + ") " + lastAlchItem.name, 70, 90);

            graphics.drawString("Alch price:", 5, 105);
            graphics.drawString("" + lastAlchItem.alchPrice, 65, 105);

            graphics.drawString("Limit:", 5, 120);
            graphics.drawString("" + lastAlchItem.limit, 65, 120);

            graphics.drawString("Buy price:", 125, 105);
            graphics.drawString("" + ((lastAlchItem.alchPrice - minProfit) - buyNatureRunePrice), 190, 105);

            graphics.drawString("Nat price:", 125, 120);
            graphics.drawString("" + buyNatureRunePrice, 190, 120);

            graphics.drawString("Calc Profit:", 125, 135);
            graphics.drawString("" + (lastAlchItem.alchPrice-((lastAlchItem.alchPrice - minProfit))), 190, 135);

            graphics.drawString("Guide price:", 255, 105);
            graphics.drawString("" + (lastAlchItem.price), 320, 105);

            graphics.drawString("Guide Profit:", 255, 120);
            graphics.drawString("" + lastAlchItem.profit, 320, 120);

            graphics.drawString("Guide Max:", 255, 135);
            graphics.drawString("" + lastAlchItem.maxProfit, 320, 135);
        }
    }
}
