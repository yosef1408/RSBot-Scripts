package superchaoran.GraniteSplitter.jobs;

import org.powerbot.script.Condition;
import org.powerbot.script.Locatable;
import org.powerbot.script.rt6.ClientContext;
import superchaoran.GraniteSplitter.GraniteSpliterMain;
import superchaoran.GraniteSplitter.constants.GraniteRaw;
import superchaoran.GraniteSplitter.script.Job;

import java.util.concurrent.Callable;

public class Bank extends Job<GraniteSpliterMain, ClientContext> {

    private final int id;
    private final int withdrawSize;

    public Bank(GraniteSpliterMain script, GraniteRaw graniteRaw) {
        super(script);
        this.id = graniteRaw.id();
        this.withdrawSize = graniteRaw.getWithdrawSize();
    }

    @Override
    public boolean activate() {
        return ctx.backpack.select().count() != withdrawSize || ctx.backpack.select().id(id).count() != withdrawSize;
    }

    @Override
    public void execute() {
        if (ctx.bank.inViewport() || ctx.bank.opened()) {
            if (ctx.bank.open()) {
                if (ctx.bank.select().id(id).isEmpty()) {
                    script.log.info("[Granite Splitter] Granite not found in the bank, stopping.");
                    script.stop();
                    ctx.controller.stop();
                    return;
                } else {
//                    if (ctx.bank.select().id(id).count()<3) {
//                        script.log.info("[Granite Splitter] Granite count smaller than 3, stopping.");
//                        script.stop();
//                        ctx.controller.stop();
//                        return;
//                    }
                }
                if (!ctx.backpack.select().isEmpty())
                    ctx.bank.depositInventory();
                if (ctx.bank.withdraw(id, withdrawSize)) {
                    Condition.wait(new Callable<Boolean>() {
                        @Override
                        public Boolean call() throws Exception {
                            return !activate();
                        }
                    });
                    ctx.bank.close();
                }
            }
        } else {
            Locatable bank = ctx.bank.nearest();
            if (bank.tile().distanceTo(ctx.players.local()) >= 7) {
                ctx.movement.step(bank);
            } else {
                ctx.camera.turnTo(bank);
            }
        }

    }
}
