package xXTheAwesomerXx.dbscripts.rs3.woodcutting;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.concurrent.Callable;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.Random;
import org.powerbot.script.Script;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Action;
import org.powerbot.script.rt6.Bank.Amount;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.Constants;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.Item;
import org.powerbot.script.rt6.Widget;

@Script.Manifest(name = "DBWoodcutter", description = "Progressive Woodcutter made by xXTheAwesomerXx", properties = "author=xXTheAwesomerXx; topic=1296051;")
public class DBWoodcutter extends PollingScript<ClientContext> implements
		MessageListener, PaintListener {

	private enum State {
		RUN, CHOP, BANK, DROP, DO_TRADE
	};

	private final Trade trade = new Trade(ctx);
	public static boolean bankRegular = false, bankOak = false,
			bankWillow = true, bankYews = true, chopOaks = false,
			chopWillows = false, chopYews = false;
	public static int oakLevel = 15, willowLevel = 30, yewLevel = 60;
	private int randomInventInt = 25;

	public static boolean getNotes = false, doTrade = false,
			allowTrade = false, guiShowing = false, addToToolbelt = true;

	public static String statusMessage = "Starting...", muleName = "",
			oakLocation = "Port Sarim", willowLocation = "Draynor",
			yewLocation = "Varrock";
	public static long START_TIME = System.currentTimeMillis(),
			CHOP_TIME = System.currentTimeMillis(), TASK_TIME = System
					.currentTimeMillis();
	private long totalRuntime, chopRuntime, taskRuntime;

	private final long START_EXPERIENCE = ctx.skills
			.experience(Constants.SKILLS_WOODCUTTING);
	private long TASK_START_EXPERIENCE = ctx.skills
			.experience(Constants.SKILLS_WOODCUTTING);
	private long expGained, taskExpGained;
	private long expHr, taskExpHr;
	private long expToLvl;
	private int taskChopped = 0, logsChopped = 0,
			currentWoodcuttingLevel = ctx.skills
					.level(Constants.SKILLS_WOODCUTTING);

	private State getState() {
		if (!doTrade) {
			if (!getNotes) {
				if (!hasExtraItems()) {
					if (ctx.backpack.select().count() <= getRandomInventInt()) {
						if (atTreeArea()) {
							statusMessage = "Chopping...";
							return State.CHOP;
						} else {
							statusMessage = "Running to Resources";
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
	}

	@Override
	public void repaint(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
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
		g.setColor(new Color(0, 0, 0, 70));
		g.fillRect(5, 35, 523, 154);
		g.setColor(new Color(204, 0, 0));
		g.drawString("DBWoodcutter: " + statusMessage, 10, 50);
		g.drawString("Total Runtime: " + formatTime(totalRuntime)
				+ " | Task Time: " + formatTime(taskRuntime), 10, 65);
		g.drawString("Last Chop: " + chopRuntime + "ms", 10, 80);
		g.drawString("Time to Lvl "
				+ (currentWoodcuttingLevel != 99 ? currentWoodcuttingLevel + 1
						: "X") + ": "
				+ formatTime((expHr <= 0 ? 0 : (expToLvl * 3600000) / expHr)),
				10, 95);
		g.drawString("Chopping Task: " + getAssignmentString()
				+ ", Banking Enabled: " + bankLogs(), 10, 110);
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
			if (m.text().contains("You get some")) {
				logsChopped++;
				taskChopped++;
				CHOP_TIME = System.currentTimeMillis();
			} else if ((m.text().contains("15") || m.text().contains("30") || m
					.text().contains("60")) && m.text().contains("advanced")) {
				taskChopped = 0;
				TASK_TIME = System.currentTimeMillis();
				TASK_START_EXPERIENCE = ctx.skills
						.experience(Constants.SKILLS_WOODCUTTING);
			} else if (m.text().contains("Accepted")
					|| m.text().contains("You can only give away")) {
				doTrade = false;
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
	public void poll() {
		switch (getState()) {
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
										.id(getAssignmentItemId() + 1).poll()
										.component().index();
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
			if (ctx.bank.opened()) {
				ctx.bank.close();
			}
			if (getAssignmentString().equalsIgnoreCase("Willow")
					&& willowLocation.equalsIgnoreCase("Draynor Village")) {
				if (ctx.widgets.widget(1184).component(11).text()
						.equalsIgnoreCase("Guard in tree")) {
					ctx.movement.step(ctx.players.local().tile().derive(2, 2));
					ctx.camera.angle('n');
				}
			}
			if (ctx.camera.pitch() > 55 || ctx.camera.pitch() < 15) {
				ctx.camera.pitch(Random.nextInt(15, 55));
			}
			if (addToToolbelt) {
				addBetterAxeToToolbelt();
			} else {
				equipBetterAxe();
			}
			final GameObject obj = ctx.objects.select()
					.id(getAssignmentTreeIds()).nearest().poll();
			if (obj.valid()) {
				if (obj.inViewport()) {
					if (ctx.players.local().animation() != -1) {
						if (bankLogs() == false) {
							final int randomInt = Random.nextInt(5, 15);
							if (ctx.backpack.select().count() > (getRandomInventInt() - randomInt)) {
								while (ctx.players.local().animation() == 21191) {
									Point pointOfRandomWillow = ctx.backpack
											.id(getAssignmentItemId())
											.shuffle().poll().nextPoint();
									if (ctx.chat.queryContinue()) {
										ctx.chat.clickContinue(true);
									} else {
										if (!ctx.combatBar.select()
												.id(getAssignmentItemId())
												.isEmpty()) {
											Action dropLogs = ctx.combatBar
													.first().poll();
											ctx.combatBar
													.actionAt(dropLogs.slot())
													.component()
													.interact("Drop");
										} else {
											ctx.input.move(pointOfRandomWillow);
											ctx.input.drag(ctx.combatBar
													.first().poll().component()
													.nextPoint(), true);
										}
									}
								}
								setRandomInventInt(Random.nextInt(20, 25));
							} else {
								final GameObject obj2 = ctx.objects.select()
										.id(getAssignmentTreeIds()).shuffle()
										.poll();
								if (obj2.valid()) {
									if (ctx.players.local().animation() != -1) {
										if (obj2.inViewport()) {
											obj2.hover();
										} else {
											ctx.camera.turnTo(obj2);
										}
									}
								}
							}
						} else {
							final GameObject obj2 = ctx.objects.select()
									.id(getAssignmentTreeIds()).shuffle()
									.poll();
							if (obj2.valid()) {
								if (ctx.players.local().animation() != -1) {
									if (obj2.inViewport()) {
										obj2.hover();
									} else {
										ctx.camera.turnTo(obj2);
									}
								}
							}
						}
					} else {
						obj.interact(true, "Chop down",
								getAssignmentTreeString());
					}
				} else {
					ctx.camera.turnTo(obj);
					obj.hover();
				}
			} else {
				final GameObject obj2 = ctx.objects.select()
						.name(getAssignmentTreeString()).nearest().poll();
				if (obj2.valid()) {
					if (ctx.players.local().animation() == -1) {
						obj2.interact(true, "Chop down",
								getAssignmentTreeString());
					}
				}
			}
			break;
		case DROP:
			if (!hasExtraItems()) {
				setRandomInventInt(Random.nextInt(20, 25));
				while (ctx.backpack.select().id(getAssignmentItemId()).count() > 0) {
					Point pointOfRandomWillow = ctx.backpack
							.id(getAssignmentItemId()).shuffle().poll()
							.nextPoint();
					if (ctx.chat.queryContinue()) {
						ctx.chat.clickContinue(true);
					} else {
						if (!ctx.combatBar.select().id(getAssignmentItemId())
								.isEmpty()) {
							Action dropLogs = ctx.combatBar.first().poll();
							ctx.combatBar.actionAt(dropLogs.slot()).component()
									.interact("Drop");
						} else {
							ctx.input.move(pointOfRandomWillow);
							ctx.input.drag(ctx.combatBar.first().poll()
									.component().centerPoint(), true);
						}
					}
				}
			} else {
				statusMessage = "Has extra logs, dropping...";
				while (hasExtraItems()) {
					for (Item i : ctx.backpack.select().id(extraItemIds())) {
						if (ctx.chat.queryContinue()) {
							ctx.chat.clickContinue(true);
						} else {
							i.interact("Drop");
						}
					}
				}
			}
			break;
		case BANK:
			if (getAssignmentString().equalsIgnoreCase("Regular")
					|| getAssignmentString().equalsIgnoreCase("Oak")
					|| (getAssignmentString().equalsIgnoreCase("Willow") && willowLocation
							.equalsIgnoreCase("Port Sarim"))) {
				final Widget bankWidget = ctx.widgets.widget(11);
				if (bankWidget.valid()) {
					if (ctx.backpack.select().id(getAssignmentItemId()).count() > 0) {
						int assignmentItemIndex = ctx.backpack.poll()
								.component().index();
						final Component component = bankWidget.component(1)
								.component(assignmentItemIndex);
						if (component.itemId() == (getAssignmentItemId())) {
							component.interact("Deposit-All");
						}
					}
				} else {
					final GameObject depositBox = ctx.objects.select()
							.id(36788).nearest().poll();
					if (depositBox.valid()) {
						if (depositBox.inViewport()) {
							depositBox.interact("Deposit");
						} else {
							ctx.camera.turnTo(depositBox);
						}
					}
				}
			} else {
				if (!ctx.bank.opened()) {
					if (ctx.bank.inViewport()) {
						ctx.bank.open();
					} else {
						ctx.camera.turnTo(ctx.bank.nearest());
					}
				} else {
					if (!getNotes) {
						while (ctx.backpack.select().id(getAssignmentItemId())
								.count() > 0) {

							ctx.bank.deposit(getAssignmentItemId(), Amount.ALL);
						}

						withdrawBestUsableAxe();
						while (hasUnusableAxe()) {
							depositUnusableAxe();
						}
						while (hasExtraAxeInInventory()) {
							depositExtraAxeInInventory();
						}
					} else {
						if (ctx.backpack.select().count() < 28) {
							if (ctx.bank.select().id(getAssignmentItemId())
									.count() > 0) {
								if (!ctx.bank.withdrawMode()) {
									ctx.bank.withdrawMode(true);
								} else {
									ctx.bank.withdraw(getAssignmentItemId(),
											Amount.ALL);
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
			ctx.camera.angle('n');
			if ((getAssignmentString().equalsIgnoreCase("Willow") || getAssignmentString()
					.equalsIgnoreCase("Yew")) && getNotes) {
				Tile pathToBank = ctx.movement.findPath(
						getAssignmentBankArea().getCentralTile()).next();
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
					}
				}
			} else {
				if (ctx.backpack.select().count() <= getRandomInventInt()) {
					Tile pathToTreeArea = null;
					if (getAssignmentString().equalsIgnoreCase("Willow")) {
						if (!willowLocation.equalsIgnoreCase("Draynor Village")) {
							pathToTreeArea = ctx.movement.findPath(
									new Tile(2986, 3189)).next();
						} else {
							if (!inOverallPortSarimArea()) {
								pathToTreeArea = ctx.movement.findPath(
										new Tile(3083, 3236)).next();
								if (!Double.isInfinite(pathToTreeArea
										.distanceTo(ctx.players.local()))) {
									// TODO: Figure out what to do here
									// Maybe stop?
								} else {
									if (comingFromDraynorLodestone())
										pathToTreeArea = ctx.movement
												.newTilePath(
														Paths.draynorLodestoneToWillows)
												.next();
								}
							} else {
								pathToTreeArea = ctx.movement.newTilePath(
										Paths.sarimToDraynorPath).next();
							}
						}
					} else if (getAssignmentString().equalsIgnoreCase("Yew")) {
						pathToTreeArea = ctx.movement
								.newTilePath(Paths.overallPathToVarrockYews)
								.randomize(1, 4).next();
					} else if (getAssignmentString()
							.equalsIgnoreCase("Regular")
							|| getAssignmentString().equalsIgnoreCase("Oak")) {
						pathToTreeArea = ctx.movement.newTilePath(
								ctx.movement
										.newTilePath(Paths.pathToPortSarimBank)
										.reverse().array()).next();
					} else {
						pathToTreeArea = ctx.movement.findPath(
								getAssignmentArea().getCentralTile()).next();
					}
					if (pathToTreeArea != null && pathToTreeArea.x() != -1) {
						ctx.movement.step(pathToTreeArea);
					} else {
						teleportToKnownLocation();
					}
				} else {
					Tile pathToBank = ctx.movement.findPath(
							getAssignmentBankArea().getCentralTile()).next();
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
						}
					}
				}
			}
			break;
		default:
			break;
		}
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
							|| (!ctx.backpack.select().id(item.id()).isEmpty() && returnToolbeltToItemId() == bestAxeId)) {
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
					|| (!ctx.backpack.select().id(bestAxeId).isEmpty() && returnToolbeltToItemId() == bestAxeId)) {
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
		if (woodcuttingLevel >= yewLevel && chopYews) {
			return 1515;
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
		if (woodcuttingLevel >= yewLevel && chopYews) {
			return 175;
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
		if (woodcuttingLevel >= yewLevel && chopYews) {
			return new int[] { 1519, 1511, 1521 };
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return new int[] { 1511, 1521 };
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return new int[] { 1511 };
		} else {
			return new int[] { 1519, 1521 };
		}
	}

	private int[] getAssignmentTreeIds() {
		int woodcuttingLevel = ctx.skills.level(Constants.SKILLS_WOODCUTTING);
		if (woodcuttingLevel >= yewLevel && chopYews) {
			return new int[] { 38755 };
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return new int[] { 38616, 38627, };
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return new int[] { 38731, 38732 };
		} else {
			return new int[] { 38760, 38762, 38782, 38783, 38784, 38786, 38788 };
		}
	}

	private String getAssignmentString() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= yewLevel && chopYews) {
			return "Yew";
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
		if (woodcuttingLevel >= yewLevel && chopYews) {
			return "Yew";
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			return "Willow";
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return "Oak";
		} else {
			return "Tree";
		}
	}

	private Area getAssignmentArea() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= yewLevel && chopYews) {
			if (!yewLocation.equalsIgnoreCase("Varrock")) {
				return new Area(new Tile(5, 6), new Tile(7, 8));
			} else {
				return new Area(new Tile(3201, 3505), new Tile(3225, 3498));
			}
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			if (!willowLocation.equalsIgnoreCase("Draynor Village")) {
				return new Area(new Tile(2970, 3200), new Tile(2997, 3180));
			} else {
				return new Area(new Tile(3080, 3239), new Tile(3092, 3226));
			}
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return new Area(new Tile(2975, 3220), new Tile(2995, 3200));
		} else {
			return new Area(new Tile(2975, 3233), new Tile(3009, 3191));
		}
	}

	private Area getAssignmentBankArea() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= yewLevel && chopYews) {
			if (yewLocation.equalsIgnoreCase("Varrock")) {
				return new Area(new Tile(3176, 3482), new Tile(3184, 3475));
			} else {
				return new Area(new Tile(5, 6), new Tile(7, 8));
			}
		} else if (woodcuttingLevel >= willowLevel && chopWillows) {
			if (willowLocation.equalsIgnoreCase("Draynor Village")) {
				return new Area(new Tile(3088, 3246), new Tile(3097, 3240));
			} else {
				return new Area(new Tile(3043, 3237), new Tile(3050, 3234));
			}
		} else if (woodcuttingLevel >= oakLevel && chopOaks) {
			return new Area(new Tile(3043, 3237), new Tile(3050, 3234));
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

	private boolean atTreeArea() {
		return getAssignmentArea().contains(ctx.players.local());
	}

	private boolean atBankArea() {
		return getAssignmentBankArea().contains(ctx.players.local());
	}

	private boolean bankLogs() {
		int woodcuttingLevel = currentWoodcuttingLevel;
		if (woodcuttingLevel >= yewLevel && chopYews) {
			if (bankYews)
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

	private boolean inVarrockYewArea() {
		return new Area(new Tile(3170, 3510), new Tile(3231, 3472))
				.contains(ctx.players.local());
	}

	private boolean comingFromDraynorLodestone() {
		return new Area(new Tile(3100, 3299), new Tile(3110, 3233))
				.contains(ctx.players.local());
	}

}
