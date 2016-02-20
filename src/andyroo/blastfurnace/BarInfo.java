package andyroo.blastfurnace;


public class BarInfo {
    /**
     * VARPBIT
     */
    public static final int ORE_INFO_VARPBIT1 = 547;
    public static final int ORE_INFO_VARPBIT2 = 548; // ore info for adamantite + runite
    public static final int BAR_INFO_VARPBIT1 = 545;
    public static final int BAR_INFO_VARPBIT2 = 546; // bar info for adamantite + runite

    private static final int IRON_MASK = 0x1F0000;
    private static final int IRON_SHIFT = 4 * 4;
    private static final int MITHRIL_MASK = 0x1F000000;
    private static final int MITHRIL_SHIFT = 6 * 4;
    private static final int ADAMANTITE_MASK = 0x1F; // ore
    private static final int ADAMANTITE_SHIFT = 0;

    /**
     * WIDGETS
     */
    private static final int BRONZE_BAR_COMPONENT = 108;
    private static final int IRON_BAR_COMPONENT = 109;
    private static final int STEEL_BAR_COMPONENT = 110;
    private static final int MITHRIL_BAR_COMPONENT = 111;
    private static final int ADAMANTITE_BAR_COMPONENT = 112;
    private static final int RUNITE_BAR_COMPONENT = 113;
    private static final int SILVER_BAR_COMPONENT = 114;
    private static final int GOLD_BAR_COMPONENT = 115;

    /**
     * MEMBER VARIABLES
     */
    private BlastFurnace.BAR barType;
    private int ratio;
    private BlastFurnace.ORE primary;
    private int oreVarpbit;
    private int barVarpbit;
    private int primaryMask;
    private int primaryShift;
    private int widgetComponent;

    /********************************/


    public BarInfo(BlastFurnace.BAR barType) {
        this.barType = barType;

        if (barType == BlastFurnace.BAR.STEEL) {
            ratio = 1;
            primary = BlastFurnace.ORE.IRON;
            oreVarpbit = ORE_INFO_VARPBIT1;
            barVarpbit = BAR_INFO_VARPBIT1;
            primaryMask = IRON_MASK;
            primaryShift = IRON_SHIFT;
            widgetComponent = STEEL_BAR_COMPONENT;
        }
        if (barType == BlastFurnace.BAR.MITHRIL) {
            ratio = 2;
            primary = BlastFurnace.ORE.MITHRIL;
            oreVarpbit = ORE_INFO_VARPBIT1;
            barVarpbit = BAR_INFO_VARPBIT1;
            primaryMask = MITHRIL_MASK;
            primaryShift = MITHRIL_SHIFT;
            widgetComponent = MITHRIL_BAR_COMPONENT;
        }
        if (barType == BlastFurnace.BAR.ADAMANTITE) {
            ratio = 3;
            primary = BlastFurnace.ORE.ADAMANTITE;
            oreVarpbit = ORE_INFO_VARPBIT2;
            barVarpbit = BAR_INFO_VARPBIT2;
            primaryMask = ADAMANTITE_MASK;
            primaryShift = ADAMANTITE_SHIFT;
            widgetComponent = ADAMANTITE_BAR_COMPONENT;
        }
        if (barType == BlastFurnace.BAR.RUNITE) {
            ratio = 4;
            primary = BlastFurnace.ORE.RUNITE;
            oreVarpbit = ORE_INFO_VARPBIT2;
            barVarpbit = BAR_INFO_VARPBIT2;
            primaryMask = 0;
            primaryShift = 0;
            widgetComponent = RUNITE_BAR_COMPONENT;
        }
    }

    public BlastFurnace.BAR getBarType() {
        return barType;
    }

    public int getRatio() {
        return ratio;
    }

    public BlastFurnace.ORE getPrimary() {
        return primary;
    }

    public int getOreVarpbit() {
        return oreVarpbit;
    }

    public int getBarVarpbit() {
        return barVarpbit;
    }

    public int getPrimaryMask() {
        return primaryMask;
    }

    public int getPrimaryShift() {
        return primaryShift;
    }

    public int getWidgetComponent() {
        return widgetComponent;
    }

    public String toString() {
        return barType.toString();
    }
}
