package Zaiat_iRage.scripts.zAlkharidFighter;

import Zaiat_iRage.scripts.zAlkharidFighter.Tasks.Bank;
import org.powerbot.script.*;
import org.powerbot.script.rt4.*;
import org.powerbot.script.rt4.ClientContext;
import Zaiat_iRage.scripts.zAlkharidFighter.Other.*;
import Zaiat_iRage.scripts.zAlkharidFighter.Tasks.*;
import org.powerbot.script.rt4.Interactive;


import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Zaiat on 4/20/2017.
 */
@Script.Manifest(
        name="zAlkharidFighter",
        description="Fights al-kharid warriors & banks for food",
        properties = "client=4;author=Zaiat_iRage;topic=1331345"
)

public class zAlkharidFighter extends PollingScript<ClientContext> implements PaintListener
{
    public static String food = "";
    public static String status = "Loading";
    public static boolean start = false;
    public static int starting_HP_exp = 0;
    public static int starting_CB_exp = 0;
    public static Area warriors_area = new Area(new Tile(3281, 3179), new Tile(3305, 3167));
    public static Area warriors_area_left = new Area(new Tile(3282, 3178), new Tile(3287, 3167));
    public static Area warriors_area_right = new Area(new Tile(3299, 3178), new Tile(3304, 3167));

    public long startTime = 0;
    private static int[] door_bounds = {108, 128,
                                        -160, -80,
                                        -32, 32};
    private List<Task> taskList = new ArrayList<Task>();

    private static long open_door_timer_1, open_door_timer_2;

    private GUI gui;

    @Override
    public void start()
    {
        taskList.addAll(Arrays.asList(new Bank(this, ctx), new Fight(this, ctx), new Eat(this, ctx), new ToggleRun(ctx)));
        gui = new GUI(this, ctx);
    }

    @Override
    public void poll()
    {
        if(start)
        {
            for (Task task : taskList)
            {
                if (task.activate())
                    task.execute();
            }
        }
    }

    int s_x=0,s_s=0;
    int h=0,m=0,s=0;
    private final Color color1 = new Color(153, 153, 255);
    private final Color color2 = new Color(0, 0, 0);
    private final Color color3 = new Color(51, 51, 51);
    private final Color color4 = new Color(255, 255, 255);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final BasicStroke stroke2 = new BasicStroke(2);
    private final BasicStroke stroke3 = new BasicStroke(3);
    private final BasicStroke stroke4 = new BasicStroke(4);
    private final Font font1 = new Font("Arial", 0, 23);
    private final Font font2 = new Font("Arial", 1, 13);
    @Override
    public void repaint(Graphics graphics)
    {
        if(start)
        {
            s_x = (int) ((getRuntime() - startTime) / 1000);

            if (s_x - s_s > 0)
            {
                s++;
            }
            if (s >= 60)
            {
                s = 0;
                m++;
            }
            if (m >= 60)
            {
                m = 0;
                h++;
            }
            s_s = s_x;
        }

        Graphics2D g = (Graphics2D)graphics;
        g.setColor(color1);
        g.fillRect(15, 15, 250, 145);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(15, 15, 250, 145);
        g.setFont(font1);
        g.setColor(color3);
        g.drawString("zAlkharidFighter", 75, 45);
        g.setFont(font2);
        if(start)
        {
            g.drawString("[Runtime] " + h + ":" + m + ":" + s, 20, 85);
            g.drawString("[Status] " + status, 20, 105);
            g.drawString("[Hitpoints Exp] " + Get_HP_EXP() + " (P/H: " + Get_HP_EXP_PerHour() + ")", 20, 125);
            g.drawString("[Combat Exp] " + Get_Combat_EXP() + " (P/H: " + Get_Combat_EXP_PerHour() + ")", 20, 145);
        }

        int x = ctx.input.getLocation().x;
        int y = ctx.input.getLocation().y;

        g.setStroke(stroke4);
        g.setColor(color2);
        g.drawOval(x-10, y-10, 20, 20);

        g.setStroke(stroke3);
        g.drawLine(x, y-4, x, y-7);
        g.drawLine(x, y+4, x, y+7);
        g.drawLine(x-4, y, x - 7, y);
        g.drawLine(x+4, y, x + 7, y);

        g.setColor(color1);
        g.setStroke(stroke1);
        g.drawLine(x, y - 4, x, y - 8);
        g.drawLine(x, y + 4, x, y + 8);
        g.drawLine(x - 4, y, x - 8, y);
        g.drawLine(x + 4, y, x + 8, y);
        g.setStroke(stroke2);
        g.drawOval(x-10, y-10, 20, 20);

    }

