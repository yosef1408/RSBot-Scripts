package scriptHelper.pathGenerator;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name = "PathGenerator", description = "Will create a path based on the movements you make and print on suspend.",
        properties = "author=TheRaph; topic=999; client=4;")
public class PathGenerator extends PollingScript<ClientContext> {
    private List<Tile> tilePath = new ArrayList<>();
    private Tile lastPath;
    private Mode mode = Mode.DESTINATION;

    public PathGenerator() {
        super();

    }

    @Override
    public void poll() {
        State state = getState();
        switch (mode) {
            case DESTINATION:
                switch (state) {
                    case WALKING:
                        if (ctx.movement.destination().nil() == ctx.movement.destination())
                            break;
                        if (lastPath == null) {

                            tilePath.add(ctx.movement.destination());
                            lastPath = ctx.movement.destination();
                            System.out.println(String.format("Adding starting destination as %s", lastPath));
                        } else if (ctx.movement.destination().x() != lastPath.x() || ctx.movement.destination().y() != lastPath.y()) {
                            System.out.println("Adding new destination to tilePath");
                            tilePath.add(ctx.movement.destination());
                            lastPath = ctx.movement.destination();
                        }
                        break;
                    case IDLE:
                        break;
                }
                break;

            case WALKING_IDLE:
                switch (state) {
                    case WALKING:
                        break;
                    case IDLE:
                        Tile currentTile = ctx.players.local().tile();
                        if (currentTile.x() != lastPath.x() || currentTile.y() != lastPath.y()) {
                            System.out.println(String.format("Current tile: %s\nLast tile: %s", ctx.players.local().tile(), lastPath.tile()));
                            tilePath.add(currentTile);
                            lastPath = ctx.players.local().tile();
                            System.out.println(String.format("Updated lastPath to %s", lastPath));
                        }
                }
        }

    }

    @Override
    public void start() {
        super.start();
        if (mode == Mode.WALKING_IDLE) {
            lastPath = ctx.players.local().tile();
            tilePath.add(lastPath);
            System.out.println(String.format("Set starting path to %s", lastPath));
        }

        System.out.println("Waiting on player to move.");
        ctx.input.blocking(false);
    }

    @Override
    public void suspend() {
        StringBuilder string = new StringBuilder("Tile[] tilePath = {\n");
        for (Tile tile : tilePath) {
            string.append(String.format("new Tile%s,", tile));
        }
        string.append("\n};");
        System.out.println(string);
        super.suspend();
    }

    @Override
    public void stop() {
        StringBuilder string = new StringBuilder("Tile[] tilePath = {\n");
        for (Tile tile : tilePath) {
            string.append(String.format("new Tile%s,", tile));
        }
        string.append("\n};");
        System.out.println(string);
        super.stop();
    }

    @Override
    public void resume() {
        super.resume();
    }

    private State getState() {
        if (ctx.players.local().inMotion())
            return State.WALKING;
        else
            return State.IDLE;
    }

    private enum Mode {
        DESTINATION,
        WALKING_IDLE
    }

    private enum State {
        WALKING,
        IDLE,
    }
}
