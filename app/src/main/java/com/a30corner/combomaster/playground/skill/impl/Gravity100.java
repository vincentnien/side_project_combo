package com.a30corner.combomaster.playground.skill.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.a30corner.combomaster.engine.sprite.BasicHpSprite.AnimationCallback;
import com.a30corner.combomaster.pad.DamageCalculator.AttackValue;
import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.effect.Shake;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.playground.entity.Member;
import com.a30corner.combomaster.playground.skill.Skill;
import com.a30corner.combomaster.utils.Constants;

public class Gravity100 extends Skill {

    public static void onFire(IEnvironment env, Member owner, ActiveSkill skill, final ICastCallback callback) {
        double percent = skill.getData().get(0) / 100.0;
        List<Enemy> enemies = env.getEnemies();
        for(Enemy enemy : enemies) {
            if(!enemy.dead()){
                double damage = enemy.getHpTotal() * percent;
                Map<String, Boolean> voidSetting = new HashMap<String, Boolean>();
                voidSetting.put(Constants.SK_VOID, env.hasSkill(Constants.SK_VOID));
                voidSetting.put(Constants.SK_VOID_ATTR, env.hasSkill(Constants.SK_VOID_ATTR));
                voidSetting.put(Constants.SK_VOID_0, env.hasSkill(Constants.SK_VOID_0));
                enemy.dealtDamageDirect(AttackValue.create(damage, -1, AttackValue.ATTACK_SINGLE), 1.2f, 
                        new AnimationCallback() {
                            
                            @Override
                            public void animationDone() {
                                callback.onCastFinish(true);
                            }
                        }, voidSetting);
                Shake.apply(enemy.sprite, 1200f, 30, 9);//30, 10);
            }
        }
    }

    @Override
    public SkillType getSkillType() {
        return SkillType.ST_GRAVITY_100;
    }

}
