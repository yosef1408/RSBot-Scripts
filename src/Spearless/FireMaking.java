package Spearless;

import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.Random;

@Script.Manifest(name = "FireMaking", properties = "author=Spearless; topic=1333740; client=4;", description = "Firemaking Varrock West baank. Oak, Willow,Yew,Maple and normal Logs")
public class FireMaking extends PollingScript<ClientContext> implements MessageListener,PaintListener {

    int switcher;
    String logs;
    int problem=0;
    int LogsID[]={1278};
    int tinderbox= 590;
    int logsBurned;
    int LOGSIDINV= 1511;
    Color color = new Color(0xDF711C);
    Font font = new Font(("Arial"), Font.BOLD, 16);
    int  FMInitLevel, hours, seconds, minutes, FMlevel,FMExpInit;
    double runTime;
    long initime;
    String status;
    Tile tile[]={new Tile(3207,3429)};
    Area CHOPPER_AREA=new Area(new Tile(3140,3466),new Tile(3171,3449));
    Area TREE_AREA=new Area(new Tile(3151,3457),new Tile(3158,3450));
    Area WALKING_AREA= new Area(new Tile(3156,3459),new Tile(3178,3437));
    Area FIRIN_AREA= new Area(new Tile(3169,3433),new Tile(3214,3423));
    Area START_AREA=new Area(new Tile(3205,3431), new Tile(3213,3427));
    Tile tileToBank[]= {new Tile(3182,3432), new Tile(3183,3439)};
    Tile tiletoLight[]= {new Tile(3177,3429), new Tile(3186,3430), new Tile(3199,3429), new Tile(3209,3428)};
    Tile TILE_TO_CHOP_AREA[]={new Tile(3173,3436), new Tile(3173,3443),new Tile(3165,3451), new Tile(3159,3451)};
    Tile TILE_TO_FIRING_AREA[]={new Tile(3159,3450),new Tile(3164,3450),new Tile(3171,3443),new Tile(3176,3429)};
    GameObject tree= ctx.objects.select().id(LogsID).nearest().poll();
    int x= ctx.players.local().tile().x();
    int y= ctx.players.local().tile().y();
    int lighter=1;

