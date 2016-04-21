package Richardluk12.aio_agility;

/**
 * Created by Rich on 4/19/2016.
 */

import org.powerbot.script.*;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.*;
import org.powerbot.script.rt6.ClientContext;
import java.awt.*;
import java.util.concurrent.Callable;

@Script.Manifest(name="AgilityTrainer",
        description="Trains Agility at any location",
        properties="author=Richardluk12;" +
                "topic=1309370;" +
                "version=1.0")

public class AgilityTrainer extends PollingScript<ClientContext> implements MessageListener,PaintListener {

    public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);

    public static final int[] LOG_BALANCE_BOUNDS = {-128, 128, -256, 0, -128, 128};
    public static final int[] TREE_BRANCH_BOUNDS = {-50, 50, -128, -256, -128, 128};
    public static final int[] OBSTACLE_NET_BOUNDS = {-128, 128, -256, -256, -128, 128};
    public static final int[] BALANCING_ROPE_BOUNDS = {0, 0, 0, 0, 0, 0};
    public static final int[] OBSTACLE_PIPE_BOUNDS = {-128, 128, -256, 0, -128, 128};
    public static final int[] BASE_BOUNDS = {-128, 128, -256, 0, -128, 128};
    public static final int[] ROPE_SWING_BOUNDS = {0, 0, -256, 0, 0, 0};

    public Course[] Courses = {
            new Course( "Gnome Tree",
                new Tile(2408, 3352, 0),
                new Obstacle("Walk-across", "Log balance", 69526, LOG_BALANCE_BOUNDS, new FAIL(false), new Tile(2474, 3429, 0)),
                new Obstacle("Climb-over", "Obstacle net", 69383, OBSTACLE_NET_BOUNDS , new FAIL(false),
                        new Tile(2476, 3423, 1),new Tile(2475, 3423, 1),
                        new Tile(2474, 3423, 1),new Tile(2473, 3423, 1),
                        new Tile(2472, 3423, 1),new Tile(2471, 3423, 1)),
                new Obstacle("Climb", "Tree branch", 69508, TREE_BRANCH_BOUNDS, new FAIL(false), new Tile(2473, 3420, 2)),
                new Obstacle("Walk-on", "Balancing rope",2312, BALANCING_ROPE_BOUNDS, new FAIL(false), new Tile(2483, 3420, 2)),
                new Obstacle("Climb-down", "Tree branch", 69163,TREE_BRANCH_BOUNDS, new FAIL(false), new Tile(2487, 3417, 0)),
                new Obstacle("Climb-over", "Obstacle net", 69384, OBSTACLE_NET_BOUNDS, new FAIL(false),
                        new Tile(2483, 3427, 0), new Tile(2484, 3427, 0),
                        new Tile(2485, 3427, 0), new Tile(2486, 3427, 0),
                        new Tile(2487, 3427, 0), new Tile(2488, 3427, 0)),
                new Obstacle("Squeeze-through", "Obstacle pipe", 69378, OBSTACLE_PIPE_BOUNDS, new FAIL(false), new Tile(2483, 3437, 0))
            ),
            new Course("Watch Tower",
                    new Tile(2496, 3064, 0),
                    new Obstacle("Climb-up", "Trellis", 20056, BASE_BOUNDS, new FAIL(false), new Tile(2548, 3117, 1)),
                    new Obstacle("Climb-down", "Ladder", 17122, BASE_BOUNDS, new FAIL(false), new Tile(2544, 3112, 0))
            ),
            new Course("Barbarian Outpost",
                    new Tile(2496, 3496, 0),
                    new Obstacle("Swing-on", "Rope swing", 43526, ROPE_SWING_BOUNDS,
                            new FAIL(true,
                                    new Obstacle("Climb-up", "Ladder", 32015, BASE_BOUNDS, new FAIL(false), new Tile(2548, 3551, 0)),
                                    new Tile(2549, 9951, 0)),
                            new Tile(2551, 3549, 0), new Tile(2552, 3549, 0)),
                    new Obstacle("Walk-across", "Log balance", 43595, LOG_BALANCE_BOUNDS,
                            new FAIL(true,
                                    new Obstacle("Walk-across", "Log balance", 43595, LOG_BALANCE_BOUNDS, new FAIL(false), new Tile(2548, 3551, 0)),
                                    new Tile(2549, 9951, 0)),
                            new Tile(2541, 3546, 0)),
                    new Obstacle("Climb over", "Obstacle net", 20211, OBSTACLE_NET_BOUNDS,
                            new FAIL(false),
                            new Tile(2537, 3546, 1)),
                    new Obstacle("Walk-across", "Balancing ledge", 2302, BASE_BOUNDS,
                            new FAIL(true,
                                    new Obstacle("", "", 2378, OBSTACLE_NET_BOUNDS, new FAIL(false), new Tile(2537, 3546, 1)),
                                    new Tile(2534, 3545, 0)),
                            new Tile(2532, 3546, 1)),
                    new Obstacle("Climb-down", "Ladder", 3205, BASE_BOUNDS,
                            new FAIL(false),
                            new Tile(2532, 3546, 0)),
                    new Obstacle("Climb-over", "Crumbling wall", 1948, BASE_BOUNDS,
                            new FAIL(false),
                            new Tile(2538, 3553, 0)),
                    new Obstacle("Climb-over", "Crumbling wall", 1948, BASE_BOUNDS,
                            new FAIL(false),
                            new Tile(2543, 3553, 0))

            )
    };

    // Class for Pitch and Angle, because turnTo sometimes makes Obstacle not in view. *Future uses, maybe*
    class PNA{
        public int min;
        public int max;
        public PNA(int _min, int _max){
            this.min = _min;
            this.max = _max;
        }
    }


    public Course current_course;

    public Game gameStats = new Game(ctx);
    public Skills myStats = new Skills(ctx);
    public final Timer t = new Timer(0);

    public AgilityTrainer(){

    }

    @Override
    public void start(){
        current_course = getCourse();
    }

    @Override
    public void messaged(MessageEvent e) {
        final String msg = e.text().toLowerCase();
        if(e.source().isEmpty()){
            if(msg.contains("fall")){
                current_course.failed_ca = true;
            }
        }
    }

    @Override
    public void repaint(Graphics graphics) {

        final Graphics2D g = (Graphics2D) graphics;
        g.setFont(TAHOMA);

        g.setColor(Color.WHITE);
        if(current_course != null) {
            g.drawString(String.format("Current: %s", current_course.name), 10, 20);
            g.drawString(String.format("Action: %s", current_course.actions[current_course.current_action].oname), 10, 40);
            g.drawString(String.format("Length: %,d", current_course.actions.length), 10, 60);
            g.drawString(String.format("Compared: %b", ctx.players.local().inMotion()), 10, 80);
            g.drawString(String.format("mapOffset: %,d %,d, %,d", gameStats.mapOffset().x(), gameStats.mapOffset().y(), gameStats.mapOffset().floor()), 10, 100);

            g.drawString(String.format("Location: %s", ctx.players.local().tile()), 10, 120);
        } else {
            g.drawString(String.format("Current: %s", "Course is Null"), 10, 20);
        }
    }


    @Override
    public void poll() {
        if( ctx.players.local().inMotion() || current_course == null){
            return;
        }


        current_course.getAction(ctx.players.local().tile());

        if(current_course.failed_ca){
            GameObject obstacle = ctx.objects.select().id(current_course.actions[current_course.current_action].canFail.obstacle.oid).nearest().first().poll();
            if (!obstacle.valid() || !obstacle.inViewport()) {
                ctx.camera.turnTo(obstacle);
                return;
            }
            obstacle.bounds(current_course.actions[current_course.current_action].canFail.obstacle.bounds);
            obstacle.interact(current_course.actions[current_course.current_action].canFail.obstacle.action, obstacle.name());
            current_course.failed_ca = false;

        } else {
            boolean sameObs = false;
            if(current_course.current_action >= 1){
               sameObs = current_course.actions[current_course.current_action-1].oid ==current_course.actions[current_course.current_action].oid;
            }
            GameObject obstacle = ctx.objects.select().id(current_course.actions[current_course.current_action].oid).nearest().first().poll();
            if(sameObs){
                obstacle = ctx.objects.select().id(current_course.actions[current_course.current_action].oid).nearest().reverse().first().poll();
            }


            if (!obstacle.valid() || !obstacle.inViewport()) {
                ctx.camera.turnTo(obstacle);

                return;
            }
            obstacle.bounds(current_course.actions[current_course.current_action].bounds);
            obstacle.interact(current_course.actions[current_course.current_action].action, obstacle.name());
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inMotion();
            }
        }, 250, 10);
        /*
        int obstacle_x = obstacle.centerPoint().x;
        int obstacle_y = obstacle.centerPoint().y;

        ctx.input.click(new Point(obstacle_x, obstacle_y), true);
*/
    }

    private Course getCourse(){
        for(Course course : Courses){
            if(course.mapBase.tile().distanceTo(gameStats.mapOffset().tile()) <= 100){
                return course;
            }
        }
        return null;
    }


}