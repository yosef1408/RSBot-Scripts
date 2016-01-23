package xXTheAwesomerXx.dbscripts.rs3.woodcutting.wrapper;

import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Player;

public abstract class Task<C extends ClientContext> extends ClientAccessor {

	public Methods m = new Methods(ctx);
	public String optionSelection, treeSelection, locSelection, typeSelection,
			conditionType;
	public Player player = ctx.players.local();
	public int queuePosition, conditionAmount;

	public boolean shouldStop;

	public Task(final C ctx) {
		super(ctx);
	}

	public abstract boolean activate();

	public abstract void execute();

	public final int getConditionAmount() {
		return conditionAmount;
	}

	public final String getConditionType() {
		return conditionType;
	}

	public final String getLocSelection() {
		return locSelection;
	}

	public final Methods getM() {
		return m;
	}

	public final String getOptionSelection() {
		return optionSelection;
	}

	public final int getQueuePosition() {
		return queuePosition;
	}

	public final String getTreeSelection() {
		return treeSelection;
	}

	public final String getTypeSelection() {
		return typeSelection;
	}

	public final boolean isShouldStop() {
		return shouldStop;
	}

	public final void setConditionAmount(final int conditionAmount) {
		this.conditionAmount = conditionAmount;
	}

	public final void setConditionType(final String conditionType) {
		this.conditionType = conditionType;
	}

	public final void setLocSelection(final String locSelection) {
		this.locSelection = locSelection;
	}

	public final void setM(final Methods m) {
		this.m = m;
	}

	public final void setOptionSelection(final String optionSelection) {
		this.optionSelection = optionSelection;
	}

	public final void setQueuePosition(final int queuePosition) {
		this.queuePosition = queuePosition;
	}

	public final void setShouldStop(final boolean shouldStop) {
		this.shouldStop = shouldStop;
	}

	public final void setTreeSelection(final String treeSelection) {
		this.treeSelection = treeSelection;
	}

	public final void setTypeSelection(final String typeSelection) {
		this.typeSelection = typeSelection;
	}

	public boolean shouldStop() {
		return shouldStop;
	}

}
