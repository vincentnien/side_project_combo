package com.a30corner.combomaster.playground.skill.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;

public class DamageHp extends Skill {

    public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
            ICastCallback callback) {
        int become = skill.getData().get(0);
        int dealtHp;
        if(become == 1) {
            dealtHp = 1-env.getTeam().getCurrentHp();
        } else {
            dealtHp = -(int)Math.ceil(env.getTeam().getCurrentHp() / (100.0/(100-become)));
        }
    	env.recoverPlayerHp(dealtHp);
    	
    	callback.onCastFinish(true);
    }

    @Override
    public SkillType getSkillType() {
        return SkillType.ST_HP_1;
    }

}
