package Spearless;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import java.awt.*;


@Script.Manifest(name = "Hunter", properties = "author=Spearless; topic=1333332; client=4;", description = "The objective of the script is for new accounts to level up Mining, Woodcutting. Start at Lumbridge with an axe, a pickaxe and a net, nothing else")
public class Hunter extends PollingScript<ClientContext> implements MessageListener,PaintListener {

    private int butterfly[]={10018, 10016, 10014};
    private int butterflyItem=10014;
    private int snaredUsed=9344;
    private int bones=526;
    private int snaredTaken=9373;
    private int birdSnareItem=10006;
    private int blubirdSnare[]={9345, 9346, 9379, 9344, 9373, 9374, 9375};
    private int snare[]={9345, 9346, 9379,9344};
    private int snakeTakenBlue[]={9375,9348};
    private int animal = 0;
    private int chicken=9978;
    private int npcButter=5555;
    private int kebbitID=5531;
    private int kebitCatched=1342;
    private int itemsSal[]={303,954};
    private int salTrap=8734;
    private int salmItemInv=10146;
    private int salmTree=8732;
    private int boxTakenID=9385;
    private int boxtrappedID[]={9382,9383};
    private int boxNormalID=9380;
    private int boxteapId=10008;
    private int boxtakenRed=9385;
    private int boxtrappedRed[]={9382,9383};
    private int boxnormalRed=9380;
    private int boxteapRed=10008;
    private int catches;
    private int redCatches;
    private int  HuInitLevel, hours, seconds, minutes, Hulevel,HuExpInit;
    private double runTime;
    private long initime;
    private int centerChecker = 0;
    private GameObject Snare = ctx.objects.select().id(9345).poll();
    private GameObject SnareUsed = ctx.objects.select().id(snaredUsed).poll();
    private GameObject snaketaken = ctx.objects.select().id(snaredTaken).poll();
    private GameObject bluebirdSna = ctx.objects.select().id(blubirdSnare).poll();

    private Boolean errorDropping = false;
    private Boolean moveOrNot = false;
    private Boolean RedCB=true;
    private Boolean champsB=false;

    private Font font = new Font(("Arial"), Font.BOLD, 16);
    private Frame2 frame2=new Frame2();
    private Antiban antiban= new Antiban(frame2);
    private Frame frame= new Frame();

    private Tile tiletocenter[] = {new Tile(2609, 2925)};
    private Tile RandomTile[] = {new Tile(ctx.players.local().tile().x(), ctx.players.local().tile().y())};
    private Tile tiletoCenterButt[] = {new Tile(2713, 3773)};
    private Area buttArea = new Area(new Tile(2702, 3780), new Tile(2730, 3759));

    public void start() {
        frame.frameAnimal();
        frame2.start();
        initime=System.currentTimeMillis();
        HuInitLevel =ctx.skills.level(Constants.SKILLS_HUNTER);
        HuExpInit= ctx.skills.experience(Constants.SKILLS_HUNTER);
    }

