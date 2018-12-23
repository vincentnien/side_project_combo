package com.a30corner.combomaster.playground.skill.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.a30corner.combomaster.pad.DamageCalculator.AttackValue;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.module.AttackModule;
import com.a30corner.combomaster.playground.skill.Skill;

public class Attack extends Skill {

    public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
            ICastCallback callback) {
        List<Integer> data = skill.getData();

        AttackValue attack = AttackValue.create(data.get(1) * owner.info.getAtk(env.isMultiMode()), data.get(2),
                data.get(0)==1? AttackValue.ATTACK_MULTIPLE:AttackValue.ATTACK_SINGLE);
        List<Enemy> list = AttackModule.findSuitableEnemies(owner.info, env.getEnemies(), attack, env.isMultiMode());
        AtomicInteger counter = new AtomicInteger(list.size());
        
        if(data.size()>=4) {
	        for(Enemy enemy : list) {
	            env.attackVampire(owner.info, attack, enemy, owner, counter, callback, data.get(3));
	        }
        } else {
	        for(Enemy enemy : list) {
	            env.attackTo(owner.info, attack, enemy, owner, counter, callback);
	        }
        }
    }

    @Override
    public SkillType getSkillType() {
        return SkillType.ST_ATTACK;
    }

}
