package Gathering.Tasks;

import Gathering.Task;
import Gathering.Walker;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class Walk extends Task {

    public final static Tile[] musaPoint = {new Tile(2925, 3179, 0), new Tile(2925, 3175, 0), new Tile(2925, 3171, 0), new Tile(2923, 3167, 0), new Tile(2923, 3163, 0), new Tile(2920, 3160, 0), new Tile(2917, 3157, 0), new Tile(2916, 3153, 0), new Tile(2920, 3153, 0), new Tile(2924, 3152, 0), new Tile(2928, 3152, 0), new Tile(2932, 3151, 0), new Tile(2937, 3147, 0), new Tile(2941, 3146, 0), new Tile(2945, 3146, 0), new Tile(2949, 3146, 0), new Tile(2953, 3146, 0), new Tile(6656, 6784, 0), new Tile(3032, 3217, 1), new Tile(3029, 3217, 0), new Tile(3027, 3221, 0), new Tile(3027, 3225, 0), new Tile(3027, 3229, 0), new Tile(3027, 3233, 0), new Tile(3030, 3236, 0), new Tile(3034, 3236, 0), new Tile(3038, 3236, 0), new Tile(3042, 3236, 0), new Tile(3046, 3235, 0)};

    public static final Tile[] draynorWillow = {new Tile(3092, 3242, 0), new Tile(3092, 3246, 0), new Tile(3088, 3247, 0), new Tile(3087, 3243, 0), new Tile(3087, 3239, 0), new Tile(3087, 3235, 0)};
    public static final Tile[] edgeVilleYew = {new Tile(3094, 3491, 0), new Tile(3090, 3490, 0), new Tile(3090, 3486, 0), new Tile(3093, 3483, 0), new Tile(3093, 3478, 0), new Tile(3094, 3474, 0), new Tile(3092, 3470, 0), new Tile(3089, 3473, 0), new Tile(3087, 3477, 0)};
    public static final Tile[] lumbridgeOak = {new Tile(3209, 3220, 2), new Tile(3206, 3217, 2), new Tile(3206, 3213, 2), new Tile(3205, 3209, 2), new Tile(3206, 3208, 1), new Tile(3206, 3208, 0), new Tile(3210, 3209, 0), new Tile(3214, 3210, 0), new Tile(3215, 3214, 0), new Tile(3215, 3218, 0), new Tile(3219, 3218, 0), new Tile(3221, 3214, 0), new Tile(3220, 3210, 0), new Tile(3217, 3207, 0)};
    public static final Tile[] lumbridgeNormal = {new Tile(3209, 3220, 2), new Tile(3206, 3217, 2), new Tile(3206, 3213, 2), new Tile(3205, 3209, 2), new Tile(3206, 3208, 1), new Tile(3206, 3208, 0), new Tile(3208, 3212, 0), new Tile(3204, 3214, 0), new Tile(3202, 3218, 0), new Tile(3198, 3218, 0), new Tile(3194, 3216, 0), new Tile(3190, 3216, 0), new Tile(3186, 3216, 0), new Tile(3182, 3216, 0), new Tile(3178, 3215, 0), new Tile(3174, 3215, 0), new Tile(3169, 3218, 0), new Tile(3165, 3218, 0), new Tile(3161, 3218, 0), new Tile(3159, 3222, 0), new Tile(3154, 3223, 0), new Tile(3150, 3222, 0)};

    public static final Tile[] lumbridgeEastMine = {new Tile(3208, 3220, 2), new Tile(3206, 3216, 2), new Tile(3206, 3212, 2), new Tile(3206, 3208, 1), new Tile(3206, 3208, 0), new Tile(3210, 3209, 0), new Tile(3214, 3210, 0), new Tile(3215, 3215, 0), new Tile(3218, 3218, 0), new Tile(3222, 3218, 0), new Tile(3227, 3218, 0), new Tile(3231, 3218, 0), new Tile(3232, 3214, 0), new Tile(3234, 3210, 0), new Tile(3235, 3204, 0), new Tile(3239, 3202, 0), new Tile(3241, 3197, 0), new Tile(3243, 3193, 0), new Tile(3244, 3189, 0), new Tile(3242, 3185, 0), new Tile(3242, 3180, 0), new Tile(3241, 3176, 0), new Tile(3238, 3171, 0), new Tile(3238, 3167, 0), new Tile(3238, 3163, 0), new Tile(3238, 3159, 0), new Tile(3236, 3155, 0), new Tile(3233, 3151, 0), new Tile(3229, 3149, 0), new Tile(3226, 3146, 0)};
    public static final Tile[] varrockMine = {new Tile(3285, 3365, 0), new Tile(3284, 3370, 0), new Tile(3287, 3374, 0), new Tile(3290, 3377, 0), new Tile(3290, 3381, 0), new Tile(3290, 3385, 0), new Tile(3290, 3389, 0), new Tile(3290, 3394, 0), new Tile(3290, 3398, 0), new Tile(3290, 3403, 0), new Tile(3288, 3409, 0), new Tile(3284, 3413, 0), new Tile(3281, 3417, 0), new Tile(3279, 3421, 0), new Tile(3276, 3424, 0), new Tile(3272, 3426, 0), new Tile(3267, 3427, 0), new Tile(3262, 3427, 0), new Tile(3258, 3428, 0), new Tile(3254, 3425, 0), new Tile(3252, 3421, 0)};
    public static final Tile[] faladorMine = {new Tile(3012, 3355, 0), new Tile(3013, 3359, 0), new Tile(3017, 3359, 0), new Tile(3021, 3359, 0), new Tile(3025, 3359, 0), new Tile(3030, 3359, 0), new Tile(3034, 3360, 0), new Tile(3038, 3360, 0), new Tile(3042, 3360, 0), new Tile(3047, 3360, 0), new Tile(3052, 3361, 0), new Tile(3057, 3364, 0), new Tile(3060, 3367, 0), new Tile(3060, 3371, 0), new Tile(3061, 3376, 0), new Tile(3058, 9776, 0), new Tile(3054, 9776, 0), new Tile(3050, 9775, 0), new Tile(3046, 9776, 0)};
    public static final Tile[] barbarianVillageMine = {new Tile(3185, 3436, 0), new Tile(3182, 3432, 0), new Tile(3178, 3432, 0), new Tile(3174, 3431, 0), new Tile(3169, 3429, 0), new Tile(3165, 3426, 0), new Tile(3161, 3424, 0), new Tile(3157, 3424, 0), new Tile(3154, 3421, 0), new Tile(3150, 3419, 0), new Tile(3146, 3418, 0), new Tile(3142, 3417, 0), new Tile(3138, 3417, 0), new Tile(3134, 3417, 0), new Tile(3130, 3417, 0), new Tile(3126, 3417, 0), new Tile(3122, 3418, 0), new Tile(3118, 3419, 0), new Tile(3114, 3420, 0), new Tile(3110, 3420, 0), new Tile(3105, 3420, 0), new Tile(3101, 3421, 0), new Tile(3096, 3421, 0), new Tile(3092, 3420, 0), new Tile(3088, 3420, 0), new Tile(3084, 3422, 0), new Tile(3080, 3423, 0)};


    private final Walker walker = new Walker(ctx);
    private String location;

    public Walk(ClientContext ctx, String location) {
        super(ctx);
        this.location = location;
    }

    @Override
    public boolean activate() {
        System.out.println("Walking");
        if(location.equals("Musa point")) {
            return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() <= 2 && musaPoint[0].distanceTo(ctx.players.local()) > 5);
        } else if(location.equals("Draynor")) {
            return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() < 1 && draynorWillow[draynorWillow.length - 1].distanceTo(ctx.players.local()) > 5);
        } else if(location.equals("Edgeville")) {
            return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() < 1 && edgeVilleYew[edgeVilleYew.length - 1].distanceTo(ctx.players.local()) > 7);
        } else if(location.equals("Lumbridge Oak")) {
            return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() < 1 && lumbridgeOak[lumbridgeOak.length - 1].distanceTo(ctx.players.local()) > 7);
        } else if(location.equals("Lumbridge Normal")) {
            return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() < 1 && lumbridgeNormal[lumbridgeNormal.length - 1].distanceTo(ctx.players.local()) > 7);
        } else if(location.equals("Lumbridge East Mine")) {
            return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() < 1 && lumbridgeEastMine[lumbridgeEastMine.length - 1].distanceTo(ctx.players.local()) > 7);
        } else if(location.equals("Varrock Mine")) {
            return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() < 1 && varrockMine[0].distanceTo(ctx.players.local()) > 7);
        } else if(location.equals("Falador Mine")) {
            return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() < 1 && faladorMine[faladorMine.length - 1].distanceTo(ctx.players.local()) > 7);
        } else if(location.equals("Barbarian Village Mine")) {
            return ctx.inventory.select().count() == 28 || (ctx.inventory.select().count() < 1 && barbarianVillageMine[barbarianVillageMine.length - 1].distanceTo(ctx.players.local()) > 7);
        }
        return false;
    }

    @Override
    public void execute() {
        if(!ctx.players.local().inMotion() || ctx.movement.destination().equals(Tile.NIL) || ctx.movement.destination().distanceTo(ctx.players.local()) < 5) {
            if(location.equals("Musa point")) {
                if(ctx.inventory.select().count() == 28) {
                    walker.walkPath(musaPoint);
                } else {
                    walker.walkPathReverse(musaPoint);
                }
            } else if(location.equals("Draynor Willow")) {
                if(ctx.inventory.select().count() == 28) {
                    walker.walkPathReverse(draynorWillow);
                } else {
                    walker.walkPath(draynorWillow);
                }
            } else if(location.equals("Edgeville Yew")) {
                if(ctx.inventory.select().count() == 28) {
                    walker.walkPathReverse(edgeVilleYew);
                } else {
                    walker.walkPath(edgeVilleYew);
                }
            } else if(location.equals("Lumbridge Oak")) {
                if(ctx.inventory.select().count() == 28) {
                    walker.walkPathReverse(lumbridgeOak);
                } else {
                    walker.walkPath(lumbridgeOak);
                }
            } else if(location.equals("Lumbridge Normal")) {
                if(ctx.inventory.select().count() == 28) {
                    walker.walkPathReverse(lumbridgeNormal);
                } else {
                    walker.walkPath(lumbridgeNormal);
                }
            } else if(location.equals("Lumbridge East Mine")) {
                if(ctx.inventory.select().count() == 28) {
                    walker.walkPathReverse(lumbridgeEastMine);
                } else {
                    walker.walkPath(lumbridgeEastMine);
                }
            } else if(location.equals("Varrock Mine")) {
                if(ctx.inventory.select().count() == 28) {
                    walker.walkPath(varrockMine);
                } else {
                    walker.walkPathReverse(varrockMine);
                }
            } else if(location.equals("Falador Mine")) {
                if(ctx.inventory.select().count() == 28) {
                    walker.walkPathReverse(faladorMine);
                } else {
                    walker.walkPath(faladorMine);
                }
            } else if(location.equals("Barbarian Village Mine")) {
                if(ctx.inventory.select().count() == 28) {
                    walker.walkPathReverse(barbarianVillageMine);
                } else {
                    walker.walkPath(barbarianVillageMine);
                }
            }
        }
    }
}
