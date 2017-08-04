package nomivore.ArrowFletcher;

import org.powerbot.script.*;
import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.ClientContext;
import scripts.ID;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Script.Manifest(
        name = "ArrowFletcher", properties = "author=Nomivore; topic=1336089; client=4;",
        description = "Fletches headless arrows and tips them")

public class ArrowFletcher extends PollingScript<ClientContext> implements PaintListener, MessageListener
{
    public static List<Task> taskList = new ArrayList<Task>();

    public static int profit;

    public void start()
    {
        taskList.add(new ShaftFletch(ctx));
        taskList.add(new ArrowFletch(ctx));
        for (Task task : taskList) {
                task.initialise();
        }
    }


    public void poll()
    {
        boolean cont = false;
        for (Task task : taskList) {
            if (task.activate()) {
                task.execute();
                cont = true;
                break;
            }
        }
        int sum = 0;
        for (Task task : taskList) {
            sum += task.profit();
        }
        profit = sum;
        if (!cont) {
            ctx.controller.stop();
        }
    }


    @Override
    public void messaged(MessageEvent me) {
        for (Task task : taskList) {
            if (task.activate()) {
                task.message(me);
                break;
            }
        }
    }

    private static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);

    public void repaint(Graphics graphics)
    {
        final Graphics2D g = (Graphics2D) graphics;
        g.setFont(TAHOMA);

        int s = (int)Math.floor(getRuntime()/1000 % 60);
        int m = (int)Math.floor(getRuntime()/60000 % 60);
        int h = (int)Math.floor(getRuntime()/3600000);
        int profitHr = (int)(profit*3600000D/getRuntime());

        int exp = ArrowFletch.getExperience();
        int expHr = (int)(exp*3600000D/getRuntime());

        g.setColor(Color.WHITE);
        g.drawString(String.format("Runtime %02d:%02d:%02d", h, m, s), 10, 120);
        g.drawString(String.format("Profit %d, per hr %d", profit, profitHr), 10, 140);
        g.drawString(String.format("Exp %d, per hr %d", exp, expHr), 10, 160);
        int yAxis = 180;
        int height = 80;
        try {
            for (Task task : taskList) {
                g.drawString(String.format("%s", task.getActionName()), 10, yAxis);
                yAxis += 20;
                height += 20;
            }
        } catch (NoClassDefFoundError e) {
        }

        g.setColor(Color.BLACK);
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
        g.setComposite(alphaComposite);
        g.fillRect(5, 100, 185, height);
    }

}