package m0tionl3ss.CharterBuyer.util;

public class Options
{
	private static Options options = new Options();
	private boolean useEscape;
	private boolean useScroll;
	private boolean outOfStock;
	private boolean shuffleWorlds;
	public boolean getShuffleWorlds() {
		return shuffleWorlds;
	}
	public void setShuffleWorlds(boolean shuffleWorlds) {
		this.shuffleWorlds = shuffleWorlds;
	}
	public boolean isOutOfStock() {
		return outOfStock;
	}
	public void setOutOfStock(boolean outOfStock) {
		this.outOfStock = outOfStock;
	}
	public boolean getUseEscape() {
		return useEscape;
	}
	public void setUseEscape(boolean useEscape) {
		this.useEscape = useEscape;
	}
	public boolean getUseScroll() {
		return useScroll;
	}
	public void setUseScroll(boolean useScroll) {
		this.useScroll = useScroll;
	}
	private Options() {}
	public static Options getInstance()
	{
		if (options == null)
			return new Options();
		else
			return options;
	}
}
