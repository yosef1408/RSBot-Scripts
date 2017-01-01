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

    private int LobsterCount = 0, LobsterProfit = 186, LobsterXP = 90, KaramjaSailor = 3648,SarimSailor = 3645, KaramjaGP = 2082, SarimGP = 2084, spotID = 1522;
    private DecimalFormat formatted = new DecimalFormat("#,###,###");
    private boolean[] counting = new boolean[28];
    private long Stopwatch = 0;
    private Area Karamjafishing = new Area(new Tile(2914,3164), new Tile(2934,3184));
    private Area EntranaDeposit = new Area(new Tile(3043,3238), new Tile(3047,3233));
    private Area KaramjaDock = new Area(new Tile(2941,3141), new Tile(2957,3151));
    private Area PortSarimDock = new Area(new Tile(3022,3216), new Tile(3032, 3226));
    private Area KaramjaShip = new Area(new Tile(2952,3144,1), new Tile(2959, 3140,1));
    private Area PortSarimShip = new Area(new Tile(3031, 3221, 1), new Tile(3036, 3214, 1));

    private Tile[] path_to_sailor_Karamja;
    private Tile[] path_to_entrana_monks;

    private TilePath to_sailor_Karamja, to_sailor_Port_Sarim, to_fishing_spot, to_entrana_monks;
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

        Initiate_paths();
        to_entrana_monks = ctx.movement.newTilePath(path_to_entrana_monks);
        to_sailor_Port_Sarim = ctx.movement.newTilePath(path_to_entrana_monks).reverse();
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
        boolean inBank = EntranaDeposit.contains(ctx.players.local().tile()),
                atFishingspot = Karamjafishing.contains(ctx.players.local().tile());
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

    public void Walking_to_Musa_Point(){
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

    public void Fishing(){
        Condition.sleep(Random.nextInt(750,2000));
        if(ctx.players.local().animation()==-1 || (System.currentTimeMillis()-Stopwatch)>180000) {
            Npc spot = ctx.npcs.select().id(spotID).nearest().poll();
            spot.interact("Cage");
            Stopwatch = System.currentTimeMillis();
        }
        Count();
    }

    public void Banking(){
        Component item = ctx.widgets.widget(192).component(2).component(2);
        Component item2 = ctx.widgets.widget(192).component(2).component(3);
        if(ctx.objects.select().id(26254).nearest().poll().inViewport()){
            if(!item.inViewport()) {
                ctx.objects.select().id(26254).within(EntranaDeposit).nearest().poll().click();
                Condition.sleep(Random.nextInt(1000, 1500));
            }
            else if(item.itemId()==377){
                item.interact("Deposit-All");
                Reset_Array();
            }
            else if(item2.itemId()==377){
                item2.interact("Deposit-All");
            Reset_Array();
            }
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

    public void Initiate_paths(){
        path_to_sailor_Karamja = new Tile[] {new Tile(2924, 3178),
                new Tile(2924, 3173), new Tile(2923, 3169),
                new Tile(2923, 3164), new Tile(2920, 3162),
                new Tile(2919, 3158), new Tile(2916, 3156),
                new Tile(2917, 3152), new Tile(2922, 3152),
                new Tile(2926, 3151), new Tile(2931, 3151),
                new Tile(2933, 3148), new Tile(2937, 3147),
                new Tile(2941, 3146), new Tile(2946, 3146),
                new Tile(2951, 3146), new Tile(2956, 3146)
        };

        path_to_entrana_monks = new Tile[] { new Tile(3027, 3218),
                new Tile(3028, 3222), new Tile(3028, 3227),
                new Tile(3028, 3232), new Tile(3029, 3236),
                new Tile(3034, 3236), new Tile(3039, 3236),
                new Tile(3044, 3235)
        };
    }

    @Override
    public void repaint(Graphics gr) {

        Color color1 = new Color(0, 0, 255, 100), color2 = new Color(0, 200, 255);
        BasicStroke stroke1 = new BasicStroke(1);
        Font font1 = new Font("Perpetua", 1, 15), font2 = new Font("Consolas", 1, 14);

        Graphics2D g = (Graphics2D) gr;
        g.setColor(color1);
        g.fillRect(0, 55,  250, 92);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(0, 55, 250, 92);
        g.setFont(font1);
        g.drawString("Time: " + formated(this.getRuntime()), 5, 70);
        g.drawString("Lobsters: " + formatted.format(LobsterCount)+" ("+formatted.format(
                getPerHour(LobsterCount, getRuntime()))+" per/Hour)",5, 84);
        g.drawString("Total Profit: " + formatted.format((LobsterCount*LobsterProfit)) + " (" + formatted.format
                ((getPerHour(LobsterCount, getRuntime())*LobsterProfit)) + " XP/Hour)", 5, 98);
        g.drawString("Total XP: " + formatted.format((LobsterCount*LobsterXP)) + " (" + formatted.format((getPerHour(
                LobsterCount, getRuntime())*LobsterXP)) + " XP/Hour)", 5, 112);
        g.drawString("Status: "+ getState().toString(),5,126);
        g.drawString("Created by Noodleking",5,140);
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

