package com.a30corner.combomaster.playground.enemy;

import java.util.ArrayList;
import java.util.List;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionModeSelection;
import com.a30corner.combomaster.playground.enemy.conditions.ConditionUsed;
import com.a30corner.combomaster.playground.entity.Enemy;

public class EnemyAttack {

	private List<EnemyCondition> conditionList;
	public List<EnemyAction> actionList;
	
	private boolean used = false;
	
	public EnemyAttack() {
		conditionList = new ArrayList<EnemyCondition>();
		actionList = new ArrayList<EnemyAction>();
	}
	
	public EnemyAttack addCondition(EnemyCondition cond) {
		conditionList.add(cond);
		return this;
	}
	
	public EnemyAttack addAction(EnemyAction action) {
		actionList.add(action);
		return this;
	}
	
	public EnemyAttack addPair(EnemyCondition cond, EnemyAction action) {
		conditionList.add(cond);
		actionList.add(action);
		return this;
	}

	public boolean isModeSelection() {
		for(EnemyCondition cond : conditionList) {
			if (cond instanceof ConditionModeSelection) {
				return true;
			}
		}
		return false;
	}

	public int nextMode() {
		for(EnemyCondition cond : conditionList) {
			if (cond instanceof ConditionModeSelection) {
				return (cond.data.size()>=3) ?cond.data.get(2):-1;
			}
		}
		return -1;
	}
	
	public boolean check(IEnvironment env, Enemy enemy) {
		boolean hasUsedCondition = false;
		for(EnemyCondition cond : conditionList) {
			if (cond instanceof ConditionUsed) {
				hasUsedCondition = true;
			}
			if(!cond.checkCondition(env, enemy)) {
				return false;
			}
		}
		if (hasUsedCondition && used) {
			return false;
		}
		used = true;
		return true;
	}
}
