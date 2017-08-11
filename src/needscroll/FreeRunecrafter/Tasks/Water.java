package Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class Water extends Craft{
	
	final static int WATER_ALTER_R = 2454;
	final static int WATER_ALTER_I = 2480;
	final static int PORTAL = 2467;
	final static int BANK = 2015;
	final static int WATER_RUNE = 555;
	final static int ECCENCE = 7936;
	public static int RUNES_MADE = 0;
	public static String STATUS = "";

	public Water(ClientContext ctx) {
		super(ctx);
	}
	
	@Override
	public boolean activate() {
		return true;
	}
	
	@Override
	public void execute() 
	{
		Tile water_a = new Tile(3163, 3185, 0); // area check if next to alter
		Tile water_a1 = new Tile(3168, 3190, 0);
		Tile water_a2 = new Tile(3158, 3180, 0);
		Area alter_area = new Area(water_a1, water_a2);
		
		//Tile water_I = new Tile(3493, 4832, 0); // area for place inside alter to check if in alter
		Tile water_I1 = new Tile(3503, 4842, 0);
		Tile water_I2 = new Tile(3483, 4822, 0);
		Area alter_inside = new Area(water_I1, water_I2);
		
		Tile water_b = new Tile(3093, 3243, 0); // area for inside bank
		Tile water_b1 = new Tile(3098, 3248, 0);
		Tile water_b2 = new Tile(3088, 3238, 0);
		Area bank_area = new Area(water_b1, water_b2);
		
		boolean full = ctx.backpack.select().count() == 28;
		boolean in_ruin_area = alter_area.contains(ctx.players.local().tile());
		boolean in_alter_inside = alter_inside.contains(ctx.players.local().tile());
		boolean in_bank = bank_area.contains(ctx.players.local().tile());
		
		if (full && !in_ruin_area && !in_alter_inside)
		{
			STATUS = "Walking Alter";
			walk_alter(water_a);
		}
		if (full && !in_alter_inside && in_ruin_area)
		{
			STATUS = "Entering Alter";
			enter_alter(WATER_ALTER_R);
		}
		if (full && in_alter_inside)
		{
			STATUS = "Crafting Runes";
			RUNES_MADE += craft(WATER_ALTER_I, WATER_RUNE);
		}
		if (!full && in_alter_inside)
		{
			STATUS = "Leaving Alter";
			leave(PORTAL);
		}
		if (!full && !in_bank)
		{
			STATUS = "Walking Bank";
			go_bank(water_b);
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
