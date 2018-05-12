package sintaax.tutorialisland.engine.constants;

import sintaax.tutorialisland.engine.objects.Context;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

public class GameObjects extends Context<ClientContext> {
    public GameObjects(ClientContext ctx) {
        super(ctx);
    }

    public final GameObject DOOR1 = ctx.objects.select().id(9398).nearest().poll();
    public final GameObject TREE = ctx.objects.select().id(9730).nearest().poll();
    public final GameObject FIRE = ctx.objects.select().id(26185).nearest().poll();
    public final GameObject GATE1 = ctx.objects.select().id(9708, 9470).nearest().poll();
    public final GameObject DOOR2 = ctx.objects.select().id(9709).nearest().poll();
    public final GameObject RANGE = ctx.objects.select().id(9736).nearest().poll();
    public final GameObject DOOR3 = ctx.objects.select().id(9710).nearest().poll();
    public final GameObject DOOR4 = ctx.objects.select().id(9716).nearest().poll();
    public final GameObject LADDER1 = ctx.objects.select().id(9726).nearest().poll();
    public final GameObject TIN_VEIN = ctx.objects.select().id(10080).nearest().poll();
    public final GameObject COPPER_VEIN = ctx.objects.select().id(10079).nearest().poll();
    public final GameObject FURNACE = ctx.objects.select().id(10082).nearest().poll();
    public final GameObject ANVIL = ctx.objects.select().id(2097).nearest().poll();
    public final GameObject GATE2 = ctx.objects.select().id(9717).nearest().poll();
    public final GameObject GATE3 = ctx.objects.select().id(9719, 9720).nearest().poll();
    public final GameObject LADDER2 = ctx.objects.select().id(9727).nearest().poll();
    public final GameObject BANK = ctx.objects.select().id(10083).nearest().poll();
    public final GameObject POLL = ctx.objects.select().id(26801, 26815).nearest().poll();
    public final GameObject DOOR5 = ctx.objects.select().id(9721).nearest().poll();
    public final GameObject DOOR6 = ctx.objects.select().id(9722).nearest().poll();
    public final GameObject LARGE_DOOR_CLOSED = ctx.objects.select().id(1521, 1524).nearest().poll();
    public final GameObject LARGE_DOOR_OPEN = ctx.objects.select().id(1522, 1525).nearest().poll();
    public final GameObject DOOR7 = ctx.objects.select().id(9723).nearest().poll();

    public boolean exists(GameObject gameObject) {
        return gameObject.valid();
    }

    public void chop(GameObject gameObject) {
        if (gameObject.inViewport()) {
            gameObject.interact("Chop down");
            ctx.camera.turnTo(gameObject);
        } else {
            ctx.movement.step(gameObject);
            ctx.camera.turnTo(gameObject);
        }
    }

    public void climbDown(GameObject gameObject) {
        if (gameObject.inViewport()) {
            gameObject.interact("Climb-down");
            ctx.camera.turnTo(gameObject);
        } else {
            ctx.movement.step(gameObject);
            ctx.camera.turnTo(gameObject);
        }
    }

    public void climbUp(GameObject gameObject) {
        if (gameObject.inViewport()) {
            gameObject.interact("Climb-up");
            ctx.camera.turnTo(gameObject);
        } else {
            ctx.movement.step(gameObject);
            ctx.camera.turnTo(gameObject);
        }
    }

    public void open(GameObject gameObject) {
        if (gameObject.inViewport()) {
            gameObject.interact("Open");
            ctx.camera.turnTo(gameObject);
        } else {
            ctx.movement.step(gameObject);
            ctx.camera.turnTo(gameObject);
        }
    }

    public void mine(GameObject gameObject) {
        if (gameObject.inViewport()) {
            gameObject.interact("Mine");
            ctx.camera.turnTo(gameObject);
        } else {
            ctx.movement.step(gameObject);
            ctx.camera.turnTo(gameObject);
        }
    }

    public void pick(GameObject gameObject) {
        if (gameObject.inViewport()) {
            gameObject.interact("Pick");
            ctx.camera.turnTo(gameObject);
        } else {
            ctx.movement.step(gameObject);
            ctx.camera.turnTo(gameObject);
        }
    }

    public void prospect(GameObject gameObject) {
        if (gameObject.inViewport()) {
            gameObject.interact("Prospect");
            ctx.camera.turnTo(gameObject);
        } else {
            ctx.movement.step(gameObject);
            ctx.camera.turnTo(gameObject);
        }
    }

    public void use(GameObject gameObject) {
        if (gameObject.inViewport()) {
            gameObject.interact("Use");
            ctx.camera.turnTo(gameObject);
        } else {
            ctx.movement.step(gameObject);
            ctx.camera.turnTo(gameObject);
        }
    }
}
