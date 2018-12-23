package com.a30corner.combomaster.playground.skill.impl;

import java.util.List;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;

public class RecoverHp extends Skill {

    public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
            ICastCallback callback) {
    	List<Integer> data = skill.getData();
    	int type = data.get(0);
    	int recover = data.get(1);
    	if(type == 1) { // percentage of hp
    		float total = env.getTeam().getHp() * recover / 100f;
    		recover = (int)total;
    	} else if(type == 2) { // ?x of pets' rcv
    		recover = recover * owner.info.getRecovery();
    	}
    	env.recoverPlayerHp(recover);
    	
    	callback.onCastFinish(true);
    }

    @Override
    public SkillType getSkillType() {
        return SkillType.ST_RECOVER_HP;
    }

}
