package sintaax.tutorialisland.engine.constants;

import sintaax.tutorialisland.engine.objects.Context;

import org.powerbot.script.rt4.ClientContext;

public class Varps extends Context<ClientContext> {
    public Varps(ClientContext ctx) {
        super(ctx);
    }

    public final boolean DESIGNER = ctx.varpbits.varpbit(22) == 0;
    public final boolean OPTIONS_TOGGLED = ctx.varpbits.varpbit(281) > 3;
    public final boolean RUN_OFF = ctx.varpbits.varpbit(173) == 0;
    public final boolean RUN_ON = ctx.varpbits.varpbit(173) == 1;
    public final boolean AUDIO_OFF = ctx.varpbits.varpbit(168) == 4;
    public final boolean AUDIO_ON = ctx.varpbits.varpbit(168) < 4;

    public final int get(int index) {
        return ctx.varpbits.varpbit(index);
    }
}
