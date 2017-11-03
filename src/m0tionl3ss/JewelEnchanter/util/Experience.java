package m0tionl3ss.JewelEnchanter.util;

import org.powerbot.script.rt4.ClientContext;

public class Experience {
	private static Experience exp = new Experience();
	private int skill;
	private int startingLevel;
	private int startExp;
	private int counterExp;
	private ClientContext ctx;
	private long timeRunning;

	private Experience() {
	}

	public static Experience getInstance() {
		if (exp == null)
			return new Experience();
		else
			return exp;
	}

	public void setTimeRunning(long time) {
		this.timeRunning = time;
	}

	public void init(ClientContext ctx, int skill) {
		this.ctx = ctx;
		this.skill = skill;
		this.startingLevel = ctx.skills.level(skill);
		this.startExp = ctx.skills.experience(skill);
		this.counterExp = ctx.skills.experience(skill);
	}

	public int getCurrentLevel() {
		return ctx.skills.level(skill);
	}
	public int getNextLevel() {
		return ctx.skills.level(skill) + 1;
	}
	public int getCurrentExp() {
		return ctx.skills.experience(skill);
	}

	public int levelsGained() {
		return getCurrentLevel() - startingLevel;
	}

	public int expGained() {
		return ctx.skills.experience(skill) - startExp;
	}

	public int expTillLevelUp() {
		return ctx.skills.experienceAt(getCurrentLevel() + 1) - getCurrentExp();
	}

	public int expPerHour() {
		return (int) (3600000 / timeRunning * expGained());
	}
	public int getStartExp()
	{
		return counterExp;
	}
	public void update(int exp)
	{
		counterExp = exp;
	}

}
