package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.RandomUtil;

public class ConditionPercentage extends EnemyCondition {

	public ConditionPercentage(int percent) {
		super(Type.PERCENTAGE, percent);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		return RandomUtil.getLuck(data.get(0));
	}

}
