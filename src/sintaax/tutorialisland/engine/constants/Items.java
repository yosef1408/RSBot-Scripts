package sintaax.tutorialisland.engine.constants;

import sintaax.tutorialisland.engine.objects.Context;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GroundItem;

public class Items extends Context<ClientContext> {
    public Items(ClientContext ctx) {
        super(ctx);
    }

    public final int TINDERBOX = 590;
    public final int LOG = 2511;
    public final int RAW_SHRIMP = 2514;
    public final int SHRIMP = 315;
    public final int POT = 1931;
    public final int POT_OF_FLOUR = 2516;
    public final int BUCKET = 1925;
    public final int BUCKET_OF_WATER = 1929;
    public final int BREAD_DOUGH = 2307;
    public final int TIN_ORE = 438;
    public final int COPPER_ORE = 436;
    public final int BRONZE_BAR = 2349;
    public final int BRONZE_DAGGER = 1205;
    public final int BRONZE_SWORD = 1277;
    public final int WOODEN_SHIELD = 1171;
    public final int SHORT_BOW = 841;
    public final int BRONZE_ARROWS =  882;
    public final int GRAIN = 1947;

    public GroundItem groundItem(int item) {
        return ctx.groundItems.select().id(item).nearest().poll();
    }

    public void equip(int item) {
        ctx.inventory.select().id(item).poll().interact("Equip");
    }

    public void take(int item) {
        GroundItem groundItem = groundItem(item);
        if (groundItem.inViewport()) {
            groundItem.interact("Take");
            ctx.camera.turnTo(groundItem);
        } else {
            ctx.movement.step(groundItem);
            ctx.camera.turnTo(groundItem);
        }
    }

    public void use(int item) {
        ctx.inventory.select().id(item).poll().interact("Use");
    }

    public void wield(int item) {
        ctx.inventory.select().id(item).poll().interact("Wield");
    }

    public boolean existsInInventory(int item) {
        return ctx.inventory.select().id(item).poll().valid();
    }

    public boolean existsOnGround(int item) {
        return ctx.groundItems.select().id(item).poll().valid();
    }

    public void makeFire() {
        use(TINDERBOX);
        use(LOG);
    }

    public void makeDough() {
        use(POT_OF_FLOUR);
        use(BUCKET_OF_WATER);
    }
}
