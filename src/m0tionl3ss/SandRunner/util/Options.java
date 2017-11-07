package m0tionl3ss.SandRunner.util;

public class Options {
	public enum Mode
	{
		PVP,CWARS
	}
	private Mode mode;
	private static Options options = new Options();
	private boolean useEscape;
	private boolean useCompass;
	public boolean getUseCompass() {
		return useCompass;
	}
	public void setUseCompass(boolean useCompass) {
		this.useCompass = useCompass;
	}
	private Options() {}
	public boolean getUseEscape() {
		return useEscape;
	}
	public void setUseEscape(boolean useEscape) {
		this.useEscape = useEscape;
	}
	public static Options getInstance()
	{
		if(options == null)
			return new Options();
		return options;
	}
	public Mode getMode() {
		return mode;
	}
	public void setMode(Mode mode) {
		this.mode = mode;
	}
}
