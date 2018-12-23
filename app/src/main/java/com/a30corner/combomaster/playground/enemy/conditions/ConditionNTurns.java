package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionNTurns extends EnemyCondition {

	public ConditionNTurns(int... data) {
		super(Type.TURN, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		return (enemy.attackCount()%data.get(0)) == 1 && (enemy.attackCount() != data.get(1));
	}
}
