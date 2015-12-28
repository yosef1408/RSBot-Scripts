package Leroux.newbhonies.tasks;

import org.powerbot.script.rt6.ClientContext;

import Leroux.newbhonies.methods.Walker;
import Leroux.newbhonies.script.Task;
import Leroux.newbhonies.constants.Areas;
import Leroux.newbhonies.constants.InvItems;
import Leroux.newbhonies.constants.Paths;

public class Move_ToField extends Task {

	public Move_ToField(ClientContext ctx) {
		super(ctx);
	}
	
	private Walker walk = new Walker(ctx);
	private InvItems items = new InvItems(ctx);
	private Paths path = new Paths();
	private Areas area = new Areas();	

	@Override
	public boolean activate() {
		
		return !area.getHoneyArea().contains(ctx.players.local())
				&& !ctx.backpack.select().contains(items.getHoneyComb());
		
	}

	@Override
	public void execute() {
		
		walk.followPath(path.getToField(), -2, 2);
		
	}
}