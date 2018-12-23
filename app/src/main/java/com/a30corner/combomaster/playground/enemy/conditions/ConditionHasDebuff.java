package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.Constants;

public class ConditionHasDebuff extends EnemyCondition {

	private static final String[] DEBUFF = {Constants.SK_TIME_CHANGE, Constants.SK_SKILL_LOCK, Constants.SK_DROP_RATE, Constants.SK_RCV_UP, Constants.SK_AWOKEN_LOCK};
	
	public ConditionHasDebuff() {
		super(Type.HAS_DEBUFF, 0);
	}
	
	public ConditionHasDebuff(int... data) {
		super(Type.HAS_DEBUFF, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		return env.hasDebuff(DEBUFF[data.get(0)], data.get(1));
	}

}
