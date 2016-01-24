package xXTheAwesomerXx.dbscripts.rs3.woodcutting.wrapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

public class Areas {

	public static final Map<String, Area> BANK_AREA;
	public static final Map<String, Area> OVERALL;

	public static final Map<String, Area> SECD_AREA;
	public static final Map<String, Area> TREE_AREA;

	static {
		final Map<String, Area> valuesByName = new HashMap<String, Area>();
		valuesByName.put("Port Sarim", new Area(new Tile(2904, 3308, 0),
				new Tile(3067, 3308, 0), new Tile(3067, 3279, 0), new Tile(
						3069, 3279, 0), new Tile(3067, 3271, 0), new Tile(3068,
								3257, 0), new Tile(3065, 3255, 0), new Tile(3065, 3143,
										0), new Tile(2960, 3181, 0), new Tile(2905, 3196, 0)));
		valuesByName.put("Draynor", new Area(new Tile(3070, 3382, 0), new Tile(
				3146, 3382, 0), new Tile(3153, 3354, 0),
				new Tile(3161, 3349, 0), new Tile(3171, 3348, 0), new Tile(
						3171, 3277, 0), new Tile(3160, 3267, 0), new Tile(3136,
								3245, 0), new Tile(3136, 3191, 0), new Tile(3065, 3193,
										0)));
		valuesByName.put("Falador", new Area(new Tile(2935, 3403, 0), new Tile(
				2947, 3428, 0), new Tile(3007, 3427, 0),
				new Tile(3068, 3388, 0), new Tile(3068, 3298, 0), new Tile(
						2880, 3296, 0), new Tile(2880, 3373, 0)));
		valuesByName.put("Varrock", new Area(new Tile(3135, 3517, 0), new Tile(
				3208, 3518, 0), new Tile(3327, 3518, 0),
				new Tile(3328, 3487, 0), new Tile(3310, 3487, 0), new Tile(
						3311, 3456, 0), new Tile(3295, 3456, 0), new Tile(3295,
								3382, 0), new Tile(3302, 3382, 0), new Tile(3302, 3359,
										0), new Tile(3310, 3359, 0), new Tile(3311, 3329, 0),
										new Tile(3205, 3329, 0), new Tile(3152, 3357, 0), new Tile(
												3144, 3385, 0), new Tile(3118, 3390, 0), new Tile(3108,
														3409, 0), new Tile(3105, 3420, 0), new Tile(3110, 3434,
																0), new Tile(3105, 3441, 0), new Tile(3102, 3460, 0),
																new Tile(3104, 3483, 0), new Tile(3120, 3495, 0)));
		valuesByName.put("Edgeville", new Area(new Tile(3044, 3518, 0),
				new Tile(3045, 3448, 0), new Tile(3088, 3448, 0), new Tile(
						3089, 3437, 0), new Tile(3108, 3437, 0), new Tile(3105,
								3442, 0), new Tile(3103, 3481, 0), new Tile(3111, 3491,
										0), new Tile(3121, 3492, 0), new Tile(3128, 3505, 0),
										new Tile(3131, 3517, 0), new Tile(3090, 3521, 0)));
		valuesByName.put("Catherby", new Area(new Tile(2775, 3480, 0),
				new Tile(2822, 3480, 0), new Tile(2840, 3456, 0), new Tile(
						2841, 3446, 0), new Tile(2851, 3441, 0), new Tile(2856,
								3436, 0), new Tile(2860, 3434, 0), new Tile(2862, 3434,
										0), new Tile(2862, 3431, 0), new Tile(2863, 3416, 0),
										new Tile(2857, 3408, 0), new Tile(2835, 3408, 0), new Tile(
												2780, 3410, 0), new Tile(2767, 3420, 0), new Tile(2733,
														3420, 0), new Tile(2734, 3437, 0), new Tile(2751, 3437,
																0), new Tile(2751, 3451, 0), new Tile(2776, 3452, 0)));
		valuesByName.put("Seers' Village", new Area(new Tile(2641, 3525, 0),
				new Tile(2663, 3549, 0), new Tile(2677, 3545, 0), new Tile(
						2773, 3543, 0), new Tile(2776, 3451, 0), new Tile(2751,
								3451, 0), new Tile(2752, 3387, 0), new Tile(2643, 3388,
										0)));
		valuesByName.put("McGrubor's Wood", new Area(new Tile(2641, 3525, 0),
				new Tile(2663, 3549, 0), new Tile(2677, 3545, 0), new Tile(
						2773, 3543, 0), new Tile(2776, 3451, 0), new Tile(2751,
								3451, 0), new Tile(2752, 3387, 0), new Tile(2643, 3388,
										0)));
		valuesByName.put("Sorcerer's Tower", new Area(new Tile(2641, 3525, 0),
				new Tile(2663, 3549, 0), new Tile(2677, 3545, 0), new Tile(
						2773, 3543, 0), new Tile(2776, 3451, 0), new Tile(2751,
								3451, 0), new Tile(2752, 3387, 0), new Tile(2643, 3388,
										0)));
		valuesByName.put("Sorcerer's Tower (NW)", new Area(new Tile(2641, 3525,
				0), new Tile(2663, 3549, 0), new Tile(2677, 3545, 0), new Tile(
						2773, 3543, 0), new Tile(2776, 3451, 0),
						new Tile(2751, 3451, 0), new Tile(2752, 3387, 0), new Tile(
								2643, 3388, 0)));
		valuesByName.put("Neitiznot", new Area(new Tile(2301, 3842, 0),
				new Tile(2381, 3841, 0), new Tile(2381, 3819, 0), new Tile(
						2367, 3808, 0), new Tile(2367, 3786, 0), new Tile(2299,
								3769, 0)));
		valuesByName.put("Yanille", new Area(new Tile(2413, 3138, 0), new Tile(
				2635, 3138, 0), new Tile(2637, 3034, 0),
				new Tile(2580, 3035, 0), new Tile(2573, 3042, 0), new Tile(
						2565, 3036, 0), new Tile(2496, 3036, 0), new Tile(2495,
								3054, 0), new Tile(2480, 3055, 0), new Tile(2436, 3056,
										0), new Tile(2318, 3056, 0), new Tile(2327, 3080, 0),
										new Tile(2363, 3080, 0), new Tile(2364, 3140, 0), new Tile(
												2415, 3141, 0)));
		valuesByName.put("Castle Wars", new Area(new Tile(2413, 3138, 0),
				new Tile(2635, 3138, 0), new Tile(2637, 3034, 0), new Tile(
						2580, 3035, 0), new Tile(2573, 3042, 0), new Tile(2565,
								3036, 0), new Tile(2496, 3036, 0), new Tile(2495, 3054,
										0), new Tile(2480, 3055, 0), new Tile(2436, 3056, 0),
										new Tile(2318, 3056, 0), new Tile(2327, 3080, 0), new Tile(
												2363, 3080, 0), new Tile(2364, 3140, 0), new Tile(2415,
														3141, 0)));
		valuesByName.put("Tree Gnome Stronghold", new Area(new Tile(2283, 3709,
				0), new Tile(2398, 3709, 0), new Tile(2408, 3551, 0), new Tile(
						2495, 3553, 0), new Tile(2496, 3336, 0),
						new Tile(2447, 3338, 0), new Tile(2262, 3437, 0)));
		valuesByName.put("Eagles' Peek", new Area(new Tile(2283, 3709, 0),
				new Tile(2398, 3709, 0), new Tile(2408, 3551, 0), new Tile(
						2495, 3553, 0), new Tile(2496, 3336, 0), new Tile(2447,
								3338, 0), new Tile(2262, 3437, 0)));
		valuesByName.put("Tirannwn", new Area(new Tile(2154, 3273, 0),
				new Tile(2324, 3272, 0), new Tile(2328, 3198, 0), new Tile(
						2361, 3197, 0), new Tile(2362, 3142, 0), new Tile(2280,
								3114, 0), new Tile(2137, 3112, 0)));
		valuesByName.put("Prifddinas", new Area(new Tile(2145, 3415, 0),
				new Tile(2302, 3415, 0), new Tile(2303, 3268, 0), new Tile(
						2145, 3267, 0)));

		OVERALL = Collections.unmodifiableMap(valuesByName);
	}
	static {
		final Map<String, Area> valuesByName = new HashMap<String, Area>();
		valuesByName.put("Normal Port Sarim", new Area(new Tile(2972, 3228, 0),
				new Tile(3011, 3228, 0), new Tile(3011, 3211, 0), new Tile(
						3007, 3212, 0), new Tile(3008, 3174, 0), new Tile(2971,
								3174, 0)));
		valuesByName.put("Normal Falador", new Area(new Tile(2944, 3420, 0),
				new Tile(2950, 3420, 0), new Tile(2962, 3412, 0), new Tile(
						2964, 3396, 0), new Tile(2944, 3396, 0)));
		valuesByName.put("Normal Catherby", new Area(new Tile(2777, 3474, 0),
				new Tile(2804, 3474, 0), new Tile(2804, 3433, 0), new Tile(
						2777, 3433, 0)));
		valuesByName.put("Normal Castle Wars", new Area(
				new Tile(2450, 3107, 0), new Tile(2467, 3107, 0), new Tile(
						2472, 3096, 0), new Tile(2472, 3089, 0), new Tile(2467,
								3083, 0), new Tile(2461, 3081, 0), new Tile(2456, 3081,
										0), new Tile(2455, 3082, 0), new Tile(2456, 3089, 0),
										new Tile(2455, 3092, 0), new Tile(2452, 3092, 0)));
		valuesByName.put("Oak Port Sarim", new Area(new Tile(3028, 3276, 0),
				new Tile(3040, 3277, 0), new Tile(3044, 3273, 0), new Tile(
						3067, 3273, 0), new Tile(3067, 3260, 0), new Tile(3032,
								3261, 0), new Tile(3032, 3264, 0), new Tile(3028, 3264,
										0)));
		valuesByName.put("Oak Falador", new Area(new Tile(3009, 3321, 0),
				new Tile(3012, 3321, 0), new Tile(3015, 3326, 0), new Tile(
						3023, 3326, 0), new Tile(3025, 3328, 0), new Tile(3030,
								3328, 0), new Tile(3030, 3314, 0), new Tile(3022, 3314,
										0), new Tile(3021, 3313, 0), new Tile(3009, 3312, 0)));
		valuesByName.put("Oak Varrock", new Area(new Tile(3160, 3424),
				new Tile(3173, 3410)));
		valuesByName.put("Oak Catherby", new Area(new Tile(2777, 3474, 0),
				new Tile(2798, 3474, 0), new Tile(2805, 3455, 0), new Tile(
						2806, 3437, 0), new Tile(2799, 3429, 0), new Tile(2776,
								3432, 0), new Tile(2773, 3452, 0)));
		valuesByName.put("Oak Castle Wars", new Area(new Tile(2450, 3107, 0),
				new Tile(2467, 3107, 0), new Tile(2472, 3096, 0), new Tile(
						2472, 3089, 0), new Tile(2467, 3083, 0), new Tile(2461,
								3081, 0), new Tile(2456, 3081, 0), new Tile(2455, 3082,
										0), new Tile(2456, 3089, 0), new Tile(2455, 3092, 0),
										new Tile(2452, 3092, 0)));
		valuesByName.put("Willow Draynor", new Area(new Tile(3080, 3239),
				new Tile(3092, 3226)));
		valuesByName.put("Willow Port Sarim", new Area(new Tile(3052, 3268),
				new Tile(3067, 3246)));
		valuesByName.put("Willow Catherby", new Area(new Tile(2768, 3420, 0),
				new Tile(2768, 3433, 0), new Tile(2791, 3433, 0), new Tile(
						2792, 3420, 0)));
		valuesByName.put("Maple Seers' Village", new Area(new Tile(2718, 3504),
				new Tile(2736, 3494)));
		valuesByName.put("Maple McGrubor's Wood", new Area(
				new Tile(2669, 3546), new Tile(2688, 3520)));
		// valuesByName.put("Arctic Pine Neitiznot", );
		valuesByName.put("Yew Varrock", new Area(new Tile(3201, 3505),
				new Tile(3225, 3498)));
		valuesByName.put("Yew Edgeville", new Area(new Tile(3085, 3482),
				new Tile(3090, 3468)));
		valuesByName.put("Yew Catherby", new Area(new Tile(2750, 3425, 0),
				new Tile(2751, 3438, 0), new Tile(2765, 3438, 0), new Tile(
						2764, 3454, 0), new Tile(2772, 3454, 0), new Tile(2773,
								3424, 0)));
		valuesByName.put("Ivy Varrock", new Area(new Tile(3201, 3505),
				new Tile(3225, 3498)));
		valuesByName.put("Ivy Falador", new Area(new Tile(3042, 3328),
				new Tile(3054, 3320)));
		valuesByName.put("Ivy Castle Wars", new Area(new Tile(2408, 3066, 0),
				new Tile(2436, 3066, 0), new Tile(2436, 3062, 0), new Tile(
						2408, 3062, 0)));
		TREE_AREA = Collections.unmodifiableMap(valuesByName);
	}

