import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Gaston on 15/12/2017.
 */
@Script.Manifest(name = "ChickenPrayer", properties = "author=Spearless; topic=1340960; client=4;", description = "Collects and prays bones from chickens")
public class GoblinPrayer extends PollingScript<ClientContext> implements MessageListener,PaintListener {
            int bonesId=526;
            int cantreach=0;
           java.util.Random random= new java.util.Random();
            GroundItem bones= ctx.groundItems.select().id(bonesId).nearest().poll();
            GameObject door= ctx.objects.select().id(1535).poll();
            GoblinAntiban goblinAntiban= new GoblinAntiban();
            Tile WALKING_TO_CENTER[]={new Tile(3249,3238,0)};
            Area GOBLIN_HOUSE = new Area(new Tile(3243, 3249), new Tile(3249, 3243));
            int bonesPrayed;
            int  pryInitLevel, hours, seconds, minutes, prylevel,pryExpInit;
            double runTime;
            long initime;
            Font font = new Font(("Arial"), Font.BOLD, 16);

    public void start() {
        goblinAntiban.start();
        initime=System.currentTimeMillis();
        pryInitLevel =ctx.skills.level(Constants.SKILLS_PRAYER);
        pryExpInit= ctx.skills.experience(Constants.SKILLS_PRAYER);
    }

    public void poll() {


        switch(state())

        {
            case SEARCH:
                goblinAntiban.antiban();
                bones= ctx.groundItems.select().id(bonesId).nearest().poll();
                    if(bones.inViewport() && ctx.players.local().animation()==-1 && !ctx.players.local().inMotion()){
                                    bones.interact("Take");
                        Condition.sleep(org.powerbot.script.Random.nextInt(800, 3000));
                }

                break;
            case PRAY:
                if(ctx.inventory.select().id(bonesId).count()>0) {
                    ctx.inventory.select().id(526).each(new Filter<Item>() {
                        @Override
                        public boolean accept(Item item) {
                            Condition.sleep(org.powerbot.script.Random.nextInt(800, 1000));
                            if (goblinAntiban.dropB == true) {
                                int dropb = random.nextInt(50);
                                if (dropb == 2) {
                                    return item.interact("Drop");
                                }
                            }

                            return item.interact("Bury");
                        }

                    });
                    if (ctx.inventory.select().count() > 0) {
                        ctx.inventory.select().each(new Filter<Item>() {
                            @Override
                            public boolean accept(Item item) {
                                Condition.sleep(org.powerbot.script.Random.nextInt(600, 1000));
                                return item.interact("Drop");

                            }

                        });

                    }
                }
        break;
        }
    }

    private State state(){
        bones= ctx.groundItems.select().id(bonesId).nearest().poll();
        if(ctx.inventory.select().count()<28 && bones.inViewport()) {

            return State.SEARCH;
        }else if(ctx.inventory.select().count()==28){
            return State.PRAY;
        }else{
            return State.CENTER;
        }
    }

    private enum State{
        SEARCH,PRAY,ANTIBAN,NOTHING,CENTER;
    }

    public void messaged(MessageEvent me) {
        String msg = me.text();

        if (msg.contains("reach that")) {
            cantreach=1;
        }else if(msg.contains("bury the bones")){
            bonesPrayed++;

        }
    }
    public void repaint(Graphics g1){

        int currentExp = ctx.skills.experience(Constants.SKILLS_PRAYER);
        int currLevel = ctx.skills.level(Constants.SKILLS_PRAYER);
        int bonesToNextLevel = (ctx.skills.experienceAt(currLevel + 1) - currentExp) / 5;
        int expGained= currentExp-pryExpInit;
        prylevel = currLevel -pryInitLevel;
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
        g1.drawString("Levels gained : " + prylevel, 20, 375);
        g1.drawString("Time passed : " + hours + " : " + minutes + " : " + seconds, 20, 400);
        g1.drawString("Experience gained : " +expGained,20,425);
        g1.drawString("Bones to next level " + bonesToNextLevel, 20, 450);
        g1.drawString("Burys :  " + bonesPrayed, 20, 475);


        g1.drawString("Spearless", 335,425);

    }
}

