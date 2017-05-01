package Zaiat_iRage.scripts.zAlkharidFighter.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;
import org.powerbot.script.Random;
import Zaiat_iRage.scripts.zAlkharidFighter.zAlkharidFighter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zaiat on 4/20/2017.
 */
public class Fight extends Task<ClientContext>
{
    private LocalPath path_to_castle = null;

    private Area warriors_area_entrance = new Area(new Tile(3291, 3179), new Tile(3296, 3175));
    private Area warriors_area_middle = new Area(new Tile(3287, 3178), new Tile(3299,3167));

    private int[] warrior_bounds = {-20, 20, -184, -12, -20, 20};

    private long attack_timer_1, attack_timer_2;

    private zAlkharidFighter script;

    public Fight(zAlkharidFighter script, ClientContext ctx)
    {
        super(ctx);
        this.script = script;
    }

    @Override
    public boolean activate()
    {
        return !ctx.inventory.select().name(zAlkharidFighter.food).isEmpty();
    }

    int i = 0;
    @Override
    public void execute()
    {
        if(!zAlkharidFighter.warriors_area.contains(ctx.players.local()))
        {
            zAlkharidFighter.status = "Going to castle";
            if(path_to_castle == null)
            {
                path_to_castle = ctx.movement.findPath(warriors_area_entrance.getRandomTile());
            }
            else
            {
                path_to_castle.traverse();
            }
        }
        else
        {
            path_to_castle = null;
            if(!TargetLockedDown())
            {
                zAlkharidFighter.status = "Looking for Al-kharid warrior";
                List<Npc> nearby_warriors = new ArrayList<Npc>();
                ctx.npcs.select().nearest().within(zAlkharidFighter.warriors_area).name("Al-Kharid warrior").each(Interactive.doSetBounds(warrior_bounds)).addTo(nearby_warriors);
                for(final Npc warrior: nearby_warriors)
                {
                    if(warrior.healthPercent() > 0 && !IsTargetFighting(warrior))
                    {
                        if(!DoorToWarriorClosed(warrior))
                        {
                            zAlkharidFighter.status = "Attacking Al-kharid warrior";
                            AttackWarrior(warrior);
                        }
                        else
                        {
                            zAlkharidFighter.status = "Opening door";
                            script.OpenDoor(ctx);
                        }
                        break;
                    }
                }
            }
            else
            {
                zAlkharidFighter.status = "Fighting Al-kharid warrior";
            }
        }
    }

    private boolean DoorToWarriorClosed(Npc warrior)
    {
        if(
            zAlkharidFighter.warriors_area_left.contains(ctx.players.local())    && !zAlkharidFighter.warriors_area_left.contains(warrior)    ||
            warriors_area_middle.contains(ctx.players.local())  && !warriors_area_middle.contains(warrior)  ||
                    zAlkharidFighter.warriors_area_right.contains(ctx.players.local())   && !zAlkharidFighter.warriors_area_right.contains(warrior))
        {
            BasicQuery<GameObject> query = ctx.objects.select().name("Large door").nearest().within(zAlkharidFighter.warriors_area);
            GameObject door = query.poll();

            while(door.tile().y() == 3167)
                door = query.poll();

            for(String action: door.actions())
            {
                if(action != null && action.equals("Open"))
                    return true;
            }
        }
        return false;
    }

    private void AttackWarrior(final Npc warrior)
    {
        if(attack_timer_1 != 0)
        {
            attack_timer_2 =  script.getRuntime();
            if(attack_timer_2 - attack_timer_1 >= 3000)
                attack_timer_1 = 0;
        }
        if(attack_timer_1 == 0)
        {
            if (!warrior.inViewport())
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
                            ctx.camera.turnTo(warrior.tile());
                            return;
                        }
                    });
                    t1.start();
                    t2.start();

                }
                catch(Error e)
                {

                }
            }
            else
            {
                warrior.interact(true, "Attack", "Al-kharid warrior");
                attack_timer_1 = script.getRuntime();
            }
        }
    }

    private boolean IsTargetFighting(Npc target)
    {
        if(target.interacting().name().equals("") || target.interacting().name().equals(ctx.players.local().name()) || !target.interacting().interacting().equals(target))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean TargetLockedDown()
    {
        return !ctx.players.local().interacting().name().equals("");
    }
}
