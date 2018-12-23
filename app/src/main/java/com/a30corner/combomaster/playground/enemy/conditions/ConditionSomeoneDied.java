package com.a30corner.combomaster.playground.enemy.conditions;

import java.util.List;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionSomeoneDied extends EnemyCondition {

	public ConditionSomeoneDied() {
		super(Type.ALIVE_ONLY, 1);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		List<Enemy> list = env.getEnemies();
		for(Enemy emy : list) {
			if (emy.dead() || emy.diedThisTurn()) {
				return true;
			}
		}
		
		return false;
	}
}
