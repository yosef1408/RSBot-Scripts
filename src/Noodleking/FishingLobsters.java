package Noodleking;

import org.powerbot.script.*;
import org.powerbot.script.GeItem;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Interactive;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.concurrent.Callable;

@Script.Manifest(name = "Fishing Lobsters/Swordfish",properties = "author=Noodleking, Terminator1; topic=1325074; client=4;",  description = "Fishes lobsters at Musa Point and banks them at the Monks of Entrana")
public class FishingLobsters extends PollingScript<ClientContext> implements PaintListener,MessageListener{

    private final int fishbounds[] = {-48, 48, 0, 0, -48, 48};int lxp = 90,sxp = 100,txp = 80,LobsterXP = 90,
            KaramjaSailor = 3648,SarimSailor = 3645, KaramjaGP = 2082, SarimGP = 2084, spotID = 1522,lobsters = 377,
            swordfish = 371,tuna = 359;
    private int xpgained = 0;
    private GeItem ge;
    private DecimalFormat formatted = new DecimalFormat("#,###,###");
    private int LobsterProfit = 0,SwordfishProfit = 0,TunaProfit = 0,LobsterCount = 0, SwordfishCount = 0,TunaCount = 0;
    private long Stopwatch = 0;
    private String interacting,input,tx[] = {"Lobsters","Swordfish+"};
    private Area Karamjafishing = new Area(new Tile(2914,3164), new Tile(2934,3184));
    private Area EntranaDeposit = new Area(new Tile(3041,3238), new Tile(3047,3233));
    private Area KaramjaDock = new Area(new Tile(2941,3141), new Tile(2957,3151));
    private Area PortSarimDock = new Area(new Tile(3022,3216), new Tile(3032, 3226));
    private Area KaramjaShip = new Area(new Tile(2952,3144,1), new Tile(2959, 3140,1));
    private Area PortSarimShip = new Area(new Tile(3031, 3221, 1), new Tile(3036, 3214, 1));
    private boolean lobster = true,fishing = false;
    private final Color color = new Color(0, 255, 0, 150);

    private final Tile[] path_to_sailor_Karamja = {
            new Tile(2924, 3178),
            new Tile(2924, 3173), new Tile(2923, 3169),
            new Tile(2923, 3164), new Tile(2920, 3162),
            new Tile(2919, 3158), new Tile(2916, 3156),
            new Tile(2917, 3152), new Tile(2922, 3152),
            new Tile(2926, 3151), new Tile(2931, 3151),
            new Tile(2933, 3148), new Tile(2937, 3147),
            new Tile(2941, 3146), new Tile(2946, 3146),
            new Tile(2951, 3146), new Tile(2956, 3146)
    };
    private final Tile[] path_to_entrana_monks = {
            new Tile(3027, 3218),
            new Tile(3028, 3222), new Tile(3028, 3227),
            new Tile(3028, 3232), new Tile(3029, 3236),
            new Tile(3034, 3236), new Tile(3039, 3236),
            new Tile(3044, 3235)
    };

    private TilePath to_sailor_Karamja, to_sailor_Port_Sarim, to_fishing_spot, to_entrana_monks;
    private boolean questfinished = false, Karamja = true, Draynor = true;

    public void start(){
        ge = new org.powerbot.script.rt4.GeItem(lobsters);
        LobsterProfit = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(swordfish);
        SwordfishProfit = ge.price;
        ge = new org.powerbot.script.rt4.GeItem(tuna);
        TunaProfit = ge.price;
        Component questchecker = ctx.widgets.widget(399).component(7).component(9);
        input = (String) JOptionPane.showInputDialog(null,"What do you want to fish:",
                "The choice of fishing",JOptionPane.QUESTION_MESSAGE,null,tx,0);
        if(input.equals("Lobsters")) {
            interacting = "Cage";
        } else {
            interacting = "Harpoon";
            lobster = false;
        }
        ctx.widgets.widget(161).component(53).click();
        Condition.sleep(100);
        if(questchecker.textColor()==65280)
            questfinished=true;
        ctx.widgets.widget(161).component(54).click();
        to_entrana_monks = ctx.movement.newTilePath(path_to_entrana_monks);
        to_sailor_Port_Sarim = ctx.movement.newTilePath(path_to_entrana_monks).reverse();
        to_sailor_Karamja = ctx.movement.newTilePath(path_to_sailor_Karamja);
        to_fishing_spot = ctx.movement.newTilePath(path_to_sailor_Karamja).reverse();
    }

