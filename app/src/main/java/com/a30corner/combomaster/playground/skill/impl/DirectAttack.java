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

public class DirectAttack extends Skill {

    public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
            final ICastCallback callback) {
        List<Integer> data = skill.getData();
        
        AttackValue attack = AttackValue.create(data.get(1), -1, 
                data.get(0)==1? AttackValue.ATTACK_MULTIPLE:AttackValue.ATTACK_SINGLE);
        List<Enemy> list = AttackModule.findSuitableEnemies(owner.info, env.getEnemies(), attack,env.isMultiMode());
        AtomicInteger counter = new AtomicInteger(list.size());
        for(Enemy enemy : list) {
            env.attackToDirect(attack, enemy, owner, counter, callback);
        }
        
        
    }

    @Override
    public SkillType getSkillType() {
        return SkillType.ST_DIRECT_ATTACK;
    }

}
