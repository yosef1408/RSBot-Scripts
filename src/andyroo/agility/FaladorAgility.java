package andyroo.agility;

import andyroo.util.Antiban;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

import java.awt.*;
import java.util.concurrent.Callable;

@Script.Manifest(
        name = "Fally Agility", properties = "author=andyroo; topic=1298690; client=4;",
        description = "v 2.2 - Completes Falador agility course"
)

/**
 * Changelog
 *
 * Fally Agility
 * v 2.2
 * Random camera angle for starting obstacle (wall)
 * Paint proggy
 *
 */

public class FaladorAgility extends PollingScript<ClientContext> implements PaintListener {

    /***********************************
     * Constants
     ***********************************/


    private static final Tile START_TILE = new Tile(3036, 3340, 0);

    private static final Area FALLY_START_AREA = new Area(new Tile(3033, 3337, 0), new Tile(3041, 3346, 0));

    private static final Area[] fallyAreas = {
            new Area(new Tile(3035, 3344, 3), new Tile(3041, 3341, 3)),
            new Area(new Tile(3052, 3341, 3), new Tile(3044, 3350, 3)),
            new Area(new Tile(3047, 3357, 3), new Tile(3051, 3359, 3)),
            new Area(new Tile(3044, 3361, 3), new Tile(3049, 3368, 3)),
            new Area(new Tile(3042, 3365, 3), new Tile(3033, 3360, 3)),
            new Area(new Tile(3025, 3351, 3), new Tile(3030, 3355, 3)),
            new Area(new Tile(3008, 3352, 3), new Tile(3022, 3359, 3)),
            new Area(new Tile(3015, 3342, 3), new Tile(3023, 3350, 3)),
            new Area(new Tile(3010, 3343, 3), new Tile(3015, 3347, 3)),
            new Area(new Tile(3008, 3334, 3), new Tile(3014, 3343, 3)),
            new Area(new Tile(3010, 3330, 3), new Tile(3018, 3335, 3)),
            new Area(new Tile(3018, 3331, 3), new Tile(3025, 3336, 3))
    };

    private static final int ROUGH_WALL_ID = 10833;

    private static final int MARK_ID = 11849;

    private static final int[] ROUGH_WALL_BOUNDS = {16, 96, -192, -68, 100, 140};

    private static final Obstacle[] FALLY_OBSTACLES = {
            new Obstacle(10834, Obstacle.Type.TIGHTROPE, new int[]{64, 120, 28, 68, 44, 128}),
            new Obstacle(10836, Obstacle.Type.HANDHOLDS, new int[]{-28, 28, -12, 60, 60, 120}),
            new Obstacle(11161, Obstacle.Type.GAP),
            new Obstacle(11360, Obstacle.Type.GAP),
            new Obstacle(11361, Obstacle.Type.TIGHTROPE, new int[]{-68, 4, 0, 76, -4, 72}),
            new Obstacle(11364, Obstacle.Type.TIGHTROPE, new int[]{0, 60, -68, 8, 44, 116}),
            new Obstacle(11365, Obstacle.Type.GAP),
            new Obstacle(11366, Obstacle.Type.LEDGE),
            new Obstacle(11367, Obstacle.Type.LEDGE),
            new Obstacle(11368, Obstacle.Type.LEDGE),
            new Obstacle(11370, Obstacle.Type.LEDGE),
            new Obstacle(11371, Obstacle.Type.EDGE)
    };

    /******************************************************************************************/

    private long startTime;
    private int startXP;
    private int startMarkCount;
    private int currentMarkCount;
    private int currentXP;
    private static String version = "2.2";

    private Area currentArea;
    private Location location;
    private int obstacleNum;
    private int energyThreshold;

    private static final int GUI_X = 350;
    private static final int GUI_Y = 339;
    private static final int GUI_WIDTH = 519;
    private static final int GUI_HEIGHT = 138;

    /******************************************************************************************/


    // output time elapsed from ms to hh:mm:ss
    public static String getTimeElapsed(long ms) {
        long sec, min, hr;

        sec = (ms / 1000) % 60;
        min = ((ms / 1000) / 60) % 60;
        hr = ((ms / 1000) / 60) / 60;

        return String.format("%02d:%02d:%02d", hr, min, sec);
    }

