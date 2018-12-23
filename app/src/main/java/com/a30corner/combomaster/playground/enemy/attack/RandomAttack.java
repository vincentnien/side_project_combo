package com.a30corner.combomaster.playground.enemy.attack;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.entity.Enemy;

public class RandomAttack extends AttackAction {
	
	@Override
	public EnemyAttack nextAttack(IEnvironment env, Enemy enemy) {
		int count = mAttackList.size();

		EnemyAttack find = null;
		List<Integer> shuffle = new ArrayList<Integer>();
		for(int i=0; i<count;++i) {
			shuffle.add(i);
		}
		Collections.shuffle(shuffle);
		
		for(int i=0; i<count; ++i) {
			EnemyAttack attack = mAttackList.get(shuffle.get(i));
			if(attack.check(env, enemy)) {
				find = attack;
				break;
			}
		}
		return find;
	}

	
	
}