    public void poll() {
        switch (state())
        {
            case BIRDSNARE:
                log.info("birdnsare");
                antiban.antiban();
                Snare = ctx.objects.select().id(snare).poll();

                snaketaken = ctx.objects.select().id(snaredTaken).poll();
                if (!Snare.inViewport() && !snaketaken.inViewport()) {
                    Item birdsnare = ctx.inventory.select().id(birdSnareItem).poll();
                    birdsnare.interact("Lay");
                    Condition.sleep(org.powerbot.script.Random.nextInt(1500, 2500));
                }
                SnareUsed = ctx.objects.select().id(snaredUsed).poll();
                if (SnareUsed.inViewport()) {
                    SnareUsed.interact("Dismantle");

                }
                GroundItem birdGround = ctx.groundItems.select().id(birdSnareItem).poll();
                if (birdGround.inViewport()) {
                    birdGround.interact("Take");


                }

                snaketaken = ctx.objects.select().id(snaredTaken).poll();
                if (snaketaken.inViewport()) {
                    snaketaken.interact("Check");
                }

                int x = org.powerbot.script.Random.nextInt(0,4);
                if (ctx.players.local().animation() == -1 && ctx.players.local().inMotion() == false && x == 1 && !Snare.inViewport()) {

                    TilePath path = ctx.movement.newTilePath(tiletocenter);
                    path.randomize(2, 2);
                    path.traverse();
                }

                break;
            case BONES:

                ctx.inventory.select().id(bones).each(new Filter<Item>() {
                    @Override
                    public boolean accept(Item item) {
                        log.info("Dropping");
                        return item.interact("Drop");
                    }
                });
                ctx.inventory.select().id(chicken).each(new Filter<Item>() {
                    @Override
                    public boolean accept(Item item) {
                        log.info("Dropping");
                        return item.interact("Drop");
                    }
                });
                break;

            case BLUEBIRD:
                bluebirdSna = ctx.objects.select().id(blubirdSnare).poll();
                if (!bluebirdSna.inViewport() && !snaketaken.inViewport()) {
                    Item birdsnare = ctx.inventory.select().id(birdSnareItem).poll();
                    birdsnare.interact("Lay");
                    Condition.sleep(org.powerbot.script.Random.nextInt(1500,2500));
                }
                SnareUsed = ctx.objects.select().id(snaredUsed).poll();
                if (SnareUsed.inViewport()) {
                    SnareUsed.interact("Dismantle");
                }
                birdGround = ctx.groundItems.select().id(birdSnareItem).poll();
                if (birdGround.inViewport()) {
                    birdGround.interact("Take");

                }

                snaketaken = ctx.objects.select().id(snakeTakenBlue).poll();
                if (snaketaken.inViewport()) {
                    snaketaken.interact("Check");
                }

                break;
            case BUTTERFLY:

                if (ctx.inventory.select().id(butterfly).count() == ctx.inventory.select().count() - 1) {
                    ctx.inventory.select().id(butterfly).each(new Filter<Item>() {
                        @Override
                        public boolean accept(Item item) {
                            log.info("Dropping");
                            int xw = org.powerbot.script.Random.nextInt(0,4);
                            if (xw == 4 &&errorDropping==true) {
                                return item.interact("Drop");
                            } else {
                                GroundItem drop = ctx.groundItems.select().id(butterflyItem).poll();
                                if (drop.inViewport()) {
                                    drop.interact("Take");
                                }
                                return item.interact("Release");
                            }
                        }
                    });
                }
                if (ctx.players.local().animation() == -1 && ctx.players.local().inMotion() == false) {
                    Npc buter = ctx.npcs.select().id(npcButter).nearest().poll();
                    if (buter.inViewport()) {
                        buter.click();
                        Condition.sleep(org.powerbot.script.Random.nextInt(1000,1200));
                    } else {
                        int randomcamera = org.powerbot.script.Random.nextInt(0,25);
                        int angle = org.powerbot.script.Random.nextInt(0,270);
                        if (randomcamera == 2) {
                            ctx.camera.angle(angle);
                            log.info("Rotating camera to find butter");
                        }
                    }
                    log.info("Catching butter");
                }
                if (!buttArea.contains(ctx.players.local()) && centerChecker == 1) {
                    TilePath center = ctx.movement.newTilePath(tiletoCenterButt);
                    center.randomize(2, 2);
                    center.traverse();
                    log.info("Going center butter");
                }
                break;

            case SPOTTTED:

                Npc kebbit= ctx.npcs.select().id(kebbitID).nearest().poll();
                Npc kebbitcathced=ctx.npcs.select().id(kebitCatched).poll();
                if(kebbit.inViewport() && ctx.players.local().animation()==-1 && ctx.players.local().inMotion()==false ){
                       if(!kebbitcathced.inViewport()) {
                           kebbit.interact("Catch");
                       }
                        Npc Kebbitcat = ctx.npcs.select().id(kebbitcathced).poll();
                    kebbitcathced=ctx.npcs.select().id(kebitCatched).poll();
                    if(Kebbitcat.inViewport()) {
                        Kebbitcat.interact("Retrieve");
                    }else{
                        log.info("Turning camera");
                    }
                    }
                    break;
            case SALAMANDER:

                GroundItem items= ctx.groundItems.select().id(itemsSal).poll();
                log.info("Salamanders");

                GameObject trap=ctx.objects.select().id(salTrap).nearest().poll();

                if(ctx.players.local().animation()==-1 && ctx.players.local().inMotion()==false && !items.inViewport() ) {
                    GameObject trees= ctx.objects.select().id(salmTree).nearest().poll();
                   trees.interact("Set-trap");
                    log.info("Setting trap");
                }
                if(trap.inViewport()){
                    trap.interact("Check");
                }
                if(ctx.inventory.select().id(salmItemInv).count()>=1){
                    ctx.inventory.select().id(salmItemInv).each(new Filter<Item>() {
                        @Override
                        public boolean accept(Item item) {
                            log.info("Dropping");

                            return item.interact("Release");

                        }
                    });

                }
                 items= ctx.groundItems.select().id(itemsSal).poll();
                if(items.inViewport()){
                    items.interact("Take");
                }

                break;
            case CHOMPAS:

                GameObject boxTaken= ctx.objects.select().id(boxTakenID).nearest().poll();
                GameObject boxtrapped= ctx.objects.select().id(boxtrappedID).nearest().poll();
                GameObject boxNormal= ctx.objects.select().id(boxNormalID).nearest().poll();
                GroundItem boxteap=ctx.groundItems.select().id(boxteapId).poll();
                Item box= ctx.inventory.select().id(boxteapId).poll();
                int xBOX= boxNormal.tile().x();
                int yBOX= boxNormal.tile().y();
                if(ctx.inventory.select().id(boxteapId).count()>0 && ctx.players.local().animation()==-1 && !boxTaken.inViewport() && moveOrNot==false &&(xBOX!=ctx.players.local().tile().x()|| ctx.players.local().tile().y()!=yBOX ) )
                {
                    box.interact("Lay");
                    Condition.sleep(org.powerbot.script.Random.nextInt(frame2.tardandoMasx,frame2.tarandomasY));

                    ctx.players.local().centerPoint();
                }
                    if(boxTaken.inViewport() && ctx.players.local().animation()==-1&& ctx.players.local().inMotion()==false){
                    boxTaken.interact("Dismantle");
                        Condition.sleep(org.powerbot.script.Random.nextInt(frame2.tardandoMasx,frame2.tarandomasY));
                }
                if(boxtrapped.inViewport()&& ctx.players.local().animation()==-1 && ctx.players.local().inMotion()==false){
                    boxtrapped.interact("Check");
                    Condition.sleep(org.powerbot.script.Random.nextInt(frame2.tardandoMasx,frame2.tarandomasY));
                }
                if(boxteap.inViewport() && ctx.players.local().animation()==-1 && ctx.players.local().inMotion()==false){
                    boxteap.interact("Take");
                    Condition.sleep(org.powerbot.script.Random.nextInt(frame2.tardandoMasx,frame2.tarandomasY));
                }
            if( (boxNormal.tile().x()==ctx.players.local().tile().x()&& ctx.players.local().tile().y()!=boxTaken.tile().y()) || xBOX!=ctx.players.local().tile().x()&& ctx.players.local().tile().y()!=yBOX && ctx.players.local().inMotion()==false && ctx.players.local().animation()==-1|| moveOrNot==true || boxTaken.tile().x()==ctx.players.local().tile().x() && boxTaken.tile().y()==ctx.players.local().tile().y()){
                TilePath path = ctx.movement.newTilePath(RandomTile);

                path.randomize(1, 1);
                path.traverse();
                moveOrNot=false;
            }
                break;
                    case REDC:

                        GameObject boxTaken1= ctx.objects.select().id(boxtakenRed).nearest().poll();
                        GameObject boxtrapped1= ctx.objects.select().id(boxtrappedRed).nearest().poll();
                        GameObject boxNormal1= ctx.objects.select().id(boxnormalRed).nearest().poll();
                        GroundItem boxteap1=ctx.groundItems.select().id(boxteapRed).poll();
                        Item box1= ctx.inventory.select().id(boxteapRed).poll();
                        int xBOX1= boxNormal1.tile().x();
                        int yBOX1= boxNormal1.tile().y();
                        if(boxtrapped1.inViewport()&& ctx.players.local().animation()==-1 && ctx.players.local().inMotion()==false){
                            boxtrapped1.interact("Check");
                            Condition.sleep(org.powerbot.script.Random.nextInt(frame2.tardandoMasx,frame2.tarandomasY));

                        }
                        if(boxTaken1.inViewport() && ctx.players.local().animation()==-1&& ctx.players.local().inMotion()==false){
                           if(frame2.getRightC()){
                               int z=org.powerbot.script.Random.nextInt(0,15);
                               switch(z){
                                   case 1:
                                       boxTaken1.click(false);
                                       break;
                               }

                           }
                            boxTaken1.interact("Dismantle");
                            Condition.sleep(org.powerbot.script.Random.nextInt(frame2.tardandoMasx,frame2.tarandomasY));
                        }
                        if(boxteap1.inViewport() && ctx.players.local().animation()==-1 && ctx.players.local().inMotion()==false){
                            if(frame2.getRightC()){
                                int z=org.powerbot.script.Random.nextInt(0,15);
                                switch(z){
                                    case 1:
                                        boxTaken1.click(false);
                                        break;
                                }

                            }
                            boxteap1.interact("Take");
                            Condition.sleep(org.powerbot.script.Random.nextInt(frame2.tardandoMasx,frame2.tarandomasY));
                        }

                        if( (boxNormal1.tile().x()==ctx.players.local().tile().x()&& ctx.players.local().tile().y()!=boxTaken1.tile().y()) || xBOX1!=ctx.players.local().tile().x()&& ctx.players.local().tile().y()!=yBOX1 && ctx.players.local().inMotion()==false && ctx.players.local().animation()==-1|| moveOrNot==true){
                            TilePath path = ctx.movement.newTilePath(RandomTile);

                            path.randomize(1, 1);
                            path.traverse();
                            moveOrNot=false;
                        }
                        if(ctx.inventory.select().id(boxteapRed).count()>0 && ctx.players.local().animation()==-1 && !boxTaken1.inViewport() && moveOrNot==false &&(xBOX1!=ctx.players.local().tile().x()|| ctx.players.local().tile().y()!=yBOX1 ) && ctx.players.local().inMotion()==false )
                        {
                            log.info("Setting trap");
                            box1.interact("Lay");
                            Condition.sleep(org.powerbot.script.Random.nextInt(frame2.tardandoMasx,frame2.tarandomasY));
                        }

                        break;
        }
    }

