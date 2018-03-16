package swamlol.sheep;

import org.powerbot.script.*;
import org.powerbot.script.rt6.*;
import org.powerbot.script.rt6.ClientContext;

import java.util.Random;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

@Script.Manifest(name = "Shepherd", description = "Shears Sheeps for good money making!", properties = "author=samlol; topic=1343625; client = 6;")

public class Sheep extends PollingScript<ClientContext> implements PaintListener {


    private TilePath pathToBank, pathToSheep;
    private static int Wool = 1737;
    private int Sheep_IDS[] = {14908, 14909, 14910,};
    public static final Tile[] PATH = {new Tile(2875, 3417, 0), new Tile(2880, 3417, 0), new Tile(2885, 3415, 0), new Tile(2890, 3415, 0), new Tile(2895, 3415, 0), new Tile(2900, 3415, 0), new Tile(2905, 3415, 0), new Tile(2910, 3419, 0), new Tile(2915, 3422, 0), new Tile(2919, 3426, 0), new Tile(2915, 3429, 0),};

    @Override
    public void start() {
        pathToSheep = new TilePath(ctx, PATH);
        pathToBank = new TilePath(ctx, PATH).reverse();
    }

    @Override
    public void poll() {


        final State state = getState();
        if (state == null) {
            return;
        }
        switch (state) {

            case WALK_TO_SHEEP:
                if(ctx.movement.destination().distanceTo(ctx.players.local()) < 10 || ctx.movement.destination().equals(Tile.NIL)){
                    ctx.camera.angle(new Random().nextInt(360));
                    pathToSheep.traverse();
                }
                break;

            case WALK_TO_BANK:
                if(ctx.movement.destination().distanceTo(ctx.players.local()) < 10 || ctx.movement.destination().equals(Tile.NIL)){
                    ctx.camera.angle(new Random().nextInt(360));
                    pathToBank.traverse();
                }
                break;

            case SHEER:
                if(ctx.camera.pitch() != 85){
                    ctx.camera.pitch(85);
                }
                final Npc sheeptosheer = ctx.npcs.select().id(Sheep_IDS).nearest().poll();
                if (sheeptosheer.inViewport()) {
                    ctx.npcs.select().id(sheeptosheer).nearest().poll().click();
                } else {
                    ctx.camera.turnTo(sheeptosheer);
                }
                break;

            case BANK:
                if (!ctx.bank.opened()) {
                    ctx.bank.open();
                }
                else if (!ctx.backpack.select().id(Wool).isEmpty()) {
                    ctx.widgets.widget(762).component(87).click();
                } else {
                    ctx.bank.close();
                }
                break;
        }
    }

    private State getState() {
        final Npc sheeptosheer = ctx.npcs.select().id(Sheep_IDS).nearest().poll();
        if (ctx.bank.opened()) {
            return State.BANK;
        } else if (ctx.backpack.select().count() < 1 && ctx.backpack.select().id(Wool).isEmpty() && !sheeptosheer.inViewport()) {
            return State.WALK_TO_SHEEP;
        } else if (ctx.bank.nearest().tile().distanceTo(ctx.players.local()) < 10 && !ctx.backpack.select().id(Wool).isEmpty()) {
            return State.BANK;
        } else if (ctx.backpack.select().count() < 28 && ctx.players.local().animation() == -1) {
            return State.SHEER;
        } else if (ctx.backpack.select().count() > 27) {
            return State.WALK_TO_BANK;
        }
        return null;
    }

    private enum State {
        WALK_TO_SHEEP,WALK_TO_BANK, BANK, SHEER
    }

    public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);

    @Override
    public void repaint(Graphics graphics) {
        long milliseconds = this.getTotalRuntime();
        long seconds = (milliseconds / 1000) % 60;
        long minutes = (milliseconds / (1000*60) % 60);
        long hours = (milliseconds / (1000 * 60 * 60) % 24);


        Graphics2D g = (Graphics2D)graphics;

        g.setColor(new Color(0,0,0,180));
        g.fillRect(0,0,150,60);

        g.setColor(new Color(2,255,2));
        g.drawRect(0,0,150,60);

        g.drawString("Sheep Shearer", 20,20);
        g.drawString("Running: " + String.format("%02d:%02d:%02d", hours, minutes, seconds),20,40);
    }

}