	static {
		final Map<String, Area> valuesByName = new HashMap<String, Area>();
		valuesByName.put("Normal Port Sarim", new Area(new Tile(3042, 3239),
				new Tile(3050, 3233)));
		valuesByName.put("Normal Falador", new Area(new Tile(2943, 3373, 0),
				new Tile(2949, 3368, 0)));
		valuesByName.put("Normal Catherby", new Area(new Tile(2805, 3442, 0),
				new Tile(2813, 3437, 0)));
		valuesByName.put("Normal Castle Wars", new Area(
				new Tile(2437, 3088, 0), new Tile(2448, 3081, 0)));
		valuesByName.put("Oak Port Sarim", new Area(new Tile(3042, 3239),
				new Tile(3050, 3233)));
		valuesByName.put("Oak Falador", new Area(new Tile(3009, 3358),
				new Tile(3023, 3352)));
		valuesByName.put("Oak Varrock", new Area(new Tile(3182, 3446),
				new Tile(3190, 3432)));
		valuesByName.put("Oak Catherby", new Area(new Tile(2805, 3442, 0),
				new Tile(2813, 3437, 0)));
		valuesByName.put("Oak Castle Wars", new Area(new Tile(2437, 3088, 0),
				new Tile(2448, 3081, 0)));
		valuesByName.put("Willow Draynor", new Area(new Tile(3088, 3246),
				new Tile(3097, 3240)));
		valuesByName.put("Willow Port Sarim", new Area(new Tile(3042, 3239),
				new Tile(3050, 3233)));
		valuesByName.put("Willow Catherby", new Area(new Tile(2805, 3442, 0),
				new Tile(2813, 3437, 0)));
		valuesByName.put("Maple Seers' Village", new Area(new Tile(2720, 3494),
				new Tile(2731, 3494), new Tile(2731, 3489),
				new Tile(2728, 3489), new Tile(2728, 3486),
				new Tile(2723, 3486), new Tile(2723, 3489),
				new Tile(2720, 3489)));
		valuesByName.put("Maple McGrubor's Wood", new Area(
				new Tile(2720, 3494), new Tile(2731, 3494),
				new Tile(2731, 3489), new Tile(2728, 3489),
				new Tile(2728, 3486), new Tile(2723, 3486),
				new Tile(2723, 3489), new Tile(2720, 3489)));
		// valuesByName.put("Arctic Pine Neitiznot", );
		valuesByName.put("Yew Varrock", new Area(new Tile(3171, 3511),
				new Tile(3185, 3499)));
		valuesByName.put("Yew Edgeville", new Area(new Tile(3090, 3500),
				new Tile(3099, 3487)));
		valuesByName.put("Yew Catherby", new Area(new Tile(2805, 3442, 0),
				new Tile(2813, 3437, 0)));
		valuesByName.put("Ivy Varrock", new Area(new Tile(3171, 3511),
				new Tile(3185, 3499)));
		valuesByName.put("Ivy Falador", new Area(new Tile(3009, 3358),
				new Tile(3023, 3352)));
		valuesByName.put("Ivy Castle Wars", new Area(new Tile(2437, 3088, 0),
				new Tile(2448, 3081, 0)));
		BANK_AREA = Collections.unmodifiableMap(valuesByName);
	}
	static {
		final Map<String, Area> valuesByName = new HashMap<String, Area>();
		valuesByName.put("Fletch Normal Port Sarim", new Area(new Tile(3043,
				3237), new Tile(3050, 3234)));
		valuesByName.put("Fletch Normal Falador", new Area(new Tile(2942, 3347,
				0), new Tile(2948, 3374, 0), new Tile(2948, 3370, 0), new Tile(
						2950, 3370, 0), new Tile(2950, 3367, 0), new Tile(2942, 3367)));
		valuesByName.put("Fletch Normal Catherby", new Area(new Tile(2805,
				3442, 0), new Tile(2813, 3437, 0)));
		valuesByName.put("Fletch Normal Castle Wars", new Area(new Tile(2437,
				3088, 0), new Tile(2448, 3081, 0)));
		valuesByName.put("Fletch Oak Port Sarim", new Area(
				new Tile(3043, 3237), new Tile(3050, 3234)));
		valuesByName.put("Fletch Oak Falador", new Area(new Tile(3009, 3358),
				new Tile(3023, 3352)));
		valuesByName.put("Fletch Oak Varrock", new Area(new Tile(3182, 3446),
				new Tile(3190, 3432)));
		valuesByName.put("Fletch Oak Catherby", new Area(
				new Tile(2805, 3442, 0), new Tile(2813, 3437, 0)));
		valuesByName.put("Fletch Oak Castle Wars", new Area(new Tile(2437,
				3088, 0), new Tile(2448, 3081, 0)));
		valuesByName.put("Fletch Willow Draynor", new Area(
				new Tile(3088, 3246), new Tile(3097, 3240)));
		valuesByName.put("Fletch Willow Port Sarim", new Area(new Tile(3043,
				3237), new Tile(3050, 3234)));
		valuesByName.put("Fletch Willow Catherby", new Area(new Tile(2805,
				3442, 0), new Tile(2813, 3437, 0)));
		valuesByName.put("Fletch Maple Seers' Village", new Area(new Tile(2720,
				3494), new Tile(2731, 3494), new Tile(2731, 3489), new Tile(
						2728, 3489), new Tile(2728, 3486), new Tile(2723, 3486),
						new Tile(2723, 3489), new Tile(2720, 3489)));
		valuesByName.put("Fletch Maple McGrubor's Wood", new Area(new Tile(
				2720, 3494), new Tile(2731, 3494), new Tile(2731, 3489),
				new Tile(2728, 3489), new Tile(2728, 3486),
				new Tile(2723, 3486), new Tile(2723, 3489),
				new Tile(2720, 3489)));
		// valuesByName.put("Fletch Arctic Pine Neitiznot", );
		valuesByName.put("Fletch Yew Varrock", new Area(new Tile(3171, 3511),
				new Tile(3185, 3499)));
		valuesByName.put("Fletch Yew Edgeville", new Area(new Tile(3090, 3500),
				new Tile(3099, 3487)));
		valuesByName.put("Fletch Yew Catherby", new Area(
				new Tile(2805, 3442, 0), new Tile(2813, 3437, 0)));
		valuesByName.put("Fletch Ivy Varrock", new Area(new Tile(3171, 3511),
				new Tile(3185, 3499)));
		valuesByName.put("Fletch Ivy Falador", new Area(new Tile(3009, 3358),
				new Tile(3023, 3352)));
		valuesByName.put("Fletch Ivy Castle Wars", new Area(new Tile(2437,
				3088, 0), new Tile(2448, 3081, 0)));
		// ---- End Fletch Areas ----
		valuesByName.put("Burn Normal Port Sarim", new Area(
				new Tile(3043, 3237), new Tile(3050, 3234)));
		valuesByName.put("Burn Normal Falador", new Area(
				new Tile(2942, 3347, 0), new Tile(2948, 3374, 0), new Tile(
						2948, 3370, 0), new Tile(2950, 3370, 0), new Tile(2950,
								3367, 0), new Tile(2942, 3367)));
		valuesByName.put("Burn Normal Catherby", new Area(new Tile(2805, 3442,
				0), new Tile(2813, 3437, 0)));
		valuesByName.put("Burn Normal Castle Wars", new Area(new Tile(2805,
				3442, 0), new Tile(2813, 3437, 0)));
		valuesByName.put("Burn Oak Port Sarim", new Area(new Tile(3043, 3237),
				new Tile(3050, 3234)));
		valuesByName.put("Burn Oak Falador", new Area(new Tile(3009, 3358),
				new Tile(3023, 3352)));
		valuesByName.put("Burn Oak Varrock", new Area(new Tile(3182, 3446),
				new Tile(3190, 3432)));
		valuesByName.put("Burn Oak Catherby", new Area(new Tile(2805, 3442, 0),
				new Tile(2813, 3437, 0)));
		valuesByName.put("Burn Oak Castle Wars", new Area(new Tile(2805, 3442,
				0), new Tile(2813, 3437, 0)));
		valuesByName.put("Burn Willow Draynor", new Area(new Tile(3088, 3246),
				new Tile(3097, 3240)));
		valuesByName.put("Burn Willow Port Sarim", new Area(
				new Tile(3043, 3237), new Tile(3050, 3234)));
		valuesByName.put("Burn Willow Catherby", new Area(new Tile(2805, 3442,
				0), new Tile(2813, 3437, 0)));
		valuesByName.put("Burn Maple Seers' Village", new Area(new Tile(2720,
				3494), new Tile(2731, 3494), new Tile(2731, 3489), new Tile(
						2728, 3489), new Tile(2728, 3486), new Tile(2723, 3486),
						new Tile(2723, 3489), new Tile(2720, 3489)));
		valuesByName.put("Burn Maple McGrubor's Wood", new Area(new Tile(2720,
				3494), new Tile(2731, 3494), new Tile(2731, 3489), new Tile(
						2728, 3489), new Tile(2728, 3486), new Tile(2723, 3486),
						new Tile(2723, 3489), new Tile(2720, 3489)));
		// valuesByName.put("Burn Arctic Pine Neitiznot", );
		valuesByName.put("Burn Yew Varrock", new Area(new Tile(3171, 3511),
				new Tile(3185, 3499)));
		valuesByName.put("Burn Yew Edgeville", new Area(new Tile(3090, 3500),
				new Tile(3099, 3487)));
		valuesByName.put("Burn Yew Catherby", new Area(new Tile(2805, 3442, 0),
				new Tile(2813, 3437, 0)));
		valuesByName.put("Burn Ivy Varrock", new Area(new Tile(3171, 3511),
				new Tile(3185, 3499)));
		valuesByName.put("Burn Ivy Falador", new Area(new Tile(3009, 3358),
				new Tile(3023, 3352)));
		valuesByName.put("Burn Ivy Castle Wars", new Area(new Tile(2443, 3088,
				0), new Tile(2447, 3084, 0)));
		// ---- End Burn Areas ----
		SECD_AREA = Collections.unmodifiableMap(valuesByName);
	}

}
