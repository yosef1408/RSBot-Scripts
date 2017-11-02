package m0tionl3ss.CharterBuyer.util;

public class Info 
{
	private static Info info = new Info();
	private int itemsBought = 0;
	
	private Info() {}
	public static Info getInstance()
	{
		if (info == null)
			return new Info();
		else
			return info;
	}
	public int getItemsBought() {
		return itemsBought;
	}
	public void addItemsBought(int amount) {
		this.itemsBought += amount;
	}
}
