package com.a30corner.combomaster.playground.enemy.attack;

import java.util.ArrayList;
import java.util.List;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyAttack;
import com.a30corner.combomaster.playground.entity.Enemy;

public abstract class AttackAction {

	protected List<EnemyAttack> mAttackList;
	
	public AttackAction() {
		mAttackList = new ArrayList<EnemyAttack>();
	}
	
	public void addAttack(EnemyAttack attack) {
		mAttackList.add(attack);
	}
	
	protected boolean canFire(int index,PadEnvironment env, Enemy enemy) {
		return mAttackList.get(index).check(env, enemy);
	}
	
	public void reset() {
	}
	
	public void removeAll() {
		mAttackList.clear();
	}
	
	public abstract EnemyAttack nextAttack(IEnvironment env, Enemy enemy);
}
