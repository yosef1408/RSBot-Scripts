package xXTheAwesomerXx.dbscripts.rs3.woodcutting.tasks;

import java.util.concurrent.Callable;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Component;
import org.powerbot.script.rt6.TilePath;
import org.powerbot.script.rt6.Widget;

import xXTheAwesomerXx.dbscripts.rs3.woodcutting.cbfmultitasker.CBFMultitasker;
import xXTheAwesomerXx.dbscripts.rs3.woodcutting.wrapper.Task;
import xXTheAwesomerXx.dbscripts.rs3.woodcutting.wrapper.Trade;

public class MuleTask extends Task<ClientContext> implements MessageListener {

	private enum State {
		DO_TRADE, GET_NOTES, RUN_TO_BANK, RUN_TO_TREE, SEND_AMOUNT, WAIT
	}

	private boolean bankStartingInventory = false, started = false;
	private boolean doTrade = false;
	private int[] extraLogIds;
	private boolean getNotes = false;
	public int inventoryLogId;
	private boolean sendCollectedAmount = false;
	private Area taskTreeArea, taskBankArea;
	private final Trade trade = new Trade(ctx);
	private TilePath treesToBankPath, lodestoneToBankPath,
	lodestoneToTreesPath, bankToTreesPath;

	public MuleTask(final ClientContext ctx, final String treeSelect,
			final String locSelect) {
		super(ctx);
		setTreeSelection(treeSelect);
		setLocSelection(locSelect);
	}

	@Override
	public boolean activate() {
		return (true);
	}

	@Override
	public void execute() {
		if (!started) {
			player = ctx.players.local();
			CBFMultitasker.doMuleTask = true; // just to make sure it's started
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
			if (ctx.backpack.select()
					.id(m.concat(m.allLogIds(), m.allUnstrungBowIds())).count() > 0) {
				bankStartingInventory = true;
			}
			started = true;
			ctx.input.sendln("");
			ctx.input.sendln("Oh it's you again!");
		}
		switch (getState()) {
			case DO_TRADE :
				if (trade.select().trading()) {
					if (trade.getTradePartner().equalsIgnoreCase(
							CBFMultitasker.muleName)) {
						if (trade.offering()) {
							if (ctx.backpack.select().id(m.allNoteIds())
									.count() == 0) {
								if (trade.partnerAcceptedOffer()) {
									trade.acceptOffer();
								} else if (!trade.acceptedOffer()) {
									trade.acceptOffer();
								}
							} else {
								for (int i = 0; i < m.allNoteIds().length; i++) {
									if (ctx.backpack.select()
											.id(m.allNoteIds()[i]).count() > 0) {
										final int assignmentItemIndex = ctx.backpack
												.select().id(m.allNoteIds()[i])
												.shuffle().poll().component()
												.index();
										final Widget widget = ctx.widgets
												.widget(336);
										if (widget.valid()) {
											final Component component = widget
													.component(0)
													.component(
															assignmentItemIndex);
											if (component.itemId() == (m
													.allNoteIds()[i])) {
												component.interact("Offer-All");
											}
										}
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
					ctx.players.select().name(CBFMultitasker.muleName).poll()
					.interact("Trade");
					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return trade.trading();
						}
					}, 2000, 5);
				}
				break;
			case SEND_AMOUNT :
				sendCollectedAmount = false;
				break;
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
			case GET_NOTES :
				CBFMultitasker.statusMessage = "Getting Notes";
				if (bankStartingInventory) {
					final int[] allItems = m.concat(m.allLogIds(),
							m.allUnstrungBowIds());
					bankStartingInventory = !m.bankItems(getLocSelection(),
							allItems);
				} else {
					getNotes = !m.getNotes(getLocSelection());
				}
				break;
			case WAIT :
				CBFMultitasker.statusMessage = "Waiting...";
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

	private final TilePath getLodestoneToBankPath() {
		return lodestoneToBankPath;
	}

	private final TilePath getLodestoneToTreesPath() {
		return lodestoneToTreesPath;
	}

	private State getState() {
		if (getNotes) {
			if (getTaskBankArea().contains(ctx.players.local())) {
				return State.GET_NOTES;
			} else {
				return State.RUN_TO_BANK;
			}
		} else {
			if (doTrade) {
				if (getTaskTreeArea().contains(ctx.players.local())) {
					return State.DO_TRADE;
				} else {
					return State.RUN_TO_TREE;
				}
			} else {
				if (sendCollectedAmount) {
					return State.SEND_AMOUNT;
				} else {
					return State.WAIT;
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

	@Override
	public void messaged(final MessageEvent m) {
		if (m.source().isEmpty()) {
			if (m.text().contains("Accepted")
					|| m.text().contains("You can only give away")) {
				doTrade = false;
			}
		} else {
			if (m.source()
					.toString()
					.replaceAll("_", " ")
					.toLowerCase()
					.equalsIgnoreCase(
							CBFMultitasker.muleName.replaceAll("_", " ")
							.toLowerCase())) {
				if (m.text()
						.toLowerCase()
						.equalsIgnoreCase(
								CBFMultitasker.tradePlayerMessage.toLowerCase())) {
					doTrade = true;
				} else if (m.text().toLowerCase()
						.equalsIgnoreCase(CBFMultitasker.getNotesMessage)) {
					getNotes = true;
				} else if (m.text().toLowerCase()
						.equalsIgnoreCase("Alright Thanks".toLowerCase())) {
					CBFMultitasker.muleTaskList.clear();
					CBFMultitasker.doMuleTask = false;
				}
			}
		}
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
