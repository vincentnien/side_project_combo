package com.a30corner.combomaster.playground.skill.impl;

import java.util.ArrayList;
import java.util.List;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;

public class BindRecover extends Skill {

	public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
			ICastCallback callback) {
		
		List<Integer> data = skill.getData();
		int recover = data.get(0);
		env.bindRecover(recover);
		
		int type = data.get(1);
		// FIXME: show sync with RecoverHp, but ... never mind.. :)
		List<Integer> recoverData = null;
		if(type == 1) {
			recoverData = new ArrayList<Integer>();
			recoverData.add(2);
			recoverData.add(data.get(2));
		} else if(type == 2) {
			recoverData = new ArrayList<Integer>();
			recoverData.add(0);
			recoverData.add(data.get(2));			
		} else if(type == 3) {
			recoverData = new ArrayList<Integer>();
			recoverData.add(1);
			recoverData.add(data.get(2));
		}
		if(recoverData != null) {
			ActiveSkill recoverSkill = new ActiveSkill(SkillType.ST_RECOVER_HP);
			recoverSkill.setData(recoverData);
			RecoverHp.onFire(env, owner, recoverSkill, callback);
		} else {
			callback.onCastFinish(true);
		}
	}

	@Override
	public SkillType getSkillType() {
		return SkillType.ST_BIND_RECOVER;
	}

}
