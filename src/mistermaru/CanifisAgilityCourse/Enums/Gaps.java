package mistermaru.CanifisAgilityCourse.Enums;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

public enum Gaps {
	
	GAP1 (10820, new Area(new Tile(3505,3497,2), new Tile(3509, 3492, 2))),
	GAP2 (10821, new Area(new Tile(3503,3504,2), new Tile(3498, 3506, 2))),
	GAP3 (10828, new Area(new Tile(3493,3505,2), new Tile(3487, 3499, 2))),
	GAP4 (10822, new Area(new Tile(3475,3500,3), new Tile(3480, 3492, 3))),
	GAP5 (10823, new Area(new Tile(3487,3478,3), new Tile(3504, 3468, 3))),
	GAP6 (10832, new Area(new Tile(3509,3475,2), new Tile(3515, 3482, 2)));
	
	private final int gapID;
	private final Area roofArea;
	
    Gaps(int gapID, Area roofArea) {
        this.gapID = gapID;
        this.roofArea = roofArea;
    }

	public int getGapID() {
		return gapID;
	}

	public Area getRoofArea() {
		return roofArea;
	}
      
}
