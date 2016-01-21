package andyroo.agility;

import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.Random;
import org.powerbot.script.rt4.Camera;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Player;

import java.awt.*;


public class Antiban {
    static public synchronized void run(final ClientContext ctx) {
        System.out.println("Antiban start");
        int roll = Random.nextInt(0, 10 + 1);
        if (ctx.game.loggedIn()) {
            switch (roll) {
                case 0: {  // move camera
                    System.out.println("antiban 0");
                    adjustCamera(ctx);
                }
                break;
                case 1: {  // switch between inventory/stats tabs
                    System.out.println("antiban 1");

                    if (ctx.game.tab() == Game.Tab.INVENTORY) {
                        ctx.game.tab(Game.Tab.STATS);
                    } else ctx.game.tab(Game.Tab.INVENTORY);
                }
                break;
                case 2: {  // right click on a player
                    Point mouseLoc = ctx.input.getLocation();
                    System.out.println("antiban 2");

                    if (ctx.players.select(new Filter<Player>() {
                        @Override
                        public boolean accept(Player player) {
                            return player.name().compareTo(ctx.players.local().name()) != 0;
                        }
                    }).viewable().peek().valid()) { // if player is not local player, and is on the screen

                        System.out.println(ctx.players.poll().name());
                        ctx.players.poll().click(false);
                        Condition.sleep(Random.getDelay());
                        ctx.input.move(mouseLoc.x + Random.nextInt(-10, 10), mouseLoc.y - Random.nextInt(5, 20));
                    }
                }
                break;
                default:
                    break;
            }
        }
        System.out.println("Antiban stop");
    }

    static private void adjustCamera(ClientContext ctx) {
        Camera cam = ctx.camera;

        if (Random.nextInt(0, 10) == 0)
            cam.pitch(Random.nextInt(50, 100));
        else {
            cam.angle(Random.nextGaussian(0, 360, cam.yaw(), 40));
            cam.pitch(true);
        }
    }

}
