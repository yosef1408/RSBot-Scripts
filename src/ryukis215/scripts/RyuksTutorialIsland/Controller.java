package scripts.RyuksTutorialIsland;

import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.Chat;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Player;
import org.powerbot.script.rt4.Varpbits;
import org.powerbot.script.rt4.Widgets;

@Script.Manifest(name = "Ryuks Tutorial Island", description = "Completes the tutorial island.", properties = "author:ryukis215; topic=1334928; client=4;")
public class Controller extends PollingScript<ClientContext> {

	final Varpbits v = ctx.varpbits;
	final Widgets w = ctx.widgets;
	final Chat c = ctx.chat;
	final Player player = ctx.players.local();
	final static Monitor monitor = new Monitor(); 
	final static Tasks tasks = new Tasks(); 
	final static Utilities utils = new Utilities(); 
		
	@Override
	public void poll() {
		
		monitor.monitorFlashingIcons();
		monitor.monitorChat();	
		monitor.monitorStage();
			
		final Status.State state = Status.getState(ctx);
		if (state == null)
			return;
		

		switch (state) {
		case GETTING_STARTED:
				tasks.getStartedTask();
			break;
		case WC_FIRE:
				tasks.woodcutFireTask();
		case FISH_COOK:
				tasks.fishCookTask();
			break;
		case COOKING:
				tasks.cookingTask();
			break;
		case OPEN_COOK_DOOR:
				tasks.openCookDoorTask();
			break;
		case EMOTE_RUN:
				tasks.emoteRunTask();
			break;
		case QUEST:
				tasks.questTask();
			break;
		case MINING:
				tasks.miningTask();
			break;
		case SMITHING:
				tasks.smithingTask();
			break;
		case EQUIPMENT:
				tasks.equipmentTask();
			break;
		case MELEE:
				tasks.meleeTask();
			break;
		case RANGE:
				tasks.rangeTask();
			break;
		case BANK:
				tasks.bankTask();
			break;
		case FINANCE:
				tasks.financeTask();
			break;
		case PRAYER:
				tasks.prayerTask();
			break;
		case OPEN_PRAYER_DOOR:
				tasks.openPrayerDoorTask();
			break;
		case MAGE_TALK:
				tasks.mageTalkTask();
			break;
		case MAGE_CAST:
				tasks.mageCastTask();
			break;
		case DEBUG:
			break;
		}
	}

}
