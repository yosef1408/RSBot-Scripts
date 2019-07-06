package PMiner;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.TilePath;

public class PMinerConst {
    public static final Tile[] pathToChickens = {new Tile(3221, 3218, 0), new Tile(3225, 3218, 0), new Tile(3229, 3218, 0), new Tile(3232, 3221, 0), new Tile(3232, 3225, 0), new Tile(3232, 3229, 0), new Tile(3229, 3232, 0), new Tile(3226, 3235, 0), new Tile(3224, 3239, 0), new Tile(3221, 3242, 0), new Tile(3221, 3246, 0), new Tile(3219, 3250, 0), new Tile(3219, 3254, 0), new Tile(3216, 3258, 0), new Tile(3215, 3262, 0), new Tile(3213, 3266, 0), new Tile(3213, 3270, 0), new Tile(3214, 3274, 0), new Tile(3211, 3277, 0), new Tile(3207, 3278, 0), new Tile(3203, 3278, 0), new Tile(3199, 3278, 0), new Tile(3195, 3280, 0), new Tile(3191, 3280, 0), new Tile(3187, 3281, 0), new Tile(3184, 3284, 0), new Tile(3180, 3287, 0), new Tile(3180, 3291, 0), new Tile(3177,3296,0)};
   // public static final Tile[] pathToBank1 = {new Tile(3287, 3368, 0), new Tile(3288, 3375, 0), new Tile(3290, 3382, 0), new Tile(3290, 3389, 0), new Tile(3290, 3396, 0), new Tile(3290, 3403, 0), new Tile(3289, 3410, 0), new Tile(3282, 3414, 0), new Tile(3279, 3421, 0), new Tile(3274, 3426, 0), new Tile(3268, 3421, 0), new Tile(3262, 3425, 0), new Tile(3255, 3427, 0), new Tile(3254, 3420, 0)};
    public static final Tile[] pathToEastBank = {new Tile(3287, 3369, 0), new Tile(3287, 3374, 0), new Tile(3290, 3378, 0), new Tile(3290, 3383, 0), new Tile(3290, 3388, 0), new Tile(3290, 3393, 0), new Tile(3290, 3398, 0), new Tile(3290, 3403, 0), new Tile(3290, 3408, 0), new Tile(3290, 3413, 0), new Tile(3290, 3418, 0), new Tile(3285, 3421, 0), new Tile(3280, 3422, 0), new Tile(3276, 3425, 0), new Tile(3271, 3427, 0), new Tile(3266, 3427, 0), new Tile(3261, 3427, 0), new Tile(3256, 3428, 0), new Tile(3254, 3423, 0)};
    public static final Tile[] pathToWestBank = {new Tile(3184,3371,0), new Tile(3181, 3371, 0), new Tile(3182, 3378, 0), new Tile(3181, 3385, 0), new Tile(3180, 3392, 0), new Tile(3175, 3397, 0), new Tile(3172, 3404, 0), new Tile(3172, 3411, 0), new Tile(3172, 3418, 0), new Tile(3172, 3425, 0), new Tile(3179, 3429, 0), new Tile(3182, 3436, 0)};
    public static final Tile[] chickenToClay = {new Tile(3171, 3292, 0), new Tile(3176, 3292, 0), new Tile(3180, 3289, 0), new Tile(3184, 3286, 0), new Tile(3189, 3284, 0), new Tile(3194, 3282, 0), new Tile(3198, 3279, 0), new Tile(3203, 3279, 0), new Tile(3208, 3279, 0), new Tile(3213, 3279, 0), new Tile(3216, 3275, 0), new Tile(3220, 3271, 0), new Tile(3224, 3268, 0), new Tile(3227, 3264, 0), new Tile(3232, 3262, 0), new Tile(3237, 3262, 0), new Tile(3241, 3265, 0), new Tile(3240, 3270, 0), new Tile(3240, 3275, 0), new Tile(3235, 3277, 0), new Tile(3237, 3282, 0), new Tile(3237, 3287, 0), new Tile(3238, 3292, 0), new Tile(3239, 3297, 0), new Tile(3239, 3302, 0), new Tile(3239, 3307, 0), new Tile(3239, 3312, 0), new Tile(3243, 3316, 0), new Tile(3248, 3318, 0), new Tile(3252, 3321, 0), new Tile(3257, 3322, 0), new Tile(3262, 3323, 0), new Tile(3266, 3327, 0), new Tile(3262, 3330, 0), new Tile(3257, 3330, 0), new Tile(3252, 3332, 0), new Tile(3248, 3335, 0), new Tile(3243, 3335, 0), new Tile(3238, 3337, 0), new Tile(3233, 3337, 0), new Tile(3228, 3338, 0), new Tile(3227, 3343, 0), new Tile(3227, 3348, 0), new Tile(3224, 3352, 0), new Tile(3221, 3356, 0), new Tile(3217, 3360, 0), new Tile(3216, 3365, 0), new Tile(3213, 3369, 0), new Tile(3210, 3373, 0), new Tile(3206, 3376, 0), new Tile(3201, 3376, 0), new Tile(3196, 3376, 0), new Tile(3191, 3377, 0), new Tile(3186, 3375, 0), new Tile(3182, 3372, 0)};
    public static final Tile[] EastBankToGE = {new Tile(3254, 3420, 0), new Tile(3254, 3425, 0), new Tile(3250, 3428, 0), new Tile(3245, 3428, 0), new Tile(3240, 3428, 0), new Tile(3235, 3428, 0), new Tile(3230, 3428, 0), new Tile(3225, 3428, 0), new Tile(3220, 3428, 0), new Tile(3216, 3432, 0), new Tile(3211, 3435, 0), new Tile(3207, 3438, 0), new Tile(3203, 3441, 0), new Tile(3199, 3444, 0), new Tile(3194, 3446, 0), new Tile(3190, 3449, 0), new Tile(3186, 3453, 0), new Tile(3181, 3454, 0), new Tile(3177, 3457, 0), new Tile(3173, 3460, 0), new Tile(3170, 3464, 0), new Tile(3166, 3467, 0), new Tile(3166, 3472, 0), new Tile(3166, 3477, 0), new Tile(3166, 3482, 0), new Tile(3166, 3487, 0)};



    public final static int CLAY_ROCK_IDs []= {11362,11363};
   public final static int IRON_ROCK_IDs []= {11364,11365};


    public final static int PICK_IDS [] = {12297,1273,1271,1275};
    public final static int BLACK_PICK = 12297;
    public final static int MITH_PICK = 1273;
    public final static int ADDY_PICK = 1271;
    public  final static int RUNE_PICK = 1275;

    public static int IRON_ORE = 440;





}
