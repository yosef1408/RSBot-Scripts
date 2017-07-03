package scripts.RyuksTutorialIsland;

import java.util.concurrent.Callable;

import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Game.Tab;
import org.powerbot.script.rt4.GameObject;
import org.powerbot.script.rt4.Interactive;
import org.powerbot.script.rt4.Magic.Spell;
import org.powerbot.script.rt4.Npc;

/**
 * 
 * Each task required to be carried out to complete each tutorial island section
 * 
 * @author User
 *
 */
public class Tasks extends Controller {

	private static long lastCheckedFighting;

	public void getStartedTask() {
		final Component appInterface = w.select().id(269).poll()
				.component(100);
		if (appInterface.visible())
			appInterface.click();

		if (v.varpbit(22) == 33554432) {
			if (!c.chatting())
				utils.interactWithNpc(3308, "Talk-to");

			if (w.component(219, 0).component(2).visible())
				w.component(219, 0).component(2).click();
		}

		if (v.varpbit(281) == 10) {
			utils.interactWithObject(9398, "Open");
			Condition.sleep(Random.nextInt(1000, 1300));
		}
	}

	public void woodcutFireTask() {
		if (!utils.canISeeNpc(3306)) {
			utils.travelToNpc(3306);
			Condition.sleep(Random.nextInt(1000, 1300));
		}

		if (!c.chatting()
				&& (v.varpbit(281) == 20 || v.varpbit(281) == 30 || v
						.varpbit(281) == 70))
			utils.interactWithNpc(3306, "Talk-to");

		if (v.varpbit(281) == 40 && v.varpbit(1021) == 0
				&& !utils.doIHaveItem(2511)) {
			utils.chopTree();
			Condition.sleep(Random.nextInt(700, 1000));
		}

		if (v.varpbit(281) == 50 && utils.doIHaveItem(2511)) {
			utils.stepToRandomEmptyTile(5);
			Condition.sleep(Random.nextInt(300, 600));
			utils.makeFire();
			Condition.sleep(Random.nextInt(1500, 2000));
		}

	}

	public void fishCookTask() {
		boolean moveOn = false;
		if (!moveOn) {
			if (v.varpbit(281) == 120) {
				moveOn = true;
			} else {

				if (v.varpbit(406) == 3
						&& (v.varpbit(281) == 80 || v.varpbit(281) == 110)
						&& player.animation() != 621
						&& !utils.doIHaveItem(2514)) {
					utils.interactWithNpc(3317, "Net");
					Condition.sleep(Random.nextInt(800, 1200));
				}

				if (utils.doIHaveItem(2514)) {
					if (!ctx.objects.select().id(26185).nearest().poll()
							.valid()) {
						utils.chopTree();
						Condition.sleep(Random.nextInt(700, 1000));
						utils.stepToRandomEmptyTile(5);
						Condition.sleep(Random.nextInt(300, 600));
						utils.makeFire();
						Condition.sleep(Random.nextInt(1500, 2000));
					}
					utils.useItemOnObject(2514, 26185);
					Condition.sleep(Random.nextInt(2500, 3000));
				}
			}
		}
		if (moveOn) {
			final Tile t = new Tile(3091, 3092, 0);
			ctx.movement.findPath(t).traverse();
			ctx.camera.angle('w');
			ctx.camera.pitch(0);

			final int[] bounds = { -16, 32, -128, 0, -4, 272 };
			final GameObject gate = ctx.objects.select().id(9708)
					.each(Interactive.doSetBounds(bounds)).nearest().poll();
			gate.interact("Open");
		}
	}

	public void cookingTask() {
		if (v.varpbit(281) == 130 && player.tile() != new Tile(3078, 3084, 0)) {
			final Tile t = new Tile(3080, 3084, 0);
			ctx.movement.findPath(t).traverse();
			Condition.sleep(Random.nextInt(1800, 2100));

			ctx.camera.pitch(true);

			ctx.objects.select().id(9709).nearest().poll().interact("Open");
			Condition.sleep(Random.nextInt(2000, 3000));
		}

		if (v.varpbit(281) == 140 && !c.chatting())
			utils.interactWithNpc(3305, "Talk-to");

		if (v.varpbit(281) == 150) {
			utils.fuseItems("Pot of flour", "Bucket of water");
			Condition.sleep(Random.nextInt(800, 1300));
		}

		if (v.varpbit(281) == 160) {
			utils.useItemOnObject(2307, 9736);
			Condition.sleep(Random.nextInt(800, 1300));
		}
	}

