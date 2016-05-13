package superchaoran.GraniteSplitter;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.rt6.ClientContext;

import org.powerbot.script.rt6.GeItem;
import superchaoran.GraniteSplitter.constants.GraniteProduct;
import superchaoran.GraniteSplitter.gui.GUI;
import superchaoran.GraniteSplitter.constants.GraniteRaw;
import superchaoran.GraniteSplitter.jobs.Bank;
import superchaoran.GraniteSplitter.jobs.Spliter;
import superchaoran.GraniteSplitter.painter.Detail;
import superchaoran.GraniteSplitter.painter.Painter;
import superchaoran.GraniteSplitter.script.Job;
import superchaoran.GraniteSplitter.script.JobContainer;
import superchaoran.GraniteSplitter.script.Script;


import java.awt.*;

/**
 * Created by chaoran on 5/12/16.
 */

@Script.Manifest(name = "Granite Splitter Ultimate", description = "(0.5m/hour) Money Making: Splites granite5kg or granite 2kg", properties = "version=1.00;topic=1311676;author=superchaoran;")
public class GraniteSpliterMain extends Script<ClientContext> implements PaintListener, MessageListener {
    private JobContainer container = new JobContainer();
    private int graniteCrafted = 0;
    private final Painter paint = new Painter(this);
    private final Detail type = new Detail("Type");
    private final Detail numberSplittedDetail = new Detail("number splitted: ");
    private GraniteRaw graniteType;
    public int numberSplitted = 0;
    long startTime;
    @Override
    public void start() {
        for (GraniteRaw n : GraniteRaw.values()) {
            n.setPrice(new GeItem(n.id()).price);
        }
        for (GraniteProduct n : GraniteProduct.values()) {
            n.setPrice(new GeItem(n.id()).price);
        }
        GraniteRaw.Granite5kg.setUnitProfit( (GraniteProduct.Granite500g.price*26 + GraniteRaw.Granite2kg.price - GraniteRaw.Granite5kg.price *3)/3 );
        GraniteRaw.Granite2kg.setUnitProfit(GraniteProduct.Granite500g.price*4 -GraniteRaw.Granite2kg.price);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI(GraniteSpliterMain.this, ctx);
                gui.setVisible(true);

            }
        });
    }

    @Override
    public void poll() {
        Job j = container.get();
        if (j != null) {
            j.execute();
        }
    }

    @Override
    public void repaint(Graphics g) {
        if(graniteType != null) {
            int totalProfit = graniteType.unitProfit * numberSplitted;
            int profitPerHour = (int) ((3600000D * totalProfit) / (System.currentTimeMillis() - startTime));
            paint.draw(g, type.setObject(graniteType.toString()), numberSplittedDetail.setObject(numberSplitted),
                    new Detail("Unit Profit:").setObject(graniteType.unitProfit),
                    new Detail("Total Profit:").setObject(totalProfit),
                    new Detail("Profit/h:").setObject(profitPerHour)
            );
        }
    }

    public void submit(GraniteRaw graniteRaw) {
        graniteType = graniteRaw;
        container = new JobContainer(new Bank(this, graniteRaw),new Spliter(this, graniteRaw));
        this.startTime = System.currentTimeMillis();
    }
    @Override
    public void messaged(MessageEvent m) {
//        String msg = m.text();
//        if (msg.equals("You add a log to the fire.") || msg.equals("The fire catches and the logs begin to burn."))
//            graniteCrafted++;
//        else if (msg.equals("The fire spirit gives you a reward to say thank you for freeing it, before disappearing."))
//            graniteCrafted++;
    }
}
