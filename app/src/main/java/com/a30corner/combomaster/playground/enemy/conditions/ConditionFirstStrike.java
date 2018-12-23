package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionFirstStrike extends EnemyCondition {

	public ConditionFirstStrike() {
		super(Type.TURN, 0);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		return true;
	}

}
