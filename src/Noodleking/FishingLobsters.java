package Fishing;

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

    private int LobsterCount = 0, LobsterProfit = 186, LobsterXP = 90;
    private DecimalFormat formatted = new DecimalFormat("#,###,###");
    private boolean[] counting = new boolean[28];
    private long Stopwatch = 0;
    private boolean not_enough_coins = false;
    private Area Karamjafishing = new Area(new Tile(2914,3164), new Tile(2934,3184));
    private Area DraynorBank = new Area(new Tile(3090,3246), new Tile(3094,3240));
    private Area KaramjaDock = new Area(new Tile(2941,3141), new Tile(2957,3151));
    private Area PortSarimDock = new Area(new Tile(3022,3216), new Tile(3032, 3226));

    private Tile[] path_to_sailor_Karamja;
    private Tile[] path_to_draynor_bank;

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

        initiate_paths();
        to_draynor_bank = ctx.movement.newTilePath(path_to_draynor_bank);
        to_sailor_Port_Sarim = ctx.movement.newTilePath(path_to_draynor_bank).reverse();
        to_sailor_Karamja = ctx.movement.newTilePath(path_to_sailor_Karamja);
        to_fishing_spot = ctx.movement.newTilePath(path_to_sailor_Karamja).reverse();
    }

    @Override
    public void poll(){
        final State state = getState();
        if(state==null || not_enough_coins)
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
            Condition.sleep(Random.nextInt(1000,1500));
            to_sailor_Karamja.traverse();
        }
        else if(KaramjaDock.contains(ctx.players.local().tile()) && Karamja){
            Condition.sleep(Random.nextInt(100,500));
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
            Condition.sleep(Random.nextInt(1000,1500));
            to_draynor_bank.traverse();
        }
    }

    public void Walking_to_Musa_Point(){
        if(!PortSarimDock.contains(ctx.players.local().tile()) && Draynor) {
            Condition.sleep(Random.nextInt(1000,1500));
            to_sailor_Port_Sarim.traverse();
        }
        else if(PortSarimDock.contains(ctx.players.local().tile()) && Draynor){
            Condition.sleep(Random.nextInt(100,500));
            ctx.npcs.select().id(3645).poll().interact("Pay-fare");
            while(!ctx.objects.select().id(2082).poll().inViewport()){
                Condition.sleep(500);
            }
            while(!KaramjaDock.contains(ctx.players.local().tile())) {
                Condition.sleep(500);
                ctx.objects.select().id(2082).poll().click();
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
        Condition.sleep(Random.nextInt(200,2000));
        if(ctx.players.local().animation()==-1 || (System.currentTimeMillis()-Stopwatch)>180000) {
            Npc spot = ctx.npcs.select().id(1522).nearest().poll();
            spot.click();
            Stopwatch = System.currentTimeMillis();
        }
        Count();
    }

    public void Banking(){
        if(!ctx.bank.opened()){
            ctx.objects.select().id(6943).within(DraynorBank).nearest().poll().click();
            Condition.sleep(Random.nextInt(1000, 1500));
        }
        else if (!ctx.inventory.id(377).isEmpty()) {
            ctx.bank.deposit("Raw lobster", 26);
            if(ctx.bank.id(995).poll().stackSize() >= 60)
                ctx.bank.withdraw(995,60);
            else
                not_enough_coins = true;
            Reset_Array();
        }
        else
            ctx.bank.close();
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

    public void initiate_paths(){
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

        path_to_draynor_bank = new Tile[] { new Tile(3027, 3218),
                new Tile(3028, 3222), new Tile(3028, 3227),
                new Tile(3028, 3232), new Tile(3029, 3236),
                new Tile(3034, 3236), new Tile(3039, 3236),
                new Tile(3042, 3238), new Tile(3042, 3243),
                new Tile(3044, 3246), new Tile(3049, 3246),
                new Tile(3052, 3248), new Tile(3054, 3251),
                new Tile(3056, 3254), new Tile(3058, 3257),
                new Tile(3059, 3261), new Tile(3060, 3265),
                new Tile(3060, 3270), new Tile(3062, 3273),
                new Tile(3065, 3275), new Tile(3069, 3276),
                new Tile(3071, 3273), new Tile(3072, 3269),
                new Tile(3074, 3266), new Tile(3074, 3261),
                new Tile(3076, 3258), new Tile(3078, 3255),
                new Tile(3079, 3251), new Tile(3082, 3250),
                new Tile(3085, 3249), new Tile(3089, 3249),
                new Tile(3092, 3247), new Tile(3093, 3244)
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
        g.drawString("Total Profit: " + formatted.format((LobsterCount*LobsterProfit)), 5, 98);
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
