package sintaax.tutorialisland.engine.constants;

import sintaax.tutorialisland.engine.objects.Context;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;

public class DesignerComponents extends Context<ClientContext> {
    public DesignerComponents(ClientContext ctx) {
        super(ctx);
    }

    public final Component designerAccept = ctx.widgets.component(269, 99);
    public final Component designerArms = ctx.widgets.component(269, 116);
    public final Component designerFeet = ctx.widgets.component(269, 119);
    public final Component designerFeet2 = ctx.widgets.component(269, 130);
    public final Component designerFemale = ctx.widgets.component(269, 137);
    public final Component designerHair = ctx.widgets.component(269, 121);
    public final Component designerHands = ctx.widgets.component(269, 117);
    public final Component designerHead = ctx.widgets.component(269, 113);
    public final Component designerJaw = ctx.widgets.component(269, 114);
    public final Component designerLegs = ctx.widgets.component(269, 118);
    public final Component designerLegs2 = ctx.widgets.component(269, 129);
    public final Component designerSkin = ctx.widgets.component(269, 131);
    public final Component designerTorso = ctx.widgets.component(269, 115);
    public final Component designerTorso2 = ctx.widgets.component(269, 127);
    public final Component designerWindow = ctx.widgets.component(269, 99);
}
