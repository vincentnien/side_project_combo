package com.a30corner.combomaster.playground.skill.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;

public class LeaderSwitch extends Skill {

    public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
            ICastCallback callback) {
    	ActiveSkill ns = new ActiveSkill(SkillType.ST_LEADER_SWITCH);
    	for(int i=0; i<6; ++i) {
    		if(env.getMember(i) == owner) {
    			ns.addData(i);
    			break;
    		}
    	}
    	env.fireGlobalSkill("leader_switch", ns, callback);
    }

    @Override
    public SkillType getSkillType() {
        return SkillType.ST_LEADER_SWITCH;
    }

}
