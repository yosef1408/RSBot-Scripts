package m0tionl3ss.JewelEnchanter.util;

public enum BoltSpell {
	OPAL(2,879),SAPPHIRE(3,9337),JADE(4,9335),PEARL(5,880),EMERALD(6,9338),RED_TOPAZ(7,9336), RUBY(8,9339), DIAMOND(9,9340),DRAGONSTONE(10,9341), ONYX(11,9342);
	private final int componentId;
	private final int itemId;
	private BoltSpell(int componentId, int itemId)
	{
		this.componentId = componentId;
		this.itemId = itemId;
	}
	public int getComponentId()
	{
		return componentId;
	}
	public int getItemId()
	{
		return itemId;
	}
	
}
