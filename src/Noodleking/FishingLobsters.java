package Noodleking;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.TilePath;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;

@Script.Manifest(name = "Fishing Lobsters",properties = "author=Noodleking; topic=1325074; client=4;",  description = "Fishes lobsters at Musa Point and banks them in Draynor Village")
public class FishingLobsters extends PollingScript<ClientContext> implements PaintListener {

    //dont look at this class, its a mess, too lazy to clean it up
    private final Color color1 = new Color(0, 0, 255, 100);
    private final Color color2 = new Color(0, 200, 255);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Perpetua", 1, 15);
    private final Font font2 = new Font("Consolas", 1, 14);
    private int LobsterCount = 0, LobsterProfit = 186, LobsterXP = 90;
    private DecimalFormat formatted = new DecimalFormat("#,###,###");
    private boolean[] counting = new boolean[28];
    private long Stopwatch = 0;
    private boolean lobster_pot = false, coins = false, fishing = false, banking = false;
    private Area Karamjafishing = new Area(new Tile(2914,3164), new Tile(2934,3184));
    private Area DraynorBank = new Area(new Tile(3087,3240), new Tile(3099,3252));
    private Area KaramjaDock = new Area(new Tile(2941,3141), new Tile(2957,3151));
    private Area PortSarimDock = new Area(new Tile(3022,3216), new Tile(3032, 3226));

    private Tile[] path_to_sailor_Karamja = {new Tile(2924, 3174), new Tile(2921, 3167),
            new Tile(2918, 3160), new Tile(2930, 3151), new Tile(2945, 3146),
    };
    private Tile[] path_to_draynor_bank = {new Tile(3027, 3221), new Tile(3028, 3232),
            new Tile(3041, 3242), new Tile(3041, 3257),
            new Tile(3052, 3265), new Tile(3062, 3273),
            new Tile(3070, 3261), new Tile(3081, 3250),
            new Tile(3093, 3243)
    };

    private TilePath to_sailor_Karamja, to_sailor_Port_Sarim, to_fishing_spot, to_draynor_bank;
    private boolean questfinished = false, Karamja = true, Draynor = true;

    public void start(){

        Component questchecker = ctx.widgets.widget(399).component(7).component(9);
        ctx.widgets.widget(161).component(53).click();
        Condition.sleep(100);
        if(questchecker.textColor()==65280)
            questfinished=true;
        ctx.widgets.widget(161).component(54).click();

        Arrays.fill(counting, Boolean.TRUE);
        for(int i = 0; i < ctx.inventory.select().count(); i++)
            counting[i]=false;

        to_draynor_bank = ctx.movement.newTilePath(path_to_draynor_bank);
        to_sailor_Port_Sarim = ctx.movement.newTilePath(path_to_draynor_bank).reverse();
        to_sailor_Karamja = ctx.movement.newTilePath(path_to_sailor_Karamja);
        to_fishing_spot = ctx.movement.newTilePath(path_to_sailor_Karamja).reverse();
    }

