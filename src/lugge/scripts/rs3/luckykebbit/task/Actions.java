package lugge.scripts.rs3.luckykebbit.task;

import lugge.scripts.rs3.luckykebbit.data.Path;
import lugge.scripts.rs3.luckykebbit.data.Tracks;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class Actions extends ClientAccessor {
    public Actions(ClientContext ctx) {
        super(ctx);
    }

    public GameObject nextObject;
    public ArrayList<GameObject> objects = new ArrayList();
    public String status = "";

    public void startTrack() {
        GameObject next = null;
        switch (Random.nextInt(1, 3)) {
            case 1:
                next = ctx.objects.select().id(66474).poll();
                break;
            case 2:
                next = ctx.objects.select().id(66473).poll();
                break;
            case 3:
                next = ctx.objects.select().id(new int[]{66474, 66473}).nearest().poll();
                break;
        }
        nextObject = next;
        objects.add(next);
        if (next.inViewport()) {
            status = "Inspect";
            next.interact("Inspect");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.varpbits.varpbit(1218) != 0;
                }
            }, 500, 5);
        } else {
            status = "Turn Cam";
            ctx.camera.turnTo(next);
            if (!next.inViewport()) {
                status = "Walk";
                ctx.movement.step(next);
                final GameObject finalNext = next;
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return finalNext.inViewport();
                    }
                }, 500, 5);
                status = "Inspect";
                next.interact("Inspect");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.varpbits.varpbit(1218) != 0;
                    }
                }, 500, 5);
            }
        }
    }

    public void followTrack() {
        for (Tracks track : Tracks.values()) {
            if (ctx.varpbits.varpbit(1218) == track.getVarpbitSetting()) {
                if (track.getNextObjectID() != 0) {
                    GameObject next = ctx.objects.select().id(track.getNextObjectID()).poll();
                    nextObject = next;
                    objects.add(next);
                    if (next.inViewport()) {
                        status = "Inspect";
                        next.interact("Inspect");
                        final int i = ctx.varpbits.varpbit(1218);
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.varpbits.varpbit(1218) != i;
                            }
                        }, 500, 5);
                        break;
                    } else {
                        status = "Turn Cam";
                        ctx.camera.turnTo(next);
                        if (!next.inViewport()) {
                            status = "Walk";
                            ctx.movement.step(next);
                        }
                        break;
                    }
                } else {
                    GameObject next = ctx.objects.select().at(track.getFinalTile().getTile()).poll();
                    nextObject = next;
                    objects.add(next);
                    if (next.inViewport()) {
                        status = "Attack";
                        next.interact("Attack");
                        Condition.wait(new Callable<Boolean>() {
                            @Override
                            public Boolean call() throws Exception {
                                return ctx.varpbits.varpbit(1218) == 0;
                            }
                        }, 500, 5);
                        objects.clear();
                        break;
                    } else {
                        status = "Turn Cam";
                        ctx.camera.turnTo(next);
                        if (!next.inViewport()) {
                            status = "Walk";
                            ctx.movement.step(next);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void cleanInventory() {
        status = "Clean Inv";
        for (Item item : ctx.backpack.select(new Filter<Item>() {
            @Override
            public boolean accept(Item item) {
                return (item.id() == 526|| item.id() == 9986 || item.id() == 685 || item.id() == 36799);
            }
        })) {
            final Item fItem = item;
            if (item.id() == 36799) {
                item.interact("Destroy");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return ctx.widgets.component(1183, 16).visible();
                    }
                }, 500, 5);
                ctx.widgets.component(1183, 16).click();
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.backpack.select().id(fItem.id()).poll().valid();
                    }
                }, 500, 5);
            } else {
                item.interact("Drop");
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return !ctx.backpack.select().id(fItem.id()).poll().valid();
                    }
                }, 500, 5);
            }
        }
    }

    public void walkBank() {
        status = "Walk";
        ctx.movement.newTilePath(Path.BOTH_WAYS.getPath()).traverse();
    }

    public void bank() {
        if (ctx.bank.opened()) {
            status = "Bank";
            if (ctx.backpack.select().isEmpty()) {
                ctx.bank.close();
            } else {
                ctx.bank.depositInventory();
            }
        } else {
            status = "Open Bank";
            ctx.bank.open();
        }
    }

    public void walkLocation() {
        status = "Walk";
        ctx.movement.newTilePath(Path.BOTH_WAYS.getPath()).reverse().traverse();
    }
}