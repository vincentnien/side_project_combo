package com.a30corner.combomaster.pad.enemy.debuff;

import com.a30corner.combomaster.pad.monster.ActiveSkill;

public class DebuffToEnemy extends Debuff {

	private ActiveSkill mSkill;

	private DebuffToEnemy(Type type, int turn, int value, ActiveSkill skill) {
		super(type, turn, value);
		mSkill = skill;
	}

	public ActiveSkill getSkill() {
		return mSkill;
	}

	public static DebuffToEnemy poison(int value, ActiveSkill skill) {
		return new DebuffToEnemy(Type.POISON, INF, value, skill);
	}

	public static DebuffToEnemy reduceDef(int turn, int value, ActiveSkill skill) {
		return new DebuffToEnemy(Type.REDUCE_DEF, turn, value, skill);
	}

	public static DebuffToEnemy delay(int value, ActiveSkill skill) {
		return new DebuffToEnemy(Type.DELAY, INF, value, skill);
	}

}
