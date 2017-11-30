package m0tionl3ss.JewelEnchanter.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.Game.Tab;
import m0tionl3ss.JewelEnchanter.util.Info;

public class EnchantBolts extends Task {
	private int boltId = Info.getInstance().getBoltSpell().getItemId();
	public EnchantBolts(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return !ctx.inventory.select().id(boltId).isEmpty() && !ctx.inventory.select().name("Cosmic rune").isEmpty();
	}

	@Override
	public void execute() {
		System.out.println(getClass().getSimpleName());
		ctx.game.tab(Tab.MAGIC);
		Component enchantBolt = ctx.widgets.widget(218).component(4);
		Component enchantCrossBoltInterface = ctx.widgets.widget(80).component(1);
		Component boltSpell = ctx.widgets.widget(80).component(Info.getInstance().getBoltSpell().getComponentId());
		Condition.wait(() -> ctx.game.tab() == Tab.MAGIC,200,5);
		if (!enchantCrossBoltInterface.visible())
		{
			enchantBolt.click();
		}		
		boltSpell.click();
		Condition.wait(() -> ctx.players.local().animation() != 4462,500,3);
		
	}

	@Override
	public String status() {
		return "Enchanting bolts..";
	}

}
