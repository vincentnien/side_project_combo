package com.a30corner.combomaster.pad.enemy.debuff;

import com.a30corner.combomaster.pad.enemy.EnemySkill;

public class DebuffToPlayer extends Debuff {

	private EnemySkill mSkill;

	private DebuffToPlayer(Type type, int turn, int value, EnemySkill skill) {
		super(type, turn, value);
		mSkill = skill;
	}

	public EnemySkill getSkill() {
		return mSkill;
	}

	public static DebuffToPlayer reduceTime(EnemySkill skill, int turn,
			int value) {
		return new DebuffToPlayer(Type.REDUCE_TIME, turn, value, skill);
	}
}
