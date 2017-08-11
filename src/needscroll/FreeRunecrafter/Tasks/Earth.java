package Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class Earth extends Craft{
	
	final static int EARTH_ALTER_R = 2455;
	final static int EARTH_ALTER_I = 2481;
	final static int PORTAL = 2468;
	final static int BANK = 782;
	final static int EARTH_RUNE = 557;
	final static int ECCENCE = 7936;
	public static int RUNES_MADE = 0;
	public static String STATUS = "";

	public Earth(ClientContext ctx) {
		super(ctx);
	}
	
	@Override
	public boolean activate() {
		return true;
	}
	
	@Override
	public void execute() 
	{
		Tile earth_a = new Tile(3303, 3475, 0); // area check if next to alter
		Tile earth_a1 = new Tile(3300, 3479, 0);
		Tile earth_a2 = new Tile(3310, 3468, 0);
		Area alter_area = new Area(earth_a1, earth_a2);
		
		//Tile earth_I = new Tile(2657, 4830, 0); // area for place inside alter to check if in alter
		Tile earth_I1 = new Tile(2667, 4845, 0);
		Tile earth_I2 = new Tile(2647, 4815, 0);
		Area alter_inside = new Area(earth_I1, earth_I2);
		
		Tile earth_b = new Tile(3254, 3421, 0); // area for inside bank
		Tile earth_b1 = new Tile(3259, 3426, 0);
		Tile earth_b2 = new Tile(3249, 3416, 0);
		Area bank_area = new Area(earth_b1, earth_b2);
		
		boolean full = ctx.backpack.select().count() == 28;
		boolean in_ruin_area = alter_area.contains(ctx.players.local().tile());
		boolean in_alter_inside = alter_inside.contains(ctx.players.local().tile());
		boolean in_bank = bank_area.contains(ctx.players.local().tile());
		
		if (full && !in_ruin_area && !in_alter_inside)
		{
			STATUS = "Walking Alter";
			walk_alter(earth_a);
		}
		if (full && !in_alter_inside && in_ruin_area)
		{
			STATUS = "Entering Alter";
			enter_alter(EARTH_ALTER_R);
		}
		if (full && in_alter_inside)
		{
			STATUS = "Crafting Runes";
			RUNES_MADE += craft(EARTH_ALTER_I, EARTH_RUNE);
		}
		if (!full && in_alter_inside)
		{
			STATUS = "Leaving Alter";
			leave(PORTAL);
		}
		if (!full && !in_bank)
		{
			STATUS = "Walking Bank";
			go_bank(earth_b);
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
