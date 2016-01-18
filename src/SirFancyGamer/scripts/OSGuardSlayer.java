package SirFancyGamer.scripts;


import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.*;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Script.Manifest(
        name = "Al-Kharid Slayer",
        description = "Kills the Al-Kharid warriors and auto heals/banks for food.",
        properties = "author=SirFancyGamer;topic=1298880;game=4;"
)

public class OSGuardSlayer extends PollingScript<ClientContext> implements PaintListener {

    Font font = new Font("Verdana", Font.BOLD, 12);

    String title = "Al-Kharid Guard Slayer";
    String expGained = "Exp gained: 0";
    String status = "Status: N/A";

    Color transGrey = new Color(119, 136, 153, 175);


    boolean needBank = true;
    boolean canAttack = false;

    GameObject closedDoor = ctx.objects.select().id(6839).nearest().poll();

    Tile castleTile = new Tile(3293, 3174);
    Tile bankTile = new Tile(3269, 3167);
    boolean foodError = false;

    int foodID;
    Bank bank = new Bank(ctx);

    org.powerbot.script.Tile[] tilesToBank = new org.powerbot.script.Tile[]{new org.powerbot.script.Tile(3293, 3179), new Tile(3286, 3179),
            new Tile(3280, 3179), new Tile(3273, 3167), new Tile(3269, 3167)};

    org.powerbot.script.Tile[] tilesToCastle = new org.powerbot.script.Tile[]{new Tile(3269, 3167), new Tile(3273, 3167), new Tile(3280, 3179),
            new Tile(3286, 3179), castleTile};

    TilePath pathBank = ctx.movement.newTilePath(tilesToBank);
    TilePath pathCastle = ctx.movement.newTilePath(tilesToCastle);

    Item food = ctx.inventory.select().id(foodID).poll();
    int randChance = 0;
    Random rn = new Random();

    long startingExp = ctx.skills.experience(0) + ctx.skills.experience(1) + ctx.skills.experience(2) + ctx.skills.experience(3)
            + ctx.skills.experience(4) + ctx.skills.experience(6);
    long tempExp = 0;

    Npc guard = ctx.npcs.select().name("Al-Kharid warrior").nearest().poll();

    Tile guardLocation = new Tile(guard.tile().x() + rn.nextInt(3), guard.tile().y() + rn.nextInt(3));


    public void poll() {
        tempExp = ctx.skills.experience(0) + ctx.skills.experience(1) + ctx.skills.experience(2) + ctx.skills.experience(3)
                + ctx.skills.experience(4) + ctx.skills.experience(6);
        expGained = "Exp gained: " + (tempExp - startingExp);
        if ((ctx.combat.health() <= 5)) {
            food = ctx.inventory.select().id(foodID).poll();
            food.interact("Eat");
        }
        if (ctx.inventory.select().id(foodID).count() <= 0) {
            closedDoor = ctx.objects.select().id(6839).nearest().poll();
            if (closedDoor.valid() && closedDoor.tile().distanceTo(ctx.players.local().tile()) <= 3) {
                closedDoor.interact("Open");
            }
            try {
                TimeUnit.MILLISECONDS.sleep((long) ((rn.nextDouble() + .55) * 1000));
            } catch (InterruptedException e) {
                e.getMessage();
            }
            needBank = true;
            canAttack = false;
        }
        if (needBank && !canAttack && ctx.players.local().tile().distanceTo(bankTile) <= 3) {
            bankInteraction();
        } else if (needBank && !canAttack) {
            walkToBank();
        }
        if (!needBank && !canAttack) {
            randomCastleTile();
            walkToCastle();
        }
        if (canAttack) {
            attackGuard();
        }
    }

    public void start() {
        System.out.println("Script started!");
        selectFoodItem();
        food = ctx.inventory.select().id(foodID).poll();
        if (ctx.inventory.select().id(foodID).count() <= 0) {
            needBank = true;
        } else {
            needBank = false;
        }
    }

