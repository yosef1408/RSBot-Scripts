package m0tionl3ss.SandRunner.util;

public class Info {
	private static Info info = new Info();
	private Info() {}
	private boolean withdrawDuelRing;
	private boolean dead = false;
	private int deadCounter = 0;

	public int getDeadCounter() {
		return deadCounter;
	}

	public void incrementDeadCounter() {
		this.deadCounter++;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	public static Info getInstance()
	{
		if(info == null)
			return new Info();
		return info;
	}

	public boolean getWithdrawDuelRing() {
		return withdrawDuelRing;
	}

	public void setWithdrawDuelRing(boolean withdrawDuelRing) {
		this.withdrawDuelRing = withdrawDuelRing;
	}

}
