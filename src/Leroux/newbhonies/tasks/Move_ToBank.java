package Leroux.newbhonies.tasks;

import org.powerbot.script.rt6.ClientContext;

import Leroux.newbhonies.methods.Walker;
import Leroux.newbhonies.script.Task;
import Leroux.newbhonies.constants.Areas;
import Leroux.newbhonies.constants.Paths;

public class Move_ToBank extends Task {

	public Move_ToBank(ClientContext ctx) {
		super(ctx);
	}
	
	private Walker walk = new Walker(ctx);
	private Paths path = new Paths();
	private Areas area = new Areas();	

	@Override
	public boolean activate() {
		
		return !area.getBankArea().contains(ctx.players.local())
					&& ctx.backpack.select().count() == 28;
		
	}

	@Override
	public void execute() {
		
		walk.followPath(path.getToBank(), -2, 2, area.getBankTile());

	}
}