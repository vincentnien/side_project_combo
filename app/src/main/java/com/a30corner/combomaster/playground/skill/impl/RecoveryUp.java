package com.a30corner.combomaster.playground.skill.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;
import com.a30corner.combomaster.utils.Constants;

public class RecoveryUp extends Skill {

    public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
            ICastCallback callback) {
    	env.fireGlobalSkill(Constants.SK_RCV_UP, skill, callback);
    }

    @Override
    public SkillType getSkillType() {
        return SkillType.ST_RECOVER_UP;
    }

}
