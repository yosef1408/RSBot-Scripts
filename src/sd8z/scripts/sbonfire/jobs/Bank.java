package sd8z.scripts.sbonfire.jobs;

import org.powerbot.script.Condition;
import org.powerbot.script.rt6.ClientContext;
import sd8z.core.script.Job;
import sd8z.scripts.sbonfire.SBonfire;

import java.util.concurrent.Callable;

public class Bank extends Job<SBonfire, ClientContext> {

    private final int id;

    public Bank(SBonfire script, int id) {
        super(script);
        this.id = id;
    }

    @Override
    public boolean activate() {
        return ctx.backpack.select().id(id).isEmpty();
    }

    @Override
    public void execute() {
        if (ctx.bank.inViewport()) {
            if (ctx.bank.open()) {
                if (ctx.bank.select().id(id).isEmpty()) {
                    script.log.info("[sBonfire] Logs not found in the bank, stopping.");
                    script.stop();
                    ctx.controller.stop();
                }
                if (!ctx.backpack.select().isEmpty())
                    ctx.bank.depositInventory();
                if (ctx.bank.withdraw(id, 0)) {
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
            ctx.camera.turnTo(ctx.bank.nearest());
        }

    }
}
