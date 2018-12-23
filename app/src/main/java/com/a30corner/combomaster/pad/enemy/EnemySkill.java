package com.a30corner.combomaster.pad.enemy;

import java.util.ArrayList;
import java.util.List;

import com.a30corner.combomaster.pad.monster.Skill;

public class EnemySkill extends Skill {

	public enum TYPE {
		STRIKE, MULTI_HIT, DAMAGE_ABSORB, ORB_CONVERSION, PERCENT_DAMAGE, BIND, BIND_SKILL, HEAL_PLAYER, EFFECT_SHIELD, POISON_ORBS, BUFF_CLEAR, ATTACK_UP, RECOVER,
		REDUCED_MOVE_TIME, COLOR_HIDE, JAMMERS, CHARGE, NO_EFFECT, NO_ATTACK, DISABLE_AWOKEN
	}

	private TYPE type;
	private List<Integer> data;

	public EnemySkill() {
	}

	public EnemySkill(TYPE type, List<Integer> data) {
		this.type = type;
		this.data = data;
	}

	public void setSkillType(TYPE type) {
		this.type = type;
	}

	public void addData(int... data) {
		if (this.data == null) {
			this.data = new ArrayList<Integer>();
		}
		for (int i = 0; i < data.length; ++i) {
			this.data.add(data[i]);
		}
	}

	public void setData(List<Integer> data) {
		this.data = data;
	}

	public TYPE getSkillType() {
		return type;
	}

	public List<Integer> getData() {
		return data;
	}

	public static EnemySkill newSkill(TYPE type, int... data) {
		List<Integer> listData = new ArrayList<Integer>();
		for (int d : data) {
			listData.add(d);
		}
		return new EnemySkill(type, listData);
	}

	@Override
	public String toString() {
		if (data != null && data.size() > 0) {
			return type + ":" + data.get(0);
		} else {
			return type + ":";
		}
	}
}
