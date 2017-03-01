package Ryzzoa.rSalamanders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.text.DecimalFormat;
import java.util.concurrent.Callable;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script.Manifest;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.GroundItem;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.Npc;
import org.powerbot.script.rt6.Player;

@Manifest(
        name = "rSalamanders",
        description = "Captures and drops red salamanders for huge XP gains!",
        properties = "author=Ryzzoa; client=6; topic=1328456"
)
public class rSalamanders extends PollingScript<ClientContext> implements PaintListener {
    private int salId = 10147;
    private int trappedId = 19659;
    private int setTrapId = 19662;
    private int youngTreeId = 19663;
    private int ropeId = 954;
    private int smallNetId = 303;
    private int ladderDownId = 26849;
    private int ladderUpId = 26850;
    private int eniolaId = 6362;
    private int runeCompId;
    private String runeName;
    private boolean needToBank;
    private boolean setNeeded;
    private Player player;
    private GUI gui;
    private int startingExp;
    private int startingLvl;
    private int trapsLaid;
    private int salsCaught;
    private double catchRate;

    public rSalamanders() {
        needToBank = ctx.backpack.select().count() == 28;
        player = ctx.players.local();
        startingExp = ctx.skills.experience(21);
        startingLvl = ctx.skills.realLevel(21);
        trapsLaid = 0;
        salsCaught = 0;
        catchRate = 0.0D;
    }

    public void start() {
        gui = new GUI();
        gui.setVisible(true);
    }