    @Override
    public void poll(){
        final State state = getState();
        if(state==null)
            return;
        else if(questfinished) {
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
                case WALKING_TO_DRAYNOR:
                    Walking_to_Draynor();
                    break;
            }
        }
    }

    public State getState(){
        int inventory_amount = ctx.inventory.select().id(377).count();
        boolean inBank = DraynorBank.contains(ctx.players.local().tile()), atFishingspot = Karamjafishing.contains(ctx.players.local().tile());
        if(atFishingspot) {
            if(inventory_amount == 26)
                return State.WALKING_TO_DRAYNOR;
            else
                return State.FISHING;
        }
        else if(inBank) {
            if (inventory_amount == 26)
                return State.BANKING;
            else
                return State.WALKING_TO_MUSA_POINT;
        }
        else if(!atFishingspot && !inBank){
            if(inventory_amount == 26)
                return State.WALKING_TO_DRAYNOR;
            else
                return State.WALKING_TO_MUSA_POINT;
        }
        return null;
    }

    public void Walking_to_Draynor(){
        if(!KaramjaDock.contains(ctx.players.local().tile()) && Karamja) {
            Condition.sleep(Random.nextInt(0,500));
            to_sailor_Karamja.traverse();
        }
        else if(KaramjaDock.contains(ctx.players.local().tile()) && Karamja){
            ctx.npcs.select().id(3648).poll().interact("Pay-fare");
            while(!ctx.objects.select().id(2084).poll().inViewport())
                Condition.sleep(500);
            while(!PortSarimDock.contains(ctx.players.local().tile())) {
                Condition.sleep(500);
                ctx.objects.select().id(2084).poll().click();
            }
            Karamja = false;
            Draynor = true;
        }
        else if(!DraynorBank.contains(ctx.players.local().tile()) && Draynor){
            Condition.sleep(Random.nextInt(0,500));
            to_draynor_bank.traverse();
        }
    }

    public void Walking_to_Musa_Point(){
        if(!PortSarimDock.contains(ctx.players.local().tile()) && Draynor) {
            Condition.sleep(Random.nextInt(0,500));
            to_sailor_Port_Sarim.traverse();
        }
        else if(PortSarimDock.contains(ctx.players.local().tile()) && Draynor){
            ctx.npcs.select().id(3645).poll().interact("Pay-fare");
            while(!ctx.objects.select().id(2082).poll().inViewport()){
                Condition.sleep(500); System.out.println("WTF");}
            while(!KaramjaDock.contains(ctx.players.local().tile())) {
                Condition.sleep(500);
                ctx.objects.select().id(2082).poll().click();
                System.out.println("WTF TIMES 2");
            }
            Draynor = false;
            Karamja = true;
        }
        else if(!Karamjafishing.contains(ctx.players.local().tile()) && Karamja){
            Condition.sleep(Random.nextInt(0,500));
            to_fishing_spot.traverse();
        }
    }

    public void Fishing(){
        Condition.sleep(Random.nextInt(200,2000));
        if(ctx.players.local().animation()==-1 || (System.currentTimeMillis()-Stopwatch)>180000) {
            Npc spot = ctx.npcs.select().id(1522).nearest().poll();
            spot.click();
            Stopwatch = System.currentTimeMillis();
        }
        Count();
    }

    public void Banking(){
        if(!ctx.bank.opened())
            ctx.bank.open();
        else if (!ctx.inventory.id(377).isEmpty()) {
            ctx.bank.deposit("Raw lobster", 26);
            ctx.bank.withdraw(995,60);
            Reset_Array();
        }
        else {
            ctx.bank.close();
            banking=false;
        }
    }

    public void Count() {
        for (int i = 0; i < 28; i++) {
            if (ctx.inventory.itemAt(i).id() == 377 && counting[i]) {
                LobsterCount++;
                counting[i] = false;
            }
        }
    }

    public void Reset_Array(){
        for(int i = 0; i < counting.length; i++)
            counting[i]=true;
    }

    @Override
    public void repaint(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        g.setColor(color1);
        g.fillRect(0, 55,  250, 106);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(0, 55, 250, 106);
        g.setFont(font1);
        g.drawString("Time: " + formated(this.getRuntime()), 5, 70);
        g.drawString("Lobsters: " + formatted.format(LobsterCount)+" ("+formatted.format(
                getPerHour(LobsterCount, getRuntime()))+" per/Hour)",5, 84);
        g.drawString("Profit per hour: " + formatted.format((getPerHour(LobsterCount, getRuntime())*LobsterProfit)),
                5, 98);
        g.drawString("Total Profit: " + formatted.format((LobsterCount*LobsterProfit)), 5, 112);
        g.drawString("Total XP: " + formatted.format((LobsterCount*LobsterXP)) + " (" + formatted.format((getPerHour(
                LobsterCount, getRuntime())*LobsterXP)) + " XP/Hour)", 5, 126);
        g.drawString("Status: "+ getState().toString(),5,140);
        g.drawString("Created by Noodleking",5,154);
    }

    String formated(long time) {
        final int sec = (int) (time / 1000),
                h = sec / 3600,
                m = sec / 60 % 60,
                s = sec % 60;
        return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
                + (s < 10 ? "0" + s : s);
    }

    public static int getPerHour(int in, long time) {
        return (int) ((in) * 3600000D / time);
    }

    private enum State{
            FISHING, WALKING_TO_DRAYNOR, BANKING, WALKING_TO_MUSA_POINT
        }

}