    public void updateInfo() {
        ctx.game.tab(Game.Tab.INVENTORY);
        if (ctx.inventory.select().id(MARK_ID).peek().valid())
            currentMarkCount = ctx.inventory.select().id(MARK_ID).poll().stackSize();
        else
            currentMarkCount = 0;
        currentXP = ctx.skills.experience(Constants.SKILLS_AGILITY);
    }

    public void repaint(Graphics graphics) {
        int x = GUI_X;
        int y = GUI_Y;
        graphics.setColor(new Color(0, 0, 0));
        graphics.fillRect(x, y, GUI_WIDTH - GUI_X, GUI_HEIGHT);
        graphics.setColor(new Color(255, 255, 255));
        x += 10;
        graphics.drawString("Version: " + version, x, y += 15);
        graphics.drawString(getTimeElapsed(System.currentTimeMillis() - startTime), x, y += 15);
        graphics.drawString("XP Gained: " + Integer.toString(currentXP - startXP), x, y += 15);
        graphics.drawString("Marks collected: " + Integer.toString(currentMarkCount - startMarkCount), x, y += 15);
    }

    public void start() {
        ctx.camera.pitch(true);
        energyThreshold = Random.nextInt(30, 60);
        if (ctx.inventory.select().id(MARK_ID).peek().valid())
            startMarkCount = ctx.inventory.poll().stackSize();
        else startMarkCount = 0;
        startXP = ctx.skills.experience(Constants.SKILLS_AGILITY);
        updateInfo();
        startTime = System.currentTimeMillis();
    }


    public void stop() {
        updateInfo();
        String totalTime = getTimeElapsed(System.currentTimeMillis() - startTime);

        log.info(version);
        log.info("Time run: " + totalTime);
        log.info("XP Gained: " + Integer.toString(currentXP - startXP));
        log.info("Marks collected: " + Integer.toString(currentMarkCount - startMarkCount));
    }


    // check for marks of grace
    private boolean markCheck() {
        final GroundItem mark = ctx.groundItems.select(10).id(MARK_ID).poll();

        if (!mark.valid())
            return true;

        if (currentArea != null && currentArea.contains(mark.tile())) {
            log.info("mark found");
            if (mark.inViewport()) {
                final int beforeCount;
                if (ctx.inventory.select().id(MARK_ID).peek().valid()) {
                    beforeCount = ctx.inventory.poll().stackSize();
                } else beforeCount = 0;

                if (mark.click("Take", Game.Crosshair.ACTION)) {
                    log.info("Attempt to pick up mark");
                    return Condition.wait(new Callable<Boolean>() {
                        public Boolean call() throws Exception {
                            return ctx.inventory.select().id(MARK_ID).poll().stackSize() > beforeCount && !mark.valid();
                        }
                    }, 300, 10);
                }


            } else if (mark.valid()) {
                log.info("walk to mark");
                ctx.movement.step(mark.tile());
            }
        } else return true; // mark is in different area

        return false;
    }


