package stormneo7.sorceress.script.summer;

import java.util.Random;

import org.powerbot.script.Tile;

import stormneo7.sorceress.SorceressGarden;
import stormneo7.sorceress.script.Season;
import stormneo7.sorceress.script.summer.guard.SummerGuardA;
import stormneo7.sorceress.script.summer.guard.SummerGuardB;
import stormneo7.sorceress.script.summer.guard.SummerGuardC;
import stormneo7.sorceress.script.summer.guard.SummerGuardD;
import stormneo7.sorceress.script.summer.guard.SummerGuardE;
import stormneo7.sorceress.script.summer.guard.SummerGuardF;
import stormneo7.sorceress.type.Guard;

public class Summer extends Season {

    public Summer(SorceressGarden instance) {
        super(instance);

        this.scriptHerbs = SummerHerbs.class;
        this.scriptFruit = SummerFruits.class;
        this.xp_rate = 3000;
        this.fruit_count = 2;

        this.gate_id = 11987;
        this.gate_bounds = new int[] { 4, 128, -240, -20, 116, 128 };
        this.gate_location = new Tile(2910, 5480);

        this.tree_id = 12943;
        this.herb_id = 4980;

        this.guards = new Guard[] { new SummerGuardA(), new SummerGuardB(), new SummerGuardC(), new SummerGuardD(), new SummerGuardE(), new SummerGuardF() };
    }

    @Override
    public boolean inGarden(Tile tile) {
        return tile.x() >= 2905 && tile.x() <= 2926 && tile.y() >= 5481 && tile.y() <= 5496;
    }

    @Override
    public Tile getRandGateTile(Random r) {
        int randX = (int) Math.round(r.nextGaussian()) * 2;
        int randY = (int) Math.round(r.nextGaussian());
        return new Tile(2910 + randX, 5485 + randY);
    }
}