package xXTheAwesomerXx.dbscripts.rs3.woodcutting.tasks;

import java.util.concurrent.TimeUnit;

import org.powerbot.script.Area;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;
import org.powerbot.script.rt6.TilePath;

import xXTheAwesomerXx.dbscripts.rs3.woodcutting.cbfmultitasker.CBFMultitasker;
import xXTheAwesomerXx.dbscripts.rs3.woodcutting.wrapper.Task;

public class ChopFletchTask extends Task<ClientContext>
		implements
			MessageListener {

	private enum State {
		BANK, CHOP, DROP_EXTRA, FLETCH, RUN_AWAY, RUN_TO_BANK, RUN_TO_TREE, TELEPORT
	}

	@SuppressWarnings("unused")
	private boolean bankStartingInventory = false, runAway = false,
	started = false, fletching = false;
	private int[] extraLogIds;
	public int inventoryLogId;
	private Area overallTaskArea, taskTreeArea, taskBankArea;
	private TilePath treesToBankPath, lodestoneToBankPath,
	lodestoneToTreesPath, bankToTreesPath;

	public ChopFletchTask(final ClientContext ctx, final int taskPosition,
			final String treeSelect, final String locSelect,
			final String optSelect, final String condSelect,
			final int condAmount, final boolean stop) {
		super(ctx);
		setQueuePosition(taskPosition);
		setTreeSelection(treeSelect);
		setLocSelection(locSelect);
		setTypeSelection("Chop | Fletch");
		setOptionSelection(optSelect);
		setConditionType(condSelect);
		setConditionAmount(condAmount);
		setShouldStop(stop);
	}

	@Override
	public boolean activate() {
		return (CBFMultitasker.taskList.get(0).equals(this));
	}

	@Override
	public void execute() {
		if (!started) {
			player = ctx.players.local();
			CBFMultitasker.taskStartWCExp = ctx.skills
					.experience(Constants.SKILLS_WOODCUTTING);
			CBFMultitasker.taskStartFMExp = ctx.skills
					.experience(Constants.SKILLS_FIREMAKING);
			CBFMultitasker.taskStartFLExp = ctx.skills
					.experience(Constants.SKILLS_FLETCHING);
			CBFMultitasker.taskStartWCLvl = ctx.skills
					.level(Constants.SKILLS_WOODCUTTING);
			CBFMultitasker.taskStartFMLvl = ctx.skills
					.level(Constants.SKILLS_FIREMAKING);
			CBFMultitasker.taskStartFLLvl = ctx.skills
					.level(Constants.SKILLS_FLETCHING);
			CBFMultitasker.taskStartTime = System.nanoTime();
			CBFMultitasker.treeSelection = getTreeSelection();
			CBFMultitasker.locSelection = getLocSelection();
			CBFMultitasker.typeSelection = getTypeSelection();
			CBFMultitasker.optionSelection = getOptionSelection();
			CBFMultitasker.queuePosition = getQueuePosition();
			CBFMultitasker.conditionType = getConditionType();
			CBFMultitasker.conditionAmount = getConditionAmount();
			CBFMultitasker.shouldStopTask = shouldStop();
			CBFMultitasker.taskCollectedLogs = 0;
			CBFMultitasker.taskBurnedLogs = 0;
			CBFMultitasker.taskCraftedLogs = 0;
			setOverallTaskArea(m.getOverallTaskArea(getLocSelection()));
			setTaskTreeArea(m.getTaskTreeArea(getTreeSelection(),
					getLocSelection()));
			setTaskBankArea(m.getTaskBankArea(getTreeSelection(),
					getLocSelection()));
			setTreesToBankPath(m.getTreesToBankPath(getTreeSelection(),
					getLocSelection()));
			setBankToTreesPath(m.getBankToTreesPath(getTreeSelection(),
					getLocSelection()));
			setLodestoneToBankPath(m.getLodestoneToBankPath(getTreeSelection(),
					getLocSelection()));
			setLodestoneToTreesPath(m.getLodestoneToTreesPath(
					getTreeSelection(), getLocSelection()));
			setInventoryLogId(m.getTaskItemId(getTreeSelection()));
			setExtraLogIds(m.extraItemIds(getTreeSelection()));
			ctx.dispatcher.add(this);
			if (m.hasExtraItems(getTreeSelection())
					|| m.hasExtraFletchItems(getTreeSelection(),
							optionSelection)) {
				bankStartingInventory = true;
			}
			if (m.setZoomLevel(Random.nextInt(
					m.getZoomLevel(getTreeSelection()) - 1,
					m.getZoomLevel(getTreeSelection()) + 1))) {
				started = true;
			}
			m.runAway(ctx.players.local().tile());
		}
		switch (getState()) {
			case RUN_TO_TREE :
				CBFMultitasker.statusMessage = "Running to Tree Area";
				m.runToTreeArea(getLocSelection(), getTaskTreeArea(),
						getBankToTreesPath(), getLodestoneToTreesPath());
				break;
			case RUN_TO_BANK :
				CBFMultitasker.statusMessage = "Running to Bank Area";
				m.runToBank(getLocSelection(), getTaskBankArea(),
						getTreesToBankPath(), getLodestoneToBankPath());
				break;
			case CHOP :
				CBFMultitasker.statusMessage = "Chopping";
				if (objectiveReached()) {
					ctx.dispatcher.remove(this);
					CBFMultitasker.removeLeadingTask();
				}
				m.chop(getTreeSelection(), getLocSelection(), getTaskTreeArea());
				break;
			case BANK :
				CBFMultitasker.statusMessage = "Banking";
				if (bankStartingInventory) {
					bankStartingInventory = !m.bankItems(getLocSelection(),
							m.concat(getExtraLogIds(), m.allUnstrungBowIds()));
				} else {
					if (ctx.backpack.select().count() == 28) {
						m.bankItems(getLocSelection(), m.allUnstrungBowIds());
					}
				}
				break;
			case DROP_EXTRA :
				CBFMultitasker.statusMessage = "Dropping Extras";
				m.dropExtraItemIds(getTreeSelection());
				break;
			case TELEPORT :
				CBFMultitasker.statusMessage = "Teleporting";
				m.teleportToKnownLocation(getLocSelection());
				break;
			case RUN_AWAY :
				CBFMultitasker.statusMessage = "Running Away";
				final Tile playerTile = player.tile().derive(
						Random.nextInt(-10, 10), Random.nextInt(-10, 10));
				if (!player.inMotion()) {
					setRunAway(!m.runAway(playerTile));
				}
				break;
			case FLETCH :
				CBFMultitasker.statusMessage = "Fletching";
				fletching = m.fletchLogs(getTreeSelection(),
						getOptionSelection());
				break;
			default :
				break;
		}
	}

	private final TilePath getBankToTreesPath() {
		return bankToTreesPath;
	}

	public int[] getExtraLogIds() {
		return extraLogIds;
	}

	private final int getInventoryLogId() {
		return inventoryLogId;
	}

	private final TilePath getLodestoneToBankPath() {
		return lodestoneToBankPath;
	}

	private final TilePath getLodestoneToTreesPath() {
		return lodestoneToTreesPath;
	}

	private final Area getOverallTaskArea() {
		return overallTaskArea;
	}

	private State getState() {
		if (ctx.players.local().inCombat()) {
			setRunAway(true);
			return State.RUN_AWAY;
		} else {
			if (bankStartingInventory) {
				if (ctx.bank.opened() || getTaskBankArea().contains(player)) {
					return State.BANK;
				} else {
					if (getOverallTaskArea().contains(player)) {
						return State.RUN_TO_BANK;
					} else {
						return State.TELEPORT;
					}
				}
			} else {
				if (m.hasExtraItems(getTreeSelection())) {
					return State.DROP_EXTRA;
				} else {
					if (ctx.backpack.select().count() > 27) {
						if (ctx.backpack.select().id(getInventoryLogId())
								.count() > 0) {
							return State.FLETCH;
						} else {
							if (ctx.backpack.select().count() != 28) {
								if (getTaskTreeArea().contains(player)) {
									return State.CHOP;
								} else {
									if (getOverallTaskArea().contains(player)) {
										return State.RUN_TO_TREE;
									} else {
										return State.TELEPORT;
									}
								}
							} else {
								if (getTaskBankArea().contains(player)
										|| ctx.bank.opened()) {
									return State.BANK;
								} else {
									if (getOverallTaskArea().contains(player)) {
										return State.RUN_TO_BANK;
									} else {
										return State.TELEPORT;
									}
								}
							}
						}
					} else {
						if (getTaskTreeArea().contains(player)) {
							return State.CHOP;
						} else {
							if (getOverallTaskArea().contains(player)) {
								return State.RUN_TO_TREE;
							} else {
								return State.TELEPORT;
							}
						}
					}
				}
			}
		}
	}

	private final Area getTaskBankArea() {
		return taskBankArea;
	}

	private final Area getTaskTreeArea() {
		return taskTreeArea;
	}

	private final TilePath getTreesToBankPath() {
		return treesToBankPath;
	}

	public boolean isRunAway() {
		return runAway;
	}

	@Override
	public void messaged(final MessageEvent m) {
		if (m.source().isEmpty()) {
			if (m.text().contains("inventory is too full")) {
				fletching = true;
			} else if (m.text().contains("You get some")
					|| m.text().contains("You successfully chop away some")) {
				CBFMultitasker.totalCollectedLogs++;
				if (treeSelection.equalsIgnoreCase("Normal")) {
					if (m.text().equalsIgnoreCase("You get some logs.")) {
						CBFMultitasker.taskCollectedLogs++;
					}
				} else {
					if (m.text().contains(treeSelection.toLowerCase())) {
						CBFMultitasker.taskCollectedLogs++;
					}
				}
			} else if (m.text().contains("You carefully cut the")) {
				CBFMultitasker.totalCraftedLogs++;
				if (optionSelection != null) {
					if (optionSelection.equalsIgnoreCase("Arrow Shaft")) {
						if (m.text().contains("arrow")) {
							CBFMultitasker.taskCraftedLogs += Integer.valueOf(m
									.text().substring(32, 34));
						}
					} else {
						if (m.text().contains(treeSelection)
								|| treeSelection.equalsIgnoreCase("Normal")) {
							CBFMultitasker.taskCraftedLogs++;
						}
					}
				}
			}
		}
	}

	private boolean objectiveReached() {
		final String condition = conditionType;
		final long conditionAmount = this.conditionAmount;
		if (shouldStop()) {
			if (condition.equalsIgnoreCase("Woodcutting Lvl")) {
				final long wcLvl = ctx.skills
						.level(Constants.SKILLS_WOODCUTTING);
				if (wcLvl >= conditionAmount) {
					return true;
				}
			} else if (condition.equalsIgnoreCase("Firemaking Lvl")) {
				final long fmLvl = ctx.skills
						.level(Constants.SKILLS_FIREMAKING);
				if (fmLvl >= conditionAmount) {
					return true;
				}
			} else if (condition.equalsIgnoreCase("Fletching Lvl")) {
				final long flLvl = ctx.skills.level(Constants.SKILLS_FLETCHING);
				if (flLvl >= conditionAmount) {
					return true;
				}
			} else if (condition.equalsIgnoreCase("Gained WC Exp")) {
				final long wcExp = ctx.skills
						.experience(Constants.SKILLS_WOODCUTTING)
						- CBFMultitasker.taskStartWCExp;
				if (wcExp >= conditionAmount) {
					return true;
				}
			} else if (condition.equalsIgnoreCase("Gained FM Exp")) {
				final long fmExp = ctx.skills
						.experience(Constants.SKILLS_FIREMAKING)
						- CBFMultitasker.taskStartFMExp;
				if (fmExp >= conditionAmount) {
					return true;
				}
			} else if (condition.equalsIgnoreCase("Gained FL Exp")) {
				final long flExp = ctx.skills
						.experience(Constants.SKILLS_FLETCHING)
						- CBFMultitasker.taskStartFLExp;
				if (flExp >= conditionAmount) {
					return true;
				}
			} else if (condition.equalsIgnoreCase("Collected Amount")) {
				final long collectedAmount = CBFMultitasker.taskCollectedLogs;
				if (collectedAmount >= conditionAmount) {
					return true;
				}
			} else if (condition.equalsIgnoreCase("Burned Amount")) {
				final long burnedAmount = CBFMultitasker.taskBurnedLogs;
				if (burnedAmount >= conditionAmount) {
					return true;
				}
			} else if (condition.equalsIgnoreCase("Fletched Amount")) {
				final long fletchedAmount = CBFMultitasker.taskCraftedLogs;
				if (fletchedAmount >= conditionAmount) {
					return true;
				}
			} else if (condition.equalsIgnoreCase("Task Runtime (Minutes)")) {
				final long minutesPassed = TimeUnit.NANOSECONDS
						.toMinutes((System.nanoTime() - CBFMultitasker.taskStartTime));
				if (minutesPassed >= conditionAmount) {
					return true;
				}
			}
		}
		return false;
	}

	private final void setBankToTreesPath(final TilePath bankToTreesPath) {
		this.bankToTreesPath = bankToTreesPath;
	}

	public void setExtraLogIds(final int[] extraLogIds) {
		this.extraLogIds = extraLogIds;
	}

	private final void setInventoryLogId(final int inventoryLogId) {
		this.inventoryLogId = inventoryLogId;
	}

	private final void setLodestoneToBankPath(final TilePath lodestoneToBankPath) {
		this.lodestoneToBankPath = lodestoneToBankPath;
	}

	private final void setLodestoneToTreesPath(
			final TilePath lodestoneToTreesPath) {
		this.lodestoneToTreesPath = lodestoneToTreesPath;
	}

	private final void setOverallTaskArea(final Area overallTaskArea) {
		this.overallTaskArea = overallTaskArea;
	}

	public void setRunAway(final boolean runAway) {
		this.runAway = runAway;
	}

	private final void setTaskBankArea(final Area taskBankArea) {
		this.taskBankArea = taskBankArea;
	}

	private final void setTaskTreeArea(final Area taskTreeArea) {
		this.taskTreeArea = taskTreeArea;
	}

	private final void setTreesToBankPath(final TilePath treesToBankPath) {
		this.treesToBankPath = treesToBankPath;
	}

}