    public void poll() {
        if(runeCompId == -1 && !gui.isVisible()) {
            ctx.controller.stop();
        } else {
            State state = state();
            GameObject youngTree;
            switch(state) {
                case SETUP:
                    Condition.wait(new Callable<Boolean>() {
                        public Boolean call() throws Exception {
                            return gui.isVisible();
                        }
                    });
                    runeCompId = gui.getID();
                    break;
                case PICKUP:
                    if(this.player.animation() == -1) {
                        GroundItem net = ctx.groundItems.select().nearest().id(smallNetId).poll();
                        final int netsBefore = ctx.backpack.select().id(smallNetId).count();
                        if(net.valid() && !net.inViewport()) {
                            ctx.camera.turnTo(net);
                        }

                        if(net.interact("Take", "Small fishing net")) {
                            Condition.wait(new Callable<Boolean>() {
                                public Boolean call() throws Exception {
                                    return netsBefore < ctx.backpack.select().id(smallNetId).count();
                                }
                            }, 250, 10);
                        }

                        GroundItem rope = ctx.groundItems.select().nearest().id(ropeId).poll();
                        final int ropesBefore = ctx.backpack.select().id(ropeId).count();
                        if(rope.valid() && !rope.inViewport()) {
                            ctx.camera.turnTo(rope);
                        }

                        if(rope.interact("Take", "Rope")) {
                            Condition.wait(new Callable<Boolean>() {
                                public Boolean call() throws Exception {
                                    return ropesBefore < ctx.backpack.select().id(ropeId).count();
                                }
                            }, 250, 10);
                        }
                    }
                    break;
                case SETTRAP:
                    if(player.animation() == -1 && setNeeded) {
                        if(ctx.backpack.select().count() >= 20) {
                            Condition.sleep(500);
                            if(!ctx.objects.select().id(trappedId).isEmpty() || !(ctx.objects.select().id(19658).isEmpty())) {
                                return;
                            }
                        }

                        youngTree = ctx.objects.select().id(youngTreeId).nearest().within(6.0D).poll();
                        final int components = ctx.backpack.select().id(new int[]{ropeId, smallNetId}).count();
                        if(!youngTree.inViewport()) {
                            ctx.camera.turnTo(youngTree);
                        }

                        if(youngTree.interact("Set-trap")) {
                            Condition.wait(new Callable<Boolean>() {
                                public Boolean call() throws Exception {
                                    return components > ctx.backpack.select().id(new int[]{ropeId, smallNetId}).count();
                                }
                            }, 250, 10);
                            ++trapsLaid;
                        } else {
                            ctx.camera.turnTo(youngTree);
                        }
                    }
                    break;
                case CHECKTRAP:
                    if(this.player.animation() == -1) {
                        youngTree = ctx.objects.select().id(trappedId).nearest().poll();
                        final int sals = ctx.backpack.select().id(salId).count();
                        if(!youngTree.inViewport()) {
                            ctx.camera.turnTo(youngTree);
                        }

                        if(youngTree.interact("Check")) {
                            Condition.wait(new Callable<Boolean>() {
                                public Boolean call() throws Exception {
                                    return sals < ctx.backpack.select().id(salId).count();
                                }
                            }, 250, 10);
                            ++salsCaught;
                        }
                    }
                    break;
                case BANK:
                    if(!ctx.bank.opened()) {
                        youngTree = ctx.objects.select().id(ladderDownId).poll();
                        if(!youngTree.inViewport()) {
                            ctx.camera.turnTo(youngTree);
                        }

                        if(youngTree.interact("Climb")) {
                            Condition.wait(new Callable<Boolean>() {
                                public Boolean call() throws Exception {
                                    return !ctx.npcs.select().id(eniolaId).isEmpty();
                                }
                            });
                        }

                        Npc eniola = ctx.npcs.select().id(eniolaId).poll();
                        if(!eniola.inViewport()) {
                            ctx.camera.turnTo(eniola);
                        }

                        if(eniola.interact("Bank")) {
                            Condition.wait(new Callable<Boolean>() {
                                public Boolean call() throws Exception {
                                    return ctx.widgets.widget(619).component(runeCompId).visible();
                                }
                            });
                            Component box = ctx.widgets.widget(619).component(runeCompId);
                            runeName = box.tooltip();
                            if(box.interact(runeName)) {
                                Condition.wait(new Callable<Boolean>() {
                                    public Boolean call() throws Exception {
                                        return ctx.bank.opened();
                                    }
                                });
                            }
                        }

                        return;
                    }

                    this.needToBank = false;
                    ctx.bank.depositAllExcept("Rope", "Small fishing net", runeName.substring(0, runeName.length() - 1));
                    if(ctx.backpack.select().id(ropeId).count() < 3) {
                        ctx.bank.withdraw(ropeId, 3 - ctx.backpack.select().id(ropeId).count());
                    }

                    if(ctx.backpack.select().id(smallNetId).count() < 3) {
                        ctx.bank.withdraw(smallNetId, 3 - ctx.backpack.select().id(smallNetId).count());
                    }

                    ctx.bank.close();
                    Condition.wait(new Callable<Boolean>() {
                        public Boolean call() throws Exception {
                            return !ctx.bank.opened();
                        }
                    });
                    Condition.sleep(250);
                    youngTree = ctx.objects.select().id(ladderUpId).poll();
                    if(!youngTree.inViewport()) {
                        ctx.camera.turnTo(youngTree);
                    }

                    if(youngTree.interact("Climb")) {
                        Condition.wait(new Callable<Boolean>() {
                            public Boolean call() throws Exception {
                                return ctx.npcs.select().id(eniolaId).isEmpty();
                            }
                        });
                    }
                    break;
                case RELEASE:
                    ctx.backpack.select().id(salId).each(new Filter<Item>() {
                        @Override
                        public boolean accept(Item item) {
                            return item.interact("Release");
                        }
                    });
                    return;
                case IDLE:
                    if(!needToBank) {
                        if(!ctx.npcs.select().id(eniolaId).within(15.0D).isEmpty()) {
                            youngTree = ctx.objects.select().id(ladderUpId).poll();
                            if(!youngTree.inViewport()) {
                                ctx.camera.turnTo(youngTree);
                            }

                            if(youngTree.interact("Climb")) {
                                Condition.wait(new Callable<Boolean>() {
                                    public Boolean call() throws Exception {
                                        return ctx.npcs.select().id(eniolaId).isEmpty();
                                    }
                                });
                            }
                        } else if(setNeeded) {
                            if(!ctx.objects.select().id(ladderDownId).within(3.0D).isEmpty()) {
                                youngTree = ctx.objects.select().id(youngTreeId).nearest().poll();
                            } else {
                                youngTree = ctx.objects.select().id(youngTreeId).nearest().within(6.0D).poll();
                            }

                            if(!youngTree.inViewport()) {
                                ctx.camera.turnTo(youngTree);
                            }

                            if(youngTree.interact("Set-trap")) {
                                Condition.wait(new Callable<Boolean>() {
                                    public Boolean call() throws Exception {
                                        return player.animation() == -1;
                                    }
                                }, 250, 10);
                            }
                        }
                    }
                case WALKING:
                    Condition.wait(new Callable<Boolean>() {
                        public Boolean call() throws Exception {
                            return !player.inMotion();
                        }
                    });
            }

        }
    }

