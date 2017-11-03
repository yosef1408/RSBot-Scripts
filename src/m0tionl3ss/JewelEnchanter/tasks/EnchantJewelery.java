package m0tionl3ss.JewelEnchanter.tasks;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GeItem;
import org.powerbot.script.rt4.Item;
import org.powerbot.script.rt4.Magic.Spell;

import m0tionl3ss.JewelEnchanter.util.Antiban;
import m0tionl3ss.JewelEnchanter.util.Info;

public class EnchantJewelery extends Task {

	//private String[] enchantableItems = {"Sapphire ring", "Sapphire amulet"};
	private int item;
	private Spell spellToUse;
	public EnchantJewelery(ClientContext ctx) {
		super(ctx);
		item = Info.getInstance().getItemToWithdrawId();
		spellToUse = Info.getInstance().getSpellToUse();
	}

	@Override
	public boolean activate() {
		return !ctx.inventory.select().id(item).isEmpty() && !ctx.inventory.select().name("Cosmic rune").isEmpty();
		
	}

	@Override
	public void execute() {
		System.out.println(getClass().getSimpleName());
		Antiban.getInstance().execute(ctx);
		
		if (!ctx.inventory.select().id(item).isEmpty())
		{
			boolean clickedSpell = ctx.magic.cast(spellToUse);
			Item itemToEnchant = ctx.inventory.select().id(item).poll();
			if (itemToEnchant.valid() && clickedSpell)
				itemToEnchant.click();
		}
		Condition.wait(() -> ctx.magic.component(spellToUse).visible() && ctx.players.local().animation() != 931,485,3);
		
		
		
		
	}

}
