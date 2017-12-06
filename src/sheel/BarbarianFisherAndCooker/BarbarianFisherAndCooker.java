package src.sheel.BarbarianFisherAndCooker;

import src.sheel.BarbarianFisherAndCooker.branches.IsInventoryFull;
import src.sheel.BarbarianFisherAndCooker.TreeBot.*;
import org.powerbot.script.Script;

@Script.Manifest(
        name = "Barbarian Fisher and Cooker",
        description = "Fishes and cooks at the barbarian village and banks after!",
        properties = "author=sheel;topic=1340719;client=6"
)
public class BarbarianFisherAndCooker extends TreeBot
{

    private IsInventoryFull isInventoryFullBranch = new IsInventoryFull(ctx);

    @Override
    public TreeTask createNewRoot() {
        return isInventoryFullBranch;
    }
}
