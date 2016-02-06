package andyroo.util;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.*;

import java.awt.*;


public class Antiban {
    public static synchronized void run(final ClientContext ctx) {
        System.out.println("Antiban start");
        int roll = Random.nextInt(0, 15 + 1);
        if(ctx.game.loggedIn()) {
            switch (roll) {
                case 0: {  // move camera
                    System.out.println("antiban 0");
                    adjustCamera(ctx);
                }
                    break;
                case 1: {  // switch between inventory/stats tabs
                    System.out.println("antiban 1");

                    ctx.game.tab(Game.Tab.STATS);
                    Condition.sleep();
                    ctx.game.tab(Game.Tab.INVENTORY);
                }
                    break;
                /*
                case 2: {  // right click on random player
                    Point mouseLoc = ctx.input.getLocation();
                    System.out.println("antiban 2");

                    if (ctx.players.select(new Filter<Player>() {
                        @Override
                        public boolean accept(Player player) {
                            return player.name().compareTo(ctx.players.local().name()) != 0;
                        }
                    }).viewable().shuffle().peek().valid()) { // if player is not local player, and is on the screen

                        System.out.println(ctx.players.poll().name());
                        ctx.players.poll().click(false);
                        Condition.sleep();
                        ctx.input.move(mouseLoc.x + Random.nextInt(-20, 20), mouseLoc.y - Random.nextInt(10, 30));
                    }
                }
                    break;
                */
                case 3: { // right click random object

                    Point mouseLoc = ctx.input.getLocation();
                    System.out.println("antiban 3");

                    if(ctx.objects.select(5).select(new Filter<GameObject>() {
                        @Override
                        public boolean accept(GameObject gameObject) {
                            return gameObject.name().compareTo("null") != 0;
                        }
                    }).viewable().peek().valid()) {
                        System.out.println(ctx.objects.peek().name());
                        ctx.objects.poll().interact(false, "Examine");
                        Condition.sleep();
                        ctx.input.move(mouseLoc.x + Random.nextInt(-20, 20), mouseLoc.y - Random.nextInt(10, 30));
                    }
                }
                case 4: { // right click random npc
                    if(ctx.npcs.select().viewable().shuffle().peek().valid()) {
                        ctx.npcs.peek().interact("Examine");
                        Condition.sleep();
                    }

                }
                default:
                    break;
            }
        }
        System.out.println("Antiban stop");
    }


    public static void doAntiban(final ClientContext ctx) {
        new Thread(new Runnable() {
            public void run() {
                Antiban.run(ctx);
            }
        }).start();
    }

    private static void adjustCamera(ClientContext ctx) {
        Camera cam = ctx.camera;

        if (Random.nextInt(0, 10) < 3)
            cam.pitch(Random.nextInt(90, 100));
        else {
            cam.angle(Random.nextGaussian(0, 360, cam.yaw(), 40));
            cam.pitch(true);
        }
    }

}
