package TMKCodes.Gathering.Tasks;

import TMKCodes.Gathering.Task;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;
import org.powerbot.script.rt4.Player;
import org.powerbot.script.rt4.PlayerQuery;
import org.powerbot.script.Random;

public class Antiban extends Task {

    public Antiban(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.players.local().animation() != -1;
    }

    @Override
    public void execute() {
        System.out.println("Antiban");
        int antiban = Random.nextInt(1000, 1);
        if(antiban > 0 && antiban <= 5) { // open skills and check fishing skill

        } else if(antiban > 5 && antiban <= 50) {
            rotateScreen();
        } else if(antiban > 50 && antiban <= 55) {
            checkPlayer();
        } else if(antiban > 65 && antiban <= 70) {
            try {
             checkTab();
            } catch (InterruptedException e) {
             e.printStackTrace();
            }
        }
    }

    private void checkTab() throws InterruptedException {
        Game.Tab ctab = ctx.game.tab();
        int itab = Random.nextInt(13, 1);
        switch(itab) {
            case 1:
                ctx.game.tab(Game.Tab.ATTACK);
                break;
            case 2:
                ctx.game.tab(Game.Tab.STATS);
                break;
            case 3:
                ctx.game.tab(Game.Tab.QUESTS);
                break;
            case 4:
                ctx.game.tab(Game.Tab.INVENTORY);
                break;
            case 5:
                ctx.game.tab(Game.Tab.EQUIPMENT);
                break;
            case 6:
                ctx.game.tab(Game.Tab.PRAYER);
                break;
            case 7:
                ctx.game.tab(Game.Tab.MAGIC);
                break;
            case 8:
                ctx.game.tab(Game.Tab.FRIENDS_LIST);
                break;
            case 9:
                ctx.game.tab(Game.Tab.IGNORED_LIST);
                break;
            case 10:
                ctx.game.tab(Game.Tab.MUSIC);
                break;
            case 11:
                ctx.game.tab(Game.Tab.CLAN_CHAT);
                break;
            case 12:
                //ctx.game.tab(Game.Tab.MUSIC);
                break;
            case 13:
                //ctx.game.tab(Game.Tab.EMOTES);
                break;
        }
        Thread.sleep(Random.nextInt(2400, 600));
        if(Random.nextInt(2, 1) == 1) {
            ctx.game.tab(ctab);
        }
    }

    private void rotateScreen() {
        ctx.camera.pitch(ctx.camera.pitch() + Random.nextInt(40, 1));
        ctx.camera.angle(ctx.camera.yaw() + Random.nextInt(83, 1));
    }

    private void checkPlayer() {
        if(ctx.players.local().interacting() != null) {
            PlayerQuery<Player> players = ctx.players.select().within(10);
            int whatPlayer = Random.nextInt(players.size(), 0);
            int i = 0;
            for (Player player : players) {
                if (i == whatPlayer) {
                    ctx.camera.turnTo(player);
                    player.hover();
                    player.interact("Lookup");
                }
                i++;
            }
        }
    }

    private void mouseMove() {
        if(ctx.players.local().interacting() != null) {

        }
    }
}
