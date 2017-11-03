package m0tionl3ss.JewelEnchanter.util;

import org.powerbot.script.rt4.Magic;

public class Info 
{
	public enum EnchantSpell 
	{
		LEVEL_1,LEVEL_2,LEVEL_3,LEVEL_4,LEVEL_5,LEVEL_6,LEVEL_7;
	}
	private int itemToWithdrawId;
	private static Info info = new Info();
	private boolean closeBankUsingeEscape;
	
	private EnchantSpell spellToUse;
	public Magic.Spell getSpellToUse() {
		switch(spellToUse)
		{
		case LEVEL_1: 
			return  Magic.Spell.ENCHANT_LEVEL_1_JEWELLERY;
		case LEVEL_2:
			return  Magic.Spell.ENCHANT_LEVEL_2_JEWELLERY;
		case LEVEL_3:
			return  Magic.Spell.ENCHANT_LEVEL_3_JEWELLERY;
		case LEVEL_4:
			return  Magic.Spell.ENCHANT_LEVEL_4_JEWELLERY;
		case LEVEL_5:
			return  Magic.Spell.ENCHANT_LEVEL_5_JEWELLERY;
		case LEVEL_6:
			return  Magic.Spell.ENCHANT_LEVEL_6_JEWELLERY;
		case LEVEL_7:
			return  Magic.Spell.ENCHANT_LEVEL_7_JEWELLERY;
		}
		return null;
	}
	public boolean getCloseBankUsingeEscape() {
		return closeBankUsingeEscape;
	}
	public void setCloseBankUsingeEscape(boolean closeBankUsingeEscape) {
		this.closeBankUsingeEscape = closeBankUsingeEscape;
	}
	public void setSpellToUse(EnchantSpell spellToUse) {
		this.spellToUse = spellToUse;
	}
	public int getItemToWithdrawId() {
		return itemToWithdrawId;
	}
	public void setItemToWithdrawId(int itemToWithdrawId) {
		this.itemToWithdrawId = itemToWithdrawId;
	}
	
	private Info() {}
	public static Info getInstance()
	{
		if (info == null)
			return new Info();
		else
			return info;
	}
}
