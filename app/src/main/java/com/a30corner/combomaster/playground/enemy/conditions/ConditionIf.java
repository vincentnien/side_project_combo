package com.a30corner.combomaster.playground.enemy.conditions;

import java.util.HashSet;
import java.util.Set;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.enemy.EnemyCondition;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.entity.Member;

public class ConditionIf extends EnemyCondition {

	public ConditionIf() {
		super(Type.IF, 1);
	}
	
	public ConditionIf(int... data) {
		super(Type.IF, data);
	}
	

	@Override
	public boolean checkCondition(IEnvironment env, Enemy enemy) {
		int count = data.get(0);
		boolean isOr = data.get(1) == 0;
		int offset = 2;
		boolean checkall = true;
		for(int i=0; i<count; ++i) {
			boolean check = false;
			boolean not = data.get(offset) == 1;
			int type = data.get(offset+1);
			++offset;
			if(type == Type.HP.ordinal()) {
				check = new ConditionHp(data.get(offset+1),data.get(offset+2)).checkCondition(env, enemy);
				offset += 3;
			} else if(type == Type.ALIVE_ONLY.ordinal()) {
				check = new ConditionAliveOnly().checkCondition(env, enemy);
				offset += 2;				
			} else if(type == Type.HAS_BUFF.ordinal()) {
				check = new ConditionHasBuff().checkCondition(env, enemy);
				offset += 2;								
			} else if(type == Type.FIND_ORB.ordinal()) {
				check = new ConditionFindOrb(new int[]{data.get(offset+1)}).checkCondition(env, enemy);
				offset += 2;
			} else if(type == Type.HAS_DEBUFF.ordinal()) {
				int debuf = data.get(offset+1);
				int val = data.get(offset+2);
				check = new ConditionHasDebuff(debuf, val).checkCondition(env, enemy);
				offset += 3;
			} else if(type == Type.FIND_TYPE.ordinal()) {
				int targetCount = data.get(offset+1);
				int[] arr = new int[targetCount+1];
				for(int index=0; index<targetCount+1; ++index) {
					arr[index] = data.get(offset+1+index);
				}
				
				check = new ConditionFindType(arr).checkCondition(env, enemy);
				offset += (targetCount+1);
			} else if(type == Type.FIND_ATTR.ordinal()) {
				int attr = data.get(offset+1);
				
				check = new ConditionFindAttr(attr).checkCondition(env, enemy);
				offset += 2;
			} else if(type == Type.LEADER_SWITCH.ordinal()) {
				check = !env.hasAlreadySwitched();
				++offset;
			} else if(type == Type.FIND_PET.ordinal()) {
				Set<Integer> set = new HashSet<Integer>(data.subList(1, data.size()));
				for(int index=0;index<6; ++index) {
					Member member = env.getMember(index);
					if(member != null) {
						if(set.contains(member.info.getNo())) {
							check = true;
							break;
						}
					}
				}
			} else if (type == Type.ENEMY_BUFF.ordinal()) {
				check = new ConditionEnemyBuff(data.get(offset+1)).checkCondition(env, enemy);
				offset += 2;
			}
			if(not) {
				check = !check;
			}
			
			if(isOr && check) {
				return true;
			} else if(!isOr) {
				checkall = checkall && check;
				if(!checkall) {
					return false;
				}
			}
		}
		return checkall;
	}

}