    private String Get_HP_EXP()
    {
        if(!start) return "0";
        int current_HP_exp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
        return Commaize.commaize(current_HP_exp - starting_HP_exp);
    }

    private String Get_HP_EXP_PerHour()
    {
        if(!start) return "0";
        int current_HP_exp = ctx.skills.experience(Constants.SKILLS_HITPOINTS);
        int exp_gained = current_HP_exp - starting_HP_exp;

            double runtime = getRuntime()-startTime;
            double exp_per_millisecond = exp_gained / runtime;
            double exp_per_second = exp_per_millisecond * 1000;
            double exp_per_minute = exp_per_second * 60;
            int exp_per_hour = (int) (exp_per_minute * 60);
            return Commaize.commaize(exp_per_hour);
    }

    private String Get_Combat_EXP()
    {
        if(!start) return "0";
        int current_CB_exp =
            ctx.skills.experience(Constants.SKILLS_ATTACK) +
            ctx.skills.experience(Constants.SKILLS_STRENGTH) +
            ctx.skills.experience(Constants.SKILLS_DEFENSE) +
            ctx.skills.experience(Constants.SKILLS_RANGE) +
            ctx.skills.experience(Constants.SKILLS_MAGIC);

        return Commaize.commaize(current_CB_exp - starting_CB_exp);
    }

    private String Get_Combat_EXP_PerHour()
    {
        if(!start) return "0";
        int current_CB_exp =
            ctx.skills.experience(Constants.SKILLS_ATTACK) +
            ctx.skills.experience(Constants.SKILLS_STRENGTH) +
            ctx.skills.experience(Constants.SKILLS_DEFENSE) +
            ctx.skills.experience(Constants.SKILLS_RANGE) +
            ctx.skills.experience(Constants.SKILLS_MAGIC);

        int exp_gained = current_CB_exp - starting_CB_exp;

            double runtime = getRuntime()-startTime;
            double exp_per_millisecond = exp_gained / runtime;
            double exp_per_second = exp_per_millisecond * 1000;
            double exp_per_minute = exp_per_second * 60;
            int exp_per_hour = (int) (exp_per_minute * 60);
            return Commaize.commaize(exp_per_hour);

    }

    public void OpenDoor(final ClientContext ctx)
    {
        if(open_door_timer_1 != 0)
        {
            open_door_timer_2 = getRuntime();
            if(open_door_timer_2 - open_door_timer_1 >= 3000)
                open_door_timer_1 = 0;
        }
        if(open_door_timer_1 == 0)
        {
            BasicQuery<GameObject> query = ctx.objects.select().name("Large door").nearest().within(warriors_area).each(Interactive.doSetBounds(door_bounds));
            GameObject temp_door = query.poll();

            while (temp_door.tile().y() == 3167)
                temp_door = query.poll();

            final GameObject door = temp_door;

            for (String action : door.actions())
            {
                if (action != null && action.equals("Open"))
                {
                    final java.util.Random r = new java.util.Random();
                    try
                    {
                        Thread t1 = new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ctx.camera.pitch(38 + r.nextInt(8));
                                return;
                            }
                        });
                        Thread t2 = new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (ctx.players.local().tile().x() < door.tile().x())
                                {
                                    if (ctx.camera.yaw() > 300 || ctx.camera.yaw() < 240)
                                        ctx.camera.angle(240 + r.nextInt(60));
                                } else
                                {
                                    if (ctx.camera.yaw() > 120 || ctx.camera.yaw() < 60)
                                        ctx.camera.angle(60 + r.nextInt(60));
                                }
                                return;
                            }
                        });
                        t1.start();
                        t2.start();
                    } catch (Error e)
                    {

                    }

                    door.interact(false, "Open", "Large door");
                }
            }
        }
    }
}
