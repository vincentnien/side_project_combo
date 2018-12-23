package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.LogUtil;

public class ConditionModeSelection extends EnemyCondition {

	public ConditionModeSelection(int... data) {
		super(Type.MODE, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		return enemy.isMode(data.get(0), data.get(1));
	}
}
