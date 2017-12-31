package scripts.smithing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class JPSConfiguration
{
    public static final int MIN_ANGLE = 20;
    public static final int MAX_ANGLE = 40;
    public static final int MIN_PITCH = 30;
    public static final int MAX_PITCH = 50;
    public static final int RETRY_COUNT = 3;
    public static final int DEFAULT_WAIT = 300;
    public static final List<Integer> BAR_IDS = Arrays.asList(2359);
    public static final List<Integer> BANK_IDS = Arrays.asList(79036);
    public static final List<String> SMELTER_NAMES = Arrays.asList("Portable forge", "Furnace");
    public static final HashMap<Integer, Integer> ORES = new HashMap<Integer, Integer>() {
        {
            put(453, 16);
            put(447, 4);
        }
    };

    public static final JPSGeneralSmithing.Action STATE_FAILURE_ACTION = () -> { };
}
