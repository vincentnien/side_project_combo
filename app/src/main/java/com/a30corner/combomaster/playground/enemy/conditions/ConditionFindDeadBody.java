package com.a30corner.combomaster.playground.enemy.conditions;

import android.util.Log;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

import java.util.List;

public class ConditionFindDeadBody extends EnemyCondition {

	public ConditionFindDeadBody() {
		super(Type.ALIVE_ONLY, 1);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		List<Enemy> list = env.getEnemies();
		int total = list.size();
		int alive = total;
		for(Enemy emy : list) {
			if (emy.dead()) {
				--alive;
			}
		}
		return (alive!=total && !enemy.dead());
	}
}
