package com.a30corner.combomaster.playground.action.impl;


import com.a30corner.combomaster.pad.monster.ActiveSkill;
import com.a30corner.combomaster.pad.monster.ActiveSkill.SkillType;
import com.a30corner.combomaster.playground.IEnvironment;
import com.a30corner.combomaster.playground.PadEnvironment.ICastCallback;
import com.a30corner.combomaster.playground.action.EnemyAction;
import com.a30corner.combomaster.playground.entity.Enemy;
import com.a30corner.combomaster.utils.Constants;

import rx.functions.Action0;

public class LockAwoken extends EnemyAction {

	public LockAwoken(String title, String description, int... val) {
		this(val);
		setDescription(title, description);
	}
	
    public LockAwoken(int... val) {
        super(Act.LOCK_AWOKEN, val);
    }

    @Override
    public void doAction(final IEnvironment env, final Enemy enemy, final ICastCallback callback) {
        super.doAction(env, enemy, callback);
        
        env.toastEnemyAction(this, enemy, new Action0() {
			
			@Override
			public void call() {
				ActiveSkill skill = new ActiveSkill(SkillType.ST_DISABLE_AWOKEN);
				skill.setData(data);
				env.fireGlobalSkill(Constants.SK_AWOKEN_LOCK, skill, callback);
			}
		});
        
    }

}
