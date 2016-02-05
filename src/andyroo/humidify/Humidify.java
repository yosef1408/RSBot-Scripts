package andyroo.humidify;

import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.*;

import java.util.concurrent.Callable;

@Script.Manifest(
        name = "Humidify", properties = "author=andyroo; topic=1300854; client=4;",
        description = "v1.0 - Humidify"
)


public class Humidify extends PollingScript<ClientContext> {

    private static int CLAY_ID = 434;
    private static int WET_CLAY_ID = 1761;
    private static int[] RUNE_ID = {9075, 554, 555};
    private static int[] EXCLUDE_DEPOSIT_ID = {9075, 554, 555, CLAY_ID};
    private static int HUMIDIFY_COMPONENT = 100;
    private static int HUMIDIFY_XP = 65;
    private static int fullInventoryCount;
    private static int castedCount = 0;

    private static int COLLECT_WIDGET = 402;
    private static int COLLECT_CLOSE_COMPONENT1 = 2;
    private static int COLLECT_CLOSE_COMPONENT2 = 11;

    private State nextState;
    private State currentState;


    public void start() {
        currentState = State.BANK;
        ctx.game.tab(Game.Tab.INVENTORY);
        ctx.camera.pitch(true);
        fullInventoryCount = 28 - ctx.inventory.select().id(RUNE_ID).count();
    }

    public void stop() {
        log.info("Casted " + castedCount + " times");
        log.info("Humidified " + castedCount * fullInventoryCount + " clay");
    }

    @Override
    public void poll() {

        checkCollectWidget();

        switch (currentState) {
            case BANK: {
                if (ctx.inventory.select().id(CLAY_ID).count() != fullInventoryCount) {
                    if (openBank()) {
                        log.info("Bank opened");
                        if (ctx.bank.depositAllExcept(EXCLUDE_DEPOSIT_ID)) {
                            log.info("Deposited");
                            if (ctx.bank.withdraw(CLAY_ID, Bank.Amount.ALL) && ctx.inventory.select().id(CLAY_ID).count() == fullInventoryCount) {
                                log.info("Withdrew");
                                ctx.bank.close();
                                nextState = State.CAST_SPELL;
                            } else {
                                log.info("Ran out of clay");
                                ctx.controller.stop();
                            }
                        }
                    }
                    else nextState = currentState;
                } else {
                    nextState = State.CAST_SPELL;
                }
            }
            break;

            case CAST_SPELL: {
                if (ctx.bank.opened())
                    ctx.bank.close();
                final int prevXP = ctx.skills.experience(Constants.SKILLS_MAGIC);
                ctx.game.tab(Game.Tab.MAGIC);
                ctx.widgets.component(ctx.magic.book().widget(), HUMIDIFY_COMPONENT).click("Cast", Game.Crosshair.ACTION); // click humidify
                if (Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        //System.out.println(prevXP + " : " + ctx.skills.experience(Constants.SKILLS_MAGIC));
                        return ctx.skills.experience(Constants.SKILLS_MAGIC) - prevXP == HUMIDIFY_XP;
                    }
                }, 100, 10)) {
                    log.info("Casted humidify");
                    castedCount++;
                    nextState = State.BANK;
                } else {
                    ctx.game.tab(Game.Tab.INVENTORY);
                    if (ctx.inventory.select().id(WET_CLAY_ID).count() == fullInventoryCount) {
                        log.info("Casted humidify");
                        castedCount++;
                        System.out.println(castedCount);
                        nextState = State.BANK;
                    }
                }
            }
            break;

            default: {

            }
            break;
        }

        currentState = nextState;
    }

    private boolean openBank() {
        return ctx.bank.inViewport() && ctx.bank.open();
    }

    private void checkCollectWidget() {
        if(ctx.widgets.widget(COLLECT_WIDGET).valid()) {
            ctx.widgets.component(COLLECT_WIDGET, COLLECT_CLOSE_COMPONENT1).component(COLLECT_CLOSE_COMPONENT2).click();
            log.info("Close collect widget");

        }
    }

    private enum State {
        BANK, CAST_SPELL, NO_RESOURCES, INVALID
    }
}
