package com.a30corner.combomaster.playground.skill.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;
import com.a30corner.combomaster.utils.Constants;

public class DropRate extends Skill {

    public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
            ICastCallback callback) {
    	boolean negative = skill.getData().get(1) >= 6;
    	String key = (negative)? Constants.SK_DROP_RATE_NEG:Constants.SK_DROP_RATE;
    	env.fireGlobalSkill(key, skill, callback);
    }

    @Override
    public SkillType getSkillType() {
        return SkillType.ST_DROP_RATE;
    }

}
