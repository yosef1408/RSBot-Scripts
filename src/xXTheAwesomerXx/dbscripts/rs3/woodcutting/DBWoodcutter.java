package xXTheAwesomerXx.dbscripts.rs3.woodcutting;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.concurrent.Callable;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Locatable;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Action;
import org.powerbot.script.rt6.Bank.Amount;
import org.powerbot.script.rt6.Camera;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Constants;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Interactive;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.Npc;
import org.powerbot.script.rt6.Player;
import org.powerbot.script.rt6.Widget;

@Script.Manifest(name = "DBWoodcutter", description = "Progressive Woodcutter made by xXTheAwesomerXx", properties = "author=xXTheAwesomerXx; topic=1296051; hidden=true;”)
public class DBWoodcutter extends PollingScript<ClientContext> implements
		MessageListener, PaintListener {

	private enum State {
		RUN, CHOP, BANK, DROP, DO_TRADE, RUN_AWAY, FACE_DEST
	};

	private final Trade trade = new Trade(ctx);
	public static boolean bankRegular = false, bankOak = false,
			bankWillow = true, bankMaples = true, bankYews = true,
			chopOaks = false, chopWillows = false, chopMaples = true,
			chopYews = false, chopIvy = true, dropRocks = false,
			dropNests = false;
	public static int oakLevel = 15, willowLevel = 30, mapleLevel = 45,
			yewLevel = 60, ivyLevel = 68;
	private int randomInventInt = 22;

	public static boolean getNotes = false, doTrade = false,
			allowTrade = false, guiShowing = false, addToToolbelt = true;
	private static boolean moveAway = false;

	public static String revisionString = "8", statusMessage = "Starting...",
			muleName = "", getLevelMessage = "", getNotesMessage = "",
			tradePlayerMessage = "", regDropAction = "Drop",
			oakDropAction = "Drop", willowDropAction = "Drop",
			mapleDropAction = "Drop", yewDropAction = "Drop",
			oakLocation = "Port Sarim", willowLocation = "Draynor",
			mapleLocation = "Seers' Village (Bank)", yewLocation = "Varrock",
			ivyLocation = "Varrock (N)";
	public static long START_TIME = System.currentTimeMillis(),
			CHOP_TIME = System.currentTimeMillis(), TASK_TIME = System
					.currentTimeMillis();
	private long totalRuntime, chopRuntime, taskRuntime;

	private final long START_EXPERIENCE = ctx.skills
			.experience(Constants.SKILLS_WOODCUTTING), START_FM_EXP = ctx.skills.experience(Constants.SKILLS_FIREMAKING);
	private long TASK_START_EXPERIENCE = ctx.skills
			.experience(Constants.SKILLS_WOODCUTTING), TASK_START_FM_EXP = ctx.skills.experience(Constants.SKILLS_FIREMAKING);
	private long expGained, taskExpGained, expFMGained, taskExpFMGained;
	private long expHr, taskExpHr;
	private long expToLvl;
	private int taskChopped = 0, logsChopped = 0,
			currentWoodcuttingLevel = ctx.skills
					.level(Constants.SKILLS_WOODCUTTING);
	public static boolean zoomLevelSet = false, dropping = false;

	private State getState() {
		if (!inCombat()) {
			if (!doTrade) {
				if (!getNotes) {
					if (!hasExtraItems()) {
						if (ctx.backpack.select().id(getAssignmentItemId())
								.count() <= getRandomInventInt()
								&& !dropping) {
							if (atTreeArea()) {
								statusMessage = "Chopping...";
								return State.CHOP;
							} else {
								statusMessage = "Running to Task Location";
								return State.RUN;
							}
						} else {
							if (bankLogs()) {
								if (atBankArea()) {
									statusMessage = "Banking...";
									return State.BANK;
								} else {
									statusMessage = "Running to Bank";
									return State.RUN;
								}
							} else {
								statusMessage = "Dropping";
								dropping = true;
								return State.DROP;
							}
						}
					} else {
						statusMessage = "Dropping Extras";
						return State.DROP;
					}
				} else {
					if (atBankArea()) {
						statusMessage = "Banking for notes";
						return State.BANK;
					} else {
						statusMessage = "Running for notes";
						return State.RUN;
					}
				}
			} else {
				statusMessage = "Trading...";
				return State.DO_TRADE;
			}
		} else {
			statusMessage = "Running away!";
			return State.RUN_AWAY;
		}
	}

	@Override
	public void repaint(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.setColor(Color.YELLOW);
		g.drawLine(ctx.input.getLocation().x - 5,
				ctx.input.getLocation().y - 5, ctx.input.getLocation().x + 5,
				ctx.input.getLocation().y + 5);
		g.drawLine(ctx.input.getLocation().x - 5,
				ctx.input.getLocation().y + 5, ctx.input.getLocation().x + 5,
				ctx.input.getLocation().y - 5);
		g.drawLine(0, ctx.input.getLocation().y, ctx.input.getLocation().x,
				ctx.input.getLocation().y);
		g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y, 2500,
				ctx.input.getLocation().y);
		g.drawLine(ctx.input.getLocation().x, 0, ctx.input.getLocation().x,
				ctx.input.getLocation().y);
		g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y,
				ctx.input.getLocation().x, 2500);
		totalRuntime = System.currentTimeMillis() - START_TIME;
		chopRuntime = System.currentTimeMillis() - CHOP_TIME;
		taskRuntime = System.currentTimeMillis() - TASK_TIME;
		expGained = ctx.skills.experience(Constants.SKILLS_WOODCUTTING)
				- START_EXPERIENCE;
		taskExpGained = ctx.skills.experience(Constants.SKILLS_WOODCUTTING)
				- TASK_START_EXPERIENCE;
		expHr = (expGained * 3600000) / totalRuntime;
		taskExpHr = (taskExpGained * 3600000) / taskRuntime;
		expToLvl = ctx.skills.experienceAt(ctx.skills
				.level(Constants.SKILLS_WOODCUTTING) + 1)
				- ctx.skills.experience(Constants.SKILLS_WOODCUTTING);
		currentWoodcuttingLevel = ctx.skills
				.level(Constants.SKILLS_WOODCUTTING);
		g.setColor(new Color(0, 0, 0, 95));
		g.fillRect(5, 35, 523, 154);
		g.setColor(new Color(204, 0, 0));
		g.drawString("DBWoodcutter Revision" + revisionString + " : "
				+ statusMessage, 10, 50);
		g.drawString("Total Runtime: " + formatTime(totalRuntime)
				+ " | Task Time: " + formatTime(taskRuntime), 10, 65);
		g.drawString("Last Chop: " + chopRuntime + "ms", 10, 80);
		g.drawString("Time to Lvl "
				+ (currentWoodcuttingLevel != 99 ? currentWoodcuttingLevel + 1
						: "X") + ": "
				+ formatTime((expHr <= 0 ? 0 : (expToLvl * 3600000) / expHr)),
				10, 95);
		g.drawString(
				"Chopping Task: " + getAssignmentString() + " at "
						+ getAssignmentLocation() + " | Banking Enabled: "
						+ bankLogs(), 10, 110);
		g.drawString("Exp Gained: " + expGained + " | Exp Gained(Task): "
				+ taskExpGained, 10, 125);
		g.drawString("Exp/Hr: " + expHr + " | Exp/Hr(Task): " + taskExpHr, 10,
				140);
		g.drawString("Exp to Lvl " + (currentWoodcuttingLevel + 1) + ": "
				+ expToLvl + " | Logs to Lvl " + (currentWoodcuttingLevel + 1)
				+ ": " + Math.round((expToLvl / getExperiencePerLog()) * 100.0)
				/ 100.0, 10, 155);
		g.drawString("Total Logs: " + logsChopped + ", " + taskChopped + " "
				+ getAssignmentString(), 10, 170);
		g.drawString("Logs/Hr(Total): " + (logsChopped * 3600000)
				/ totalRuntime + " | Logs/Hr(" + getAssignmentString() + "): "
				+ (taskChopped * 3600000) / taskRuntime, 10, 185);
		if (getState().equals(State.DROP)) {
			final Tile tile = ctx.players.local().tile();
			g.setColor(new Color(25, 10, 0, 95));
			for (Polygon poly : tile.matrix(ctx).triangles()) {
				g.drawPolygon(poly);
			}
		}
	}

	// ---- End Paint Listener ----

	private String formatTime(long millis) {
		int hours = (int) (millis / 3600000);
		millis %= 3600000;

		int minutes = (int) (millis / 60000);
		millis %= 60000;

		int seconds = (int) (millis / 1000);

		return (hours < 10 ? "0" + hours : hours) + " : "
				+ (minutes < 10 ? "0" + minutes : minutes) + " : "
				+ (seconds < 10 ? "0" + seconds : seconds);
	}

	@Override
	public void messaged(MessageEvent m) {
		if (m.source().isEmpty()) {
			if (m.text().contains("You get some")
					|| m.text().contains("You successfully chop away some")) {
				System.out.println("Got a message");
				if (getAssignmentString().equalsIgnoreCase("Regular")) {
					System.out.println("Assigned is regular");
					if (m.text().equalsIgnoreCase("You get some logs.")) {
						System.out.println("Got the sysmessage for regular");
						logsChopped++;
						taskChopped++;
						CHOP_TIME = System.currentTimeMillis();
					}
				} else {
					System.out.println("Assigned not regular");
					if (m.text().contains(getAssignmentString().toLowerCase())) {
						System.out.println("Contains 'assignment string'");
						logsChopped++;
						taskChopped++;
						CHOP_TIME = System.currentTimeMillis();
					} else {
						System.out.println("No good string: " + m.text());
					}
				}
			} else if (((m.text().contains(Integer.toString(oakLevel)) && chopOaks)
					|| (m.text().contains(Integer.toString(willowLevel)) && chopWillows)
					|| (m.text().contains(Integer.toString(mapleLevel)) && chopMaples)
					|| (m.text().contains(Integer.toString(yewLevel)) && chopYews) || (m
					.text().contains(Integer.toString(ivyLevel)) && chopIvy))
					&& m.text().contains("advanced")) {
				taskChopped = 0;
				TASK_TIME = System.currentTimeMillis();
				TASK_START_EXPERIENCE = ctx.skills
						.experience(Constants.SKILLS_WOODCUTTING);
				zoomLevelSet = false;
			} else if (m.text().contains("Accepted")
					|| m.text().contains("You can only give away")) {
				doTrade = false;
			} else if (m.text().contains("can't light a fire")) {
				moveAway = true;
			}
		}
		if (m.source().equalsIgnoreCase(muleName)) {
			if (m.text().equalsIgnoreCase("What's your woodcutting level?")) {
				ctx.input.sendln("");
				ctx.input.sendln(Integer.toString(ctx.skills
						.level(Constants.SKILLS_FISHING)));
			} else if (m.text().equalsIgnoreCase(
					"How many logs do you guys have?")) {
				ctx.input.sendln("");
				ctx.input.sendln(Integer.toString(ctx.backpack.select()
						.id(getAssignmentItemId()).count()
						+ ctx.backpack.select().id(getAssignmentItemId() + 1)
								.count(true))
						+ " " + getAssignmentString());
			} else if (m.text().equalsIgnoreCase("Trade me") && allowTrade) {
				doTrade = true;
			} else if (m.text().equalsIgnoreCase("Get notes") && allowTrade) {
				getNotes = true;
			}
		}
	}

	@Override
	public void start() {
		DBWoodcutterGUI gui = new DBWoodcutterGUI();
		gui.setVisible(true);
		while (gui.isVisible()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void stop() {
		this.ctx.dispatcher.remove(this);
	}

	@Override
	public void poll() {
		// while (1 == 1) {
		switch (getState()) {
		case FACE_DEST:
			if (!turnTo(ctx.movement.destination())) {
				ctx.camera.turnTo(ctx.movement.destination());
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						return facingTile(ctx.movement.destination());
					}
				}, 1000, 10);
			}
			break;
		case RUN_AWAY:
			if (!moveAway) {
				if (inCombat()) {
					final Tile npcInCombatWith = ctx.players.local()
							.interacting().tile();
					ctx.movement.step(npcInCombatWith.derive(5, 5));
				}
			} else {
				Tile moveAwayTile = new Tile(ctx.players.local().tile().x()
						+ Random.nextInt(2, 4), ctx.players.local().tile().y()
						+ Random.nextInt(2, 4));
				ctx.movement.step(moveAwayTile);
			}
			break;
		case DO_TRADE:
			if (!allowTrade) {
				doTrade = false;
			} else {
				if (trade.select().trading()) {
					if (trade.getTradePartner().equalsIgnoreCase(muleName)) {
						if (trade.offering()) {
							if (ctx.backpack.select()
									.id(getAssignmentItemId() + 1).count() == 0) {
								if (trade.partnerAcceptedOffer()) {
									trade.acceptOffer();
								} else if (!trade.acceptedOffer()) {
									trade.acceptOffer();
								}
							} else {
								int assignmentItemIndex = ctx.backpack.select()
										.id(getAssignmentItemId() + 1)
										.shuffle().poll().component().index();
								final Widget widget = ctx.widgets.widget(336);
								if (widget.valid()) {
									final Component component = widget
											.component(0).component(
													assignmentItemIndex);
									if (component.itemId() == (getAssignmentItemId() + 1)) {
										component.interact("Offer-All");
									}
								}
							}
						} else if (trade.reviewing()) {
							if (trade.partnerAcceptedReview()) {
								trade.acceptReview();
							} else if (!trade.acceptedReview()) {
								trade.acceptReview();
							}
						}
					} else {
						if (trade.offering()) {
							trade.declineOffer();
						} else {
							trade.declineReview();
						}
					}
				} else {
					ctx.players.select().name(muleName).poll()
							.interact("Trade");
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return trade.trading();
						}
					}, 2000, 5);
				}
			}
			break;
		case CHOP:
			if (ctx.backpack.collapsed()) {
				System.out.println("Backpack collapsed: " + ctx.backpack.collapsed());
				ctx.widgets.widget(Constants.BACKPACK_WIDGET).component(0).click();
			}
			final Widget bankWidgetC = ctx.widgets.widget(11);
			if (bankWidgetC.valid()) {
				ctx.input.click(bankWidgetC.component(41).component(1)
						.nextPoint(), true);
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						return !bankWidgetC.valid();
					}
				}, 1000, 20);
			}
			if (ctx.bank.opened()) {
				ctx.bank.close();
				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() {
						return !ctx.bank.opened();
					}
				}, 1000, 20);
			}
			if (getAssignmentString().equalsIgnoreCase("Willow")
					&& willowLocation.equalsIgnoreCase("Draynor Village")) {
				if (ctx.widgets.widget(1184).component(11).text()
						.equalsIgnoreCase("Guard in tree")) {
					ctx.movement.step(ctx.players.local().tile().derive(2, 2));
					ctx.camera.angle('n');
				}
			}
			if (getAssignmentString().equalsIgnoreCase("Ivy")) {
				if (ivyLocation.equalsIgnoreCase("Varrock (N)")) {
					if (ctx.camera.yaw() < 134 || ctx.camera.yaw() > 236) {
						combineCamera(Random.nextInt(140, 230),
								Random.nextInt(9, 30));
						// ctx.camera.angle('s');
						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() {
								final Camera c = ctx.camera;
								return c.yaw() >= 134
										|| ctx.camera.yaw() <= 236;
							}
						}, 1000, 20);
					}
				} else {
					if (ivyLocation.equalsIgnoreCase("Falador (SE)")) {
						if (ctx.camera.yaw() < 10 || ctx.camera.yaw() > 80) {
							combineCamera(Random.nextInt(14, 75),
									Random.nextInt(9, 30));
							// ctx.camera.angle('s');
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									final Camera c = ctx.camera;
									return c.yaw() >= 10
											|| ctx.camera.yaw() <= 80;
								}
							}, 1000, 20);
						}
					}
				}
			}
			if (!getAssignmentString().equalsIgnoreCase("Ivy")) {
				if (ctx.camera.pitch() < 15 || ctx.camera.pitch() > 55) {
					combineCamera(
							Random.nextInt(ctx.camera.yaw() - 5,
									ctx.camera.yaw() + 5),
							Random.nextInt(20, 50));
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() {
							final Camera c = ctx.camera;
							return c.pitch() >= 15 || ctx.camera.pitch() <= 55;
						}
					}, 1000, 20);
				}
			} else {
				if (ctx.camera.pitch() < 2 || ctx.camera.pitch() > 18) {
					combineCamera(
							Random.nextInt(ctx.camera.yaw() - 5,
									ctx.camera.yaw() + 5),
							Random.nextInt(4, 15));
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() {
							final Camera c = ctx.camera;
							return c.pitch() >= 2 || ctx.camera.pitch() <= 18;
						}
					}, 1000, 20);
				}
			}
			//TODO ADD
			if (zoomLevelSet == false) {
				setZoomLevel(Random.nextInt(getZoomLevel() - 1,
						getZoomLevel() + 1));
			}
			if (addToToolbelt) {
				addBetterAxeToToolbelt();
			} else {
				equipBetterAxe();
			}
			// END TODO ADD
			GameObject objFinder = null;
			final int[] ivyBounds = { -128, 128, -664, -256, -64, 86 };
			if (!getAssignmentString().equalsIgnoreCase("Ivy")) {
				objFinder = ctx.objects.select(25).id(getAssignmentTreeIds())
						.within(getAssignmentArea()).nearest().limit(3).poll();
			} else {
				objFinder = ctx.objects.select(10).id(getAssignmentTreeIds())
						.each(Interactive.doSetBounds(ivyBounds))
						.within(getAssignmentArea()).nearest().limit(3).poll();
			}
			GameObject obj = objFinder;
			if (obj.inViewport()) {
				if (ctx.players.local().animation() != -1) {
					if (bankLogs() == false) {
						final int randomInt = Random.nextInt(5, 15);
						final int animationId = ctx.players.local().animation();
						if (animationId == 21191 || animationId == 872) {
							if (ctx.backpack.select().count() > (getRandomInventInt() - randomInt)
									&& !getAssignmentString().equalsIgnoreCase(
											"Ivy")) {
								final Point pointOfRandomLog = ctx.backpack
										.id(getAssignmentItemId()).first()
										.poll().nextPoint();
								if (ctx.chat.queryContinue()) {
									ctx.chat.clickContinue(true);
								} else {
									if (!ctx.combatBar.select()
											.id(getAssignmentItemId())
											.isEmpty()) {
										Action dropLogs = ctx.combatBar.first()
												.poll();
										final int oldIndex = ctx.backpack
												.select()
												.id(getAssignmentItemId())
												.count();
										ctx.combatBar
												.actionAt(dropLogs.slot())
												.component()
												.interact(
														getAssignmentDropAction());
										Condition.wait(new Callable<Boolean>() {
											@Override
											public Boolean call() {
												return ctx.backpack.select()
														.count() <= oldIndex
														&& ctx.players.local()
																.animation() == -1;
											}
										}, 750, 10);
									} else {
										try {
											ctx.input.move(pointOfRandomLog);
											ctx.input.drag(ctx.combatBar
													.first().poll().component()
													.centerPoint(), true);
											Condition.wait(
													new Callable<Boolean>() {
														@Override
														public Boolean call() {
															return !(ctx.combatBar
																	.select()
																	.id(getAssignmentItemId())
																	.isEmpty());
														}
													}, 2500, 20);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}
						} else {
							setRandomInventInt(Random.nextInt(18, 23));
						}
					}
				} else {
					ctx.input.speed(Random.nextInt(3, 7));
					if (!getAssignmentString().equalsIgnoreCase("Ivy")) {
						if (ctx.players.local().animation() == -1) {
							obj.interact(true, "Chop down",
									getAssignmentTreeString());
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									final Player p = ctx.players.local();
									return p.animation() == -1 && !p.inMotion()
											&& p.idle();
								}
							}, 1200, 10);
						} else {
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									final Player p = ctx.players.local();
									return p.animation() == -1 && !p.inMotion()
											&& p.idle();
								}
							}, 1200, 10);
						}
					} else {
						if (ctx.players.local().animation() == -1) {
							obj.interact(true, "Chop",
									getAssignmentTreeString());
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									final Player p = ctx.players.local();
									return p.animation() == -1 && !p.inMotion()
											&& p.idle();
								}
							}, 2200, 15);
						}
					}
				}
			} else {
				if (!getAssignmentString().equalsIgnoreCase("Ivy")) {
					ctx.camera.turnTo(obj.tile().derive(Random.nextInt(-6, 6),
							Random.nextInt(-6, 6)));
				} else {
					ctx.movement.step(ctx.movement.newTilePath(obj.tile())
							.next());
				}
			}
			break;
		case DROP:
			if (!hasExtraItems()) {
				if (ctx.backpack.select().id(getAssignmentItemId()).count() >= (28 - getRandomInventInt())) {
					if (!tileHasFire()) {
						final Point pointOfRandomLog = ctx.backpack
								.id(getAssignmentItemId()).first().poll()
								.nextPoint();
						if (ctx.chat.queryContinue()) {
							ctx.chat.clickContinue(true);
						} else {
							if (!ctx.combatBar.select()
									.id(getAssignmentItemId()).isEmpty()) {
								Action dropLogs = ctx.combatBar.first().poll();
								final int oldIndex = ctx.backpack.select()
										.id(getAssignmentItemId()).count();
								ctx.combatBar.actionAt(dropLogs.slot())
										.component()
										.interact(getAssignmentDropAction());
								Condition.wait(new Callable<Boolean>() {
									@Override
									public Boolean call() {
										return ctx.backpack.select().count() <= oldIndex
												&& ctx.players.local()
														.animation() == -1;
									}
								}, 1000, 2);
							} else {
								try {
									ctx.input.move(pointOfRandomLog);
									ctx.input.drag(ctx.combatBar.first().poll()
											.component().centerPoint(), true);
									Condition.wait(new Callable<Boolean>() {
										@Override
										public Boolean call() {
											return !(ctx.combatBar.select().id(
													getAssignmentItemId())
													.isEmpty());
										}
									}, 2000, 5);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} else {
						final Tile moveAwayTile = new Tile(ctx.players.local()
								.tile().x()
								+ Random.nextInt(2, 4), ctx.players.local()
								.tile().y()
								+ Random.nextInt(2, 4));
						if (ctx.movement.step(moveAwayTile)) {
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return !ctx.players.local().inMotion();
								}
							}, 1000, 5);
							moveAway = false;
						}
					}
				} else {
					dropping = false;
				}
			} else {
				statusMessage = "Has extra logs, dropping...";
				if (hasExtraItems()) {
					for (Item i : ctx.backpack.select().id(extraItemIds())) {
						if (ctx.chat.queryContinue()) {
							ctx.chat.clickContinue(true);
						} else {
							final int oldIndex = ctx.backpack.select()
									.id(getAssignmentItemId()).count();
							i.interact("Drop");
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return ctx.backpack.select().count() < oldIndex
											|| ctx.players.local().animation() == -1;
								}
							}, 1200, 10);
						}
					}
				}
			}
			break;
		case BANK:
			if (getAssignmentString().equalsIgnoreCase("Regular")
					|| (getAssignmentString().equalsIgnoreCase("Oak") && oakLocation
							.equalsIgnoreCase("Port Sarim"))
					|| (getAssignmentString().equalsIgnoreCase("Willow") && willowLocation
							.equalsIgnoreCase("Port Sarim"))) {
				final Widget bankWidget = ctx.widgets.widget(11);
				if (bankWidget.valid()) {
					if (!hasLogsToBank()) {
						if (ctx.backpack.select().id(getAssignmentItemId())
								.count() > 0) {
							int assignmentItemIndex = ctx.backpack.shuffle()
									.peek().component().index();
							final Component component = bankWidget.component(1)
									.component(assignmentItemIndex);
							if (component.itemId() == (getAssignmentItemId())) {
								component.interact("Deposit-All");
							}
						}
					} else {
						if (hasLogsToBank()) {
							for (int i = 0; i < otherLogIds().length; i++) {
								if (ctx.backpack.select().id(otherLogIds()[i])
										.count() > 0) {
									final int hasLogId = otherLogIds()[i];
									int assignmentItemIndex = ctx.backpack
											.shuffle().poll().component()
											.index();
									final Component component = bankWidget
											.component(1).component(
													assignmentItemIndex);
									component.interact("Deposit-All");
									Condition.wait(new Callable<Boolean>() {
										@Override
										public Boolean call() {
											return ctx.backpack.select()
													.id(hasLogId).count() == 0;
										}
									}, 1000, 20);

								}
							}
						}
					}
				} else {
					final GameObject depositBox = ctx.objects.select()
							.id(36788).nearest().poll();
					if (depositBox.valid()) {
						if (depositBox.inViewport()) {
							depositBox.interact(true, "Deposit");
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return bankWidget.valid();
								}
							}, 1000, 20);
						} else {
							ctx.camera.turnTo(depositBox);
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return depositBox.inViewport();
								}
							}, 1000, 20);
						}
					}
				}
			} else {
				if (!ctx.bank.opened()) {
					final Npc bankers = ctx.npcs.select()
							.id(getAssignmentBankerNpcIds()).nearest()
							.shuffle().poll();
					if (!bankers.valid()) {
						if (ctx.bank.inViewport()) {
							ctx.bank.open();
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return ctx.bank.opened();
								}
							}, 2000, 5);
						} else {
							ctx.camera.turnTo(ctx.bank.nearest());
							Condition.wait(new Callable<Boolean>() {
								@Override
								public Boolean call() {
									return ctx.bank.inViewport();
								}
							}, 1000, 5);
						}
					} else {
						bankers.interact("Bank");
						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() {
								return ctx.bank.opened()
										|| ctx.players.local().inMotion();
							}
						}, 2000, 5);
					}
				} else {
					if (!getNotes) {
						if (!hasLogsToBank()) {
							if (ctx.backpack.select().id(getAssignmentItemId())
									.count() > 0) {
								ctx.bank.deposit(getAssignmentItemId(),
										Amount.ALL);
							}

							withdrawBestUsableAxe();
							if (hasUnusableAxe()) {
								depositUnusableAxe();
							}
							if (hasExtraAxeInInventory()) {
								depositExtraAxeInInventory();
							}
						} else {
							if (hasLogsToBank()) {
								for (int i = 0; i < otherLogIds().length; i++) {
									if (ctx.backpack.select()
											.id(otherLogIds()[i]).count() > 0) {
										ctx.bank.deposit(otherLogIds()[i],
												Amount.ALL);
									}
								}
							}
						}
					} else {
						if (ctx.backpack.select().count() < 28) {
							if (ctx.bank.select().id(allLogIds()).count() > 0) {
								if (!ctx.bank.withdrawMode()) {
									ctx.bank.withdrawMode(true);
								} else {
									for (int i = 0; i < allLogIds().length; i++) {
										if (ctx.bank.select()
												.id(allLogIds()[i]).count() > 0) {
											ctx.bank.withdraw(allLogIds()[i],
													Amount.ALL);
										}
									}
								}
							} else {
								getNotes = false;
							}
						} else {
							ctx.bank.deposit(getAssignmentItemId(), Amount.ALL);
						}
					}
				}
			}
			break;
		case RUN:
			if (ctx.bank.opened()) {
				ctx.bank.close();
			}
			if (!doTrade && trade.trading()) {
				if (trade.offering()) {
					trade.declineOffer();
				} else if (trade.reviewing()) {
					trade.declineReview();
				}
			}
			equipBetterAxe();
			if (hasDestination()) {
				if (!facingDestination()) {
					ctx.camera.turnTo(ctx.movement.destination());
				}
			}
			ctx.camera.turnTo(ctx.movement.destination());
			if ((getAssignmentString().equalsIgnoreCase("Willow") || getAssignmentString()
					.equalsIgnoreCase("Yew")) && getNotes) {
				Tile pathToBank = ctx.movement.findPath(
						getAssignmentBankArea().getCentralTile()).next();
				if (pathToBank != null && pathToBank.x() != -1) {
					ctx.movement.step(pathToBank);
				} else {
					if (getAssignmentString().equalsIgnoreCase("Yew")
							|| (getAssignmentString().equalsIgnoreCase("Ivy") && ivyLocation
									.equalsIgnoreCase("Varrock (N)"))) {
						if (yewLocation.equalsIgnoreCase("Varrock")
								|| ivyLocation.equalsIgnoreCase("Varrock (N)")) {
							if (!inVarrockYewArea()) {
								teleportToKnownLocation();
							} else {
								pathToBank = ctx.movement
										.newTilePath(Paths.pathToVarrockYewBank)
										.randomize(1, 4).next();
								ctx.movement.step(pathToBank);
							}
						} else {
							if (ivyLocation.equalsIgnoreCase("Falador (SE)")) {
								if (ctx.players.local().tile().x() > 3059) {
									teleportToKnownLocation();
								} else {
									pathToBank = ctx.movement
											.newTilePath(
													ctx.movement
															.newTilePath(
																	Paths.ivyToFallyBank)
															.array())
											.randomize(1, 4).next();
								}
							} else {
								statusMessage = "Not doing varrock yews, but need to run to yew bank - Report this";
							}
						}
					}
				}
			} else {
				if (ctx.backpack.select().count() <= getRandomInventInt()) {
					Tile pathToTreeArea = null;
					if (getAssignmentString().equalsIgnoreCase("Willow")) {
						if (!willowLocation.equalsIgnoreCase("Draynor Village")) {
							if (!inOverallPortSarimArea()) {
								pathToTreeArea = ctx.movement.findPath(
										new Tile(3057, 3250)).next();
							} else {
								pathToTreeArea = ctx.movement
										.newTilePath(
												Paths.portSarimLodestoneToPortSarimWillows)
										.next();
							}
						} else {
							if (!inOverallPortSarimArea()) {
								pathToTreeArea = ctx.movement.findPath(
										new Tile(3083, 3236)).next();
								if (Double.isInfinite(pathToTreeArea
										.distanceTo(ctx.players.local()))) {
									if (comingFromDraynorLodestone()) {
										pathToTreeArea = ctx.movement
												.newTilePath(
														Paths.draynorLodestoneToWillows)
												.next();
									}
								}
							} else {
								pathToTreeArea = ctx.movement.newTilePath(
										Paths.sarimToDraynorPath).next();
							}
						}
					} else if (getAssignmentString().equalsIgnoreCase("Yew")
							|| (getAssignmentString().equalsIgnoreCase("Ivy") && ivyLocation
									.equalsIgnoreCase("Varrock (N)"))) {
						if (ctx.players.local().tile().x() > 3164) {
							pathToTreeArea = ctx.movement.newTilePath(
									Paths.varrockLodestoneToYews).next();
						} else {
							if (Lodestone.VARROCK.canUse(ctx)) {
								teleportToKnownLocation();
							} else {
								pathToTreeArea = ctx.movement.newTilePath(
										Paths.overallPathToVarrockOaks).next();
							}
						}
					} else if (getAssignmentString().equalsIgnoreCase("Ivy")
							&& ivyLocation.equalsIgnoreCase("Falador (SE)")) {
						if (inOverallFaladorArea()) {
							pathToTreeArea = ctx.movement.newTilePath(
									Paths.faladorLodestoneToIvy).next();
						} else {
							if (!Lodestone.FALADOR.canUse(ctx)) {
								if (ctx.players.local().tile().y() > 3325) {
									if (ctx.players.local().tile().x() > 3059) {
										pathToTreeArea = ctx.movement
												.newTilePath(
														Paths.varrockToFallyIvy)
												.randomize(1, 4).next();
									} else {
										pathToTreeArea = ctx.movement
												.newTilePath(
														ctx.movement
																.newTilePath(
																		Paths.ivyToFallyBank)
																.reverse()
																.array())
												.randomize(1, 4).next();
									}
								} else {
									pathToTreeArea = ctx.movement
											.newTilePath(
													Paths.portSarimDraynorToFallyIvy)
											.randomize(1, 4).next();
								}
							} else {
								teleportToKnownLocation();
							}
						}
					} else if (getAssignmentString().equalsIgnoreCase("Maple")) {
						if (inOverallSeersVillage()) {
							if (mapleLocation
									.equalsIgnoreCase("Seers' Village (Bank)")) {
								if (ctx.players.local().tile().x() > 2683
										&& ctx.players.local().tile().x() < 2738
										&& ctx.players.local().tile().y() > 3464) {
									System.out
											.println("Using LS to seers maple");
									pathToTreeArea = ctx.movement.newTilePath(
											Paths.lodestoneToSeersBankMaple)
											.next();
								} else {
									System.out
											.println("Using LS to seers bank");
									pathToTreeArea = ctx.movement.newTilePath(
											Paths.lodestoneMaplesToSeersBank)
											.next();
								}
							} else {
								if (mapleLocation
										.equalsIgnoreCase("Seers' Village (NLS)")) {
									if (ctx.players.local().tile().x() < 2695) {
										pathToTreeArea = ctx.movement
												.newTilePath(
														Paths.lodestoneToLodestoneMaples)
												.randomize(1, 4).next();
									} else {
										pathToTreeArea = ctx.movement
												.newTilePath(
														ctx.movement
																.newTilePath(
																		Paths.lodestoneMaplesToSeersBank)
																.reverse()
																.array())
												.randomize(1, 4).next();
									}
								}
							}
						} else {
							teleportToKnownLocation();
						}
					} else if (getAssignmentString().equalsIgnoreCase("Oak")) {
						if (oakLocation.equalsIgnoreCase("Port Sarim")) {
							pathToTreeArea = ctx.movement.newTilePath(
									Paths.portSarimLodeStoneToTrees).next();
						} else if (oakLocation.equalsIgnoreCase("Varrock (W)")) {
							if (ctx.players.local().tile().x() > 3164) {
								pathToTreeArea = ctx.movement.newTilePath(
										Paths.varrockLodestoneToOaks).next();
							} else {
								if (Lodestone.VARROCK.canUse(ctx)) {
									teleportToKnownLocation();
								} else {
									pathToTreeArea = ctx.movement.newTilePath(
											Paths.overallPathToVarrockOaks)
											.next();
								}
							}
						}
					} else {
						pathToTreeArea = ctx.movement.findPath(
								getAssignmentArea().getRandomTile()).next();
					}
					if (pathToTreeArea != null && pathToTreeArea.x() != -1) {
						ctx.movement.step(pathToTreeArea);// TODO
						final Tile pathStep = pathToTreeArea;
						System.out.println("Stepped");
						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() {
								System.out.println("TR:" + ctx.movement.distance(pathStep));
								return ctx.movement.distance(pathStep) < Random.nextInt(45, 60);
							}
						}, 1000, 10);
						System.out.println("Broke");
					} else {
						teleportToKnownLocation();
					}
				} else {
					Tile pathToBank = ctx.movement.findPath(
							getAssignmentBankArea().getRandomTile()).next();
					if (pathToBank != null && pathToBank.x() != -1) {
						ctx.movement.step(pathToBank);
					} else {
						if (getAssignmentString().equalsIgnoreCase("Yew")) {
							if (yewLocation.equalsIgnoreCase("Varrock")) {
								if (!inVarrockYewArea()) {
									teleportToKnownLocation();
								} else {
									pathToBank = ctx.movement.newTilePath(
											Paths.pathToVarrockYewBank).next();
									ctx.movement.step(pathToBank);
								}
							} else {
								statusMessage = "Not doing varrock yews, but need to run to yew bank";
							}
						} else if (getAssignmentString().equalsIgnoreCase(
								"Willow")
								&& willowLocation
										.equalsIgnoreCase("Port Sarim")) {
							if (!inOverallPortSarimArea()) {
								teleportToKnownLocation();
							} else {
								pathToBank = ctx.movement.newTilePath(
										Paths.pathToPortSarimBank).next();
								ctx.movement.step(pathToBank);
							}
						} else if (getAssignmentString()
								.equalsIgnoreCase("Oak")
								&& willowLocation
										.equalsIgnoreCase("Varrock (W)")) {
							pathToBank = ctx.movement
									.newTilePath(
											ctx.movement
													.newTilePath(
															Paths.varrockWestBankToOaks)
													.reverse().array()).next();
						}
					}
				}
			}
			break;
		default:
			Tile moveAwayTile = new Tile(ctx.players.local().tile().x()
					+ Random.nextInt(2, 4), ctx.players.local().tile().y()
					+ Random.nextInt(2, 4));
			ctx.movement.step(moveAwayTile);
			break;
		}
		// }
	}

	private void teleportToKnownLocation() {
		if (ctx.players.local().idle()) {
			if (getAssignmentString().equalsIgnoreCase("Willow")
					&& willowLocation.equalsIgnoreCase("Draynor Village")) {
				if (!inOverallDraynorArea()) {
					if (Lodestone.DRAYNOR.canUse(ctx)) {
						Lodestone.DRAYNOR.teleport(ctx);
					} else {
						if (Lodestone.PORT_SARIM.canUse(ctx)) {
							Lodestone.PORT_SARIM.teleport(ctx);
						} else {
							stop();
						}
					}
				}
			} else {
				if (getAssignmentString().equalsIgnoreCase("Yew")) {
					if (yewLocation.equalsIgnoreCase("Varrock")) {
						if (Lodestone.VARROCK.canUse(ctx)) {
							Lodestone.VARROCK.teleport(ctx);
						} else {
							if (Lodestone.DRAYNOR.canUse(ctx)) {
								Lodestone.DRAYNOR.teleport(ctx);
							} else {
								if (Lodestone.PORT_SARIM.canUse(ctx)) {
									Lodestone.PORT_SARIM.teleport(ctx);
								} else {
									stop();
								}
							}
						}
					} else {
						if (Lodestone.DRAYNOR.canUse(ctx)) {
							Lodestone.DRAYNOR.teleport(ctx);
						} else {
							if (Lodestone.PORT_SARIM.canUse(ctx)) {
								Lodestone.PORT_SARIM.teleport(ctx);
							} else {
								stop();
							}
						}
					}
				} else if (getAssignmentString().equalsIgnoreCase("Maple")) {
					if (Lodestone.SEERS_VILLAGE.canUse(ctx)) {
						Lodestone.SEERS_VILLAGE.teleport(ctx);
					} else {
						stop();
					}
				} else if (getAssignmentString().equalsIgnoreCase("Ivy")) {
					if (ivyLocation.equalsIgnoreCase("Varrock (N)")) {
						if (Lodestone.VARROCK.canUse(ctx)) {
							Lodestone.VARROCK.teleport(ctx);
						} else {
							if (Lodestone.DRAYNOR.canUse(ctx)) {
								Lodestone.DRAYNOR.teleport(ctx);
							} else {
								if (Lodestone.PORT_SARIM.canUse(ctx)) {
									Lodestone.PORT_SARIM.teleport(ctx);
								} else {
									stop();
								}
							}
						}
					} else {
						if (Lodestone.FALADOR.canUse(ctx)) {
							Lodestone.FALADOR.teleport(ctx);
						} else if (Lodestone.PORT_SARIM.canUse(ctx)) {
							Lodestone.PORT_SARIM.teleport(ctx);
						} else if (Lodestone.DRAYNOR.canUse(ctx)) {
							Lodestone.DRAYNOR.teleport(ctx);
						}
					}
				} else if (getAssignmentString().equalsIgnoreCase("Oak")) {
					if (oakLocation.equalsIgnoreCase("Varrock (W)")) {
						if (Lodestone.VARROCK.canUse(ctx)) {
							Lodestone.VARROCK.teleport(ctx);
						} else {
							if (Lodestone.DRAYNOR.canUse(ctx)) {
								Lodestone.DRAYNOR.teleport(ctx);
							} else {
								if (Lodestone.PORT_SARIM.canUse(ctx)) {
									Lodestone.PORT_SARIM.teleport(ctx);
								} else {
									stop();
								}
							}
						}
					}
				} else {
					if (Lodestone.PORT_SARIM.canUse(ctx)) {
						Lodestone.PORT_SARIM.teleport(ctx);
					} else {
						stop();
					}
				}
			}
		}
	}

	private boolean hasUnusableAxe() {
		if (ctx.backpack.select().id(1355).count() > 0) {
			if (currentWoodcuttingLevel < 21) {
				return true;
			}
		}
		if (ctx.backpack.select().id(1357).count() > 0) {
			if (currentWoodcuttingLevel < 31) {
				return true;
			}
		}
		if (ctx.backpack.select().id(1359).count() > 0) {
			if (currentWoodcuttingLevel < 41) {
				return true;
			}
		}
		return false;
	}

	private void depositUnusableAxe() {
		if (ctx.bank.opened()) {
			if (ctx.backpack.select().id(1355).count() > 0) {
				if (currentWoodcuttingLevel < 21) {
					ctx.bank.deposit(1355, Amount.ALL);
				}
			}
			if (ctx.backpack.select().id(1357).count() > 0) {
				if (currentWoodcuttingLevel < 31) {
					ctx.bank.deposit(1357, Amount.ALL);
				}
			}
			if (ctx.backpack.select().id(1359).count() > 0) {
				if (currentWoodcuttingLevel < 41) {
					ctx.bank.deposit(1359, Amount.ALL);
				}
			}
		}
	}

	private void withdrawBestUsableAxe() {
		if (ctx.bank.opened()) {
			if (currentWoodcuttingLevel >= 41) {
				if (ctx.bank.select().id(1359).count() > 0) {
					if (returnToolbeltToItemId() != 1359) {
						if (ctx.equipment.select().id(1359).count() == 0) {
							if (ctx.backpack.select().id(1359).count() == 0) {
								ctx.bank.withdraw(1359, Amount.ONE);
							}
						}
					}
				} else if (ctx.bank.select().id(1357).count() > 0) {
					if (returnToolbeltToItemId() < 1357) {
						if (ctx.equipment.select().id(1357).count() == 0) {
							if (ctx.backpack.select().id(1357).count() == 0) {
								ctx.bank.withdraw(1357, Amount.ONE);
							}
						}
					}
				} else if (ctx.bank.select().id(1355).count() > 0) {
					if (returnToolbeltToItemId() < 1355) {
						if (ctx.equipment.select().id(1355).count() == 0) {
							if (ctx.backpack.select().id(1355).count() == 0) {
								ctx.bank.withdraw(1355, Amount.ONE);
							}
						}
					}
				}
			} else if (currentWoodcuttingLevel >= 31) {
				if (ctx.bank.select().id(1357).count() > 0) {
					if (returnToolbeltToItemId() < 1357) {
						if (ctx.equipment.select().id(1357).count() == 0) {
							if (ctx.backpack.select().id(1357).count() == 0) {
								ctx.bank.withdraw(1357, Amount.ONE);
							}
						}
					}
				} else if (ctx.bank.select().id(1355).count() > 0) {
					if (returnToolbeltToItemId() < 1355) {
						if (ctx.equipment.select().id(1355).count() == 0) {
							if (ctx.backpack.select().id(1355).count() == 0) {
								ctx.bank.withdraw(1355, Amount.ONE);
							}
						}
					}
				}
			} else if (currentWoodcuttingLevel >= 21) {
				if (ctx.bank.select().id(1355).count() > 0) {
					if (returnToolbeltToItemId() < 1355) {
						if (ctx.equipment.select().id(1355).count() == 0) {
							if (ctx.backpack.select().id(1355).count() == 0) {
								ctx.bank.withdraw(1355, Amount.ONE);
							}
						}
					}
				}
			}
		}
	}

	private int returnToolbeltToItemId() {
		int toolbeltAxe = ctx.varpbits.varpbit(1102);
		int itemId = 0;
		if (toolbeltAxe == 84934639)
			itemId = 1359;
		if (toolbeltAxe == 68157423)
			itemId = 1357;
		if (toolbeltAxe == 51380207)
			itemId = 1355;
		return itemId;
	}

	private void depositExtraAxeInInventory() {
		if (ctx.bank.opened()) {
			int bestAxeId = 0;
			if (currentWoodcuttingLevel >= 41) {
				if (ctx.backpack.select().id(1355).count() > 0) {
					bestAxeId = 1355;
				}
				if (ctx.backpack.select().id(1357).count() > 0) {
					bestAxeId = 1357;
				}
				if (ctx.backpack.select().id(1359).count() > 0) {
					bestAxeId = 1359;
				}
			} else if (currentWoodcuttingLevel >= 31) {
				if (ctx.backpack.select().id(1355).count() > 0) {
					bestAxeId = 1355;
				}
				if (ctx.backpack.select().id(1357).count() > 0) {
					bestAxeId = 1357;
				}
			} else if (currentWoodcuttingLevel >= 21) {
				if (ctx.backpack.select().id(1355).count() > 0) {
					bestAxeId = 1355;
				}
			}
			if (bestAxeId != 0) {
				for (Item item : ctx.backpack.select().id(1355, 1357, 1359)) {
					if (item.id() < bestAxeId
							|| ((!ctx.backpack.select().id(bestAxeId).isEmpty()) && returnToolbeltToItemId() >= bestAxeId)) {
						ctx.bank.deposit(item.id(), Amount.ALL);
					} else {
						if (ctx.backpack.select().id(item.id()).count() > 1) {
							ctx.bank.deposit(bestAxeId, (ctx.backpack.select()
									.id(bestAxeId).count() - 1));
						}
					}
				}
			}
		}
	}

	private boolean hasExtraAxeInInventory() {
		int bestAxeId = 0;
		boolean hasExtraAxe = false;
		if (currentWoodcuttingLevel >= 41) {
			if (ctx.backpack.select().id(1355).count() > 0) {
				bestAxeId = 1355;
			}
			if (ctx.backpack.select().id(1357).count() > 0) {
				if (bestAxeId == 1355) {
					hasExtraAxe = true;
				}
				bestAxeId = 1357;
			}
			if (ctx.backpack.select().id(1359).count() > 0) {
				if (bestAxeId == 1355 || bestAxeId == 1357) {
					hasExtraAxe = true;
				}
				bestAxeId = 1359;
			}
		} else if (currentWoodcuttingLevel >= 31) {
			if (ctx.backpack.select().id(1355).count() > 0) {
				bestAxeId = 1355;
			}
			if (ctx.backpack.select().id(1357).count() > 0) {
				if (bestAxeId == 1355) {
					hasExtraAxe = true;
				}
				bestAxeId = 1357;
			}
		} else if (currentWoodcuttingLevel >= 21) {
			if (ctx.backpack.select().id(1355).count() > 0) {
				bestAxeId = 1355;
			}
		}
		if (bestAxeId != 0) {
			if (ctx.backpack.select().id(bestAxeId).count() > 1
					|| ((!ctx.backpack.select().id(bestAxeId).isEmpty()) && returnToolbeltToItemId() >= bestAxeId)) {
				hasExtraAxe = true;
			}
		}
		return hasExtraAxe;
	}

	private void equipBetterAxe() {
		if (!ctx.backpack.select().id(getBetterAxeId()).isEmpty()) {
			if (ctx.equipment.select().id(getBetterAxeId()).isEmpty()) {
				if (hasRequiredAttackLevel()) {
					for (Item i : ctx.backpack.shuffle()) {
						i.interact("Wield");
					}
				}
			}
		}
	}

	private void addBetterAxeToToolbelt() {
		if (ctx.backpack.select().id(getBetterAxeId()).count() > 0) {
			if (returnToolbeltToItemId() != getBetterAxeId()) {
				ctx.backpack.select().id(getBetterAxeId()).poll()
						.interact("Add to tool belt");
			}
		}
	}

	private void setRandomInventInt(int randomInt) {
		randomInventInt = randomInt;
	}

	private int getRandomInventInt() {
		return randomInventInt;
	}

	private int getAssignmentItemId() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			return 0;
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			return 1515;
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			return 1517;
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return 1519;
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return 1521;
		} else {
			return 1511;
		}
	}

	private double getExperiencePerLog() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			return 332.5;
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			return 175;
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			return 100;
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return 67.5;
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return 37.5;
		} else {
			return 25;
		}
	}

	private int getBetterAxeId() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= 61) {
			return 1359;
		} else if (woodcuttingLevel >= 41) {
			return 1359;
		} else if (woodcuttingLevel >= 31) {
			return 1357;
		} else if (woodcuttingLevel >= 21) {
			return 1355;
		} else if (woodcuttingLevel >= 6) {
			return 1353;
		}
		return 0;
	}

	private int[] extraItemIds() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			return new int[] { 1511, 1519, 1521 };
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			return new int[] { 1511, 1519, 1521 };
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			return new int[] { 1511, 1519, 1521 };
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return new int[] { 1511, 1521 };
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return new int[] { 1511 };
		} else {
			return new int[] { 1519, 1521 };
		}
	}

	private int[] otherLogIds() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			return new int[] { 1511, 1515, 1517, 1519, 1521 };
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			return new int[] { 1511, 1515, 1517, 1519, 1521 };
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			return new int[] { 1511, 1519, 1521 };
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return new int[] { 1511, 1515, 1517, 1521 };
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return new int[] { 1511, 1515, 1517, 1521 };
		} else {
			return new int[] { 1515, 1517, 1519, 1521 };
		}
	}

	private int[] allLogIds() {
		return new int[] { 1511, 1515, 1517, 1519, 1521 };
	}

	private boolean hasLogsToBank() {
		return ctx.backpack.select().id(otherLogIds()).count() > 0;
	}

	private int[] getAssignmentTreeIds() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			return new int[] { 46318, 46322, 46324, 86561, 86562, 86563, 86564 };
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			return new int[] { 38755 };
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			return new int[] { 51843, 51845 };
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return new int[] { 38616, 38627, };
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return new int[] { 38731, 38732 };
		} else {
			return new int[] { 38760, 38762, 38782, 38783, 38784, 38786, 38788 };
		}
	}

	private int[] getAssignmentBankerNpcIds() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			return new int[] { 3418, 2718 };
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			return new int[] { 3418, 2718 };
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			return new int[] { 495 };
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return new int[] { 4456, 4457, 4458, 4459 };// TODO
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return new int[] { 553, 2759 };
		} else {
			return new int[] { 0 };
		}
	}

	private String getAssignmentString() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			return "Ivy";
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			return "Yew";
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			return "Maple";
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return "Willow";
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return "Oak";
		} else {
			return "Regular";
		}
	}

	private String getAssignmentTreeString() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			return "Ivy";
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			return "Yew";
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			return "Maple Tree";
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return "Willow";
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return "Oak";
		} else {
			return "Tree";
		}
	}

	private String getAssignmentLocation() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			return ivyLocation;
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			return yewLocation;
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			return mapleLocation;
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return willowLocation;
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return oakLocation;
		} else {
			return "Port Sarim";
		}
	}

	private String getAssignmentDropAction() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= yewLevel && chopYews) {
			if (hasRequiredFiremakingLevel())
				return yewDropAction;
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			if (hasRequiredFiremakingLevel())
				return mapleDropAction;
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			if (hasRequiredFiremakingLevel())
				return willowDropAction;
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			if (hasRequiredFiremakingLevel())
				return oakDropAction;
		} else {
			if (hasRequiredFiremakingLevel())
				return regDropAction;
		}
		return "Drop";
	}

	private Area getAssignmentArea() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			if (ivyLocation.equalsIgnoreCase("Varrock (N)")) {
				return new Area(new Tile(3201, 3505), new Tile(3225, 3498));
			} else {
				if (ivyLocation.equalsIgnoreCase("Falador (SE)")) {
					return new Area(new Tile(3042, 3328), new Tile(3054, 3320));
				} else {
					return new Area(new Tile(3201, 3505), new Tile(3225, 3498));
				}
			}
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			if (!yewLocation.equalsIgnoreCase("Varrock")) {
				return new Area(new Tile(5, 6), new Tile(7, 8));
			} else {
				return new Area(new Tile(3201, 3505), new Tile(3225, 3498));
			}
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			if (mapleLocation.endsWith("Seers' Village (Bank)")) {
				return new Area(new Tile(2718, 3504), new Tile(2736, 3494));
			} else {
				return new Area(new Tile(2669, 3546), new Tile(2688, 3520));
			}
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			if (!willowLocation.equalsIgnoreCase("Draynor Village")) {
				return new Area(new Tile(3052, 3268), new Tile(3067, 3246));
			} else {
				return new Area(new Tile(3080, 3239), new Tile(3092, 3226));
			}
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			if (oakLocation.equalsIgnoreCase("Port Sarim")) {
				return new Area(new Tile(3028, 3274), new Tile(3063, 3258));
			} else if (oakLocation.equalsIgnoreCase("Varrock (W)")) {
				return new Area(new Tile(3160, 3424), new Tile(3173, 3410));
			} else {
				return new Area(new Tile(3028, 3274), new Tile(3063, 3258));// Default
																			// PS
			}
		} else {
			return new Area(new Tile(2975, 3233), new Tile(3009, 3191));
		}
	}

	private Area getAssignmentBankArea() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			if (ivyLocation.equalsIgnoreCase("Varrock (N)")) {
				return new Area(new Tile(3176, 3482), new Tile(3184, 3475));
			} else {
				if (ivyLocation.equalsIgnoreCase("Falador (SE)")) {
					return new Area(new Tile(3009, 3358), new Tile(3023, 3352));
				} else {
					return new Area(new Tile(3176, 3482), new Tile(3184, 3475));
				}
			}
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			if (yewLocation.equalsIgnoreCase("Varrock")) {
				return new Area(new Tile(3176, 3482), new Tile(3184, 3475));
			} else {
				return new Area(new Tile(5, 6), new Tile(7, 8));
			}
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			if (mapleLocation.equalsIgnoreCase("Seers' Village (Bank)")) {
				return new Area(new Tile(2720, 3494), new Tile(2731, 3494),
						new Tile(2731, 3489), new Tile(2728, 3489), new Tile(
								2728, 3486), new Tile(2723, 3486), new Tile(
								2723, 3489), new Tile(2720, 3489));
			} else {
				return new Area(new Tile(2720, 3494), new Tile(2731, 3494),
						new Tile(2731, 3489), new Tile(2728, 3489), new Tile(
								2728, 3486), new Tile(2723, 3486), new Tile(
								2723, 3489), new Tile(2720, 3489));
			}
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			if (willowLocation.equalsIgnoreCase("Draynor Village")) {
				return new Area(new Tile(3088, 3246), new Tile(3097, 3240));
			} else {
				return new Area(new Tile(3043, 3237), new Tile(3050, 3234));
			}
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			if (oakLocation.equalsIgnoreCase("Port Sarim")) {
				return new Area(new Tile(3043, 3237), new Tile(3050, 3234));
			} else if (oakLocation.equalsIgnoreCase("Varrock (W)")) {
				return new Area(new Tile(3182, 3446), new Tile(3190, 3432));
			} else {
				return new Area(new Tile(3043, 3237), new Tile(3050, 3234));
			}
		} else {
			return new Area(new Tile(3043, 3237), new Tile(3050, 3234));
		}
	}

	private boolean hasRequiredAttackLevel() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		int strLevel = ctx.skills.level(Constants.SKILLS_STRENGTH);
		if (woodcuttingLevel >= 41) {
			if (strLevel >= 50)
				return true;
		} else if (woodcuttingLevel >= 31) {
			if (strLevel >= 40)
				return true;
		} else if (woodcuttingLevel >= 21) {
			if (strLevel >= 30)
				return true;
		} else if (woodcuttingLevel >= 6) {
			if (strLevel >= 20)
				return true;
		}
		return false;
	}

	private boolean hasRequiredFiremakingLevel() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		int fmLevel = ctx.skills.level(Constants.SKILLS_FIREMAKING);
		if (woodcuttingLevel >= yewLevel && chopYews) {
			if (fmLevel >= 60)
				return true;
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			if (fmLevel >= 45)
				return true;
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			if (fmLevel >= 30)
				return true;
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			if (fmLevel >= 15)
				return true;
		} else if (getAssignmentString().equalsIgnoreCase("Regular")
				&& fmLevel >= 1) {
			return true;
		}
		return false;

	}

	private boolean atTreeArea() {
		return getAssignmentArea().contains(ctx.players.local());
	}

	private boolean atBankArea() {
		return getAssignmentBankArea().contains(ctx.players.local());
	}

	private boolean inCombat() {
		return ctx.players.local().inCombat();
	}

	private boolean bankLogs() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= ivyLevel && chopIvy) {
			if (!dropNests || !dropRocks)
				return true;
		} else if (woodcuttingLevel >= yewLevel && chopYews) {
			if (bankYews)
				return true;
		} else if (woodcuttingLevel >= mapleLevel && chopMaples) {
			if (bankMaples)
				return true;
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			if (bankWillow)
				return true;
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			if (bankOak)
				return true;
		} else {
			if (bankRegular)
				return true;
		}
		return false;
	}

	private boolean hasExtraItems() {
		for (int i = 0; i < extraItemIds().length; i++) {
			if (ctx.backpack.select().id(extraItemIds()[i]).count() > 0) {
				return true;
			}
		}
		return false;
	}

	private boolean inOverallPortSarimArea() {
		return new Area(new Tile(2908, 3313), new Tile(3069, 3158))
				.contains(ctx.players.local());
	}

	private boolean inOverallDraynorArea() {
		return new Area(new Tile(3070, 3321), new Tile(3102, 3224))
				.contains(ctx.players.local());
	}

	private boolean inOverallVarrockArea() {
		return new Area(new Tile(3135, 3517, 0), new Tile(3194, 3517, 0),
				new Tile(3199, 3511, 0), new Tile(3231, 3511, 0), new Tile(
						3236, 3505, 0), new Tile(3256, 3505, 0), new Tile(3259,
						3366, 0), new Tile(3137, 3367, 0)).contains(ctx.players
				.local());
	}

	private boolean inVarrockYewArea() {
		return new Area(new Tile(3170, 3510), new Tile(3231, 3472))
				.contains(ctx.players.local());
	}

	private boolean inOverallSeersVillage() {
		return new Area(new Tile(2632, 3547), new Tile(2782, 3547), new Tile(
				2783, 3456), new Tile(2632, 3456))
				.contains(ctx.players.local());
	}

	private boolean comingFromDraynorLodestone() {
		return new Area(new Tile(3100, 3299), new Tile(3110, 3233))
				.contains(ctx.players.local());
	}

	private boolean inOverallFaladorArea() {
		return new Area(new Tile(2936, 3411, 0), new Tile(3007, 3411, 0),
				new Tile(3027, 3395, 0), new Tile(3042, 3391, 0), new Tile(
						3067, 3390, 0), new Tile(3067, 3379, 0), new Tile(3067,
						3366, 0), new Tile(3060, 3360, 0), new Tile(3061, 3350,
						0), new Tile(3065, 3349, 0), new Tile(3066, 3332, 0),
				new Tile(3067, 3328, 0), new Tile(3065, 3325, 0), new Tile(
						3061, 3327, 0), new Tile(3025, 3327, 0), new Tile(3022,
						3326, 0), new Tile(3016, 3325, 0), new Tile(3011, 3321,
						0), new Tile(3007, 3321, 0), new Tile(2985, 3306, 0),
				new Tile(2937, 3306, 0)).contains(ctx.players.local());
	}

	private boolean hasDestination() {
		if (ctx.movement.destination().x() == -1
				|| ctx.movement.destination().y() == -1) {
			return false;
		} else {
			return true;
		}
	}

	private boolean facingDestination() {
		if (!hasDestination())
			return true;
		if (hasDestination() && facingTile(ctx.movement.destination()))
			return true;
		return false;
	}

	private boolean facingTile(Tile tile) {
		return tile.matrix(ctx).inViewport();
	}

	private boolean turnTo(Locatable l) {
		final Tile ltile = l.tile();
		if (facingTile(ltile)) {
			return true;
		} else {
			ctx.camera.turnTo(l);
			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() {
					return facingTile(ltile); // condition is met. for example,
												// the backpack turns empty
				}
			}, 2000, 5);
			if (facingTile(ltile))
				return true;

		}
		return false;
	}

	private boolean combineCamera(final int angle, final int pitch) {
		final Runnable setAngle = new Runnable() {
			@Override
			public void run() {
				ctx.camera.angle(angle);
			}
		};
		final Runnable setPitch = new Runnable() {
			@Override
			public void run() {
				ctx.camera.pitch(pitch);
			}
		};

		if (Random.nextInt(0, 100) < 50) {
			new Thread(setAngle).start();
			new Thread(setPitch).start();
		} else {
			new Thread(setPitch).start();
			new Thread(setAngle).start();
		}

		return Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return ctx.camera.pitch() == pitch && ctx.camera.yaw() == angle;
			}
		}, Random.nextInt(50, 200), Random.nextInt(4, 8));
	}

	private boolean tileHasFire() {
		final Tile ptile = ctx.players.local().tile();
		final GameObject obj = ctx.objects.select().id(70757, 70761).nearest().poll();
		if (ctx.movement.distance(obj) == 0) {
			return true;
		} else {
			return false;
		}
	}

	public void setZoomLevel(int zoomLevel) {
		for (int i = 0; i < 10; i++) {
			ctx.input.send("{VK_PAGE_UP}");
		}
		for (int i = 0; i < zoomLevel; i++) {
			ctx.input.send("{VK_PAGE_DOWN}");
		}
		zoomLevelSet = true;
	}

	private int getZoomLevel() {
		if (getAssignmentString().equalsIgnoreCase("Regular")) {
			return 10;
		} else if (getAssignmentString().equalsIgnoreCase("Oak")) {
			return 6;
		} else if (getAssignmentString().equalsIgnoreCase("Willow")) {
			return 8;
		} else if (getAssignmentString().equalsIgnoreCase("Maple")) {
			return 8;
		} else if (getAssignmentString().equalsIgnoreCase("Yew")) {
			return 7;
		} else if (getAssignmentString().equalsIgnoreCase("Ivy")) {
			return 4;
		} else {
			return 5;
		}
	}

}
