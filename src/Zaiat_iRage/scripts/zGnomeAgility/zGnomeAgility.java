package Zaiat_iRage.scripts.zGnomeAgility;

import Zaiat_iRage.scripts.zGnomeAgility.Tasks.*;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zaiat on 4/28/2017.
 */
@Script.Manifest(
        name="zGnomeAgility",
        description="Gnome Agility Course",
        properties = "client=4;author=Zaiat_iRage;topic=1331810"
)
public class zGnomeAgility extends PollingScript<ClientContext> implements PaintListener
{
    private Task RunState = new CheckRunState(ctx);
    private List<Task> taskList = new ArrayList<Task>();
    private  String STATUS;
    private int starting_exp;
    private int starting_level;

    public Area area_course_1 = new Area(new Tile(2469, 3440+1, 0), new Tile(2490+1, 3414, 0));
    private Area area_course_2 = new Area(new Tile(2469, 3440+1, 1), new Tile(2490+1, 3414, 1));
    private Area area_course_3 = new Area(new Tile(2469, 3440+1, 2), new Tile(2490+1, 3414, 2));

    @Override
    public void start()
    {
        starting_exp = ctx.skills.experience(Constants.SKILLS_AGILITY);
        starting_level = ctx.skills.realLevel(Constants.SKILLS_AGILITY);;
        taskList.addAll(Arrays.asList(new CrossLogBalance(this, ctx), new ClimbObstacleNet_1(this, ctx), new ClimbTreeBranch_1(this, ctx), new CrossBalancingRope(this, ctx), new ClimbTreeBranch_2(this, ctx), new ClimbObstacleNet_2(this, ctx), new SqueezeThroughPipe(this, ctx)));
    }

    @Override
    public void poll()
    {
        if(RunState.activate())
            RunState.execute();

        if(InAgilityCourse())
        {
            for (Task task : taskList)
                if (task.activate())
                    task.execute();
        }
        else
            UpdateScript("Error, not in course area");
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


    public void repaint(Graphics graphics)
    {
        s_x = (int) (getRuntime() / 1000);

        if(s_x - s_s > 0)
        {
            s++;
        }
        if (s >= 60) {
            s = 0;
            m++;
        }
        if (m >= 60)
        {
            m = 0;
            h++;
        }
        s_s = s_x;

        String experience_text = Commaize.commaize(ctx.skills.experience(Constants.SKILLS_AGILITY) - starting_exp);

        double runtime =  getRuntime();
        double exp_per_millisecond = (ctx.skills.experience(Constants.SKILLS_AGILITY) - starting_exp) / runtime;
        double exp_per_second = exp_per_millisecond * 1000;
        double exp_per_minute = exp_per_second * 60;
        int exp_per_hour = (int) (exp_per_minute * 60);

        String experience_ph_text = Commaize.commaize(exp_per_hour);
        String experience_left = Commaize.commaize(ctx.skills.experienceAt(ctx.skills.realLevel(Constants.SKILLS_AGILITY)+1) - ctx.skills.experience(Constants.SKILLS_AGILITY));

        Graphics2D g = (Graphics2D)graphics;
        g.setColor(color1);
        g.fillRect(15, 15, 265, 145);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(15, 15, 265, 145);
        g.setFont(font1);
        g.setColor(color3);
        g.drawString("zGnomeAgility", 75, 45);
        g.setFont(font2);
        g.drawString("[Runtime] " + h + ":" + m + ":" + s, 20, 85);
        g.drawString("[Status] " + STATUS, 20, 105);
        g.drawString("[Agility Lvl] " + ctx.skills.realLevel(Constants.SKILLS_AGILITY) + " (+" + (ctx.skills.realLevel(Constants.SKILLS_AGILITY) - starting_level) + ") (Exp Left: " + experience_left +")", 20, 125);
        g.drawString("[Agility Exp] " + experience_text + " (P/H: " + experience_ph_text + ")", 20, 145);

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

    public void UpdateScript(String status)
    {
        STATUS = status;
    }

    public void CameraLookatSync(final int angle)
    {
        try
        {
            Thread t1 = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    ctx.camera.pitch(38 + Random.nextInt(0,8));
                    return;
                }
            });
            Thread t2 = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    ctx.camera.angle(angle + Random.nextInt(-15, 15));
                    return;
                }
            });
            t1.start();
            t2.start();
        } catch (Error e)
        {

        }
    }

    private boolean InAgilityCourse()
    {
        return area_course_1.contains(ctx.players.local()) || area_course_2.contains(ctx.players.local()) || area_course_3.contains(ctx.players.local());
    }
}
