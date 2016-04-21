package lugge.scripts.rs3.luckykebbit.task;

import lugge.scripts.rs3.luckykebbit.data.Tiles;
import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

public class Conditions extends ClientAccessor {
    public Conditions(ClientContext ctx) {
        super(ctx);
    }

    public States getCurrentState() {
        if (!ctx.backpack.select().id(526).isEmpty() || !ctx.backpack.select().id(9986).isEmpty() || !ctx.backpack.select().id(685).isEmpty() || !ctx.backpack.select().id(36799).isEmpty()) {
            return States.CLEAN_INVENTORY;
        }

        if (ctx.backpack.select().count() >= 26) {
            if (ctx.players.local().tile().distanceTo(Tiles.BANK_POINT.getTile()) >= 5) {
                return States.WALK_BANK;
            } else {
                return States.BANK;
            }
        }

        if (ctx.players.local().tile().distanceTo(Tiles.CENTER_POINT.getTile()) > 10) {
            return States.WALK_LOCATION;
        }

        if (ctx.players.local().animation() == -1 && !ctx.players.local().inMotion()) {
            if (ctx.varpbits.varpbit(1218) == 0) {
                return States.START_TRACK;
            } else {
                return States.FOLLOW_TRACK;
            }
        }
        return States.NULL;
    }
}
