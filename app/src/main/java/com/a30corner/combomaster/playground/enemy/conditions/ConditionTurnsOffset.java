package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionTurnsOffset extends EnemyCondition {

	public ConditionTurnsOffset(int... data) {
		super(Type.TURN, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		return ((enemy.attackCount()-data.get(0))%data.get(1)) == 0;
	}
}
