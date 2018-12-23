package com.a30corner.combomaster.playground.skill;

import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;

public abstract class Skill {

    //public abstract void onFire(PadEnvironment env, Member owner, ActiveSkill skill, ICastCallback callback);
    
    public abstract SkillType getSkillType();
}
