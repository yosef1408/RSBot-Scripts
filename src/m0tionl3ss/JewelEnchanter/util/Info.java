package m0tionl3ss.JewelEnchanter.util;

import org.powerbot.script.rt4.Magic.Spell;

public class Info 
{
	public enum EnchantSpells
	{
		LEVEL_1,LEVEL_2,LEVEL_3,LEVEL_4,LEVEL_5,LEVEL_6,LEVEL_7;
	}
	public enum Mode
	{
		BOLT,JEWEL;
	}
	private BoltSpell boltSpell;
	private int itemToWithdrawId;
	private static Info info = new Info();
	private boolean closeBankUsingEscape;
	private EnchantSpells spellToUse;
	private Mode mode;
	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	public Spell getSpellToUse() {
		switch(spellToUse)
		{
		case LEVEL_1: 
			return  Spell.ENCHANT_LEVEL_1_JEWELLERY;
		case LEVEL_2:
			return  Spell.ENCHANT_LEVEL_2_JEWELLERY;
		case LEVEL_3:
			return  Spell.ENCHANT_LEVEL_3_JEWELLERY;
		case LEVEL_4:
			return  Spell.ENCHANT_LEVEL_4_JEWELLERY;
		case LEVEL_5:
			return  Spell.ENCHANT_LEVEL_5_JEWELLERY;
		case LEVEL_6:
			return  Spell.ENCHANT_LEVEL_6_JEWELLERY;
		case LEVEL_7:
			return  Spell.ENCHANT_LEVEL_7_JEWELLERY;
		
		}
		return null;
	}
	public boolean getCloseBankUsingeEscape() {
		return closeBankUsingEscape;
	}
	public void setCloseBankUsingeEscape(boolean closeBankUsingEscape) {
		this.closeBankUsingEscape = closeBankUsingEscape;
	}
	public void setSpellToUse(EnchantSpells spellToUse) {
		this.spellToUse = spellToUse;
	}
	public int getItemToWithdrawId() {
		return itemToWithdrawId;
	}
	public void setItemToWithdrawId(int itemToWithdrawId) {
		this.itemToWithdrawId = itemToWithdrawId;
	}
	public BoltSpell getBoltSpell() {
		return boltSpell;
	}
	public void setBoltSpell(BoltSpell boltSpell) {
		this.boltSpell = boltSpell;
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
