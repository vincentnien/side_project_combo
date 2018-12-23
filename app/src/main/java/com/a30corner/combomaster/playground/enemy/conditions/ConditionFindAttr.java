package com.a30corner.combomaster.playground.enemy.conditions;

import android.util.Pair;

import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.Team;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionFindAttr extends EnemyCondition {

	public ConditionFindAttr(int data) {
		super(Type.FIND_ATTR, data);
	}
	
	public ConditionFindAttr(int[] data) {
		super(Type.FIND_ATTR, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		Team team = env.getTeam();
		int type = data.get(0);
		
		for(int i=0; i<6; ++i) {
			MonsterInfo info = team.getMember(i);
			if ( info != null ) {
				Pair<Integer, Integer> props = info.getProps();
				if(props.first == type || props.second == type) {
					return true;
				}
			}
		}
		return false;
	}

}
