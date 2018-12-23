package com.a30corner.combomaster.playground.skill.impl;

import android.util.Pair;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.pad.monster.MonsterInfo;
import com.a30corner.combomaster.pad.monster.MonsterSkill;
import com.a30corner.combomaster.pad.monster.TeamInfo;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;
import com.a30corner.combomaster.utils.Constants;
import com.a30corner.combomaster.utils.LogUtil;

import java.util.Arrays;
import java.util.List;

public class ReduceShieldByAwoken extends Skill {

	public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
			ICastCallback callback) {
		List<Integer> data = skill.getData();
		int size = data.size();
		int awokenSize = data.get(2);
		int total = 0;
		TeamInfo info = env.getTeam().team();
		for (int i = 0; i < 6; ++i) {
			MonsterInfo minfo = info.getMember(i);
			// FIXME: check bind or not
			if (minfo == null) {
				LogUtil.d("monster(", i, ") is null");
				continue;
			}
			for(int j=0; j<awokenSize; ++j) {
				total += minfo.getTargetAwokenCount(MonsterSkill.AwokenSkill.get(data.get(3+j)),env.isMultiMode());
			}
		}
		int totalReduced = (total * data.get(1)); // percent
		if(totalReduced>100) {
			totalReduced = 100;
		}
		ActiveSkill reduceShield = ActiveSkill.create(SkillType.ST_REDUCE_SHIELD);
		reduceShield.setData(Arrays.asList(data.get(0), totalReduced));
		reduceShield.setTurn(data.get(0), false);
		env.fireGlobalSkill(Constants.SK_SHIELD, reduceShield, callback);
	}

	@Override
	public SkillType getSkillType() {
		return SkillType.ST_DEF_UP_BY_AWOKEN;
	}

}
