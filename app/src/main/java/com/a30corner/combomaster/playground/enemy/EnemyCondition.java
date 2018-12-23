package com.a30corner.combomaster.playground.enemy;

import java.util.ArrayList;
import java.util.List;

import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.entity.Enemy;

public abstract class EnemyCondition {

	public enum Type {
		HP(0),
		TURN(1),
		FIND_TYPE(2),
		FIND_ATTR(3),
		PERCENTAGE(4),
		FIND_ORB(5),
		ALIVE_ONLY(6),
		HAS_BUFF(7),
		HAS_DEBUFF(8),
		FIND_PET(9),
		USED(10),
		IF(11),
		USED_IF(12),
		LEADER_SWITCH(13),
		ENEMY_DEBUFF(14),
		ENEMY_BUFF(15),
		MODE(16);
		
		
		private final int id;
		Type(int id) {
			this.id = id;
		}
	}
	
	protected Type mType;
	protected List<Integer> data;

	public EnemyCondition(Type type, Integer[] data) {
		mType = type;
		this.data = new ArrayList<Integer>();
		for(int i=0; i<data.length; ++i) {
			this.data.add(data[i]);
		}
	}
	
	public EnemyCondition(Type type, int... data) {
		mType = type;
		this.data = new ArrayList<Integer>();
		for(int i=0; i<data.length; ++i) {
			this.data.add(data[i]);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(mType.ordinal()).append("/");
		int size = data.size();
		for(int i=0; i<size; ++i) {
			sb.append(data.get(i)).append(",");
		}
		return sb.toString();
	}

	public abstract boolean checkCondition(IEnvironment env, Enemy enemy);
}