    @Override
    public void stop() {
        System.out.println("Script stopped!");
        if (foodError) {
            JOptionPane.showMessageDialog(null, "Ran out of food!", "Error: No Food!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void attackGuard() {
        status = "Status: Attacking";
        if (!ctx.players.local().inCombat()) {
            guard = ctx.npcs.select().name("Al-Kharid warrior").nearest().poll();
            closeDoor();
            ctx.camera.turnTo(guard);
            guard.interact("Attack");
            try {
                TimeUnit.MILLISECONDS.sleep((long) ((rn.nextDouble() + .15) * 1000));
            } catch (InterruptedException e) {
                e.getMessage();
            }
            randChance = rn.nextInt(100);
            if (randChance >= 45) {
                ctx.input.move(rn.nextInt(350), rn.nextInt(300));
            }

            try {
                TimeUnit.MILLISECONDS.sleep((long) ((rn.nextDouble() + .85) * 1000));
            } catch (InterruptedException e) {
                e.getMessage();
            }
        } else {
            randChance = rn.nextInt(100);
            if (randChance >= 97) {
                ctx.input.move(ctx.input.getLocation().x + rn.nextInt(25), ctx.input.getLocation().y + rn.nextInt(25));
            }
            if (randChance <= 3) {
                ctx.input.move(ctx.input.getLocation().x - rn.nextInt(25), ctx.input.getLocation().y - rn.nextInt(25));
            }
            if (randChance >= 30 && randChance <= 32) {
                ctx.input.move(ctx.input.getLocation().x - rn.nextInt(25), ctx.input.getLocation().y + rn.nextInt(25));
            }
            if (randChance >= 40 && randChance <= 42) {
                ctx.input.move(ctx.input.getLocation().x + rn.nextInt(25), ctx.input.getLocation().y - rn.nextInt(25));
            }
            ctx.camera.turnTo(randomGuardLocation());
            try {
                TimeUnit.MILLISECONDS.sleep((long) ((rn.nextDouble() + .25) * 1000));
            } catch (InterruptedException e) {
                e.getMessage();
            }
        }
    }

    public Tile randomGuardLocation() {
        return guardLocation = new Tile(guard.tile().x() + rn.nextInt(3), guard.tile().y() + rn.nextInt(3));
    }

    public void walkToCastle() {
        status = "Status: Walking to castle";
        randomCastleTile();
        if (ctx.players.local().tile().distanceTo(castleTile) > 3) {
            pathCastle.traverse();
            ctx.camera.turnTo(pathCastle.next());
            canAttack = false;
            try {
                TimeUnit.MILLISECONDS.sleep((long) (rn.nextDouble() + .85) * 1000);
            } catch (InterruptedException e) {
                e.getMessage();
            }
        } else if (ctx.inventory.select().id(foodID).count() > 0 && !needBank) {
            canAttack = true;
        }
    }

    public void walkToBank() {
        status = "Status: Walking to bank";
        if (ctx.inventory.select().id(foodID).count() <= 0) {
            needBank = true;
        }
        if (ctx.players.local().tile().distanceTo(new Tile(3269, 3167)) > 3) {
            pathBank.traverse();
            ctx.camera.turnTo(pathBank.next());
            try {
                TimeUnit.MILLISECONDS.sleep((long) (rn.nextDouble() + .85) * 1000);
            } catch (InterruptedException e) {
                e.getMessage();
            }
        } else {
            return;
        }
    }

    public void bankInteraction() {
        status = "Status: Interacting with bank";
        Npc banker = ctx.npcs.select().name("Banker").nearest().poll();
        try {
            TimeUnit.MILLISECONDS.sleep((long) ((rn.nextDouble() + .75) * 1000));
        } catch (InterruptedException e) {
            e.getMessage();
        }
        if (bank.opened()) {
            if (bank.select().id(foodID).count() <= 0) {
                foodError = true;
                bank.close();
                ctx.controller.stop();
            } else if (bank.withdraw(foodID, 28)) {
                needBank = false;
                bank.close();
            }
        } else if (needBank) {
            ctx.camera.turnTo(banker);
            banker.interact("Bank");
        }
    }

    public void selectFoodItem() {
        boolean success = false;
        while (!success) {
            try {
                foodID = Integer.parseInt(JOptionPane.showInputDialog(null, "Type in your desired food ID (Integer value only)", "Select Food",
                        JOptionPane.QUESTION_MESSAGE));
            } catch (NumberFormatException e) {
                foodID = -1;
            }

            if (foodID >= 0) {
                success = true;
            }
        }
    }

    public void closeDoor() {
        closedDoor = ctx.objects.select().id(6839).nearest().poll();
        if (closedDoor.valid() && closedDoor.tile().distanceTo(guard.tile()) <= 6 && !ctx.players.local().inCombat()) {
            status = "Status: Opening door..";
            ctx.camera.turnTo(new Tile(closedDoor.tile().x() + 10, closedDoor.tile().y() + 10));
            ctx.camera.pitch(30);
            closedDoor.interact("Open");
            try {
                TimeUnit.MILLISECONDS.sleep((long) ((rn.nextDouble() + .75) * 1000));
            } catch (InterruptedException e) {
                e.getMessage();
            }
        }
    }

    public void randomCastleTile() {
        int tempNum = rn.nextInt(5);

        switch (tempNum) {
            case 1:
                castleTile = new Tile(3293, 3174);
                break;
            case 2:
                castleTile = new Tile(3296, 3172);
                break;
            case 3:
                castleTile = new Tile(3288, 3175);
                break;
            case 4:
                castleTile = new Tile(3290, 3171);
                break;
            case 5:
                castleTile = new Tile(3293, 3169);
                break;
            default:
                castleTile = new Tile(3293, 3174);
                break;
        }
    }


    @Override
    public void repaint(Graphics graphics) {
        graphics.setFont(font);
        graphics.setColor(transGrey);
        graphics.drawRect(0, 0, 230, 75);
        graphics.fillRect(0, 0, 230, 75);
        graphics.setColor(Color.black);
        graphics.getFont();
        graphics.drawString(title, 15, 15);
        graphics.drawString(status, 15, 35);
        graphics.drawString(expGained, 15, 55);

    }
}
