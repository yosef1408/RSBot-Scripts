package Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class Fire extends Craft{
	
	final static int FIRE_ALTER_R = 2456;
	final static int FIRE_ALTER_I = 2482;
	final static int PORTAL = 2469;
	final static int BANK = 83954;
	final static int FIRE_RUNE = 554;
	final static int ECCENCE = 7936;
	public static int RUNES_MADE = 0;
	public static String STATUS = "";

	public Fire(ClientContext ctx) {
		super(ctx);
	}
	
	@Override
	public boolean activate() {
		return true;
	}
	
	@Override
	public void execute() 
	{
		Tile fire_a = new Tile(3310, 3252, 0); // area check if next to alter
		Tile fire_a1 = new Tile(3320, 3262, 0);
		Tile fire_a2 = new Tile(3300, 3242, 0);
		Area alter_area = new Area(fire_a1, fire_a2);
		
		//Tile fire_I = new Tile(2577, 4846, 0); // area for place inside alter to check if in alter
		Tile fire_I1 = new Tile(2587, 4856, 0);
		Tile fire_I2 = new Tile(2567, 4836, 0);
		Area alter_inside = new Area(fire_I1, fire_I2);
		
		Tile fire_b = new Tile(3347, 3238, 0); // area for inside bank
		Tile fire_b1 = new Tile(3357, 3248, 0);
		Tile fire_b2 = new Tile(3337, 3238, 0);
		Area bank_area = new Area(fire_b1, fire_b2);
		
		boolean full = ctx.backpack.select().count() == 28;
		boolean in_ruin_area = alter_area.contains(ctx.players.local().tile());
		boolean in_alter_inside = alter_inside.contains(ctx.players.local().tile());
		boolean in_bank = bank_area.contains(ctx.players.local().tile());
		
		if (full && !in_ruin_area && !in_alter_inside)
		{
			STATUS = "Walking Alter";
			walk_alter(fire_a);
		}
		if (full && !in_alter_inside && in_ruin_area)
		{
			STATUS = "Entering Alter";
			enter_alter(FIRE_ALTER_R);
		}
		if (full && in_alter_inside)
		{
			STATUS = "Crafting Runes";
			RUNES_MADE += craft(FIRE_ALTER_I, FIRE_RUNE);
		}
		if (!full && in_alter_inside)
		{
			STATUS = "Leaving Alter";
			leave(PORTAL);
		}
		if (!full && !in_bank)
		{
			STATUS = "Walking Bank";
			go_bank(fire_b);
		}
		if (!full && in_bank)
		{
			STATUS = "Banking";
			get_items(BANK);
		}
	}
	
	public int get_runes()
	{
		return RUNES_MADE;
	}
	
	public String get_status()
	{
		return STATUS;
	}
}
