package iDzn.ConstructUp;

import iDzn.ConstructUp.Tasks.*;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Callable;


@Script.Manifest(name= "ConstructUp", description="Trains construction", properties="client=4; author=iDzn; topic=1341359;")

public class ConstructUp extends PollingScript<ClientContext> implements PaintListener, MouseListener{
    private Image bg = null;
    private Image bg2 = null;
    private Image bg3 = null;
    public int Obj = 0, ObjSpace = 0, Planks = 0, idleTimer = 0, nPlanks = 0, PlanksRequired = 0, SleepX = 0, SleepY = 0, Waiting, leaveTrue,
               time = 0, startTime = 0, time1 = 0, time2 = 0, time3 = 0, time4 = 0, xpGained, xpStart, xpCurrent, levelCurrent, PaintXpGained,
               lvlStart, lvlGained, xMouse=0, yMouse=0, xpPerBuild, itemsMade, planksUsed;
    public Area PhialsArea = new Area(new Tile(2945, 3209, 0), new Tile(2955, 3228, 0));
    private Npc Butler;
    GameObject DoorHotspot = ctx.objects.select().id(15316, 15313, 15314, 15307, 15308, 15309, 15310, 15311, 15312, 15305, 13506).nearest().poll();
    public Component BuildNameWidget, WrongWidget, BuildWidget;
    public String WidgetText, NotedWidgetText, BuildName;
    public int[] ObjBounds = {0, 0, 0, 0, 0, 0};
    public final int[] ChairBounds = {-28, 22, -124, 0, -26, 18};
    public final int[] BookBounds = {-50, 58, -180, 0, -9, 50};
    public final int[] TableBounds = {-231, 207, -77, 0, -78, 88};
    public final int[] LarderBounds = {-69, 88, -201, 0, -114, 86};
    public final int[] DoorBounds = {-102, 93, -200, 0, -2, 36};
    public long getRunTime() {
        return getRuntime();
    }


    private ArrayList<Task> taskList = new ArrayList<Task>();

