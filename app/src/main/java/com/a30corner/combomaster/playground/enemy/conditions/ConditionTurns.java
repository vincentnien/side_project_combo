package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.LogUtil;

public class ConditionTurns extends EnemyCondition {

	public ConditionTurns(int... data) {
		super(Type.TURN, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		LogUtil.d("attackc=", enemy.attackCount(), " each ", data.get(0));
		return (enemy.attackCount()%data.get(0)) == 0;
	}
}
