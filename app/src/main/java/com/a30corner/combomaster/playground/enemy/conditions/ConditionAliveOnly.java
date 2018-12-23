package com.a30corner.combomaster.playground.enemy.conditions;

import java.util.List;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionAliveOnly extends EnemyCondition {

	public ConditionAliveOnly() {
		super(Type.ALIVE_ONLY, 1);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		List<Enemy> list = env.getEnemies();
		int alive = list.size();
		for(Enemy emy : list) {
			if (emy.dead()) {
				--alive;
			}
		}
		
		return (alive==1 && !enemy.dead());
	}
}
