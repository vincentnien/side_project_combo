package com.a30corner.combomaster.playground.skill.impl;

import com.a30corner.combomaster.pad.DamageCalculator.AttackValue;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.module.AttackModule;
import com.a30corner.combomaster.playground.skill.Skill;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TeamHpAttack extends Skill {

    public static void onFire(IEnvironment env, Member owner, ActiveSkill skill,
            ICastCallback callback) {
        List<Integer> data = skill.getData();

        float percent = env.getTeam().getCurrentHp();
        int x = data.get(1);
        AttackValue attack = AttackValue.create((percent * x), data.get(2),
                data.get(0)==1? AttackValue.ATTACK_MULTIPLE:AttackValue.ATTACK_SINGLE);
        List<Enemy> list = AttackModule.findSuitableEnemies(owner.info, env.getEnemies(), attack,env.isMultiMode());
        AtomicInteger counter = new AtomicInteger(list.size());

        for(Enemy enemy : list) {
            env.attackTo(owner.info, attack, enemy, owner, counter, callback);
        }
    }

    @Override
    public SkillType getSkillType() {
        return SkillType.ST_HP_ATTACK;
    }

}