    private State state() {
        if(this.gui.isVisible()) {
            return State.SETUP;
        } else {
            int trapCount = ctx.objects.select().id(setTrapId).size();
            byte maxPack = 28;
            int emptySlots = maxPack - ctx.backpack.select().count();
            setNeeded = trapCount != 3 &&
                            emptySlots - (ctx.groundItems.select().id(new int[]{ropeId, smallNetId}).count()) >= trapCount * 3 + 1 &&
                            ctx.backpack.select().id(ropeId).count() >= 1 && ctx.backpack.select().id(smallNetId).count() >= 1;
            boolean walking = player.inMotion();
            if(walking) {
                return State.WALKING;
            } else if(ctx.backpack.select().count() == 28) {
                needToBank = true;
                return State.BANK;
            } else {
                return !ctx.objects.select().id(trappedId).nearest().isEmpty()
                        ? State.CHECKTRAP
                        : (!ctx.objects.select().nearest().id(youngTreeId).within(6.0D).isEmpty() &&
                            setNeeded)
                            ? State.SETTRAP
                            : (!ctx.groundItems.select().id(new int[]{ropeId, smallNetId}).nearest().isEmpty()
                                ? State.PICKUP
                                : State.IDLE);
            }
        }
    }

    public void repaint(Graphics g1) {
        long runTime = this.getRuntime();
        int currentExp = ctx.skills.experience(21);
        int currentLvl = ctx.skills.realLevel(21);
        int expRemaining = ctx.skills.experienceAt(currentLvl + 1) - currentExp;
        int expGained = currentExp - startingExp;
        int expPerHour = (int)(3600000.0D / (double)runTime * (double)expGained);
        int goldEarned = salsCaught * 2298;
        int goldPerHour = (int)(3600000.0D / (double)runTime * (double)goldEarned);
        int salsPerHour = (int)(3600000.0D / (double)runTime * (double)salsCaught);
        if(trapsLaid != 0) {
            catchRate = (double)salsCaught / (double)trapsLaid * 100.0D;
        }

        short defaultX = 560;
        byte defaultY = 10;
        Graphics2D g = (Graphics2D)g1;
        g.fillRect(defaultX, defaultY, 200, 260);
        g.setBackground(Color.WHITE);
        g.setColor(Color.black);
        g.drawString("Runtime: " + Time(runTime), defaultX, 20);
        g.drawString("Current Exp: " + currentExp, defaultX, 40);
        g.drawString("Current Level: " + currentLvl + "(" + (currentLvl - startingLvl) + ")", defaultX, 60);
        g.drawString("Exp to Next Level: " + String.valueOf(expRemaining), defaultX, 80);
        g.drawString("Time to Next Level: " + getTimeToNextLevel(expRemaining, expPerHour), defaultX, 100);
        g.drawString("Total Exp Gained: " + expGained, defaultX, 120);
        g.drawString("Exp Per Hour: " + expPerHour, defaultX, 140);
        g.drawString("Traps Laid: " + trapsLaid, defaultX, 160);
        g.drawString("Salamanders Caught: " + salsCaught, defaultX, 180);
        g.drawString("Catch Rate: " + (int)Math.floor(catchRate) + "%", defaultX, 200);
        g.drawString("Salamanders Per Hour: " + salsPerHour, defaultX, 220);
        g.drawString("Gold Earned: " + goldEarned, defaultX, 240);
        g.drawString("Gold Per Hour: " + goldPerHour, defaultX, 260);
        Point p = ctx.input.getLocation();
        g1.setColor(Color.CYAN);
        g1.drawOval(p.x - 3, p.y - 3, 5, 5);
    }

    private String Time(long i) {
        DecimalFormat nf = new DecimalFormat("00");
        long hours = i / 3600000L;
        long millis = i - hours * 3600000L;
        long minutes = millis / 60000L;
        millis -= minutes * 60000L;
        long seconds = millis / 1000L;
        return nf.format(hours) + ":" + nf.format(minutes) + ":" + nf.format(seconds);
    }

    private String getTimeToNextLevel(int expLeft, int xpPerHour) {
        return xpPerHour < 1?"No EXP gained yet.":Time((long)((double)expLeft * 3600000.0D / (double)xpPerHour));
    }

    private enum State {
        SETTRAP,
        CHECKTRAP,
        PICKUP,
        RELEASE,
        BANK,
        IDLE,
        WALKING,
        SETUP
    }
}
