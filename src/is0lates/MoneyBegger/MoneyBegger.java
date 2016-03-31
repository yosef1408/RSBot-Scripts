package MoneyBegger;


import javafx.util.Pair;
import org.powerbot.script.*;
import org.powerbot.script.rt6.*;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Collections;

@Script.Manifest(name = "Money Begger", description = "Begs for money at Grand Exchange", properties = "client=6; topic=0")
public class MoneyBegger extends PollingScript<ClientContext> implements MessageListener, PaintListener {

    public static final int WIDGET_TRADE_MAIN = 335;
    public static final int WIDGET_TRADE_SECOND = 334;
    public static final int WIDGET_TRADE_MAIN_NAME = 17;
    public static final int WIDGET_TRADE_SECOND_NAME = 54;
    public static final int WIDGET_TRADE_MAIN_OUR = 34;
    public static final int WIDGET_TRADE_MAIN_THEIR = 32;
    public static final int WIDGET_TRADE_MAIN_ACCEPT = 21;
    public static final int WIDGET_TRADE_MAIN_DECLINE = 23;
    public static final int WIDGET_TRADE_SECOND_ACCEPT = 45;
    public static final int WIDGET_TRADE_SECOND_DECLINE = 50;
    public static final int WIDGET_TRADE_OUR_AMOUNT = 32;
    public static final int WIDGET_TRADE_THEIR_AMOUNT = 35;
    public final static int WIDGET_TRADE_MAIN_INV_SLOTS = 23;
    public static final int TRADE_TYPE_MAIN = 0;
    public static final int TRADE_TYPE_SECONDARY = 1;
    public static final int TRADE_TYPE_NONE = 2;
    public static final int MONEY_ID = 995;
    public static final int INCOMING_OFFER_COMPONENT_ID = 29;

    private final Tile bankTile = new Tile(3180, 3476, 0);

    private Tile startPosition;
    private int loopCount = 0;
    public boolean tradeAccepted = false;
    private int tradeAttempts = 0;
    public String lastTradedName;
    public int lastTradedValue;

    private int tradeWait1 = 0;
    private int tradeWait2 = 0;

    private final ArrayList<String> tradeWithNameList = new ArrayList<String>();
    private final ArrayList<String> tradedWithPlayerNameList = new ArrayList<String>();

    private int moneyGained = 0;

    private String status = "Starting";

    private boolean inTrade() {
        return ctx.widgets.component(WIDGET_TRADE_MAIN, 0).visible() || ctx.widgets.component(WIDGET_TRADE_SECOND, 0).visible();
    }

    public void typeMessage(String message) {
        ctx.widgets.select();
        if(ctx.widgets.component(137, 130).text().equals("[Press Enter to Chat]")) {
            Condition.sleep(1000);
            ctx.input.send("{VK_ENTER down}");
            Condition.sleep(50);
            ctx.input.send("{VK_ENTER up}");
            Condition.sleep(50);
            ctx.input.send(message);
            Condition.sleep(50);
            ctx.input.send("{VK_ENTER down}");
            Condition.sleep(50);
            ctx.input.send("{VK_ENTER up}");
        } else {
            ctx.input.send("{VK_ESCAPE down}");
            Condition.sleep(50);
            ctx.input.send("{VK_ESCAPE up}");
        }
    }

    public void hopWorld() {
        boolean member = ctx.widgets.component(1484, 5).visible();
        ctx.widgets.component(1431, 12).click();
        Condition.sleep(200);
        ctx.widgets.component(1433, 8).click();
        Condition.sleep(200);

    }

    public void doDance() {
        ctx.input.send("{VK_1 down}");
        Condition.sleep(50);
        ctx.input.send("{VK_1 up}");
    }

    public void messaged(MessageEvent messageEvent) {
        if(messageEvent.text().replaceAll("<.*?>", "").contains("Accepted trade.")) {
            moneyGained += lastTradedValue;
            status = "Accepted trade";
            tradeAccepted = true;
            tradeWithNameList.clear();
        }
        if(messageEvent.text().replaceAll("<.*?>", "").contains("Other player declined trade!")) {
            status = ("Declined trade");
            tradeWait1 = 60;
            tradeWait2 = 60;
            tradeWithNameList.clear();
        }
        if(messageEvent.text().replaceAll("<.*?>", "").contains("wishes to trade with you.")) {

            tradeWait1 = 0;
            tradeWait2 = 0;
            String messageString = messageEvent.toString();
            String name = messageString.substring(messageString.indexOf("[")+1,messageString.indexOf("]"));
            int tradeOccurences = Collections.frequency(tradedWithPlayerNameList, name);
            if(!tradeWithNameList.contains(name) && tradeOccurences < 10) {
                status = ("Start trade " + name);
                tradeWithNameList.add(name);
            } else {
                status = ("Don't trade " + name);
            }
        }
    }

