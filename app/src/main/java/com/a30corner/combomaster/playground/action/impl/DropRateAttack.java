package com.a30corner.combomaster.playground.action.impl;

import rx.functions.Action0;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;

public class DropRateAttack extends EnemyAction {

    public DropRateAttack(int... val) {
        super(Act.ADD_DROP_RATE, val);
    }

    public DropRateAttack(String title, String desc, int... val) {
        super(Act.ADD_DROP_RATE, val);
        setDescription(title, desc);
    }
    
    @Override
    public void doAction(final IEnvironment env, Enemy enemy,
    		final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
		        ActiveSkill skill = new ActiveSkill(SkillType.ST_DROP_RATE);
		        skill.setData(data);
		        env.fireSkill(null, skill, callback);
			}
        });

    }

}