    private void waitObstacle() {
        new Thread(new Runnable() {
            public void run() {
                Antiban.run(ctx);
            }
        }).start();

        Condition.wait(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Player me = ctx.players.local();
                log.info("Waiting to animate");
                return me.animation() != -1;
            }
        }, 250, 8);
        Condition.wait(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Player me = ctx.players.local();
                log.info("Waiting to stop moving");
                return !me.inMotion();
            }
        }, 250, 8);
    }


    private void waitMovement() {
        Condition.wait(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Player me = ctx.players.local();
                //log.info("waiting to stop moving");
                return !me.inMotion();
            }
        }, 250, 8);
    }

    public void poll() {
        boolean obstacleFound = false;
        State s = state();

        if (!markCheck()) {
            log.info("Failed to pick up mark");
            return;
        } else log.info("Picked up mark");
        updateInfo();

        switch (s) {
            case INVALID: {
                Condition.sleep();
            }
            break;

            case RUN_TOGGLE: {
                ctx.movement.running(true);
                energyThreshold = Random.nextInt(30, 60);
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return ctx.movement.running();
                    }
                }, 250, 4);
            }
            break;

            case FELL: {
                ctx.movement.step(START_TILE);
                Condition.wait(new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return START_TILE.distanceTo(ctx.players.local()) < 5;
                    }
                }, 300, 10);
            }
            break;

            case START_POINT: {
                GameObject roughWall = ctx.objects.select(10).id(ROUGH_WALL_ID).poll();

                if (roughWall.valid()) {
                    if (roughWall.inViewport()) {
                        if (Random.nextInt(0, 2) == 0)
                            ctx.camera.angle(Random.nextInt(-30, 30 + 1));
                        else ctx.camera.angle(Random.nextInt(150, 210 + 1));
                        roughWall.bounds(ROUGH_WALL_BOUNDS);
                        log.info("rough wall found");
                        if (roughWall.click("Climb", Game.Crosshair.ACTION)) {

                            Condition.wait(new Callable<Boolean>() {
                                public Boolean call() throws Exception {
                                    Player me = ctx.players.local();
                                    return me.tile().floor() == 3;
                                }
                            }, 250, 10);
                        }
                    } else {
                        ctx.camera.angle(0);
                        ctx.camera.angle(180);
                        ctx.movement.step(START_TILE);
                        Condition.wait(new Callable<Boolean>() {
                            public Boolean call() throws Exception {
                                return START_TILE.distanceTo(ctx.players.local()) < 3;
                            }
                        }, 250, 6);

                    }
                }
            }
            break;

            case OBSTACLE: {
                GameObject obstacleObject = null;
                Obstacle obstacleInfo = FALLY_OBSTACLES[obstacleNum];

                if (location == Location.FALADOR)
                    obstacleObject = ctx.objects.select(10).id(obstacleInfo.getId()).poll();

                if (obstacleObject == null) {
                    log.info("Obstacle not found");
                    break;
                }

                if (obstacleObject.valid()) {
                    if (obstacleObject.inViewport()) {
                        log.info("Obstacle " + obstacleNum + " found");

                        if (obstacleInfo.getBounds() != null) {
                            obstacleObject.bounds(obstacleInfo.getBounds());
                        }
                        if (obstacleObject.click(obstacleInfo.getAction(), Game.Crosshair.ACTION)) {
                            obstacleFound = true;
                        }
                    } else {
                        log.info("Move to obstacle " + obstacleNum);

                        final Tile obstacleTile = obstacleObject.tile();

                        ctx.movement.step(obstacleTile);
                        Condition.wait(new Callable<Boolean>() {
                            public Boolean call() throws Exception {
                                return obstacleTile.distanceTo(ctx.players.local()) < 3;
                            }
                        }, 250, 6);
                    }
                }
            }
            break;

            default:
                break;
        }

        if (obstacleFound) {
            waitObstacle();
        }
    }


    private State state() {
        if (!ctx.game.loggedIn()) {
            return State.INVALID;
        }

        Player me = ctx.players.local();

        if (!ctx.movement.running() && (ctx.movement.energyLevel() > energyThreshold)) {
            log.info("Toggle run");
            return State.RUN_TOGGLE;
        }

        if (FALLY_START_AREA.contains(me.tile())) {
            log.info("On the ground");

            currentArea = FALLY_START_AREA;
            return State.START_POINT;
        }

        if (me.tile().floor() == 0) {
            log.info("Fell");
            return State.FELL;
        }

        obstacleNum = -1;
        for (int i = 0; i < fallyAreas.length; i++) {
            if (fallyAreas[i].contains(me.tile())) {
                log.info("Obstacle " + i);

                obstacleNum = i;
                currentArea = fallyAreas[i];
                location = Location.FALADOR;
                break;
            }
        }

        if (obstacleNum != -1) {
            return State.OBSTACLE;
        }

        return State.INVALID;
    }


    private enum State {
        RUN_TOGGLE, START_POINT, OBSTACLE, INVALID, FELL
    }

    private enum Location {
        FALADOR
    }
}
