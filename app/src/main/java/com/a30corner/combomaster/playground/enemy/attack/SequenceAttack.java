package com.a30corner.combomaster.playground.enemy.attack;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.entity.Enemy;

public class SequenceAttack extends AttackAction {

	private int index = 0;
	
	@Override
	public void reset() {
		index = 0;
	}
	
	@Override
	public EnemyAttack nextAttack(IEnvironment env, Enemy enemy) {
		EnemyAttack find = null;
		int size = mAttackList.size();
		for(int i=0; i<size; ++i) {
			int newindex = (index + i)%size;
			EnemyAttack attack = mAttackList.get(newindex);
			if(attack.check(env, enemy)) {
				index = newindex;
				find = attack;
				break;
			}
		}
		
		if (++index >= size) {
			index = 0;
		}
		return find;
	}

	
	
}
