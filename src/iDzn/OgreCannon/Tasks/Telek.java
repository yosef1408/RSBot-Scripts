package iDzn.OgreCannon.Tasks;

import iDzn.OgreCannon.OgreCannon;
import iDzn.OgreCannon.Task;
import org.powerbot.bot.rt4.client.Client;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;

import java.awt.*;
import java.util.concurrent.Callable;

import static org.powerbot.script.rt4.Magic.Spell.TELEKINETIC_GRAB;

public class Telek  extends Task<org.powerbot.script.ClientContext<Client>> {

    OgreCannon main;

    public Telek(ClientContext ctx, OgreCannon main) {

        super(ctx);
        this.main = main;

    }


    @Override
    public boolean activate() {
        return !ctx.groundItems.select().id(5300, 5304, 5295).isEmpty() && ctx.inventory.select().id(556, 563).count() > 1;

    }

    @Override
    public void execute() {
        if (ctx.inventory.select().count() < 27) ;
        {
            boolean Spell = ctx.magic.cast(TELEKINETIC_GRAB);
            final GroundItem rSeed = ctx.groundItems.select().id(5295).poll();
            final GroundItem sSeed = ctx.groundItems.select().id(5300).poll();
            final GroundItem tSeed = ctx.groundItems.select().id(5304).poll();
            if (Spell) {
                main.telek = 1;
            }
            if (rSeed.valid() && Spell)
                if (!rSeed.inViewport()) {
                    ctx.camera.turnTo(rSeed);
                    ctx.camera.pitch(67);
                }
            rSeed.interact("Cast");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !rSeed.valid();
                }
            }, 250, 20);
          if (sSeed.valid() && Spell)
                if(!sSeed.inViewport()){
                    ctx.camera.turnTo(sSeed);
                    ctx.camera.pitch(67);
                }
            sSeed.interact("Cast", "Snapdragon seed");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !sSeed.valid();
                }
            }, 300, 20);
            if (tSeed.valid() && Spell)
                if(!tSeed.inViewport()){
                    ctx.camera.turnTo(sSeed);
                    ctx.camera.pitch(67);
                }
            tSeed.interact("Cast", "Torstol seed");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !tSeed.valid();
                }
            }, 300, 20);
        }
            if (main.telek == 1 && ctx.groundItems.select().id(5300, 5304, 5295).isEmpty()) {
                int h = Random.nextInt(560, 730);
                int v = Random.nextInt(208, 217);
                ctx.input.click(new Point(h, v), true);
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return main.telek == 0;
                    }
                }, 200, 20);
            }
        }
    }

