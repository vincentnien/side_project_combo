package com.a30corner.combomaster.playground.enemy.conditions;

import java.util.HashSet;
import java.util.Set;

import android.util.Pair;

import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill.MonsterType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.Team;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionFindType extends EnemyCondition {

	public ConditionFindType(int[] data) {
		super(Type.FIND_TYPE, data);
	}
	
	public ConditionFindType(Integer[] data) {
		super(Type.FIND_TYPE, data);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		Team team = env.getTeam();
		int count = data.get(0);
		Set<Integer> set = new HashSet<Integer>(data.subList(1, 1+count));
		for(int i=0; i<6; ++i) {
			MonsterInfo info = team.getMember(i);
			if ( info != null ) {
				Pair<MonsterType, MonsterType> types = info.getMonsterTypes();
				if (set.contains(types.first.value()) || set.contains(types.second.value())) {
					return true;
				}
				if (set.contains(info.getMonsterType3().value())) {
					return true;
				}
			}
		}
		return false;
	}

}
