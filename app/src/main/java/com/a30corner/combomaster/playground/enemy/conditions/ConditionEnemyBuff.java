package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.BuffOnEnemySkill;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.BuffOnEnemy;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionEnemyBuff extends EnemyCondition {

	public ConditionEnemyBuff() {
		super(Type.ENEMY_BUFF, 0);
	}

	public ConditionEnemyBuff(int... data) {
		super(Type.ENEMY_BUFF, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		BuffOnEnemySkill.Type[] types = BuffOnEnemySkill.Type.values();
		for (BuffOnEnemy buff : enemy.getBuffList()) {
			if (buff.isSkill(types[data.get(0)])) {
				return true;
			}
		}
		return false;
	}

}