    @Override
    public void start() {
        bg = downloadImage("http://i.imgur.com/lbZtawo.png");
        bg2 = downloadImage("http://i.imgur.com/52bYcAb.png");
        bg3 = downloadImage("http://i.imgur.com/PW8UH6k.png");
        xpStart = ctx.skills.experience(Constants.SKILLS_CONSTRUCTION);
        lvlStart = ctx.skills.realLevel(Constants.SKILLS_CONSTRUCTION);

      final JFrame frame = new JFrame();

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        frame.getContentPane().add(panel);

        JPanel panel4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel4.setSize(200, 130);
        panel4.setLocation(20, 130);
        frame.getContentPane().add(panel4);

        JPanel panel5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel5.setSize(200, 225);
        panel5.setLocation(530, 160);
        frame.getContentPane().add(panel5);

        JPanel panel1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel1.setSize(200, 225);
        panel1.setLocation(20, 50);
        frame.getContentPane().add(panel1);

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel2.setSize(200, 225);
        panel2.setLocation(275, 50);
        frame.getContentPane().add(panel2);

        JPanel panel3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel3.setSize(200, 225);
        panel3.setLocation(530, 50);
        frame.getContentPane().add(panel3);

        JPanel panel6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        frame.getContentPane().add(panel6);
        JButton button = new JButton("Start");
        panel6.add(button);

        JLabel label = new JLabel("Please select your options or visit the thread for help.");
        panel6.add(label);

        JLabel Butlerlabel = new JLabel("      Butler or Phials?");
        Font font = new Font("", Font.BOLD,15);
        Butlerlabel.setFont(font);
        panel1.add(Butlerlabel);

        final JCheckBox Butler = new JCheckBox("Butler (Butler/Demon)");
        panel1.add(Butler);

        final JCheckBox Phials = new JCheckBox("Phials");
        panel1.add(Phials);

        JLabel Chairlabel = new JLabel("      Chairs      ");
        Chairlabel.setFont(font);
        panel2.add(Chairlabel);

        final JCheckBox CrudeChair = new JCheckBox("Crude Chair");
        panel2.add(CrudeChair);

        final JCheckBox WoodenChair = new JCheckBox("Wooden Chair");
        panel2.add(WoodenChair);

        final JCheckBox RockingChair = new JCheckBox("Rocking Chair");
        panel2.add(RockingChair);

        final JCheckBox OakChair = new JCheckBox("Oak Chair");
        panel2.add(OakChair);

        final JCheckBox OakArmChair = new JCheckBox("Oak Armchair");
        panel2.add(OakArmChair);

        final JCheckBox TeakChair = new JCheckBox("Teak Chair");
        panel2.add(TeakChair);

        final JCheckBox MahogChair = new JCheckBox("Mahogany Chair");
        panel2.add(MahogChair);

        JLabel Booklabel = new JLabel("      Bookcases      ");
        Booklabel.setFont(font);
        panel3.add(Booklabel);

        final JCheckBox WoodenBook = new JCheckBox("Wooden Bookcase");
        panel3.add(WoodenBook);

        final JCheckBox OakBook = new JCheckBox("Oak Bookcase");
        panel3.add(OakBook);

        final JCheckBox MahogBook = new JCheckBox("Mahogany Bookcase");
        panel3.add(MahogBook);

        JLabel Larderlabel = new JLabel("      Larders      ");
        Larderlabel.setFont(font);
        panel4.add(Larderlabel);

        final JCheckBox WoodenLarder = new JCheckBox("Wooden larder");
        panel4.add(WoodenLarder);

        final JCheckBox OakLarder = new JCheckBox("Oak larder");
        panel4.add(OakLarder);

        JLabel Extralabel = new JLabel("      Miscellaneous      ");
        Extralabel.setFont(font);
        panel4.add(Extralabel);

        final JCheckBox OakDungDoor = new JCheckBox("Oak dungeon door");
        panel4.add(OakDungDoor);

        JLabel Tablelabel = new JLabel("      Tables      ");
        Tablelabel.setFont(font);
        panel5.add(Tablelabel);

        final JCheckBox WoodTable = new JCheckBox("Wood table");
        panel5.add(WoodTable);

        final JCheckBox OakTable = new JCheckBox("Oak table");
        panel5.add(OakTable);

        final JCheckBox COakTable = new JCheckBox("Carved oak table");
        panel5.add(COakTable);

        final JCheckBox TeakTable = new JCheckBox("Teak table");
        panel5.add(TeakTable);

        final JCheckBox MahogTable = new JCheckBox("Mahogany table");
        panel5.add(MahogTable);

        frame.setSize(new Dimension(800, 400));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle("Select Options");
        frame.setVisible(true);

       button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taskList.add(new FailSafe(ctx, ConstructUp.this));
                taskList.add(new HouseNavigation(ctx, ConstructUp.this));
                taskList.add(new CCWidgets(ctx, ConstructUp.this));
                if (!Phials.isSelected() && !Butler.isSelected()) {
                    return;
                }
                if (Phials.isSelected()) {
                    leaveTrue = 1;
                    taskList.add(new Phials(ctx, ConstructUp.this));

                } else if (Butler.isSelected()) {
                    taskList.add(new Butler(ctx, ConstructUp.this));
                }
                 if (CrudeChair.isSelected()) {
                    Obj = 6752;
                    ObjSpace = 4515;
                    BuildName = "Chair space";
                    Planks = 960;
                    nPlanks = 961;
                    PlanksRequired = 2;
                    ObjBounds = ChairBounds;
                    BuildWidget = ctx.widgets.widget(458).component(4);
                    WrongWidget = BuildWidget = ctx.widgets.widget(458).component(4).component(5);
                    BuildNameWidget = ctx.widgets.widget(458).component(4).component(2);
                    WidgetText = "Crude wooden chair";
                    NotedWidgetText = "plank";
                    SleepX = 4000;
                    SleepY = 6000;
                    xpPerBuild = 58;
                }
                 else if (WoodenChair.isSelected()) {
                     Obj = 6753;
                     ObjSpace = 4515;
                     BuildName = "Chair space";
                     Planks = 960;
                     nPlanks = 961;
                     PlanksRequired = 3;
                     ObjBounds = ChairBounds;
                     BuildWidget = ctx.widgets.widget(458).component(5);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(5).component(5);
                     BuildNameWidget = ctx.widgets.widget(458).component(5).component(2);
                     WidgetText = "Wooden chair";
                     NotedWidgetText = "plank";
                     SleepX = 4000;
                     SleepY = 6000;
                     xpPerBuild = 87;

                 }
                 else if (RockingChair.isSelected()) {
                     Obj = 6754;
                     ObjSpace = 4515;
                     BuildName = "Chair space";
                     Planks = 960;
                     nPlanks = 961;
                     PlanksRequired = 3;
                     ObjBounds = ChairBounds;
                     BuildWidget = ctx.widgets.widget(458).component(6);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(6).component(5);
                     BuildNameWidget = ctx.widgets.widget(458).component(6).component(2);
                     WidgetText = "Rocking chair";
                     NotedWidgetText = "plank";
                     SleepX = 4000;
                     SleepY = 6000;
                     xpPerBuild = 87;

                 }
                 else if (OakChair.isSelected()) {
                     Obj = 6755;
                     ObjSpace = 4515;
                     BuildName = "Chair space";
                     Planks = 8778;
                     nPlanks = 8779;
                     PlanksRequired = 2;
                     ObjBounds = ChairBounds;
                     BuildWidget = ctx.widgets.widget(458).component(7);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(7).component(5);
                     BuildNameWidget = ctx.widgets.widget(458).component(7).component(2);
                     WidgetText = "Oak chair";
                     NotedWidgetText = "Oak plank";
                     SleepX = 1500;
                     SleepY = 3000;
                     xpPerBuild = 120;

                 }
                 else if (OakArmChair.isSelected()) {
                     Obj = 6756;
                     ObjSpace = 4515;
                     BuildName = "Chair space";
                     Planks = 8778;
                     nPlanks = 8779;
                     PlanksRequired = 3;
                     ObjBounds = ChairBounds;
                     BuildWidget = ctx.widgets.widget(458).component(8);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(8).component(5);
                     BuildNameWidget = ctx.widgets.widget(458).component(8).component(2);
                     WidgetText = "Oak armchair";
                     NotedWidgetText = "Oak plank";
                     SleepX = 1500;
                     SleepY = 3000;
                     xpPerBuild = 180;

                 }
                 else if (TeakChair.isSelected()) {
                     Obj = 6757;
                     ObjSpace = 4515;
                     BuildName = "Chair space";
                     Planks = 8780;
                     nPlanks = 8781;
                     PlanksRequired = 2;
                     ObjBounds = ChairBounds;
                     BuildWidget = ctx.widgets.widget(458).component(9);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(9).component(5);
                     BuildNameWidget = ctx.widgets.widget(458).component(9).component(2);
                     WidgetText = "Teak armchair";
                     NotedWidgetText = "Teak plank";
                     SleepX = 1500;
                     SleepY = 3000;
                     xpPerBuild = 180;

                 }
                 else if (MahogChair.isSelected()) {
                     Obj = 6758;
                     ObjSpace = 4515;
                     BuildName = "Chair space";
                     Planks = 8782;
                     nPlanks = 8783;
                     PlanksRequired = 2;
                     ObjBounds = ChairBounds;
                     BuildWidget = ctx.widgets.widget(458).component(10);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(10).component(5);
                     BuildNameWidget = ctx.widgets.widget(458).component(10).component(2);
                     WidgetText = "Mahogany armchair";
                     NotedWidgetText = "Mahogany plank";
                     SleepX = 1500;
                     SleepY = 3000;
                     xpPerBuild = 280;

                 }
               else if (WoodenBook.isSelected()) {
                    Obj = 6768;
                    ObjSpace = 4521;
                     BuildName = "Bookcase space";
                    Planks = 960;
                    nPlanks = 961;
                    PlanksRequired = 4;
                    ObjBounds = BookBounds;
                    BuildNameWidget = ctx.widgets.widget(458).component(4).component(2);
                    BuildWidget = ctx.widgets.widget(458).component(4);
                    WrongWidget = BuildWidget = ctx.widgets.widget(458).component(4).component(5);
                    WidgetText = "Wooden bookcase";
                    NotedWidgetText = "plank";
                    SleepX = 8000;
                    SleepY = 10000;
                     xpPerBuild = 115;

                }
                 else if (OakBook.isSelected()) {
                     Obj = 6769;
                     ObjSpace = 4521;
                     BuildName = "Build Bookcase space";
                     Planks = 8778;
                     nPlanks = 8779;
                     PlanksRequired = 3;
                     ObjBounds = BookBounds;
                     BuildNameWidget = ctx.widgets.widget(458).component(5).component(2);
                     BuildWidget = ctx.widgets.widget(458).component(5);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(5).component(5);
                     WidgetText = "Oak bookcase";
                     NotedWidgetText = "Oak plank";
                     SleepX = 1500;
                     SleepY = 3000;
                     xpPerBuild = 180;

                 }
                 else if (MahogBook.isSelected()) {
                     Obj = 6770;
                     ObjSpace = 4521;
                     BuildName = "Bookcase space";
                     Planks = 8782;
                     nPlanks = 8783;
                     PlanksRequired = 3;
                     ObjBounds = BookBounds;
                     BuildNameWidget = ctx.widgets.widget(458).component(6).component(2);
                     BuildWidget = ctx.widgets.widget(458).component(6);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(6).component(5);
                     WidgetText = "Mahogany bookcase";
                     NotedWidgetText = "Mahogany plank";
                     SleepX = 1500;
                     SleepY = 3000;
                     xpPerBuild = 420;

                 }
                else if (WoodenLarder.isSelected()) {
                    Obj = 13565;
                    ObjSpace = 15403;
                    BuildName = "Larder space";
                    Planks = 960;
                    nPlanks = 961;
                    PlanksRequired = 8;
                    ObjBounds = LarderBounds;
                    BuildNameWidget = ctx.widgets.widget(458).component(4).component(2);
                    BuildWidget = ctx.widgets.widget(458).component(4);
                    WrongWidget = BuildWidget = ctx.widgets.widget(458).component(4).component(5);
                    WidgetText = "Wooden larder";
                    NotedWidgetText = "plank";
                    SleepX = 9500;
                    SleepY = 11500;
                     xpPerBuild = 228;

                }
                else if (OakLarder.isSelected()) {
                    Obj = 13566;
                    ObjSpace = 15403;
                    BuildName = "Larder space";
                    Planks = 8778;
                    nPlanks = 8779;
                    PlanksRequired = 8;
                    ObjBounds = LarderBounds;
                    BuildNameWidget = ctx.widgets.widget(458).component(5).component(2);
                    BuildWidget = ctx.widgets.widget(458).component(5);
                    WrongWidget = BuildWidget = ctx.widgets.widget(458).component(5).component(5);
                    WidgetText = "Oak larder";
                    NotedWidgetText = "Oak plank";
                    SleepX = 1500;
                    SleepY = 3000;
                     xpPerBuild = 480;

                }
                 else if (WoodTable.isSelected()) {
                     Obj = 13293;
                     ObjSpace = 15298;
                     BuildName = "Table space";
                     Planks = 960;
                     nPlanks = 961;
                     PlanksRequired = 4;
                     ObjBounds = TableBounds;
                     BuildNameWidget = ctx.widgets.widget(458).component(4).component(2);
                     BuildWidget = ctx.widgets.widget(458).component(4);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(4).component(5);
                     WidgetText = "Wood dining table";
                     NotedWidgetText = "plank";
                     SleepX = 2000;
                     SleepY = 3500;
                     xpPerBuild = 115;
                 }
                else if (OakTable.isSelected()) {
                    Obj = 13294;
                    ObjSpace = 15298;
                     BuildName = "Table space";
                    Planks = 8778;
                    nPlanks = 8779;
                    PlanksRequired = 4;
                    ObjBounds = TableBounds;
                    BuildNameWidget = ctx.widgets.widget(458).component(5).component(2);
                    BuildWidget = ctx.widgets.widget(458).component(5);
                    WrongWidget = BuildWidget = ctx.widgets.widget(458).component(5).component(5);
                    WidgetText = "Oak dining table";
                    NotedWidgetText = "Oak plank";
                    SleepX = 2000;
                    SleepY = 3500;
                     xpPerBuild = 240;
                    }
                 else if (COakTable.isSelected()) {
                     Obj = 13295;
                     ObjSpace = 15298;
                     BuildName = "Table space";
                     Planks = 8778;
                     nPlanks = 8779;
                     PlanksRequired = 6;
                     ObjBounds = TableBounds;
                     BuildNameWidget = ctx.widgets.widget(458).component(6).component(2);
                     BuildWidget = ctx.widgets.widget(458).component(6);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(6).component(5);
                     WidgetText = "Carved oak table";
                     NotedWidgetText = "Oak plank";
                     SleepX = 2000;
                     SleepY = 3500;
                     xpPerBuild = 360;
                 }
                 else if (TeakTable.isSelected()) {
                     Obj = 13296;
                     ObjSpace = 15298;
                     BuildName = "Table space";
                     Planks = 8780;
                     nPlanks = 8781;
                     PlanksRequired = 4;
                     ObjBounds = TableBounds;
                     BuildNameWidget = ctx.widgets.widget(458).component(7).component(2);
                     BuildWidget = ctx.widgets.widget(458).component(7);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(7).component(5);
                     WidgetText = "Teak table";
                     NotedWidgetText = "Teak plank";
                     SleepX = 2000;
                     SleepY = 3500;
                     xpPerBuild = 360;
                 }
                 else if (MahogTable.isSelected()) {
                     Obj = 13298;
                     ObjSpace = 15298;
                     BuildName = "Table space";
                     Planks = 8782;
                     nPlanks = 8783;
                     PlanksRequired = 6;
                     ObjBounds = TableBounds;
                     BuildNameWidget = ctx.widgets.widget(458).component(9).component(2);
                     BuildWidget = ctx.widgets.widget(458).component(9);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(9).component(5);
                     WidgetText = "Mahogany table";
                     NotedWidgetText = "Mahogany plank";
                     SleepX = 2000;
                     SleepY = 3500;
                     xpPerBuild = 840;
                 }
                 else if (OakDungDoor.isSelected()) {
                     taskList.add(new Dungeon(ctx, ConstructUp.this));
                     Obj = 13344;
                     ObjSpace = 15328;
                     BuildName = "Door space";
                     Planks = 8778;
                     nPlanks = 8779;
                     PlanksRequired = 10;
                     ObjBounds = DoorBounds;
                     BuildNameWidget = ctx.widgets.widget(458).component(4).component(2);
                     BuildWidget = ctx.widgets.widget(458).component(4);
                     WrongWidget = BuildWidget = ctx.widgets.widget(458).component(4).component(5);
                     WidgetText = "Oak door";
                     NotedWidgetText = "Oak plank";
                     SleepX = 2000;
                     SleepY = 3000;
                     xpPerBuild = 600;
                 }

                 else {
                    ctx.controller.stop();

                }
                frame.dispose();
            }
        });

    }

    private void split(long milliseconds) {
        time1 = (int) ((milliseconds / (1000 * 60 * 60 * 24)) % 7);
        time2 = (int) (milliseconds / 1000) % 60;
        time3 = (int) ((milliseconds / (1000 * 60)) % 60);
        time4 = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
    }

    @Override
    public void poll() {
        idleTimer();
        timeToRemove();
        timeToBuild();
        timeToLeave();
        timeToWait();
        timeToJump();
        timeToRun();
        for (Task task : taskList) {
            if (ctx.controller.isStopping()) {
                break;
            }
            if (task.activate()) {
                task.execute();

            }


        }

    }

    private void idleTimer() {
        if (ctx.players.local().animation() == -1) {
            idleTimer++;
        } else if (!(ctx.players.local().animation() == -1)) {
            idleTimer = 0;
        }
    }

    private void timeToRemove() {
        GameObject Object = ctx.objects.select().id(Obj).nearest().poll();

        if (idleTimer > 1 && Object.inViewport()
                && (ctx.inventory.select().id(Planks).count() > (PlanksRequired - 1)
                || Waiting==1
                || ctx.widgets.widget(219).component(0).component(2).text().contains("Thanks")
                || ctx.widgets.widget(217).component(3).text().contains("Thanks")
                || ctx.widgets.widget(162).component(43).visible())
                && !ctx.players.local().inMotion()) {
            if (!Object.inViewport()){
                ctx.camera.turnTo(ctx.objects.select().id(Obj).nearest().poll());
                ctx.camera.pitch(80);
            }
            System.out.println("Time to remove.");
            Object.interact("Remove");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.component(219, 0).visible();
                }
            }, 150, 5);
        } else if (ctx.players.local().animation() != -1) {
            idleTimer = 0;
        }
    }

    private void timeToBuild() {
        GameObject ObjectSpace = ctx.objects.select().id(ObjSpace).nearest().poll();
        GameObject Object = ctx.objects.select().id(Obj).nearest().poll();
        Item P = ctx.inventory.select().id(Planks).poll();
        if (ctx.widgets.widget(233).component(0).visible()
                && ctx.widgets.widget(233).component(0).visible()) {
            System.out.println("Gz");
            ctx.widgets.widget(233).component(2).click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.widgets.widget(233).component(0).visible();
                }
            }, 80, 15);
        }
        if (!ctx.players.local().inMotion() && idleTimer > 1
                && !Object.valid() && ObjectSpace.inViewport()
                && (ctx.widgets.widget(162).component(43).visible()
                || ctx.widgets.widget(219).component(0).component(2).text().contains("Thanks")
                || ctx.widgets.widget(217).component(3).text().contains("Thanks"))
                && ctx.inventory.select().id(P).count() >= PlanksRequired) {
            if (!ctx.game.tab(Game.Tab.INVENTORY)){
                ctx.game.tab(Game.Tab.INVENTORY);
            }
            if (!ObjectSpace.inViewport()){
                ctx.camera.turnTo(ctx.objects.select().id(ObjSpace).nearest().poll());
                ctx.camera.pitch(80);
            }
            System.out.println("Time to build.");
            ObjectSpace.interact("Build",BuildName);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.widgets.component(458, 0).visible();
                }
            }, 150, 5);
        } else if (ctx.players.local().animation() != -1) {
            idleTimer = 0;
        }
    }

    private void timeToLeave() {
        Item P = ctx.inventory.select().id(Planks).poll();
        GameObject DungDoor = ctx.objects.select().id(15328, 13344).nearest().poll();
        GameObject Stairs = ctx.objects.select().id(13497).nearest().poll();
        GameObject Scarecrow = ctx.objects.select().id(9667).nearest().poll();
        final GameObject Dungeon = ctx.objects.select().id(4529).nearest().poll();
        final GameObject PortalOutHouse = ctx.objects.select().id(15478).nearest().poll();
        final GameObject PortalInHouse = ctx.objects.select().id(4525).nearest().poll();

        if (leaveTrue==1 && DungDoor.valid() && Stairs.inViewport() && !ctx.players.local().inMotion() && idleTimer > 2
                && ctx.inventory.select().id(P).count() < PlanksRequired && !PhialsArea.contains(ctx.players.local())){
            System.out.println("Climbing up the stairs.");
            Stairs.interact("Climb-up");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return Dungeon.inViewport();
                }
            }, 150, 5);
        }
        if (leaveTrue==1 && !PhialsArea.contains(ctx.players.local()) && Scarecrow.valid()){
            System.out.println("Wrong direction, doing a U-Turn.");
            ctx.camera.turnTo(PortalOutHouse);
            ctx.movement.step(PortalOutHouse);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return PortalOutHouse.inViewport();
                }
            }, 150, 10);
        }
        if (leaveTrue==1 && DoorHotspot.valid() && !ctx.players.local().inMotion() && idleTimer > 2 && ctx.inventory.select().id(P).count() < PlanksRequired
                && ctx.widgets.widget(162).component(43).visible() && !PortalOutHouse.valid()) {
            System.out.println("Time to leave.");
            ctx.camera.turnTo(PortalInHouse);
            ctx.movement.step(PortalInHouse);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return PortalInHouse.inViewport();
                }
            }, 150, 5);
            PortalInHouse.interact("Enter");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return PortalOutHouse.valid();
                }
            }, 150, 5);
        } else if (ctx.players.local().animation() != -1) {
            idleTimer = 0;
        }
    }

    private void timeToJump() {
        final GameObject PortalOutHouse = ctx.objects.select().id(15478).nearest().poll();
        final GameObject PortalInHouse = ctx.objects.select().id(4525).nearest().poll();
        if (PortalOutHouse.inViewport() && ctx.inventory.select().count() == 28) {
            System.out.println("Time to jump through space.");
            PortalOutHouse.interact("Build mode");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return PortalInHouse.inViewport();
                }
            }, 150, 5);
        } else if (ctx.players.local().animation() != -1) {
            idleTimer = 0;
        }
    }

    private void timeToRun() {
        GameObject Pool = ctx.objects.select().name("Pool of Revitalisation", "Pool of Rejuvenation").nearest().poll();
        if (ctx.movement.energyLevel() > 50 && !ctx.movement.running()) {
            System.out.println("Turning run on.");
            ctx.movement.running(true);
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.movement.running();
                }
            }, 150, 5);
        }

        if (Pool.valid() && ctx.movement.energyLevel() < 40) {
            ctx.movement.step(Pool);
            System.out.println("Finding pool");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return !ctx.players.local().inMotion();
                }
            }, 300, 10);
            Pool.interact("Drink");
            System.out.println("Drinking from pool");
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.movement.energyLevel() > 40;
                }
            }, 100, 10);
        }
    }
    private void timeToWait() {
        Butler = ctx.npcs.select().name("Demon butler", "Butler").nearest().poll();
        if (ctx.widgets.widget(231).component(3).text().contains("unholy flame")
                || ctx.widgets.widget(231).component(3).text().contains("can only carry")
                || ctx.widgets.widget(231).component(3).text().contains("able to carry")
                || ctx.widgets.widget(231).component(3).text().contains("Very good")){
           Waiting = 1;
           ctx.widgets.widget(231).component(2).click();
        }
        if ((ctx.widgets.widget(231).component(3).text().contains("returned")
                || ctx.widgets.widget(231).component(3).text().contains("hold")
                || ctx.widgets.widget(231).component(3).text().contains("Your goods"))

                && Planks>PlanksRequired){
            ctx.widgets.widget(231).component(2).click();
            Waiting = 0;
        }
        if (Butler.inViewport() && idleTimer>20){
            Waiting = 0;
            System.out.println("Failsafe activated, collecting items from butler.");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        e.getPoint();
        xMouse=e.getPoint().x;
        yMouse=e.getPoint().y;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }


    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    @Override
    public void repaint(Graphics graphics) {
        levelCurrent = ctx.skills.realLevel(Constants.SKILLS_CONSTRUCTION);
        xpCurrent = (ctx.skills.experience(Constants.SKILLS_CONSTRUCTION));
        xpGained = xpCurrent - xpStart;
        PaintXpGained = xpCurrent - xpStart;
        lvlGained = levelCurrent - lvlStart;
        planksUsed = itemsMade*PlanksRequired;
        double xpPerHour = (long) ((xpGained * 3600000D) / time);
        long xpNextLevel = (ctx.skills.experienceAt(levelCurrent + 1) -xpCurrent);
        long xpBetween = (ctx.skills.experienceAt(levelCurrent + 1)-ctx.skills.experienceAt(levelCurrent));
        double xpDone = xpBetween - xpNextLevel;
        double xpPart = xpDone/xpBetween;
        int percentageDone = (int)(xpPart*Math.pow(10,2));
        time = (int) (getRuntime() - startTime);
        time1 = time;

        Graphics2D g = (Graphics2D) graphics;

        g.drawImage(bg3, 6, 269, null);
        g.setFont(new Font("Impact", Font.PLAIN, 17));
        if (xMouse>395 && xMouse<498 && yMouse>414 && yMouse<464) {
            try {
                Desktop.getDesktop().browse(new URL("http://www.powerbot.org/community/topic/1341359-osrs-constructup-construction-script/").toURI());
            } catch (Exception e) {}
            xMouse=0;
            yMouse=0;
        }
            if (xMouse>8 && xMouse<35 && yMouse>362 && yMouse<411){

            g.setColor(new Color(0, 0, 0, 255));
            g.fillRect(394, 442, 100, 15);
            g.drawRect(394, 442, 100, 15);
            g.setColor(new Color(0, 175, 210, 255));
            g.fillRect(394, 442, percentageDone, 15);
            g.drawRect(394, 442, percentageDone, 15);
                g.setColor(new Color(255,255,255, 255));
                g.setFont(new Font("Impact", Font.PLAIN, 12));
                g.drawString(" " + percentageDone +"%", 435, 456);
                g.setFont(new Font("Impact", Font.PLAIN, 17));
            g.drawImage(bg, 6, 269, null);
            g.setColor(new Color(228, 251, 255, 255));
            g.drawString(" " +(long) xpPerHour, 171, 423);
            g.setColor(new Color(169, 242, 255, 255));
            g.drawString(" " +xpNextLevel, 203, 456);
                g.setFont(new Font("Impact", Font.PLAIN, 12));
                if (levelCurrent!=0){

                    g.drawString(" " +levelCurrent, 377, 456);
                    g.drawString(" " +(levelCurrent+1), 496, 456);
                } else {
                    g.drawString(" 0" ,377, 456);
                    g.drawString(" 0", 496, 456);
                }
                g.setFont(new Font("Impact", Font.PLAIN, 17));
            g.setColor(new Color(255, 255, 255, 255));
            g.drawString(" " +xpGained, 179, 388);
            split(time);
            g.drawString("" + String.format("%02d:%02d:%02d", time4, time3, time2), 378, 388);
            g.setColor(new Color(228, 251, 255, 255));
            g.drawString(" " + lvlGained, 422, 420);
        }
        if (xMouse>8 && xMouse<35 && yMouse>413 && yMouse<461){
            g.drawImage(bg2, 6, 269, null);
            g.setColor(new Color(169, 242, 255, 255));
            if (Waiting == 1) {
                g.drawString("Waiting for butler", 220, 456);
            }
            if (Waiting == 0) {
                g.drawString("Not waiting for butler", 220, 456);
            }
            if (xpGained>0) {
                itemsMade = xpGained/xpPerBuild;
                g.setColor(new Color(255, 255, 255, 255));
                g.drawString(" " + itemsMade + " " + WidgetText +"s", 189, 388);
                g.setColor(new Color(228, 251, 255, 255));
                g.drawString(" " + planksUsed, 196, 422);
            }
        }

        }

    }