package Trikkstr.Scripts.Tasks;

import Trikkstr.Scripts.GoblinKiller.CONSTANTS;
import Trikkstr.Scripts.GoblinKiller.GoblinKiller;
import Trikkstr.Scripts.GoblinKiller.Task;
import org.powerbot.script.Condition;
import org.powerbot.script.Filter;
import org.powerbot.script.rt4.*;

import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public class Fight extends Task
{

    private final int GOBLIN[] = { 3029, 3030, 3031, 3032, 3034, 3035 };
    private final int FOOD[] = { 315, 1971, 2309 };

    private final int HUT_VALUES[] = {3243, 3244, 3245, 3246, 3247, 3248};

    private final Walker walker = new Walker(ctx);

    //inventoryOpen VS inventoryClosed
    private final Component inventory = ctx.widgets.widget(161).component(61);
    private final Component unselectedInventory = ctx.widgets.widget(161).component(54);

    private Npc goblin;

    private int startingHealth;
    private int currentHealth;
    private int startingInventory;
    private int currentInventory;
    private int startAmountInventory;

    private boolean hit;
    private int x;
    private int y;
    private int i;
    private int j;
    private int count;

    public Fight(ClientContext ctx)
    {
        super(ctx);
    }

    @Override
    public boolean activate()
    {
        return true;
    }

    @Override
    public void execute()
    {
        System.out.printf("Executing Fight.\n");
        System.out.printf("Health: %d\n", ctx.combat.health());
        System.out.println("Food Items: "+ctx.inventory.select().id(FOOD).count());

        //IF YOU ARE NOT PAST THE GATE THEN WALK THE PATH, THEN OPEN THE GATE

        if(ctx.players.local().tile().x() > 3267)
        {
            walkToGoblins();
        }
        else
        {
            if(GoblinKiller.getBones() && bonesNearby() && !ctx.players.local().inCombat())
            {
                pickupBones();
            }

            if (bonesInInventory() && !ctx.players.local().inCombat())
            {
                handleBones();
            }

            if (lootNearby() && !ctx.players.local().inCombat())
            {
                System.out.printf("Looting.\n");
                pickup();
            }

            if (hasFood() || ctx.combat.health() >= 10)
            {
                System.out.printf(("Player is ready for combat.\n"));

                if (needsHeal())
                {
                    System.out.printf("Needs Heal: True\n");
                    heal();
                }
                else if (shouldAttack())
                {
                    System.out.printf("Should Attack: True\n");
                    attack();
                }
            }
        }
    }

    private boolean bonesInInventory()
    {
        return ctx.inventory.select().id(526).count() > 0;
    }

    private boolean needsHeal()
    {
        return ctx.combat.health() < 6;
    }

    private boolean shouldAttack()
    {
        return !ctx.players.local().inCombat();
    }

    private boolean hasFood()
    {
        return ctx.inventory.select().id(FOOD).count() > 0;
    }

    private boolean lootNearby()
    {
        GroundItem loot = ctx.groundItems.select().name(Pattern.compile("(.*rune)|(Coins)|(.*bolts)")).nearest().poll();

        if(ctx.inventory.select().id(FOOD).count() > 0)
        {
            return(loot.tile().distanceTo(ctx.players.local().tile()) <= 4);
        }
        else
        {
            return(loot.tile().distanceTo(ctx.players.local().tile()) <= 18);
        }

    }

    private boolean bonesNearby()
    {
        GroundItem loot = ctx.groundItems.select().id(526).nearest().poll();

        return(loot.tile().distanceTo(ctx.players.local().tile()) <= 2);
    }

    private void pickup()
    {
        GroundItem loot = getLoot();

        startingInventory = ctx.inventory.select().name(Pattern.compile("(.*rune)|(Coins)|(.*bolts)")).count(true);

        if(ctx.inventory.select().id(loot).count() > 0 || ctx.inventory.count() < 28)
        {
            ctx.camera.turnTo(loot);

            if(!loot.inViewport())
                ctx.movement.step(loot);

            loot.interact("Take");
        }

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                currentInventory = ctx.inventory.select().name(Pattern.compile("(.*rune)|(Coins)|(.*bolts)")).count(true);
                return currentInventory != startingInventory;
            }
        }, 400, 12);
    }

    private void pickupBones()
    {
        GroundItem loot = getBones();

        startingInventory = ctx.inventory.select().id(526).count(true);

        if(ctx.inventory.select().id(loot).count() > 0 || ctx.inventory.count() < 28)
        {
            ctx.camera.turnTo(loot);

            if(!loot.inViewport())
                ctx.movement.step(loot);

            loot.interact("Take");
        }

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                currentInventory = ctx.inventory.select().id(526).count(true);
                return currentInventory != startingInventory;
            }
        }, 400, 12);
    }

    private void attack()
    {
        System.out.printf("Selecting A Goblin To Attack\n");

        goblin = getGoblin();

        if(!goblin.inViewport())
        {
            ctx.camera.turnTo(goblin);
            ctx.movement.step(goblin);
        }

        goblin.interact("Attack", "Goblin");

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return goblin.inCombat();
            }
        }, 500, 10);

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                return ctx.players.local().inCombat();
            }
        }, 500, 10);
    }

    private void heal()
    {
        if(unselectedInventory.textureId() == -1)
        {
            inventory.click();
        }

        Item food = ctx.inventory.select().id(FOOD).poll();

        startingHealth = ctx.combat.health();

        food.interact("Eat");

        Condition.wait(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                currentHealth = ctx.combat.health();
                return currentHealth != startingHealth;
            }
        }, 150, 20);
    }

    private void handleBones()
    {
        if(unselectedInventory.textureId() == -1)
        {
            inventory.click();
        }

        for(Item bones : ctx.inventory.select().id(526))
        {
            startAmountInventory = ctx.inventory.select().count();

            if(GoblinKiller.getBones())
            {
                bones.interact("Bury", "Bones");
            }
            else
            {
                bones.interact("Drop", "Bones");
            }

            Condition.wait(new Callable<Boolean>()
            {
                @Override
                public Boolean call() throws Exception
                {
                    return ctx.inventory.select().count() != startAmountInventory;
                }
            }, 75, 20);

            Condition.sleep(org.powerbot.script.Random.nextInt(650, 850));
        }
    }

    private Npc getGoblin()
    {
        return ctx.npcs.select().id(GOBLIN).select(new Filter<Npc>()
        {
            @Override
            public boolean accept(Npc npc)
            {
                return !npc.inCombat();
            }
        }).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc)
            {
                hit = false;
                x = npc.tile().x();
                y = npc.tile().y();
                for(i = 0;  i < HUT_VALUES.length; i++ )
                {
                    if(x == HUT_VALUES[i])
                        for(j = 0; j < HUT_VALUES.length; j++)
                            if(y == HUT_VALUES[j])
                                hit = true;
                }
                return !hit;
            }
        }).select(new Filter<Npc>() {
            @Override
            public boolean accept(Npc npc)
            {
                count = 0;
                y = npc.tile().y();
                for(i = 0;  i < HUT_VALUES.length; i++ )
                {
                    if(y == HUT_VALUES[i])
                        count += 1;
                }
                return count == 0;
            }
        }).nearest().poll();
    }

    private GroundItem getLoot()
    {
        return ctx.groundItems.select().name(Pattern.compile("(.*rune)|(Coins)|(.*bolts)")).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem item)
            {
                hit = false;
                x = item.tile().x();
                y = item.tile().y();
                for(i = 0;  i < HUT_VALUES.length; i++ )
                {
                    if(x == HUT_VALUES[i])
                        for(j = 0; j < HUT_VALUES.length; j++)
                            if(y == HUT_VALUES[j])
                                hit = true;
                }
                return !hit;
            }
        }).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem item)
            {
                int count = 0;
                int y = item.tile().y();
                for(i = 0;  i < HUT_VALUES.length; i++ )
                {
                    if(y == HUT_VALUES[i])
                        count += 1;
                }
                return count == 0;
            }
        }).nearest().poll();
    }

    private GroundItem getBones()
    {
        return ctx.groundItems.select().id(526).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem item)
            {
                hit = false;
                x = item.tile().x();
                y = item.tile().y();
                for(i = 0;  i < HUT_VALUES.length; i++ )
                {
                    if(x == HUT_VALUES[i])
                        for(j = 0; j < HUT_VALUES.length; j++)
                            if(y == HUT_VALUES[j])
                                hit = true;
                }
                return !hit;
            }
        }).select(new Filter<GroundItem>() {
            @Override
            public boolean accept(GroundItem item)
            {
                int count = 0;
                int y = item.tile().y();
                for(i = 0;  i < HUT_VALUES.length; i++ )
                {
                    if(y == HUT_VALUES[i])
                        count += 1;
                }
                return count == 0;
            }
        }).nearest().poll();
    }

    private void walkToGoblins()
    {
        walker.walkPath(CONSTANTS.AK_BANK_TO_GOBLINS);

        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().inCombat();
            }
        }, 1000, 1);

        //if close to the gate and not already on the other side, then pay the toll
        if(ctx.objects.select().id(2882).poll().tile().distanceTo(ctx.players.local()) < 8
                && ctx.players.local().tile().x() > 3267)
        {
            System.out.printf("Opening Al-Kharid Gate\n");
            if (!ctx.objects.select().id(2882).poll().inViewport())
                ctx.camera.turnTo(ctx.objects.select().id(2882).poll());

            ctx.objects.select().id(2882).poll().interact("Pay-toll(10gp)", "Gate");

            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.players.local().tile().x() < 3268;
                }
            }, 500, 6);
        }
    }
}
