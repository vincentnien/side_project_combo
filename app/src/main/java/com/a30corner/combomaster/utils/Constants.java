package com.a30corner.combomaster.utils;

public class Constants {
	public static final int DEFAULT_SECOND = 5000;

	public static final float HP_ADDITION = 0.1f;
	public static final float ATK_ADDITION = 0.05f;
	public static final float RCV_ADDITION = 0.15f;

	public static final float SECOND_REFRESH = 0.4f;

	public static final float SECOND_DROP = 0.55f;
	public static final float SECOND_REMOVE = 0.45f;
	public static final float SECOND_SWAP = 0.08f;
	public static final float SECOND_COMBO = SECOND_REMOVE / 2f;

	public static final float SECOND_DROP_7x6 = 0.55f;
	public static final float SECOND_DROP_5x4 = 0.55f;
	
	public static final float SECOND_MOVE_HALF = 0.2f;

	public static final int ORB_SIZE = 88;
	public static final int ORB_SIZE_7x6 = 74;
	public static final int ORB_SIZE_5x4 = 106;

	public static final int BANNER_HEIGHT = 55;

	public static int OFFSET_X = 2;
	public static int OFFSET_Y = 500;
	
	public static final String PREF_NAME = "comboMaster";

	public static final int HALF_ORB_SIZE = ORB_SIZE / 2;
	public static final int HALF_ORB_SIZE_7x6 = ORB_SIZE_7x6 / 2;
	public static final int HALF_ORB_SIZE_5x4 = ORB_SIZE_5x4 / 2;

	public static final int MODE_NORMAL = 0;
	public static final int MODE_MULTIPLE = 3;
	public static final int MODE_CHALLENGE = 1;
	public static final int MODE_SPECIAL = 2;
	
	public static final String REPORT_URL = "https://www.facebook.com/pages/%E8%BD%89%E7%8F%A0%E5%A4%A7%E5%B8%AB%E7%A4%BE%E7%BE%A4/835302989824803";
	public static final String TUTORIAL_URL = "https://slides.com/vincentsh/team_tutorial#/";

	public static final String SK_SKILL_LOCK = "skillLocked";
	public static final String SK_DROP_RATE = "dropRate";
	public static final String SK_DROP_RATE_NEG = "dropRateNeg";
	public static final String SK_TIME_CHANGE = "timeChange";
	public static final String SK_TIME_CHANGE_X = "timeChangeX";
	public static final String SK_POWER_UP = "powerUp";
	public static final String SK_LEADER_SWITCH = "leaderSwith";
	public static final String SK_SHIELD = "shield";
	public static final String SK_REDUCE_DEF = "reduceDef";
	public static final String SK_COUNTER_ATTACK = "counterAttack";
	public static final String SK_AWOKEN_LOCK = "awokenLock";
	public static final String SK_DROP_LOCK = "dropLock";
	public static final String SK_CLOUD = "cloud";

	public static final String SK_ADD_COMBO = "addCombo";
	public static final String SK_RCV_UP = "recoveryUp";
	public static final String SK_TURN_RECOVER = "turnRecover";

	public static final String SK_ENHANCE_ORB = "enhanceOrb";
	public static final String SK_NO_DROP = "noDrop";

	public static final String SK_VOID = "void";
	public static final String SK_VOID_ATTR = "voidAttr";

	public static final String SK_PERIOD_CHANGE = "periodChange";

	public static final String SK_LOCK_REMOVE = "lockRemove";
}
