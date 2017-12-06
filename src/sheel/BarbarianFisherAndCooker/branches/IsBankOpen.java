package src.sheel.BarbarianFisherAndCooker.branches;

import src.sheel.BarbarianFisherAndCooker.leaves.LoadBankPreset;
import src.sheel.BarbarianFisherAndCooker.leaves.OpenBank;
import src.sheel.BarbarianFisherAndCooker.TreeBot.BranchTask;
import src.sheel.BarbarianFisherAndCooker.TreeBot.TreeTask;
import org.powerbot.script.rt6.ClientContext;

public class IsBankOpen extends BranchTask {

    private OpenBank openBankLeaf = new OpenBank(ctx);
    private LoadBankPreset loadBankPreset = new LoadBankPreset(ctx);

    public IsBankOpen(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean validate() {
        return ctx.bank.opened();
    }

    @Override
    public TreeTask successTask() {
        return loadBankPreset;
    }

    @Override
    public TreeTask failureTask() {
        return openBankLeaf;
    }
}
