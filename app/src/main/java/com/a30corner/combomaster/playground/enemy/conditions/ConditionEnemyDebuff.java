package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.Constants;

public class ConditionEnemyDebuff extends EnemyCondition {

	public ConditionEnemyDebuff() {
		super(Type.ENEMY_DEBUFF, 0);
	}

	public ConditionEnemyDebuff(int... data) {
		super(Type.ENEMY_DEBUFF, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		return enemy.getDebuffList().size()>0;
	}

}
