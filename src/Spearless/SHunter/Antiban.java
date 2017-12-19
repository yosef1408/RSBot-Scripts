package Spearless;


import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Npc;
import org.powerbot.script.rt4.Player;


import java.util.Random;

/**
 * Created by Gaston on 4/12/2017.
 */
public class Antiban extends PollingScript<ClientContext> {

    private final Frame2 frame2;

    public Antiban( Frame2 in ) {
         frame2 = in;
    }

    public void start() {
    }
    private void changeBright() {
    ctx.widgets.component(548, 41).valid();
    ctx.widgets.component(548, 41).hover();
    ctx.widgets.component(548, 41).click();
    Condition.sleep(org.powerbot.script.Random.nextInt(250, 500));
    int randombright = org.powerbot.script.Random.nextInt(0, 5);
    switch (randombright) {

        case 1:
            ctx.widgets.component(261, 16).hover();
            ctx.widgets.component(261, 16).click();

            break;
        case 2:
            ctx.widgets.component(261, 15).hover();
            ctx.widgets.component(261, 15).click();
            break;
        case 3:
            ctx.widgets.component(261, 17).hover();
            ctx.widgets.component(261, 17).click();
            break;
        case 4:
            ctx.widgets.component(261, 18).hover();
            ctx.widgets.component(261, 18).click();
            break;
    }
    Condition.sleep(org.powerbot.script.Random.nextInt(250, 500));
    ctx.widgets.component(548, 50).hover();
    ctx.widgets.component(548, 50).click();

    }

    private void checkInvPrice(){
        ctx.widgets.component(548, 51).hover();
        ctx.widgets.component(548, 51).click();
        Condition.sleep(org.powerbot.script.Random.nextInt(250, 500));
        ctx.widgets.component(387, 19).hover();
        ctx.widgets.component(387, 19).click();
        Condition.sleep(org.powerbot.script.Random.nextInt(250, 500));
        ctx.widgets.component(464, 10).hover();
        ctx.widgets.component(464, 10).click();
        Condition.sleep(org.powerbot.script.Random.nextInt(1500, 2000));
        ctx.widgets.component(464, 1).component(11).hover();
        ctx.widgets.component(464, 1).component(11).click();
        Condition.sleep(org.powerbot.script.Random.nextInt(250, 500));
        ctx.widgets.component(548, 50).hover();
        ctx.widgets.component(548, 50).click();

    }
    private void checkHunterSkill(){
        if (!ctx.widgets.component(320, 16).inViewport()) {
            ctx.widgets.component(548, 48).valid();
            ctx.widgets.component(548, 48).hover();
            Condition.sleep(org.powerbot.script.Random.nextInt(250, 500));
            ctx.widgets.component(548, 48).click();
            log.info("Checking skill");
        }
        ctx.widgets.component(320, 16).hover();
        Condition.sleep(org.powerbot.script.Random.nextInt(400, 700));
        ctx.widgets.component(548, 50).hover();
        ctx.widgets.component(548, 50).click();
    }
   private void hoveringNpc() {
        int randomn = org.powerbot.script.Random.nextInt(0, 20);
        if (randomn == 20) {

            Npc Npc;
            Npc = ctx.npcs.select().poll();
            Npc.hover();
            Npc.click(false);

            log.info("Hovering over npc");

        }
    }


    void antiban() {

        Random random = new Random();
        int invx = random.nextInt(15);
        int x = org.powerbot.script.Random.nextInt(0, 1000) - 300;
        int y = org.powerbot.script.Random.nextInt(0, 1300) - 400;

        switch (invx) {
                case 1:
                    if(frame2.getCheckSk()) {
                        checkHunterSkill();
                        log.info("checking skill");
                    }
                break;
            case 2:
                if (frame2.getRotate()) {
                    log.info("camera s");
                    ctx.camera.angle('s');
                }
                break;
            case 3:
                if (frame2.getRotate()) {
                    log.info("Camera w");
                    ctx.camera.angle('w');
                }

                break;
            case 4:
                if (frame2.getRotate()) {

                    log.info("Camera n");
                    ctx.camera.angle('n');
                }
                break;
            case 5:
                if (frame2.getRotate()) {
                    log.info("camera e");
                    ctx.camera.angle('e');
                }
                break;
            case 7:
                if (frame2.getMoveM()) {
                    log.info("Moving mouse randomly");
                    ctx.input.move(x, y);
                }
                break;
            case 8:
                if (frame2.getMoveM()) {
                    log.info("Moving mouse randomly");
                    ctx.input.move(x, y);
                }
                break;
            case 9:
                if (frame2.getHoverOb()) {
                    hoveringNpc();
                    }
                break;
            case 10:
                if (frame2.getBrigh()) {
                   changeBright();
                }
                break;
            case 11:
                if (frame2.getXp()) {

                    ctx.widgets.component(160, 1).hover();
                    ctx.widgets.component(160, 1).click();
                }
                break;

            case 12:
                if (frame2.getMon()) {
                   checkInvPrice();
                }

                break;
            case 13:
                if (frame2.getHoverPlay()) {
                    ctx.players.select();
                    ctx.players.within(20);
                    final Player p = ctx.players.nearest().poll();
                    p.hover();
                }
                    break;
        }
    }

    public void poll() {

        switch(state())

        {
            case MINE:

        }
    }

    private State state(){

        return State.MINE;
    }

    private enum State{
        MINE
    }
}
