package com.a30corner.combomaster.playground.enemy.conditions;

import java.util.HashSet;
import java.util.Set;

import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.Team;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;

public class ConditionFindPet extends EnemyCondition {

	public ConditionFindPet(int... no) {
		super(Type.FIND_PET, no);
	}

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		Team team = env.getTeam();
		
		Set<Integer> set = new HashSet<Integer>();
		for(Integer d : data) {
			set.add(d);
		}
		
		for(int i=0; i<6; ++i) {
			MonsterInfo info = team.getMember(i);
			if (info != null && set.contains(info.getNo())) {
				return true;
			}
		}
		return false;
	}

}