    private State state() {

        if (ctx.inventory.select().count() < 27 && frame.animal==1)

            return State.BIRDSNARE;

        else  if(ctx.inventory.select().count()==28 ||ctx.inventory.select().count()==27 && (animal==1|| animal==2)){

            return State.BONES;
        }else if(ctx.inventory.select().count()<27 && frame.animal==2){
            return State.BLUEBIRD;
        }if(frame.animal==3){
            return State.BUTTERFLY;
        }else if(frame.animal==4){
            return State.SPOTTTED;
        }else if(frame.animal==5){
            return State.SALAMANDER;
        }else if(frame.animal==6){
            return State.CHOMPAS;
        }else if(frame.animal==7){
            return State.REDC;
        }else{
            return State.NOTHING;
        }
    }

    private enum State{
        BIRDSNARE,BONES,BLUEBIRD,BUTTERFLY,NOTHING,SPOTTTED,SALAMANDER,CHOMPAS,REDC;
    }
    public void messaged(MessageEvent me) {
        String msg = me.text();

        if(msg.contains("caught a chinchompa"))
        {
            catches++;

        }else if(msg.contains("caught a carnivorous")){
            redCatches++;
        }else if(msg.contains("can't lay")){
            moveOrNot=true;
        }

}
    public void repaint(Graphics g1){

        int currentExp = ctx.skills.experience(Constants.SKILLS_HUNTER);
        int currLevel = ctx.skills.level(Constants.SKILLS_HUNTER);
        int catchesToNextLevel = (ctx.skills.experienceAt(currLevel + 1) - currentExp) / 198;
        int expGained= currentExp-HuExpInit;
        Hulevel = currLevel -HuInitLevel;
        hours = (int) ((System.currentTimeMillis() - initime) / 3600000);
        minutes = (int) ((System.currentTimeMillis() - initime) / 60000 % 60);
        seconds = (int) ((System.currentTimeMillis() - initime) / 1000) % 60;
        runTime = (double) (System.currentTimeMillis() - initime) / 3600000;

        Graphics2D g2= (Graphics2D) g1;
        int posx= (int) ctx.input.getLocation().getX();
        int posy= (int) ctx.input.getLocation().getY();
        g2.setColor(Color.GREEN);
        g2.drawLine(posx,posy-10,posx,posy+10);
        g2.drawLine(posx-10,posy,posx+10,posy);
        g2.setColor(Color.GREEN);
        g2.drawOval(posx-9,posy-9,18,18);
        g1.setColor(Color.BLACK);
        g1.fillRect(1,340,515,140);
        long thickness = 4;
        BasicStroke basic= new BasicStroke(thickness);
        g2.setColor(Color.WHITE);
        g2.setStroke(basic);
        g2.drawRect(1, 340, 515, 140);

        g1.setColor(Color.WHITE);
        g1.setFont(font);
        g1.drawString("Levels gained : " + Hulevel, 20, 375);
        g1.drawString("Time passed : " + hours + " : " + minutes + " : " + seconds, 20, 400);
        g1.drawString("Experience gained : " +expGained,20,425);
        g1.drawString("Catchs to next level: " + catchesToNextLevel, 20, 450);


        int money= (int) ((catches*1010)/runTime);
        int moneyRed=(int)((redCatches*1400));
        int moneyCatchesRed=(int)((redCatches*1400)/runTime);
        int ActualMoney=(int)((catches*1010));
        if(champsB==true) {
            g1.drawString("Catches :  " + catches, 20, 475);
            g1.drawString("Money/Hour " + money, 335, 375);
            g1.drawString("Money made: " + ActualMoney, 335, 400);
        }
        if(RedCB==true){
            g1.drawString("Catches :  " + redCatches, 20, 475);
            g1.drawString("Money/Hour " + moneyCatchesRed, 335, 375);
            g1.drawString("Money made: " + moneyRed, 335, 400);
        }
        g1.drawString("Spearless", 335,425);

    }

    }



