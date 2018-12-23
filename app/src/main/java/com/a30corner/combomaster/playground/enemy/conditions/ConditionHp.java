package com.a30corner.combomaster.playground.enemy.conditions;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionHp extends EnemyCondition {

	public ConditionHp(int... data) {
		super(Type.HP, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		int check = data.get(0);
		float percent = enemy.getHpPercent();
		int hp = data.get(1);
		switch(check) {
		case 0: // equal
			// should always be 100
			return percent == hp;
		case 1: // more than
			return percent >= hp;
		case 2: // less than
			return percent < hp;
		}
		return false;
	}

}
