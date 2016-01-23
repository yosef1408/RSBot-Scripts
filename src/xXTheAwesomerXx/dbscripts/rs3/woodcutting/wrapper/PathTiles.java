package xXTheAwesomerXx.dbscripts.rs3.woodcutting.wrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.powerbot.script.Tile;

public class PathTiles {
	
	public static final Map<String, Tile[]> TREE_TO_BANK;
	static {
		final Map<String, Tile[]> valuesByName = new HashMap<String, Tile[]>();

		valuesByName.put("Normal Port Sarim", Tiles.normalPortSarimTreesToBank);
		valuesByName.put("Normal Falador", Tiles.normalFaladorTreesToBank);
		valuesByName.put("Normal Catherby", Tiles.normalCatherbyTreesToBank);
		valuesByName.put("Normal Castle Wars", Tiles.normalCastleWarsTreesToBank);
		valuesByName.put("Oak Port Sarim", Tiles.oakPortSarimTreesToBank);
		valuesByName.put("Oak Falador", Tiles.oakFaladorTreesToBank);
		valuesByName.put("Oak Varrock", Tiles.oakVarrockTreesToBank);
		valuesByName.put("Oak Catherby", Tiles.oakCatherbyTreesToBank);
		valuesByName.put("Oak Castle Wars", Tiles.oakCastleWarsTreesToBank);
		valuesByName.put("Willow Draynor", Tiles.willowDraynorTreesToBank);
		valuesByName.put("Willow Port Sarim", Tiles.willowPortSarimTreesToBank);
		valuesByName.put("Willow Catherby", Tiles.willowCatherbyTreesToBank);
		valuesByName.put("Maple Seers' Village", Tiles.mapleSeersVillageTreesToBank);
		valuesByName.put("Maple McGrubor's Wood", Tiles.mapleMcGruborsTreesToBank);
		//valuesByName.put("Arctic Pine Neitiznot", );
		valuesByName.put("Yew Varrock", Tiles.yewVarrockTreesToBank);
		valuesByName.put("Yew Edgeville", Tiles.yewEdgevilleTreesToBank);
		valuesByName.put("Yew Catherby", Tiles.yewCatherbyTreesToBank);
		valuesByName.put("Ivy Varrock", Tiles.ivyVarrockTreesToBank);
		valuesByName.put("Ivy Falador", Tiles.ivyFaladorTreesToBank);
		valuesByName.put("Ivy Castle Wars", Tiles.ivyCastleWarsTreesToBank);

		TREE_TO_BANK = Collections.unmodifiableMap(valuesByName);
	}
	
	public static final Map<String, Tile[]> LODESTONE_TO_BANK;
	static {
		final Map<String, Tile[]> valuesByName = new HashMap<String, Tile[]>();
		
		valuesByName.put("Normal Port Sarim", Tiles.normalPortSarimLodestoneToBank);
		valuesByName.put("Normal Falador", Tiles.normalFaladorLodestoneToBank);
		valuesByName.put("Normal Catherby", Tiles.normalCatherbyLodestoneToBank);
		valuesByName.put("Normal Castle Wars", Tiles.normalCastleWarsLodestoneToBank);
		valuesByName.put("Oak Port Sarim", Tiles.oakPortSarimLodestoneToBank);
		valuesByName.put("Oak Falador", Tiles.oakFaladorLodestoneToBank);
		valuesByName.put("Oak Varrock", Tiles.oakVarrockLodestoneToBank);
		valuesByName.put("Oak Catherby", Tiles.oakCatherbyLodestoneToBank);
		valuesByName.put("Oak Castle Wars", Tiles.oakCastleWarsLodestoneToBank);
		valuesByName.put("Willow Draynor", Tiles.willowDraynorLodestoneToBank);
		valuesByName.put("Willow Port Sarim", Tiles.willowPortSarimLodestoneToBank);
		valuesByName.put("Willow Catherby", Tiles.willowCatherbyLodestoneToBank);
		valuesByName.put("Maple Seers' Village", Tiles.mapleSeersVillageLodestoneToBank);
		valuesByName.put("Maple McGrubor's Wood", Tiles.mapleMcGruborsLodestoneToBank);
		//valuesByName.put("Arctic Pine Neitiznot", );
		valuesByName.put("Yew Varrock", Tiles.yewVarrockLodestoneToBank);
		valuesByName.put("Yew Edgeville", Tiles.yewEdgevilleLodestoneToBank);
		valuesByName.put("Yew Catherby", Tiles.yewCatherbyLodestoneToBank);
		valuesByName.put("Ivy Varrock", Tiles.ivyVarrockLodestoneToBank);
		valuesByName.put("Ivy Falador", Tiles.ivyFaladorLodestoneToBank);
		valuesByName.put("Ivy Castle Wars", Tiles.ivyCastleWarsLodestoneToBank);
		
		LODESTONE_TO_BANK = Collections.unmodifiableMap(valuesByName);
	}
	
	public static final Map<String, Tile[]> LODESTONE_TO_TREE;
	static {
		final Map<String, Tile[]> valuesByName = new HashMap<String, Tile[]>();
		
		valuesByName.put("Normal Port Sarim", Tiles.normalPortSarimLodestoneToTrees);
		valuesByName.put("Normal Falador", Tiles.normalFaladorLodestoneToTrees);
		valuesByName.put("Normal Catherby", Tiles.normalCatherbyLodestoneToTrees);
		valuesByName.put("Normal Castle Wars", Tiles.normalCastleWarsLodestoneToTrees);
		valuesByName.put("Oak Port Sarim", Tiles.oakPortSarimLodestoneToTrees);
		valuesByName.put("Oak Falador", Tiles.oakFaladorLodestoneToTrees);
		valuesByName.put("Oak Varrock", Tiles.oakVarrockLodestoneToTrees);
		valuesByName.put("Oak Catherby", Tiles.oakCatherbyLodestoneToTrees);
		valuesByName.put("Oak Castle Wars", Tiles.oakCastleWarsLodestoneToTrees);
		valuesByName.put("Willow Draynor", Tiles.willowDraynorLodestoneToTrees);
		valuesByName.put("Willow Port Sarim", Tiles.willowPortSarimLodestoneToTrees);
		valuesByName.put("Willow Catherby", Tiles.willowCatherbyLodestoneToTrees);
		valuesByName.put("Maple Seers' Village", Tiles.mapleSeersVillageLodestoneToTrees);
		valuesByName.put("Maple McGrubor's Wood", Tiles.mapleMcGruborsLodestoneToTrees);
		//valuesByName.put("Arctic Pine Neitiznot", );
		valuesByName.put("Yew Varrock", Tiles.yewVarrockLodestoneToTrees);
		valuesByName.put("Yew Edgeville", Tiles.yewEdgevilleLodestoneToTrees);
		valuesByName.put("Yew Catherby", Tiles.yewCatherbyLodestoneToTrees);
		valuesByName.put("Ivy Varrock", Tiles.ivyVarrockLodestoneToTrees);
		valuesByName.put("Ivy Falador", Tiles.ivyFaladorLodestoneToTrees);
		valuesByName.put("Ivy Castle Wars", Tiles.ivyCastleWarsLodestoneToTrees);
		
		LODESTONE_TO_TREE = Collections.unmodifiableMap(valuesByName);
	}
	
}