    public void frame(){

        final JFrame frame = new JFrame();
        frame.setSize(300, 400);
        frame.setVisible(true);
        final JButton CF = new JButton("Chop and firemaking");
        final JButton BF = new JButton("Bank and firemaking");
        final JButton OLogs= new JButton("Oak logs");
        final JButton Nlogs= new JButton("Logs");
        final JButton WLogs = new JButton("Willow logs");
        final JButton YLogs= new JButton("Yew logs");
        final JButton MLogs= new JButton("Maple logs");
        JPanel panel = new JPanel();

        panel.add(CF);
        panel.add(BF);

        panel.add(Nlogs);
        panel.add(OLogs);
        panel.add(WLogs);
        panel.add(YLogs);
        panel.add(MLogs);
        frame.add(panel);
        MLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(MLogs.isEnabled()){
                    LOGSIDINV=1517;
                    logs="Maple logs";
                }
            }
        });
        YLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(YLogs.isEnabled()){
                    LOGSIDINV=1515;
                    logs="Yew logs";
                }
            }
        });
        CF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(CF.isEnabled()){
                    switcher=1;
                }
            }
        });

        BF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(BF.isEnabled()){
                    switcher=2;
                    log.info(""+switcher);
                }
            }
        });

        OLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(OLogs.isEnabled()){
                    LOGSIDINV=1521;
                    logs="Oak logs";
                }
            }
        });

        Nlogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(Nlogs.isEnabled()){
                    LOGSIDINV=1511;
                    logs="Logs";

                }
            }
        });

        WLogs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(WLogs.isEnabled()){
                    logs="Willow logs";
                    LOGSIDINV=1519;
                    log.info("Willow selected");
                }
            }
        });

    }
    public void start(){
        try {
            checkingFiremakingSkill();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        frame();
        int x=ctx.input.speed(100);
        initime=System.currentTimeMillis();
        FMInitLevel =ctx.skills.level(Constants.SKILLS_FIREMAKING);
        FMExpInit= ctx.skills.experience(Constants.SKILLS_FIREMAKING);
        status="Setting up";
        log.info(""+x);

    }
    public void moveToInv() {

        Random randomINVX = new Random();
        Random randomINVY = new Random();
        int invx = randomINVX.nextInt(30) + 627;
        int invy = randomINVY.nextInt(30) + 169;
        ctx.input.move(invx, invy);
        ctx.input.click(true);
    }
    void checkingFiremakingSkill() throws InterruptedException {
        Random random = new Random();
        int speed= random.nextInt(3);
        int x = random.nextInt(30) + 560;
        int y = random.nextInt(31) + 170;
        ctx.input.move(x, y);
        ctx.input.click(true);

        int xsk = random.nextInt(60) + 674;
        int ysk = random.nextInt(30) + 333;
        ctx.input.move(xsk, ysk);

        Random randomsleep = new Random();
        int sleepe = randomsleep.nextInt(2000);
        Thread.sleep(2000 + sleepe);

        moveToInv();
        log.info(""+ x+xsk+ysk);

    }
    void changeMouseSpeed(){
        java.util.Random random= new Random();
        int x= random.nextInt(15);
        int mouseSpeed= random.nextInt(5);
        Random random1= new Random();
        Random random2= new Random();
        int x1= random1.nextInt(800);
        int y1=random2.nextInt(800);
        switch(x){
            case 1:
                ctx.input.move(x1,y1);
                break;
            case 3:
                ctx.input.move(x1,y1);
                break;
            default:
                ctx.input.speed(mouseSpeed);
                break;
        }



    }

    void movingTile(){
        log.info("Moving tile");
        int x= ctx.players.local().tile().x();
        int y= ctx.players.local().tile().y();
        Tile MOVING_TILE[]={new Tile(x-2,y+1)};
        TilePath path= ctx.movement.newTilePath(MOVING_TILE);
        path.randomize(2,0);
        path.traverse();
        lighter=1;
    }
    void movePlayer(){
        TilePath path= ctx.movement.newTilePath(tiletoLight);
        path.randomize(1,1);
        path.traverse();

    }
    public void poll() {

        switch (state()) {
            case LIGHT:

                changeMouseSpeed();
                ctx.input.speed(90);
                if( ctx.players.local().animation()==-1 && lighter==1||ctx.players.local().animation()==-1&& lighter==2|| START_AREA.contains(ctx.players.local()) && ctx.players.local().animation()==-1&& (lighter==1||lighter==2)|| lighter!=1 && lighter!=2 && ctx.players.local().inMotion()==false && ctx.players.local().animation()==-1&&ctx.players.local().orientation()==6 && ctx.inventory.select().count()>2
                        ||lighter!=1 && lighter!=2 && ctx.players.local().inMotion()==false&&START_AREA.contains(ctx.players.local()) && ctx.players.local().animation()==-1&&(ctx.players.local().orientation()==0||ctx.players.local().orientation()==7||ctx.players.local().orientation()==5) && ctx.inventory.select().count()>2) {
                    status = "Lighting";
                    Item tinder = ctx.inventory.select().id(tinderbox).poll();
                    tinder.interact("Use");
                    Item log1 = ctx.inventory.select().id(LOGSIDINV).poll();
                    log1.interact("Use");
                    lighter=0;
                }
                if(problem==1) {
                    movingTile();

                    problem=0;

                    log.info("Problem =0");
                }
                problem=0;
                break;
            case GOLIGHTAREA:
                changeMouseSpeed();
                status="Going to lighting area";
                movePlayer();

                break;

            case GOTREEAREA:

                status="Going to tree area";
                TilePath path= ctx.movement.newTilePath(TILE_TO_CHOP_AREA);
                path.randomize(2,2);
                path.traverse();

                break;
            case CHOP:

                changeMouseSpeed();
                status="Chopping";
                tree= ctx.objects.select().id(LogsID).nearest().poll();
                if( !ctx.players.local().inMotion()&&ctx.players.local().animation()==-1&&CHOPPER_AREA.contains(ctx.players.local())) {
                    tree.interact("Chop");

                    if (!tree.inViewport()) {
                        ctx.camera.turnTo(tree);
                    }
                }
                break;
            case COMEBACKTOFIRING:

                changeMouseSpeed();
                status="Going to lighting zone";
                TilePath path1= ctx.movement.newTilePath(TILE_TO_FIRING_AREA);
                path1.randomize(2,2);
                path1.traverse();
                break;
            case BANK:
                changeMouseSpeed();
                status="Getting Logs";
                TilePath path3= ctx.movement.newTilePath(tileToBank);
                path3.randomize(2,2);
                path3.traverse();
                GameObject banker= ctx.objects.select().id(7409).nearest().poll();
                if(banker.inViewport() && ctx.inventory.select().count()<28&& !ctx.players.local().inMotion()){
                    banker.interact("");

                }

                if(ctx.bank.open() && ctx.inventory.select().count()<28) {

                    Item extractLogs = ctx.bank.select().name(logs).poll();
                    extractLogs.interact("Withdraw-All");

                }
                break;

        }
    }
    private State state(){
        if ( ctx.inventory.select().count()> 2&&START_AREA.contains(ctx.players.local())|| ctx.inventory.select().count()<28 &&FIRIN_AREA.contains(ctx.players.local())&& ctx.inventory.select().count()>2) {
            log.info("Burning");
            return State.LIGHT;
        }else if(!START_AREA.contains(ctx.players.local())&& ctx.inventory.select().count()==28&& !CHOPPER_AREA.contains(ctx.players.local()) && !WALKING_AREA.contains(ctx.players.local())){
            log.info("Going light area");
            return State.GOLIGHTAREA;
        }else if(ctx.inventory.select().count()==2&& !CHOPPER_AREA.contains(ctx.players.local())&& switcher==1){
            return State.GOTREEAREA;
        }else if(ctx.inventory.select().count()<28 && CHOPPER_AREA.contains(ctx.players.local())&& switcher==1){
            log.info("Chop");
            return State.CHOP;
        }else if(ctx.inventory.select().count()==28 && !FIRIN_AREA.contains(ctx.players.local())&& switcher==1){
            log.info("Come back to firing");
            return State.COMEBACKTOFIRING;
        }else if(switcher==2&&ctx.inventory.select().count()<3){

            return  State.BANK;
        }else{
            log.info("NOTHING");
            return State.NOTHING;
        }
    }
    private enum State{
        LIGHT,GOLIGHTAREA,NOTHING,GOTREEAREA,CHOP,COMEBACKTOFIRING,BANK;

    }
    public void repaint(Graphics g1) {
        Graphics2D g2 = (Graphics2D) g1;
        int posx= (int) ctx.input.getLocation().getX();
        int posy= (int) ctx.input.getLocation().getY();
        g2.setColor(Color.RED);
        g2.drawLine(posx,posy-10,posx,posy+10);
        g2.drawLine(posx-10,posy,posx+10,posy);
        g2.setColor(Color.ORANGE);
        g2.drawOval(posx-9,posy-9,18,18);

        int currentExp = ctx.skills.experience(Constants.SKILLS_FIREMAKING);
        int currLevel = ctx.skills.level(Constants.SKILLS_FIREMAKING);
        int logsToNextLevel = (ctx.skills.experienceAt(currLevel + 1) - currentExp) / 90;
        int expGained= currentExp-FMExpInit;
        FMlevel = currLevel -FMInitLevel;
        hours = (int) ((System.currentTimeMillis() - initime) / 3600000);
        minutes = (int) ((System.currentTimeMillis() - initime) / 60000 % 60);
        seconds = (int) ((System.currentTimeMillis() - initime) / 1000) % 60;
        runTime = (double) (System.currentTimeMillis() - initime) / 3600000;

        g1.setColor(color);
        g1.fillRect(8,340,515,140);
        long thickness = 4;
        BasicStroke basic= new BasicStroke(thickness);
        g2.setColor(Color.black);
        g2.setStroke(basic);
        g2.drawRect(8, 340, 515, 140);

        g1.setColor(Color.BLACK);
        g1.setFont(font);
        g1.drawString("Levels gained : " + FMlevel, 20, 375);
        g1.drawString("Time passed : " + hours + " : " + minutes + " : " + seconds, 20, 400);
        g1.drawString("Experience gained : " +expGained,20,425);
        g1.drawString("Logs to next level: " + logsToNextLevel, 20, 450);
        g1.drawString("Logs lighted:  " + logsBurned, 20, 475);
        g1.drawString("Version 1.0",335,375);
        g1.drawString("Spearless", 335,400);
        g1.setColor(color);
        g1.fillRect(290, 15, 225, 40);
        g1.setColor(Color.BLACK);
        g1.drawString("Status : " +status , 295, 50);

        g2.setColor(Color.red);
        g2.setStroke(basic);
        g2.drawRect(290, 15, 225, 40);


    }
    public void messaged(MessageEvent me) {
        String msg = me.text();
        if (msg.contains("light a fire here")) {
            problem++;
            log.info("Problem +1");
            y++;
        }
        else if(me.type()==105)
        {
            logsBurned++;
            lighter++;
        }
    }
}
