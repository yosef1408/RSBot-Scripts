package montezuma.script.task;

import java.awt.Graphics;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

/**
 * Chop a tree based on the name of a GameObject "Tree"
 * 
 * @author Robby
 *
 */
public class UseBankTask extends Task<ClientContext> {

	private GameObject booth;
	private String name;
	private String task;

	public UseBankTask(ClientContext ctx, String name, String task) {
		super(ctx);
		this.booth = null;
		this.name = name;
		this.task = task;
	}

	@Override
	public boolean activate() {
		return !ctx.objects.select().name(name).isEmpty() && ctx.players.local().animation() == -1;
	}

	@Override
	public void execute() {

		booth = ctx.objects.nearest().poll();

		if (booth.inViewport()) {
			
			System.out.println(ctx.chat.canContinue() + " " + ctx.chat.chatting());
			
			if(ctx.chat.canContinue()) {
				ctx.chat.clickContinue();
			} else if (ctx.chat.chatting()) {
				ctx.chat.select().text("Yes.").poll().select();
			} else {
				booth.interact(task);
			}
			
		} else {
			
			ctx.movement.step(booth);
			ctx.camera.turnTo(booth);
		}

	}

	@Override
	public boolean draw() {
		return booth != null;
	}

	@Override
	public void paint(Graphics g1) {
		booth.draw(g1);
	}

}
