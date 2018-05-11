package sintaax.tutorialisland.engine.modules.zero.tasks;

import sintaax.tutorialisland.engine.constants.Varps;
import sintaax.tutorialisland.engine.objects.Task;
import sintaax.tutorialisland.engine.constants.DesignerComponents;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.ClientContext;

public class Designer extends Task<ClientContext> {
    public Designer(ClientContext ctx) {
        super(ctx);
    }

    private DesignerComponents designerComponents = new DesignerComponents(ctx);
    private Varps varps = new Varps(ctx);

    @Override
    public boolean activate() {
        return varps.DESIGNER;
    }

    @Override
    public void execute() {
        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerArms.click();
            WaitRandom();
        }

        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerFeet.click();
            WaitRandom();
        }

        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerFeet2.click();
            WaitRandom();
        }

        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerHair.click();
            WaitRandom();
        }

        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerHands.click();
            WaitRandom();
        }

        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerHead.click();
            WaitRandom();
        }

        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerJaw.click();
            WaitRandom();
        }

        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerLegs.click();
            WaitRandom();
        }

        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerLegs2.click();
            WaitRandom();
        }

        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerSkin.click();
            WaitRandom();
        }

        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerTorso.click();
            WaitRandom();
        }

        for (int x = 0; x < ClickRandom(); x++) {
            designerComponents.designerTorso2.click();
            WaitRandom();
        }

        if (Random.nextBoolean())
            designerComponents.designerFemale.click();

        designerComponents.designerAccept.click();
    }

    private int ClickRandom() {
        return Random.nextInt(0, 10);
    }

    private void WaitRandom() {
        Condition.sleep(Random.nextInt(512, 1024));
    }
}