    private Double getPrice(int itemId) {
        final String content = downloadString("http://itemdb-rs.runescape.com/viewitem.ws?obj=" + itemId);
        if (content == null) {
            return 0.0;
        }

        final String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("Current Guide Price")) {
                String rString = lines[i].replace("<h3>Current Guide Price <span title='", "");
                rString = rString.substring(rString.indexOf("'>") + 2, 20);
                rString = rString.replace(",", "");
                return Double.parseDouble(rString.trim());
            }
        }
        return 0.0;
    }

    public void handleTrade() {
        status = ("Handeling trade");
        int _lastTradedValue = 0;
        if(ctx.widgets.component(WIDGET_TRADE_MAIN, WIDGET_TRADE_MAIN_ACCEPT).visible()) {
            status = ("Trade screen open");
            while (tradeWait1 < 60) {
                lastTradedValue = 0;
                if(!inTrade()) {
                    break;
                }
                if(ctx.widgets.component(WIDGET_TRADE_SECOND, 0).visible()){
                    status = ("Got items, onto confirm");
                    lastTradedValue = _lastTradedValue;
                    break;
                }
                status = ("Waiting for items");
                Component offerComponent = ctx.widgets.component(WIDGET_TRADE_MAIN, INCOMING_OFFER_COMPONENT_ID);
                for(int componentNumber = 0; componentNumber <= 28; componentNumber++) {
                    if(offerComponent.component(componentNumber).itemId() > 0) {
                        status = "Accepting trade";
                        if(offerComponent.component(componentNumber).itemId() == 995) {
                            lastTradedValue += offerComponent.component(componentNumber).itemStackSize() * 1;
                        } else {
//                            lastTradedValue += getPrice(offerComponent.component(componentNumber).itemId());
                        }
                        ctx.widgets.component(WIDGET_TRADE_MAIN, WIDGET_TRADE_MAIN_ACCEPT).click();
                    }
                }
                _lastTradedValue = lastTradedValue;
                Condition.sleep(1000);
                tradeWait1++;
            }
        }
        Condition.sleep(1000);
        if(ctx.widgets.component(WIDGET_TRADE_SECOND, 0).visible()){
            tradeWait2 = 0;
            while (tradeWait2 < 60) {
                if(!inTrade()) {
                    break;
                }
                if(!ctx.widgets.component(WIDGET_TRADE_SECOND, 0).visible()){
                    status = ("Confirm screen closed");
                    break;
                }
                status = ("Confirm screen open");
                ctx.widgets.component(WIDGET_TRADE_SECOND, WIDGET_TRADE_SECOND_ACCEPT).click();
                Condition.sleep(1000);
                tradeWait2++;
            }
            ctx.widgets.component(WIDGET_TRADE_SECOND, WIDGET_TRADE_SECOND_DECLINE).click();
        }
        if (ctx.widgets.component(WIDGET_TRADE_MAIN, WIDGET_TRADE_MAIN_DECLINE).visible()) {
            ctx.widgets.component(WIDGET_TRADE_MAIN, WIDGET_TRADE_MAIN_DECLINE).click();
            status = ("Declining trade");
        }
    }

    @Override
    public void poll() {
        status = ("Loop" + loopCount);
        if(loopCount == 0) {
            startPosition =  ctx.players.local().tile();
        }
        loopCount++;
        if(inTrade()) {
            handleTrade();
        } else if(ctx.backpack.select().count() > 0) {
            if(!ctx.bank.opened()) {
                ctx.movement.step(bankTile);
                Condition.sleep(2000);
                ctx.camera.turnTo(bankTile);
                Condition.sleep(500);
                ctx.bank.open();
            } else {
                ctx.bank.depositInventory();
                Condition.sleep(200);
                ctx.bank.close();
                Condition.sleep(200);
            }
        } else {
            status = ("Not in trade");
            if(tradeAttempts >= 5) {
                tradeWithNameList.clear();
                tradeAttempts = 0;
            }
            if(!tradeWithNameList.isEmpty() && tradeAttempts < 5) {
                ctx.players.select();
                for (Player player : ctx.players) {
                    if (tradeWithNameList.contains(player.name())) {
                        int tradeOccurences = Collections.frequency(tradedWithPlayerNameList, player.name());
                        ctx.camera.turnTo(player);
                        // trade same player max 15 times
                        if(tradeOccurences < 15) {
                            lastTradedName = player.name();
                            tradeWithNameList.remove(player.name());
                            tradedWithPlayerNameList.add(lastTradedName);
                            status = ("Trade " + player.name());
                            player.interact(true, "Trade with", player.name());
                            tradeAttempts++;
                            status = ("In trade");
                            Condition.sleep(5000);
                            handleTrade();
                        }
                    }
                }
            } else {
                if(tradeAccepted) {
//                    doDance();
                    status = "Saying thanks";
                    typeMessage("TY " + lastTradedName.toLowerCase().replace(" ", " "));
                    tradeAccepted = false;
                } else {
                    if(startPosition.distanceTo(ctx.players.local().tile()) > 2) {
                        status = ("Walk to start position");
                        ctx.movement.step(startPosition);
                    }
                    Condition.sleep(1000);
                    if(!tradeWithNameList.isEmpty() && tradeAttempts < 5) {
                        return;
                    }
                    status = "Begging";
                    typeMessage("Need 1k or armor plz");
                }
            }
        }
        Condition.sleep(1000);
    }

    Font font = new Font("Verdana", Font.BOLD, 12);
    Color background = new Color(100, 100, 100, 150);
    public void repaint(Graphics graphics) {
        graphics.setFont(font);
        graphics.setColor(background);
        graphics.drawRect(0, 0, 250, 100);
        graphics.fillRect(0, 0, 250, 100);
        graphics.setColor(Color.red);
        graphics.getFont();
        graphics.drawString("Grand Exchange Money Beggar", 5, 15);
        graphics.drawString("Status: " + status, 5, 30);
        graphics.drawString("Money gained: " + moneyGained, 5, 45);
    }

}

