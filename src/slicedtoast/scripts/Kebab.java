package slicedtoast.scripts;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.Tile;

import java.util.concurrent.Callable;

public class Kebab extends Task {

    public Kebab(ClientContext ctx) {
        super(ctx);
    }

    Area kebabRoom = new Area(new Tile(3271, 3183), new Tile(3275, 3179));

    @Override
    public boolean activate()
    {
        return ctx.inventory.select().count() != 28 && !ctx.inventory.select().id(995).isEmpty();
        //smith when inventory isn't full and you have coins
    }

    @Override
    public void execute()
    {
        checkRun();
        rotateKebabMan(); //rotate towards mr. k
        Npc karim = ctx.npcs.select().id(529).poll(); //create npc object
        if(!karim.valid())
        {
            ctx.movement.step(new Tile(getRand(3271, 3275), getRand(3179, 3183))); //click somewhere in the room
            sleep(4000);
            karim = ctx.npcs.select().id(529).poll();
        }
        checkDoor(); //if the door isn't opened, try and open it before moving in
        karim.interact(false, "Talk-to", "Karim"); //right-click talk to karim for the first time
        sleep(getRand(50, 100));
        rotateKebabManToBuy();
        sleep(1000); //wait 1 second, and if you haven't moved yet, try again
        while(!ctx.players.local().inMotion() && !ctx.chat.chatting())
        {
            checkDoor();
            ctx.movement.step(karim); //walk towards him
            ctx.camera.turnTo(karim); //turn towards him
            karim.interact("Talk-to");
        }
        Condition.wait(new Callable<Boolean>(){ //wait until bank is opened
            @Override
            public Boolean call() throws Exception
            {
                // return ctx.widgets.widget(231).component(2).visible();
                return ctx.chat.chatting();
            }
        }, 1000, 15); //check to see if chat log is open every second for 15 seconds for the first time
        buyKebab(); //run through the rest of the chat
        while(ctx.inventory.select().count() < 27 && !ctx.inventory.select().id(995).isEmpty()) //while your inventory is not full, or you're not out of coins
        {
            if(!kebabRoom.contains(ctx.players.local())) //if we're not in the kebab room, relocate the guy
            {
                ctx.camera.turnTo(karim); //turn towards him
                ctx.movement.step(karim); //walk towards him
            }
            karim.interact("Talk-to"); //try to left click him
            buyKebab(); //then buy 1 kebab
        }
    }

    private void buyKebab()
    {
        sleep(getRand(27, 95));
        Condition.wait(new Callable<Boolean>(){ //wait until bank is opened
            @Override
            public Boolean call() throws Exception
            {
                return ctx.chat.canContinue();
            }
        }, 100, 60); //check to see if chat log is open every 100 ms for 6 seconds
        ctx.chat.clickContinue();
        sleep(getRand(27, 95));
        Condition.wait(new Callable<Boolean>(){ //wait until bank is opened
            @Override
            public Boolean call() throws Exception
            {
                return ctx.widgets.widget(219).component(0).component(2).visible();
            }
        }, 100, 30); //check to see if next chat log is open every 100 ms for 3 seconds
        ctx.widgets.widget(219).component(0).component(2).interact("Continue");
        sleep(getRand(27, 95));
        Condition.wait(new Callable<Boolean>(){ //wait until bank is opened
            @Override
            public Boolean call() throws Exception
            {
                //   return ctx.widgets.widget(217).component(2).visible();
                return ctx.chat.canContinue();
            }
        }, 100, 30); //check to see if next chat log is open every 100 ms for 3 seconds
        ctx.chat.clickContinue();
        sleep(getRand(27, 95));
    }

    private void rotateCamKebabManMulti() throws InterruptedException
    {
        Thread thread1 = new Thread()
        {
            public void run() {
                if (getRand(0, 2) == 0) {
                    ctx.camera.angle(getRand(318, 360));
                }
                else
                {
                    ctx.camera.angle(getRand(0, 21));
                }
            }
        };
        Thread thread2 = new Thread()
        {
            public void run()
            {
                ctx.camera.pitch(getRand(0, 23));
            }
        };
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    private void rotateCamKebabManToBuyMulti() throws InterruptedException
    {
        Thread thread1 = new Thread()
        {
            public void run()
            {
                ctx.camera.angle(getRand(0, 360));
            }
        };
        Thread thread2 = new Thread()
        {
            public void run()
            {
                ctx.camera.pitch(99);
            }
        };
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

    private void rotateKebabMan()
    {
        try
        {
            rotateCamKebabManMulti(); //rotate camera towards the thingy
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }

    private void rotateKebabManToBuy()
    {
        try
        {
            rotateCamKebabManToBuyMulti(); //rotate camera towards the thingy
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
