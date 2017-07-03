package scripts.RyuksTutorialIsland;

import org.powerbot.script.rt4.ClientContext;

/**
 * 
 * Getting, setting and determining current status/situation
 * 
 * @author Ryukis215
 *
 */
public class Status {

	public enum State {
		GETTING_STARTED, WC_FIRE, FISH_COOK, COOKING, 
		OPEN_COOK_DOOR, EMOTE_RUN, QUEST, MINING, SMITHING,
		EQUIPMENT, MELEE, RANGE, BANK, FINANCE, PRAYER, 
		OPEN_PRAYER_DOOR, MAGE_TALK, MAGE_CAST, DEBUG
	}
	
	private static boolean debug = false;
	
	public static State getState(ClientContext c){		
		if(debug)
			return State.DEBUG;
		
		if(Monitor.stage == 1)
			return State.GETTING_STARTED;
		if(Monitor.stage == 3)
			return State.WC_FIRE;
		if(Monitor.stage == 4)
			return State.FISH_COOK;
		if(Monitor.stage == 5)
			return State.COOKING;
		if(Monitor.stage == 6)
			return State.OPEN_COOK_DOOR;
		if(Monitor.stage == 7)
			return State.EMOTE_RUN;
		if(Monitor.stage == 8)
			return State.QUEST;
		if(Monitor.stage == 9)
			return State.MINING;
		if(Monitor.stage == 10)
			return State.SMITHING;
		if(Monitor.stage == 11)
			return State.EQUIPMENT;
		if(Monitor.stage == 12)
			return State.MELEE;
		if(Monitor.stage == 13)
			return State.RANGE;//there is no stage 14, its fake
		if(Monitor.stage == 15)
			return State.BANK;
		if(Monitor.stage == 16)
			return State.FINANCE;
		if(Monitor.stage == 17)
			return State.PRAYER;
		if(Monitor.stage == 18)
			return State.OPEN_PRAYER_DOOR;
		if(Monitor.stage == 19)
			return State.MAGE_TALK;
		if(Monitor.stage == 20)
			return State.MAGE_CAST;

		return State.DEBUG;
		
	}
}
