package iDzn.OgreCannon.Tasks;

import iDzn.OgreCannon.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;

import java.util.concurrent.Callable;

import static org.powerbot.script.rt4.Magic.Spell.TELEKINETIC_GRAB;

public class Telek  extends Task {
    public Telek(ClientContext ctx) {
        super(ctx);
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
            GroundItem rSeed = ctx.groundItems.select().id(5295).poll();
            GroundItem sSeed = ctx.groundItems.select().id(5300).poll();
            GroundItem tSeed = ctx.groundItems.select().id(5304).poll();

            if (rSeed.valid() && Spell)
                if(!rSeed.inViewport()){
                ctx.camera.turnTo(rSeed);
                ctx.camera.pitch(0);
                }
                rSeed.interact("Cast", "Ranarr seed");

            if (sSeed.valid() && Spell)
                if(!sSeed.inViewport()){
                    ctx.camera.turnTo(sSeed);
                    ctx.camera.pitch(0);
                }
            sSeed.interact("Cast", "Snapdragon seed");

            if (tSeed.valid() && Spell)
                if(!tSeed.inViewport()){
                    ctx.camera.turnTo(sSeed);
                    ctx.camera.pitch(0);
                }
            tSeed.interact("Cast", "Torstol seed");
        }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().animation()==-1;
                }
            }, 200, 20);
        }

    }

