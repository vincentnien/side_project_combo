package com.a30corner.combomaster.playground.skill.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;

import java.util.ArrayList;
import java.util.List;

public class AwokenRecover extends Skill {

	public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
			ICastCallback callback) {
		
		List<Integer> data = skill.getData();
		int recover = data.get(0);
		env.awokenRecover(recover);

		callback.onCastFinish(true);
	}

	@Override
	public SkillType getSkillType() {
		return SkillType.ST_AWOKEN_RECOVER;
	}

}
