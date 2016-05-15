package superchaoran.HerbsUlitimate;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;
import superchaoran.HerbsUlitimate.constants.Herb;
import superchaoran.HerbsUlitimate.constants.Method;
import superchaoran.HerbsUlitimate.constants.MethodChosen;
import superchaoran.HerbsUlitimate.constants.UnfPotion;
import superchaoran.HerbsUlitimate.gui.GUI;
import superchaoran.HerbsUlitimate.jobs.Bank;
import superchaoran.HerbsUlitimate.jobs.CleanHerb;
import superchaoran.HerbsUlitimate.jobs.MakeUnfPotion;
import superchaoran.HerbsUlitimate.painter.Detail;
import superchaoran.HerbsUlitimate.painter.Painter;
import superchaoran.HerbsUlitimate.script.Job;
import superchaoran.HerbsUlitimate.script.JobContainer;
import superchaoran.HerbsUlitimate.script.Script;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static superchaoran.HerbsUlitimate.constants.Method.MakeUnfPotion;

/**
 * Created by chaoran on 5/12/16.
 */

@Script.Manifest(name = "Herbs Ultimate", description = "[0.4-0.7m/hour];05/15/2016;Show Realtime Price/Profit;Support All Herbs;Cleaning/Making (UNF)", properties = "version=1.00;topic=1311952;author=superchaoran;")
public class HerbsUltimateMain extends Script<ClientContext> implements PaintListener{
    private JobContainer container;
    private final Painter paint = new Painter(this);
    private long startTime;
    private MethodChosen methodChosen;
    private GUI gui;

    @Override
    public void start() {
        /* Dont pull all price at UI thread or main thread, only pull from background
         * If the graphic files are loaded from an initial thread, there may be a delay before the GUI appears.
         * If the graphic files are loaded from the event dispatch thread, the GUI may be temporarily unresponsive.
         */

        SwingWorker worker = new SwingWorker<Void, Void>() {

            @Override
            public Void doInBackground() {
                gui.setLoadingJLabel("Loading complete. All price/profit are matching market.");
                gui.getContentPane().repaint();
                for (Herb i : Herb.values()){
                    i.setGrimyPrice(new GeItem(i.getGrimyId()).price);
                    i.setCleanPrice(new GeItem(i.getGrimyId()).price);
                    i.getUnitProfit();
                }

                for (UnfPotion i : UnfPotion.values()){
                    i.getUnitProfit();
                }
                gui.setLoadingJLabel("Loading complete. All price/profit are matching market.");
                gui.getContentPane().repaint();
                return null;
            }

        };



        container = new JobContainer();
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                gui = new GUI(HerbsUltimateMain.this, ctx);
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
        if(methodChosen!=null) {
            Method method = methodChosen.getMethod();
            if (method != null) {
                switch (method) {
                    case CleanHerb:
                        Herb herb = methodChosen.getHerb();
                        if (methodChosen.getHerb() != null) {
                            int totalProfit = herb.getUnitProfit() * herb.getNumberCleaned();
                            int profitPerHour = (int) ((3600000D * totalProfit) / (System.currentTimeMillis() - startTime));
                            paint.draw(g,
                                    new Detail(herb.name()).setObject(""),
                                    new Detail("Number cleaned: ").setObject(herb.getNumberCleaned()),
                                    new Detail("Unit Profit:").setObject(herb.getUnitProfit()),
                                    new Detail("Total Profit:").setObject(totalProfit),
                                    new Detail("Profit/h:").setObject(profitPerHour)
                            );
                        }
                        break;
                    case MakeUnfPotion:
                        UnfPotion unfPotion = methodChosen.getUnfPotion();
                        if (unfPotion != null) {
                            int totalProfit = unfPotion.getUnitProfit() * unfPotion.getNumberMade();
                            int profitPerHour = (int) ((3600000D * totalProfit) / (System.currentTimeMillis() - startTime));
                            paint.draw(g,
                                    new Detail(unfPotion.name()).setObject(""),
                                    new Detail("Number made: ").setObject(unfPotion.getNumberMade()),
                                    new Detail("Unit Profit:").setObject(unfPotion.getUnitProfit()),
                                    new Detail("Total Profit:").setObject(totalProfit),
                                    new Detail("Profit/h:").setObject(profitPerHour)
                            );
                        }
                }
            }
        }
    }

    public void submit(MethodChosen methodChosen) {
        if(methodChosen!=null) {
            this.methodChosen = methodChosen;
            Method method = methodChosen.getMethod();
            switch(method){
                case CleanHerb:
                    container = new JobContainer(new Bank(this, methodChosen), new CleanHerb(this, methodChosen));
                    break;
                case MakeUnfPotion:
                    container = new JobContainer(new Bank(this, methodChosen), new MakeUnfPotion(this, methodChosen));
                    break;
            }
            this.startTime = System.currentTimeMillis();
        }
    }

    public void setMethodChosen(MethodChosen methodChosen){
        this.methodChosen = methodChosen;
    }

    public MethodChosen getMethodChosen(){
        return methodChosen;
    }
}
