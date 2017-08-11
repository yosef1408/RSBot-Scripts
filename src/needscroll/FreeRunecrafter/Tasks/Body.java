package needscroll.FreeRunecrafter.Tasks;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class Body extends Craft{
	
	final static int BODY_ALTER_R = 2457;
	final static int BODY_ALTER_I = 2483;
	final static int PORTAL = 2470;
	//final static int[] BANK = {42377, 42378, 42217};
	final static int BANK = 42217;
	final static int BODY_RUNE = 559;
	final static int ECCENCE = 7936;
	public static int RUNES_MADE = 0;
	public static String STATUS = "";
	
	public Body(ClientContext ctx) {
		super(ctx);
	}
	
	@Override
	public boolean activate() {
		return true;
	}
	
	@Override
	public void execute() 
	{
		Tile body_a = new Tile(3055, 3444, 0); // area check if next to alter
		Tile body_a1 = new Tile(3048, 3450, 0);
		Tile body_a2 = new Tile(3060, 3438, 0);
		Area alter_area = new Area(body_a1, body_a2);
		
		//Tile body_I = new Tile(2521, 4847, 0); // area for place inside alter to check if in alter
		Tile body_I1 = new Tile(2512, 4855, 0);
		Tile body_I2 = new Tile(2528, 4836, 0);
		Area alter_inside = new Area(body_I1, body_I2);
		
		Tile body_b = new Tile(3094, 3494, 0); // area for inside bank
		Tile body_b1 = new Tile(3089, 3501, 0);
		Tile body_b2 = new Tile(3099, 3486, 0);
		Area bank_area = new Area(body_b1, body_b2);
		
		boolean full = ctx.backpack.select().count() == 28;
		boolean in_ruin_area = alter_area.contains(ctx.players.local().tile());
		boolean in_alter_inside = alter_inside.contains(ctx.players.local().tile());
		boolean in_bank = bank_area.contains(ctx.players.local().tile());
		
		if (full && !in_ruin_area && !in_alter_inside)
		{
			STATUS = "Walking Alter";
			walk_alter(body_a);
		}
		if (full && !in_alter_inside && in_ruin_area)
		{
			STATUS = "Entering Alter";
			enter_alter(BODY_ALTER_R);
		}
		if (full && in_alter_inside)
		{
			STATUS = "Crafting Runes";
			RUNES_MADE += craft(BODY_ALTER_I, BODY_RUNE);
		}
		if (!full && in_alter_inside)
		{
			STATUS = "Leaving Alter";
			leave(PORTAL);
		}
		if (!full && !in_bank)
		{
			STATUS = "Walking Bank";
			go_bank(body_b);
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
