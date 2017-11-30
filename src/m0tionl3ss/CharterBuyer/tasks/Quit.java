package m0tionl3ss.CharterBuyer.tasks;

import org.powerbot.script.rt4.ClientContext;
import m0tionl3ss.CharterBuyer.util.Tools;

public class Quit extends Task{

	public Quit(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean activate() {
		return ctx.inventory.isEmpty();
	}

	@Override
	public void execute() {
		System.out.println(getClass().getSimpleName());
		Tools.logout(ctx);
		ctx.controller.stop();
		
	}

	@Override
	public String status() {
		return "Quiting..";
	}

}
