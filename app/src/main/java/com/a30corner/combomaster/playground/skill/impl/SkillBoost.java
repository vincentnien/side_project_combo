package com.a30corner.combomaster.playground.skill.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;
import com.a30corner.combomaster.utils.RandomUtil;

import java.util.ArrayList;
import java.util.List;

public class SkillBoost extends Skill {

	public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
			ICastCallback callback) {
		List<Integer> data = skill.getData();
		if (data.size() == 1) {
			int adjust = data.get(0);
			env.skillBoost(adjust, owner, callback);
		} else {
			List<Integer> adjust = new ArrayList<Integer>();
			for(int i=0; i<6; ++i) {
				adjust.add(RandomUtil.range(data.get(0), data.get(1)));
			}
			env.skillBoost(adjust, owner, callback);
		}
	}

	@Override
	public SkillType getSkillType() {
		return SkillType.ST_SKILL_BOOST;
	}

}
