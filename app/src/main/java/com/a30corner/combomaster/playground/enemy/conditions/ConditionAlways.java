package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionAlways extends EnemyCondition {

	public ConditionAlways() {
		super(Type.PERCENTAGE, 100);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		return true;
	}

}
