package scripts.zAlkharidFighter.Tasks;


import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.LocalPath;
import scripts.zAlkharidFighter.zAlkharidFighter;

import java.util.Random;

/**
 * Created by Zaiat on 4/20/2017.
 */
public class Bank extends Task<ClientContext>
{
    private LocalPath path_to_bank = null;
    private Area bank_area = new Area(new Tile(3269, 3174), new Tile(3273, 3161));

    public Bank(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        return ctx.inventory.select().name(zAlkharidFighter.food).isEmpty();
    }

    @Override
    public void execute()
    {
        if(!bank_area.contains(ctx.players.local()))
        {
            zAlkharidFighter.status = "Going to bank";

            if((zAlkharidFighter.warriors_area_left.contains(ctx.players.local()) || zAlkharidFighter.warriors_area_right.contains(ctx.players.local())) && IsDoorClosed())
            {
                zAlkharidFighter.OpenDoor(ctx);
            }
            else
            {
                Go_To_Bank();
            }
        }
        else
        {
            path_to_bank = null;
            if(!ctx.bank.open())
            {
                zAlkharidFighter.status = "Opening bank";
                ctx.bank.open();
            }
            else if(ctx.inventory.select().name(zAlkharidFighter.food).isEmpty())
            {
                zAlkharidFighter.status = "Withdrawing food";
                if(!ctx.bank.select().name(zAlkharidFighter.food).isEmpty())
                {
                    ctx.bank.withdraw(ctx.bank.select().name(zAlkharidFighter.food).poll(), 0);
                }
                else
                {
                    ctx.controller.stop();
                }
            }
        }
    }

    private boolean IsDoorClosed()
    {
        GameObject door = ctx.objects.select().name("Large door").nearest().within(zAlkharidFighter.warriors_area).poll();
        for(String action: door.actions())
        {
            if(action != null && action.equals("Open"))
                return true;
        }
        return false;
    }

    private void Go_To_Bank()
    {
        if(path_to_bank == null)
        {
            path_to_bank = ctx.movement.findPath(bank_area.getCentralTile());
        }
        else
        {
            if (ctx.camera.yaw() < 135 || ctx.camera.yaw() > 30)
            {
                final Random r = new Random();
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
                            ctx.camera.angle(30 + r.nextInt(105));
                            return;
                        }
                    });
                    t1.start();
                    t2.start();
                } catch (Error e)
                {

                }
            }

            path_to_bank.traverse();
        }
    }
}
