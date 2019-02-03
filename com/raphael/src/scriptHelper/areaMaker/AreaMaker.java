package scriptHelper.areaMaker;

import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Script.Manifest(name = "AreaMaker", description = "Will create an area based on the movements you make and print on suspend.",
        properties = "author=TheRaph; topic=999; client=4;")
public class AreaMaker extends PollingScript<ClientContext> implements PaintListener {
    private List<Tile> tilePath = new ArrayList<>();
    private boolean state;

    public AreaMaker() {
        super();

    }

    @Override
    public void poll() {
        if (state != ctx.movement.running()) {
            System.out.println(String.format("Adding %s to area", ctx.players.local().tile()));
            tilePath.add(ctx.players.local().tile());
            state = !state;
        }

    }

    @Override
    public void start() {
        super.start();
        System.out.println("Waiting on players input.");
        ctx.input.blocking(false);
        state = ctx.movement.running();

    }

    @Override
    public void suspend() {
        StringBuilder string = new StringBuilder("Area newArea = new Area(\n");
        for (Tile tile : tilePath) {
            string.append(String.format("new Tile%s,", tile));
        }
        string.append("\n);");
        System.out.println(string);
        super.suspend();
    }

    @Override
    public void stop() {
        StringBuilder string = new StringBuilder("Area newArea = new Area(\n");
        for (Tile tile : tilePath) {
            string.append(String.format("new Tile%s,", tile));
        }
        string.append("\n);");
        System.out.println(string);
        super.suspend();
    }

    @Override
    public void resume() {
        super.resume();
    }


    @Override
    public void repaint(Graphics graphics) {

    }
}