    @Override
    public void poll(){
        final State state = getState();
        if(state!=null && questfinished) {
            switch (state) {
                case BANKING:
                    Banking();
                    break;
                case WALKING_TO_MUSA_POINT:
                    Walking_to_Musa_Point();
                    break;
                case FISHING:
                    Fishing();
                    break;
                case DROP:
                    Drop();
                    break;
                case WALKING_TO_DRAYNOR:
                    Walking_to_Draynor();
                    break;
            }
        }
    }

    private State getState(){
        int inventory_amount = ctx.inventory.select().count();
        boolean inBank = EntranaDeposit.contains(ctx.players.local().tile()),
                atFishingspot = Karamjafishing.contains(ctx.players.local().tile());
        if(atFishingspot) {
            if(inventory_amount == 28)
                if(ctx.inventory.select().id(tuna).count()==0)
                    return State.WALKING_TO_DRAYNOR;
                else
                    return State.DROP;
            else
                return State.FISHING;
        }
        else if(inBank) {
            if (inventory_amount == 28)
                return State.BANKING;
            else
                return State.WALKING_TO_MUSA_POINT;
        }
        else{
            if(inventory_amount == 28)
                return State.WALKING_TO_DRAYNOR;
            else
                return State.WALKING_TO_MUSA_POINT;
        }
    }

    private void Walking_to_Draynor(){
        if(!KaramjaDock.contains(ctx.players.local().tile()) && Karamja) {
            Condition.sleep(Random.nextInt(1000,1500));
            to_sailor_Karamja.traverse();
        }
        else if(KaramjaDock.contains(ctx.players.local().tile()) && Karamja){
            Condition.sleep(Random.nextInt(100,500));
            while(!ctx.objects.select().id(SarimGP).poll().inViewport() || ctx.npcs.select().id(KaramjaSailor).poll().inViewport()){
                ctx.npcs.select().id(KaramjaSailor).poll().interact("Pay-fare");
                Condition.sleep(1000);
            }
            while(!PortSarimShip.contains(ctx.players.local().tile()))
                Condition.sleep(500);
            while(PortSarimShip.contains(ctx.players.local().tile())) {
                ctx.objects.select().id(SarimGP).poll().click();
                Condition.sleep(1000);
            }
            Karamja = false;
            Draynor = true;
        }
        else if(!EntranaDeposit.contains(ctx.players.local().tile()) && Draynor){
            Condition.sleep(Random.nextInt(1000,1500));
            to_entrana_monks.traverse();
        }
    }

    private void Walking_to_Musa_Point(){
        if(!PortSarimDock.contains(ctx.players.local().tile()) && Draynor) {
            Condition.sleep(Random.nextInt(1000,1500));
            to_sailor_Port_Sarim.traverse();
        }
        else if(PortSarimDock.contains(ctx.players.local().tile()) && Draynor){
            Condition.sleep(Random.nextInt(100,500));
            while(!ctx.objects.select().id(KaramjaGP).poll().inViewport() || ctx.npcs.select().id(SarimSailor).poll().inViewport()){
                Condition.sleep(1000);
                ctx.npcs.select().id(SarimSailor).poll().interact("Pay-fare");
            }
            while(!KaramjaShip.contains(ctx.players.local().tile())) {
                Condition.sleep(500);
            }
            while(KaramjaShip.contains(ctx.players.local().tile())) {
                Condition.sleep(1000);
                ctx.objects.select().id(KaramjaGP).poll().click();
            }
            Draynor = false;
            Karamja = true;
        }
        else if(!Karamjafishing.contains(ctx.players.local().tile()) && Karamja){
            Condition.sleep(Random.nextInt(1000,1500));
            to_fishing_spot.traverse();
        }
    }

    private boolean ManhattenDistance(){
        if(ctx.players.local().tile().distanceTo(ctx.npcs.select().id(spotID).nearest().poll().tile())==1)
            return true;
        return false;
    }

