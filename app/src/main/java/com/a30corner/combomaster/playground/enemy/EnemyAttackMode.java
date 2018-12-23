package com.a30corner.combomaster.playground.enemy;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment;
import com.a30corner.combomaster.playground.action.impl.Attack;
import com.a30corner.combomaster.playground.enemy.attack.AttackAction;
import com.a30corner.combomaster.playground.enemy.attack.SequenceAttack;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionAlways;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionFirstStrike;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionModeSelection;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionPercentage;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.entity.Enemy;

public class EnemyAttackMode {

	//List<Pair<EnemyCondition, EnemyAttack>> enemyMode = new ArrayList<Pair<EnemyCondition, EnemyAttack>>();
	
	Map<EnemyCondition, AttackAction> enemyMode = new HashMap<EnemyCondition, AttackAction>();
	List<EnemyCondition> conditionList = new ArrayList<EnemyCondition>();

	int currentMode = 1;
	
	
	public EnemyAttackMode createEmptyFirstStrike() {
		EnemyCondition cond = new ConditionFirstStrike();
		AttackAction action = new SequenceAttack();
		addAttack(cond, action);
		return this;
	}
	
	public EnemyAttackMode createEmptyMustAction() {
		EnemyCondition cond = new ConditionAlways();
		AttackAction action = new SequenceAttack();
		addAttack(cond, action);
		return this;
	}

	// for simple monster, just attack
	public EnemyAttackMode createSimpleAttackAction(String title, String description, int attack) {		
		createEmptyFirstStrike();
		createEmptyMustAction();
		
		EnemyCondition cond = new ConditionPercentage(100);
		AttackAction action = new SequenceAttack();
		action.addAttack(new EnemyAttack().addPair(new ConditionAlways(), new Attack(title, description, attack)));
		addAttack(cond, action);
		return this;
	}
	
	/**
	 * must put in this sequences -> 0: first strike, 1: must action 
	 * @param condition
	 * @param action
	 */
	public void addAttack(EnemyCondition condition, AttackAction action) {
		conditionList.add(condition);
		enemyMode.put(condition, action);
	}
	
	public EnemyAttack firstStrike(IEnvironment env, Enemy enemy) {
		// 0 is the first strike
		EnemyCondition cond = conditionList.get(0);
		return enemyMode.get(cond).nextAttack(env, enemy);
	}
	
	public EnemyAttack nextMove(IEnvironment env, Enemy enemy) {
		// find must use - index = 1
		EnemyAttack must = findAttack(1, env, enemy);
		if (must != null) {
			return must;
		}

		// find next
		int size = conditionList.size();
		for(int i=2; i<size; ++i) {
			EnemyAttack attack = findAttack(i, env, enemy);
			if ( attack != null ) {
				EnemyCondition condition = conditionList.get(i);
				if(condition instanceof ConditionUsed) {
					enemyMode.get(condition).removeAll();
				}
				if (attack.isModeSelection()) {
					enemy.gotoMode(attack.nextMode());
				}
				return attack;
			}
		}
		return null;
	}
	
	private EnemyAttack findAttack(int index,IEnvironment env, Enemy enemy) {
		EnemyCondition cond = conditionList.get(index);
		if (cond.checkCondition(env, enemy)) {
			AttackAction action = enemyMode.get(cond);
			if(index == 1) {
				action.reset();
			}
			return action.nextAttack(env, enemy);
		}
		return null;
	}

}
