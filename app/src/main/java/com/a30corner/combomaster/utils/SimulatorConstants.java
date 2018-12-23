package com.a30corner.combomaster.utils;

import java.util.HashMap;
import java.util.Map;

import com.a30corner.combomaster.ComboMasterApplication;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.AwokenSkill;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MonsterType;
import com.a30corner.combomaster.playground.entity.Enemy;

public class SimulatorConstants {
	public static final float SECOND_REFRESH = 0.4f;

	public static final float SECOND_DROP = 0.55f;
	public static final float SECOND_REMOVE = 0.45f;
	public static final float SECOND_SWAP = 0.12f;
	public static final float SECOND_COMBO = SECOND_REMOVE / 2f;

	public static final float SECOND_DROP_7x6 = 0.50f;
	public static final float SECOND_DROP_5x4 = 0.50f;
	
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

	public static final String REPORT_URL = "https://www.facebook.com/pages/%E8%BD%89%E7%8F%A0%E5%A4%A7%E5%B8%AB%E7%A4%BE%E7%BE%A4/835302989824803";
	
    private static final Map<MonsterType, AwokenSkill> KILLER_MAP;
	private static final Map<MonsterType, MonsterSkill.MoneyAwokenSkill> KILLER_MAP_SUB;
    static {
    	KILLER_MAP = new HashMap<MonsterType, AwokenSkill>();
    	KILLER_MAP.put(MonsterType.ATTACKER, AwokenSkill.ATTACKER_KILLER);
    	KILLER_MAP.put(MonsterType.AWOKEN, AwokenSkill.AWOKEN_KILLER);
    	KILLER_MAP.put(MonsterType.BALANCED, AwokenSkill.BALANCED_KILLER);
    	KILLER_MAP.put(MonsterType.DEVIL, AwokenSkill.DEVIL_KILLER);
    	KILLER_MAP.put(MonsterType.DRAGON, AwokenSkill.DRAGON_KILLER);
    	KILLER_MAP.put(MonsterType.ENHANCE_MATRIAL, AwokenSkill.ENHANCE_MATRIAL_KILLER);
    	KILLER_MAP.put(MonsterType.EVO_MATRIAL, AwokenSkill.EVO_MATRIAL_KILLER);
    	KILLER_MAP.put(MonsterType.GOD, AwokenSkill.GOD_KILLER);
    	KILLER_MAP.put(MonsterType.HEALER, AwokenSkill.HEALER_KILLER);
    	KILLER_MAP.put(MonsterType.MACHINE, AwokenSkill.MACHINE_KILLER);
    	KILLER_MAP.put(MonsterType.PHYSICAL, AwokenSkill.PHYSICAL_KILLER);
		KILLER_MAP.put(MonsterType.SELL, AwokenSkill.SELL_KILLER);

		KILLER_MAP_SUB = new HashMap<MonsterType, MonsterSkill.MoneyAwokenSkill>();
		KILLER_MAP_SUB.put(MonsterType.ATTACKER, MonsterSkill.MoneyAwokenSkill.ATTACKER_KILLER);
		KILLER_MAP_SUB.put(MonsterType.BALANCED, MonsterSkill.MoneyAwokenSkill.BALANCED_KILLER);
		KILLER_MAP_SUB.put(MonsterType.DEVIL, MonsterSkill.MoneyAwokenSkill.DEVIL_KILLER);
		KILLER_MAP_SUB.put(MonsterType.DRAGON, MonsterSkill.MoneyAwokenSkill.DRAGON_KILLER);
		KILLER_MAP_SUB.put(MonsterType.GOD, MonsterSkill.MoneyAwokenSkill.GOD_KILLER);
		KILLER_MAP_SUB.put(MonsterType.HEALER, MonsterSkill.MoneyAwokenSkill.HEALER_KILLER);
		KILLER_MAP_SUB.put(MonsterType.MACHINE, MonsterSkill.MoneyAwokenSkill.MACHINE_KILLER);
		KILLER_MAP_SUB.put(MonsterType.PHYSICAL, MonsterSkill.MoneyAwokenSkill.PHYSICAL_KILLER);
    }

	public static int getGuardBreakCount(MonsterInfo info, Enemy e) {
		return 0;
	}

	public static int getSubKillerCount(MonsterInfo info, Enemy e) {
		int counter = 0;
		for(MonsterType type : e.types) {
			int count = info.getTargetMoneyAwokenCount(KILLER_MAP_SUB.get(type));
			if(count>0) {
				counter += count;
			}
		}
		return counter;
	}
    
    public static int getKillerCount(MonsterInfo monsterInfo, Enemy e) {
    	int counter = 0;
    	for(MonsterType type : e.types) {
    		int count = monsterInfo.getTargetAwokenCount(KILLER_MAP.get(type), ComboMasterApplication.getsInstance().isCopMode());
    		if(count>0) {
    			counter += count;
    		}
    	}
		return counter;
	}
    
    public static boolean isKillerTarget(MonsterInfo monsterInfo, Enemy e) {
    	for(MonsterType type : e.types) {
    		int count = monsterInfo.getTargetAwokenCount(KILLER_MAP.get(type), ComboMasterApplication.getsInstance().isCopMode());
    		if(count>0) {
    			return true;
    		}
    	}
		return false;
	}
}