    private void Fishing(){
        if(((!ManhattenDistance() || (ctx.players.local().animation() == -1)) && !ctx.players.local().inMotion()) ||
                (System.currentTimeMillis()-Stopwatch)>Random.nextInt(120000,180000)) {
            fishing = false;
            Npc spot = ctx.npcs.select().id(spotID).each(Interactive.doSetBounds(fishbounds)).nearest().poll();
            spot.interact(interacting);
            Stopwatch = System.currentTimeMillis();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return fishing;
                }
            },75,10);
        }
    }

    public void Drop() {
        for(Item item: ctx.inventory.select()) {
            if(ctx.inventory.selectedItemIndex()!=-1)
                ctx.inventory.select().shuffle().poll().click();
            if(item.id() == tuna)
                item.interact("Drop");
        }
    }

    private void Banking(){
        Component item = null;
        int fish = -1;
        if(!ctx.widgets.widget(192).component(2).inViewport() && ctx.objects.select().id(26254).nearest().poll().inViewport()) {
                ctx.objects.select().id(26254).within(EntranaDeposit).nearest().poll().click();
                Condition.sleep(Random.nextInt(1000, 1500));
        } else {
            for (int i = 0; i < 28; i++) {
                System.out.println(ctx.widgets.widget(192).component(2).component(i).itemId() + " " + ctx.widgets.widget(192).component(2).component(i).modelId());
                if (lobster) {
                    if (ctx.widgets.widget(192).component(2).component(i).itemId() == lobsters) {
                        fish = i;
                        break;
                    }
                } else {
                    if (ctx.widgets.widget(192).component(2).component(i).itemId() == swordfish) {
                        fish = i;
                        break;
                    }
                }
            }
        }

        //Yes I could use i instead of fish,however it won't support a future update
        item = ctx.widgets.widget(192).component(2).component(fish);
        item.interact("Deposit-all");
    }

    @Override
    public void repaint(Graphics gr) {

        Color color1 = new Color(0, 0, 255, 100), color2 = new Color(0, 200, 255);
        BasicStroke stroke1 = new BasicStroke(1);
        Font font1 = new Font("Perpetua", 1, 15), font2 = new Font("Consolas", 1, 14);

        Graphics2D g = (Graphics2D) gr;
        if(getState().toString().equals("FISHING")) {
            g.setColor(color);
            g.drawPolygon(ctx.npcs.select().id(spotID).nearest().poll().tile().matrix(ctx).bounds());
        }
        g.setColor(color1);
        g.fillRect(0, 55,  250, 104);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(0, 55, 250, 104);
        g.setFont(font1);
        g.drawString("Time: " + formated(this.getRuntime()), 5, 70);
        if(lobster) {
            g.drawString("Lobsters:" + formatted.format(LobsterCount) + "(" + formatted.format(
                    getPerHour(LobsterCount, getRuntime())) + " per/Hour)", 5, 84);
            g.drawString("Total Profit: " + formatted.format((LobsterCount*LobsterProfit)) + " (" + formatted.format
                    ((getPerHour((LobsterCount*LobsterProfit), getRuntime()))) + " GP/Hour)", 5, 98);
        } else {
            g.drawString("Swordfish:" + formatted.format(SwordfishCount) + "(" + formatted.format(
                    getPerHour(SwordfishCount, getRuntime())) + " )", 5, 84);
            g.drawString("Total Profit: " + formatted.format((SwordfishCount*SwordfishProfit)+(TunaCount*TunaProfit)) + " (" + formatted.format
                    ((getPerHour((SwordfishCount*SwordfishProfit)+(TunaCount*TunaProfit), getRuntime()))) + " GP/Hour)", 5, 98);
        }
        g.drawString("Total XP: " + formatted.format(xpgained) + " (" + formatted.format((getPerHour(xpgained, getRuntime()))) + " XP/Hour)", 5, 112);
        g.drawString("Status: "+ getState().toString(),5,126);
        g.drawString("Created by Noodleking",5,140);
        g.drawString("Co-author:Terminator1",5,155);
    }

    private String formated(long time) {
        final int sec = (int) (time / 1000),
                h = sec / 3600,
                m = sec / 60 % 60,
                s = sec % 60;
        return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
                + (s < 10 ? "0" + s : s);
    }

    private static int getPerHour(int in, long time) {
        return (int) ((in) * 3600000D / time);
    }

    @Override
    public void messaged(MessageEvent messageEvent) {
        String txt = messageEvent.text().toLowerCase();
        if(lobster) {
            if (txt.contains("you attempt")) {
                fishing = true;
            } else if(txt.contains("you catch")) {
                LobsterCount++;
                xpgained+=lxp;
            }
        } else {
            if (txt.contains("you start")) {
                fishing = true;
            } else if(txt.contains("you catch a s")){
                SwordfishCount++;
                xpgained+=sxp;
            } else if(txt.contains("you catch a s")){
                xpgained+=txp;
            }
        }
    }

    private enum State{
        FISHING, WALKING_TO_DRAYNOR, BANKING, WALKING_TO_MUSA_POINT, DROP
    }

}


