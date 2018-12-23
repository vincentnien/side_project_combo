package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionUsed extends EnemyCondition {

	public ConditionUsed() {
		super(Type.USED, 0);
	}
	
	public ConditionUsed(Type type, int... data) {
		super(type, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		return true;
	}

}
