package Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class Mind extends Craft{
	
	final static int MIND_ALTER_R = 2453;
	final static int MIND_ALTER_I = 2479;
	final static int PORTAL = 2466;
	final static int BANK = 11758;
	final static int MIND_RUNE =  558;
	final static int ECCENCE = 7936;
	public static int RUNES_MADE = 0;
	public static String STATUS = "";
	
	public Mind(ClientContext ctx) {
		super(ctx);
	}
	
	@Override
	public boolean activate() {
		return true;
	}
	
	@Override
	public void execute() 
	{
		Tile mind_a = new Tile(2980, 3513, 0); // area check if next to alter
		Tile mind_a1 = new Tile(2986, 3519, 0);
		Tile mind_a2 = new Tile(2976, 3509, 0);
		Area alter_area = new Area(mind_a1, mind_a2);
		
		//Tile mind_I = new Tile(2793, 4829, 0); // area for place inside alter to check if in alter
		Tile mind_I1 = new Tile(2795, 4825, 0);
		Tile mind_I2 = new Tile(2780, 4846, 0);
		Area alter_inside = new Area(mind_I1, mind_I2);
		
		Tile mind_b = new Tile(2946, 3370, 0); // area for inside bank
		Tile mind_b1 = new Tile(2942, 3374);
		Tile mind_b2 = new Tile(2950, 3364, 0);
		Area bank_area = new Area(mind_b1, mind_b2);
		
		boolean full = ctx.backpack.select().count() == 28;
		boolean in_ruin_area = alter_area.contains(ctx.players.local().tile());
		boolean in_alter_inside = alter_inside.contains(ctx.players.local().tile());
		boolean in_bank = bank_area.contains(ctx.players.local().tile());
		
		if (full && !in_ruin_area && !in_alter_inside)
		{
			STATUS = "Walking Alter";
			walk_alter(mind_a);
		}
		if (full && !in_alter_inside && in_ruin_area)
		{
			STATUS = "Entering Alter";
			enter_alter(MIND_ALTER_R);
		}
		if (full && in_alter_inside)
		{
			STATUS = "Crafting Runes";
			RUNES_MADE += craft(MIND_ALTER_I, MIND_RUNE);
		}
		if (!full && in_alter_inside)
		{
			STATUS = "Leaving Alter";
			leave(PORTAL);
		}
		if (!full && !in_bank)
		{
			STATUS = "Walking Bank";
			go_bank(mind_b);
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
