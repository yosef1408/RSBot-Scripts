package LoftySoM.clayHumidifier;

import LoftySoM.scriptHelper.Helper;
import LoftySoM.scriptHelper.HelperSingleton;
import LoftySoM.scriptHelper.PaintSingleton;
import LoftySoM.scriptHelper.SkillTracker;
import org.powerbot.script.Condition;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.*;


import java.awt.*;
/**
 * Created by Raphael on 2-3-2019.
 */
@Script.Manifest(name = "Clay Humidifier Beta", description = "Simple clay humidifier.",
        properties = "author=LoftySoM; topic=999; client=4;")
public class ClayHumidifier extends PollingScript<ClientContext> implements PaintListener {
    private Helper helper;
    private int clayCount = 0;
    private int astralRunesUsed;
    private SkillTracker skillTracker;

    @Override
    public void start() {
        super.start();
        HelperSingleton.setContext(ctx);
        helper = HelperSingleton.getHelper();
        skillTracker = HelperSingleton.getSkillTracker();
        PaintSingleton.initiate();
        Condition.wait(ctx.game::loggedIn);
        // todo Add withdrawing Astral runes from bank
        if (!helper.inventoryContains("Astral rune")) {
            helper.suspendAlert("Please start with Astral runes in inventory");
            ctx.controller.stop();
        }
        if (!ctx.equipment.itemAt(Equipment.Slot.MAIN_HAND).name().toLowerCase().contains("steam")) {
            helper.suspendAlert("Please start with a Steam battlestaff or  Mystical steam battlestaff equipped");
            ctx.controller.stop();
        }
        if (!ctx.bank.nearest().tile().matrix(ctx).valid()) {
            helper.alert("Please start near a bank!", "No bank found.");
            ctx.controller.stop();
        }
        if (ctx.magic.book() != Magic.Book.LUNAR) {
            helper.alert("Please start on the lunar spell book!", "Wrong spell book.");
            ctx.controller.stop();
        }
        PaintSingleton.paintAboveChat(true);
    }

    @Override
    public void poll() {
        State state = getState();
        switch (state) {
            case BANK:
                helper.setStatus("Banking");
                if (ctx.bank.opened()) {
                    if (helper.inventoryContains("Soft clay")) {
                        if (!helper.drawNumber(20))
                            ctx.bank.depositInventory();
                        else
                            ctx.bank.depositAllExcept("Astral rune");
                        helper.sleep();
                    }
                    else if (!helper.inventoryContains("Clay")) {
                        Item clay = ctx.bank.select().name("Clay").poll();
                        if (clay.valid()) {
                            helper.log("Withdrawing Clay");
                            ctx.bank.withdraw(clay, 27);
                            helper.log("Closing bank window");
                            ctx.input.send("{VK_ESCAPE}");
                        } else {
                            helper.setStatus("Stopping..");
                            helper.alert("No clay found in bank", "No more clay");
                            ctx.controller.stop();
                        }
                    } else {
                        helper.setStatus("Closing bank!");
                        ctx.input.send("{VK_ESCAPE}");
                        helper.sleep();
                    }
                } else if (!helper.inventoryContains("Clay")) {
                    ctx.objects.select().name("Grand Exchange booth").action("Bank").within(2);
                    if (!ctx.objects.isEmpty())
                        Condition.wait(() -> ctx.objects.poll().interact("Bank"), 300);
                    else
                        Condition.wait(ctx.bank::open);
                    Condition.wait(ctx.bank::opened);
                }
                break;
            case CAST_HUMIDIFY:
                helper.setStatus("Casting spell");
                if (!helper.inventoryContains("Astral rune")) {
                    helper.alert("No Astral runes in inventory", "No astral runes.");
                }
                ctx.magic.cast(Magic.LunarSpell.HUMIDIFY);
                System.out.println(clayCount);
                incrementClayCount();
                astralRunesUsed++;
                System.out.println(clayCount);
                if (!helper.drawNumber(5)) {
                    helper.sleep();
                    ctx.objects.select().name("Bank booth").nearest().poll().hover();
                }
                Condition.wait(() -> helper.inventoryContains("Soft clay"));
                helper.sleep();
                break;
        }
    }

    enum State {
        BANK,
        CAST_HUMIDIFY,
        ANIMATING
    }
    private void incrementClayCount(){
        int oldClay = clayCount;
        clayCount = oldClay + 27;
    }
    private State getState() {
        if ((helper.inventoryContains("Soft clay") && !helper.inventoryContains("Clay")) || ctx.bank.opened()
                || (!helper.inventoryContains("Soft clay") && !helper.inventoryContains("Clay")))
            return State.BANK;
        else if (helper.inventoryContains("Clay"))
            return State.CAST_HUMIDIFY;
        return State.ANIMATING;
    }

    @Override
    public void repaint(Graphics graphics) {
        if (!PaintSingleton.ready())
            return;
        PaintSingleton.update(graphics);
        // todo Clay softened per hour
        PaintSingleton.paintAll(
                String.format("Status: %s", helper.getStatus()),
                String.format("Astral runes used: %d", astralRunesUsed),
                String.format("Clays softened: %d", clayCount),
                String.format("Clays made per hour: %s", (int) (helper.second() * clayCount)),
                String.format("time elapsed: %s", skillTracker.time(ctx.controller.script().getRuntime()))
        );
    }
}
