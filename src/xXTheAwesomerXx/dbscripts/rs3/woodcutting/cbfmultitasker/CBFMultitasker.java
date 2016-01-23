package xXTheAwesomerXx.dbscripts.rs3.woodcutting.cbfmultitasker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.powerbot.script.MessageEvent;
import org.powerbot.script.MessageListener;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Constants;

import xXTheAwesomerXx.dbscripts.rs3.woodcutting.cbfmultitasker.ui.CBFGui;
import xXTheAwesomerXx.dbscripts.rs3.woodcutting.tasks.MuleTask;
import xXTheAwesomerXx.dbscripts.rs3.woodcutting.wrapper.Methods;
import xXTheAwesomerXx.dbscripts.rs3.woodcutting.wrapper.Task;

@Script.Manifest(name = "CBFMultitasker", description = "Chained Multitasker by xXTheAwesomerXx", properties = "author=xXTheAwesomerXx; topic=1299510;")
public class CBFMultitasker extends PollingScript<ClientContext>
implements
PaintListener,
MessageListener,
MouseListener,
MouseMotionListener {
	public static boolean addToToolbelt = false, useLodestone = false,
			scriptStarted = false, shouldStopTask = false, doMuleTask = false,
			allowMuleTrading = false;
	public static List<Task> muleTaskList = new ArrayList<Task>();
	private static ClientContext scriptContext;
	public static String statusMessage = "Starting Script...", muleName = "",
			initiateMuleMessage = "", getNotesMessage = "",
			tradePlayerMessage = "";
	@SuppressWarnings("rawtypes")
	public static List<Task> taskList = new ArrayList<Task>();
	public static int taskListSize = 0, paintIndex = 1, runTries = 0,
			conditionAmount = 1, queuePosition = 0;
	public static long taskStartWCExp = 1, taskStartFMExp = 1,
			taskStartFLExp = 1, taskStartWCLvl = 1, taskStartFMLvl = 1,
			taskStartFLLvl = 1, taskCollectedLogs = 0, taskBurnedLogs = 0,
			taskCraftedLogs = 0, totalRuntime = 0, taskRuntime = 0,
			scriptStartTime = 0, taskStartTime = 0;

	private static long taskWCLvlsGained = 0;

	public static long totalBurnedLogs = 0;

	public static long totalCollectedLogs = 0;

	public static long totalCraftedLogs = 0;
	public static String treeSelection = "", locSelection = "",
			typeSelection = "", optionSelection = "", conditionType = "";

	public static ClientContext getScriptContext() {
		return scriptContext;
	}

	public static void removeLeadingTask() {
		if (taskList.size() > 0) {
			taskList.remove(0);
		}
	}

	public static final ClientContext script() {
		return getScriptContext();
	}

	public static void setScriptContext(final ClientContext scriptContext) {
		CBFMultitasker.scriptContext = scriptContext;
	}

	private Image bg, showPaintImage;
	Ellipse2D.Double circle = new Ellipse2D.Double(322, 1, 58, 58),
			circle2 = new Ellipse2D.Double(322, 69, 58, 58),
			circle3 = new Ellipse2D.Double(322, 135, 58, 58);
	Rectangle2D.Double closeBox = new Rectangle2D.Double(291, 2, 17, 17);
	private int currentFiremakingLevel = 1, currentFletchingLevel = 1,
			currentFLExperience = 1, currentFMExperience = 1,
			currentWCExperience = 1, currentWoodcuttingLevel = 1,
			FLExperienceAtMyLevel = 1, FLExperienceAtNextLevel = 1,
			FLExpReq = 1, FLExpToLvl = 1, FLProgress = 1,
			FMExperienceAtMyLevel = 1, FMExperienceAtNextLevel = 1,
			FMExpReq = 1, FMExpToLvl = 1, FMProgress = 1, logsToLevelFL = 1,
			logsToLevelFM = 1, logsToLevelWC = 1, WCExperienceAtMyLevel = 1,
			WCExperienceAtNextLevel = 1, WCExpReq = 1, WCExpToLvl = 1,
			WCProgress = 1;
	private final Methods m = new Methods(ctx);
	private final String revisionString = "v1.0";
	private boolean showPaint = true;
	private long startWCExp = 1, startFMExp = 1, startFLExp = 1,
			startWCLvl = 1, startFMLvl = 1, startFLLvl = 1;
	private long taskFLExpGained = 1;
	private long taskFLExpHr = 1;
	private long taskFLLvlsGained = 1;
	private long taskFMExpGained = 1;
	private long taskFMExpHr = 1;
	private long taskFMLvlsGained = 1;
	private long taskWCExpGained = 1;
	private long taskWCExpHr = 1;
	private long totalFLExpGained = 1;
	private long totalFLLvlsGained = 1;
	private long totalFMExpGained = 1;
	private long totalFMLvlsGained = 1;
	private long totalWCExpGained = 1;
	private long totalWCLvlsGained = 1;
	private final int woodcutting = Constants.SKILLS_WOODCUTTING,
			firemaking = Constants.SKILLS_FIREMAKING,
			fletching = Constants.SKILLS_FLETCHING;

	private String formatTime(long millis) {
		final int hours = (int) (millis / 3600000);
		millis %= 3600000;

		final int minutes = (int) (millis / 60000);
		millis %= 60000;

		final int seconds = (int) (millis / 1000);

		return (hours < 10 ? "0" + hours : hours) + " : "
		+ (minutes < 10 ? "0" + minutes : minutes) + " : "
		+ (seconds < 10 ? "0" + seconds : seconds);
	}

	private double getFLExperiencePerLog(final String treeType,
			final String optionSelection) {
		if (treeType.equalsIgnoreCase("Yew")) {
			if (optionSelection.contains("Shortbow")) {
				return 67.5;
			} else if (optionSelection.contains("Shieldbow")) {
				return 75;
			} else if (optionSelection.contains("Stock")) {
				return 50;
			} else if (optionSelection.contains("Arrow")) {
				return 15;
			}
		} else if (treeType.equalsIgnoreCase("Maple")) {
			if (optionSelection.contains("Shortbow")) {
				return 50;
			} else if (optionSelection.contains("Shieldbow")) {
				return 58.3;
			} else if (optionSelection.contains("Stock")) {
				return 32;
			} else if (optionSelection.contains("Arrow")) {
				return 12.5;
			}
		} else if (treeType.equalsIgnoreCase("Willow")) {
			if (optionSelection.contains("Shortbow")) {
				return 33.3;
			} else if (optionSelection.contains("Shieldbow")) {
				return 41.5;
			} else if (optionSelection.contains("Stock")) {
				return 22;
			} else if (optionSelection.contains("Arrow")) {
				return 10;
			}
		} else if (treeType.equalsIgnoreCase("Oak")) {
			if (optionSelection.contains("Shortbow")) {
				return 16.5;
			} else if (optionSelection.contains("Shieldbow")) {
				return 25;
			} else if (optionSelection.contains("Stock")) {
				return 16;
			} else if (optionSelection.contains("Arrow")) {
				return 7.5;
			}
		} else if (treeType.equalsIgnoreCase("Normal")) {
			if (optionSelection.contains("Shortbow")) {
				return 5;
			} else if (optionSelection.contains("Shieldbow")) {
				return 10;
			} else if (optionSelection.contains("Stock")) {
				return 6;
			} else if (optionSelection.contains("Arrow")) {
				return 5;
			}
		}
		return 0.0;
	}

	private double getFMExperiencePerLog(final String treeType) {
		if (treeType.equalsIgnoreCase("Yew")) {
			return 202.5;
		} else if (treeType.equalsIgnoreCase("Maple")) {
			return 142.5;
		} else if (treeType.equalsIgnoreCase("Willow")) {
			return 90;
		} else if (treeType.equalsIgnoreCase("Oak")) {
			return 60;
		} else if (treeType.equalsIgnoreCase("Normal")) {
			return 40;
		}
		return 0.0;
	}

	public String getTaskString(final String treeType, final String locString,
			final String typeSelection, final String optionSelection) {
		String taskString = "";
		if (typeSelection.equalsIgnoreCase("Chop")) {
			taskString += "Chopping ";
		} else if (typeSelection.equalsIgnoreCase("Chop | Drop")) {
			taskString += "Dropping ";
		} else if (typeSelection.equalsIgnoreCase("Chop | Bank")) {
			taskString += "Banking ";
		} else if (typeSelection.equalsIgnoreCase("Chop | Burn")) {
			taskString += "Burning ";
		} else if (typeSelection.equalsIgnoreCase("Chop | Fletch")) {
			taskString += "Fletching ";
		}
		taskString += treeType + " ";
		if (typeSelection.equalsIgnoreCase("Chop | Fletch")) {
			taskString += optionSelection + " ";
		}
		taskString += "at " + locString;
		return taskString;
	}

	private double getWCExperiencePerLog(final String treeType) {
		if (treeType.equalsIgnoreCase("Ivy")) {
			return 332.5;
		} else if (treeType.equalsIgnoreCase("Yew")) {
			return 175;
		} else if (treeType.equalsIgnoreCase("Maple")) {
			return 100;
		} else if (treeType.equalsIgnoreCase("Willow")) {
			return 67.5;
		} else if (treeType.equalsIgnoreCase("Oak")) {
			return 37.5;
		} else if (treeType.equalsIgnoreCase("Normal")) {
			return 25;
		}
		return 0.0;
	}

	@Override
	public void messaged(final MessageEvent m) {
		if (m.source().toString().isEmpty()) {

		} else {
			if (allowMuleTrading) {
				if (m.source()
						.toString()
						.replaceAll("_", " ")
						.toLowerCase()
						.equalsIgnoreCase(
								muleName.replaceAll("_", " ").toLowerCase())) {
					if (m.text()
							.toLowerCase()
							.equalsIgnoreCase(initiateMuleMessage.toLowerCase())) {
						muleTaskList.add(new MuleTask(ctx, treeSelection,
								locSelection));
						doMuleTask = true;
					}
				}
			}
		}
	}

	@Override
	public void mouseClicked(final MouseEvent me) {
		if (closeBox.contains(me.getPoint())) {
			showPaint = !showPaint;
		}
	}

	@Override
	public void mouseDragged(final MouseEvent me) {

	}

	@Override
	public void mouseEntered(final MouseEvent me) {

	}

	@Override
	public void mouseExited(final MouseEvent me) {

	}

	@Override
	public void mouseMoved(final MouseEvent me) {
		if (circle.contains(me.getPoint())) {
			paintIndex = 1;
		} else if (circle2.contains(me.getPoint())) {
			paintIndex = 2;
		} else if (circle3.contains(me.getPoint())) {
			paintIndex = 3;
		} else {
			paintIndex = 0;
		}
	}

	@Override
	public void mousePressed(final MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(final MouseEvent arg0) {

	}

	@Override
	public void poll() {
		if (addToToolbelt) {
			if (m.hasBetterAxeInInventory()) {
				m.addBetterAxeToToolbelt();
			}
		} else {
			m.equipBetterAxe();
		}
		if (!doMuleTask) {
			if ((taskList != null) && (taskList.size() > 0)) {
				for (int i = 0; i < taskList.size(); i++) {
					if (taskList.get(i).activate()) {
						taskList.get(i).execute();
					}
				}
			} else {
				System.out.println("Stopping from main because of no tasks...");
				ctx.controller.stop();
			}
		} else {
			if ((muleTaskList != null) && (muleTaskList.size() > 0)) {
				if (muleTaskList.get(0).activate()) {
					muleTaskList.get(0).execute();
				}
			}
		}
	}

	@Override
	public void repaint(final Graphics graphics) {
		final Graphics2D g = (Graphics2D) graphics;
		if (scriptStarted && (taskList.size() >= 1) && ctx.game.loggedIn()) {
			final int stepSize = 16;
			g.setFont(new Font("Calibri", Font.HANGING_BASELINE, 14));
			totalRuntime = (System.nanoTime() - scriptStartTime) / 1000000;
			taskRuntime = (System.nanoTime() - taskStartTime) / 1000000;
			currentWoodcuttingLevel = ctx.skills.level(woodcutting);
			currentFiremakingLevel = ctx.skills.level(firemaking);
			currentFletchingLevel = ctx.skills.level(fletching);
			currentWCExperience = ctx.skills.experience(woodcutting);
			currentFMExperience = ctx.skills.experience(firemaking);
			currentFLExperience = ctx.skills.experience(fletching);
			totalWCExpGained = currentWCExperience - startWCExp;
			totalFMExpGained = currentFMExperience - startFMExp;
			totalFLExpGained = currentFLExperience - startFLExp;
			totalWCLvlsGained = currentWoodcuttingLevel - startWCLvl;
			totalFMLvlsGained = currentFiremakingLevel - startFMLvl;
			totalFLLvlsGained = currentFletchingLevel - startFLLvl;
			taskWCExpGained = currentWCExperience - taskStartWCExp;
			taskFMExpGained = currentFMExperience - taskStartFMExp;
			taskFLExpGained = currentFLExperience - taskStartFLExp;
			taskWCLvlsGained = currentWoodcuttingLevel - taskStartWCLvl;
			taskFMLvlsGained = currentFiremakingLevel - taskStartFMLvl;
			taskFLLvlsGained = currentFletchingLevel - taskStartFLLvl;
			taskWCExpHr = (taskWCExpGained * 3600000) / taskRuntime;
			taskFMExpHr = (taskFMExpGained * 3600000) / taskRuntime;
			taskFLExpHr = (taskFLExpGained * 3600000) / taskRuntime;
			WCExpToLvl = ctx.skills.experienceAt(currentWoodcuttingLevel + 1)
					- currentWCExperience;
			FMExpToLvl = ctx.skills.experienceAt(currentFiremakingLevel + 1)
					- currentFMExperience;
			FLExpToLvl = ctx.skills.experienceAt(currentFletchingLevel + 1)
					- currentFLExperience;
			final String WCTTL = (currentWoodcuttingLevel != 99
					? currentWoodcuttingLevel + 1
							: "X")
							+ ": "
							+ formatTime((taskWCExpHr <= 0 ? 0 : (WCExpToLvl * 3600000)
									/ taskWCExpHr));
			final String FMTTL = (currentFiremakingLevel != 99
					? currentFiremakingLevel + 1
							: "X")
							+ ": "
							+ formatTime((taskFMExpHr <= 0 ? 0 : (FMExpToLvl * 3600000)
									/ taskFMExpHr));
			final String FLTTL = (currentFletchingLevel != 99
					? currentFletchingLevel + 1
							: "X")
							+ ": "
							+ formatTime((taskFLExpHr <= 0 ? 0 : (FLExpToLvl * 3600000)
									/ taskFLExpHr));
			WCExperienceAtNextLevel = ctx.skills.experienceAt(ctx.skills
					.level(woodcutting) + 1);
			WCExperienceAtMyLevel = ctx.skills.experienceAt(ctx.skills
					.level(woodcutting));
			WCExpReq = WCExperienceAtNextLevel - WCExperienceAtMyLevel;
			if (WCExpToLvl <= 0) {
				WCExpToLvl = 1;
			}
			if (WCExpReq <= 0) {
				WCExpReq = 1;
			}
			WCProgress = ((100 - ((int) (WCExpToLvl * 100D) / WCExpReq)));
			FMExperienceAtNextLevel = ctx.skills.experienceAt(ctx.skills
					.level(firemaking) + 1);
			FMExperienceAtMyLevel = ctx.skills.experienceAt(ctx.skills
					.level(firemaking));
			FMExpReq = FMExperienceAtNextLevel - FMExperienceAtMyLevel;
			if (FMExpToLvl <= 0) {
				FMExpToLvl = 1;
			}
			if (FMExpReq <= 0) {
				FMExpReq = 1;
			}
			FMProgress = ((100 - ((int) (FMExpToLvl * 100D) / FMExpReq)));
			FLExperienceAtNextLevel = ctx.skills.experienceAt(ctx.skills
					.level(woodcutting) + 1);
			FLExperienceAtMyLevel = ctx.skills.experienceAt(ctx.skills
					.level(fletching));
			FLExpReq = FLExperienceAtNextLevel - FLExperienceAtMyLevel;
			if (FLExpToLvl <= 0) {
				FLExpToLvl = 1;
			}
			if (FLExpReq <= 0) {
				FLExpReq = 1;
			}
			FLProgress = ((100 - ((int) (FLExpToLvl * 100D) / FLExpReq)));
			logsToLevelWC = (int) (WCExpToLvl / getWCExperiencePerLog(treeSelection));
			logsToLevelFM = (int) (FMExpToLvl / getFMExperiencePerLog(treeSelection));
			if (typeSelection.contains("Fletch")) {
				logsToLevelFL = (int) (FLExpToLvl / getFLExperiencePerLog(
						treeSelection, optionSelection));
			}
			final List<String> messages = new ArrayList<String>();
			final int maxProgress = 100;
			final int fadeTime = 5;
			final int outterFraction = 1;
			final int xPos = 322, yPos1 = 0, yPos2 = 68, yPos3 = 135;
			final Graphics2D g2 = (Graphics2D) graphics;
			if (showPaint) {
				if (WCProgress <= (fadeTime + maxProgress)) {
					final int progressToDraw = Math
							.min(WCProgress, maxProgress);
					Color drawColor = new Color(0, 205, 0);

					if (WCProgress > maxProgress) {
						final int fadeAmount = fadeTime
								- (WCProgress - maxProgress);
						drawColor = new Color(drawColor.getRed(),
								drawColor.getGreen(), drawColor.getBlue(),
								(int) ((((float) fadeAmount) / fadeTime) * 255));
					}

					g2.setColor(drawColor);
					final int angle = -(int) (((float) progressToDraw / maxProgress) * 360);
					g2.fillArc(xPos, yPos1, 58, 58, 90, angle);

					g2.setColor(Color.white);
					g2.fillArc(58 / outterFraction / 2,
							58 / outterFraction / 2,
							(58 * (outterFraction - 1)) / outterFraction,
							(58 * (outterFraction - 1)) / outterFraction, 90,
							angle);

					g2.drawOval(58 / outterFraction / 2,
							58 / outterFraction / 2,
							(58 * (outterFraction - 1)) / outterFraction,
							(58 * (outterFraction - 1)) / outterFraction);
				}
				if (FMProgress <= (fadeTime + maxProgress)) {
					final int progressToDraw = Math
							.min(FMProgress, maxProgress);
					Color drawColor = new Color(255, 48, 48);

					if (FMProgress > maxProgress) {
						final int fadeAmount = fadeTime
								- (FMProgress - maxProgress);
						drawColor = new Color(drawColor.getRed(),
								drawColor.getGreen(), drawColor.getBlue(),
								(int) ((((float) fadeAmount) / fadeTime) * 255));
					}

					g2.setColor(drawColor);
					final int angle = -(int) (((float) progressToDraw / maxProgress) * 360);
					g2.fillArc(xPos, yPos2, 58, 58, 90, angle);

					g2.setColor(Color.white);
					g2.fillArc(58 / outterFraction / 2,
							58 / outterFraction / 2,
							(58 * (outterFraction - 1)) / outterFraction,
							(58 * (outterFraction - 1)) / outterFraction, 90,
							angle);

					g2.drawOval(58 / outterFraction / 2,
							58 / outterFraction / 2,
							(58 * (outterFraction - 1)) / outterFraction,
							(58 * (outterFraction - 1)) / outterFraction);
				}
				if (FLProgress <= (fadeTime + maxProgress)) {
					final int progressToDraw = Math
							.min(FLProgress, maxProgress);
					Color drawColor = new Color(0, 191, 255);

					if (FLProgress > maxProgress) {
						final int fadeAmount = fadeTime
								- (FLProgress - maxProgress);
						drawColor = new Color(drawColor.getRed(),
								drawColor.getGreen(), drawColor.getBlue(),
								(int) ((((float) fadeAmount) / fadeTime) * 255));
					}

					g2.setColor(drawColor);
					final int angle = -(int) (((float) progressToDraw / maxProgress) * 360);
					g2.fillArc(xPos, yPos3, 58, 58, 90, angle);

					g2.setColor(Color.white);
					g2.fillArc(58 / outterFraction / 2,
							58 / outterFraction / 2,
							(58 * (outterFraction - 1)) / outterFraction,
							(58 * (outterFraction - 1)) / outterFraction, 90,
							angle);

					g2.drawOval(58 / outterFraction / 2,
							58 / outterFraction / 2,
							(58 * (outterFraction - 1)) / outterFraction,
							(58 * (outterFraction - 1)) / outterFraction);
				}
			}
			if (typeSelection.equalsIgnoreCase("Chop")
					|| typeSelection.equalsIgnoreCase("Chop | Drop")
					|| typeSelection.equalsIgnoreCase("Chop | Bank")) {
				g.setColor(new Color(0, 205, 0));
			} else if (typeSelection.contains("Fletch")) {
				g.setColor(new Color(0, 191, 255));
			} else if (typeSelection.contains("Burn")) {
				g.setColor(new Color(255, 48, 48));
			}
			g.drawLine(ctx.input.getLocation().x - 5,
					ctx.input.getLocation().y - 5,
					ctx.input.getLocation().x + 5,
					ctx.input.getLocation().y + 5);
			g.drawLine(ctx.input.getLocation().x - 5,
					ctx.input.getLocation().y + 5,
					ctx.input.getLocation().x + 5,
					ctx.input.getLocation().y - 5);
			g.drawLine(0, ctx.input.getLocation().y, ctx.input.getLocation().x,
					ctx.input.getLocation().y);
			g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y,
					3000, ctx.input.getLocation().y);
			g.drawLine(ctx.input.getLocation().x, 0, ctx.input.getLocation().x,
					ctx.input.getLocation().y);
			g.drawLine(ctx.input.getLocation().x, ctx.input.getLocation().y,
					ctx.input.getLocation().x, 3000);
			if (showPaint) {
				g.drawImage(bg, 5, 0, null);
			}
			if (paintIndex == 0) {
				messages.add("Status: " + statusMessage);
				messages.add("Total Runtime: "
						+ formatTime((System.nanoTime() - scriptStartTime) / 1000000));
				messages.add("Task Runtime: "
						+ formatTime((System.nanoTime() - taskStartTime) / 1000000));
				messages.add(getTaskString(treeSelection, locSelection,
						typeSelection, optionSelection));
				messages.add("Queue: " + taskList.get(0).queuePosition + "/"
						+ taskListSize);
				if (shouldStopTask) {
					messages.add("Task Progress: " + taskProgress() + "%");
				} else {
					messages.add("Task Running Indefinetly!");
				}
				messages.add("Lvl Progress: WC - " + WCProgress + "%, FM - "
						+ FMProgress + "%, FL - " + FLProgress + "%");
			} else if (paintIndex == 1) {
				messages.add("Lvls Gained: " + totalWCLvlsGained + " | "
						+ taskWCLvlsGained + " (Task)");
				messages.add("Exp Gained: " + totalWCExpGained + " | "
						+ taskWCExpGained + " (Task)");
				messages.add("Exp to Lvl "
						+ (ctx.skills.level(woodcutting) + 1) + ": "
						+ WCExpToLvl + " | " + taskWCExpHr + " P/H");
				messages.add("Logs to Lvl "
						+ (ctx.skills.level(woodcutting) + 1) + ": "
						+ logsToLevelWC + " | "
						+ ((taskCollectedLogs * 3600000) / taskRuntime)
						+ " P/H");
				messages.add("TTL " + WCTTL);
				messages.add("Logs Chopped: " + totalCollectedLogs + " | "
						+ taskCollectedLogs + " (Task)");
			} else if (paintIndex == 2) {
				messages.add("Lvls Gained: " + totalFMLvlsGained + " | "
						+ taskFMLvlsGained + " (Task)");
				messages.add("Exp Gained: " + totalFMExpGained + " | "
						+ taskFMExpGained + " (Task)");
				messages.add("Exp to Lvl " + (ctx.skills.level(firemaking) + 1)
						+ ": " + FMExpToLvl + " | " + taskFMExpHr + " P/H");
				messages.add("Fires to Lvl "
						+ (ctx.skills.level(firemaking) + 1) + ": "
						+ logsToLevelFM + " | "
						+ ((taskBurnedLogs * 3600000) / taskRuntime) + " P/H");
				messages.add("TTL " + FMTTL);
				messages.add("Logs Burned: " + totalBurnedLogs + " | "
						+ taskBurnedLogs + " (Task)");
			} else if (paintIndex == 3) {
				messages.add("Lvls Gained: " + totalFLLvlsGained + " | "
						+ taskFLLvlsGained + " (Task)");
				messages.add("Exp Gained: " + totalFLExpGained + " | "
						+ taskFLExpGained + " (Task)");
				messages.add("Exp to Lvl " + (ctx.skills.level(fletching) + 1)
						+ ": " + FLExpToLvl + " | " + taskFLExpHr + " P/H");
				if ((optionSelection != null) && (optionSelection != "")) {
					messages.add(optionSelection + "'s to Lvl "
							+ (ctx.skills.level(fletching) + 1) + ": "
							+ logsToLevelFL + " | "
							+ ((taskCraftedLogs * 3600000) / taskRuntime)
							+ " P/H");
				}
				messages.add("TTL " + FLTTL);
				if ((optionSelection != null)
						&& optionSelection.equalsIgnoreCase("Arrow Shaft")) {
					messages.add("Logs Fletched: " + totalCraftedLogs + " | "
							+ (taskCraftedLogs / m.shaftsPerLog(treeSelection))
							+ " (Task)");
				} else {
					messages.add("Logs Fletched: " + totalCraftedLogs + " | "
							+ taskCraftedLogs + " (Task)");
				}
			}
			if (showPaint) {
				for (int i = 1; i < (messages.size() + 1); i++) {
					g.drawString(messages.get(i - 1), 30, 35 + (i * stepSize));
				}
				g.drawString(revisionString, 225, 26);
			} else {
				g.drawImage(showPaintImage, (int) closeBox.x, (int) closeBox.y,
						null);
			}
		}
	}

	@Override
	public void start() {
		setScriptContext(ctx);
		try {
			bg = ImageIO.read(getClass().getResourceAsStream(
					"CBFMultitaskerBG.png"));
			showPaintImage = ImageIO.read(getClass().getResourceAsStream(
					"showPaintButton.png"));
		} catch (final Exception e) {
			e.printStackTrace();
		}
		final CBFGui gui = new CBFGui();
		gui.setVisible(true);
		while (gui.isVisible()) {
			try {
				Thread.sleep(500);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
		startWCExp = ctx.skills.experience(woodcutting);
		startFMExp = ctx.skills.experience(firemaking);
		startFLExp = ctx.skills.experience(fletching);
		startWCLvl = ctx.skills.level(woodcutting);
		startFMLvl = ctx.skills.level(firemaking);
		startFLLvl = ctx.skills.level(fletching);
		scriptStartTime = System.nanoTime();
	}

	@Override
	public void stop() {
		ctx.game.logout(true);
		System.out.println("Logging out...");
		System.out.println("Total Runtime: " + formatTime(totalRuntime));
	}

	public int taskProgress() {
		long credits = 0;
		if (conditionType.equalsIgnoreCase("Woodcutting Lvl")) {
			credits = ctx.skills.experience(woodcutting);
			final int WCExperienceAtReqLevel = ctx.skills
					.experienceAt(conditionAmount);
			final int WCExperienceAtMyLevel = (int) taskStartWCExp;
			final int WCExpReq = WCExperienceAtReqLevel - WCExperienceAtMyLevel;
			return ((100 - ((int) (WCExpReq * 100D) / WCExpReq)));
		} else if (conditionType.equalsIgnoreCase("Firemaking Lvl")) {
			credits = currentFiremakingLevel;
			final int FMExperienceAtReqLevel = ctx.skills
					.experienceAt(conditionAmount);
			final int FMExperienceAtMyLevel = (int) taskStartFMExp;
			final int FMExpReq = FMExperienceAtReqLevel - FMExperienceAtMyLevel;
			return ((100 - ((int) (FMExpReq * 100D) / FMExpReq)));
		} else if (conditionType.equalsIgnoreCase("Fletching Lvl")) {
			credits = currentFletchingLevel;
			final int FLExperienceAtReqLevel = ctx.skills
					.experienceAt(conditionAmount);
			final int FLExperienceAtMyLevel = (int) taskStartFLExp;
			final int FLExpReq = FLExperienceAtReqLevel - FLExperienceAtMyLevel;
			return ((100 - ((int) (FLExpReq * 100D) / FLExpReq)));
		} else if (conditionType.equalsIgnoreCase("Gained WC Exp")) {
			credits = taskWCExpGained;
		} else if (conditionType.equalsIgnoreCase("Gained FM Exp")) {
			credits = taskFMExpGained;
		} else if (conditionType.equalsIgnoreCase("Gained FL Exp")) {
			credits = taskFLExpGained;
		} else if (conditionType.equalsIgnoreCase("Collected Amount")) {
			credits = taskCollectedLogs;
		} else if (conditionType.equalsIgnoreCase("Burned Amount")) {
			credits = taskBurnedLogs;
		} else if (conditionType.equalsIgnoreCase("Fletched Amount")) {
			credits = taskCraftedLogs;
		} else if (conditionType.equalsIgnoreCase("Task Runtime (Minutes)")) {
			credits = TimeUnit.NANOSECONDS
					.toMinutes((System.nanoTime() - taskStartTime));
		}
		return ((100 - ((int) ((conditionAmount - credits) * 100D) / conditionAmount)));
	}

}