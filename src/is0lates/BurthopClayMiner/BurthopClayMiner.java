package BurthopClayMiner;

import org.powerbot.bot.rt6.Con;
import org.powerbot.script.Condition;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.MobileIdNameQuery;

@Script.Manifest(name = "Burthop Clay Miner", description = "Mines clay in the cave in Burthop", properties = "client=6; topic=0")
public class BurthopClayMiner extends PollingScript<ClientContext> {

    private static int clayRockId1 = 27007;
    private static int clayRockId2 = 67008;
    private static Tile rockPosition = new Tile(2274, 4525);
    private static Tile caveExitPosition = new Tile(2292, 4516);
    private static Tile caveEntrancePosition = new Tile(2876, 3503);
    private static Tile bankPosition = new Tile(2888, 3536);
    private static int bankBoothId = 25688;
    private static int caveEntranceId = 66876;
    private static int caveExitId = 67002;
        private static int miningAnimationId = 625;
    //    private static int miningAnimationId = 626;
//    private static int miningAnimationId = 627;

    static enum ScriptState {
        WALK_TO_BANK,
        OPEN_BANK,
        DEPOSIT_BANK,
        WALK_TO_CAVE_ENTRANCE,
        ENTER_CAVE,
        WALK_TO_CLAY,
        MINE_CLAY,
        MINING_CLAY,
        WALK_TO_CAVE_EXIT,
        EXIT_CAVE
    }

    public ScriptState getState() {
        if(ctx.backpack.select().count() == 28) {
            if(!ctx.bank.opened()) {
                if(ctx.players.local().tile().distanceTo(bankPosition) > ctx.players.local().tile().distanceTo(caveExitPosition)) {
                    if(ctx.players.local().tile().distanceTo(caveExitPosition) > 2) {
                        return  ScriptState.WALK_TO_CAVE_EXIT;
                    } else {
                        return ScriptState.EXIT_CAVE;
                    }
                } else {
                    if(ctx.players.local().tile().distanceTo(bankPosition) > 2) {
                        return ScriptState.WALK_TO_BANK;
                    } else {
                        return ScriptState.OPEN_BANK;
                    }
                }
            } else {
                return ScriptState.DEPOSIT_BANK;
            }
        } else {
            if(ctx.players.local().tile().distanceTo(caveEntrancePosition) < ctx.players.local().tile().distanceTo(rockPosition)) {
                if(ctx.players.local().tile().distanceTo(caveEntrancePosition) > 2) {
                    return ScriptState.WALK_TO_CAVE_ENTRANCE;
                } else {
                    return ScriptState.ENTER_CAVE;
                }
            } else {
                if(ctx.players.local().tile().distanceTo(rockPosition) > 2) {
                    return ScriptState.WALK_TO_CLAY;
                } else {
                    if(ctx.players.local().animation() == miningAnimationId) {
                        return ScriptState.MINING_CLAY;
                    } else {
                        return ScriptState.MINE_CLAY;
                    }
                }
            }
        }
    }

    public void poll() {
        switch (getState()) {
            case WALK_TO_BANK :
                System.out.println("Walking to bank");
                ctx.movement.step(bankPosition);
                Condition.sleep(1000);
                break;
            case OPEN_BANK :
                System.out.println("Opening bank");
                GameObject bankBooth = ctx.objects.select().id(bankBoothId).poll();
                ctx.camera.turnTo(bankBooth);
                Condition.sleep(500);
                ctx.bank.open();
                break;
            case DEPOSIT_BANK :
                System.out.println("Depositing Bank");
                ctx.bank.depositInventory();
                Condition.sleep(200);
                ctx.bank.close();
                Condition.sleep(200);
                break;
            case WALK_TO_CAVE_ENTRANCE :
                System.out.println("Walking to cave entrance");
                ctx.movement.step(caveEntrancePosition);
                break;
            case ENTER_CAVE :
                System.out.println("Entering cave");
                GameObject caveEntrance = ctx.objects.select().id(caveEntranceId).poll();
                ctx.camera.turnTo(caveEntrance);
                Condition.sleep(200);
                caveEntrance.click();
                break;
            case WALK_TO_CLAY :
                System.out.println("Walking to clay");
                ctx.movement.step(rockPosition);
                Condition.sleep(1000);
                break;
            case MINE_CLAY :
                System.out.println("Interacting clay rock");
                GameObject rock = ctx.objects.select().name("Clay rock").nearest().poll();
                rock.interact("Mine");
                Condition.sleep(1000);
                break;
            case MINING_CLAY:
                System.out.println("Mining clay");
                Condition.sleep(1000);
                break;
            case WALK_TO_CAVE_EXIT :
                System.out.println("Walking to cave exit");
                ctx.movement.step(caveExitPosition);
                Condition.sleep(1000);
                break;
            case EXIT_CAVE :
                System.out.println("Exiting cave");
                GameObject caveExit = ctx.objects.select().id(caveExitId).poll();
                ctx.camera.turnTo(caveExit);
                Condition.sleep(200);
                caveExit.click();
                break;
        }
    }

}