	public void openCookDoorTask() {
		if (v.varpbit(281) == 180 && v.varpbit(1021) == 0
				&& player.tile() != new Tile(3072, 3090, 0)) {
			ctx.camera.pitch(false);
			final int[] bounds = { -44, 4, -236, 0, 4, 132 };
			final GameObject door = ctx.objects.select().id(9710)
					.each(Interactive.doSetBounds(bounds)).nearest().poll();
			if (!door.inViewport() || player.tile().distanceTo(door) > 4) {
				ctx.camera.turnTo(door);
				ctx.movement.step(door.tile());
			}
			door.interact("Open");
			Condition.sleep(Random.nextInt(1200, 1500));
		}
	}

	public void emoteRunTask() {

		if (v.varpbit(281) == 187 && v.varpbit(1021) == 0) {

			if (!w.widget(548).component(35).visible()) {
				w.widget(548).component(35).click();
				Condition.sleep(Random.nextInt(200, 400));
			}

			ctx.input.click(w.component(261, 1).centerPoint(), true);
		}

		if (v.varpbit(281) == 200 && v.varpbit(1021) == 0) {

			if (!w.widget(548).component(34).visible()) {
				w.widget(548).component(34).click();
				Condition.sleep(Random.nextInt(200, 400));
			}

			ctx.input.click(w.component(261, 72).centerPoint(), true);
		}

		if (v.varpbit(281) == 210) {
			utils.travelToTile(3086, 3126);

			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return player.tile().distanceTo(new Tile(3086, 3126, 0)) <= 7;
				}
			}, 350, 10);

			ctx.camera.pitch(70);
			ctx.camera.angle('s');

			final GameObject door = ctx.objects.select().id(9716).nearest()
					.poll();
			if (!door.inViewport())
				ctx.movement.step(door.tile());

			if (player.tile().distanceTo(door) < 7) {
				utils.interactWithObject(9716, "Open");
				Condition.sleep(Random.nextInt(400, 700));
			}

		}
	}

	public void questTask() {
		if ((v.varpbit(281) == 220 || v.varpbit(281) == 240)
				&& (v.varpbit(406) == 7 || v.varpbit(1021) == 0)
				&& !c.chatting()) {
			utils.interactWithNpc(3312, "Talk-to");
			Condition.sleep(Random.nextInt(300, 500));
		}

		if (v.varpbit(281) == 250) {
			utils.interactWithObject(9726, "Climb-down");
			Condition.sleep(Random.nextInt(500, 800));
		}
	}

	public void miningTask() {
		if (v.varpbit(281) == 260 && v.varpbit(23) == 64 && v.varpbit(406) == 8) {

			if (!utils.canISeeNpc(3311))
				utils.travelToTile(3081, 9506);

			if (!c.chatting() && utils.canISeeNpc(3311))
				utils.interactWithNpc(3311, "Talk-to");
		}

		if (v.varpbit(281) == 270) {
			utils.interactWithObject(10080, "Prospect");
			Condition.sleep(Random.nextInt(3000, 5000));
		}

		if (v.varpbit(281) == 280) {
			utils.interactWithObject(10079, "Prospect");
			Condition.sleep(Random.nextInt(3000, 5000));
		}

		if (v.varpbit(281) == 290 && !c.chatting()) {
			utils.interactWithNpc(3311, "Talk-to");
			Condition.sleep(Random.nextInt(800, 1300));
		}

		if (v.varpbit(281) == 300) {
			utils.interactWithObject(10080, "Mine");
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return utils.doIHaveItem(438);
				}
			}, 600, 10);
		}

		if (v.varpbit(281) == 310) {
			utils.interactWithObject(10079, "Mine");
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return utils.doIHaveItem(436);
				}
			}, 600, 10);
		}

	}

	public void smithingTask() {
		if (v.varpbit(281) == 320) {
			utils.useItemOnObject(436, 10082);
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return v.varpbit(281) != 320;
				}
			}, 600, 10);
			w.component(162, 31).click();
		}

		if (v.varpbit(281) == 330 && !c.chatting()) {
			utils.interactWithNpc(3311, "Talk-to");
			Condition.sleep(Random.nextInt(800, 1300));
		}

		if ((v.varpbit(281) == 340 || v.varpbit(281) == 350)) {
			if (!w.component(312, 2).visible()) {
				utils.useItemOnObject(2349, 2097);
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return w.component(312, 2).component(2)
								.visible();
					}
				}, 250, 10);
			} else {
				w.component(312, 2).component(2).interact("Smith 1");
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return player.animation() == -1;
					}
				}, 250, 10);
			}
		}

		if (v.varpbit(281) == 360) {
			utils.travelToTile(3093, 9502);
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return player.tile().distanceTo(new Tile(3093, 9502, 0)) < 5;
				}
			});
			ctx.camera.angle('e');
			ctx.camera.pitch(0);
			utils.interactWithObject(9717, "Open");
			if (player.tile().x() == 3095
					&& (player.tile().y() == 9503 || player.tile().y() == 9502)) {
				ctx.camera.angle('w');
			}
		}
	}

	public void equipmentTask() {
		if (v.varpbit(281) == 370) {
			utils.travelToNpc(3307);
			if (utils.canISeeNpc(3307) && !c.chatting())
				utils.interactWithNpc(3307, "Talk-to");
		}

		if (v.varpbit(281) == 400 || v.varpbit(281) == 405) {
			ctx.game.tab(Tab.EQUIPMENT);
			w.widget(387).component(18).click();
			Condition.sleep(Random.nextInt(250, 400));
			utils.clickItemInInv(1205);
		}

		if (v.varpbit(281) == 410) {
			ctx.input.click(ctx.widgets.component(84, 4).centerPoint(), true);
			if (!c.chatting())
				utils.interactWithNpc(3307, "Talk-to");
			Condition.sleep(Random.nextInt(250, 400));
		}

		if (v.varpbit(281) == 420) {
			ctx.game.tab(Tab.INVENTORY);
			utils.clickItemInInv(1277);
			utils.clickItemInInv(1171);
			Condition.sleep(Random.nextInt(500, 700));
		}

	}

	public void meleeTask() {

		if (v.varpbit(281) == 440) {
			utils.interactWithObject(9719, "Open");
			Condition.sleep(Random.nextInt(500, 700));
		}
		if (v.varpbit(281) == 450 || v.varpbit(281) == 460) {
			if (!player.inCombat()) {
				Npc rat = utils.attackableNpc(3313);
				rat.interact("Attack");
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return player.inCombat();
					}
				}, 600, 10);
				ctx.camera.pitch(false);
			}
		}

		if (v.varpbit(281) == 470) {
			if (utils.inFightArea()) {
				utils.interactWithObject(9719, "Open");
				Condition.sleep(Random.nextInt(800, 1200));
			}

			if (!utils.inFightArea() && !c.chatting()) {
				utils.interactWithNpc(3307, "Talk-to");
				Condition.sleep(Random.nextInt(700, 1200));
			}
		}
	}

	public void rangeTask() {
		if (player.animation() != -1)
			lastCheckedFighting = System.currentTimeMillis();

		if (v.varpbit(281) == 480 && v.varpbit(406) == 12) {
			ctx.game.tab(Tab.INVENTORY);
			utils.clickItemInInv(882);
			utils.clickItemInInv(841);
			Condition.sleep(Random.nextInt(500, 700));
		}

		if ((v.varpbit(281) == 480 || v.varpbit(281) == 490)) {
			if (System.currentTimeMillis() - lastCheckedFighting > Random
					.nextInt(1400, 1700)) {
				Npc rat = utils.attackableNpc(3313);
				ctx.camera.turnTo(rat);
				rat.interact("Attack");
				Condition.sleep(Random.nextInt(500, 700));
			}
		}

		if (v.varpbit(281) == 500) {
			utils.travelToTile(3111, 9524);
			Condition.sleep(Random.nextInt(500, 700));
			utils.interactWithObject(9727, "Climb-up");
			Condition.sleep(Random.nextInt(500, 700));
			ctx.camera.pitch(true);
		}
	}

	public void bankTask() {
		if (v.varpbit(281) == 510) {
			if (player.tile().distanceTo(new Tile(3121, 3121, 0)) > 7) {
				utils.travelToTile(3121, 3121);
				Condition.sleep(Random.nextInt(500, 700));
			}
			if (!ctx.chat.chatting())
				utils.interactWithObject(10083, "Use");

			if (w.component(219, 0).component(1).visible())
				w.component(219, 0).component(1).click();

			ctx.bank.close();
		}

		if (v.varpbit(281) == 520) {
			if (!ctx.chat.chatting())
				utils.interactWithObject(26801, "Use");

			if (w.widget(12).valid())
				w.component(12, 3).component(3).click();

			if (w.component(193, 2).visible()) {
				w.component(193, 2).click();
				Condition.sleep(Random.nextInt(500, 700));
			}
		}

		if (v.varpbit(281) == 525) {
			ctx.movement.step(new Tile(3124, 3124, 0));
			Condition.sleep(Random.nextInt(500, 700));
			utils.interactWithObject(9721, "Open");
			Condition.sleep(Random.nextInt(500, 700));
		}
	}

	public void financeTask() {
		if (v.varpbit(281) == 530 && !c.chatting())
			utils.interactWithNpc(3310, "Talk-to");
		if (v.varpbit(281) == 540 && !c.chatting())
			utils.interactWithObject(9722, "Open");
	}

	public void prayerTask() {
		if (v.varpbit(281) == 550 || v.varpbit(281) == 570
				|| v.varpbit(281) == 600) {
			if (player.tile().x() > 3128) {
				utils.travelToTile(3130, 3107);
				Condition.sleep(Random.nextInt(500, 700));
			}

			final GameObject ld = ctx.objects.select().name("Large Door")
					.nearest().poll();
			if (ld.id() == 1524 || ld.id() == 1521 && player.tile().x() > 3128) {
				utils.interactWithObject(ld.id(), "Open");
				Condition.sleep(Random.nextInt(250, 400));
			}

			if (!c.chatting())
				utils.interactWithNpc(3319, "Talk-to");
		}
	}

	public void openPrayerDoorTask() {
		if (v.varpbit(281) == 610) {
			ctx.camera.angle('s');
			utils.interactWithObject(9723, "Open");
		}
	}

	public void mageTalkTask() {
		if (v.varpbit(281) == 620) {
			if (!utils.canISeeNpc(3309))
				utils.travelToTile(3141, 3087);

			if (!c.chatting())
				utils.interactWithNpc(3309, "Talk-to");
		}
	}

	public void mageCastTask() {
		if (v.varpbit(281) == 650) {
			ctx.game.tab(Tab.MAGIC);
			Npc chicken = ctx.npcs.select().name("Chicken").nearest().poll();
			if (!chicken.inViewport())
				ctx.camera.turnTo(chicken);

			if (player.tile().y() < 3090)
				ctx.movement.step(new Tile(3140, 3091, 0));

			ctx.camera.pitch(true);

			ctx.magic.cast(Spell.WIND_STRIKE);
			Condition.sleep(Random.nextInt(100, 300));
			chicken.interact("Cast");
			Condition.sleep(Random.nextInt(500, 700));
		}
		if (v.varpbit(281) == 670 && !c.chatting()
				&& !w.widget(193).component(2).visible()) {
			utils.interactWithNpc(3309, "Talk-to");
		}
		if (w.component(219, 0).component(1).visible())
			w.component(219, 0).component(1).click();

		if (w.widget(193).component(2).visible())
			w.widget(193).component(2).click();
	}
}
