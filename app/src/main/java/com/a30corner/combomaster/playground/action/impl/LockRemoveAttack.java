package com.a30corner.combomaster.playground.action.impl;

import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.Constants;

import rx.functions.Action0;

public class LockRemoveAttack extends EnemyAction {

    public LockRemoveAttack(int... val) {
        super(Act.LOCK_REMOVE, val);
    }

    public LockRemoveAttack(String title, String desc, int... val) {
        super(Act.LOCK_REMOVE, val);
        setDescription(title, desc);
    }
    
    @Override
    public void doAction(final IEnvironment env, Enemy enemy,
    		final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
		        ActiveSkill skill = new ActiveSkill(SkillType.ST_LOCK_REMOVE);
		        skill.setData(data);

                env.fireGlobalSkill(Constants.SK_LOCK_REMOVE, skill, callback);
			}
        });

    }

}
