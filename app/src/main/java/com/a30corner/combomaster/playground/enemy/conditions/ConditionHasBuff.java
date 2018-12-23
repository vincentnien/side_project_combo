package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionHasBuff extends EnemyCondition {

	public ConditionHasBuff() {
		super(Type.HAS_BUFF, 0);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		return env.hasBuff();
	}

}
