package WheatPicker; /**
 * Created by Chris on 21-3-2016.
 */

import org.powerbot.script.Condition;
import  org.powerbot.script.Filter;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.*;
import org.powerbot.script.Tile;

import java.util.concurrent.Callable;
@Script.Manifest(name = "Harvest Wheat", description = "Harvests wheat above lumbridge", properties = "client=6; topic=0")
public class WheatPicker extends PollingScript<ClientContext> {

    private int wheatId[] = {15506,15507};
    private int openGateId[] = {45211,45213};
    private int closedGateId[] = {45210,45212};
    private int bankChestId = 90271;
    org.powerbot.script.Random rand = new org.powerbot.script.Random();

    public static final Tile BANK_TILE = new Tile(3170, 3281, 0);
    public static final Tile FIELD_TILE = new Tile(3163, 3295, 0);


    private boolean isGateOpen() {
        final GameObject openGate = ctx.objects.select().id(openGateId).poll();
        return openGate.object != null;
    }

    private void openGate() {
        final GameObject closedGate = ctx.objects.select().id(closedGateId).poll();
        if (closedGate.inViewport()) {
            ctx.camera.turnTo(closedGate);
            Condition.sleep(500);
            closedGate.click();
        }
    }

    @Override
    public void poll() {

        final GameObject openGate = ctx.objects.select().id(openGateId).poll();
        final GameObject wheat = ctx.objects.select().id(wheatId).nearest().poll();

        switch(state()) {
            case OPEN_GATE:
                System.out.println("Opening Gate");
                ctx.movement.step(FIELD_TILE);
                this.openGate();
            break;
            case PICK:
                System.out.println("Picking");
                if(!wheat.inViewport()) {
                    ctx.movement.step(FIELD_TILE);
                    ctx.camera.turnTo(FIELD_TILE);
                    ctx.camera.turnTo(wheat);
                } else {
                    wheat.click();
                    Condition.sleep(250);
                }
                break;
            case BANK:
                System.out.println("Banking");
                GameObject bankChest = (GameObject)((MobileIdNameQuery)((ClientContext)this.ctx).objects.select()).id(new int[]{this.bankChestId}).poll();
                if(!bankChest.inViewport()) {
                    ctx.movement.step(BANK_TILE);
                    ctx.camera.turnTo(bankChest);
                    Condition.sleep(4000);
                    Condition.wait(new Callable() {
                        public Boolean call() throws Exception {
                            return Boolean.valueOf(WheatPicker.BANK_TILE.distanceTo(((ClientContext)WheatPicker.this.ctx).players.local()) < 5.0D);
                        }
                    }, 250, 10);
                } else if(!ctx.bank.opened()) {
                    bankChest.interact("Use");
                    Condition.sleep(500);
                } else {
                    ctx.bank.depositInventory();
                    ctx.bank.close();
                }
        }
    }

    private State state() {
        if(!isGateOpen()) {
            return State.OPEN_GATE;
        }
        if(ctx.backpack.select().count() < 28) {
            return State.PICK;
        }
        return State.BANK;
    }

    private enum State {
        PICK, BANK, OPEN_GATE
    }

}
