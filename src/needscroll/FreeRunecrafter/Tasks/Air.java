package needscroll.FreeRunecrafter.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class Air extends Craft{

	public Air(ClientContext ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	final static int AIR_ALTER_R = 2452;
	final static int AIR_ALTER_I = 2478;
	final static int PORTAL = 2465;
	final static int BANK = 782;
	final static int ECCENCE = 7936;
	final static int AIR_RUNE = 556;
	public static int RUNES_MADE = 0;
	public static String STATUS = "";

	@Override
	public boolean activate() {
		return true;
	}

	@Override
	public void execute() 
	{
		Tile air_a = new Tile(3131, 3408, 0); // area check if next to alter
		Tile air_a1 = new Tile(3135, 3412, 0);
		Tile air_a2 = new Tile(3124, 3401, 0);
		Area alter_area = new Area(air_a1, air_a2);
		
		//Tile air_I = new Tile(2841, 4830, 0); // area for place inside alter to check if in alter
		Tile air_I1 = new Tile(2851, 4840, 0);
		Tile air_I2 = new Tile(2831, 4820, 0);
		Area alter_inside = new Area(air_I1, air_I2);
		
		Tile air_b = new Tile(3186, 3435, 0); // area for inside bank
		Tile air_b1 = new Tile(3191, 3440, 0);
		Tile air_b2 = new Tile(3181, 3430, 0);
		Area bank_area = new Area(air_b1, air_b2);
		
		boolean full = ctx.backpack.select().count() == 28;
		boolean in_ruin_area = alter_area.contains(ctx.players.local().tile());
		boolean in_alter_inside = alter_inside.contains(ctx.players.local().tile());
		boolean in_bank = bank_area.contains(ctx.players.local().tile());
		
		if (full && !in_ruin_area && !in_alter_inside)
		{
			walk_alter(air_a);
			STATUS = "Walking Alter";
		}
		if (full && !in_alter_inside && in_ruin_area)
		{
			enter_alter(AIR_ALTER_R);
			STATUS = "Entering Alter";
		}
		if (full && in_alter_inside)
		{
			RUNES_MADE += craft(AIR_ALTER_I, AIR_RUNE);
			STATUS = "Crafting Runes";
		}
		if (!full && in_alter_inside)
		{
			leave(PORTAL);
			STATUS = "Leaving Alter";
		}
		if (!full && !in_bank)
		{
			go_bank(air_b);
			STATUS = "Walking Bank";
		}
		if (!full && in_bank)
		{
			get_items(BANK);
			STATUS = "Banking";
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
